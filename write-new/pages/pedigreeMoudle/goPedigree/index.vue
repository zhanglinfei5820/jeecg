<template>
	<view class="container">
          <view class="box">
          <view class="flex gap row1">
          	<text class="size-24">{{huzhu}}</text>
          	<!-- <text class="size-12 padding-sm bg-color-blue white bordr-radius flex align-center justify-center">第三世</text> -->
          	<text class="zupu">{{title}}</text>
          </view>
          <view class="row2">
          	<text>电话：</text>
          	<text>{{phoneNumber}}</text>
          </view>
          <view class="row3 ellipsis">
          	<text>地址：</text>
          	<text class="ellipsis">{{address}}</text>
          </view>
          	<view class="flex  align-center row4">
          		<view class="flex flex-1 align-start  ">
          			<text>总人数：</text>
          			<text style="color: #FA8C16;">{{allNum}}</text>
          		</view>
          		<view class="flex flex-1  align-start">
          			<text>男性：</text>
          			<text style="color: #FA8C16;" >{{manNum}}</text>
          		</view>
          		<view class="flex  align-start ">
          			<text>女性：</text>
          			<text style="color: #FA8C16;" >{{wumanNum}}</text>
          		</view>
          	</view>
          	<view class="row5 flex justify-between align-center">
          		 <view  class="flex align-center" @click="edit">
          		 	<image style="width: 48rpx;height: 48rpx;margin-right: 10rpx;" src="../static/edit.jpg" mode="aspectFill"></image>
          		 	 <text style="font-size: 36rpx;">编辑</text>
          		 </view>
          		<button @click="yulan">预览</button>
          	</view>
          </view>
		  <view class="foot">
		  	<button>创建新户主家庭</button>
		  </view>
	</view>

</template>

<script>
	import request from '../../../request/index'
	export default {
		data() {
			return {
				allNum: 0,
				manNum: 0,
				wumanNum: 0,
				title: "",
				name: "",
				phoneNumber: "",
				phoneNumber1: "",
				address: "",
				huzhu: ""
			}
		},
		onLoad() {

			//this.getData(1)
			//获取族谱信息
			let pedigreeInfo = JSON.parse(uni.getStorageSync("pedigree") || "{}");
			this.title = pedigreeInfo?.familyname;
			
			//本地获取用户手机号
			let huzhuInfos = uni.getStorageSync('huzhuInfos');

			if (!!huzhuInfos) {
				let {
					phone,
					name,
					id,
					area
				} = JSON.parse(huzhuInfos)
                this.address = area;
				this.phoneNumber = phone;
				this.huzhu = name;
				this.getHouse(id)
			}

		},
		methods: {
			palyPhone() {
				uni.makePhoneCall({
					phoneNumber: this.phoneNumber
				});
			},

			
			edit() {

				uni.redirectTo({
					url: '/pages/pedigreeMoudle/editPedigree/index'
				});
			},
			yulan(){
				uni.redirectTo({
					url: '/pages/pedigreeMoudle/addPedigree/index'
				});
			},
			getHouse(id) {

				request({
					url: "/functionMenu/relatives/list",
					method: "get",
					data: {
						householderid: id
					}
				}).then((res) => {
					//	console.log(res,111)
					if (res?.data?.rows) {

						// 计算人数
						let data = res?.data?.rows;
						let woman = 0;
						let man = 0;

						data.map(item => {
							if (item.gender == "0") {
								man++;

								if (!!item.partnername) {
									woman++
								}
							} else if (item.gender == "1") {
								woman++;
								if (!!item.partnername) {
									man++

								}
							}
						})
						//	console.log(man,woman,11)
						this.allNum = man + woman;
						this.manNum = man;
						this.wumanNum = woman;

					}
				})
			},
		}

	}
</script>

<style lang="scss" scoped>
	.container {
		width: 100%;
		height: 100vh;
		display: flex;
		flex-direction: column;
		background-color: #EEECEB;
		padding: 32rpx 24rpx;

		.box {
			padding: 24upx 16rpx;
			background: #fff;
			border-radius: 24rpx;
			color: #333;
			font-size: 36rpx;
			.row1{
				font-size: 48rpx;
				height: 68rpx;
				margin-bottom: 16rpx;
				.zupu{
					background-color: #6DF9C7;
					color: #fff;
					font-size: 36rpx;
					padding:8rpx;
					margin-left: 18rpx;
					border-radius: 8rpx;
				}
			}
			.row2{
				color: #777;
				height: 50rpx;
				margin-bottom: 16rpx;
			}
			.row3{
				color: #777;
				height: 50rpx;
				font-size: 34rpx;
				margin-bottom: 16rpx;
			}
			.row4{
				background-color: #FFF7E6;
				height: 82rpx;
				border-radius: 8rpx;
				padding: 0 24rpx;
				margin-bottom: 16rpx;
			}
		    .row5{
				height: 58rpx;
				padding-left: 8rpx;
				button{
					width: 182rpx;
					background-color: #FA8C16;
					border-radius: 8rpx;
					color: #fff;
					font-size: 32rpx;
					margin: 0;
					height: 58rpx;
			
				}
			}
		}

		.foot{
			position: fixed;
			height: 140rpx;
			padding: 16rpx;
			bottom: 0;
			left: 0;
			width: 100%;
			background-color: #fff;
			button{
				background-color: #FA8C16;
				height: 108rpx;
				border-radius: 16rpx;
				color: #fff;
				font-size: 48rpx;
			}
		}

		
	}
</style>