# 分页查询使用指南

## 概述

用户登录服务现已实现完整的分页功能，支持以下操作：
- 分页获取所有用户
- 按用户名搜索用户（支持模糊查询）
- 按邮箱搜索用户（支持模糊查询）
- 支持自定义排序字段和排序顺序

## API 接口说明

### 1. 分页获取所有用户

**请求方式：** `GET /api/users`

**请求参数：**
| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| page | int | 0 | 页码（从0开始） |
| size | int | 10 | 每页大小 |
| sortBy | String | id | 排序字段（id, username, email, createdAt, updatedAt） |
| sortOrder | String | ASC | 排序顺序（ASC 升序，DESC 降序） |

**请求示例：**
```bash
# 获取第一页，每页10条记录
curl "http://localhost:8080/api/users?page=0&size=10"

# 按创建时间降序排列
curl "http://localhost:8080/api/users?page=0&size=10&sortBy=createdAt&sortOrder=DESC"

# 按用户名升序排列
curl "http://localhost:8080/api/users?page=0&size=20&sortBy=username&sortOrder=ASC"
```

**响应示例：**
```json
{
  "content": [
    {
      "id": 1,
      "username": "john_doe",
      "email": "john@example.com",
      "createdAt": "2026-02-25T10:30:00",
      "updatedAt": "2026-02-25T10:30:00"
    },
    {
      "id": 2,
      "username": "jane_doe",
      "email": "jane@example.com",
      "createdAt": "2026-02-25T11:00:00",
      "updatedAt": "2026-02-25T11:00:00"
    }
  ],
  "pageable": {
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 10,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalElements": 2,
  "totalPages": 1,
  "first": true,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 2,
  "empty": false
}
```

### 2. 按用户名搜索用户

**请求方式：** `GET /api/users/search/username`

**请求参数：**
| 参数 | 类型 | 是否必须 | 说明 |
|------|------|---------|------|
| username | String | 是 | 用户名（支持模糊查询，忽略大小写） |
| page | int | 否 | 页码（默认0） |
| size | int | 否 | 每页大小（默认10） |
| sortBy | String | 否 | 排序字段（默认id） |
| sortOrder | String | 否 | 排序顺序（默认ASC） |

**请求示例：**
```bash
# 搜索用户名包含"john"的用户
curl "http://localhost:8080/api/users/search/username?username=john"

# 分页搜索，第二页，每页5条
curl "http://localhost:8080/api/users/search/username?username=john&page=1&size=5"

# 按邮箱排序搜索结果
curl "http://localhost:8080/api/users/search/username?username=john&sortBy=email&sortOrder=DESC"
```

### 3. 按邮箱搜索用户

**请求方式：** `GET /api/users/search/email`

**请求参数：**
| 参数 | 类型 | 是否必须 | 说明 |
|------|------|---------|------|
| email | String | 是 | 邮箱（支持模糊查询，忽略大小写） |
| page | int | 否 | 页码（默认0） |
| size | int | 否 | 每页大小（默认10） |
| sortBy | String | 否 | 排序字段（默认id） |
| sortOrder | String | 否 | 排序顺序（默认ASC） |

**请求示例：**
```bash
# 搜索邮箱包含"example"的用户
curl "http://localhost:8080/api/users/search/email?email=example"

# 分页搜索
curl "http://localhost:8080/api/users/search/email?email=gmail&page=0&size=20"
```

## 响应数据结构说明

**Page 对象包含以下字段：**
- `content`: 当前页的用户数据数组
- `pageable`: 分页信息
  - `pageNumber`: 当前页码
  - `pageSize`: 每页大小
  - `offset`: 偏移量
- `totalElements`: 总记录数
- `totalPages`: 总页数
- `number`: 当前页号（从0开始）
- `size`: 当前页大小
- `numberOfElements`: 当前页的实际元素数量
- `first`: 是否为第一页
- `last`: 是否为最后一页
- `empty`: 是否为空

