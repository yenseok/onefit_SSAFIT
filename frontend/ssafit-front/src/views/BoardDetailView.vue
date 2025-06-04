<template>
  <div id="app-wrapper" class="app-wrapper">
  <div v-if="board" class="detail-container">
    <!-- 상단 바 -->
    <!-- <header class="header-bar">
      <div
        class="position-relative d-flex align-items-center justify-content-between px-3 py-2 border-bottom"
      >
        <div class="z-1">
          <button class="btn p-0 border-0 bg-transparent" @click="goBack">
            <i class="bi bi-chevron-left text-primary fs-4"></i>
          </button>
        </div>
        <h5 class="m-0 text-center position-absolute start-0 end-0 mx-auto">OnePit</h5>
        <div style="width: 24px"></div>
      </div>
    </header> -->

    <main class="container py-3 pb-5">
      <div class="d-flex justify-content-between align-items-center mb-1">
        <h5 class="fw-bold m-0">{{ board.title }}</h5>
        <div v-if="token && board.userId === currentUserId" class="text-end">
          <i
            class="bi bi-pencil-square text-secondary me-2 fs-5"
            @click="goToEdit(board.boardId)"
          ></i>
          <i class="bi bi-trash text-secondary fs-5" @click="deleteBoard(board.boardId)"></i>
        </div>
      </div>

      <div class="d-flex justify-content-between align-items-center text-muted small mb-2">
        <div>
          <span>{{ board.nickName }}</span> ·
          <span>{{ formatDate(board.createdAt) }} {{ formatTime(board.createdAt) }}</span>
        </div>
        <div class="d-flex align-items-center gap-3">
          <div><i class="bi bi-eye me-1"></i>{{ board.viewCount }}</div>
          <div><i class="bi bi-heart me-1"></i>{{ board.likeCount }}</div>
        </div>
      </div>

      <div class="divider"></div>

      <!-- 이미지 슬라이더 -->
      <div class="slider-container mb-3" v-if="board.imageUrls && board.imageUrls.length">
        <Swiper
          :modules="[Navigation, Pagination]"
          navigation
          pagination
          :slides-per-view="1"
          class="custom-swiper"
        >
          <SwiperSlide v-for="(imgUrl, index) in board.imageUrls" :key="index">
            <img :src="apiBaseUrl + imgUrl" class="slide-image" @error="onImageError" />
          </SwiperSlide>
        </Swiper>
      </div>

      <div class="mb-3 content-text">{{ board.content }}</div>

      <div class="divider"></div>

      <div class="d-flex align-items-center justify-content-center mb-4">
        <button
          class="like-btn btn rounded-pill px-4"
          :class="{ liked: liked }"
          @click="toggleLike"
        >
          <i class="bi bi-heart-fill me-1" :class="{ 'text-primary': liked }"></i>
          {{ board.likeCount }}
        </button>
      </div>

      <div v-if="token" class="comment-form">
        <textarea
          v-model="newComment"
          class="form-control mb-2"
          rows="2"
          placeholder="댓글을 입력하세요"
        ></textarea>
        <button class="btn btn-primary btn-sm float-end" @click="submitComment">댓글 작성</button>
      </div>

      <div class="comments-section mt-4">
        <h6 class="fw-bold mb-2">댓글</h6>
        <div v-for="comment in comments" :key="comment.commentId" class="border rounded p-2 mb-2">
          <div class="d-flex justify-content-between align-items-center text-muted small">
            <span class="fw-bold">{{ comment.nickName }}</span>
            <div v-if="comment.userId === currentUserId">
              <i class="bi bi-pencil-square text-secondary me-2" @click="startEdit(comment)"></i>
              <i class="bi bi-trash text-secondary" @click="deleteComment(comment.commentId)"></i>
            </div>
          </div>

          <div v-if="editTarget !== comment.commentId">
            <div class="comment-content mt-1">{{ comment.content }}</div>
          </div>
          <div v-else>
            <textarea v-model="editContent" class="form-control mb-2" rows="2"></textarea>
            <div class="text-end">
              <button class="btn btn-sm btn-primary me-2" @click="submitEdit(comment.commentId)">
                저장
              </button>
              <button class="btn btn-sm btn-secondary" @click="editTarget = null">취소</button>
            </div>
          </div>

          <div class="text-muted small mt-1">
            {{ formatDate(comment.createdAt) }} {{ formatTime(comment.createdAt) }}
          </div>
        </div>
      </div>
    </main>
  </div>
  </div>
