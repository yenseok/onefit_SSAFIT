<template>
  <div class="flex flex-col items-center text-xs">
    <img :src="iconSrc" class="w-5 h-5 mb-1" />
    <span :class="active ? 'text-blue-600 font-semibold' : 'text-gray-400'">
      {{ label }}
    </span>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  label: string
  icon: string
  active?: boolean
}>()

const iconSrc = computed(() =>
  new URL(`../../assets/icons/menu/${props.icon}_${props.active ? 'blue' : 'gray'}.png`, import.meta.url).href
)
</script>
