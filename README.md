👨‍🏫 EduHubSocial - College Classroom Social App
EduHubSocial is a full-stack mobile application designed to enhance communication and collaboration between students and teachers within a classroom or college environment. It mimics the features of a social platform like LinkedIn but focuses on educational use cases—allowing users to post, comment, like, share, and chat in real-time.
This project is built using Android (Kotlin) for the frontend and Spring Boot for the backend. It features real-time messaging via WebSockets, secure authentication using JWT, Firebase Cloud Messaging (FCM) for push notifications, and follows a clean MVVM architecture on the Android side.

🚀 Features
📲 Mobile App (Frontend - Android)
✅ User Authentication (Login/Signup) using JWT
✅ View all posts in a feed (like LinkedIn)
✅ Create, Like, Comment, and Share posts
✅ Real-time chat using WebSockets
✅ Push Notifications via Firebase Cloud Messaging (FCM)
✅ Clean MVVM Architecture
✅ State-safe Navigation using Navigation Graph
✅ Dependency Injection using Hilt
✅ Background operations with Kotlin Coroutines
✅ Token storage using SharedPreferences
✅ Responsive and intuitive UI



🖥️ Backend (Spring Boot)
✅ RESTful APIs built with Spring Boot
✅ Secure authentication and authorization with Spring Security + JWT
✅ MySQL database integration
✅ WebSocket integration for real-time messaging
✅ Firebase Admin SDK integration for push notifications
✅ API documentation with Swagger
✅ Role-based access control (User / Teacher)



🔐 Authentication Flow
User logs in or signs up.
Credentials are authenticated, and a JWT Token is issued.
Token is stored locally using SharedPreferences.
All secure API requests include the token in the Authorization header.
Backend validates and authorizes API access using Spring Security.



🧑‍🏫 User Roles & Functionalities
👨‍🎓 Student
Can view and interact with posts
Can like, comment, and share study-related content
Can send messages to teachers or classmates
Can receive real-time chat and notification updates



👩‍🏫 Teacher
Can create academic posts (e.g., assignments, tips, study materials)
Can interact with students via chat or posts
Can moderate comments and interactions



💬 Real-time Chat (WebSocket)
WebSocket endpoints for chat
One-on-one conversations between users
Real-time message delivery and updates
Optional features: typing indicators, read receipts

🔔 Notifications (FCM)
Push notifications powered by Firebase Cloud Messaging (FCM)
Users receive notifications on new messages or important posts
Notifications sent via Firebase Admin SDK from the backend



📚 Tech Stack
Layer	Tech Stack
Frontend	Android (Kotlin), MVVM, Hilt, Coroutines, Retrofit, Navigation Graph, WebSocket, FCM
Backend	Spring Boot, Spring Security, JWT, WebSocket, MySQL, Swagger, Firebase Admin SDK
Database	MySQL
Auth	JWT (JSON Web Token)
Realtime	WebSockets (SockJS + STOMP)
Push	Firebase Cloud Messaging (FCM)



🧭 App Flow
User Registration/Login
→ Token received and stored locally
Main Feed Loaded
→ Posts displayed in reverse chronological order
Interaction
→ Users can like, comment on, and share posts
Post Creation
→ Students and teachers can share academic updates
Chat
→ One-on-one real-time conversations using WebSocket
Notifications
→ Real-time push notifications for messages or mentions


📦 Future Enhancements

🛠️ Admin Setup (Backend API Only)
✅ Create and manage user roles
✅ Monitor flagged posts (for moderation if required)
✅ Track WebSocket message traffic and logs
📁 File sharing in posts and chat
🧑‍🎓 Classroom Groups / Subject-wise feeds
🎓 Achievements & Badges system
🔍 Search and Filter by tag or user
📊 Analytics Dashboard for engagement metrics