## 前端集成示例

### Vue.js / JavaScript 示例

```javascript
// 获取分页用户列表
async function fetchUsers(page = 0, size = 10, sortBy = 'id', sortOrder = 'ASC') {
  try {
    const response = await fetch(
      `/api/users?page=${page}&size=${size}&sortBy=${sortBy}&sortOrder=${sortOrder}`
    );
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Failed to fetch users:', error);
  }
}

// 搜索用户
async function searchUsers(username, page = 0, size = 10) {
  try {
    const response = await fetch(
      `/api/users/search/username?username=${encodeURIComponent(username)}&page=${page}&size=${size}`
    );
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Failed to search users:', error);
  }
}

// 按邮箱搜索
async function searchByEmail(email, page = 0, size = 10) {
  try {
    const response = await fetch(
      `/api/users/search/email?email=${encodeURIComponent(email)}&page=${page}&size=${size}`
    );
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Failed to search by email:', error);
  }
}

// 处理分页数据
function handlePaginationData(pageData) {
  const users = pageData.content; // 当前页的用户
  const currentPage = pageData.number; // 当前页码
  const totalPages = pageData.totalPages; // 总页数
  const totalElements = pageData.totalElements; // 总记录数
  
  console.log(`显示第 ${currentPage + 1} 页，共 ${totalPages} 页，共 ${totalElements} 条记录`);
  
  return {
    users,
    currentPage,
    totalPages,
    totalElements,
    hasNextPage: !pageData.last,
    hasPreviousPage: !pageData.first
  };
}

// 使用示例
async function main() {
  // 获取第一页用户（每页10条，按ID升序）
  const result = await fetchUsers(0, 10, 'id', 'ASC');
  const { users, totalPages, hasNextPage } = handlePaginationData(result);
  
  console.log('用户列表:', users);
  console.log('是否有下一页:', hasNextPage);
  
  // 搜索用户名包含"john"的用户
  const searchResult = await searchUsers('john', 0, 10);
  console.log('搜索结果:', searchResult);
}
```

## 常见用法场景

### 场景1：用户列表分页展示
```bash
# 每页显示20条记录，按创建时间倒序
GET /api/users?page=0&size=20&sortBy=createdAt&sortOrder=DESC
```

### 场景2：用户搜索功能
```bash
# 搜索用户名包含"admin"的用户，每页15条
GET /api/users/search/username?username=admin&page=0&size=15
```

### 场景3：后台管理分页查询
```bash
# 第5页，每页25条，按用户名字母顺序
GET /api/users?page=4&size=25&sortBy=username&sortOrder=ASC
```

## 排序字段说明

支持的排序字段：
- `id` - 用户ID（数字排序）
- `username` - 用户名（字母排序）
- `email` - 邮箱（字母排序）
- `createdAt` - 创建时间（时间排序）
- `updatedAt` - 更新时间（时间排序）

## 性能建议

1. **合理设置分页大小**：建议每页10-50条记录，不要过大
2. **避免深层分页**：大数据量时，避免查询过深的页码
3. **使用搜索缩小范围**：如果需要查找特定用户，使用搜索接口而不是全表分页
4. **合理选择排序字段**：优先选择有索引的字段（id, username, email）进行排序

## 错误处理

**常见错误响应：**

400 Bad Request - 参数错误
```json
{
  "error": "Invalid parameter value"
}
```

404 Not Found - 资源不存在
```json
{
  "error": "User not found"
}
```

500 Internal Server Error - 服务器错误
```json
{
  "error": "Internal server error"
}
```

## 总结

这个分页实现提供了：
- ✅ 标准的 Spring Data JPA 分页支持
- ✅ 灵活的排序选项
- ✅ 模糊搜索功能
- ✅ 清晰的 API 接口
- ✅ 完整的分页信息返回

通过这些 API，你可以轻松实现用户列表展示、搜索、排序等常见功能。
