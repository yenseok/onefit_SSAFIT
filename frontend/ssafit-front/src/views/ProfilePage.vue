<template>
  <div class="profile-page">
    <div class="profile-wrapper">
      <header class="header">
        <button class="back-btn" @click="router.push({ name: 'home' })">
          <i class="bi bi-chevron-left text-primary fs-4"></i>
        </button>
        <h5 class="header-title">프로필</h5>
        <button class="edit-btn" @click="goToEdit">수정</button>
      </header>

      <!-- 프로필 이미지 + 이름 -->
      <div class="profile-section">
        <div class="profile-image">
          <img :src="getProfileImageUrl()" />
        </div>
        <div class="profile-name">
          <div class="name">{{ user.name }}</div>
          <div class="nickname">@{{ user.nickName }}</div>
        </div>
      </div>

      <!-- 항목들 -->
      <div class="profile-items">
        <div class="item">
          <div class="label">이메일</div>
          <div class="value">{{ user.email }}</div>
        </div>
        <div class="item">
          <div class="label">전화번호</div>
          <div class="value">{{ user.phone }}</div>
        </div>
        <div class="item clickable" @click="goToChangePassword">
          <div class="label">비밀번호 변경</div>
          <i class="bi bi-chevron-right icon"></i>
        </div>
        <div class="item">
          <div class="label">헬스장</div>
          <div class="value">{{ user.gymName }}</div>
        </div>
        <div class="item">
          <div class="label">트레이너</div>
          <div class="value">{{ user.trainerName || '미지정' }}</div>
        </div>
        <div class="item clickable" @click="goToMyPosts">
          <div class="label">내 글 보기</div>
          <i class="bi bi-chevron-right icon"></i>
        </div>
        <div class="item clickable danger" @click="deleteAccount">
          <div class="label">회원 탈퇴</div>
          <i class="bi bi-chevron-right icon"></i>
        </div>
        <div class="item clickable" @click="logout">
          <div class="label">로그아웃</div>
          <i class="bi bi-chevron-right icon"></i>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api/api'
import router from '@/router'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const user = ref({})

// 기본 이미지 (절대 URL)
const defaultProfileImage = 'http://localhost:8080/upload/profile/profile_photo.png'

// 사용자 ID 복원
let userId = Number(sessionStorage.getItem('user-id'))
const token = sessionStorage.getItem('access-token')

if (!userId && token) {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    userId = Number(payload.sub)
    sessionStorage.setItem('user-id', payload.sub)
  } catch (e) {
    console.error('JWT 파싱 실패', e)
  }
}

// 유저 정보 조회
const getUserInfo = async () => {
  if (!userId || isNaN(userId)) {
    alert('로그인이 필요합니다.')
    router.push({ name: 'login' })
    return
  }
  try {
    const response = await api.get(`http://localhost:8080/users/${userId}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    user.value = response.data
  } catch (err) {
    if (err.response?.status === 403) {
      alert('접근 권한이 없습니다.')
      router.push({ name: 'login' })
    } else {
      console.error('유저 정보 조회 실패', err)
    }
  }
}

// 프로필 이미지 주소 반환
const getProfileImageUrl = () => {
  const img = user.value.profileImage
  if (!img) return defaultProfileImage
  return img.startsWith('http') ? img : `http://localhost:8080${img}`
}


// 라우팅 핸들러
const goToEdit = () => router.push({ name: 'profile-edit', params: { userId } })
const goToChangePassword = () => router.push({ name: 'change-password', params: { userId } })
const goToMyPosts = () => router.push({ name: 'my-posts' })

// 로그아웃
const logout = () => {
  if (confirm('정말 로그아웃하시겠습니까?')) {
    authStore.logout()
  }
}

// 회원탈퇴
const deleteAccount = async () => {
  if (!confirm('정말 탈퇴하시겠습니까?')) return
  try {
    await api.delete(`http://localhost:8080/users/${userId}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    alert('회원 탈퇴 완료')
    logout()
  } catch (err) {
    alert('회원 탈퇴 실패')
    console.error(err)
  }
}

onMounted(getUserInfo)
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
.profile-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 100px 0px 66px;
}
.profile-image img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}
.profile-name {
  margin-top: 12px;
  text-align: center;
}
.profile-name .name {
  font-size: 18px;
  font-weight: bold;
}
.profile-name .nickname {
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
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #eee;
  font-size: 16px;
  height: 66px;
}
.profile-items .label {
  color: #222;
}
.profile-items .value {
  color: #888;
  font-size: 14px;
}
.profile-items .clickable {
  cursor: pointer;
}
.profile-items .danger {
  color: #f44336;
  font-weight: bold;
}
.item.clickable .icon {
  font-size: 18px;
  color: #006FFD;
}
.item.danger .icon {
  color: #999 !important;
}
@media screen and (max-width: 768px) {
  .profile-image img {
    width: 90px;
    height: 90px;
  }
  .profile-name .name {
    font-size: 16px;
  }
  .profile-items .item {
    padding: 12px;
    font-size: 15px;
  }
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
.edit-btn {
  background: none;
  border: none;
  color: #006ffd;
  font-weight: bold;
  font-size: 14px;
  padding: 4px 8px;
  cursor: pointer;
}
</style>
