# 👨‍🏫 **EduHubSocial - College Classroom Social App**

**EduHubSocial** is a full-stack mobile application designed to enhance communication and collaboration between **students** and **teachers** within a classroom or college environment.  
It mimics features of a social platform like **LinkedIn**, but focuses on **educational use cases** — allowing users to **post**, **comment**, **like**, **share**, and **chat in real-time**.

This project is built using **Android (Kotlin)** for the frontend and **Spring Boot** for the backend.  
It features **real-time messaging via WebSockets**, **secure JWT-based authentication**, **Firebase Cloud Messaging (FCM)** for push notifications, and follows a clean **MVVM architecture**.

---

## 🚀 **Features**

### 📲 **Mobile App (Frontend - Android)**

- ✅ **User Authentication** (Login/Signup) using JWT  
- ✅ **Feed view** with all posts (like LinkedIn)  
- ✅ **Create, Like, Comment, and Share** posts  
- ✅ **Real-time chat** using WebSockets  
- ✅ **Push Notifications** via FCM  
- ✅ **Clean MVVM Architecture**  
- ✅ **State-safe Navigation** using Navigation Graph  
- ✅ **Dependency Injection** using Hilt  
- ✅ **Background operations** using Kotlin Coroutines  
- ✅ **Token storage** with SharedPreferences  
- ✅ **Responsive and intuitive UI**

---

### 🖥️ **Backend (Spring Boot)**

- ✅ **RESTful APIs** built using Spring Boot  
- ✅ **JWT + Spring Security** for secure authentication  
- ✅ **MySQL** for database storage  
- ✅ **WebSocket integration** for real-time chat  
- ✅ **Firebase Admin SDK** for push notifications  
- ✅ **API Documentation** using Swagger  
- ✅ **Role-based access control** (Student / Teacher)

---

## 🔐 **Authentication Flow**

1. User logs in or signs up  
2. Backend verifies credentials and issues a **JWT token**  
3. Token is stored locally using **SharedPreferences**  
4. All secure API requests include the token in the **Authorization** header  
5. Backend verifies the token using **Spring Security**

---

## 🧑‍🏫 **User Roles & Functionalities**

### 👨‍🎓 **Student**

- View and interact with posts  
- Like, comment, and share study-related content  
- Send messages to teachers or classmates  
- Receive real-time chat and notification updates

### 👩‍🏫 **Teacher**

- Create academic posts (e.g., assignments, tips, materials)  
- Interact with students via posts and chat  
- Moderate comments and interactions

---

## 💬 **Real-time Chat (WebSocket)**

- WebSocket endpoints for chat  
- One-on-one conversations  
- Real-time message delivery  
- *(Optional: Typing indicators, read receipts)*

---

## 🔔 **Notifications (FCM)**

- FCM integrated for push notifications  
- Receive alerts on new messages or important posts  
- Backend sends notifications via **Firebase Admin SDK**

---

## 📚 **Tech Stack**

| Layer       | Tech Stack                                                                 |
|-------------|------------------------------------------------------------------------------|
| **Frontend** | Android (Kotlin), MVVM, Hilt, Coroutines, Retrofit, Navigation Graph, WebSocket, FCM |
| **Backend**  | Spring Boot, Spring Security, JWT, WebSocket, MySQL, Swagger, Firebase Admin SDK |
| **Database** | MySQL                                                                       |
| **Auth**     | JWT (JSON Web Token)                                                        |
| **Realtime** | WebSockets (SockJS + STOMP)                                                 |
| **Push**     | Firebase Cloud Messaging (FCM)                                              |

---

## 🧭 **App Flow**

1. **User Registration/Login**  
2. **Token received and stored locally**  
3. **Posts feed displayed** sorted by newest first  
4. **Users interact** (like/comment/share)  
5. **Users post academic content**  
6. **Real-time chat** between users  
7. **Push notifications** for messages and mentions

---

## 🛠️ **Admin Setup (Backend API Only)**

- ✅ Manage user roles  
- ✅ Moderate flagged or inappropriate posts  
- ✅ Monitor WebSocket connections and logs

---

## 📦 **Future Enhancements**

- 📁 **File sharing** in posts and chat  
- 🧑‍🏫 **Classroom groups** and subject-based feeds  
- 🎓 **Achievement badges** for milestones  
- 🔍 **Search and filtering** by tags or usernames  
- 📊 **User engagement analytics**

---

