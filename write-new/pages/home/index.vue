<template>
	<view class="container">
		<!-- 轮播图 -->
		<view class="swiper-container">
			<swiper class="swiper" circular indicator-dots="true" autoplay="true" interval="5000"
				indicator-active-color="#fff" :duration="500">
				<swiper-item v-for="(item, index) in bannerTop" :key="index">
					<image :src="item.simage" mode="aspectFill"></image>
				</swiper-item>
			</swiper>
		</view>
		<view class="nav-lists">
			<view class="lis" @click="toRead(0)">
				<image src="https://lidoogroup.com/bwg.jpg" mode=""></image>
				<view class="title">
					人生博物馆
				</view>
			</view>
			<view class="lis" @click="toRead(1)">
				<image src="https://lidoogroup.com/hyl.jpg" mode=""></image>
				<view class="title">
					创写回忆录
				</view>
			</view>
			<view class="lis" @click="toDiscounts">
				<image src="https://lidoogroup.com/mrth.jpg" mode=""></image>
				<view class="title">
					每日特惠
				</view>
			</view>
			<view class="lis" @click="familyTree">
				<image src="https://lidoogroup.com/dzjp.jpg" mode=""></image>
				<view class="title">
					电子家谱
				</view>
			</view>
			<view class="lis">
				<image src="https://lidoogroup.com/kf.jpg" mode=""></image>
				<view class="title">
					线上客服
				</view>
				<button class="kefu" style="background-color: transparent;margin: 0;" open-type="contact"></button>
			</view>
			<view class="lis" @click="toActive">
				<image src="https://lidoogroup.com/hd.jpg" mode=""></image>
				<view class="title">
					更多
				</view>
			</view>
		</view>
		<view class="content">
			<view class="box-more">
				<view class="my-active flex  flex-direction">
					<uni-section type="line" title="限时体验" bgColor="transparent" titleColor="#333"></uni-section>
				</view>
				<image @click="courseDes" class="my-active-image" :src="bannerCourse" mode="aspectFill"></image>
			</view>
			<view class="box-more">
				<view class="my-active flex  flex-direction">
					<uni-section type="line" title="优秀回忆录推荐" bgColor="transparent" titleColor="#333"></uni-section>
				</view>
				<image @click="toRead(0)" class="my-active-image" :src="bannerRead" mode="aspectFill"></image>

			</view>
			<view class="box-more">
				<view class="my-active flex  flex-direction">
					<uni-section type="line" title="会员优惠" bgColor="transparent" titleColor="#333"></uni-section>
				</view>
				<view class="swiper-box-container">
					<swiper class="swiper" circular indicator-dots="true" autoplay="true" interval="5000"
						indicator-active-color="#fff" :duration="500">
						<swiper-item v-for="(item, index) in banner" :key="index" @click="toDes(item)">
							<image :src="item.simage" mode="aspectFill"></image>
						</swiper-item>
					</swiper>
				</view>
			</view>
			<view class="box-more">
				<view class="my-active flex  flex-direction">
					<uni-section type="line" title="最后告白" bgColor="transparent" titleColor="#333"></uni-section>
				</view>
				<image @click="toDes1" class="my-active-image" :src="bannerAfter.simage" mode="aspectFill"></image>
			
			
			</view>
		</view>

		<!-- 自定义 Tabbar -->
		<Cos-Tabbar :current="0" :tabBarList="tabBerLists" />
	</view>
</template>

