# 分页 API 快速参考卡片

## API 端点速查表

| 功能 | 方法 | 端点 | 用途 |
|------|------|------|------|
| 分页列表 | GET | `/api/users` | 获取所有用户（支持排序） |
| 用户名搜索 | GET | `/api/users/search/username` | 按用户名搜索 |
| 邮箱搜索 | GET | `/api/users/search/email` | 按邮箱搜索 |
| 获取单个 | GET | `/api/users/{id}` | 根据ID获取用户 |

---

## 快速使用示例

### 获取第一页用户（每页10条）
```bash
curl "http://localhost:8080/api/users?page=0&size=10"
```

### 获取第二页，每页20条，按创建时间倒序
```bash
curl "http://localhost:8080/api/users?page=1&size=20&sortBy=createdAt&sortOrder=DESC"
```

### 搜索用户名包含"admin"
```bash
curl "http://localhost:8080/api/users/search/username?username=admin"
```

### 搜索邮箱包含"gmail"，每页15条
```bash
curl "http://localhost:8080/api/users/search/email?email=gmail&size=15"
```

---

## 排序参数组合

| 排序需求 | sortBy | sortOrder |
|---------|--------|-----------|
| 按ID升序（默认） | id | ASC |
| 按ID降序 | id | DESC |
| 按用户名升序 | username | ASC |
| 按邮箱升序 | email | ASC |
| 最新创建的优先 | createdAt | DESC |
| 最近更新的优先 | updatedAt | DESC |

---

## 响应数据关键字段

| 字段 | 说明 | 类型 |
|------|------|------|
| `content` | 当前页的用户数组 | Array |
| `totalElements` | 总记录数 | int |
| `totalPages` | 总页数 | int |
| `number` | 当前页号（从0开始） | int |
| `size` | 页面大小 | int |
| `numberOfElements` | 本页实际记录数 | int |
| `first` | 是否为第一页 | boolean |
| `last` | 是否为最后一页 | boolean |

---

## 前端集成代码片段

### Vue 3 Composition API
```javascript
import { ref, reactive } from 'vue'

export function usePagination() {
  const page = ref(0)
  const size = ref(10)
  const users = ref([])
  const total = ref(0)
  const totalPages = ref(0)

  const fetchUsers = async (pageNum = 0, pageSize = 10) => {
    const response = await fetch(
      `/api/users?page=${pageNum}&size=${pageSize}`
    )
    const data = await response.json()
    
    users.value = data.content
    total.value = data.totalElements
    totalPages.value = data.totalPages
    page.value = pageNum
    size.value = pageSize
  }

  const searchUsers = async (username, pageNum = 0) => {
    const response = await fetch(
      `/api/users/search/username?username=${encodeURIComponent(username)}&page=${pageNum}`
    )
    const data = await response.json()
    
    users.value = data.content
    total.value = data.totalElements
    totalPages.value = data.totalPages
  }

  return { users, page, total, totalPages, fetchUsers, searchUsers }
}
```

### React Hooks
```javascript
import { useState } from 'react'

export function usePagination() {
  const [page, setPage] = useState(0)
  const [users, setUsers] = useState([])
  const [totalPages, setTotalPages] = useState(0)

  const fetchUsers = async (pageNum = 0, pageSize = 10) => {
    const response = await fetch(
      `/api/users?page=${pageNum}&size=${pageSize}`
    )
    const data = await response.json()
    
    setUsers(data.content)
    setTotalPages(data.totalPages)
    setPage(pageNum)
  }

  return { users, page, totalPages, fetchUsers }
}
```

---

## 常见错误和解决方案

| 错误 | 原因 | 解决方案 |
|------|------|---------|
| 400 Bad Request | 参数格式错误 | 检查 page/size 是否为整数 |
| 404 Not Found | 资源不存在 | 检查 API 路径是否正确 |
| 500 Server Error | 服务器错误 | 查看服务器日志 |
| 空结果 | 搜索关键词不匹配 | 尝试其他搜索词 |

---

## 分页计算公式

```javascript
// 计算当前页的起始记录号
startRecord = page * size + 1

// 计算当前页的结束记录号
endRecord = Math.min((page + 1) * size, totalElements)

// 判断是否有下一页
hasNext = !isLast

// 判断是否有上一页
hasPrev = !isFirst

// 计算跳转到最后一页的页码
lastPageNum = totalPages - 1
```

---

## 性能优化建议

1. **合理的页面大小**
   - 推荐：10-25 条/页
   - 避免过大（>100）或过小（<5）

2. **搜索优化**
   - 在后端实现搜索而不是前端过滤
   - 使用模糊搜索前给用户提示

3. **缓存策略**
   - 缓存热门分页（第1-3页）
   - 缓存搜索结果避免重复查询

4. **分页深度**
   - 避免用户访问很深的分页
   - 建议通过搜索而不是翻页查找

---

## 测试 curl 命令集合

```bash
# 基本分页
curl -X GET "http://localhost:8080/api/users?page=0&size=10"

# 排序测试
curl -X GET "http://localhost:8080/api/users?page=0&size=10&sortBy=createdAt&sortOrder=DESC"

# 用户名搜索
curl -X GET "http://localhost:8080/api/users/search/username?username=test"

# 邮箱搜索
curl -X GET "http://localhost:8080/api/users/search/email?email=example.com"

# 分页搜索
curl -X GET "http://localhost:8080/api/users/search/username?username=admin&page=1&size=20"
```

---

**最后更新：** 2026年2月25日
