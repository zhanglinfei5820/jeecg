<template>
	<view class="container">

		<view class="flex box" style="padding-top: 30%;">
			<text style="flex:1">户主名</text>
			<view style="flex:3" class="input-set">
				<input class="uni-input" placeholder="请输入户主名称" v-model="userName" />
			</view>

		</view>
		<view class="flex box">
			<text style="flex:1">手机号</text>
			<view style="flex:3" class="input-set">
				<input v-model="phoneNum" type="number" placeholder="请输入户主手机号" />
			</view>
		</view>
		<button style="width: 60%;margin-top: 80upx;" type="primary" @click="login">确定</button>
	</view>
</template>

<script>
	import request from '../../../request/index'
	export default {
		data() {
			return {
				phoneNum: "", //手机号
				userName: "", //密码
			}
		},
		onLoad() {

		},
		methods: {

			login() {
				if (this.phoneNum.length != 11) {
					uni.showToast({
						title: "请输入正确格式手机号"
					})
					return
				}
				if (this.phoneNum == '' || this.userName == "") {
					uni.showToast({
						title: "不能为空"
					})
					return
				}

				request({
					url: "/functionMenu/collectiontask/selecthuzhu",
					method: "get",
					data: {
						"nickName": this.userName,
						"rolename": "huzhu",
						"phonenumber": this.phoneNum
					}
				}).then((res) => {
					// console.log(res, 99)
					if (res?.data?.code == 200) {

						//如果新增跳转，存在跳转到add页面,保存户主信息
						if (res?.data?.data) {

							// uni.setStorageSync("huzhuInfos", JSON.stringify(res?.data?.data));
							this.$store.dispatch("setHuzhu",res?.data?.data);
							uni.navigateTo({
								url: `/pages/pedigreeMoudle/addPedigree/index`
							});
						} else {
							uni.navigateTo({
								url: `/pages/pedigreeMoudle/pedigreeRegister/index?userName=${this.userName}&phoneNum=${this.phoneNum}`
							});
						}

					}

				})

			}
		}
	}
</script>

<style lang="scss" scoped>
	.container {
		width: 100%;
		height: calc(100vh - 88upx);
		display: flex;
		flex-direction: column;

		.box {
			padding: 20upx;
			border-bottom: 1upx solid #eee;
		}
	}
</style>