</template>

<script setup lang="ts">
interface Comment {
  commentId: number
  content: string
  createdAt: string
  userId: number
  nickName: string
}

import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api/api'
import { Swiper, SwiperSlide } from 'swiper/vue'
import { Navigation, Pagination } from 'swiper/modules'
import 'swiper/css'
import 'swiper/css/pagination'
import 'swiper/css/navigation'

const route = useRoute()
const board = ref<any>(null)
const comments = ref<Comment[]>([]) // 타입 지정 추가
const router = useRouter()
const newComment = ref('')
const editTarget = ref<number | null>(null)
const editContent = ref('')
const token = sessionStorage.getItem('access-token')
const currentUserId = token ? parseInt(JSON.parse(atob(token.split('.')[1])).sub) : null
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

function startEdit(comment: Comment) {
  editTarget.value = comment.commentId
  editContent.value = comment.content
}
function goBack() {
  router.back()
}
function getFullImageUrl(path: string) {
  return `https://onefit-vue.s3.ap-southeast-2.amazonaws.com${path}`
}

onMounted(async () => {
  const { boardId } = route.params
  const res = await api.get(`/boards/${boardId}`)
  board.value = res.data

  if (token && currentUserId) {
    const likeRes = await api.get(`/boards/${boardId}/like?userId=${currentUserId}`)
    liked.value = likeRes.data
  }

  const commentRes = await api.get(`/comments/board/${boardId}`)
  comments.value = commentRes.data
})

function goHome() {
  router.push('/')
}

function formatDate(dateStr: string) {
  return dateStr?.split('T')[0] || ''
}

function formatTime(dateStr: string) {
  return dateStr ? dateStr.substring(11, 16) : ''
}

function onImageError(event: Event) {
  ;(event.target as HTMLImageElement).src = '/default-thumbnail.png'
}

async function submitComment() {
  const { boardId } = route.params
  if (!newComment.value.trim()) return alert('댓글을 입력해주세요.')

  try {
    await api.post(
      '/comments',
      {
        content: newComment.value,
        boardId: parseInt(boardId as string),
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      },
    )

    newComment.value = ''
    const commentRes = await api.get(`/comments/board/${boardId}`)
    comments.value = commentRes.data
  } catch (error) {
    alert('댓글 작성 실패')
  }
}

async function submitEdit(commentId: number) {
  try {
    await api.put(
      `/comments/${commentId}`,
      {
        content: editContent.value,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      },
    )
    editTarget.value = null
    const { boardId } = route.params
    const commentRes = await api.get(`/comments/board/${boardId}`)
    comments.value = commentRes.data
  } catch (error) {
    alert('댓글 수정 실패')
  }
}

