<template>
	<view class="container">
		<view class="course-item ">
			<view class="title flex align-center justify-between">
				<text>{{course.title}}</text>
				<view class="change-product" @click="changeProduct">
					<text>切换产品</text>
					<image class="tip" src="../../../static/image/right.jpg" mode="aspectFill"></image>
				</view>
			</view>
			<view class="product-des">
				{{course.productfeature}}
			</view>
			<view class="all-price">
				<view class="title">
					<text>消费总额</text>
					<view class="price-right">
						<text
							style="color: #FF2600;font-size: 24rpx;width: 24rpx;display: flex; justify-content: center;">¥</text>
						<text style="color: #FF2600;font-size: 40rpx;">{{course.price}}</text>
					</view>
				</view>
				<view class="tip">
					(最多使用{{upper[course.couponcount]}}张优惠券)
				</view>
			</view>
		</view>
		<view class="sale">
			<view class="title flex align-center justify-between">
				<view class="title-left">
					<text>已选择{{num}}张代金券，立省</text>
					<text style="color: #FF2600;margin: 0 14rpx">60000</text>
					<text>元</text>
				</view>
				<view class="change-product" @click="$refs.popupEdit.open()">
					<text>修改</text>
					<image class="tip" src="../../../static/image/right.jpg" mode="aspectFill"></image>
				</view>
			</view>
			<view class="select-page">
				<view class="des flex align-center">
					<text class="flex align-center justify-center" style="width: 38rpx;">¥</text>
					<text>6000</text>
					<text style="margin: 0 8rpx;">X</text>
					<text>{{num}}</text>
					<text style="margin-left:8rpx;">张</text>
				</view>
				<view class="info">
					199代6000,仅限{{upper[course.couponcount]}}张
				</view>
				<view class="tag">
					已选
				</view>
			</view>
			<view class="sale-bottom">
				<text>购劵金额¥{{ 199 * num}}，用劵抵扣</text>
				<text>-¥{{6000 * num}}</text>
			</view>
		</view>

		<view class="refund">
			<view class="refund-title">
				<image class="tip" src="../static/tip.png" mode="aspectFill"></image>
				<text>购买须知</text>
			</view>
			<view class="refund-info">
				该产品最多只可使用{{upper[course.couponcount]}}张优惠券，购买优惠券下单后，等待客服与您联系，沟通相关细节，并线下签约合同及付剩余款项。
			</view>
		</view>

		<view class="foot">
			<view class="title">
				<text>购券金额￥{{ 199 * num}} + 剩余应付</text>
				<text style="color: #FF2600;">￥{{atherPrice}}</text>
				<text style="color: #C41A1A;">(线下签约后支付)</text>
			</view>
			<view class="submit" @click="onClickItem">
				<text>确认下单</text>
				<text style="font-size: 36rpx;">￥</text>
				<text>{{ 199 * num}}</text>
			</view>
		</view>
		<uni-popup ref="popupProduct" type="bottom" border-radius="10px 10px 0 0">
			<view class="product-change">
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
				<image @click="closeProduct" class="close" src="../static/close.png" mode=""></image>
			</view>
		</uni-popup>

		<uni-popup ref="popupEdit" type="bottom" border-radius="10px 10px 0 0">
			<view class="product-change juan-change">
				<view class="title">
					修改用劵
				</view>
				<view class="juan-title">
					<text>可用代金券</text>
					<text style="font-size: 28rpx;">已选{{num}}张</text>
				</view>
				<view class="select-num">
					<view class="title1">
						6000元代金券
					</view>
					<view class="title2">
						<text>199代6000元，仅限{{course.couponcount}}张</text>
						<view class="sum">
							<image v-show="num >1" src="../static/subActive.jpg" mode=""  @click="decrement"></image>
							<image v-show="num ==1" src="../static/sub.png" mode=""></image>
							<text>{{num}}</text>
							<image v-show="num == course.couponcount" src="../static/add.png" mode=""></image>
							<image v-show="num < course.couponcount" src="../static/addActive.png"  @click="increment"  mode=""></image>
						</view>
					</view>
					<view class="title3">
						<text style="font-size: 24rpx;">¥</text>
						<text style="font-size: 48rpx;font-weight: 500;">{{199 * num}}</text>
					</view>
				</view>

				<image @click="closeJuan" class="close" src="../static/close.png" mode=""></image>
				<view class="foot1">
					<view class="title">
						<text>购券金额￥{{199 * num}} 用券抵扣</text>
						<text style="color: #FF2600;">-￥{{6000 * num}}</text>

					</view>
					<view class="submit" @click="closeJuan">
						<text>确认</text>
					</view>
				</view>
			</view>
		</uni-popup>

	</view>
