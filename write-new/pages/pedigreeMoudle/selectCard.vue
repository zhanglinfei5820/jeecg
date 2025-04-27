<template>
	<view v-show="isShow" class="container1" >
		<view style="margin-top: 25%;">
			<view class="flex" style="height: 180rpx; padding:0 176rpx;">
				<view class="flex-1 flex justify-between">
					<view class="flex box relative" @click.stop="addInfo('parent')">
						<image class="img" src="https://lidoogroup.com/grandpa-active.png" mode="aspectFit"></image>

						<view v-if="!isClickParent" class="overlay"></view>
					</view>
					<view class="flex box" @click.stop="addInfo('mom')" style="position: relative;">
						<image class="img" src="https://lidoogroup.com/grandma.png" mode="aspectFit"></image>
						<!-- <text>添加母亲</text> -->
						<view class="overlay"></view>
					</view>

				</view>
				
			</view>
			<view class="flex" style="height: 400rpx;margin-top: 10rpx;padding:0 32rpx;">
				<view style="width: 180rpx;" class="align-center flex-direction">
					<view style="margin-bottom: 40rpx;" class="flex  box relative" @click.stop="addInfo('wife')">
						<image class="img" src="https://lidoogroup.com/mom-active.png" mode="aspectFit"></image>
						<!-- <text>添加配偶</text> -->
						<view v-if="!isClickWife" class="overlay"></view>
					</view>
					<view class="flex  box relative" @click.stop="hide">
						<image class="img" src="https://lidoogroup.com/close.png" mode="aspectFit"></image>

					</view>
				</view>
				<view style="width: 260rpx;margin: 0 32rpx;" class="flex" @click.stop="addInfo('me')">
					<view class="flex box1 relative">

						<image class="img" src="https://lidoogroup.com/parent-active.png" mode="aspectFit"></image>
						<!-- <text>{{name}}</text> -->
						<!-- <text class="size-12">（户主）</text> -->
						<view v-if="!isClickSelf" class="overlay"></view>
					</view>
				</view>
				<view style="width: 180rpx;" class="flex align-center flex-direction ">
					<view style="margin-bottom: 40rpx;" class="flex box" @click.stop="addInfo('son')">
						<image class="img" src="https://lidoogroup.com/son-active.png" mode="aspectFit"></image>
						<!-- <text>添加儿子</text> -->
					</view>
					<view class="flex box" @click.stop="addInfo('daughter')">
						<image class="img" src="https://lidoogroup.com/daughter-active.png" mode="aspectFit"></image>
						<!-- <text>添加女儿</text> -->
					</view>
				</view>
			</view>
			<view class="flex" style="height: 180rpx; padding:0 176rpx;margin-top: 10rpx;">
				
				<view class="flex-1 flex justify-between">
					<view class="flex box relative" @click.stop="addInfo('grandson')">
						<image class="img" src="https://lidoogroup.com/grandson-active.png" mode="aspectFit"></image>
						<!-- <text>添加孙子</text> -->
						<view v-if="!isClickSon" class="overlay"></view>
					</view>
					<view class="flex box relative" @click.stop="addInfo('granddaughter')">
						<image class="img" src="https://lidoogroup.com/granddaughter-active.png" mode="aspectFit"></image>
						<!-- <text>添加孙女</text> -->
						<view v-if="!isClickSon" class="overlay"></view>
					</view>
				</view>
				
			</view>
		</view>
		<button @click="hide()" class="btn" type="default">关闭</button>
	</view>
</template>

<script>
	export default {
		props: {
			isShow: {
				type: Boolean,
				default: false
			},
			id1: {
				type: String | Number,
				default: ""
			},
			treeData: {
				type: Array,
				default: []
			}
		},
		data() {
			return {
				name: "",
				sex: "",
				isClickSon: false, //允许点击孙子信息
				isClickParent: true, //允许点击父亲信息
				isClickWife: true, //允许点击配偶信息
				isClickSelf: true, // 允许自己点击
			}
		},
		created() {
			// 获取当前族谱信息，如果有儿子，孙子信息可以点击，否则先添加儿子女儿，如果配偶和父亲填写一次则禁止

			// let treeData = JSON.parse( uni.getStorageSync("treeData") || "{}");

			// console.log(huzhuData,111) 
			// console.log(huzhuData,111) 
		},

		watch: {
			treeData: {
				handler(newData, old) {
					let treeData = newData;
					//	 console.log(treeData,111)
					let huzhuData = [];
					if (treeData.length == 0) {
						return
					}
					if (!treeData[0]?.huzhu) {
						// console.log(777)
						this.isClickParent = false;
						huzhuData = treeData[0].children;
					} else {
						huzhuData = treeData;
					}
					if (huzhuData[0].spouse?.length > 0) {
						this.isClickWife = false;
					}
					if (huzhuData[0].children?.length > 0) {
						this.isClickSon = true;
					}
					if (!!huzhuData[0].isAdd) {
						this.isClickSelf = false;
					}
					this.name = huzhuData[0]?.name;
					this.sex = huzhuData[0]?.sex;
				},
				immediate: true,
				deep: true
			}
		},
		onLoad() {

		},
		onShow() {

		},
		methods: {
			//添加父亲
			addInfo(type) {
				//console.log(type,11)
				if (type != "mom") {
					if (type == "parent" && !this.isClickParent) {
						return
					}
					if (type == "wife" && !this.isClickWife) {
						return

					}
					if ((type == "grandson" || type == "granddaughter") && !this.isClickSon) {
						return

					}
					if (type == "me" && !this.isClickSelf) {
						return

					}

					let sex = 0;
					if (type == "me" || type == "son" || type == "parent" || type == "grandson") {
						sex = 0
					} else {
						sex = 1
					}
					//如果是户主当做编辑，而不是新增
					if (type == "me") {
						let huzhuInfos = JSON.parse(uni.getStorageSync("huzhuInfos") || "{}");
						let id = huzhuInfos.id;

						uni.navigateTo({
							url: `/pages/pedigreeMoudle/writePedigree/index?type=${type}&name=${this.name}&id=${this.id1}&sex=${this.sex}`
						});

					} else {
						uni.navigateTo({
							url: `/pages/pedigreeMoudle/writePedigree/index?type=${type}&id=${this.id1}&sex=${sex}`
						});
					}
				}

			},
			hide() {
				this.$emit('hide');

			}
		}

	}
</script>

<style lang="scss" scoped>
	.container1 {
		background: rgba(0, 0, 0, 0.5);
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100% !important;
		z-index: 99;

		.box {
			width: 180rpx;
			display: flex;
			flex-direction: column;
			align-items: center;
			font-size: 24rpx;
			justify-content: center;

			// background: #fff;
			.img {
				width: 180rpx;
				height: 180rpx;
			}
		}

		.box1 {
			width: 260rpx;
			display: flex;
			align-items: center;
			font-size: 36rpx;
			.img {
				width: 260rpx;
				height: 260rpx;
			}
		}

		.btn {
			width: 702rpx;
			border-radius: 16rpx;
			height: 84rpx;
			margin-top: 74rpx;
			letter-spacing: 20rpx;
			background-color: #FA8C16;
			color: #fff;
			font-size: 40rpx;
		}
	}

	.relative {
		position: relative;
	}

	.overlay {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(255, 255, 255, 0.5);
		/* 半透明白色遮罩 */
		z-index: 9999;

	}
</style>