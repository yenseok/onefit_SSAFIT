<template>
  <div id="app-wrapper" class="app-wrapper">
  <div class="signup-page">
    <header class="header">
      <button class="back-btn" @click=router.back()>
        <i class="bi bi-chevron-left text-primary fs-4"></i>
      </button>
      <h5 class="header-title">회원가입</h5>
    </header>
    <div class="signup-container">

      <!-- 단계 인디케이터 -->
      <div class="step-indicator">
        <div class="step" :class="{ active: step === 1 }">
          <div class="circle">1</div>
          <div>개인정보 입력</div>
        </div>
        <div class="step" :class="{ active: step === 2 }">
          <div class="circle">2</div>
          <div>헬스장 등록</div>
        </div>
        <div class="step" :class="{ active: step === 3 }">
          <div class="circle">3</div>
          <div>트레이너 등록</div>
        </div>
      </div>

      <!-- 1단계 -->
      <div v-if="step === 1" class="form-wrapper">
        <div class="form-group">
          <label for="name">이름</label>
          <input id="name" v-model="user.name" placeholder="이름" required />
        </div>
        <div class="form-group">
          <label for="email">이메일</label>
          <input id="email" v-model="user.email" placeholder="name@email.com" required />
          <p :class="['input-guide', emailStatus]">
            <span v-if="emailStatus === 'valid'">올바른 이메일입니다.</span>
            <span v-else-if="emailStatus === 'invalid'">이메일 형식이 올바르지 않습니다.</span>
            <span v-else>이메일 형식으로 입력해주세요. (예: name@email.com)</span>
          </p>
        </div>
        <div class="form-group">
          <label for="phone">전화번호</label>
          <input id="phone" v-model="user.phone" placeholder="전화번호" required />
        </div>
        <div class="form-group">
          <label for="nick">아이디</label>
          <input id="nick" v-model="user.nickName" placeholder="아이디" required />
          <p :class="['input-guide', nickStatus]">{{ nickMessage }}</p>
        </div>
        <div class="form-group">
          <label for="pw">비밀번호</label>
          <input id="pw" v-model="user.password" type="password" placeholder="비밀번호" required />
          <p :class="['input-guide', pwStatus]">{{ pwMessage }}</p>
        </div>
        <div class="form-group">
          <label for="pw-check">비밀번호 확인</label>
          <input id="pw-check" v-model="passwordCheck" type="password" placeholder="비밀번호 확인" required />
          <p :class="['input-guide', pwCheckStatus]">{{ pwCheckMessage }}</p>
        </div>
        <footer class="fixed-footer" v-if="step === 1">
          <button class="next-btn" @click="goToStep2">다음</button>
        </footer>
      </div>

      <!-- 2단계 -->
      <div v-if="step === 2" class="form-wrapper">
        <p class="subtitle bold">다니고 있는 헬스장을 등록해 주세요.</p>
        <div class="search-wrapper">
          <i class="bi bi-search search-icon"></i>
          <input type="text" v-model="gymKeyword" @input="searchGyms" placeholder="검색" />
          <ul v-if="gyms.length > 0" class="dropdown gym-dropdown">
            <li v-for="gym in gyms" :key="gym.gymId" @click="selectGym(gym)">
              <div class="gym-name">{{ gym.gymName }}</div>
              <div class="gym-address">{{ gym.gymAddress }}</div>
            </li>
          </ul>
        </div>
        <footer class="fixed-footer">
          <button class="next-btn" @click="goToStep3">다음</button>
        </footer>
      </div>

      <!-- 3단계 -->
<!-- 3단계 -->
<div v-if="step === 3" class="form-wrapper">
  <p class="subtitle bold">트레이너를 등록해 주세요.</p>
  <div class="search-wrapper">
    <i class="bi bi-search search-icon"></i>
    <input
      type="text"
      v-model="trainerKeyword"
      @input="searchTrainers"
      placeholder="검색"
    />
    <!-- 반드시 search-wrapper 안쪽에 둬야 함 -->
    <ul v-if="filteredTrainers.length > 0" class="dropdown trainer-dropdown">
      <li
        v-for="trainer in filteredTrainers"
        :key="trainer.trainerId"
        @click="selectTrainer(trainer)"
      >
        <div class="trainer-name">{{ trainer.trainerName }}</div>
      </li>
    </ul>
  </div>

  <div class="fixed-bottom">
    <button class="secondary full-width" @click="submitSignup">건너뛰기</button>
    <button class="primary full-width" @click="submitSignup">회원가입</button>
  </div>
</div>

    </div>
  </div>
</div>
</template>


<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import api from '@/api/api'
import router from '@/router'


const step = ref(1)
const passwordCheck = ref('')
const emailError = ref('')
const nickError = ref('')
const pwError = ref('')
const pwCheckError = ref('')


