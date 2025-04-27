<template>
	<view class="container">
		<view class="course-item">

			<image :src="course.image" class="course-image" @click="goDes(course)"></image>

			<view class="course-info">
				<view class="flex course-title">

					{{course.title}}
				</view>
				<view class="course-description">
					<text style="color: #333;">产品介绍：</text>
					<text>{{ course.description }}</text>
				</view>
				<view class="course-description">
					<text style="color: #333;">特色介绍：</text>
					<text>{{ course.productfeature }}</text>
				</view>
				<view class="course-price-bought">
					<view class="price" v-if="course.level == 1">
						<text style="color: #777;">¥</text>
						<text style="text-decoration: line-through;color: #777;" class="course-price">{{ course.price }}</text>
						<text class="course-price" style="margin-left: 12rpx;" >¥{{ course.couponprice}}</text>
					</view>
					<view class="price" v-else>
						<text>¥</text>
						<text class="course-price">{{ course.price }}</text>
						<text style="font-size: 32rpx;" v-if="course.id ==2">起</text>
					</view>
					<text class="course-bought">累计已有{{ course.paycount }}人报名</text>
				</view>
			</view>

		</view>
			<view class="coupon" v-if="course.level !=1 && !isOrder">
			<view class="coupon-dec">
				<view class="coupon-money">
					<text>199</text>
					<text style="margin: 0 14rpx;">代</text>
					<text style="margin-right: 14rpx;">6000</text>
					<text>元代金券</text>
				</view>

				<view class="tip-title">
					<text style="margin-right: 16rpx;">该产品只限用{{course.couponcount}}张</text>
					<text>下单时可购买</text>
				</view>
			</view>
			<button @click="goToCheckout" >领劵下单</button>
		</view> 
		 <view class="product-change" v-if="course.level !=1 && !isOrder">
			<view class="title">
				产品选择
			</view>
			<view class="product">
				<view  @click="changeId(item)" class="product-box" :class="{active:item.id ==id}" v-for="(item,index) in courseList" :key="index">
					<view class="product-box-title">
						<text
							style="color: #FF2600;font-size: 24rpx;width: 24rpx;display: flex; justify-content: center;">¥</text>
						<text style="color: #FF2600;font-size: 40rpx;">{{item.price}}</text>
						<text class="flex align-center justify-between price">劵后价¥{{item.couponprice}}</text>
						<text style="color: #333;font-size: 32rpx;">{{item.pname}}</text>
					</view>
					<view class="product-box-des">
						{{item.productfeature}}
					</view>
				</view>
				
			</view>
		</view>
		<view class="refund">
			<view class="refund-left">
				<image class="tip" src="../../static/image/refundTip.jpg" mode="aspectFit"></image>
				<text>不满意超额退款</text>
			</view>

		</view>
		<view class="refund-details">

			<text v-if="course.level != 1">{{course.refundrules}}</text>
			<image  v-else :src="course.refundrules" mode="aspectFill"></image>
		</view>
		<view v-if="course.level ==1 && !isOrder" class="go-buy flex justify-between align-center">

			<button class="kefu flex " style="background-color: transparent;margin: 0;" @contact="concat"
				open-type="contact">
				<image class="kefu-img" src="../../static/image/kefu.jpg" mode="aspectFill"></image>
				<text>客服</text>
			</button>
			<button class="buy-btn" @click="onClickItem(course)">立即购买</button>
		</view>
		<view v-if="course.level !=1 && !isOrder" class="go-buy flex justify-between align-center">

			<button class="kefu flex " style="background-color: transparent;margin: 0;" @contact="concat"
				open-type="contact">
				<image class="kefu-img" src="../../static/image/kefu.jpg" mode="aspectFill"></image>
				<text>客服</text>
			</button>
			<button class="team" @click="team">团队详情</button>
			<button class="get-coupon-order" @click="goToCheckout" >领劵下单</button>
		</view>

	</view>
</template>

