<template>
    <div id="app-wrapper" class="app-wrapper">
  <div class="container">
    <!-- 헤더 -->
    <header class="header">
      <button class="back-btn" @click="router.back()">
        <i class="bi bi-chevron-left text-primary fs-4"></i>
      </button>
      <h5 class="header-title">비밀번호 변경</h5>
      <div class="spacer"></div>
    </header>

    <!-- 비밀번호 입력 -->
    <div class="content">
      <p class="guide">새 비밀번호를 입력하세요</p>

      <!-- 새 비밀번호 -->
      <div class="password-wrapper">
  <input
    :type="showNew ? 'text' : 'password'"
    v-model="newPassword"
    placeholder="새 비밀번호"
  />
  <i
    :class="showNew ? 'bi bi-eye-slash' : 'bi bi-eye'"
    class="toggle-icon"
    @click="showNew = !showNew"
  ></i>
  <p class="password-msg" :class="pwStatus">{{ pwMessage }}</p>
</div>

      <!-- 확인 비밀번호 -->
      <div class="password-wrapper">
  <input
    :type="showConfirm ? 'text' : 'password'"
    v-model="passwordCheck"
    placeholder="비밀번호 확인"
  />
  <i
    :class="showConfirm ? 'bi bi-eye-slash' : 'bi bi-eye'"
    class="toggle-icon"
    @click="showConfirm = !showConfirm"
  ></i>
  <p class="password-msg" :class="pwCheckStatus">{{ pwCheckMessage }}</p>
</div>

      <button class="verify-btn" @click="changePassword">비밀번호 변경하기</button>
    </div>
  </div>
</div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api/api'

const router = useRouter()
const route = useRoute()

// userId 파라미터가 없으면 sessionStorage에서 가져오기 (백업용)
const userId = route.params.userId || sessionStorage.getItem('user-id')

// 올바른 키 이름 사용: 'access-token'
const token = sessionStorage.getItem('access-token')

const newPassword = ref('')
const passwordCheck = ref('')
const showNew = ref(false)
const showConfirm = ref(false)

const pwStatus = computed(() => {
  const regex = /^(?=.*[a-z])(?=.*[A-Z]).{8,}$/
  return regex.test(newPassword.value) ? 'valid' : 'invalid'
})

const pwMessage = computed(() => {
  if (!newPassword.value) return '비밀번호는 대소문자 포함 8자 이상이어야 합니다.'
  const regex = /^(?=.*[a-z])(?=.*[A-Z]).{8,}$/
  return regex.test(newPassword.value)
    ? '안전한 비밀번호입니다.'
    : '비밀번호는 대소문자 포함 8자 이상이어야 합니다.'
})

const pwCheckStatus = computed(() => {
  if (!passwordCheck.value) return ''
  return passwordCheck.value === newPassword.value ? 'valid' : 'invalid'
})

const pwCheckMessage = computed(() => {
  if (!passwordCheck.value) return '비밀번호를 한 번 더 입력해주세요.'
  return passwordCheck.value === newPassword.value
    ? '비밀번호가 일치합니다.'
    : '비밀번호가 일치하지 않습니다.'
})

const changePassword = async () => {
  if (newPassword.value !== passwordCheck.value) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  }
  console.log("요청 보낼 토큰:", token)
  try {
    await api.put(
      `/users/${userId}/password`,
      { newPassword: newPassword.value },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    )
    alert('비밀번호 변경 완료')
    router.push({ name: 'profile' })
  } catch (err) {
    console.error('비밀번호 변경 실패:', err)
    alert('비밀번호 변경 실패')
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
  margin-bottom: 16px;
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
  background-color: #005be0;
}
.password-msg {
  font-size: 13px;
  margin-top: 4px;
}

.password-msg.valid {
  color: #2db400;
}

.password-msg.invalid {
  color: #f44336;
}

</style>
