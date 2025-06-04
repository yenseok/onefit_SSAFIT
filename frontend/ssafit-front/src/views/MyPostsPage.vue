<template>
  <div id="app-wrapper" class="app-wrapper">
  <div class="my-posts-page">
    <!-- 상단 바: 뒤로가기 + 검색창 + 필터 -->
    <header class="search-header fixed-top bg-white px-3 py-2 border-bottom">
      <div class="d-flex align-items-center justify-content-between">
        <button class="btn p-0 border-0 bg-transparent" @click="goBack">
          <i class="bi bi-chevron-left text-primary fs-4"></i>
        </button>
        <div class="input-group mx-2 flex-grow-1" style="max-width: 80%">
          <span class="input-group-text bg-white border-end-0 rounded-start-pill">
            <i class="bi bi-search text-muted"></i>
          </span>
          <input
            v-model="search"
            type="text"
            class="form-control border-start-0 rounded-end-pill search-bar"
            placeholder="내 게시글 검색"
          />
        </div>
        <div style="width: 24px"></div>
      </div>

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

    <!-- 게시글 리스트 -->
    <main class="results px-3 mt-3">
      <div class="vstack gap-3">
        <div
          v-for="post in filteredPosts"
          :key="post.boardId"
          class="card p-3 card-hover"
          @click="goToDetail(post.boardId)"
        >
          <!-- 상단 정보 -->
          <div class="d-flex justify-content-between mb-2 align-items-center">
            <!-- 왼쪽: 프로필 이미지 + 닉네임 + 작성일 -->
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

          <!-- 썸네일 이미지 -->
          <template v-if="post.thumbnail">
            <div class="thumbnail-wrap my-2">
              <img
                :src="API_BASE_URL + post.thumbnail"
                alt="썸네일"
                class="thumbnail"
                @error="onImageError"
              />
            </div>
          </template>

          <!-- 하단 정보 -->
          <div class="text-muted small">
            <i class="bi bi-heart-fill text-primary me-1"></i> {{ post.likeCount }} &nbsp;&nbsp;
            <i class="bi bi-chat-dots text-primary me-1"></i> {{ post.commentCount || 0 }}
          </div>
        </div>
      </div>

      <div v-if="filteredPosts.length === 0" class="no-results">게시글이 없습니다.</div>
    </main>
  </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/api'

const router = useRouter()
const search = ref('')
const searchType = ref<'all' | 'title' | 'content'>('all')
const posts = ref<any[]>([])
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

function onImageError(event: Event) {
  const img = event.target as HTMLImageElement
  img.src = '/default-thumbnail.png'
}

const filterOptions = [
  { label: '전체', value: 'all' },
  { label: '제목', value: 'title' },
  { label: '내용', value: 'content' },
]

const token = sessionStorage.getItem('access-token')
const userId = sessionStorage.getItem('user-id')

onMounted(async () => {
  try {
    const { data } = await api.get(`/boards/my/${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
    posts.value = data
  } catch (err) {
    alert('내 게시글을 불러오지 못했습니다.')
  }
})

const filteredPosts = computed(() => {
  if (!search.value.trim()) return posts.value

  const keyword = search.value.toLowerCase()

  return posts.value.filter((post) => {
    switch (searchType.value) {
      case 'title':
        return post.title?.toLowerCase().includes(keyword)
      case 'content':
        return post.content?.toLowerCase().includes(keyword)
      default:
        return (
          post.title?.toLowerCase().includes(keyword) ||
          post.content?.toLowerCase().includes(keyword)
        )
    }
  })
})

function formatDate(dateStr: string) {
  return dateStr?.split('T')[0] || ''
}

function goToDetail(boardId: number) {
  router.push(`/board/${boardId}`)
}

function goBack() {
  router.back()
}
</script>

<style scoped>
.my-posts-page {
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
main {
  padding-top: 90px;
  padding-bottom: 20px !important; /* TheFooterNav 높이 고려한 여백 */
}
.thumbnail-wrap {
  width: 100%;
  height: 180px;
  overflow: hidden;
  border-radius: 8px;
}
.profile-img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}
.thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.card-hover {
  transition: background-color 0.2s ease;
  border-radius: 12px;
  border: 1px solid #ddd;
  background-color: #fff;
  margin-bottom: 12px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.04);
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

.no-results {
  text-align: center;
  color: gray;
  margin-top: 20px;
  font-size: 16px;
}
</style>
