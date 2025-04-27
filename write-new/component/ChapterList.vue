<template>
  <div>
    <div>{{ novel.name }} - 选择章节</div>
    <ul>
      <li v-for="chapter in novel.chapters" :key="chapter.id">
        {{ chapter.name }}
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'ChapterList',
  data() {
  	return {
  		novels: [
        {
          id: 1,
          name: 'anc',
          chapters: [
            { id: 1, name: '第1章 天界之门' },
            { id: 2, name: '第2章 天界之门' },
            { id: 3, name: '第3章 天界之门' },
          ],
        },
        {
          id: 2,
          name: 'bnc',
          chapters: [
            { id: 1, name: '第1章 天界之门' },
            { id: 2, name: '第2章 天界之门' },
            { id: 3, name: '第3章 天界之门' },
          ],
        }
      ],
  	};
  },
  methods: {
    selectChapter(chapter) {
      this.$emit('select-chapter', chapter);
    }
  }
};
</script>

<style scoped>
ul {
  list-style-type: none;
  padding: 0;
}

li {
  cursor: pointer;
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

li:hover {
  background-color: #f0f0f0;
}
</style>