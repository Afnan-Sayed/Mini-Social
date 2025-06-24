# 🧑‍🤝‍🧑 MiniSocial

## 📌 Project Overview

**MiniSocial** is a lightweight social networking application designed to enhance user interactions in a collaborative environment.  
It allows users to:
- Register and manage profiles
- Connect with friends
- Create and engage with posts
- Participate in groups
- Receive real-time notifications

Built with a modular **Java EE** architecture, MiniSocial ensures:
- Clean separation of concerns
- Robust functionality
- Secure access control

It leverages **RESTful APIs**, **role-based security**, and **asynchronous messaging** for a seamless user experience.

---

## 👩‍💻 Team Members
- **Malak Sherif** — User Management and Notifications Management
- **Jana Ahmed** — Connection Management 
- **Afnan Sayed** — Post Management  
- **Afnan Abdelkareem** — Group Management  
 
---

## ✨ Features

### 1. 👤 User Management
- **User Registration:** Email, password, name, bio, role (user/admin)
- **Login:** Secure authentication
- **Profile Management:** Update name, bio, email, password
- **Role-Based Access:** Admin-only privileges using JWT

### 2. 🤝 Connection Management
- **Friend Requests:** Send, accept, or reject
- **View Connections:** Manage friend list
- **Friend Suggestions:** Based on mutual connections
- **User Search:** By name or email

### 3. 📝 Post Management
- **Create Posts:** With optional media attachments (image URLs)
- **Edit/Delete Posts**
- **Feed Retrieval:** View posts from user and friends
- **Engagement:** Like and comment on posts

### 4. 👥 Group Management
- **Create Groups:** Open or closed with name, description
- **Join/Leave Groups:** Admin approval for closed groups
- **Group Posts:** Visible only to group members
- **Roles & Permissions:** Promote admins, remove users/posts, delete group

### 5. 🔔 Notifications
- **Real-Time via JMS:** Friend requests, likes, comments, group activities
- **Activity Log:** Displays recent user actions

### 6. 🔒 Authentication & Security
- **Role-Based Access Control:** Using JWT
- **Access Restrictions:** 403 on unauthorized requests
- **Secure Login:** Credential validation

---

## 🧩 Bonus Features

- Media attachments (image URLs)
- Friend suggestions based on connections
- Activity log with JMS
- Search users by name/email

---

## 🛠️ Tech Stack

| Technology         | Description                             |
|--------------------|-----------------------------------------|
| Java EE            | Core enterprise functionality           |
| JBoss EAP 8        | Application server                      |
| EJB                | Stateless beans (UserService, etc.)     |
| JPA                | ORM for entities (User, Post, etc.)     |
| JTA                | Transaction management                  |
| JAX-RS             | RESTful APIs                            |
| JWT                | Role-based security                     |
| JMS                | Async messaging (notifications)         |
| MySQL              | Relational database                     |
| Maven              | Build and dependency management         |

---

## 📘 API Documentation

APIs are documented in the provided Postman collection: `Mini-Social.postman_collection.json`.

### 🔹 User Management
- `POST /api/user/register` — Register a new user  
- `POST /api/user/login` — Authenticate user  
- `PUT /api/{user}/update` — Update user profile  

### 🔹 Connection Management
- `POST /api/connections/sendFriendRequest` — Send a friend request  
- `GET /api/connections/friends` — View all friends  
- `GET /api/connections/suggestFriends` — Get friend suggestions  
- `GET /api/connections/search` — Search users by name or email  

### 🔹 Post Management
- `POST /api/post/createPost` — Create a new post  
- `GET /api/feed/getFeed` — Retrieve user feed  
- `POST /api/engagement/likePost` — Like a post  
- `POST /api/engagement/commentOnPost` — Comment on a post  

### 🔹 Group Management
- `POST /api/groups/create` — Create a new group  
- `POST /api/groups/{groupId}/join` — Join a group  
- `PUT /api/groups/requests/{requestId}/handle` — Approve/reject group join requests  
- `POST /api/post/createPost` — Share a post in a group  

### 🔹 Notifications
- `GET /api/notifications/activityLog/{userId}` — View recent user actions  
- 📩 **JMS Queues** used for asynchronous event handling

> 📂 For detailed request bodies and examples, import the Postman collection into Postman.

---

## 🧪 Installation & Setup

### ✅ Prerequisites
- Java JDK 11 or higher  
- Maven 3.6+  
- MySQL 8.0+  
- JBoss EAP 8  
- Git

---

### 📦 Steps

#### 1. Clone the Repository

```
git clone https://github.com/Afnan-Sayed/Mini-Social.git
cd Mini-Social
```
#### 2. Create MySQL Database
```
CREATE DATABASE MiniSocial;
```

#### 3. Configure JBoss EAP 8
##### Download & extract JBoss EAP 8

Navigate to: `<JBOSS_HOME>/standalone/configuration`

##### Edit standalone-full.xml:
🔸 Datasource config (inside <datasources>)
```
<datasource jndi-name="java:jboss/datasources/MyDS" pool-name="MyDS" enabled="true" use-ccm="true">
    <connection-url>jdbc:mysql://localhost:3306/MiniSocial</connection-url>
    <driver>mysql</driver>
    <security>
        <user-name>yourMySQLUsername</user-name>
        <password>yourMySQLPassword</password>
    </security>
</datasource>
```
🔸 MySQL Driver (inside <drivers>)
```
<driver name="mysql" module="com.mysql">
    <xa-datasource-class>com.mysql.cj.jdbc.MysqlXADataSource</xa-datasource-class>
</driver>
```
🔸 JMS Queue (inside messaging-activemq)
```
<jms-queue name="NotificationQueue" entries="queue/NotificationQueue jms/queue/NotificationQueue java:jboss/exported/jms/queue/NotificationQueue"/>
📁 Copy MySQL JDBC driver (mysql-connector-java-<version>.jar) to:
<JBOSS_HOME>/standalone/deployments
```
#### 4. Build the Project
```
mvn clean install
```
#### 5. Deploy to JBoss
##### Copy WAR to deployments
`cp target/Mini-Social-1.0-SNAPSHOT.war <JBOSS_HOME>/standalone/deployments`

##### Run server
`<JBOSS_HOME>/bin/standalone.sh -c standalone-full.xml`

#### 6. Access the App
`http://localhost:8080/Mini-Social-1.0-SNAPSHOT`

#### 7. Test the APIs
Import Mini-Social.postman_collection.json into Postman
Use the provided request bodies and parameters
