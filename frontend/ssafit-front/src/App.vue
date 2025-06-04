<template>
  <div id="app">
    <header class="fixed-header">
      <TheHeaderNav />
    </header>
    <div class="content-wrapper">
      <router-view />
    </div>
  </div>
</template>


<script setup>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import TheHeaderNav from '@/components/common/TheHeaderNav.vue'

const route = useRoute()
const isBoardCreatePage = computed(() => route.path === '/board/create')
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  font-family: sans-serif;
  background-color: white;
  overflow-x: hidden;
}

#app {
  width: 100%;
  max-width: 500px;
  margin: 0 auto;
  position: relative;
  padding: 0px;
}

/* ✅ 헤더 고정 */
.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;

  /* ✅ 브라우저 전체 너비 가운데 정렬을 위해 flex 사용 */
  display: flex;
  justify-content: center;

  background-color: white;
  z-index: 1000;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.fixed-header > * {
  width: 100%;
  max-width: 500px; /* ✅ app-wrapper에 맞춤 */
}

/* ✅ 헤더 높이만큼 아래에 여백 확보 */
.content-wrapper {
  padding-top: 56px; /* 헤더 높이에 맞춰 조절 */
}

/* 기타 스타일 */
*, *::before, *::after {
  box-sizing: border-box;
}
</style>