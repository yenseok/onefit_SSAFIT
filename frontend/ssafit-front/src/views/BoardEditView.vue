<template>
  <div id="app-wrapper" class="app-wrapper">
  <div class="edit-container">
    <header class="header-bar fixed-top border-bottom">
      <div class="d-flex align-items-center justify-content-between px-3 py-2">
        <button class="text-primary btn border-0 bg-transparent fw-bold" @click="goCancel">
          취소
        </button>
        <h5 class="header-title">게시글 수정</h5>
        <button class="text-primary btn border-0 bg-transparent fw-bold" @click="submitEdit">
          완료
        </button>
      </div>
    </header>

    <main class="container">

      <div class="py-5">
        <div class="form-title">제목</div>
<input
  v-model="title"
  required
  class="form-control border-0 border-bottom"
  style="padding: 16px 0px; " 
/>
      </div>

<div class="mb-3" style="width: 500px;">
<!-- label -->
<!-- <label class="form-title" style="padding-left: 16px; width: 500px;">내용</label> -->

<!-- textarea -->
<textarea
  v-model="content"
  required
  rows="10"
  class="form-control border-0"
  style="padding-left: 10px !important;"
></textarea>

</div>


      <!-- 기존 이미지 -->
      <div class="gallery">
        <div
          class="thumbnail-wrapper position-relative"
          v-for="(url, i) in existingImages"
          :key="'existing-' + i"
        >
          <img :src="API_BASE_URL + url" class="thumbnail" @error="onImageError" />
          <button class="delete-btn" @click.prevent="removeExistingImage(i)">×</button>
        </div>
      </div>

      <!-- 새 이미지 추가 -->
      <div class="mb-3">
        <div class="form-title">이미지 추가</div>
        <input type="file" multiple @change="handleFiles" class="form-control" />
        <!-- 새 이미지 미리보기 삭제 버튼 추가 -->
        <div class="gallery mt-2">
          <div
            class="thumbnail-wrapper position-relative"
            v-for="(url, i) in previewUrls"
            :key="'new-' + i"
          >
            <img :src="url" class="thumbnail" />
            <button class="delete-btn" @click.prevent="removeNewImage(i)">×</button>
          </div>
        </div>
      </div>
    </main>
  </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api/api'

const route = useRoute()
const router = useRouter()
const boardId = parseInt(route.params.boardId as string)

const title = ref('')
const content = ref('')
const selectedFiles = ref<File[]>([])
const previewUrls = ref<string[]>([])
const existingImages = ref<string[]>([])
const deleteImageUrls = ref<string[]>([])

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
const token = sessionStorage.getItem('access-token')

onMounted(async () => {
  const res = await api.get(`/boards/${boardId}`)
  title.value = res.data.title
  content.value = res.data.content
  existingImages.value = res.data.imageUrls || []
})

function goCancel() {
  router.back()
}

function handleFiles(event: Event) {
  const files = (event.target as HTMLInputElement).files
  if (files) {
    const newFiles = Array.from(files)

    // 새 파일들을 누적
    selectedFiles.value.push(...newFiles)

    // 미리보기 URL도 누적
    newFiles.forEach((file) => {
      previewUrls.value.push(URL.createObjectURL(file))
    })
  }
}

function removeNewImage(index: number) {
  previewUrls.value.splice(index, 1)
  selectedFiles.value.splice(index, 1)
}

function onImageError(event: Event) {
  const img = event.target as HTMLImageElement
  img.src = '/default-thumbnail.png'
}

function removeExistingImage(index: number) {
  const removed = existingImages.value.splice(index, 1)[0]
  deleteImageUrls.value.push(removed)
}

async function submitEdit() {
  if (!title.value || !content.value) {
    alert('제목과 내용을 모두 입력해주세요.')
    return
  }

  const board = {
    title: title.value,
    content: content.value,
    deleteImageUrls: deleteImageUrls.value,
  }

  const formData = new FormData()
  formData.append(
    'board',
    new Blob(
      [
        JSON.stringify({
          title: title.value,
          content: content.value,
        }),
      ],
      { type: 'application/json' },
    ),
  )

  // 기존처럼 새 이미지 추가
  selectedFiles.value.forEach((file) => {
    formData.append('files', file)
  })

  // 삭제 요청한 기존 이미지 경로들도 추가
  deleteImageUrls.value.forEach((url) => {
    formData.append('deleteImageUrls', url)
  })

  try {
    await api.put(`/boards/${boardId}`, formData, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'multipart/form-data',
      },
    })
    alert('게시글이 수정되었습니다.')
    router.push(`/board/${boardId}`)
  } catch (err) {
    console.error('게시글 수정 실패:', err)
    alert('게시글 수정 실패')
  }
}
</script>

<style scoped>
.mb-3 {
  width: 500px;
}
.edit-container {
  padding: 55px 0px 100px !important;
  width: 500px !important;
  max-width: 500px !important;
  margin: 0 auto;
}
.header-bar {
  top: 60px; /* 또는 TheHeaderNav의 정확한 높이 */
  height: 53px;
  z-index: 1020;
  width: 100%;
  max-width: 500px;
  margin: 0 auto;
}
.header-title {
  font-weight: bold;
  font-size: 16px;
  margin: 0 auto;
}
main {
  padding: 0;
  width: 100%;
  max-width: 500px;
  min-width: 500px;
  margin: 0 0;
  min-height: calc(100vh - 110px - 20px);
}
.container {
  width: 100%;
  max-width: 500px;
  min-width: 500px;
  margin: 0 0;
  min-height: calc(100vh - 110px - 20px);
}
.py-5 {
  padding: 10px !important;
}


.form-title {
  font-weight: bold;
  margin: 0 0 4px 0; /* 좌우 마진 제거 */
  padding-left: 0;
}

.gallery {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.thumbnail-wrapper {
  width: 80px;
  height: 80px;
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid #ddd;
  background-color: #f3f3f3;
  position: relative;
}
.thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.delete-btn {
  position: absolute;
  top: 0;
  right: 0;
  width: 22px;
  height: 22px;
  background-color: #0d6efd;
  color: white;
  font-weight: bold;
  border: none;
  border-radius: 50%;
  line-height: 18px;
  cursor: pointer;
}
</style>