import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
import api from '@/api/api'; // 우리가 만든 axios 인스턴스

import router from '@/router';

const REST_API_URL = 'http://localhost:8080';

function base64UrlDecode(str) {
  str = str.replace(/-/g, '+').replace(/_/g, '/');
  while (str.length % 4) str += '=';
  try {
    return decodeURIComponent(
      atob(str)
        .split('')
        .map(c => '%' + c.charCodeAt(0).toString(16).padStart(2, '0'))
        .join('')
    );
  } catch {
    return atob(str);
  }
}

export const useAuthStore = defineStore('auth', () => {
const loginUser = ref(null);  // 닉네임
const role = ref(null);       // 권한
const user = ref(null);       // 전체 유저 정보 (UserResponseDto)
const isLoggedIn = computed(() => !!loginUser.value);

const userLogin = async (nickName, password) => {
  try {
    const response = await api.post(`/auth/login`,
      { nickName, password },
      { headers: { 'Content-Type': 'application/json' } }
    );

    const token = response.data.accessToken;
    const payload = JSON.parse(base64UrlDecode(token.split('.')[1]));

    loginUser.value = payload.nickName;
    role.value = payload.roles;
    user.value = response.data.user; //  여기서 유저 전체 저장

    const gymId = response.data.user.gymId;
    console.log("user.gymId 저장:", gymId);

    const trainerId = response.data.user.trainerId;


    sessionStorage.setItem('access-token', token);
    sessionStorage.setItem('user-id', payload.sub);
    sessionStorage.setItem('nickname', payload.nickName);
    sessionStorage.setItem('role', payload.roles);
    sessionStorage.setItem('gym-id', response.data.user.gymId);
    sessionStorage.setItem('trainer-id', trainerId);

    if (token) {
  const base64Url = token.split('.')[1];
  const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  const jsonPayload = decodeURIComponent(
    atob(base64)
      .split('')
      .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
      .join('')
  );
  console.log("JWT Payload:", JSON.parse(jsonPayload));
}

    router.push({ name: 'home' });
  } catch (err) {
    console.error('로그인 실패:', err);
    alert('닉네임 또는 비밀번호가 올바르지 않습니다.');
    router.push({ name: 'home' });
  }
};




  // 로그아웃
  const logout = () => {
    loginUser.value = null;
    role.value = null;
    sessionStorage.clear();
    router.push({ name: 'home' });
  };

  //  새로고침 시 세션 복원
  const restoreSession = () => {
    const token = sessionStorage.getItem('access-token');
    const nickname = sessionStorage.getItem('nickname');
    const savedRole = sessionStorage.getItem('role');
    const userId = sessionStorage.getItem('user-id');
    const gymId = sessionStorage.getItem('gym-id');
    const trainerId = sessionStorage.getItem('trainer-id');

    console.log('복원 정보:', { token, nickname, savedRole, userId, gymId, trainerId });

    if (token && nickname && savedRole && userId && gymId && gymId !== 'null') {
      loginUser.value = nickname;
      role.value = savedRole;

      user.value = {
        userId: Number(userId),
        nickName: nickname,
        gymId: Number(gymId),
        trainerId: trainerId && trainerId !== 'null' ? Number(trainerId) : null
      };

      console.log('user 복원 완료:', user.value);
    } else {
      console.warn('복원 실패: 일부 정보 누락');
    }
  };



  return { loginUser, role, user, isLoggedIn, userLogin, logout, restoreSession };
});
