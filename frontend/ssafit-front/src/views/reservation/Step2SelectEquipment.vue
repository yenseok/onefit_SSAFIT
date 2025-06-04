<template>
  <div id="app-wrapper" class="app-wrapper">
  <div class="equipment-select-container">
    <header class="header">
<button class="back-btn" @click="goToStep1">
  <i class="bi bi-chevron-left text-primary fs-4"></i>
</button>

      <h5 class="header-title">운동기구 예약</h5>
    </header>
    <!-- 단계 표시 -->
    <div class="step-indicator">
      <div class="step" :class="{ active: currentStep === 1 }">
        <div class="circle">1</div>
        <div class="label">날짜 선택</div>
      </div>
      <div class="step" :class="{ active: currentStep === 2 }">
        <div class="circle">2</div>
        <div class="label">운동기구 선택</div>
      </div>
      <div class="step" :class="{ active: currentStep === 3 }">
        <div class="circle">3</div>
        <div class="label">시간 선택</div>
      </div>
    </div>

<div class="search-wrapper">
  <div class="search-icon-wrapper">
    <i class="bi bi-search"></i>
  </div>
  <input
    v-model="keyword"
    placeholder="운동기구 검색"
    class="search-input"
  />
</div>

<ul v-if="filteredEquipmentList.length > 0" class="card-list">
  <li
    v-for="item in filteredEquipmentList"
    :key="item.equipmentId"
    @click="selectEquipment(item)"
    :class="['equipment-card', { selected: selectedEquipment?.equipmentId === item.equipmentId }]"
  >
    <div class="image-container">
      <img :src="getEquipmentImage(item)" alt="운동기구 이미지" />
    </div>
    <div class="text-container">
      <p class="equipment-tags">
        <span v-for="tag in item.tags" :key="tag">#{{ tag }}</span>
      </p>
      <p class="equipment-name">{{ item.equipmentName }}</p>
    </div>
  </li>
</ul>

<footer class="fixed-footer">
    <button :disabled="!selectedEquipment" @click="goToTimeSelect" class="next-btn">
      시간 선택하러 가기
    </button>
    </footer>
  </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import api from '@/api/api'
import { useRouter } from 'vue-router'

const keyword = ref('')
const originalEquipmentList = ref([])
const selectedEquipment = ref(null)
const router = useRouter()

const gymId = sessionStorage.getItem('gym-id')
const selectedDate = sessionStorage.getItem('reservation-date')
const currentStep = 2

if (!selectedDate) {
  alert('날짜가 없습니다. 다시 선택해주세요.')
  router.push({ name: 'equipment-calendar' })
}

const formattedDate = new Date(selectedDate).toISOString().split('T')[0]

// 1. 장비 불러오기
const fetchEquipments = async () => {
  try {
    const res = await api.get(`/equipments`, {
      params: { gymId, date: formattedDate },
      headers: { Authorization: `Bearer ${sessionStorage.getItem('access-token')}` },
    })
    originalEquipmentList.value = res.data
  } catch (err) {
    console.error('전체 운동기구 불러오기 에러:', err)
  }
}

// 2. 필터링된 리스트
const filteredEquipmentList = computed(() => {
  const q = keyword.value.toLowerCase()
  return originalEquipmentList.value.filter(item =>
    item.equipmentName.toLowerCase().includes(q) ||
    item.tags?.some(tag => tag.toLowerCase().includes(q))
  )
})

// 3. 선택 시 처리
const selectEquipment = (item) => {
  selectedEquipment.value = item
  keyword.value = item.equipmentName
  sessionStorage.setItem('equipmentId', item.equipmentId)
  sessionStorage.setItem('equipmentName', item.equipmentName)
  sessionStorage.setItem('reservation-date', selectedDate)
}

// 4. 다음 단계로
const goToTimeSelect = () => {
  if (!selectedEquipment.value) {
    alert('운동기구를 선택해주세요.')
    return
  }
  router.push({ name: 'equipment-time' })
}

const goToStep1 = () => {
  router.push({ name: 'equipment-calendar' })
}

// 5. 이미지 동적 처리
const getEquipmentImage = (item) => {
  const isSelected = selectedEquipment.value?.equipmentId === item.equipmentId
  const baseName = (item.equipmentImg || 'default').replace('_gray.png', '').replace('_blue.png', '')
  const color = isSelected ? 'blue' : 'gray'
  return `http://localhost:8080/upload/equipment/${baseName}_${color}.png`
}

// 6. 초기 로딩
onMounted(async () => {
  await fetchEquipments()
  console.log("장비 목록 확인:", originalEquipmentList.value)
  originalEquipmentList.value.forEach(item => {
    console.log(`${item.equipmentName} → tags: `, item.tags)
  })
})

</script>


