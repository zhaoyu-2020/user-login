# 如何在项目中使用Lombok

本文档说明如何正确配置和使用Lombok。

## 为什么需要配置Lombok？

Lombok通过注解在**编译时**自动生成getter、setter、构造函数等样板代码。要让它正常工作，需要：
1. **Maven配置**：让编译器能够处理Lombok注解
2. **IDE配置**：让IDE能够识别Lombok生成的代码

---

## 1. Maven配置（已完成）

### 1.1 添加Lombok依赖（pom.xml）

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

### 1.2 配置Maven编译器插件

在 `pom.xml` 的 `<build>` 部分添加：

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>17</source>
        <target>17</target>
        <annotationProcessorPaths>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.30</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

✅ **这个配置已经添加到pom.xml中了！**

---

## 2. IDE配置

### IntelliJ IDEA

#### 方法1：通过插件（推荐）

1. 打开 **Preferences/Settings** (Mac: `⌘,` / Windows: `Ctrl+Alt+S`)
2. 导航到 **Plugins**
3. 搜索 **"Lombok"**
4. 安装 **Lombok Plugin**
5. 重启 IntelliJ IDEA

#### 方法2：启用注解处理

1. 打开 **Preferences/Settings**
2. 导航到 **Build, Execution, Deployment → Compiler → Annotation Processors**
3. 勾选 **Enable annotation processing**
4. 点击 **Apply** 和 **OK**

### VS Code

1. 安装扩展：**"Lombok Annotations Support for VS Code"**
2. 安装 Java 扩展包：**"Extension Pack for Java"**

### Eclipse

1. 下载 Lombok jar: https://projectlombok.org/download
2. 运行 jar 文件：`java -jar lombok.jar`
3. 安装程序会自动找到Eclipse，按提示安装
4. 重启Eclipse

---

## 3. 使用Lombok示例

### 原始代码（使用Lombok）

```java
package com.example.userlogin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data                    // 生成getter/setter/toString/equals/hashCode
@NoArgsConstructor       // 生成无参构造函数
@AllArgsConstructor      // 生成全参构造函数
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String password;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### 编译后等价代码（Lombok自动生成）

Lombok会在编译时自动生成：
- `getId()`, `setId(Long id)`
- `getUsername()`, `setUsername(String username)`
- `getPassword()`, `setPassword(String password)`
- `getEmail()`, `setEmail(String email)`
- `getCreatedAt()`, `setCreatedAt(LocalDateTime createdAt)`
- `getUpdatedAt()`, `setUpdatedAt(LocalDateTime updatedAt)`
- `toString()`, `equals()`, `hashCode()`
- 无参构造函数 `User()`
- 全参构造函数 `User(Long id, String username, ...)`

---

## 4. 常用Lombok注解

| 注解 | 功能 |
|------|------|
| `@Data` | 生成getter、setter、toString、equals、hashCode方法 |
| `@Getter` | 只生成getter方法 |
| `@Setter` | 只生成setter方法 |
| `@NoArgsConstructor` | 生成无参构造函数 |
| `@AllArgsConstructor` | 生成全参构造函数 |
| `@RequiredArgsConstructor` | 为final字段生成构造函数 |
| `@ToString` | 生成toString方法 |
| `@EqualsAndHashCode` | 生成equals和hashCode方法 |
| `@Builder` | 生成建造者模式代码 |
| `@Slf4j` | 生成日志对象 |

---

## 5. 验证Lombok配置

### 步骤1：更新User.java使用Lombok

恢复到使用Lombok注解的版本：

```java
package com.example.userlogin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50之间")
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度至少为6位")
    @Column(nullable = false)
    private String password;

    @Email(message = "邮箱格式不正确")
    @Column(unique = true, length = 100)
    private String email;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

### 步骤2：编译测试

```bash
cd user-login-service
mvn clean compile
```

如果配置正确，应该能够成功编译！

### 步骤3：打包测试

```bash
mvn clean package
```

---

## 6. 故障排除

### 问题：编译错误 "找不到符号: 方法 getUsername()"

**原因**：Lombok注解处理器没有正确配置

**解决方案**：
1. 确认 `pom.xml` 中有 `maven-compiler-plugin` 配置（已添加✅）
2. 清理并重新编译：`mvn clean compile`
3. 如果还是不行，尝试：`mvn clean install -U`

### 问题：IDE中代码报错但Maven编译成功

**原因**：IDE没有安装Lombok插件

**解决方案**：
1. 安装对应IDE的Lombok插件（见上方IDE配置）
2. 启用注解处理
3. 重启IDE

### 问题：Lombok版本冲突

**解决方案**：在 `pom.xml` 中显式指定Lombok版本：

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version>
    <optional>true</optional>
</dependency>
```

---

## 7. 当前项目状态

✅ **Maven配置已完成** - 添加了maven-compiler-plugin的Lombok注解处理器配置  
⚠️ **User.java使用手动getter/setter** - 避免IDE未配置导致的问题  
💡 **建议**：配置好IDE后，可以恢复使用Lombok注解简化代码

---

## 总结

使用Lombok需要：
1. ✅ Maven中添加Lombok依赖和注解处理器配置（已完成）
2. ⚠️ IDE中安装Lombok插件并启用注解处理（需要手动配置）
3. ✅ 使用Lombok注解简化代码

现在maven配置已经完成，只需要在你的IDE中安装Lombok插件即可开始使用！
