<template>
  <div id="app-wrapper" class="app-wrapper">
  <div class="write-container">
    <!-- 상단 바 -->
    <header class="header-bar fixed-top bg-white px-3 py-2 border-bottom">
      <div class="position-relative d-flex align-items-center justify-content-between">
        <button class="text-primary btn border-0 bg-transparent fw-bold" @click="handleCancel">
          취소
        </button>
        <h5 class="header-title">게시글 작성</h5>
        <button class="text-primary btn border-0 bg-transparent fw-bold" @click="handleComplete">
          완료
        </button>
      </div>
    </header>

    <!-- 제목 입력 -->
    <main>
      <div class="section border-bottom">
        <input
          v-model="title"
          type="text"
          class="form-control border-0"
          placeholder="제목을 입력해주세요"
        />
      </div>
      <br />

      <!-- 이미지 업로더 -->
      <div class="section image-upload-box">
        <div class="upload-box d-flex justify-content-center align-items-center flex-column">
          <label for="imageInput" class="upload-label text-center">
            <i class="bi bi-plus-circle fs-3 mb-2"></i>
            <div>사진 업로드</div>
          </label>
          <input id="imageInput" type="file" multiple @change="handleFileChange" class="d-none" />
        </div>
      </div>

      <!-- 이미지 미리보기 + 삭제 버튼 -->
      <div class="gallery">
        <div class="thumbnail-wrapper" v-for="(url, index) in previewUrls" :key="index">
          <img :src="url" class="thumbnail" />
          <button class="delete-btn" @click="removeImage(index)">×</button>
        </div>
      </div>

      <!-- 본문 작성 -->
      <div class="section content-box">
        <textarea
          v-model="content"
          placeholder="본문을 작성해주세요"
          class="form-control border-0"
          rows="8"
        />
      </div>
    </main>
  </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api/api' // 수정: api 인스턴스 사용

const router = useRouter()
const title = ref('')
const content = ref('')
const imageFiles = ref<File[]>([])
const previewUrls = ref<string[]>([])
const token = sessionStorage.getItem('access-token')

// 이미지 선택
function handleFileChange(event: Event) {
  const files = (event.target as HTMLInputElement).files
  if (files) {
    const newFiles = Array.from(files)
    imageFiles.value.push(...newFiles)
    previewUrls.value.push(...newFiles.map((file) => URL.createObjectURL(file)))
  }
}

// 이미지 제거
function removeImage(index: number) {
  imageFiles.value.splice(index, 1)
  URL.revokeObjectURL(previewUrls.value[index])
  previewUrls.value.splice(index, 1)
}

// 취소
function handleCancel() {
  if (confirm('작성을 취소하시겠습니까?')) {
    router.push('/')
  }
}

// 완료
async function handleComplete() {
  if (!title.value || !content.value) {
    alert('제목과 내용을 모두 입력해주세요.')
    return
  }

  try {
    const formData = new FormData()
    const board = {
      title: title.value,
      content: content.value,
    }

    formData.append('board', new Blob([JSON.stringify(board)], { type: 'application/json' }))
    imageFiles.value.forEach((file) => {
      formData.append('files', file)
    })

    await api.post('/boards', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        Authorization: `Bearer ${token}`,
      },
    })

    alert('게시글이 등록되었습니다.')
    router.push('/')
  } catch (error) {
    console.error('게시글 작성 실패:', error)
    alert('게시글 작성에 실패했습니다.')
  }
}
</script>

<style scoped>
.write-container {
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
  padding-top: 2px;
  padding-bottom: 20px;
  width: 100%;
  max-width: 500px;
  min-width: 500px;
  margin: 0 auto;
  min-height: calc(100vh - 110px - 20px);
  /* display: flex;
  flex-direction: column;
  gap: 16px; */
}
.section {
  padding: 12px 16px;
}
.image-upload-box {
  background-color: #f1f5ff;
  border-radius: 12px;
  padding: 16px;
}
.upload-label {
  cursor: pointer;
  color: #0d6efd;
}
.gallery {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 8px 16px;
}
.thumbnail-wrapper {
  width: 80px;
  height: 80px;
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid #ddd;
  background-color: #f3f3f3;
  position: relative; /* 삭제 버튼 위치 지정 */
}
.thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}
.delete-btn {
  position: absolute;
  top: 0;
  right: 0;
  background: rgba(255, 255, 255, 0.7);
  border: none;
  color: #0d6efd;
  font-size: 16px;
  padding: 0 6px;
  cursor: pointer;
}
.content-box {
  border-radius: 12px;
  margin: 16px;
  padding: 8px;
  border: 1px solid #0d6efd;
}
</style>