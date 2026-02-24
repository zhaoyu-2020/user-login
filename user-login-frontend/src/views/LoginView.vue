<template>
  <div class="auth-page">
    <div class="blob blob-1"></div>
    <div class="blob blob-2"></div>

    <el-card class="auth-card glass-card" shadow="hover">
      <div class="auth-logo">
        <div class="logo-icon">🔐</div>
        <h1 class="logo-title">用户管理系统</h1>
        <p class="logo-sub">欢迎回来，请登录您的账号</p>
      </div>

      <el-alert v-if="error" :title="error" type="error" show-icon :closable="false" style="margin-bottom: 20px;" />

      <el-form :model="form" @submit.prevent="handleLogin" class="auth-form" size="large">
        <el-form-item>
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
            autocomplete="username"
          />
        </el-form-item>

        <el-form-item>
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            :prefix-icon="Lock"
            autocomplete="current-password"
            show-password
          />
        </el-form-item>

        <el-button type="primary" native-type="submit" class="btn-full" :loading="loading" size="large">
          登 录
        </el-button>
      </el-form>

      <p class="auth-footer">
        还没有账号？
        <router-link to="/register" class="auth-link">立即注册</router-link>
      </p>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth.js'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const auth = useAuthStore()

const form = reactive({ username: '', password: '' })
const loading = ref(false)
const error = ref('')

async function handleLogin() {
  if (!form.username || !form.password) {
    error.value = '请输入用户名和密码'
    return
  }
  error.value = ''
  loading.value = true
  try {
    await auth.login(form)
    router.push('/dashboard')
  } catch (err) {
    error.value = err.response?.data?.error || '登录失败，请检查用户名和密码'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  position: relative;
  overflow: hidden;
  background-color: var(--bg-color); /* Add a fallback bg color if needed */
}

.blob {
  position: fixed;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.18;
  pointer-events: none;
  animation: float 8s ease-in-out infinite;
}
.blob-1 {
  width: 500px; height: 500px;
  background: radial-gradient(circle, #7c3aed, #4f46e5);
  top: -150px; left: -100px;
}
.blob-2 {
  width: 400px; height: 400px;
  background: radial-gradient(circle, #06b6d4, #7c3aed);
  bottom: -100px; right: -80px;
  animation-delay: -4s;
}
@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(20px, -30px) scale(1.05); }
}

.auth-card {
  width: 100%;
  max-width: 420px;
  position: relative;
  z-index: 1;
  border-radius: var(--radius-lg, 16px);
  border: 1px solid var(--glass-border, rgba(255, 255, 255, 0.1));
  background: rgba(255, 255, 255, 0.9);
}

:deep(.el-card__body) {
  padding: 40px;
}

.auth-logo {
  text-align: center;
  margin-bottom: 30px;
}
.logo-icon {
  font-size: 48px;
  margin-bottom: 12px;
}
.logo-title {
  font-size: 22px;
  font-weight: 700;
  color: #333;
  margin-bottom: 6px;
}
.logo-sub {
  font-size: 14px;
  color: #666;
}

.auth-form {
  margin-bottom: 24px;
}

.btn-full {
  width: 100%;
  margin-top: 10px;
}

.auth-footer {
  text-align: center;
  font-size: 14px;
  color: #666;
}
.auth-link {
  color: #409eff;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
}
.auth-link:hover {
  color: #66b1ff;
}

/* Specific to keeping the global variables but adapting Element's styles slightly */
@media (prefers-color-scheme: dark) {
  .auth-card {
    background: rgba(30,30,30,0.8);
    backdrop-filter: blur(16px);
  }
  .logo-title {
    color: #eee;
  }
  .logo-sub, .auth-footer {
    color: #aaa;
  }
}
</style>
