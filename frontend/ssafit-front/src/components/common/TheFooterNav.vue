<template>
  <footer class="footer-nav">
    <router-link to="/" class="tab">
      <img :src="getIcon('home')" alt="홈" class="icon" />
      <span>홈</span>
    </router-link>
    <router-link to="/my-reservations" class="tab">
      <img :src="getIcon('calendar')" alt="나의 예약" class="icon" />
      <span>나의 예약</span>
    </router-link>
    <router-link to="/board/create" class="tab">
      <img :src="getIcon('create')" alt="글쓰기" class="icon" />
      <span>글쓰기</span>
    </router-link>
    <router-link to="/reservation/pt" class="tab">
      <img :src="getIcon('pt')" alt="PT 예약" class="icon" />
      <span>PT 예약</span>
    </router-link>
    <router-link to="/equipment-reservation" class="tab">
      <img :src="getIcon('dumbell')" alt="운동기구 예약" class="icon" />
      <span>운동기구</span>
    </router-link>
  </footer>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const getIcon = (tabName) => {
  const isActive = (() => {
    switch (tabName) {
      case 'home': return route.path === '/'
      case 'calendar': return route.path === '/my-reservations'
      case 'create': return route.path === '/board/create'
      case 'pt': return route.path === '/reservation/pt'
      case 'dumbell': return route.path.startsWith('/equipment-reservation')
      default: return false
    }
  })()

  const color = isActive ? 'blue' : 'gray'
  return new URL(`/src/assets/icons/menu/${tabName}_${color}.png`, import.meta.url).href
}
</script>

<style scoped>
.footer-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 64px;
  background-color: white;
  border-top: 1px solid #ddd;
  display: flex;
  justify-content: space-around;
  align-items: center;
  z-index: 1000;
}

.tab {
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: 12px;
  color: #333;
  text-decoration: none;
}

.icon {
  width: 24px;
  height: 24px;
  margin-bottom: 2px;
}
</style>
