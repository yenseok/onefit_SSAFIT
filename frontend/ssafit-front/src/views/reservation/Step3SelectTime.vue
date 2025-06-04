<template>
   <div id="app-wrapper" class="app-wrapper">
  <div class="container py-4">
    <header class="header">
      <button class="back-btn" @click="goBackToStep2">
        <i class="bi bi-chevron-left text-primary fs-4"></i>
      </button>
      <h5 class="header-title">운동기구 예약</h5>
    </header>
    <div class="d-flex justify-content-between text-center step-header-wrapper">
      <div class="w-100 step-box">
    <div :class="['circle', { active: currentStep === 1 }]">1</div>
    <div :class="['step-label', { 'active-label': currentStep === 1 }]">날짜 선택</div>
  </div>
  <div class="w-100 step-box">
    <div :class="['circle', { active: currentStep === 2 }]">2</div>
    <div :class="['step-label', { 'active-label': currentStep === 2 }]">운동기구 선택</div>
  </div>
  <div class="w-100 step-box">
    <div :class="['circle', { active: currentStep === 3 }]">3</div>
    <div :class="['step-label', { 'active-label': currentStep === 3 }]">시간 선택</div>
  </div>
</div>



    <h4 class="fw-bold mb-3">
      {{ formattedDate }}<br />
      <span class="text-primary">{{ equipmentName }}</span> 사용 시간을 선택해 주세요.
    </h4>

    <div
  v-for="(group, hour) in groupedTimes"
  :key="hour"
  class="hour-group"
>
  <h5 class="fw-bold time-header">{{ getTimePeriodLabel(hour) }}</h5>
  <div class="d-flex flex-wrap gap-2">
    <button
  v-for="time in group"
  :key="time"
  class="btn"
  :disabled="reservedTimes.includes(time) || disabledTimes.includes(time)"
  :class="{
    'btn-primary': selectedTime === time,
    'btn-outline-primary': !selectedTime || selectedTime !== time,
    'disabled bg-light text-secondary border-secondary': reservedTimes.includes(time) || disabledTimes.includes(time)
  }"
  @click="selectTime(time)"
>
  {{ time }}
</button>

  </div>
</div>


    <footer class="fixed-footer">
      <button class="btn btn-primary reservation-btn" @click="submitReservation">
        운동기구 예약하기
      </button>
    </footer>
</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/api'
import { useRouter } from 'vue-router'
const router = useRouter()


const selectedTime = ref('')
const times = ref([])
const reservedTimes = ref([])
const groupedTimes = ref({})
const equipmentName = sessionStorage.getItem('equipmentName') || '기구'
const equipmentId = sessionStorage.getItem('equipmentId')
const rawDate = sessionStorage.getItem('reservation-date')
const disabledTimes = ref([])
const userId = sessionStorage.getItem('user-id')
const formattedDate = new Date(rawDate).toLocaleDateString('ko-KR', {
  year: 'numeric',
  month: 'long',
  day: 'numeric',
})
const currentStep = 3 // ← 현재 몇 번째 단계인지에 따라 1, 2, 3으로 조정
// 아래에 추가
const goBackToStep2 = () => {
  router.push({ name: 'equipment-select' })  // 라우터 이름에 맞게 수정
}

const selectTime = (time) => {
  selectedTime.value = time
}

const generateTimes = () => {
  const all = []
  const now = new Date()
  const isToday = new Date(rawDate).toDateString() === now.toDateString()

  for (let h = 10; h <= 22; h++) {
    for (let m = 0; m < 60; m += 10) {
      const hour = String(h).padStart(2, '0')
      const minute = String(m).padStart(2, '0')
      const timeStr = `${hour}:${minute}`
      all.push(timeStr)

      if (isToday) {
        const timeDate = new Date(rawDate)
        timeDate.setHours(h, m, 0, 0)
        if (timeDate <= now) disabledTimes.value.push(timeStr)  // ✅ 현재보다 이전 시간 비활성화
      }
    }
  }

  times.value = all
  const grouped = {}
  all.forEach(time => {
    const hour = time.split(':')[0]
    if (!grouped[hour]) grouped[hour] = []
    grouped[hour].push(time)
  })
  groupedTimes.value = grouped
}

