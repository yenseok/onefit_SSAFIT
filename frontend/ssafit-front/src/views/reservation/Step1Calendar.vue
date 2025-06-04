<template>
<div id="app-wrapper" class="app-wrapper">
    <div class="step-calendar-page">
      <!-- 헤더 -->
      <header class="header">

          <button class="back-btn" @click="router.back()">
            <i class="bi bi-chevron-left text-primary fs-4"></i>
          </button>
          <h5 class="header-title">운동기구 예약</h5>

      </header>

      <!-- 본문 -->
      <div class="content-wrapper">
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

        <footer class="fixed-footer">
          <button class="next-button" @click="goToNextStep">
            운동기구 선택하러 가기
          </button>
        </footer>
      </div>
    </div>
</div>
</template>


<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import VueDatePicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'

const selectedDate = ref(new Date())
const dateFormat = 'yyyy-MM-dd'
const router = useRouter()
const currentStep = 1

const goToNextStep = () => {
  sessionStorage.setItem('reservation-date', selectedDate.value.toISOString())
  router.push({ name: 'equipment-select' })
}
</script>

<style scoped>
:deep(.dp__menu) {
  left: 50% !important;
  transform: translateX(-50%) !important;
}


.step-calendar-page {
  padding: 0px 0px 100px !important;
  width: 500px !important;
  max-width: 500px !important;
  margin: 0 auto;
}


/* 모든 내용을 제한된 너비로 묶어줌 */
.content-wrapper {
  overflow-x: auto;
  top: 0px;
  margin-bottom: 12px;
  padding: 4px 12px 80px;
  box-sizing: border-box;
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

/* step indicator */
.step-indicator {
  padding: 80px 4px 80px;
  display: flex;
  flex-direction: row; /* ← row로 변경 */
  align-items: center;
  justify-content: space-around;
}

.step {
  text-align: center;
  color: #ccc;
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

.step.active .circle {
  background-color: #006FFD;
  color: white;
}

.step .label {
  margin-top: 4px;
  font-size: 14px;
}

.step.active .label {
  color: black;
  font-weight: 500;
}

.step.active {
  color: #006FFD;
  font-weight: bold;
  border-bottom: 2px solid #006FFD;
}

.dp__main {
  padding: 0px 0px 30px;
}

.dp__instance_calendar {
  width: 400px;
  height: 400px;
}
/* 달력 가운데 정렬 */
.calendar-container {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
  width: 100%;
}

.calendar-container :deep(.dp__main) {
  margin: 0 auto;
  display: flex;
  justify-content: center;
}


/* 버튼 정렬 */
.button-wrapper {
  display: flex;
  justify-content: center;
}

.next-button {
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
  background-color: #006FFD;
  border: 0cm;
  color: white;
}
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
</style>
