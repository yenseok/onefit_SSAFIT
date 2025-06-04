<template>
  <div id="app-wrapper" class="app-wrapper">
  <div class="search-container">
    <!-- 상단 고정 검색창 + 필터 탭 포함 -->
    <header class="search-header fixed-top bg-white px-3 py-2 border-bottom">
      <!-- 상단: 뒤로가기 + 검색창 -->
      <div class="d-flex align-items-center justify-content-between">
        <button class="btn p-0 border-0 bg-transparent" @click="goBack">
          <i class="bi bi-chevron-left text-primary fs-4"></i>
        </button>
        <div class="input-group mx-2 flex-grow-1">
          <span class="input-group-text bg-white border-end-0 rounded-start-pill">
            <i class="bi bi-search text-muted"></i>
          </span>
          <input
            v-model="keyword"
            type="text"
            class="form-control border-start-0 rounded-end-pill search-bar"
            placeholder="검색"
          />
        </div>
        <div style="width: 24px"></div>
      </div>

      <!-- 하단: 필터 탭 -->
      <div class="filter-tabs mt-2 d-flex justify-content-around">
        <div
          v-for="option in filterOptions"
          :key="option.value"
          :class="['filter-tab', { active: searchType === option.value }]"
          @click="searchType = option.value"
        >
          {{ option.label }}
        </div>
      </div>
    </header>

    <!-- 검색 결과 -->
    <main class="results px-3 mt-3">
      <div class="vstack gap-3">
        <div
          v-for="post in filteredPosts"
          :key="post.boardId"
          class="card p-3 card-hover"
          @click="goToDetailWithEffect($event, post.boardId)"
        >
          <!-- 상단 정보 -->
          <div class="d-flex justify-content-between align-items-center">
            <!-- 왼쪽: 프로필 + 닉네임 + 작성일 -->
            <div class="d-flex align-items-center text-muted small">
              <img
                v-if="post.profileImage"
                :src="API_BASE_URL + post.profileImage"
                alt="프로필"
                class="profile-img me-2"
                @error="onImageError"
              />
              <div class="d-flex flex-column justify-content-center lh-sm">
                <div class="fw-bold">{{ post.nickName }}</div>
                <div class="text-muted" style="font-size: 12px">
                  {{ formatDate(post.createdAt) }}
                </div>
              </div>
            </div>

            <!-- 오른쪽: 조회수 -->
            <div class="text-muted small align-self-start">{{ post.viewCount }} 읽음</div>
          </div>

          <!-- 제목 -->
          <div class="fs-6 fw-bold mb-1">{{ post.title }}</div>

          <!-- 썸네일 -->
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

          <!-- 하단 좋아요/댓글 -->
          <div class="text-muted small">
            <i class="bi bi-heart-fill text-primary me-1"></i> {{ post.likeCount }} &nbsp;&nbsp;
            <i class="bi bi-chat-dots text-primary me-1"></i> {{ post.commentCount }}
          </div>
        </div>
      </div>

      <div v-if="filteredPosts.length === 0" class="no-results">게시글이 없습니다.</div>
    </main>
  </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/api'

const router = useRouter()
const keyword = ref('')
const searchType = ref<'all' | 'title' | 'content' | 'writer'>('all')
const posts = ref<any[]>([])
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

const filterOptions = [
  { label: '전체', value: 'all' },
  { label: '작성자', value: 'writer' },
  { label: '제목', value: 'title' },
  { label: '내용', value: 'content' },
]

onMounted(async () => {
  const { data } = await api.get('/boards/latest')
  posts.value = data
})

const filteredPosts = computed(() => {
  if (!keyword.value.trim()) return posts.value

  const lowerKeyword = keyword.value.toLowerCase()
  return posts.value.filter((post) => {
    switch (searchType.value) {
      case 'title':
        return post.title?.toLowerCase().includes(lowerKeyword)
      case 'content':
        return post.content?.toLowerCase().includes(lowerKeyword)
      case 'writer':
        return post.nickName?.toLowerCase().includes(lowerKeyword)
      default:
        return (
          post.title?.toLowerCase().includes(lowerKeyword) ||
          post.content?.toLowerCase().includes(lowerKeyword) ||
          post.nickName?.toLowerCase().includes(lowerKeyword)
        )
    }
  })
})

function goBack() {
  router.back()
}

function formatDate(dateStr: string) {
  return dateStr?.split('T')[0] || ''
}

function goToDetail(boardId: number) {
  router.push(`/board/${boardId}`)
}
function onImageError(event: Event) {
  const img = event.target as HTMLImageElement
  img.src = '/default-thumbnail.png'
}
function goToDetailWithEffect(event: MouseEvent, boardId: number) {
  const card = event.currentTarget as HTMLElement
  card.classList.add('clicked')
  setTimeout(() => {
    card.classList.remove('clicked')
    router.push(`/board/${boardId}`)
  }, 150)
}
</script>

<style scoped>
main {
  padding-top: 90px;
  padding-bottom: 20px !important; /* TheFooterNav 높이 고려한 여백 */
}
.search-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}
.search-header {
  top: 56px; /* 또는 TheHeaderNav의 정확한 높이 */
  z-index: 1020;
  width: 100%;
  max-width: 500px;
  margin: 0 auto;
}
.search-header input:focus {
  border: 1px solid #0d6efd !important;
  box-shadow: 0 0 0 0.2rem rgba(13, 110, 253, 0.25);
}
.profile-img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
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
.thumbnail-box {
  width: 100px;
  height: 100px;
  flex-shrink: 0;
  overflow: hidden;
  border-radius: 8px;
  background-color: #f2f2f2;
  display: flex;
  align-items: center;
  justify-content: center;
}
.thumbnail-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}
.card-hover {
  transition: background-color 0.2s ease;
  margin-bottom: 8px; /* 카드 사이 하단 여백 */
  border-radius: 12px;
  border: 1px solid #ddd;
  background-color: #ffffff;
  padding: 16px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.04); /* 살짝 부각 */
}
.card-hover:hover {
  background-color: #e6f0ff;
}
.card-hover.clicked {
  background-color: #cce0ff !important;
}
.filter-tab {
  padding: 6px 12px;
  cursor: pointer;
  font-size: 14px;
  color: gray;
}

.filter-tab.active {
  border-bottom: 2px solid black;
  font-weight: bold;
  color: black;
}

/* header 내부에 필터 탭이 있으므로 margin-top 조정 */
.results {
  /* top: 130px; */
  width: 100%;
  max-width: 500px;
  min-width: 500px;
  margin: 0 auto;
  min-height: calc(100vh - 110px - 20px);
  /* display: flex;
  flex-direction: column;
  gap: 16px; */
}

.post-card {
  padding: 16px;
  border-radius: 12px;
  background-color: #f9f9f9;
  border: 1px solid #ddd;
}
.no-results {
  text-align: center;
  color: gray;
  margin-top: 20px;
  font-size: 16px;
}
</style>
