<template>
	<view class="container">
		<view class="title flex align-center justify-center">
			长按扫码采丁
		</view>
		<view class="title1 flex align-center justify-center ">
			我发起了《{{ title }}》采丁
		</view>
		<view class="box flex align-center ">
			<view class="title3 flex align-center justify-center">
				长按识别二维码访问
			</view>
			<image :src="image" alt="" />
			<button open-type="share" >
				立即分享
			</button>
		</view>
			<image class="bg" src="https://lidoogroup.com/shareBg.png" mode="aspectFill"></image>
		
	</view>
</template>
<script>
	export default {
		data() {
			return {
				phoneNum: "",
				title: "",
				url: "",
				access_token: "",
				image: "",
				paramsEncode: ""
			};
		},
		onLoad(options) {
			let userInfos = uni.getStorageSync("userInfos");
			let {
				phoneNumber,
				nickName,
				uid
			} = JSON.parse(userInfos) || {}
			this.title = options.name;
		
			this.paramsEncode  = `${uid}-${phoneNumber}`
			//console.log(this.url, 88)
			// / this.getAccessToken()
			this.getQRCode();
		},

		//底部加载更多
		onReachBottom() {},
		methods: {
		
			getQRCode(){
				// let parms = {
				// 		// 更多参数请看官方文档
				// 		"path": "pages/pedigreeMoudle/openShare/index", // 默认是主页，页面 page
				// 		"scene": this.paramsEncode, // 参数
				// 		"check_path": false, // 默认是true，检查page 是否存在
				// 		"env_version": "trial", // 要打开的小程序版本。正式版为 "release"，体验版为 "trial"，开发版为 "develop"。默认是正式版。                  
				// 		"width": "230"
				// 	}
				
				//生成商品的
				let parms = {
						// 更多参数请看官方文档
						"path": "pages/courseDetail/index?id=6&level=1", // 默认是主页，页面 page
						"scene": "6-1", // 参数 前面为商品id，后面为level1特价 2是多选 3是一种产品
						"check_path": false, // 默认是true，检查page 是否存在
						"env_version": "release", // 要打开的小程序版本。正式版为 "release"，体验版为 "trial"，开发版为 "develop"。默认是正式版。                  
						"width": "230"
					}
				
				this.$request({
					url: '/weixin/wxgetqrcode',
					method: "post",
					data:parms
				}).then(res=>{
					
			if (res && res?.data) {
						
				this.image = `data:image/jpeg;base64,${res.data}`
				 }
					
				})
			},
			onShareAppMessage(res) {
				let userInfos = uni.getStorageSync("userInfos");

				if (!!userInfos) {
					let {
						phoneNumber,
						nickName,
						uid
					} = JSON.parse(userInfos);

					if (res.from === "button") {
						// console.log(res.target)
					}
					return {
						title: "自定义分享标题",
						path: `/pages/pedigreeMoudle/openShare/index?uid=${uid}&name=${nickName}&phoneNumber=${phoneNumber}`,
					};
				}
			},
		},
	};
</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		padding:14rpx 40rpx 0;
		width: 100%;
		height: 100vh;
		background-color: #eee;
		
		.bg{
			position: fixed;
			top: 0;
			left: 0;
			width: 100%;
			height: 100vh;
		}
		.title{
			font-size: 100rpx;
			height: 138rpx;
			color: #333;
			letter-spacing: 8rpx;
			position: absolute;
			top: 0;
			z-index: 9;
			left: 0;
			width: 100%;
		}
		.title1{
			font-size: 48rpx;
			height: 10%;
			color: #333;
			position: absolute;
			top: 138rpx;
			z-index: 9;
			left: 0;
			width: 100%;
		}
		.box{
			flex-direction: column;
			width: 100%;
			height: 66%;
			background: linear-gradient(#fff,#FFC689);
			color: #000;
			font-size: 40rpx;
			padding-top:8%;
			position: relative;
			border-radius: 24rpx;
			z-index: 9;
			top:18%;
			.title3{
				height: 100rpx;
				letter-spacing: 4rpx;
				margin-bottom: 28rpx;
			}
			image{
				width: 460rpx;
				height: 460rpx;
				border-radius: 24rpx;
				border: 1px solid #FA8C16;
			}
			button{
				position: absolute;
				bottom: 4.3%;
				right: 4.3%;
				width: 568rpx;
				background-color: #FA8C16;
				height: 84rpx;
				border-radius: 8rpx;
				color: #fff;
				font-size: 40rpx;
			}
		}
	}
</style>