</template>

<script>
	import {
		getProductDetail,
		getMoreProductDetail
	} from '../../../api/product';
	export default {
		data() {
			return {
				course: {},
				courseList: [],
				id: "",
				upper:["一","一","","","","五"],
				num:"",
				atherPrice:"",
				
			};
		},
		onLoad(options) {
			this.id = options.id;
			this.level = options.level;
			if (options.level == 2) {
				//获取多个产品
				this.getMoreDetail();
			} else {
				this.getDetail(options.id);
			}
		},
		
		methods: {
			async getDetail(id) {
				const res = await getProductDetail(id)

				if (res?.data?.data) {
					this.productInfo([res?.data?.data])
				} else {
					uni.showToast({
						title: "获取产品详情失败"
					})
				}

			},
			async getMoreDetail() {
				let res = await getMoreProductDetail();
				if (res?.data?.rows.length > 0) {
					this.productInfo(res?.data?.rows)
				} else {
					uni.showToast({
						title: "获取产品详情失败"
					})
				}
			},
			//处理数据
			productInfo(data) {
				data.map(item => {
					let course = {
						...item,
						paycount: item.paycount || 10,
						isRefundDetailsVisible: false,
						title: item?.pname,
						image: item?.pimage,
						description: item.introduce,
					}
					if (this.id == item.id) {
						this.course = course;
						this.num =item.couponcount;
						this.atherPrice = item.price - this.num * 6000;
					}
					this.courseList.push(course)

				})
				//console.log(this.course)
			},
			changeId(item) {
				this.id = item.id;
				this.course = this.courseList.find((item)=>item.id == this.id);
				this.atherPrice = this.course.price - this.num * 6000;
			},
			onClickItem() {
				// console.log(this.$store.state.userInfos,this.$store.state.userInfos.openid)
				//发起支付，获取支付鉴权信息
				this.$request({
					url: "/ballFishing/wechat/pay",
					method: "post",
					data: {
						productid: this.course.id,
						price: this.course.price,
						couponprice:this.atherPrice,
						// totalfee:item.price,
						phonenumber:this.$store.state.userInfos.phoneNumber,
						totalfee: this.num * 199,  
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
			changeProduct() {
				this.$refs.popupProduct.open()
			},
			closeProduct() {
				this.$refs.popupProduct.close()
			},
			closeJuan(){
				this.$refs.popupEdit.close()
			},
			//减少按钮
			decrement(){
				this.num--;
				this.atherPrice = this.course.price - this.num * 6000;
			},
			//新增按钮
			increment(){
				this.num++;
				this.atherPrice = this.course.price - this.num * 6000;
			}

		}
	};
</script>

<style lang="scss" scoped>
	.container {
		padding: 16rpx 24rpx;
		background-color: #eeeceb;
		height: calc(100vh - 164rpx);
		overflow: scroll;
	}

	.course-item {
		padding: 16rpx;
		background-color: #fff;
		color: #333;
		font-size: 36rpx;
		margin-bottom: 24rpx;

		.title {
			height: 50rpx;
			color: #333;
			font-size: 36rpx;
			margin-bottom: 16rpx;
			border-radius: 20rpx 20rpx 0 0;

			.change-product {
				color: #777;
				font-size: 28rpx;
				display: flex;
				align-items: center;

				image {
					width: 28rpx;
					height: 28rpx;
					margin-left: 8rpx;
				}
			}
		}

		.product-des {
			color: #777;
			font-size: 32rpx;
			margin-bottom: 16rpx;
		}

		.all-price {
			height: 156rpx;
			background-color: #fff;
			border-radius: 24rpx;
			padding: 20rpx 14rpx 24rpx;
			border: 1px solid #F8F7F6;

			.title {
				display: flex;
				height: 50rpx;
				align-items: center;
				justify-content: space-between;
				color: #333;
				font-size: 36rpx;
				margin-bottom: 8rpx;

				.price-right {
					display: flex;
					align-items: baseline;
				}
			}

			.tip {
				height: 44rpx;
				color: #C41A1A;
				font-size: 32rpx;
			}
		}
	}

	.sale {
		padding: 16rpx;
		background-color: #fff;
		color: #333;
		font-size: 36rpx;
		margin-bottom: 24rpx;

		.title {
			height: 50rpx;
			color: #333;
			font-size: 36rpx;
			margin-bottom: 16rpx;

			.change-product {
				color: #777;
				font-size: 28rpx;
				display: flex;
				align-items: center;

				image {
					width: 28rpx;
					height: 28rpx;
					margin-left: 8rpx;
				}
			}
		}

		.select-page {
			height: 132rpx;
			background-color: #FDF6EB;
			border-radius: 18rpx;
			color: #915F46;
			font-size: 36rpx;
			padding: 22rpx 16rpx 16rpx;
			position: relative;
			margin-bottom: 16rpx;

			.des {
				height: 50rpx;
				margin-bottom: 8rpx;
			}

			.info {
				height: 40rpx;
				color: #aaa;
				font-size: 28rpx;
			}

			.tag {
				position: absolute;
				top: 0;
				right: 0;
				height: 42rpx;
				width: 56rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				color: #fff;
				background-color: #FA8C16;
				font-size: 24rpx;
				border-top-right-radius: 18rpx;
			}
		}

		.sale-bottom {
			color: #333;
			font-size: 32rpx;
			height: 44rpx;
			display: flex;
			justify-content: flex-end;
		}
	}

	.refund {
		font-size: 32rpx;
		color: #777;

		.refund-title {
			display: flex;
			align-items: center;
			height: 44rpx;
		}

		.tip {
			width: 40rpx;
			height: 40rpx;
			margin-right: 14rpx;
		}

		.refund-info {
			padding-left: 48rpx;
			font-size: 28rpx;
		}
	}

	.foot {
		width: 100%;
		position: fixed;
		bottom: 0;
		left: 0;
		height: 204rpx;
		background-color: #fff;
		color: #333;
		font-size: 28rpx;
		padding: 16rpx;

		.title {
			display: flex;
			align-items: center;
			justify-content: center;
			height: 40rpx;
			margin-bottom: 24rpx;
		}

		.submit {
			height: 108rpx;
			padding: 30rpx 0;
			background-color: #FA8C16;
			border-radius: 16rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			color: #fff;
			font-size: 48rpx;
		}
	}

	.product-change {
		height: 56vh;
		color: #333;
		font-size: 36rpx;
		padding: 0 24rpx;
		background-color: #eeeceb;
		border-top-right-radius: 24rpx;
		border-top-left-radius: 24rpx;
		position: relative;

		.close {
			width: 48rpx;
			height: 48rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			top: 25rpx;
			right: 18rpx;
			z-index: 99;
			position: absolute
		}

		.title {
			height: 98rpx;
			display: flex;
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

	.juan-change {
		.juan-title {
			height: 50rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			color: #333;
			font-size: 36rpx;
			margin-bottom: 16rpx;
		}

		.select-num {
			background-color: #fff;
			border-radius: 22rpx;
			padding: 16rpx;
			color: #333;
			font-size: 32rpx;
			margin-bottom: 16rpx;

			.title1 {
				height: 44rpx;
				display: flex;
				align-items: center;
				margin-bottom: 16rpx;
			}

			.title2 {
				color: #aaa;
				font-size: 28rpx;
				height: 50rpx;
				display: flex;
				align-items: center;
				margin-bottom: 16rpx;
				justify-content: space-between;

				.sum {
					display: flex;
					align-items: center;
					color: #333;
					font-size: 36rpx;
					gap: 22rpx;

					image {
						width: 40rpx;
						height: 40rpx;
					}
				}
			}
		}

		.title3 {
			color: #FF2600;
			display: flex;
			align-items: baseline;
		}

		.foot1 {
			width: 100%;
			position: fixed;
			bottom: 0;
			left: 0;
			height: 204rpx;
			background-color: #fff;
			color: #333;
			font-size: 28rpx;
			padding: 16rpx;

			.title {
				display: flex;
				align-items: center;
				justify-content: center;
				height: 40rpx;
				margin-bottom: 24rpx;
			}

			.submit {
				height: 108rpx;
				padding: 30rpx 0;
				background-color: #FA8C16;
				border-radius: 16rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				color: #fff;
				font-size: 48rpx;
			}
		}
	}
</style>