<template>
	<div>
		<div v-for="(novel, index) in novels" :key="index">
			<div class="novel-list" @click="selectNovel(novel)">
				<div class="novel-img">
					<img :src="novel.imgurl" alt="">
				</div>
				<div class="novel-info">
					<div class="novel-title">
						<div class="novel-name">{{novel.textname}}</div>
					</div>
					<div class="novel-introduce">{{novel.content}}
					</div>
					<div class="novel-bottom">
						<div class="novel-status">已经连载{{novel.consecutivedays}}天</div>
						<div class="novel-days">{{readPeople}}人在看</div>
					</div>
				</div>
			</div>
		</div>
		<!-- <movable-area class="movable-area" @click="toDetail">
			<movable-view class="movable-view" :x="x" :y="y" direction="all">
				<image style="width: 130rpx;height: 138rpx;" :src="require('../static/buysame.png')"></image>
			</movable-view>
		</movable-area> -->
		<image class="buy" @click="toDetail" style="width: 130rpx;height: 138rpx;" :src="require('../static/buysame.png')"></image>
	</div>
</template>

<script>
	export default {
		name: 'NovelList',
		data() {
			return {
				novels: [],
				x: 260,
				y: 230,
				readPeople: "",
			};
		},
		methods: {
			randomPeople() {
				var people = Math.floor(Math.random() * 10000) + 1;
				this.readPeople = people;
			},
			toDetail() {
				uni.navigateTo({
					url: '/pages/courseDetail/index?id=2&level=2'
				})
			},
			selectNovel(e) {
				// debugger
				// console.log(e.id)
				// uni.navigateTo({
				// 	url: `/pages/novel/index?data=${JSON.stringify(e.id)}`
				// });
				uni.navigateTo({
					url: `/pages/article/index?data=${JSON.stringify(e.id)}`
				});
			},
			getList() {
				var self = this
				this.$request({
					url: "/functionMenu/textinfo/list",
					method: "get"
				}).then((res) => {
					self.novels = res.data.rows
					console.log(self.novels)
				})
			}
		},
		created() {
			this.getList()
			this.randomPeople()
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

	.novel-list {
		display: flex;
		align-items: center;
		justify-content: flex-start;
		gap: 16rpx;
		margin-bottom: 16rpx;
	}

	.novel-img {
		width: 162rpx;
		height: 220rpx;
		border-radius: 18rpx;
		overflow: hidden;
		/* 确保图片在圆角内 */
		margin-left: 24rpx;
	}

	.novel-img img {
		width: 100%;
		/* 宽度占满父元素 */
		height: 100%;
		/* 高度占满父元素 */
		object-fit: cover;
		/* 让图片保持比例并填满容器 */
	}

	.novel-info {
		width: 556rpx;
		height: 220rpx;
	}

	.novel-title {
		width: 556rpx;
		height: 50rpx;
		display: flex;
		justify-content: space-between;
	}

	.novel-name {
		font-size: 40rpx;
		font-weight: Medium;
		color: #333;
		width: 572rpx;
		height: 50rpx;
	}

	.novel-status {
		height: 48rpx;
		font-size: 32rpx;
		border-radius: 10rpx;
		color: #FAAD14;
		background-color: #F8F1E7;
		padding: 3rpx;
	}

	.novel-introduce {
		font-size: 32rpx;
		color: #777;
		margin-top: 16rpx;
		width: 572rpx;
		height: 88rpx;
		overflow: hidden;
		/* 隐藏超出内容 */
		display: -webkit-box;
		/* 使用弹性盒模型 */
		-webkit-box-orient: vertical;
		/* 垂直排列 */
		-webkit-line-clamp: 2;
		/* 限制为2行 */
		text-overflow: ellipsis;
		/* 使用省略号 */
	}

	.novel-bottom {
		display: flex;
		margin-top: 16rpx;
	}

	.novel-days {
		height: 48rpx;
		font-size: 32rpx;
		border-radius: 10rpx;
		color: #FAAD14;
		background-color: #F8F1E7;
		margin-left: 20rpx;
		padding: 3rpx;
	}

	.movable-area {
		height: 95vh;
		width: 750rpx;
		top: 20;
		position: fixed;
		pointer-events: none;
		z-index: 9999;
	}

	.movable-view {
		width: 130rpx;
		height: 138rpx;
		pointer-events: auto;
	}
	.buy{
		position: fixed;
		bottom: 214rpx;
		right: 24rpx;
		z-index: 10;
	}
</style>