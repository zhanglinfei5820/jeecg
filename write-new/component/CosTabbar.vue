<template>
<view class="tab-bar1" style="bottom: 0;">
   		<!-- <view class="tab-bar-border"></view> -->
   		<view v-for="(item,index) in tabBarList" :key="index" class="tab-bar-item" :data-id="index"
   			@click="jump(item)">
   			<image :src="current == item.index ? item.selectedIconPath : item.iconPath"></image>
   			<view   :style="{'color':current == item.index ? '#333333' : '#777777'}" style="margin-top: 10rpx;">
   				{{item.text}}</view>
   		</view>
   	</view>

</template>

<script>
	import {tabBar} from '../utils/tabBar.js' 
	export default {
		props: {
			tabBarList: {
				type: Array,
				default: tabBar["huzhu"]
			},
			current: Number
		},
		data() {
			return {
				value1: 0, // 默认页面
				inactiveColor: '#22bf8e' // 高亮颜色
			}
		},
		mounted(){
			//console.log(3333,this.current)
			// 影藏原生的tabbar,有自定义tabbar的页面都要写一遍
				uni.hideTabBar()
		},
		onLoad() {
			//console.log(3333)
		// 影藏原生的tabbar,有自定义tabbar的页面都要写一遍
			uni.hideTabBar()
		},

		methods: {
			// 点击跳转对应tabbar页面
			jump(e) {
				uni.switchTab({
					url: e.pagePath
				})
			}
		}
	}

</script>

<style lang="scss" scoped>
		.tab-bar1 {
			position: fixed;
			bottom: 0;
			left: 0;
			right: 0;
			height: 188rpx;
			// border-top: 1px solid #ccc;
			background: white;
			display: flex;
			z-index: 997;
			// padding-bottom: 20rpx;
		}
		.tab-bar-border {
			// background-color: rgba(0, 0, 0, 0.33);
			background-color: white;
			position: absolute;
			left: 0;
			top: 0;
			width: 100%;
			height: 1px;
			// border-top: 2rpx solid rgba(187, 187, 187, 0.3);
			transform: scaleY(0.5);
		}
	
		.tab-bar-item {
			flex: 1;
			text-align: center;
			display: flex;
			justify-content: center;
			align-items: center;
			flex-direction: column;
			 padding: 10rpx 0;
			 height: 188rpx;
		}
	
		.tab-bar-item image {
			width: 24px;
			height: 24px;
		}
	
		.tab-bar-item view {
			font-size: 30rpx;
		}

</style>