const user = reactive({
  name: '',
  email: '',
  phone: '',
  nickName: '',
  password: '',
  gymId: '',
  trainerId: null,
  profileImage: '',
  role: 'USER'
})

// 조건 검사 후 색상 클래스 반환
const emailStatus = computed(() => {
  if (!user.email) return ''
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return regex.test(user.email) ? 'valid' : 'invalid'
})
const nickStatus = computed(() => {
  if (!user.nickName) return ''
  return user.nickName.length >= 6 ? 'valid' : 'invalid'
})
const pwStatus = computed(() => {
  if (!user.password) return ''
  const regex = /^(?=.*[a-z])(?=.*[A-Z]).{8,}$/
  return regex.test(user.password) ? 'valid' : 'invalid'
})

const nickMessage = computed(() => {
  if (!user.nickName) return '아이디는 6자 이상이어야 합니다.'
  return user.nickName.length >= 6 ? '사용 가능한 아이디입니다.' : '아이디는 6자 이상이어야 합니다.'
})

const pwMessage = computed(() => {
  if (!user.password) return '비밀번호는 대소문자 포함 8자 이상이어야 합니다.'
  const regex = /^(?=.*[a-z])(?=.*[A-Z]).{8,}$/
  return regex.test(user.password) ? '안전한 비밀번호입니다.' : '비밀번호는 대소문자 포함 8자 이상이어야 합니다.'
})

const pwCheckStatus = computed(() => {
  if (!passwordCheck.value) return ''
  return passwordCheck.value === user.password ? 'valid' : 'invalid'
})
const pwCheckMessage = computed(() => {
  if (!passwordCheck.value) return '비밀번호를 한 번 더 입력해주세요.'
  return passwordCheck.value === user.password ? '비밀번호가 일치합니다.' : '비밀번호가 일치하지 않습니다.'
})

const goBack = () => {
  if (step.value > 1) {
    step.value -= 1
  } else {
    router.back()  // 1단계에서는 실제 뒤로가기 (예: 로그인 페이지)
  }
}


const gyms = ref([])
const trainers = ref([])



const goToStep2 = () => {
  // 에러 초기화
  emailError.value = ''
  nickError.value = ''
  pwError.value = ''
  pwCheckError.value = ''

  let valid = true

  if (!user.email.trim()) {
    emailError.value = '이메일을 입력해주세요.'
    valid = false
  } else {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(user.email)) {
      emailError.value = '올바른 이메일 형식이 아닙니다. 예: name@email.com'
      valid = false
    }
  }

  if (!user.nickName.trim()) {
    nickError.value = '아이디를 입력해주세요.'
    valid = false
  } else if (user.nickName.length < 6) {
    nickError.value = '아이디는 6자 이상이어야 합니다.'
    valid = false
  }

  const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z]).{8,}$/
  if (!user.password.trim()) {
    pwError.value = '비밀번호를 입력해주세요.'
    valid = false
  } else if (!passwordRegex.test(user.password)) {
    pwError.value = '대소문자 포함 8자 이상이어야 합니다.'
    valid = false
  }

  if (passwordCheck.value !== user.password) {
    pwCheckError.value = '비밀번호가 일치하지 않습니다.'
    valid = false
  }


  if (valid) {
    step.value = 2
  }
}



const goToStep3 = () => {
  console.log("선택된 gymId:", user.gymId)
  if (!user.gymId) {
    alert('헬스장을 선택해주세요.')
    return
  }
  step.value = 3
}

const submitSignup = async () => {
  try {
    await api.post('/auth/signup', user)
    router.push({  name: 'signup-success' })
  } catch (err) {
    alert('회원가입 실패: ' + err.response?.data || err.message)
  }
}

const gymKeyword = ref('')
const selectedGymName = ref('')

// 헬스장 키워드로 검색
const searchGyms = async () => {
  if (!gymKeyword.value.trim()) {
    gyms.value = []
    return
  }

  try {
    const response = await api.get('/gyms', {
  params: { keyword: gymKeyword.value }
})
    gyms.value = response.data
  } catch (err) {
    console.error('헬스장 검색 실패', err)
  }
}

// 헬스장 선택
const selectGym = (gym) => {
  user.gymId = Number(gym.gymId)
  selectedGymName.value = gym.gymName
  gymKeyword.value = gym.gymName
  gyms.value = []
}

const trainerKeyword = ref('')
const selectedTrainerName = ref('')
const filteredTrainers = ref([])

const searchTrainers = async () => {
  console.log("트레이너 검색 조건:", trainerKeyword.value, user.gymId) // 디버깅용 로그

  if (!trainerKeyword.value.trim() || !user.gymId) {
    filteredTrainers.value = []
    return
  }

  try {
    const res = await api.get('/trainers', {
      params: { keyword: trainerKeyword.value, gymId: user.gymId }
    })
    filteredTrainers.value = res.data
  } catch (err) {
    console.error('트레이너 검색 실패', err)
  }
}




