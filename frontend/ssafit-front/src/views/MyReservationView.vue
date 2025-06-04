<template>
  <div id="app-wrapper" class="app-wrapper">
  <div class="reservation-container">
    <header class="header">
      <button class="back-btn" @click="router.back()">
        <i class="bi bi-chevron-left text-primary fs-4"></i>
      </button>
      <h5 class="header-title">나의 예약</h5>
    </header>

    <!-- 날짜 선택 -->
    <div class="date-row-wrapper">
      <div class="date-row">
        <div
          v-for="(day, index) in weekDates"
          :key="index"
          :class="['date-box', isSelected(day) ? 'selected' : '']"
          @click="selectDate(day)"
        >
          <div>{{ getDayName(day) }}</div>
          <div>{{ getDateNumber(day) }}</div>
        </div>
      </div>
    </div>

    <!-- 오늘의 예약 텍스트 -->
    <div class="today-text">
      <strong>오늘의 예약</strong> <strong>{{ selectedDate }}</strong>
    </div>

    <!-- 예약 카드 -->
    <div v-if="reservations.length > 0" class="card-list">
      <div
        v-for="(res, index) in reservations"
        :key="index"
        class="reservation-card"
      >
        <div class="card-left">
          <div class="image-wrapper">
            <span class="type-label" :class="res.type === 'PT' ? 'pt' : 'equipment'">
              {{ res.type === 'PT' ? 'PT' : '운동 기구' }}
            </span>
            <img
              v-if="res.type === 'EQUIPMENT'"
              class="card-img"
              :src="getEquipmentImage(res)"
              alt="운동기구 이미지"
            />
          </div>
          <div>
            <p class="name">{{ res.name }}</p>
            <p class="time">{{ formatTime(res.time) }}</p>
          </div>
        </div>
        <button class="cancel-btn" @click="cancelReservation(res)">
  예약 취소
</button>

      </div>
    </div>

    <div v-else class="no-reservation">선택한 날짜에 예약이 없습니다.</div>

      </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/api'
import { useRouter } from 'vue-router'

const router = useRouter()
const today = new Date().toISOString().substring(0, 10)
const selectedDate = ref(today)
const reservations = ref([])

const formatTime = (timeStr) => {
  if (!timeStr) return ''
  const [hour, minute] = timeStr.split(':').map(Number)
  const period = hour < 12 ? '오전' : '오후'
  const displayHour = hour % 12 === 0 ? 12 : hour % 12
  return `${period} ${displayHour}시 ${minute.toString().padStart(2, '0')}분`
}

const getEquipmentImage = (res) => {
  if (!res.imageUrl) return ''
  return `http://localhost:8080/upload/equipment/${res.imageUrl}`
}

const getExtendedDates = () => {
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  const dates = []
  for (let i = 0; i <= 20; i++) {
    const d = new Date(today)
    d.setDate(today.getDate() + i)
    dates.push(d.toISOString().substring(0, 10))
  }
  return dates
}


const weekDates = getExtendedDates()

const isSelected = (date) => selectedDate.value === date
const getDayName = (date) => ['일', '월', '화', '수', '목', '금', '토'][new Date(date).getDay()]
const getDateNumber = (date) => new Date(date).getDate()

const selectDate = (date) => {
  selectedDate.value = date
  fetchReservations()
}

const fetchReservations = async () => {
  const userId = sessionStorage.getItem('user-id')
  const token = sessionStorage.getItem('access-token')
  try {
    const response = await api.get(`http://localhost:8080/daily-reservations/user/${userId}`, {
      params: { date: selectedDate.value },
      headers: { Authorization: `Bearer ${token}` }
    })
    reservations.value = response.data
  } catch (err) {
    console.error('예약 조회 실패:', err)
    reservations.value = []
  }
}

const cancelReservation = async (res) => {
  const reservationId = res.reservationId
  const isPT = res.type === 'PT'

  if (!reservationId) {
    console.error('reservationId가 없습니다:', res)
    return
  }

  if (!confirm('정말 예약을 취소하시겠습니까?')) return

  const token = sessionStorage.getItem('access-token')

  try {
    // PT와 EQUIPMENT 각각 다른 경로 사용
    const url = isPT
      ? `/pt/${reservationId}`
      : `/reservations/${reservationId}`

    await api.delete(url, {
      headers: { Authorization: `Bearer ${token}` }
    })

    alert('예약이 취소되었습니다.')
    fetchReservations()
  } catch (err) {
    console.error('예약 취소 실패:', err)
    alert('예약 취소에 실패했습니다.')
  }
}

onMounted(() => {
  fetchReservations()
  setTimeout(() => {
    const boxes = document.querySelectorAll('.date-box')
    const todayIndex = weekDates.findIndex(d => d === today)
    const scrollBox = document.querySelector('.date-row-wrapper')
    const target = boxes[todayIndex]
    if (target && scrollBox) {
      scrollBox.scrollLeft = target.offsetLeft - 10
    }
  }, 100)
})
</script>

<style scoped>
.content-wrapper {
  position: relative;
  padding-top: 60px; /* ✅ date-row의 높이만큼 여백 확보 */
  max-width: 500px;
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
.reservation-container {
  padding: 20px 0px 100px !important;
  width: 500px !important;
  max-width: 500px !important;
  margin: 0 auto;
  height: 850px;
}
.date-row-wrapper {
  overflow-x: auto;
  margin-bottom: 12px;
  padding: 4px 12px;
  box-sizing: border-box;
}
.date-row {
  position: absolute;
  top: 0;
  left: 0;
  display: flex;
  gap: 12px;
  width: 500px;
  padding: 10px 0px;
  padding-left: 10px;
  background-color: white;
  z-index: 10;
  overflow-x: auto;  /* ✅ 내용 넘치면 가로 스크롤 생김 */
}


.date-box {
  min-width: 48px;
  height: 60px;
  text-align: center;
  border-radius: 12px;
  padding: 6px 0;
  font-size: 13px;
  color: #333;
  background-color: #f0f0f0;
  flex-shrink: 0;
}
.date-box.selected {
  background-color: #006FFD;
  color: white;
}
.today-text {
  font-size: 15px;
  margin-bottom: 12px;
  font-weight: bold;
}
.card-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.reservation-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 10px;
  padding: 16px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  min-height: 100px;
}
.card-left {
  display: flex;
  align-items: center;
  gap: 20px;
}
.card-img {
  width: 40px;
  height: 40px;
  object-fit: contain;
}
.image-wrapper {
  position: relative;
  width: 40px;
  height: 60px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
}
.type-label {
  position: absolute;
  top: -10px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 10px;
  font-weight: bold;
  padding: 2px 6px;
  border-radius: 999px;
  background-color: #006FFD;
  color: white;
  white-space: nowrap;
}
.name {
  font-weight: bold;
  margin: 4px 0;
  line-height: 1.4;
}
.time {
  font-size: 13px;
  color: gray;
  margin: 0;
}
.cancel-btn {
  background-color: #006FFD;
  color: white;
  border: none;
  padding: 6px 10px;
  border-radius: 8px;
  font-size: 13px;
  cursor: pointer;
}
.no-reservation {
  margin-top: 20px;
  text-align: center;
  color: gray;
}

</style>