async function deleteComment(commentId: number) {
  if (!confirm('댓글을 삭제하시겠습니까?')) return

  try {
    await api.delete(`/comments/${commentId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    comments.value = comments.value.filter((c) => c.commentId !== commentId)
  } catch (err) {
    alert('댓글 삭제 실패')
  }
}

const liked = ref(false)

// 좋아요 토글
async function toggleLike() {
  if (!token || !currentUserId) {
    alert('로그인 후 이용해주세요.')
    return
  }

  try {
    await api.patch(`/boards/${board.value.boardId}/like?userId=${currentUserId}`, null, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })

    // 좋아요 상태 토글 후 숫자 변경
    liked.value = !liked.value
    board.value.likeCount += liked.value ? 1 : -1
  } catch (err) {
    alert('좋아요 처리 중 오류 발생')
  }
}

function goToEdit(boardId: number) {
  router.push(`/board/edit/${boardId}`)
}

async function deleteBoard(boardId: number) {
  if (!confirm('정말로 게시글을 삭제하시겠습니까?')) return

  try {
    await api.delete(`/boards/${boardId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    alert('게시글이 삭제되었습니다.')
    router.push('/') // 삭제 후 홈으로 이동
  } catch (error) {
    alert('게시글 삭제 실패')
  }
}
</script>

<style scoped>
.slider-container {
  margin-bottom: 20px;
  margin: 10px 0;
}
.container {
  width: 500px;
  margin: 0 0;
  padding: 0px 0px 0px 0px;
  min-height: 100vh;
}
.swiper-slide {
  display: flex;
  justify-content: center;
  align-items: center; /* 수직 정렬 */
  height: 300px; /* Swiper 높이 고정 (필요 시 조정) */
  background-color: #fff;
}

.slide-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain; /* 이미지 비율 유지하며 컨테이너에 맞춤 */
  object-position: center;
  border-radius: 8px;
  background-color: #f5f5f5;
}
.image-scroll-wrapper {
  overflow-x: auto;
  white-space: nowrap;
  -webkit-overflow-scrolling: touch;
}
.header-bar {
  position: sticky;
  top: 0;
  z-index: 1000;
  background-color: white;
  border-bottom: 1px solid #ddd;
}
.post-meta {
  position: relative;
}
.divider {
  border-top: 1px solid #ddd;
  margin: 16px 0;
}

main {
  padding-bottom: 40px !important; /* TheFooterNav 높이 고려한 여백 */
}
.detail-container {
  padding: 0px 0px 100px !important;
  width: 500px !important;
  max-width: 500px !important;
  margin: 0 auto;
}
.title {
  font-size: 24px;
  font-weight: bold;
}
.profile-img {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 8px;
  vertical-align: middle;
}
.info {
  font-size: 14px;
  color: gray;
  margin-bottom: 10px;
}
.slider-container {
  margin: 10px 0;
  overflow-x: auto;
  white-space: nowrap;
}
.slider {
  display: flex;
  gap: 10px;
}
.content-text {
  margin: 30px 0px;
  padding-bottom: 50px;
  font-size: 16px;
}

.post-actions {
  margin: 10px 0;
  display: flex;
  gap: 10px;
}

.post-actions button {
  padding: 6px 10px;
  border: none;
  background-color: #eee;
  cursor: pointer;
  border-radius: 4px;
  font-size: 14px;
}

.likes {
  font-weight: bold;
  margin-bottom: 10px;
}
.like-btn {
  background-color: white;
  border: 1px solid #0d6efd;
  color: #0d6efd;
  transition: all 0.2s ease-in-out;
  box-shadow: none;
}

.like-btn:hover,
.like-btn:focus,
.like-btn.liked {
  background-color: white; /* 배경은 항상 흰색 유지 */
  color: #0d6efd;
}

.like-btn i {
  color: #0d6efd !important; /* 하트도 항상 파란색 유지 */
}

.comments-section {
  margin-top: 24px;
  padding: 0 16px;
}
.comments-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
}
.comment-header {
  font-size: 13px;
  color: #666;
  display: flex;
  justify-content: space-between;
}
.comment-content {
  font-size: 14px;
  margin-top: 4px;
}
.comment-form {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin: 16px 0;
  padding: 0 16px;
}
.comment-form textarea {
  width: 100%;
  height: 60px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 6px;
  resize: none;
}
.comment-form button {
  align-self: flex-end;
  padding: 6px 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.comment-actions {
  margin-top: 4px;
  display: flex;
  gap: 8px;
}
.comment-actions button {
  font-size: 12px;
  padding: 4px 8px;
  background-color: #eee;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
}
</style>