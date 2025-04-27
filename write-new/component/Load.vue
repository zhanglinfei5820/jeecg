<template>
  <view class="loading-overlay" v-if="showOverlay">
    <view class="loading-container">
      <view class="spinner"></view>
      <text v-if="message" class="loading-message">{{ message }}</text>
    </view>
  </view>
</template>

<script>
export default {
  props: {
    message: {
      type: String,
      default: '加载中...'
    },
    showOverlay: {
      type: Boolean,
      default: true
    }
  }
}
</script>

<style scoped>
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40rpx;
  background-color: #fff;
  border-radius: 16rpx;
  box-shadow: 0 8rpx 16rpx rgba(0, 0, 0, 0.1);
}

.spinner {
  border: 8rpx solid rgba(0, 0, 0, 0.1);
  border-left-color: #007aff;
  border-radius: 50%;
  width: 80rpx;
  height: 80rpx;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-message {
  margin-top: 20rpx;
  font-size: 28rpx;
  color: #666;
}
</style>