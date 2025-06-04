<template>
  <div id="app-wrapper" class="app-wrapper">
  <div class="pt-reservation-container">
    <!-- 상단 바 -->
    <header class="header border-bottom">
      <div class="d-flex align-items-center justify-content-between">
        <h5 class="header-title">PT 예약 - {{ trainerName }}</h5>
        <div class="profile-icon">
          <i class="bi bi-person-circle fs-4"></i>
        </div>
      </div>
    </header>

    <!-- 날짜 선택 (달력 스타일) -->
    <main>
      <div class="calendar-container">
        <VueDatePicker
          v-model="selectedDate"
          :enable-time-picker="false"
          inline
          auto-apply
          :hide-navigation-buttons="false"
          :format="dateFormat"
          :teleport="false"
          locale="ko"
          :week-start="0"
          :min-date="new Date()"
        />
      </div>

      <!-- 시간 선택 -->
      <div class="time-grid">
        <button
          v-for="time in timeOptions"
          :key="time"
          :disabled="reservedTimes.includes(time)"
          :class="[
            'time-btn',
            {
              selected: selectedTime === time,
              reserved: reservedTimes.includes(time),
            },
          ]"
          @click="selectedTime = time"
        >
          {{ time }}
        </button>
      </div>
    </main>
    <!-- 예약 버튼 -->
    <button class="reserve-btn" :disabled="!selectedDate || !selectedTime" @click="reservePT">
      PT 예약하기
    </button>
  </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/api'
import VueDatePicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'

const router = useRouter()
const selectedDate = ref(null)
const selectedTime = ref('')
const reservedTimes = ref([])
const trainerName = ref('')

const timeOptions = [
  '08:00',
  '09:00',
  '10:00',
  '11:00',
  '12:00',
  '13:00',
  '14:00',
  '15:00',
  '16:00',
  '17:00',
  '18:00',
  '19:00',
  '20:00',
  '21:00',
  '22:00',
]

const fetchUserTrainerName = async () => {
  const token = sessionStorage.getItem('access-token')
  let userId = sessionStorage.getItem('user-id')

  if (!userId && token) {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]))
      userId = payload.sub
      sessionStorage.setItem('user-id', userId)
    } catch (e) {
      console.error('JWT 파싱 실패', e)
    }
  }

  if (!userId || !token) return

  try {
    const res = await api.get(`http://localhost:8080/users/${userId}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
    trainerName.value = res.data.trainerName || '미지정'
  } catch (err) {
    console.error('트레이너 이름 불러오기 실패:', err)
  }
}

const fetchReservedTimes = async () => {
  const token = sessionStorage.getItem('access-token')
  if (!selectedDate.value || !trainerName.value || !token) return

  const formatted = selectedDate.value.toISOString().split('T')[0]
  try {
    const { data } = await api.get(`http://localhost:8080/pt/trainer/reserved-times`, {
      params: {
        trainerName: trainerName.value,
        date: formatted,
      },
      headers: { Authorization: `Bearer ${token}` },
    })
    reservedTimes.value = data
  } catch (err) {
    console.error('예약 시간 불러오기 실패:', err)
  }
}

const reservePT = async () => {
  const userId = sessionStorage.getItem('user-id')
  const token = sessionStorage.getItem('access-token')

  try {
    await api.post(
      'http://localhost:8080/pt',
      {
        userId,
        trainerName: trainerName.value,
        ptDate: selectedDate.value.toISOString().split('T')[0],
        ptTime: selectedTime.value,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      },
    )

    alert('예약이 완료되었습니다!')
    router.push('/my-reservations')
  } catch (err) {
    if (err.response?.status === 409) {
      alert('이미 예약된 시간입니다.')
    } else {
      alert('예약에 실패했습니다.')
    }
  }
}

watch(selectedDate, fetchReservedTimes)
onMounted(fetchUserTrainerName)

const dateFormat = (date) => {
  const yyyy = date.getFullYear()
  const mm = String(date.getMonth() + 1).padStart(2, '0')
  const dd = String(date.getDate()).padStart(2, '0')
  return `${yyyy}년 ${mm}월 ${dd}일`
}
</script>

<style scoped>
main {
  top: 60px;
  padding-bottom: 70px !important; /* TheFooterNav 높이 고려한 여백 */
  width: 100%;
  max-width: 500px;
  min-width: 500px;
  margin: 0 auto;
}
.pt-reservation-container {
  padding: 10px 0px 100px !important;
  width: 500px !important;
  height: 800px;
  margin: 0 auto;
  top: 0;
}
.header {
  position: fixed;
  top: 56px;
  z-index: 1000;
  width: 500px;
  background-color: white;
  display: flex;
  padding: 12px 16px;
  border-bottom: 1px solid #ddd;
}
.header-title {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  font-weight: bold;
  font-size: 16px;
  margin: 0;
}
.back-btn {
  background: none;
  border: none;
}
.profile-icon {
  width: 24px;
}
.calendar-container {
  display: flex;
  justify-content: center;
  align-items: center; /* 필요시 수직도 중앙 */
  margin: 24px auto;
  width: 100%;
}
:deep(.dp__main) {
  width: auto;
  max-width: 400px;
}
:deep(.dp__month_year_row) {
  background-color: #0d6efd !important;
  color: white !important;
  font-weight: bold;
  font-size: 16px;
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
  padding: 8px 12px;
  justify-content: center;
  gap: 12px;
}

:deep(.dp__month_year_select) {
  background-color: white !important;
  color: #8f9098 !important;
  font-weight: 600;
  border: none;
  outline: none;
  font-size: 15px;
  cursor: pointer;
}
:deep(.dp__calendar_header) {
  background-color: white !important;
}

:deep(.dp__calendar_header_item) {
  color: #0d6efd !important;
  font-weight: 600;
}
:deep(.dp__month_year_select:hover) {
  text-decoration: underline;
}

:deep(.dp__active_date) {
  background-color: #0d6efd !important;
  color: white !important;
}
.time-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  margin-bottom: 24px;
  padding: 0 16px;
}
.time-btn {
  padding: 10px;
  border: 1.5px solid #0d6efd;
  border-radius: 12px;
  background: white;
  color: #0d6efd;
  font-weight: 500;
  transition: 0.2s;
}
.time-btn.selected {
  background-color: #0d6efd;
  color: white;
}
.time-btn.reserved {
  background-color: #eee;
  color: #aaa;
  border-color: #ccc;
  text-decoration: line-through;
}
.reserve-btn {
  position: fixed;
  bottom: 16px;
  left: 50%;
  transform: translateX(-50%);
  width: 90%;
  max-width: 500px;
  padding: 12px;
  border: none;
  background-color: #0d6efd;
  color: white;
  font-weight: bold;
  border-radius: 12px;
  font-size: 16px;
  z-index: 1000;
}
.reserve-btn:disabled {
  background-color: #ccc;
  color: white;
}
</style>
