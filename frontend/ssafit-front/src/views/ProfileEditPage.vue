
<template>
  <div id="app-wrapper" class="app-wrapper">
  <div class="profile-page">
    <div class="profile-wrapper">
      <!-- 상단 바 -->
      <header class="header">
        <button class="back-btn" @click="cancelEdit">
          <i class="bi bi-chevron-left text-primary fs-4"></i>
        </button>
        <h5 class="header-title">프로필</h5>
        <button class="edit-btn" @click="submitEdit">완료</button>
      </header>

      <!-- 프로필 이미지 + 이름 -->
      <div class="profile-section">
        <div class="profile-image-wrapper">
<img :src="fullProfileImage" class="profile-img" />
          <label class="edit-icon">
            <input type="file" accept="image/*" @change="onImageChange" hidden />
            <i class="bi bi-pencil-fill"></i>
          </label>
        </div>
        <div class="profile-name">
          <input v-model="user.name" class="name-input" />
          <div class="nickname">@{{ user.nickName }}</div>
        </div>
      </div>

      <!-- 수정 가능한 항목들 -->
      <div class="profile-items">
        <div class="item">
          <div class="label">이메일</div>
          <input v-model="user.email" type="email" />
        </div>
        <div class="item">
          <div class="label">전화번호</div>
          <input v-model="user.phone" type="tel" />
        </div>

        <!-- 헬스장 자동완성 -->
        <div class="item clickable" @click="goToGymSearch">
          <div class="label">헬스장</div>
          <div class="gym-row">
            <div class="value">{{ user.gymName || '선택 안 됨' }}</div>
            <i class="bi bi-chevron-right icon"></i>
          </div>
        </div>

      </div>
    </div>
  </div>
  </div>
</template>



<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import api from '@/api/api'
import router from '@/router'
import { useRoute } from 'vue-router'

const gyms = ref([])
const route = useRoute()
const userId = Number(route.params.userId)
const token = sessionStorage.getItem('access-token')
const user = ref({
  trainerName: '',
  trainerId: null,
  gymName: '',
  gymId: null,
})

const defaultProfileImage = 'http://localhost:8080/upload/profile/profile_photo.png'
const selectedIndex = ref(-1)
const trainers = ref([])
const selectedTrainerIndex = ref(-1)
const trainerKeyword = ref('')
const filteredTrainers = ref([])
const showTrainerSearch = ref(false)


const goToGymSearch = () => {
  router.push({
    name: 'gym-search',
    query: { from: 'profile-edit', returnToTrainer: true }
  })
}

const goToTrainerSearch = () => {
  if (!user.value.gymId) {
    alert('먼저 헬스장을 선택해주세요.');
    return;
  }
  router.push({
    name: 'trainer-search',
    query: {
      from: 'profile-edit',
      gymId: user.value.gymId
    }
  });
}

const fullProfileImage = computed(() => {
  if (previewImage.value) return previewImage.value
  if (user.value.profileImage?.startsWith('http')) return user.value.profileImage
  if (user.value.profileImage) return `http://localhost:8080${user.value.profileImage}`
  return defaultProfileImage
})


const previewImage = ref('')

const onImageChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    user.value.profileImageFile = file
    const reader = new FileReader()
    reader.onload = () => {
      previewImage.value = reader.result
    }
    reader.readAsDataURL(file)
  }
}



