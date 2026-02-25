import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as apiLogin } from '../api/index.js'
import Cookies from 'js-cookie'

export const useAuthStore = defineStore('auth', () => {
    // Read from cookie on spin-up
    const savedUser = Cookies.get('user')
    const user = ref(savedUser ? JSON.parse(savedUser) : null)

    const isLoggedIn = computed(() => !!user.value)

    async function login(credentials) {
        const res = await apiLogin(credentials)
        user.value = res.data.user
        // Save to cookie for 7 days
        Cookies.set('user', JSON.stringify(user.value), { expires: 7 })
        return res.data
    }

    function logout() {
        user.value = null
        Cookies.remove('user')
    }

    return { user, isLoggedIn, login, logout }
})
