package com.recommender.newshub.aop;

import com.recommender.newshub.domain.User;
import com.recommender.newshub.domain.enums.UserRole;
import com.recommender.newshub.exception.ex.ForbiddenException;
import com.recommender.newshub.exception.ex.UnauthenticatedException;
import com.recommender.newshub.constants.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdminValidationAspect {
    @Before("within(com.recommender.newshub.web.controller.AdminController) && args(.., request)")
    public void validateAdminAccount(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new UnauthenticatedException("Session not found. Please login.");
        }

        User user = (User) session.getAttribute(SessionConst.USER);
        if (user == null) {
            throw new UnauthenticatedException("User not authenticated");
        }

        if (!UserRole.ROLE_ADMIN.equals(user.getRole())) {
            throw new ForbiddenException("Access denied");
        }
    }
}
