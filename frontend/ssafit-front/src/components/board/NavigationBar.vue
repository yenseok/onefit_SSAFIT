<template>
  <nav class="flex flex-col px-4 pt-4 pb-1.5 mt-3 text-base font-semibold bg-white">
    <div class="flex justify-between items-center">
      <button @click="props.onCancel" class="text-[#006FFD] px-3 py-2">취소</button>
      <h1 class="text-black font-bold">{{ props.title }}</h1>
      <button @click="props.onComplete" class="text-[#006FFD] px-3 py-2">완료</button>
    </div>
  </nav>
</template>

<script lang="ts" setup>
import type { NavigationBarProps } from './types';
const props = defineProps<NavigationBarProps>();
</script>