// 시간대에 따라 '오전/오후' 텍스트 반환
const getTimePeriodLabel = (hour) => {
  const h = parseInt(hour)
  const period = h < 12 ? '오전' : '오후'
  const displayHour = h <= 12 ? h : h - 12
  return `${period} ${displayHour}시`
}


const submitReservation = async () => {
  if (!selectedTime.value) return alert('시간을 선택하세요.')
  const payload = {
    userId: Number(userId),
    equipmentId: Number(equipmentId),
    reservationDate: rawDate,
    reservationTime: selectedTime.value
  }
  try {
    await api.post('http://localhost:8080/reservations', payload, {
      headers: { Authorization: `Bearer ${sessionStorage.getItem('access-token')}` }
    })
    alert('예약이 완료되었습니다.')
    location.href = '/'
  } catch (err) {
    console.error(err)
    alert('예약 실패')
  }
}

onMounted(async () => {
  generateTimes()
  try {
const res = await api.get(`/reservations/${equipmentId}/times`, {
  params: { date: rawDate.split('T')[0] }, // 날짜 포맷 yyyy-MM-dd로 자름
  headers: {
    Authorization: `Bearer ${sessionStorage.getItem('access-token')}`
  }
})

    reservedTimes.value = res.data.map(t => t.substring(0, 5))
  } catch (err) {
    console.error('시간 조회 실패', err)
  }
})
</script>


<style scoped>
/* #app-wrapper가 이미 32px padding을 갖고 있다고 가정 */
.container.py-4 {
  padding: 0px 0px 100px !important;
  width: 500px !important;
  max-width: 500px !important;
  margin: 0 auto;
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


.reservation-btn {
  width: 100%;
  max-width: 100%;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
  align-items: center;
  justify-content: center;
}



/* 콘텐츠가 버튼에 가리지 않도록 아래 여백 확보 */
.container {
  padding-bottom: 200px; /* 버튼 높이만큼 공간 확보 */
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
/* 선택된 시간 버튼은 Bootstrap의 btn-primary로 처리됨. 추가 커스텀은 필요 없음 */
/* 시간대 블록 사이에 회색 선 추가 + 세로 간격 조금 더 */
.hour-group {
  height: 120px;
  padding-top: 16px;
  padding-bottom: 16px;
  border-top: 1px solid #ddd;
}

/* 비활성화된 버튼 스타일 보정 */
button:disabled,
button.disabled {
  opacity: 1; /* Bootstrap의 disabled opacity 제거 */
  background-color: #e9ecef !important;
  color: #6c757d !important;
  border-color: #ced4da !important;
  cursor: not-allowed;
}

/* 시간 선택 버튼 공통 스타일 (여백 균일화) */
button.btn {
  min-width: 72px;
  padding: 0.5rem 0.75rem;
  font-weight: 500;
  border-radius: 12px;
}

/* 시간 헤더 스타일 */
/* 시간대 제목 색상 회색으로 변경 */
h4.fw-bold {
  margin: 16px 0px;
  color: black;
  font-weight: 600;
}
h5.fw-bold.time-header{
  margin: 0px 0px 16px;
}

.text-primary {
  font-weight: bold;
}

.step-box {
  padding: 100px 4px 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.circle {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: #ddd;
  color: #999;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  margin-bottom: 4px;
}

.circle.active {
  background-color: #006ffd;
  color: white;
}

.step-label {
  font-size: 14px;
  color: #ccc;
  font-weight: 500;
  border-bottom: none;
}

.step-label.active-label {
  color: black;
  border-bottom: 2px solid #006ffd;
}
.step-header-wrapper {
  margin-top: 20px; /* 상단 헤더와 간격 */
  margin-bottom: 24px; /* 기존 아래 여백 유지 */
}


</style>
