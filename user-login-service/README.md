# User Login Service

一个基于Spring Boot 3的用户登录服务，提供用户注册、登录和管理功能。

## 技术栈

- **Spring Boot**: 3.2.2
- **Java**: 17
- **Maven**: 项目管理和构建工具
- **MySQL**: 数据库
- **Spring Web**: REST API
- **Spring Data JPA**: 数据持久化
- **Lombok**: 简化Java代码

## 项目结构

```
user-login-service/
├── src/
│   ├── main/
│   │   ├── java/com/example/userlogin/
│   │   │   ├── UserLoginApplication.java      # 主应用类
│   │   │   ├── controller/
│   │   │   │   └── UserController.java        # REST控制器
│   │   │   ├── service/
│   │   │   │   └── UserService.java           # 业务逻辑层
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java        # 数据访问层
│   │   │   └── entity/
│   │   │       └── User.java                  # 用户实体类
│   │   └── resources/
│   │       └── application.properties         # 应用配置
│   └── test/
└── pom.xml                                     # Maven配置
```

## 快速开始

### 前置条件

1. 安装 Java 17 或更高版本
2. 安装 Maven 3.6+
3. 安装 MySQL 8.0+

### 数据库设置

1. 启动MySQL服务
2. 创建数据库（应用会自动创建）或手动创建：

```sql
CREATE DATABASE user_login_db;
```

3. 修改 `src/main/resources/application.properties` 中的数据库配置：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/user_login_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=你的用户名
spring.datasource.password=你的密码
```

### 运行应用

在 `user-login-service` 目录下执行：

```bash
# 编译项目
mvn clean install

# 运行应用
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动

## API 端点

### 1. 用户注册

```
POST /api/users/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456",
  "email": "test@example.com"
}
```

### 2. 用户登录

```
POST /api/users/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456"
}
```

### 3. 获取所有用户

```
GET /api/users
```

### 4. 根据ID获取用户

```
GET /api/users/{id}
```

### 5. 更新用户信息

```
PUT /api/users/{id}
Content-Type: application/json

{
  "email": "newemail@example.com"
}
```

### 6. 删除用户

```
DELETE /api/users/{id}
```

## 测试API

可以使用 curl、Postman 或其他 REST 客户端测试API。

### 示例：使用 curl 注册用户

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456",
    "email": "test@example.com"
  }'
```

### 示例：使用 curl 登录

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456"
  }'
```

## 配置说明

### application.properties 主要配置项

- `server.port`: 应用端口（默认8080）
- `spring.datasource.*`: 数据库连接配置
- `spring.jpa.hibernate.ddl-auto`: 数据库表自动创建策略（update表示自动更新表结构）

## 数据模型

### User 实体

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键，自增 |
| username | String | 用户名，唯一，3-50字符 |
| password | String | 密码，至少6位 |
| email | String | 邮箱，唯一 |
| createdAt | LocalDateTime | 创建时间 |
| updatedAt | LocalDateTime | 更新时间 |

## 注意事项

⚠️ **安全提醒**: 
- 当前版本密码是明文存储，生产环境中应使用 BCrypt 等加密方式
- 建议添加 JWT 或 Session 进行身份验证
- 建议添加输入验证和异常处理

## 后续改进建议

- [ ] 密码加密（BCrypt）
- [ ] JWT 身份验证
- [ ] 添加角色和权限管理
- [ ] 添加单元测试和集成测试
- [ ] 添加 API 文档（Swagger/OpenAPI）
- [ ] 添加日志记录
- [ ] 添加异常处理器

## 许可证

MIT License
