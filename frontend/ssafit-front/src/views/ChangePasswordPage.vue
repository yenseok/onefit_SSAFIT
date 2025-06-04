<template>
  <div id="app-wrapper" class="app-wrapper">
  <div class="container">
    <!-- 헤더 -->
    <header class="header">
      <button class="back-btn" @click="router.back()">
        <i class="bi bi-chevron-left text-primary fs-4"></i>
      </button>
      <h5 class="header-title">비밀번호 확인</h5>
      <div class="spacer"></div>
    </header>

    <!-- 비밀번호 입력 -->
    <div class="content">
      <p class="guide">현재 비밀번호를 입력하세요</p>
      <div class="password-wrapper">
        <input
          :type="showPassword ? 'text' : 'password'"
          v-model="currentPassword"
          placeholder="비밀번호"
        />
        <i
          :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"
          class="toggle-icon"
          @click="togglePassword"
        ></i>
      </div>

      <button class="verify-btn" @click="verifyPassword">인증하기</button>
    </div>
  </div>
</div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api/api'

const router = useRouter()
const route = useRoute()
const userId = route.params.userId
const currentPassword = ref('')
const showPassword = ref(false)

const togglePassword = () => {
  showPassword.value = !showPassword.value
}

const verifyPassword = async () => {
  try {
    const dto = { userId: Number(userId), currentPassword: currentPassword.value }
    await api.post(`http://localhost:8080/auth/verify-password`, dto)
    router.push({ name: 'change-password-step2', params: { userId } })
  } catch (err) {
    alert('비밀번호가 일치하지 않습니다.')
  }
}
</script>

<style scoped>
.container {
  padding: 0px 0px 100px !important;
  width: 500px !important;
  max-width: 500px !important;
  margin: 0 auto;
}

.header {
  position: fixed;
  top: 57px;
  z-index: 1000;
  width: 500px;
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #ddd;
}

.back-btn {
  background: none;
  border: none;
  padding: 0;
}

.header-title {
  font-weight: bold;
  font-size: 16px;
  margin: 0 auto;
}

.spacer {
  width: 42px;
}

.content {
  padding: 24px 20px;
}

.guide {
  font-size: 15px;
  font-weight: bold;
  margin-bottom: 12px;
}

.password-wrapper {
  position: relative;
  margin-bottom: 24px;
}

.password-wrapper input {
  width: 100%;
  padding: 12px 40px 12px 16px;
  font-size: 16px;
  border-radius: 12px;
  border: 1px solid #ccc;
  background-color: #fff;
}

.toggle-icon {
  position: absolute;
  right: 14px;
  top: 50%;
  transform: translateY(-50%);
  color: #888;
  font-size: 18px;
  cursor: pointer;
}

.verify-btn {
  width: 100%;
  background-color: #006ffd;
  color: #fff;
  font-weight: bold;
  padding: 14px;
  font-size: 16px;
  border: none;
  border-radius: 12px;
  cursor: pointer;
}

.verify-btn:hover {
  background-color: #006ffd;
}
</style>
