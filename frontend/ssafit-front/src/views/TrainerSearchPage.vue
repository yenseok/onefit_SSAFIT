<template>
    <div id="app-wrapper" class="app-wrapper">

  <div class="trainer-search-page">
    <!-- 헤더 -->
    <header class="header">
      <div class="header-side">
        <button class="back-btn" @click="goBackToEdit">취소</button>

      </div>
      <div class="header-title">트레이너 검색</div>
      <div class="header-side"></div>
    </header>

    <!-- 검색창 -->
    <div class="search-wrapper">
      <i class="bi bi-search search-icon"></i>
      <input
        type="text"
        v-model="keyword"
        @input="searchTrainers"
        placeholder="검색"
      />
      <ul v-if="filteredTrainers.length > 0" class="dropdown">
        <!-- 미지정 항목 -->
        <li @click="clearTrainer">
          <div class="trainer-name" style="color: #888;">트레이너 미지정</div>
        </li>

        <!-- 검색 결과 -->
        <li
          v-for="trainer in filteredTrainers"
          :key="trainer.trainerId"
          @click="selectTrainer(trainer)"
        >
          <div class="trainer-name">{{ trainer.trainerName }}</div>
        </li>
      </ul>
    </div>

    <!-- 확인 버튼 -->
    <footer class="fixed-footer">
      <button class="primary full-width" @click="confirmSelection">확인</button>
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
const selectedTrainer = ref(null)
const filteredTrainers = ref([])

const gym = JSON.parse(sessionStorage.getItem('selected-gym') || '{}')
const gymId = gym?.gymId

const goBackToEdit = () => {
  const userId = sessionStorage.getItem('user-id')
  router.push({ name: 'profile-edit', params: { userId } })
}


// 트레이너 선택
const selectTrainer = (trainer) => {
  selectedTrainer.value = trainer
  keyword.value = trainer.trainerName
  filteredTrainers.value = []
}

// 트레이너 미지정 선택
const clearTrainer = () => {
  selectedTrainer.value = null
  keyword.value = '미지정'
  sessionStorage.setItem('selected-trainer', JSON.stringify({ trainerId: null, trainerName: '' }))
  filteredTrainers.value = []
}

// 확인 버튼
const confirmSelection = () => {
  const userId = sessionStorage.getItem('user-id')  // 필수 param 가져오기

  if (selectedTrainer.value) {
    sessionStorage.setItem('selected-trainer', JSON.stringify(selectedTrainer.value))
    router.push({ name: 'profile-edit', params: { userId } })  // userId 포함
  } else {
    alert('트레이너를 선택해주세요.')
  }
}



// 트레이너 검색
const searchTrainers = async () => {
  const token = sessionStorage.getItem('access-token')

  if (!token) {
    alert('로그인이 필요합니다.')
    router.push('/login')
    return
  }

  if (!keyword.value.trim() || !gymId) return

  console.log('✅ token:', token)
  console.log('✅ gymId:', gymId)
  console.log('✅ keyword:', keyword.value)

  try {
    const res = await api.get('/trainers', {
      headers: {
        Authorization: `Bearer ${token}`
      },
      params: {
        keyword: keyword.value,
        gymId
      }
    })

    console.log('✅ 검색 결과:', res.data)
    filteredTrainers.value = res.data
  } catch (err) {
    console.error('❌ 트레이너 검색 실패:', err.message || err)
    alert('트레이너 검색 중 오류가 발생했습니다.')
  }
}
</script>

<style scoped>
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
.trainer-search-page {
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

.header-side {
  width: 42px;
  display: flex;
  align-items: center;
}

.back-btn {
  background: none;
  border: none;
  color: #006ffd;
  font-weight: bold;
  font-size: 14px;
  padding: 4px 0;
  cursor: pointer;
}

.header-title {
  font-weight: bold;
  font-size: 16px;
  text-align: center;
  flex: 1;
}

.search-wrapper {
  position: fixed;
  top: 124px; /* 헤더 높이에 맞게 조정 */
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  max-width: 500px;
  padding: 20px 16px;
  z-index: 999; /* 헤더보다 아래이되 다른 콘텐츠보다 위 */
  background-color: white; 
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

.trainer-name {
  font-weight: 600;
  font-size: 15px;
}

.dropdown li:hover {
  background-color: #f8f8f8;
}

.fixed-bottom {
  position: fixed;
  bottom: 70px;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  max-width: 400px;
  padding: 0 16px;
  background-color: white;
  z-index: 100;
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
</style>