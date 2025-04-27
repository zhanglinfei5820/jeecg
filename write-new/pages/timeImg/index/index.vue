<template>
	<view class="container">
	   
	  <button class="add" @tap="chooseImage" :disabled="images.length >9">
		  <text>添加图片</text>
		</button>
		<view class="tip">
			请最多上传10张您印象最深刻的照片
		</view>
		<view class="list">
			<view class="box" v-for="(image, index) in images" :key="index">
			    <image  mode="aspectFill" src="../static/test.png" ></image>
				<view class="handle">
					<button @click="toEdit(image.id)">编辑</button>
					<button @click="del(image.id)">删除</button>
				</view>
			  </view>
		</view>
		  <button class="submit" @click="submit">生成回忆录</button>
	</view>

</template>

<script>
	export default {
		data() {
			return {
				images:[],
				num:"",  //上次图片总数量
				allNum:10,//最大上传数量10张
			}
		},
		onLoad(options) {
			this.getData()
		},
		methods:{
			chooseImage() {
			      uni.chooseImage({
			        count: this.allNum - this.num, // 最多可以选择的图片张数
			        sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
			        sourceType: ['album'], // 可以指定来源是相册还是相机，默认二者都有
			        success: (res) => {
			          // tempFilePath可以作为img标签的src属性显示图片
			          const tempFilePaths = res.tempFilePaths;
			          // this.images = tempFilePaths.map(filePath => ({
			          //   path: filePath
			          // }));
			          // 调用上传图片的函数
			         // this.uploadImage(tempFilePaths[0]);
			        },
			        fail: (err) => {
			          console.error('选择图片失败：', err);
			        }
			      });
			    },
			    uploadImage(filePath) {
			      uni.uploadFile({
			        url: 'https://your-server-url/upload', // 仅为示例，替换为你的上传接口
			        filePath: filePath,
			        name: 'file',
			        formData: {
			          user: 'test'
			        },
			        success: (uploadFileRes) => {
			          if (uploadFileRes.statusCode === 200) {
			            const data = JSON.parse(uploadFileRes.data);
			            console.log('上传成功：', data);
			          } else {
			            console.error('上传失败：', uploadFileRes);
			          }
			        },
			        fail: (err) => {
			          console.error('上传文件失败：', err);
			        }
			      });
			    },
				toEdit(id){
					//console.log(id,11)
					uni.navigateTo({
						url:`/pages/timeImg/edit/index?id=${id}`
					})
				},
				//提交
				submit(){
					//
				},
				//删除	
				del(id){
					
				},
				//查看当前上传图片数量
				getData(){
					//
					let imageList = [
					{
						path:"https://lidoogroup.com/hd.jpg",
						id:1
					},
					{
						path:"https://lidoogroup.com/hd.jpg",
						id:2
					},
					{
						path:"https://lidoogroup.com/hd.jpg",
						id:3
					}
				]
					this.images = imageList;
					this.num = imageList.length;
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
		.add{
			width: 200rpx;
			height: 80rpx;
			display: flex;
            align-items: center;
			justify-content: center;
			margin: 0;
		}
		.tip{
			height: 48rpx;
			margin-top: 20rpx;
		}
		.list{
			width: 100%;
			display: flex;
			flex-wrap: wrap;
			margin-top: 20rpx;
			gap: 26rpx;
			.box{
				width: 340rpx;
				height: 320rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				position: relative;
				.handle{
					position: absolute;
					height: 120rpx;
					top: 100rpx;
					left: 0;
					width: 100%;
					display: flex;
					align-items: center;
					justify-content: space-between;
					button{
						width: 140rpx;
						background: transparent;
						color: #fff;
						border: 1rpx solid #ffaa7f;
					}
				}
				image{
					width: 340rpx;
					height: 320rpx;
				}
			}
		}
		.submit {
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