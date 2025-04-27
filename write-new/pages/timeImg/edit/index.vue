<template>
	<view class="container">
	  
	      <view class="img">
	      	<image src="../static/test.png" mode="aspectFill"></image>
	      </view>
		<view class="form">
			<view class="item">
				<text class="title">图片的拍摄时间</text>
				<textarea v-model="formData.time" placeholder="请输入拍摄时间" auto-height="true" class="auto-resize-textarea" />
				
			</view>
			<view class="item">
				<text class="title">图片的拍摄地点</text>
				<textarea v-model="formData.there" placeholder="请输入拍摄地点" auto-height="true" class="auto-resize-textarea" />
				
			</view>
			<view class="item">
				<text class="title">拍照为了记录什么事情？</text>
				<textarea v-model="formData.story" placeholder="请输入为什么要拍下这张照片" auto-height="true" class="auto-resize-textarea" />
				
			</view>
		</view>
		<button class="save" @click="save">保存</button>
	</view>

</template>

<script>
	export default {
		data() {
			return {
					
				formData:{
					time:"",
					there:"",
					story:""
				}
			}
		},
		onLoad(options) {
			//根据ID，获取图片相关的信息
			
		},
		methods:{
		  save(){
			  
		  }
			    
		}
		
	}
</script>

<style scoped lang="scss">
	.container {
		width: 100%;
		height: 100vh;
		overflow: hidden;
		padding: 20rpx;
		 background-color: #EEECEB;
		.img{
			width: 100%;
			height: 400rpx;
			image{
				width: 100%;
				height: 400rpx;
			}
		}
		.item{
			font-size: 36rpx;
			.title{
				height: 60rpx;
				display: flex;
				align-items: center;
			}
			.auto-resize-textarea {
				width: 670rpx;
				font-size: 32rpx;
				margin-top: 12rpx;
				color: #777777;
				resize: none;
				/* 禁用手动调整大小 */
				word-wrap: break-word;
				/* 自动换行 */
				min-height: 44rpx;
				/* 最小高度 */
			}
		}
		.save {
			width: 100%;
			font-size: 40rpx;
			margin-top: 32rpx;
			background-color: #FA8C16;
			color: white;
			border: none;
			height: 84rpx;
			border-radius: 16rpx;
			position: fixed;
			bottom: 0rpx;
			left: 0;
		}
	}

</style>