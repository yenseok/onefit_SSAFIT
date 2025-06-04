import { defineStore } from 'pinia'

export const useBoardStore = defineStore('board', {
  state: () => ({
    boards: []
  }),
  actions: {
    async fetchBoards() {
      // API 호출하여 boards 상태 갱신
    }
  }
})