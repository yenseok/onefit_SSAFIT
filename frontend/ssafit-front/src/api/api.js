import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  withCredentials: true, // JWT나 쿠키 사용하는 경우
});

export default api;
