import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      '@/api': fileURLToPath(new URL('./src/api', import.meta.url)),
      '@components': fileURLToPath(new URL('./src/components', import.meta.url))
    },
  },
  server: {
    proxy: {
      '/boards': {
        target: 'http://localhost:8080', // 스프링 서버 주소로 수정
        changeOrigin: true,
        secure: false,
      },
      '/comments': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: path => path
      },
    },
  },
})
