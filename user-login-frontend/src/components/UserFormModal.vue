<template>
  <el-dialog
    :model-value="true"
    :title="isEdit ? '编辑用户' : '新增用户'"
    width="460px"
    @close="$emit('close')"
    :close-on-click-modal="false"
  >
    <el-alert v-if="error" :title="error" type="error" show-icon :closable="false" style="margin-bottom: 20px;" />

    <el-form :model="form" @submit.prevent="handleSubmit" label-width="120px" label-position="top">
      <el-form-item label="用户名" required>
        <el-input v-model="form.username" placeholder="3-50 个字符" />
      </el-form-item>

      <el-form-item label="邮箱">
        <el-input v-model="form.email" placeholder="example@email.com" />
      </el-form-item>

      <el-form-item :label="isEdit ? '密码 (留空则不修改)' : '密码'" :required="!isEdit">
        <el-input
          v-model="form.password"
          type="password"
          placeholder="至少 6 位"
          show-password
          @keyup.enter="handleSubmit"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="$emit('close')" :disabled="loading">取消</el-button>
        <el-button type="primary" :loading="loading" @click="handleSubmit">
          {{ isEdit ? '保存修改' : '确认' }}
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { updateUser, register } from '../api/index.js'

const props = defineProps({
  user: { type: Object, default: null }
})
const emit = defineEmits(['close', 'saved'])

const isEdit = ref(!!props.user)
const loading = ref(false)
const error = ref('')

const form = reactive({
  username: props.user?.username || '',
  email: props.user?.email || '',
  password: ''
})

watch(() => props.user, (u) => {
  isEdit.value = !!u
  form.username = u?.username || ''
  form.email = u?.email || ''
  form.password = ''
})

async function handleSubmit() {
  if (!form.username || (!isEdit.value && !form.password)) {
    error.value = '请输入必填项'
    return
  }
  error.value = ''
  loading.value = true
  try {
    const payload = {}
    if (form.username) payload.username = form.username
    if (form.email) payload.email = form.email
    if (form.password) payload.password = form.password

    if (isEdit.value) {
      await updateUser(props.user.id, payload)
    } else {
      await register({ username: form.username, email: form.email, password: form.password })
    }
    emit('saved')
    emit('close')
  } catch (err) {
    error.value = err.response?.data?.error || '操作失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* No specific custom css needed as Element Plus Dialog/Form handle the heavy lifting */
</style>
