<template>
  <div class="auth-page">
    <div class="blob blob-1"></div>
    <div class="blob blob-2"></div>

    <el-card class="auth-card glass-card" shadow="hover">
      <div class="auth-logo">
        <div class="logo-icon">✨</div>
        <h1 class="logo-title">创建账号</h1>
        <p class="logo-sub">注册一个新账号开始使用</p>
      </div>

      <el-alert v-if="error" :title="error" type="error" show-icon :closable="false" style="margin-bottom: 20px;" />
      <el-alert v-if="successMsg" :title="successMsg" type="success" show-icon :closable="false" style="margin-bottom: 20px;" />

      <el-form :model="form" @submit.prevent="handleRegister" class="auth-form" size="large">
        <el-form-item>
          <el-input
            v-model="form.username"
            placeholder="用户名 (3-50 个字符)"
            :prefix-icon="User"
            autocomplete="username"
          />
        </el-form-item>

        <el-form-item>
          <el-input
            v-model="form.email"
            type="email"
            placeholder="邮箱 (可选)"
            :prefix-icon="Message"
            autocomplete="email"
          />
        </el-form-item>

        <el-form-item>
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码 (至少 6 位)"
            :prefix-icon="Lock"
            autocomplete="new-password"
            show-password
          />
        </el-form-item>

        <el-button type="primary" native-type="submit" class="btn-full" :loading="loading" size="large">
          注 册
        </el-button>
      </el-form>

      <p class="auth-footer">
        已有账号？
        <router-link to="/login" class="auth-link">立即登录</router-link>
      </p>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api/index.js'
import { User, Lock, Message } from '@element-plus/icons-vue'

const router = useRouter()
const form = reactive({ username: '', email: '', password: '' })
const loading = ref(false)
const error = ref('')
const successMsg = ref('')

async function handleRegister() {
  if (!form.username || !form.password) {
    error.value = '请输入必填项'
    return
  }
  error.value = ''
  successMsg.value = ''
  loading.value = true
  try {
    await register(form)
    successMsg.value = '注册成功！即将跳转到登录页...'
    setTimeout(() => router.push('/login'), 1500)
  } catch (err) {
    error.value = err.response?.data?.error || '注册失败，请稍后重试'
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
  background-color: var(--bg-color);
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
  background: radial-gradient(circle, #06b6d4, #4f46e5);
  top: -100px; right: -100px;
}
.blob-2 {
  width: 400px; height: 400px;
  background: radial-gradient(circle, #7c3aed, #ec4899);
  bottom: -80px; left: -80px;
  animation-delay: -4s;
}
@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(-20px, -25px) scale(1.05); }
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
.logo-sub { font-size: 14px; color: #666; }

.auth-form {
  margin-bottom: 24px;
}
.btn-full { width: 100%; margin-top: 10px; }
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
.auth-link:hover { color: #66b1ff; }

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