<script>
	import { getProductDetail,getMoreProductDetail } from '../../api/product';
	
	export default {
		data() {
			return {
				course: {},
				courseList:[],
				isOrder: false,
				id: "",
				level:"", //levle 1为 9.9特价没有优惠券的，2为多选产品 ，3为单个有优惠券
			};
		},
		onLoad(options) {
			this.id = options.id;
			this.isOrder = options.orderId || false;
			this.level = options.level;
			if(options.level == 2){
				//获取多个产品
				 this.getMoreDetail();	
			}else{
			  this.getDetail(options.id);	
			}
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
		 async	getDetail(id) {
				const res = await getProductDetail(id)
					
				if(res?.data?.data){
					this.productInfo([res?.data?.data])
				}else{
					uni.showToast({
						title:"获取产品详情失败"
					})
				}
				
			},
		 async	getMoreDetail(){
			   let res = await getMoreProductDetail();
			   if(res?.data?.rows.length > 0){
				   this.productInfo(res?.data?.rows)
			   }else{
					uni.showToast({
						title:"获取产品详情失败"
					})
				}
			},
			 //处理数据
			 productInfo(data){
				data.map(item =>{
					let course = {
						...item,
						paycount: item.paycount || 10,
						isRefundDetailsVisible: false,
					    title: item?.pname,
					    image: item?.pimage,
					    description: item.introduce,
					}
					if(this.id == item.id){
						this.course = course;
					}
					this.courseList.push(course)
					
				})
				//console.log(this.course)
			 },
			changeId(item){
				this.id =item.id;
			},
			onClickItem(item) {

				//判断当前用户是否手机号授权，如果有则直接下一步，否则跳转到登录页面授权
				let userInfos = this.$store.state.userInfos
                     // console.log(userInfos,888)
				if (!userInfos || userInfos?.phoneNumber?.length != 11) {
					uni.showModal({
						content: '下单需要手机号授权',
						confirmText: '去登录',
						success: (res) => {
							if (res.confirm) {
								// 保存当前页面的路径和参数
								const currentPage = getCurrentPages().pop();
								const route = currentPage.route;
								const options = currentPage.options;
								uni.setStorageSync('redirectInfo', {
									route,
									options
								});
								uni.navigateTo({
									url: '/pages/login/index'
								});
								
							}
						}
					});
				}else{
					//发起支付，获取支付鉴权信息
					this.$request({
						url: "/ballFishing/wechat/pay",
						method: "post",
						data: {
							productid: item.id,
							price: item.price,
							phonenumber:this.$store.state.userInfos.phoneNumber,
							 totalfee:item.couponprice,
							// totalfee: 0.01,
							userid: this.$store.state.userInfos.uid,
							//wxopenid:this.$store.state.userInfos.openid
						}
					}).then((res) => {
						//console.log(res,111)
						if (res?.data?.data) {
							this.pay(res?.data?.data)
						} else {
							uni.showToast({
								title: "购买失败"
							})
						}
					
					}).catch(() => {
						uni.showToast({
							title: "购买失败"
						})
					})
				}
			},
			//支付
			pay(payParams) {
				uni.requestPayment({
					provider: 'wxpay',
					timeStamp: payParams.timeStamp,
					nonceStr: payParams.nonceStr,
					package: payParams.package,
					signType: payParams.signType,
					paySign: payParams.paySign,
					success: (res) => {
						//  console.log('支付成功', res);
						uni.navigateTo({
							url: '/pages/baseForm/index?orderId=' + payParams.orderid
						});
						// 处理支付成功的逻辑
					},
					fail: (err) => {
						console.error('支付失败', err);
						// 处理支付失败的逻辑
						uni.showModal({
							title: "支付失败"
						})
					},
					complete: (res) => {
						console.log('支付完成', res);
						// 处理支付完成的逻辑
					}
				});
			},

			concat(e) {
				console.log(e, 11)
			},
			toggleRefundDetails(i) {
				this.course.isRefundDetailsVisible = !this.course.isRefundDetailsVisible
			},
			//去下单页面
			goToCheckout() {
				let userInfos = this.$store.state.userInfos
				     // console.log(userInfos,888)
				if (!userInfos || userInfos?.phoneNumber?.length != 11) {
					uni.showModal({
						content: '下单需要手机号授权',
						confirmText: '去登录',
						success: (res) => {
							if (res.confirm) {
								// 保存当前页面的路径和参数
								const currentPage = getCurrentPages().pop();
								const route = currentPage.route;
								const options = currentPage.options;
								uni.setStorageSync('redirectInfo', {
									route,
									options
								});
								uni.navigateTo({
									url: '/pages/login/index'
								});
								
							}
						}
					});
				}else{
					uni.navigateTo({
						url: `/pages/order/checkout/index?id=${this.id}&level=${this.level}`
					});
				}
			},
			team(){
				this.$request({
					url: "/functionMenu/poster/list",
					method: "get",
					data: {
						module: "团队详情"
					}
				}).then((res)=>{
					//console.log(res,111)
					if(res?.data?.rows.length > 0)
					uni.navigateTo({
						url: `/pages/banner/index?list=${res?.data?.rows[0].bimage}`
					})	
				})
			}
		}
	};
</script>

