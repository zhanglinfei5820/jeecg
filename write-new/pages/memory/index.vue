
<template>
	<view class="container">
	
		<!-- Tab 栏 -->
		<view class="tab-bar">
			<view v-for="(tab, index) in tabs" :key="index" :class="['tab-item', { active: currentIndex === index }]"
				@click="switchTab(index)">
				{{ tab }}
			</view>
		</view>

		<!-- 内容区域 -->
		<view class="content">
			<Course v-if="currentIndex == 1"></Course>
			<NovelList v-else></NovelList>
		</view>

		<!-- 自定义 Tabbar -->
		<Cos-Tabbar :current="4" :tabBarList="tabBerLists" />
	</view>
</template>

<script>
import Course from '../../component/Course.vue';
import NovelList from '../../component/novelList.vue';
import CosTabbar from '../../component/CosTabbar.vue';

export default {
	components: {
		Course,
		NovelList,
		CosTabbar
	},
	data() {
		return {
			tabs: ['优秀回忆录','创写回忆录'],
			currentIndex: 0,
			tabBerLists: [],
			banner: []
		};
	},
	onLoad() {
		
		this.currentIndex = uni.getStorageSync('memoryType') || 0;
		// 隐藏原生的tabbar,有自定义tabbar的页面都要写一遍
		uni.hideTabBar();
		
		//this.getBanner()
	},
	onShow() {
		this.currentIndex = uni.getStorageSync('memoryType') || 0;
		this.tabBerLists = uni.getStorageSync('tabBarList'); // 自定义的tabbar赋值
	},
	//右上角分享
	 onShareAppMessage(res) {
	    if (res.from === 'button') {// 来自页面内分享按钮
	      console.log(res.target)
	    }
	    return {
	      title: '您叙说我采写，欢迎您来~',
	      path: '/pages/home/index'
	    }
	  },
	methods: {
		switchTab(index) {
			this.currentIndex = index;
			uni.setStorageSync('memoryType', index);
		},
		getBanner(){
			this.$request({
				url:"/functionMenu/poster/list",
				method:"get",
				data:{
					module:"回忆录阅读"
				}
			}).then((res)=>{
				//console.log(res,999)
				if(res?.data?.rows.length > 0){
					this.banner = res?.data?.rows
				}
			})
		},
		toDes(item){
			uni.navigateTo({
				url:`/pages/banner/index?list=${item.bimage}` 
			})
		}
	}
};
</script>

<style lang="scss" scoped>
.container {
	width: 100%;
	height: 100vh;
	display: flex;
	flex-direction: column;
	background: #EEECEB;
	position: relative;
}


.tab-bar {
	position: fixed;
	top: 8rpx; /* 调整到搜索框下方 */
	left: 0;
	width: 100%;
	z-index: 10;
    padding: 4rpx 0;
    height: 88rpx;
	font-size: 32rpx;
	gap: 48rpx;
	display: flex;
	justify-content: center;

	.tab-item {
		padding: 4rpx 0 10rpx;
		font-size: 36rpx;
		color: #777;

		&.active {
			color: #333;
			font-size: 36rpx;
			border-bottom: 8rpx solid #FA8C16;
		}
	}
}

.content {
	flex: 1;
	margin-top: 116rpx; /* 调整到 Tab 栏下方 */
	overflow-y: auto;
}
</style>