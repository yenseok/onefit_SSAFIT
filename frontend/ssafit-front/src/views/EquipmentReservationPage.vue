<template>
  <div class="reservation-wrapper">
    <div class="reservation-container">
      <router-view />
    </div>
  </div>
</template>

<style scoped>
.reservation-wrapper {
  display: flex;
  justify-content: center;
  width: 100%;
}

.reservation-container {
  width: 100%;
  max-width: 500px;
}
</style>