<style lang="scss" scoped>
	.container {
		gap: 16rpx;
		background-color: #eeeceb;
		height: calc(100vh - 164rpx);
		overflow: scroll;
	}

	.course-item {

		border-radius: 16rpx;
		margin-bottom: 16rpx;

		position: relative;
	}

	.course-image {
		width: 100%;
		height: 440rpx;
		// border-top-left-radius: 16rpx;
		// border-top-right-radius: 16rpx;
		margin-bottom: 16rpx;
	}

	.course-info {

		// margin-top: 16rpx;
		padding: 16rpx 24rpx;
		background-color: #fff;
	}

	.course-title {
		font-weight: bold;
		height: 62rpx;

		font-size: 44rpx;
		color: #333;
	}

	.course-description {
		color: #777;
		font-size: 36rpx;
		margin: 14rpx 0 16rpx 0;

	}

	.coupon {
		height: 152rpx;
		display: flex;
		padding: 16rpx 24rpx;
		justify-content: space-between;
		background: #fff;
		margin-bottom: 16rpx;

		.coupon-dec {
			display: flex;
			flex-direction: column;

			.coupon-money {
				height: 62rpx;
				color: #FA8C16;
				font-size: 44rpx;
			}

			.tip-title {
				height: 50rpx;
				color: #aaa;
				font-size: 36rpx;
				margin-top: 8rpx;
			}
		}

		button {
			width: 170rpx;
			height: 120rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			margin: 0;
			border-radius: 16rpx;
			background-color: #FA8C16;
			font-size: 36rpx;
			color: #fff;
		}
	}

	.course-price-bought {
		display: flex;
		justify-content: space-between;
		height: 56rpx;
		align-items: center;

		.price {
			color: #FF2600;
			font-size: 24rpx;
		}
	}

	.course-price {
		font-size: 48rpx;
	}

	.course-bought {
		color: #333;
		font-size: 32rpx;
		display: flex;

	}

	.refund {
		padding: 16rpx 24rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		height: 100rpx;
		font-size: 36rpx;
		background-color: #fff;
		color: #333;

		.refund-left {
			display: flex;
			align-items: center;
		}

		.tip {
			width: 48rpx;
			height: 48rpx;
			margin-right: 14rpx;
		}

		.refund-right {
			width: 48rpx;
			height: 48rpx;
			transition: transform 0.3s ease;
		}


	}

	.refund-details {

		padding: 0rpx 24rpx 16rpx;
		background-color: #fff;
		color: #777;
		// background: #f9f9f9;


	}

	.rotate {
		transform: rotate(90deg);
	}

	.product-change {
		margin-bottom: 16rpx;
		background-color: #fff;
		padding: 16rpx 24rpx;

		.title {
			height: 50rpx;
			color: #333;
			font-size: 36rpx;
			align-items: center;
			justify-content: center;
		}

		.product {

			.product-box {
				width: 100%;
				border-radius: 24rpx;
				display: flex;
				padding: 16rpx;
				background-color: #fff;
				font-size: 32rpx;
				flex-direction: column;
				color: #333;
				border: 1px solid #E6E6E6;
				margin-bottom: 16rpx;

				.product-box-title {
					display: flex;
					flex-wrap: wrap;
					align-items: baseline;
					height: 46rpx;
					margin-bottom: 16rpx;

					.price {
						color: #fff;
						font-size: 28rpx;
						height: 40rpx;
						padding: 0 6rpx;
						border-radius: 8rpx;
						background: linear-gradient(to right, #FF1C6E, #FF5029);
						margin: 0 14rpx;
					}
				}
			}

			:last-child {
				margin-bottom: 0;
			}

			.active {
				background-color: #FDF6EB;
				border: 1px solid #FA8C16;
			}
		}
	}

	.go-buy {
		padding: 0 16rpx;
		height: 148rpx;
		position: fixed;
		bottom: 0;
		gap: 24rpx;
		width: 100%;
		background-color: #fff;

		.kefu {
			width: 68rpx;

			flex-direction: column;
		}

		.kefu-img {
			width: 60rpx;
			height: 60rpx;
		}

		.buy-btn {
			width: 578rpx;
			height: 108rpx;
			color: #fff;
			background-color: #FA8C16;
			display: flex;
			align-items: center;
			justify-content: center;
			font-size: 48rpx;
		}

		.get-coupon-order {
			width: 300rpx;
			height: 108rpx;
			border-radius: 16rpx;
			color: #fff;
			background-color: #FA8C16;
			font-size: 48rpx;
		}

		.team {
			width: 300rpx;
			height: 108rpx;
			border-radius: 16rpx;
			color: #FA8C16;
			background-color: #FDF6EB;
			font-size: 48rpx;
		}
	}
</style>