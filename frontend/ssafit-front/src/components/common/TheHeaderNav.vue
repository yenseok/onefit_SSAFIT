<template>
  <header class="header">
    <div class="header-wrapper">
      <div class="header-inner">
        <router-link to="/" class="logo">OneFit</router-link>

        <nav class="nav-links">
          <!-- 로그인 필요한 메뉴들 -->
          <a href="#" @click.prevent="handleAuthGuard('/my-reservations')">나의 예약</a>
          <router-link to="/equipment-reservation">운동기구 예약</router-link>
          <a href="#" @click.prevent="handleAuthGuard('/reservation/pt')">PT 예약</a>

          <!-- 로그인 / 프로필 -->
          <router-link v-if="authStore.isLoggedIn" to="/profile">프로필</router-link>
          <router-link v-else to="/auth">로그인</router-link>
        </nav>
      </div>
    </div>
  </header>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const router = useRouter()

const handleAuthGuard = (targetPath) => {
  if (!authStore.isLoggedIn) {
    alert('로그인 후 이용해 주세요.')
    router.push('/auth') // 👉 alert 후 로그인 페이지로 이동
    return
  }
  router.push(targetPath)
}
</script>



<style scoped>
.header {
  position: fixed;
  height: 56px;
  top: 0;
  background-color: white;
  border-bottom: 1px solid #ddd;
  z-index: 1000;
  width: 100%;
  display: flex;
  justify-content: center; /* ✅ 가운데 정렬 */
}
.header-wrapper {
  width: 100%;
  max-width: 500px;
  height: 60px; /* 원하는 세로 높이 */
  display: flex;
  align-items: center; /* 세로 중앙 정렬 */
  padding: 0 16px;     /* 좌우 여백 */
  box-sizing: border-box;
}

.header-inner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  white-space: nowrap;
  flex-wrap: nowrap;
}

header.fixed-header {
  padding: 12px 16px; /* 상하 12px, 좌우 16px */
}


.logo {
  font-size: 18px;
  font-weight: bold;
  color: #006ffd;
  text-decoration: none;
}


.nav-links {
  display: flex;
  gap: 16px;
  flex-wrap: nowrap;         /* ✅ 줄바꿈 방지 */
  white-space: nowrap;       /* ✅ 글자 줄바꿈 방지 */
  overflow-x: auto;          /* ✅ 혹시 넘치면 스크롤 (선택) */
}


.nav-links a {
  font-size: 13px;
  color: #006ffd;
  font-weight: 500;
  text-decoration: none;

  text-align: center;
}


</style>
