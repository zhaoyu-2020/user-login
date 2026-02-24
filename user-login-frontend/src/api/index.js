import axios from 'axios'

const http = axios.create({
    baseURL: '/api',
    timeout: 10000,
    headers: { 'Content-Type': 'application/json' }
})

// ── Auth ──────────────────────────────────────────────────────────────────────
export const login = (data) => http.post('/login', data)
export const register = (data) => http.post('/register', data)

// ── Users CRUD ────────────────────────────────────────────────────────────────
export const getUsers = () => http.get('/users')
export const getUserById = (id) => http.get(`/users/${id}`)
export const updateUser = (id, data) => http.put(`/users/${id}`, data)
export const deleteUser = (id) => http.delete(`/users/${id}`)