const getUserInfo = async () => {
  try {
    const response = await api.get(`http://localhost:8080/users/${userId}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    Object.assign(user.value, response.data)  // 수정된 부분
  } catch (err) {
    console.error('유저 정보 불러오기 실패', err)
  }
}


const submitEdit = async () => {
  // 1. 트레이너 유효성 검사
  if (user.value.trainerName && !user.value.trainerId) {
    alert(`"${user.value.trainerName}" 트레이너는 선택된 헬스장에 소속되어 있지 않습니다.`)
    return
  }

  // 2. 헬스장도 이름만 입력하고 선택 안 한 경우 방지
  if (user.value.gymName && !user.value.gymId) {
    alert(`"${user.value.gymName}" 헬스장을 정확히 선택해주세요.`)
    return
  }

  try {
    const formData = new FormData()
    formData.append(
      'user',
      new Blob([JSON.stringify(user.value)], { type: 'application/json' })
    )

    if (user.value.profileImageFile) {
      formData.append('file', user.value.profileImageFile)
    }

    await api.put(`http://localhost:8080/users/${userId}`, formData, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })

    alert('수정 완료')
    router.push({ name: 'profile' })
  } catch (err) {
    console.error('수정 실패', err)
    alert('수정 중 오류 발생')
  }
}





const searchTrainers = async () => {
  const keyword = trainerKeyword.value.trim()
  const gymId = user.value.gymId

  if (!keyword || !gymId) {
    filteredTrainers.value = []
    return
  }

  try {
    const res = await api.get('/trainers', {
      params: { keyword, gymId }
    })
    filteredTrainers.value = res.data
  } catch (err) {
    console.error('트레이너 검색 실패:', err)
    filteredTrainers.value = []
  }
}


const moveTrainerDown = () => {
  if (trainers.value.length === 0) return
  selectedTrainerIndex.value = (selectedTrainerIndex.value + 1) % trainers.value.length
}
const moveTrainerUp = () => {
  if (trainers.value.length === 0) return
  selectedTrainerIndex.value =
    (selectedTrainerIndex.value - 1 + trainers.value.length) % trainers.value.length
}
const confirmTrainerSelection = () => {
  if (selectedTrainerIndex.value !== -1) {
    selectTrainer(trainers.value[selectedTrainerIndex.value])
  }
}
const selectTrainer = (trainer) => {
  user.value.trainerId = trainer.trainerId
  user.value.trainerName = trainer.trainerName
  trainerKeyword.value = trainer.trainerName
  filteredTrainers.value = []
  showTrainerSearch.value = false
}

const showTrainerDropdown = computed(() => {
  return (user.value.trainerName ?? '').trim() !== '' && trainers.value.length > 0
})





const gymSuggestions = ref([])


const searchGyms = async () => {
  const keyword = user.value.gymName
  if (keyword === '') {
    gyms.value = []
    selectedIndex.value = -1
    return
  }

  try {
    const res = await axios.get(`http://locahost:8080/gyms?keyword=${keyword}`)
    gyms.value = res.data
    selectedIndex.value = -1
  } catch (err) {
    console.error('헬스장 검색 실패', err)
  }
}

const moveDown = () => {
  if (gyms.value.length === 0) return
  selectedIndex.value = (selectedIndex.value + 1) % gyms.value.length
}

const moveUp = () => {
  if (gyms.value.length === 0) return
  selectedIndex.value =
    (selectedIndex.value - 1 + gyms.value.length) % gyms.value.length
}

const confirmSelection = () => {
  if (selectedIndex.value !== -1) {
    selectGym(gyms.value[selectedIndex.value])
  }
}

const selectGym = (gym) => {
  user.value.gymName = gym.gymName
  user.value.gymId = gym.gymId
  gyms.value = []
  selectedIndex.value = -1
}

const showDropdown = computed(() => {
  return user.value.gymName.trim() !== '' && gyms.value.length > 0
})

const cancelEdit = () => {
  router.push({ name: 'profile' })
}

const applySelectedGym = () => {
  const saved = sessionStorage.getItem('selected-gym')
  if (saved) {
    const gym = JSON.parse(saved)
    user.value.gymId = gym.gymId
    user.value.gymName = gym.gymName
    sessionStorage.removeItem('selected-gym')
  }
}


const applySelectedTrainer = () => {
  const saved = sessionStorage.getItem('selected-trainer')
  if (saved) {
    const trainer = JSON.parse(saved)
    user.value.trainerId = trainer.trainerId
    user.value.trainerName = trainer.trainerName
    sessionStorage.removeItem('selected-trainer')
  }
}

watch(
  () => route.fullPath,
  () => {
    applySelectedGym()
    applySelectedTrainer()
  }
)
watch(() => user.gymId, (newGymId, oldGymId) => {
  if (newGymId !== oldGymId) {
    user.trainerId = null
    user.trainerName = ''
    sessionStorage.removeItem('selected-trainer')
    sessionStorage.removeItem('selected-gym')  // 이것도 지워야 헷갈림 없음
  }
})


onMounted(async () => {
  await getUserInfo()
  applySelectedGym()
  applySelectedTrainer()
})

</script>

<style scoped>
.profile-page {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  background-color: #fff;
  padding-bottom: 80px;
  box-sizing: border-box;
  height: 1000px;
}

.profile-wrapper {
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

.header-title {
  font-weight: bold;
  font-size: 16px;
  margin: 0 auto;
}

.back-btn,
.edit-btn {
  background: none;
  border: none;
  color: #006ffd;
  font-weight: bold;
  font-size: 14px;
  padding: 4px 8px;
  cursor: pointer;
}

.profile-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 24px 0;
}

.profile-image-wrapper {
  position: relative;
  display: inline-block;
  width: 100px;
  height: 100px;
}

.profile-img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  background-color: #f0f0f0;
}

.edit-icon {
  position: absolute;
  bottom: 0;
  right: 0;
  background-color: #006ffd;
  color: white;
  border-radius: 50%;
  width: 28px;
  height: 28px;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
}

.profile-name {
  margin-top: 12px;
  text-align: center;
}

.name-input {
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 4px 8px;
  font-size: 16px;
  text-align: center;
  width: 150px;
  margin-top: 8px;
}

.nickname {
  color: #888;
  font-size: 14px;
  margin-top: 4px;
}

.profile-items {
  display: flex;
  flex-direction: column;
  border-top: 1px solid #eee;
}

.profile-items .item {
  display: flex;
  flex-direction: column;
  padding: 16px;
  border-bottom: 1px solid #eee;
  font-size: 16px;
}

.profile-items .label {
  margin-bottom: 8px;
  font-weight: bold;
  color: #222;
}

.profile-items input {
  padding: 8px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.item.clickable {
  display: flex;
  flex-direction: column;
  padding: 16px;
  border-bottom: 1px solid #eee;
}

.gym-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.gym-row .value {
  font-size: 14px;
  color: #000;
  flex: 1;
  text-align: left;
}

.gym-row .icon {
  font-size: 20px;
  color: #006FFD;
}
</style>
```
