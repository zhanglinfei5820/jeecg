<template>
	<view class="container">
		<scroll-view v-if="!isShowClose" scroll-y="true" class="scroll-view">
			<image :src="list" mode="widthFix" show-menu-by-longpress="true"></image>
			
		</scroll-view>
		<video  id="video" v-if="isEpitaph && isShowClose" :src="src" autoplay="true"></video>
		<view @click="playVideo" class="video-paly" v-if="!isShowClose && isEpitaph" >
			<image src="../../static/play.jpg" mode=""></image>
			<view class="text">
				视频
			</view>
		</view>
		<view class="video-paly-close" v-if="isShowClose && isEpitaph" @click="closeVideo" >
			<image src="../../static/close.png" mode=""></image>
			<view class="text">
				关闭
			</view>
		</view>
	</view>

</template>

<script>
	export default {
		data() {
			return {
				list: "",
				isEpitaph:false,
				isShowClose:false,
				src:"",
			}
		},
		onLoad(options) {
			this.list = options.list;
			if(options?.type == "epitaph"){
				this.isEpitaph =true;
				this.isShowClose = true;
				  uni.setNavigationBarTitle({
				        title: '最后告白'
				    });
			}
			if(options.src){
				this.src = options.src;
			}
		},
		methods:{
			closeVideo(){
				this.isShowClose= false;
			},
			playVideo(){
				this.isShowClose= true;
			}
		}
		
	}
</script>

<style scoped lang="scss">
	.container {
		width: 100%;
		height: 100vh;
		overflow: hidden;
		.scroll-view {
			width: 100%;
			height: 100%;
			image {
				width: 100%;
				display: block; // 确保图片宽度占满父容器
			}
		}
		#video{
			width: 100%;
			height: 100vh;
		}
		.video-paly{
			position: fixed;
			right: 24rpx;
			bottom: 144rpx;
			width: 130rpx;
			height: 138rpx;
			background-color: #fff;
			display: flex;
			align-items: center;
			flex-direction: column;
			padding: 20rpx;
			color: #FA8C16;
			font-size: 30rpx;
			border-radius: 24rpx;
			border-bottom-left-radius: 0;
			image{
				width: 48rpx;
				height: 48rpx;
				margin-bottom: 8rpx;
				
			}
		}
		.video-paly-close{
			position: fixed;
						right: 24rpx;
						bottom: 144rpx;
						width: 130rpx;
						height: 138rpx;
						background-color: #fff;
						display: flex;
						align-items: center;
						flex-direction: column;
						padding: 20rpx;
						color: #000;
						font-size: 30rpx;
						border-radius: 24rpx;
						
						image{
							width: 48rpx;
							height: 48rpx;
							margin-bottom: 8rpx;
							
						}
		}
	}



	
</style>