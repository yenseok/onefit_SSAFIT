<template>
  <div id="app-wrapper" class="app-wrapper">
  <div class="login-page">


    <div class="login-container">
      <h1 class="login-title">로그인</h1>

      <input type="text" class="login-input" placeholder="아이디" v-model.trim="nickName" />
      <input type="password" class="login-input" placeholder="비밀번호" v-model.trim="password" />

      <button class="login-btn" @click="login">로그인</button>

      <p class="signup-link">
        <router-link to="/signup">회원가입 하러 가기</router-link>
      </p>
    </div>
  </div>
</div>
</template>



<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';


const nickName = ref('');
const password = ref('');
const store = useAuthStore();

const login = () => {
  store.userLogin(nickName.value, password.value);
};
</script>

<style scoped>
.login-page {
  padding: 100px 0px 100px !important;
  width: 500px !important;
  height: 800px;
  margin: 0 auto;
  top: 0;
}

.login-container {
  position: relative; /* 기존 absolute 제거 */
  top: 0;
  width: 400px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  left: 50%;
  transform: translateX(-50%);
}


/* 스타일은 동일 */
.login-title {
  font-size: 24px;
  font-weight: 700;
  text-align: center;
}

.login-input {
  padding: 14px 16px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 12px;
  background-color: #fafafa;
  outline: none;
}

.login-input::placeholder {
  color: #8f9098;
}

.login-btn {
  padding: 14px;
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  background-color: #006ffd;
  border: none;
  border-radius: 12px;
  cursor: pointer;
}
.login-btn:hover {
  background-color: #005be0;
}

.signup-link {
  font-size: 14px;
  text-align: center;
  color: #8f9098;
}
.signup-link a {
  color: #006ffd;
  text-decoration: none;
}
.signup-link a:hover {
  text-decoration: underline;
}


.login-link {
  color: #006ffd;
  font-weight: 500;
  text-decoration: none;
}
.login-link:hover {
  text-decoration: underline;
}


</style>
