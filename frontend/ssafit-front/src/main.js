import './assets/main.css'
import './assets/base.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import 'bootstrap-icons/font/bootstrap-icons.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

const app = createApp(App)

const pinia = createPinia()
app.use(pinia)
app.use(router)

// pinia 등록 이후에 useAuthStore 호출
import { useAuthStore } from '@/stores/auth'
const authStore = useAuthStore()
authStore.restoreSession()

app.mount('#app')
