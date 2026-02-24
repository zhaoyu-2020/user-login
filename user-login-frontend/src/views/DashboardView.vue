<template>
  <div class="dashboard">
    <!-- ── Header ──────────────────────────────────────── -->
    <el-card class="dash-header glass-card" shadow="hover">
      <div class="header-left">
        <span class="header-icon">🏠</span>
        <div>
          <h1 class="header-title">用户管理系统</h1>
          <p class="header-sub">欢迎回来，<strong>{{ auth.user?.username }}</strong></p>
        </div>
      </div>
      <el-button type="danger" plain icon="SwitchButton" @click="handleLogout">
        退出登录
      </el-button>
    </el-card>

    <!-- ── Main Content ───────────────────────────────── -->
    <main class="dash-main">
      <!-- Toolbar -->
      <div class="toolbar">
        <div class="toolbar-left">
          <h2 class="section-title">👥 用户列表</h2>
          <el-tag type="primary" effect="light" round>共 {{ users.length }} 位用户</el-tag>
        </div>
        <el-button type="primary" icon="Plus" @click="openAdd">
          新增用户
        </el-button>
      </div>

      <!-- Error -->
      <el-alert v-if="fetchError" :title="fetchError" type="error" show-icon :closable="false" style="margin-bottom:16px" />

      <!-- Table card -->
      <el-card class="table-card glass-card" shadow="hover">
        <el-table :data="users" v-loading="loading" style="width: 100%" row-class-name="table-row">
          <!-- Empty State -->
          <template #empty>
            <el-empty description="暂无用户数据">
              <el-button type="primary" size="small" @click="openAdd">新增第一个用户</el-button>
            </el-empty>
          </template>

          <el-table-column prop="id" label="ID" width="100">
            <template #default="{ row }">
              <el-tag size="small" type="info" effect="plain" class="id-badge">#{{ row.id }}</el-tag>
            </template>
          </el-table-column>

          <el-table-column prop="username" label="用户名" min-width="150">
            <template #default="{ row }">
              <div class="user-cell">
                <div class="custom-avatar">{{ row.username[0].toUpperCase() }}</div>
                <span>{{ row.username }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column prop="email" label="邮箱" min-width="180">
            <template #default="{ row }">
              <span class="email-text">{{ row.email || '—' }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="createdAt" label="创建时间" width="180">
            <template #default="{ row }">
              <span class="time-cell">{{ formatDate(row.createdAt) }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="updatedAt" label="更新时间" width="180">
            <template #default="{ row }">
              <span class="time-cell">{{ formatDate(row.updatedAt) }}</span>
            </template>
          </el-table-column>

          <el-table-column label="操作" width="180" fixed="right">
            <template #default="{ row }">
              <el-button size="small" type="primary" plain icon="Edit" @click="openEdit(row)">
                编辑
              </el-button>
              
              <el-popconfirm
                title="确定要删除该用户吗？此操作不可撤销。"
                confirm-button-text="删除"
                cancel-button-text="取消"
                confirm-button-type="danger"
                icon="Warning"
                icon-color="#f56c6c"
                width="220"
                @confirm="doDelete(row)"
              >
                <template #reference>
                  <el-button size="small" type="danger" plain icon="Delete" :loading="deletingId === row.id">
                    删除
                  </el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </main>

    <!-- ── Add/Edit Modal ─────────────────────────────── -->
    <UserFormModal
      v-if="showModal"
      :user="editingUser"
      @close="closeModal"
      @saved="onSaved"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth.js'
import { getUsers, deleteUser } from '../api/index.js'
import UserFormModal from '../components/UserFormModal.vue'

const router = useRouter()
const auth = useAuthStore()

const users = ref([])
const loading = ref(false)
const fetchError = ref('')
const showModal = ref(false)
const editingUser = ref(null)
const deletingId = ref(null)

// ── Fetch ────────────────────────────────────────────────────────────────────
async function fetchUsers() {
  loading.value = true
  fetchError.value = ''
  try {
    const res = await getUsers()
    users.value = res.data
  } catch (err) {
    fetchError.value = '加载数据失败，请刷新重试'
  } finally {
    loading.value = false
  }
}

onMounted(fetchUsers)

// ── Modal ────────────────────────────────────────────────────────────────────
function openAdd() {
  editingUser.value = null
  showModal.value = true
}

function openEdit(user) {
  editingUser.value = { ...user }
  showModal.value = true
}

function closeModal() {
  showModal.value = false
  editingUser.value = null
}

function onSaved() {
  fetchUsers()
}

// ── Delete ───────────────────────────────────────────────────────────────────
async function doDelete(user) {
  deletingId.value = user.id
  try {
    await deleteUser(user.id)
    fetchUsers()
  } catch (err) {
    fetchError.value = err.response?.data?.error || '删除失败'
  } finally {
    deletingId.value = null
  }
}

// ── Auth ─────────────────────────────────────────────────────────────────────
function handleLogout() {
  auth.logout()
  router.push('/login')
}

// ── Utils ────────────────────────────────────────────────────────────────────
function formatDate(dt) {
  if (!dt) return '—'
  return new Date(dt).toLocaleString('zh-CN', {
    year: 'numeric', month: '2-digit', day: '2-digit',
    hour: '2-digit', minute: '2-digit'
  })
}
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  gap: 24px;
  padding: 24px;
  max-width: 1280px;
  margin: 0 auto;
}

/* ── Header ────────────────────────── */
.dash-header {
  border-radius: var(--radius-lg, 12px);
  border: 1px solid var(--glass-border, rgba(255, 255, 255, 0.1));
}

:deep(.dash-header .el-card__body) {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 28px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 14px;
}
.header-icon { font-size: 32px; }
.header-title {
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary, #333);
  margin-bottom: 2px;
}
.header-sub { font-size: 13px; color: var(--text-muted, #999); }
.header-sub strong { color: #409eff; }

/* ── Main ──────────────────────────── */
.dash-main { display: flex; flex-direction: column; gap: 16px; }

.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 12px;
}
.toolbar-left { display: flex; align-items: center; gap: 12px; }
.section-title { font-size: 18px; font-weight: 600; color: var(--text-primary, #333); }

/* ── Table Card ───────────────────── */
.table-card {
  border-radius: var(--radius-lg, 12px);
  border: 1px solid var(--glass-border, rgba(255, 255, 255, 0.1));
  overflow: hidden;
}

:deep(.table-card .el-card__body) {
  padding: 0;
}

.id-badge {
  font-family: monospace;
  font-weight: 600;
}

.user-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.custom-avatar {
  width: 32px; height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, #409eff, #66b1ff);
  display: flex; align-items: center; justify-content: center;
  font-size: 13px; font-weight: 700; color: #fff;
  flex-shrink: 0;
}

.email-text { color: var(--text-secondary, #666); font-size: 13px; }
.time-cell { color: var(--text-muted, #999); font-size: 12px; white-space: nowrap; }

/* Default element-plus styles might overlap dark theme logic, leaving this here for fallback */
@media (prefers-color-scheme: dark) {
  .header-title, .section-title {
    color: #eee;
  }
}
</style>
