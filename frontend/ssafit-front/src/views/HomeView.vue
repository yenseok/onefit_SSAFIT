<template>
  <div id="app-wrapper" class="app-wrapper">
  <div class="home-view">
    <!-- 상단 검색바 -->
    <header class="header fixed-top bg-white px-3 py-2 border-bottom">
      <!-- 검색 바 -->
      <div class="d-flex align-items-center justify-content-between">
        <div class="input-group mx-2 flex-grow-1" style="max-width: 80%">
          <span class="input-group-text bg-white border-end-0 rounded-start-pill">
            <i class="bi bi-search text-muted"></i>
          </span>
          <input
            v-model="search"
            @focus="goToSearch"
            type="text"
            class="form-control border-start-0 rounded-end-pill search-bar"
            placeholder="검색"
          />
        </div>

        <!-- 글쓰기 버튼 -->
        <button class="btn p-0 border-0 bg-transparent" @click="goToWrite" title="글 작성">
          <i class="bi bi-pencil-square fs-4 text-primary"></i>
        </button>
      </div>
    </header>

    <!-- 인기 게시글 -->
    <main>
      <section class="px-3 mt-4">
        <div class="d-flex overflow-auto gap-3 pb-2">
          <div
            v-for="post in popularBoards"
            :key="post.boardId"
            class="card text-center popular-card"
            @click="goToDetailWithEffect($event, post.boardId)"
          >
            <!-- 썸네일 또는 프로필 이미지 대체 -->
            <div class="thumbnail-container d-flex align-items-center justify-content-center bg-light">
              <img
                v-if="post.thumbnail"
                :src="API_BASE_URL + post.thumbnail"
                alt="썸네일"
                class="thumbnail-img"
                @error="onImageError"
              />
              <img
                v-else-if="post.profileImage"
                :src="API_BASE_URL + post.profileImage"
                alt="프로필"
                class="profile-thumb"
                @error="onImageError"
              />
              <i v-else class="bi bi-person-circle fs-1 text-secondary"></i>
            </div>

            <div class="card-body p-2">
              <h6 class="card-title post-title">{{ post.title }}</h6>
              <span class="nickname">{{ post.nickName }}</span>
              <div class="text-muted small">
                <i class="bi bi-heart-fill text-primary me-1"></i> {{ post.likeCount }}
              </div>
            </div>
          </div>
        </div>
      </section>

      <hr />

      <!-- 최신 게시글 -->
      <section class="px-3 mt-5">
        <div class="vstack gap-3">
          <div
            v-for="post in latestBoards"
            :key="post.boardId"
            class="card p-3 card-hover"
            @click="goToDetailWithEffect($event, post.boardId)"
          >
            <div class="d-flex justify-content-between mb-2">
              <div class="d-flex flex-column">
                <div class="d-flex align-items-center gap-2 mb-1">
                  <img
                    v-if="post.profileImage"
                    :src="API_BASE_URL + post.profileImage"
                    alt="프로필 이미지"
                    class="profile-large"
                    @error="onImageError"
                  />
                  <div class="d-flex flex-column">
                    <span class="nickname">{{ post.nickName }}</span>
                    <small class="text-muted">{{ formatDate(post.createdAt) }}</small>
                  </div>
                </div>
              </div>
              <div class="text-muted small align-self-start">{{ post.viewCount }} 읽음</div>
            </div>

            <div class="fs-6 fw-bold mb-1">{{ post.title }}</div>

            <template v-if="post.thumbnail">
              <div class="latest-thumbnail-wrap my-2">
                <img
                  :src="API_BASE_URL + post.thumbnail"
                  alt="썸네일"
                  class="latest-thumbnail"
                  @error="onImageError"
                />
              </div>
            </template>

            <div class="text-muted small">
              <i class="bi bi-heart-fill text-primary me-1"></i> {{ post.likeCount }} &nbsp;&nbsp;
              <i class="bi bi-chat-dots text-primary me-1"></i> {{ post.commentCount }}
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import api from '@/api/api'
import { useAuthStore } from '@/stores/auth'

interface Board {
  boardId: number
  thumbnail: string | null
  title: string
  nickName: string
  profileImage?: string
  createdAt: string
  viewCount: number
  likeCount: number
  commentCount: number
}

const authStore = useAuthStore()
const search = ref('')
const popularBoards = ref<Board[]>([])
const latestBoards = ref<Board[]>([])
const router = useRouter()
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

onMounted(async () => {
  const { data: popular } = await api.get('/boards/popular')
  const { data: latest } = await api.get('/boards/latest')

  const commentPromises = latest.map((board) => api.get(`/boards/${board.boardId}/comment-count`))

  const commentCounts = await Promise.all(commentPromises)
  latest.forEach((board, i) => {
    board.commentCount = commentCounts[i].data
  })

  popularBoards.value.splice(0, popularBoards.value.length, ...popular)
  latestBoards.value.splice(0, latestBoards.value.length, ...latest)
})

function goToSearch() {
  router.push('/search')
}

function formatDate(dateStr: string): string {
  return dateStr?.split('T')[0] || ''
}

function goToDetailWithEffect(event: MouseEvent, boardId: number) {
  const card = event.currentTarget as HTMLElement
  card.classList.add('clicked')
  setTimeout(() => {
    card.classList.remove('clicked')
    router.push(`/board/${boardId}`)
  }, 150)
}
function goToWrite() {
  router.push('/board/create')
}
function onImageError(event: Event) {
  const img = event.target as HTMLImageElement
  img.onerror = null
  img.style.display = 'none'
}
</script>

<style scoped>
.container-centered {
  max-width: 500px;
  margin: 0 auto;
  padding-left: 16px;
  padding-right: 16px;
  background-color: white;
}
.home-view {
  padding: 60px 0px 100px !important;
  width: 500px !important;
  max-width: 500px !important;
  margin: 0 auto;
  top: 0;
  /* height: 1500px; */
}
.header {
  top: 56px; /* 또는 TheHeaderNav의 정확한 높이 */
  z-index: 1020;
  width: 100%;
  max-width: 500px;
  margin: 0 auto;
}
.header input:focus {
  border: 1px solid #0d6efd !important;
  box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.25);
}
button[title='글 작성'] {
  margin-left: 8px;
}
main {
  top: 0;
  padding-top: 0px;
  padding-bottom: 20px !important; /* TheFooterNav 높이 고려한 여백 */
}
.profile-large {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
  background-color: #f0f0f0;
}
.nickname {
  font-size: 13px;
  font-weight: 750;
  color: #6c757d; /* Bootstrap text-muted와 유사한 연한 회색 */
}
.popular-card {
  min-width: 140px;
  max-width: 140px;
  height: 210px;
  flex-shrink: 0;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
}
.post-title {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
}
.card-hover {
  transition: background-color 0.2s ease;
}
.card-hover:hover {
  background-color: #e6f0ff;
}
.card-hover.clicked {
  background-color: #cce0ff !important;
}
.profile-thumb {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
}
.thumbnail-container {
  width: 100%;
  height: 100px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8f9fa;
}
.thumbnail-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.latest-thumbnail-wrap {
  width: 100%;
  height: 180px;
  overflow: hidden;
  border-radius: 8px;
}
.latest-thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>