<style scoped>
.fixed-footer {
  position: fixed;
  bottom: 0;
  left: 50%; /* 화면 기준 중앙 정렬 */
  transform: translateX(-50%); /* 정확히 가운데 */
  width: 100%;
  height: 66px;
  max-width: 500px; /* ✅ app-wrapper와 동일한 최대 너비 */
  padding: 12px 12px 24px;
  background-color: white;
  z-index: 999;
}

.next-btn {
  width: 100%;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0px !important;
  margin: 0px !important;
  height: 42px;
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
.header-title {
  font-weight: bold;
  font-size: 16px;
  margin: 0 auto;
}
.back-btn {
  background: none;
  border: none;
  padding: 0;
}
.equipment-select-container {
  padding: 0px 0px 100px !important;
  width: 500px !important;
  height: 917px;
  margin: 0 0;
}
.content-wrapper {
  padding: 0px 0px 0px;
  width: 500px;
  max-width: 500px;
  margin: 0 0;
}



.search-input {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 8px;
  margin-bottom: 20px;
}

.equipment-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}
.card-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 열 2개 고정 */
  gap: 60px 20px;
  padding: 60px 0px 40px;
  width: 100%;
  box-sizing: border-box;
  justify-items: center;
}

.equipment-card {
  width: 140px;
  height: 200px;
  border: 2px solid #ddd;
  border-radius: 16px;
  background-color: white;
  overflow: hidden;
  cursor: pointer;
  transition: border 0.3s, background-color 0.3s;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.equipment-card.selected {
  border: 2px solid #006FFD;
  background-color: #eaf2ff;
}

.equipment-card img {
  width: 80px;
  height: 80px;
  object-fit: contain;
  margin-bottom: 10px;
}

.image-container {
  width: 100%;
  height: 100px;
  padding: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.image-container img {
  width: 64px;
  height: 64px;
  object-fit: contain;
}
.text-container {
  height: 80px;
  width: 100%;
  text-align: center;
  padding: 12px 0px;
  background-color: #f9f9f9;
}
.equipment-card.selected .text-container {
  background-color: #006FFD;
  color: white;
}
.equipment-tags {
  font-size: 13px;
  color: #666;
  margin-bottom: 4px;
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  justify-content: center;
}
.equipment-tags span {
  background-color: #f0f0f0;
  padding: 2px 6px;
  border-radius: 12px;
  font-size: 12px;
  color: #333;
}

.equipment-card.selected .equipment-tags {
  color: #cce0ff;
}
.equipment-card.selected .equipment-tags span {
  background-color: #cce0ff;
  color: #003e9c;
}
.equipment-card.selected .equipment-name {
  color: white;
}
.equipment-name {
  font-size: 15px;
  font-weight: bold;
  color: #555;
}

.equipment-title {
  font-size: 16px;
  font-weight: bold;
}

.next-btn {
  width: 100%;
  padding: 12px;
  background-color: #006ffd;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  margin-top: 20px;
}

.next-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.step-indicator {
  padding: 80px 4px 40px;
  display: flex;
  flex-direction: row; /* ← row로 변경 */
  align-items: center;
  justify-content: space-around; /* 또는 center / space-between 등 */
}


.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.circle {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #ddd;
  color: #999;
  font-weight: bold;
  font-size: 16px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.step.active .circle {
  background-color: #006ffd;
  color: white;
}

.label {
  font-size: 14px;
  color: #ccc;
  margin-top: 6px;
}


.step.active .label::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 50%;
  transform: translateX(-50%);
  width: 80%;
  height: 2px;
  background-color: #006ffd;
  border-radius: 1px;
}
.step .circle {
  width: 32px;
  height: 32px;
  line-height: 32px;
  border-radius: 50%;
  background-color: #ddd;
  color: #999;
  font-weight: bold;
  margin: 0 auto;
}


.step .label {
  margin-top: 4px;
  font-size: 14px;
}

.step.active .label {
  color: black;
  font-weight: 500;
}
.search-wrapper {
  position: relative;
  margin-bottom: 20px;
  height: 44px;
}
.search-icon-wrapper {
  position: absolute;
  left: 14px;
  top: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 44px;
}

.search-icon-wrapper i {
  font-size: 20px;
  color: #888;
  pointer-events: none;
  display: flex;
  margin-top: 38px;
}

.search-icon {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 18px;
  color: #888;
  pointer-events: none;
}

.search-input {
  width: 100%;
  height: 100%;
  padding: 0 16px 0 44px; /* ← 왼쪽에 아이콘 들어올 자리 확보 */
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 999px;
  outline: none;
  box-sizing: border-box;
  transition: border 0.2s;
  margin: 20px 0px;
}

.search-input:focus {
  border-color: #006ffd;
}

</style>
