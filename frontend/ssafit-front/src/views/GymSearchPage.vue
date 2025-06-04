<template>
  <div id="app-wrapper" class="app-wrapper">
  <div class="gym-search-page">
    <!-- 프로필과 동일한 헤더 구조 -->
    <header class="header">
      <div class="header-side">
        <button class="back-btn" @click="router.back()">취소</button>
      </div>
      <h5 class="header-title">헬스장 검색</h5>
      <div class="header-side"></div>
    </header>

    <!-- 검색창 -->
    <div class="search-wrapper">
      <i class="bi bi-search search-icon"></i>
      <input type="text" v-model="keyword" @input="searchGyms" placeholder="검색" />
<ul v-if="gyms.length > 0" class="dropdown">
<li
  v-for="gym in gyms"
  :key="gym.gymId"
  @click="selectGym(gym)"
  :class="{ selected: selectedGym && selectedGym.gymId === gym.gymId }"
>
  <div class="gym-name-row">
    <div class="gym-name">{{ gym.gymName }}</div>
    <i v-if="selectedGym && selectedGym.gymId === gym.gymId" class="bi bi-check-circle-fill check-icon"></i>
  </div>
  <div class="gym-address">{{ gym.gymAddress }}</div>
</li>

</ul>


    </div>
<footer class="fixed-footer">
  <button class="primary full-width" :disabled="!selectedGym" @click="confirmSelection">
    확인
  </button>
</footer>

  </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/api'

const router = useRouter()
const keyword = ref('')
const gyms = ref([])
const selectedGym = ref(null)

const searchGyms = async () => {
  try {
    const res = await api.get('/gyms', { params: { keyword: keyword.value } })
    gyms.value = res.data
  } catch (err) {
    console.error('헬스장 검색 실패', err)
  }
}

const selectGym = (gym) => {
  selectedGym.value = gym
  console.log('✅ 선택된 gym:', gym.gymName)  // 콘솔에서도 확인 가능
}


const confirmSelection = () => {
  if (!selectedGym.value) return
  sessionStorage.setItem('selected-gym', JSON.stringify(selectedGym.value))
  router.push({
    name: 'trainer-search',
    query: { gymId: selectedGym.value.gymId, from: 'profile-edit' }
  })
}
</script>



<style scoped>
.gym-name-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.check-icon {
  color: #006ffd;
  font-size: 18px;
}

.gym-search-page {
  padding: 0px 0px 100px !important;
  width: 500px !important;
  max-width: 500px !important;
  margin: 0 auto;
}

/* ✅ 프로필 페이지와 동일한 헤더 */
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
  color: #006ffd;
  font-weight: bold;
  font-size: 14px;
  padding: 4px 8px;
  cursor: pointer;
}
.header-title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  font-weight: bold;
  font-size: 16px;
  text-align: center;
}
.header-right-space {
  width: 42px;
}

/* ✅ 검색창 */
.search-wrapper {
  position: fixed;
  top: 124px; /* 헤더 높이에 맞게 조정 */
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  max-width: 500px;
  padding: 20px 16px;
  z-index: 999; /* 헤더보다 아래이되 다른 콘텐츠보다 위 */
  background-color: white; /* 필요시 배경색 추가 */
}

.search-wrapper input {
  width: 100%;
  padding: 12px 16px 12px 40px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 999px;
  background-color: #fff;
}
.search-wrapper .search-icon {
  position: absolute;
  top: 50%;
  left: 28px;
  transform: translateY(-50%);
  font-size: 16px;
  color: #888;
}

/* ✅ 드롭다운 */
.dropdown {
  list-style: none;
  margin: 4px 0 0;
  padding: 0;
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: white;
  max-height: 200px;
  overflow-y: auto;
  position: absolute;
  top: 100%;
  left: 16px;
  right: 16px;
  z-index: 10;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.dropdown li {
  padding: 10px 12px;
  cursor: pointer;
  border-bottom: 1px solid #eee;
}
.dropdown li:last-child {
  border-bottom: none;
}
.gym-name {
  font-weight: 600;
  font-size: 15px;
}
.gym-address {
  font-size: 13px;
  color: #666;
  margin-top: 2px;
}
.dropdown li:hover {
  background-color: #f8f8f8;
}
.fixed-footer {
  position: fixed;
  bottom: 0;
  left: 50%; /* 화면 기준 중앙 정렬 */
  transform: translateX(-50%); /* 정확히 가운데 */
  width: 100%;
  max-width: 500px; /* ✅ app-wrapper와 동일한 최대 너비 */
  padding: 12px 32px;
  background-color: white;
  z-index: 999;
  padding: 12px 12px;
}

.full-width {
  width: 100%;
}
button {
  width: 100%;
  padding: 14px;
  font-size: 16px;
  font-weight: 600;
  color: white;
  background-color: #006ffd;
  border: none;
  border-radius: 12px;
  cursor: pointer;
}
button:hover {
  background-color: #005be0;
}
.dropdown li.selected {
  background-color: #eaf2ff;
  font-weight: bold;
  border-left: 4px solid #006ffd; /* 시각적으로 더 확실하게 표시 */
}



</style>