<script>
	import CosTabbar from '../../component/CosTabbar.vue';
	import {
		tabBar
	} from '../../utils/tabBar.js'
	import {
		firstLogin,
		getCode,
		secondLogin
	} from '../../api/login';
	import {
		getHouse,
		getPedigreeInfo
	} from "../pedigreeMoudle/api/index"
	export default {
		components: {
			CosTabbar
		},
		data() {
			return {
				banner: [], //底部优惠广告
				bannerTop: [], //头部广告
				bannerCourse: "", //课程广告
				bannerRead: "", //阅读广告
				bannerAfter: "", //身后事
				dailyOffer: {}, // 每日优惠
				tabBerLists: [],
				userInfos: null,
			};
		},
		onLoad() {
			//1如果本地没有用户信息，传code进行新增用户
			//2 如果本地有信息则进行二次登录
			let userInfos = JSON.parse(uni.getStorageSync('userInfos') || "{}");

			if (!!userInfos && userInfos?.phoneNumber?.length == 11) {
				this.getWechatPayParamsSecond(userInfos?.phoneNumber)
			} else {
				this.handfirstLogin()
			}
			// 隐藏原生的tabbar,有自定义tabbar的页面都要写一遍
			uni.hideTabBar();
			this.getBanner();
			//朋友圈
			wx.showShareMenu({
			
			withShareTicket: true,
			
			menus: ['shareAppMessage', 'shareTimeline']
			
			});
		},
		onShow() {
			this.tabBerLists = uni.getStorageSync('tabBarList') || tabBar["huzhu"];
			// 自定义的tabbar赋值
		},
		//右上角分享
		onShareAppMessage(res) {
			if (res.from === 'button') { // 来自页面内分享按钮
				console.log(res.target)
			}
			return {
				title: '您叙说我采写，欢迎您来~',
				path: '/pages/home/index'
			}
		},
		methods: {
			async handfirstLogin() {
				let code = await getCode();
				//  console.log(code,777)
				if (code) {
					let res = await firstLogin(code);
					// console.log(res,11111)
					// 保存用户信息 
					this.saveUserInfo(res)
				} else {
					this.saveUserInfo([])
				}

			},
			getBanner() {
				this.$request({
					url: "/functionMenu/poster/list",
					method: "get",
					data: {
						module: "首页"
					}
				}).then((res) => {
					//console.log(res, 999)
					if (res?.data?.rows.length > 0) {
						let banners = res?.data?.rows

						banners.map(item => {
							if (item.module == "首页-顶部") {
								this.bannerTop.push(item)
							} else if (item.module == "首页-限时体验") {
								this.bannerCourse = item.simage
							} else if (item.module == "首页-推荐") {
								this.bannerRead = item.simage
							} else if (item.module == "首页-优惠") {
								this.banner.push(item)
							} else if (item.module == "首页-身后事") {
								this.bannerAfter = item
							}
						})

					} else {
						uni.showToast({
							title: "获取首页图片失败"
						})
					}
				})
			},
			toDes(item) {
				//如果有appid 跳转小程序，否则跳转详情
				//console.log(item,111)

				if (item?.appid) {
					uni.navigateToMiniProgram({
						appId: item.appid,
						path: item.bimage,
						success(res) {
							// 打开成功
						}
					})
				} else {
					uni.navigateTo({
						url: `/pages/banner/index?list=${item.bimage}`
					})
				}

			},
			toDes1() {
				uni.navigateTo({
					url: `/pages/banner/index?list=${this.bannerAfter.bimage}&type=epitaph&src=${this.bannerAfter.content}`
				})
			},
			toRead(type) {
				//tab栏不支持传参
				uni.setStorageSync('memoryType', type);
				uni.switchTab({
					url: `/pages/memory/index`
				})
			},
			toDiscounts() {
				uni.navigateTo({
					url: "/pages/discounts/index"
				})
			},
			toActive() {
				uni.navigateTo({
					url: "/pages/active/index"
				})
			},
			//电子家谱
			async familyTree() {
				//获取当前户主，如果有信息跳转到编辑页面，没有跳转到新增页面
				// let res = await getHouse(this.$store.state.userInfos.uid);
				// if (res?.data?.rows.length > 0) {
				// 	uni.setStorageSync("huzhuInfos", JSON.stringify(res?.data?.rows[0]));
				// 	this.getPedigree(res?.data?.rows[0].genealogyid)

				// } else {
				// 	uni.navigateTo({
				// 		url: '/pages/pedigreeMoudle/pedigreeRegister/index'
				// 	});
				// }
				this.$request({
					url: "/functionMenu/poster/list",
					method: "get",
					data: {
						module: "电子家谱"
					}
				}).then((res)=>{
					if (res?.data?.rows.length > 0) {
						let banners = res?.data?.rows[0];
						// console.log(banners,77)
						//this.bannerFamilyTree =banners;
					uni.navigateTo({
						url: `/pages/banner/index?list=${banners.simage}`
					})
					}
				})
			},
			async getPedigree(id) {
				let res = await getPedigreeInfo(id);
				if (res?.data?.data) {
					uni.setStorageSync('pedigree', JSON.stringify(res?.data.data));
					uni.navigateTo({
						url: '/pages/pedigreeMoudle/addPedigree/index'
					});
				}
			},
			showDevelopingTip() {
				uni.showModal({
					content: '暂未开通，敬请期待！',
					showCancel: false, // 不显示取消按钮
					confirmText: '确定' // 确定按钮的文字
				});
			},
			courseDes() {
				uni.navigateTo({
					url: '/pages/courseDetail/index?id=1&level=1'
				})
			},

			async getWechatPayParamsSecond(phoneNum) {
				const code = await getCode();

				if (code) {
					let res = await secondLogin(code, phoneNum);
					//console.log(res, 11)
					this.saveUserInfo(res)
				} else {
					this.saveUserInfo([])
				}

				// 保存用户信息 

			},
			//保存用户信息
			saveUserInfo(res) {
				if (res?.data?.code == 200) {
					//console.log(res.data,888)
					const token = res?.data?.token;
					this.$store.dispatch("setToken", token);
					const userInfos = {
						username: res?.data?.userinfo?.phonenumber,
						nickName: res?.data?.userinfo?.nickName,
						phoneNumber: res?.data?.userinfo?.phonenumber,
						uid: res?.data?.userinfo?.userId,
						role: res?.data?.userinfo?.rolename
					}
					this.$store.dispatch("setUser", userInfos);

				} else {
					uni.showModal({
						content: '获取用户信息失败，请重新登录',
						showCancel: false,
						confirmText: '去登录',
						success: (res) => {
							if (res.confirm) {
								uni.redirectTo({
									url: '/pages/login/index'
								});
							}
						}
					});
				}
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
		padding: 0 24rpx;
		overflow-y: auto;
	}

	.swiper-container {
		// position: fixed;
		// top: 0rpx;
		// left: 24rpx;
		width: 100%;
		z-index: 10;
		// padding:  24rpx;
		box-sizing: border-box;

		.swiper {
			height: 320rpx;
			border-radius: 16rpx;
			margin-bottom: 32rpx;

			image {
				width: 100%;
				height: 320rpx;
				border-radius: 16rpx;
			}

			::v-deep .uni-swiper-dot {
				height: 8rpx;
				width: 8rpx;
			}

			.wx-swiper-dot {
				height: 8rpx;
				width: 8rpx;
			}

			.wx-swiper-dot-active {
				width: 28rpx;
				/* 高亮时的宽度 */
				background-color: #fff;
				/* 高亮时的颜色 */
				border-radius: 8rpx;
				/* 高亮时的圆角 */
			}


			::v-deep .uni-swiper-dot-active {
				width: 28rpx;
				/* 高亮时的宽度 */
				background-color: #fff;
				/* 高亮时的颜色 */
				border-radius: 8rpx;
				/* 高亮时的圆角 */
			}

			::v-deep .uni-swiper-dots-horizontal {
				bottom: -2rpx;
			}
		}
	}

	.nav-lists {
		height: 452rpx;
		display: flex;
		width: 100%;
		flex-wrap: wrap;
		// position: fixed;
		// top: 352rpx;
		/* 调整到轮播图下方 */

		gap: 33rpx;

		.lis {
			position: relative;
			width: 212rpx;
			height: 210rpx;
			display: flex;
			flex-direction: column;
			padding: 16rpx 0;
			align-items: center;
			justify-content: center;
			color: #333;
			font-size: 36rpx;
			background-color: #fff;
			border-radius: 24rpx;

			image {
				width: 120rpx;
				height: 120rpx;
			}

			.title {
				margin-top: 8rpx;
			}

			.kefu {
				position: absolute;
				top: 0;
				left: 0;
				width: 212rpx;
				height: 210rpx;
				z-index: 1;
			}
		}
	}

	.content {
		flex: 1;
		margin-bottom: 180rpx;
		margin-top: 32rpx;
		/* 调整到 Tab 栏下方 */


		.box-more {
			margin-bottom: 24rpx;

			.my-active {
				height: 56rpx;
				display: flex;
				margin-bottom: 8rpx;
			}

			.my-active-image {
				width: 100%;
				height: 320rpx;
				border-radius: 24rpx;
			}

			.swiper-box-container {
				width: 100%;
				height: 320rpx;

				image {
					width: 100%;
					height: 320rpx;
					border-radius: 16rpx;
				}
			}
		}

	}
</style>