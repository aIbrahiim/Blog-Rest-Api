# Spring Boot, Spring Security, JWT, JPA, Rest API, MySQL

Restful CRUD API for a blog using Spring Boot

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/woodyinho/Blog-Rest-Api.git
```

**2. Create Mysql database**

create database db_app


**3. Change mysql username and password as per your installation**

+ open `src/main/resources/application.properties`
+ change `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

**4. Run the app using maven**

```bash
mvn spring-boot:run
```
The app will start running at <http://localhost:8888>

## Explore Rest APIs

The app defines following CRUD APIs.

### Auth

| Method | Url | Decription | Sample Valid Request Body | 
| ------ | --- | ---------- | --------------------------- |
| POST   | /app-ws/auth/signup | Sign up | [JSON](#signup) |
| POST   | /app-ws/auth/signin | Log in | [JSON](#signin) |

### Users

| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /app-ws/users/profile| Get logged in user profile | |
| PUT    | /app-ws/users/profile | Update user (If profile belongs to logged in user) | [JSON](#userupdate) |
| DELETE | /app-ws/users/profile | Delete user (For logged in user ) | |


### Posts

| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /app-ws/posts | Get all posts | |
| GET    | /app-ws/posts/{id} | Get post by id | |
| POST   | /app-ws/posts | Create new post (By logged in user) | [JSON](#postcreate) |
| PUT    | /app-ws/posts/{id} | Update post (If post belongs to logged in user) | [JSON](#postupdate) |
| DELETE | /app-ws/posts/{id} | Delete post (If post belongs to logged in user ) | |

### Comments

| Method | Url | Description | Sample Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /app-ws/posts/{postId}/comments | Get all comments which belongs to post with id = postId | |
| POST   | /app-ws/posts/{postId}/comments | Create new comment for post with id = postId (By logged in user) | [JSON](#commentcreate) |


## Sample Valid JSON Request Bodys

##### <a id="signup">Sign Up -> /api/auth/signup</a>
```json
{
	"firstName": "Jordan",
	"lastName": "Henderson",
	"password": "password",
	"email": "jhendo14@gmail.com"
}
```

##### <a id="signin">Sign In -> /app-ws/auth/signin</a>
```json
{
	"Email": "jhendo14@gmail.com",
	"password": "password"
}
```


##### <a id="postcreate">Create Post -> /app-ws/posts</a>
```json
{
	"title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
	"body": "quia et suscipit suscipit recusandae consequuntur expedita et cum reprehenderit molestiae ut ut quas totam nostrum rerum est autem sunt rem eveniet architecto"
}
```

##### <a id="postupdate">Update Post -> /app-ws/posts/{id}</a>
```json
{
	"title": "UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED",
	"body": "UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED UPDATED "
}
```

##### <a id="commentcreate">Create Comment -> /app-ws/posts/{postId}/comments</a>
```json
{
	"body": "laudantium enim quasi est quidem magnam voluptate ipsam eos tempora quo necessitatibus dolor quam autem quasi reiciendis et nam sapiente accusantium"
}
```
