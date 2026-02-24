import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as apiLogin } from '../api/index.js'

export const useAuthStore = defineStore('auth', () => {
    const user = ref(JSON.parse(sessionStorage.getItem('user') || 'null'))

    const isLoggedIn = computed(() => !!user.value)

    async function login(credentials) {
        const res = await apiLogin(credentials)
        user.value = res.data.user
        sessionStorage.setItem('user', JSON.stringify(user.value))
        return res.data
    }

    function logout() {
        user.value = null
        sessionStorage.removeItem('user')
    }

    return { user, isLoggedIn, login, logout }
})
