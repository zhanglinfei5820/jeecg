<template>
	<view class="container">
		<view v-for="(course, index) in courses" :key="index" class="course-item">
			<view class="flex course-title">

				{{course.title}}
			</view>
			<image :src="course.image" class="course-image" @click="goDes(course)"></image>
			<view class="course-info">

				<view class="course-price-bought" @click="goDes(course)">
					<view class="price">
						 <view class="">
						 	<text style="margin-top: 16rpx;font-size: 36rpx;">¥</text>
						 	<text class="course-price">{{ course.price }}</text>
							<text style="font-size: 32rpx;" v-if="course.id ==2">起</text>
						 </view>
						<view class="flex align-center justify-between price1">劵后价¥{{course.couponprice}}</view>
					</view>
					<text class="course-bought">{{ course.paycount }}人已报名</text>
				</view>
				<view class="course-description" @click="goDes(course)">
					<text style="color: #333;">产品介绍：</text>
					<text>{{ course.introduce }}</text>
				</view>
				<view class="refund" @click="toggleRefundDetails(index)">
					<view class="refund-left">
						<image class="tip" src="../static/image/tip.jpg" mode=""></image>
						<text>不满意超额退款</text>
					</view>
					<image class="refund-right" :class="{ 'rotate': course.isRefundDetailsVisible }"   src="../static/image/right.jpg"  mode=""></image>
				</view>
				<view class="refund-details" v-show="course.isRefundDetailsVisible">
					
					<text v-if="course.level != '1'">{{course.refundrules}}</text>
					<image  style="width: 100%;" v-else :src="course.refundrules" mode="aspectFill"></image>
				</view>
				<view class="go-buy flex justify-between align-center">
		
					<button class="kefu flex " style="background-color: transparent;margin: 0;" @contact="concat" open-type="contact">
						<image class="kefu-img" src="../static/image/kefu.jpg" mode="aspectFill"></image>
						<text>客服</text>
					</button>
					<button class="buy-btn" @click="onClickItem(course)">领劵下单</button>
				</view>
			</view>
		</view>
		 
	<!-- 	<uni-load-more :status="status"></uni-load-more> -->
	</view>
</template>

<script>
	export default {
        
		data() {
			return {
				isRefundDetailsVisible: false,
				showQr:false,
				courses: [
					// {
					// 	title: '课程1',
					// 	image: './static/course.jpeg',
					// 	description: '这是课程1的描述',
					// 	price: 99,
					// 	bought: 1234,
					// 	id: 1
					// }
				],
				status: "more", // 当前数据状态
				page: 1,
				pageSize: 20,
			};
		},
		mounted() {
		//	console.log(11, this)
		},
		created() {
			this.getList()
		},
		methods: {
			toggleRefundDetails(i) {

				const newCourses = [...this.courses];
				newCourses[i].isRefundDetailsVisible = !newCourses[i].isRefundDetailsVisible;
				this.courses.splice(0, this.courses.length, ...newCourses);
			},
			goDes(item){
				//console.log(item,11)
				uni.navigateTo({
					url: `/pages/courseDetail/index?id=${item.id}&level=${item.level}`
				})
			},
			onClickItem(item) {
            uni.navigateTo({
            	url: `/pages/courseDetail/index?id=${item.id}&level=${item.level}`
            })
			},
			getList() {
				this.$request({
					url: "/functionMenu/productinfo/list",
					method: "get",
					data: {
						pageNum: this.page,
						pageSize: this.pageSize
					}
				}).then((res) => {
					//console.log(res, 111)
					this.status = res.data.total > this.page * this.pageSize ? "more" : "noMore";
					if (res?.data?.rows?.length > 0) {
						if (this.status == "more") {
							this.page++
						}
						res?.data?.rows.map((item) => {
							if(item.level != '1'){
								if(item.id != "3"){
									let obj = {
										...item,
										title: item?.pname,
										image: item?.pimage,
										paycount: item.paycount || 10,
										isRefundDetailsVisible: false
									}
									// console.log(obj, 88)
									this.courses.push(obj)
								}
								
							}
							
						})
					}
				})
			},
			showQrCode(){
				this.$refs.popupQr.open()
			},
			concat(e){
				console.log(e,11)
			}
		}
	};
</script>

<style lang="scss" scoped>
	.container {
		padding: 0 24rpx;
		margin-bottom: 218rpx;
	}

	// .content {
	// 	margin-top: 20rpx;

	// }

	.course-item {

		border-radius: 16rpx;
		margin-bottom: 32rpx;
		background: #fff;
		position: relative;
	}

	.course-image {
		width: 100%;
		height: 320rpx;
		border-top-left-radius: 16rpx;
		border-top-right-radius: 16rpx;
		// margin-right: 20rpx;
	}

	.course-info {
		
		margin-top: 16rpx;

	}

	.course-title {

		font-weight: bold;
		position: absolute;
		top: 0;
		left: 0;
		border-top-left-radius: 16rpx;
		height: 66rpx;
		padding: 8rpx;
		background-color: #FA8C16;
		font-size: 36rpx;
		color: #fff;
		z-index: 8;
		display: flex;
		align-items: center;
	}

	.course-description {
		color: #777;
		font-size: 32rpx;
		margin: 14rpx 0;
		padding: 0 16rpx;
	}

	.course-price-bought {
		display: flex;
		justify-content: space-between;
		height: 136rpx;
		align-items: center;
		padding: 0 16rpx;

		.price {
			color: #FF2600;
			// height: 84rpx;
			
			font-size: 60rpx;
			display: flex;
			// align-items: center;
			flex-direction: column;
			.price1 {
				color: #fff;
				font-size: 32rpx;
				height: 44rpx;
				padding: 0 6rpx;
				border-radius: 8rpx;
				background: linear-gradient(to right, #FF1C6E, #FF5029);
			
			}
		}
	}

	.course-price {
		font-size: 60rpx;
	}

	.course-bought {
		height: 48rpx;
		border-radius: 28rpx;
		background: rgba(0, 0, 0, 0.49);
		color: #fff;
		padding: 0 20rpx;
		font-size: 32rpx;
		display: flex;
		align-items: center;
	}

	.refund {
		padding: 0 16rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		height: 82rpx;
		border-bottom: 1rpx solid #E6E6E6;
        font-size: 36rpx;
		color: #333;
		.refund-left {
			display: flex;
			align-items: center;
		}

		.tip {
			width: 48rpx;
			height: 48rpx;
			margin-right: 18rpx;
		}

		.refund-right {
			width: 48rpx;
			height: 48rpx;
			transition: transform 0.3s ease;
		}


	}

	.refund-details {

         padding: 10rpx 16rpx;
		
		color: #333;
		// background: #f9f9f9;


	}
.rotate {
  transform: rotate(90deg);
}
	.go-buy {
		padding: 0 16rpx;
		height: 148rpx;
		
		.kefu{
			width: 68rpx;
			flex-direction: column;
			text{
				height: 48rpx;
			}
		}
		.kefu-img {
			width: 60rpx;
			height: 60rpx;
		}
		.buy-btn{
			width: 578rpx;
			height: 108rpx;
			color: #fff;
			background-color: #FA8C16;
			display: flex;
			align-items: center;
            justify-content: center;
			font-size: 48rpx;
		}
	}
	.qr-code-modal {
	  position: fixed;
	  top: 50%;
	  left: 50%;
	  transform: translate(-50%, -50%);
	  background-color: white;
	  padding: 20px;
	  border-radius: 10px;
	  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	  z-index: 1000;
	}
	
	.qr-code {
	  width: 200px;
	  height: 200px;
	}
</style>