<template>
	<view class="container">
		<view class="title flex align-center">
			<uni-section bgColor="transparent" type="line" title="基础信息评估" titleColor="#FA8C16"></uni-section>
		</view>
		<image class="title-icon" src="https://lidoogroup.com/baseForm.png" mode="aspectFill"></image>
		<view class="form-box">
			<view class="form-item">
				<text>姓名</text>
				<textarea v-model="formData.name" placeholder="请输入姓名" auto-height="true" class="auto-resize-textarea" />
			</view>
			<view class="form-item">
				<text>出生年月</text>
				<textarea v-model="formData.birthDate" placeholder="请输入出生年月" auto-height="true"
					class="auto-resize-textarea" />
			</view>
			<view class="form-item">
				<text>性别</text>
				<textarea v-model="formData.sex" placeholder="请输入性别" auto-height="true" class="auto-resize-textarea" />
			</view>
			<view class="form-item">
				<text>最高学历</text>
				<textarea v-model="formData.highestEducation" placeholder="请输入最高学历" auto-height="true"
					class="auto-resize-textarea" />
			</view>
			<view class="form-item">
				<text>职业</text>
				<textarea v-model="formData.occupation" placeholder="请输入职业" auto-height="true"
					class="auto-resize-textarea" />
			</view>
			<view class="form-item">
				<text>出生地</text>
				<textarea v-model="formData.birthPlace" placeholder="请输入出生地" auto-height="true"
					class="auto-resize-textarea" />
			</view>
			<view class="form-item">
				<text>主要学习地址</text>
				<textarea v-model="formData.mainStudyAddress" placeholder="请输入主要学习地址" auto-height="true"
					class="auto-resize-textarea" />
			</view>
			<view class="form-item">
				<text>主要工作地</text>
				<textarea v-model="formData.mainWorkAddress" placeholder="请输入主要工作地" auto-height="true"
					class="auto-resize-textarea" />
			</view>
			<view class="form-item">
				<text>迁徙经历</text>
				<textarea v-model="formData.migrationHistory" placeholder="例如:1960年因父母工作调动,从**地迁移到**地" auto-height="true"
					class="auto-resize-textarea" />
			</view>
		</view>
		<button @click="submitForm">提交</button>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				orderId: "",
				formData: {
					name: '',
					birthDate: '',
					sex: "",
					highestEducation: '',
					occupation: '',
					birthPlace: '',
					mainStudyAddress: '',
					mainWorkAddress: '',
					migrationHistory: ''
				},
				errors: {}
			};
		},
		onLoad(options) {
			this.orderId = options.orderId;
		},
		methods: {

			validateForm() {
				this.errors = {};

				if (!this.formData.name) {
					this.errors.name = '姓名不能为空';
					return {
						isValid: false,
						missingField: '姓名'
					};
				}
				if (!this.formData.birthDate) {
					this.errors.birthDate = '出生年月不能为空';
					return {
						isValid: false,
						missingField: '出生年月'
					};
				}
				if (!this.formData.sex && this.formData.sex !== 0) {
					this.errors.sex = '性别不能为空';
					return {
						isValid: false,
						missingField: '性别'
					};
				}
				if (!this.formData.highestEducation) {
					this.errors.highestEducation = '最高学历不能为空';
					return {
						isValid: false,
						missingField: '最高学历'
					};
				}
				if (!this.formData.occupation) {
					this.errors.occupation = '职业不能为空';
					return {
						isValid: false,
						missingField: '职业'
					};
				}
				if (!this.formData.birthPlace) {
					this.errors.birthPlace = '出生地不能为空';
					return {
						isValid: false,
						missingField: '出生地'
					};
				}
				if (!this.formData.mainStudyAddress) {
					this.errors.mainStudyAddress = '主要学习地址不能为空';
					return {
						isValid: false,
						missingField: '主要学习地址'
					};
				}
				if (!this.formData.mainWorkAddress) {
					this.errors.mainWorkAddress = '主要工作地不能为空';
					return {
						isValid: false,
						missingField: '主要工作地'
					};
				}
				if (!this.formData.migrationHistory) {
					this.errors.migrationHistory = '迁徙经历不能为空';
					return {
						isValid: false,
						missingField: '迁徙经历'
					};
				}
				return {
					isValid: true,
					missingField: null
				};
			},
			submitForm() {
					const { isValid, missingField } = this.validateForm();
				
							if (isValid) {
								this.$request({
									url: "/functionMenu/surveyinfo/add",
									method: "post",
									data: {
										birthday: this.formData.birthDate,
										birthplace: this.formData.birthPlace,
										career: this.formData.occupation,
										higheducation: this.formData.highestEducation,
										moveexperience: this.formData.migrationHistory,
										name: this.formData.name,
										orderid: this.orderId,
										sex: this.formData.sex,
										studyaddress: this.formData.mainStudyAddress,
										userid: this.$store.state.userInfos.uid,
										workplace: this.formData.mainWorkAddress
									}
								}).then((res) => {
									if (res?.data?.code === 200) {
										uni.showToast({
											title: '提交成功',
											icon: 'success'
										});
										 setTimeout(() => {
										        uni.switchTab({
										          url: '/pages/my/index'
										        });
										      }, 1000); 
									} else {
										uni.showToast({
											title: '提交失败',
											icon: 'none'
										});
									}
								}).catch((err) => {
									uni.showToast({
										title: '请求失败',
										icon: 'none'
									});
								});
							} else {
								uni.showModal({
									title: '提示',
									content: `${missingField}未填写`,
									showCancel: false
								});
							}
			}
		}
	};
</script>

<style scoped lang="scss">
	.container {
		padding: 50rpx 24rpx 0;
		background-color: #ffebd5;
		position: relative;
		width: 100%;
		height: 100vh;
		box-sizing: border-box;

		.title {
			height: 108rpx;
			padding: 0 16rpx;
			font-size: 40rpx;
			border-top-right-radius: 16rpx;
			border-top-left-radius: 16rpx;
			background-color: rgba(250, 140, 22, 0.1);
		}

		.title-icon {
			width: 188rpx;
			height: 206rpx;
			position: absolute;
			right: 74rpx;
			top: -10rpx
		}

		.form-box {
			background-color: #fff;
			height: 78vh;
			overflow-y: scroll;
			padding: 16rpx;
			border-bottom-right-radius: 16rpx;
			border-bottom-left-radius: 16rpx;

			.form-item {
				// height: 138rpx;
				padding: 16rpx 0;

				border-bottom: 1px solid rgba(170, 170, 170, 0.5);

				text {
					height: 50rpx;
					color: #000000;
					font-size: 36rpx;

				}

				input {
					// height: 44rpx;
					width: 670rpx;
					color: #777777;
					margin-top: 12rpx;
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
		}

		button {
			width: 100%;
			font-size: 40rpx;
			margin-top: 32rpx;
			background-color: #FA8C16;
			color: white;
			border: none;
			height: 84rpx;
			border-radius: 16rpx;
		}
	}
</style>