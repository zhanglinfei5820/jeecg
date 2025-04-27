<template>
  <div>
    <div>{{ chapter.name }}</div>
    <p>内容: 这里是 {{ chapter.name }} 的内容...</p>
    <!-- 你可以在这里添加更多阅读功能，比如滚动加载、字体调整等 -->
  </div>
</template>

<script>
export default {
  name: 'Reader',
  props: ['chapter']
};
</script>

<style scoped>
/* 添加一些简单的样式 */
p {
  font-size: 1.2em;
  line-height: 1.6em;
  margin: 20px 0;
}
</style>