const selectTrainer = (trainer) => {
  user.trainerId = trainer.trainerId
  selectedTrainerName.value = trainer.trainerName
  trainerKeyword.value = trainer.trainerName  // 검색창에도 채우기
  filteredTrainers.value = [] // 드롭다운 숨기기
}
</script>

<style scoped>

/* 로고 스타일은 유지 */
.signup-inner {
  width: 100%;
  max-width: 400px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  padding: 0 16px;
  box-sizing: border-box;
}

.signup-container {
  padding: 0px 0px 0px !important;
  width: 500px !important;
  margin: 0 0;
}


/* signup-page: 전체 화면 */
.signup-page {
  padding: 10px 0px 100px !important;
  width: 500px !important;
  height: 1000px;
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
  padding: 0;
  width: auto;
  height: auto;
}



h2 {
  text-align: center;
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 12px;
}

/* 단계 인디케이터 */
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



/* 입력 form 래퍼 */
.form-wrapper {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 16px;
  overflow: visible;
  padding: 0px 40px 100px;
}

/* 각 form 그룹 */
.form-group {
  display: flex;
  flex-direction: column;
  gap: 2px;
  margin-bottom: 0;
}

/* 라벨 */
label {
  font-size: 14px;
  color: #ccc;
  margin-top: 6px;
}

/* input */
input {
  width: 100%;
  padding: 14px 16px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 12px;
  outline: none;
  background-color: #fafafa;
  box-sizing: border-box;
}
input:focus {
  border-color: #006ffd;
  background-color: white;
}
input::placeholder {
  color: #aaa;
}

/* 버튼 */
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
button.primary {
  margin-top: 12px;
}
button.secondary {
  background-color: #eee;
  color: #333;
  margin-top: 40px;
}
button.secondary:hover {
  background-color: #ddd;
}

/* 설명 텍스트 */
.subtitle.bold {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 12px;
  text-align: left;
}
.input-guide {
  font-size: 13px;
  margin-top: 2px;
  padding-left: 4px;
  color: #aaa;
}
.input-guide.valid {
  color: #4caf50;
}
.input-guide.invalid {
  color: #f44336;
}
.error-message {
  color: #f44336;
  font-size: 13px;
  margin-top: 2px;
  padding-left: 4px;
}

/* 검색 영역 */
.search-wrapper {
  position: relative;
  margin-bottom: 32px;
}
.search-wrapper input {
  width: 100%;
  padding: 12px 16px 12px 36px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 999px;
  background-color: #fff;
}
.search-wrapper .search-icon {
  position: absolute;
  top: 50%;
  left: 12px;
  transform: translateY(-50%);
  font-size: 16px;
  color: #888;
}

/* 드롭다운 */
.gym-dropdown,
.trainer-dropdown {
  list-style: none;
  padding: 0;
  margin: 4px 0 0 0; /*  input 바로 아래에 4px만 간격 */
  border: 1px solid #ccc;
  border-radius: 8px;
  background-color: white;
  color: black;
  max-height: 200px;
  overflow-y: auto;
  position: absolute; /* input 기준 아래에 띄우기 */
  top: 100%;           /*  input 아래로 */
  left: 0;
  width: 100%;
  z-index: 10;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.gym-dropdown li,
.trainer-dropdown li {
  padding: 10px 12px;
  cursor: pointer;
  border-bottom: 1px solid #f2f2f2;
}
.gym-dropdown li:last-child,
.trainer-dropdown li:last-child {
  border-bottom: none;
}
.gym-name,
.trainer-name {
  font-weight: 600;
  font-size: 15px;
}
.gym-address {
  font-size: 13px;
  color: #666;
  margin-top: 2px;
}
.gym-dropdown li:hover,
.trainer-dropdown li:hover {
  background-color: #f8f8f8;
}

/* 상단바 */
.top-bar {
  position: relative;
  padding: 16px 0;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 56px;
}
.back-icon {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 24px;
  color: #006FFD;
  cursor: pointer;
}
.title {
  font-size: 22px;
  font-weight: bold;
  line-height: 1;
  margin: 0;
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
.full-width {
  width: 100%;
}
.fixed-bottom {
  position: fixed;
  bottom: 0;
  left: 50%; /* 화면 기준 중앙 정렬 */
  transform: translateX(-50%); /* 정확히 가운데 */
  width: 100%;
  height: 180px;
  max-width: 500px; /* ✅ app-wrapper와 동일한 최대 너비 */
  padding: 12px 12px 24px;
  background-color: white;
  z-index: 999;
}
</style>
