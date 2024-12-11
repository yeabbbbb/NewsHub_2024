package com.recommender.newshub.resolver;

import com.recommender.newshub.exception.ex.PageException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageCheckArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PageCheck.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String page = webRequest.getParameter("page");

        if (page == null) {
            throw new PageException("Page value is required");
        }

        int pageInt = Integer.parseInt(page);
        if (pageInt < 1) {
            throw new PageException("Page number must be 1 or greater");
        }

        return pageInt - 1;
    }
}
