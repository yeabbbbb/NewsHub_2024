# 📰 News Hub - Personalized News Recommendation System
> A personalized news recommendation web service that reflects both **short-term and long-term user interests**

---

## 📌 Overview
News Hub is a personalized news recommendation system designed to address the **time-sensitive nature of news consumption**.

Unlike traditional recommendation systems that rely on a single user representation, this project leverages the **LSTUR (Long- and Short-term User Representation)** model to capture:
- Short-term interests (recent user behavior)
- Long-term preferences (historical patterns)

This enables more accurate and context-aware news recommendations.

---

## 🏆 Key Achievements
- 🥇 Selected as a **demo candidate among 73 teams** and received the **highest grade (A+)**
- ✅ Successfully deployed a full-stack AI-powered web service on AWS EC2
- 🔐 Resolved cross-domain authentication issue by implementing HTTPS and secure cookie configuration

---

## 🎯 Key Features
- 🔍 Personalized news recommendation (LSTUR-based)
- 🧠 User interest modeling from click history
- 📰 Latest and trending news delivery
- 📂 Category filtering and search
- 🌐 Responsive web UI

---

## 🏗️ System Architecture
```
[Frontend] (HTML/CSS/JS)
        ↓
[Nginx Reverse Proxy]
        ↓
[Backend API Server] (Spring Boot)
        ↓
[AI Recommendation Server] (Flask + TensorFlow)
        ↓
[Database] (MySQL - AWS RDS)
```

### Highlights
- Fully separated architecture (Frontend / Backend / AI server)
- Nginx reverse proxy for traffic handling
- HTTPS + secure cookie configuration
- Scalable deployment structure on AWS EC2

---

## 🧠 Recommendation Model

### LSTUR (Long- and Short-term User Representation)
- **Short-term interest** → modeled using GRU on recent interactions
- **Long-term interest** → captured via user embedding

These representations are combined to predict click probability.

### Datasets
- Microsoft MIND dataset
- Adressa dataset

### Evaluation Metrics
- AUC
- MRR
- nDCG@5 / nDCG@10

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|------------|
| Frontend | HTML, CSS, JavaScript |
| Backend | Java 17, Spring Boot |
| AI Server | Python 3.9, TensorFlow 2.7, Flask |
| Database | MySQL (AWS RDS) |
| Infrastructure | AWS EC2, Nginx |
| Protocol | REST API, HTTPS |

---

## 🚀 Getting Started

### 1. Run Recommender Server
```bash
cd news-hub-recommender
nohup python3 app.py &
```

### 2. Run Backend Server

**(1) Create configuration file**

`news-hub-backend/src/main/resources/application-SECRET-KEY.yml`
```yaml
news-api-key: <api-key>
mysql-url: <mysql-url>
mysql-username: <mysql-username>
mysql-password: <mysql-password>
```

**(2) Database setting**
- First run → `create`
- After that → `update`

**(3) Run server**
```bash
cd news-hub-backend
nohup ./gradlew bootRun &
```

Access Swagger:
```
http://<ec2_public_ip>:8080/swagger
```

### 3. Run Frontend

Modify Nginx configuration:
- Set `root` to directory containing `home.html`
- Set `index` to `home.html`
```bash
sudo systemctl restart nginx
```

Access:
```
http://<ec2_public_ip>:8080
```

---

## ⚙️ Challenges & Solutions

### 🔐 Cross-Domain Cookie Authentication Issue

**Problem**

Login cookie was blocked due to cross-domain restrictions between frontend and backend servers, preventing user authentication across services.

**Root Cause**

Browsers block cookies in cross-domain requests by default unless the server explicitly allows it via `SameSite` and `Secure` attributes — and these attributes require HTTPS to function correctly.

**Solution**
- Obtained SSL certificate and migrated the service to HTTPS
- Added `credentials: 'include'` to client-side fetch requests
- Set `SameSite=None; Secure` on server-side cookie configuration
```javascript
// Client-side
fetch(url, {
  credentials: 'include'
})
```
```
# Server-side cookie config
SameSite=None
Secure=true
```

---

### 🚀 Server Load & Stability Issue

**Problem**

Performance degradation due to single-server architecture handling frontend, backend, and AI inference simultaneously.

**Solution**
- Separated Frontend / Backend / AI servers across multiple AWS EC2 instances
- Distributed system architecture for improved scalability and fault isolation

---

## 📅 Project Info

- **Duration:** Mar 2024 – Oct 2024 (7 months)
- **Team Size:** 2 members

### My Role
- Frontend development (UI components, responsive design)
- Cloud deployment and infrastructure setup (AWS EC2, Nginx)
- Resolved cross-domain authentication issue, enabling seamless user login
- Evaluated recommendation model performance using AUC, MRR, and nDCG metrics

---

## 💡 Future Work
- Mobile application development
- Multimodal recommendation (image/video)
- User feedback-based recommendation improvement
