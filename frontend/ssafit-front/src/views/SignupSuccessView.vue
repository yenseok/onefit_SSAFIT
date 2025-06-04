<!-- src/views/SignupSuccessView.vue -->
<template>
  <div id="app-wrapper" class="app-wrapper">
  <div class="signup-success-wrapper">
    <div class="signup-success-container">
      <h2 class="title">회원가입을 축하합니다! 🎉</h2>
      <p class="subtitle">이제 로그인하고 다양한 서비스를 이용해보세요.</p>
      <div class="button-group">
        <button class="action-button" @click="goLogin">로그인</button>
        <button class="action-button secondary" @click="goHome">메인페이지</button>
      </div>
    </div>
  </div>
</div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

const goLogin = () => router.push({ name: 'login' })
const goHome = () => router.push({ name: 'home' })
</script>

<style scoped>
/* 전체 페이지 정렬 */
.signup-success-wrapper {
  padding: 10px 0px 100px !important;
  width: 500px !important;
  height: 800px;
  margin: 0 auto;
  top: 0;
}

.signup-success-container {
  width: 100%;
  max-width: 500px;
  text-align: center;
  font-family: 'sans-serif';
}

/* 제목 */
.title {
  margin-top: 100px;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 12px;
}

/* 부제목 */
.subtitle {
  font-size: 16px;
  color: #666;
  margin-bottom: 32px;
}

/* 버튼 그룹 */
.button-group {
  margin-top:100px;
  display: flex;
  justify-content: center;
  gap: 12px;
}

/* 버튼 스타일 */
.action-button {
  padding: 12px 20px;
  font-size: 16px;
  border: none;
  border-radius: 10px;
  background-color: #006ffd;
  color: white;
  cursor: pointer;
  flex: 1;
  max-width: 180px;
  transition: background-color 0.2s;
}

.action-button:hover {
  background-color: #0055cc;
}

.action-button.secondary {
  background-color: #eee;
  color: #333;
}

.action-button.secondary:hover {
  background-color: #ddd;
}
</style>
