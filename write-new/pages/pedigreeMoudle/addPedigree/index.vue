<template>
	<view class="container">
		
		<tree :treeDatas="treeDatas"></tree>
		<image class="bg" src="../static/treeBg.jpg" mode="aspectFill"></image>
		<view class="foot1">
			<image src="https://lidoogroup.com/name.png" mode="aspectFill"></image>
			<text>{{surname}}</text>
		</view>
		<view class="foot">
			<button @click="submit">提交审核</button>
			<button @click="isShow=true">继续编辑</button>
			<button @click="goYulan">查看详情</button>
		</view>
		<select-card :treeData="treeData" :id1="id" :isShow="isShow" @hide="hideChild"></select-card>
		<uni-popup ref="message" type="dialog">
			<uni-popup-dialog type="info" cancelText="取消" confirmText="确定" title="提示" content="请确认家庭成员是否全部添加完毕"
				@confirm="dialogConfirm"></uni-popup-dialog>
		</uni-popup>
	</view>

</template>

<script>
	import request from '../../../request/index'

	import tree from '../tree.vue';
	import selectCard from '../selectCard.vue'
	export default {
		components: {
			selectCard,
			tree
		},
		data() {
			return {
				title: "",
				surname:"",
				isShow: true,
				id: "",
				huzhu: {},
				treeData: [],
				treeDatas: {
					 "grandPa":[],
					 "huzhu":[],
					 "son":[],
					 "grandSon":[]
				}
			}
		},
		onLoad() {
			//获取族谱信息
			let pedigreeInfo = JSON.parse(uni.getStorageSync("pedigree") || "{}");
			uni.setNavigationBarTitle({
				title: pedigreeInfo?.familyname
			});
			this.surname = pedigreeInfo?.surname;

			let huzhuInfos = JSON.parse(uni.getStorageSync("huzhuInfos") || "{}");
			//console.log(huzhuInfos,111)
			this.id = huzhuInfos.id;

			this.huzhu = {
				name: huzhuInfos?.name || "",
				sex: huzhuInfos?.sex || 0, // 1儿子-2女儿；
				generation: 2,
				spouse: [],
				children: [],
				huzhu: true
			}
			//console.log(this.huzhu,11)
			// this.treeData = [this.huzhu];
			this.getFamilyList();

		},
		onShow() {

			this.isShow = true;
		},
		methods: {
			getFamilyList() {
				let huzhuInfo = uni.getStorageSync("huzhuInfos")
				let id = JSON.parse(huzhuInfo)?.id;
				request({
					url: "/functionMenu/relatives/list",
					method: "get",
					data: {
						householderid: id
					}
				}).then((res) => {
						
					if (res?.data?.rows.length >0) {

						uni.setStorageSync("treeDataList", JSON.stringify(res?.data?.rows))
						//添加家人
						let data1 = res?.data?.rows.filter(item => item.householdrelationship == 'parent');
						let data2 = res?.data?.rows.filter(item => item.householdrelationship == 'wife');
						let data3 = res?.data?.rows.filter(item => item.householdrelationship == 'son');
						let data4 = res?.data?.rows.filter(item => item.householdrelationship == 'daughter');
						let data5 = res?.data?.rows.filter(item => item.householdrelationship == 'grandson');
						let data6 = res?.data?.rows.filter(item => item.householdrelationship == 'granddaughter');
						let data7 = res?.data?.rows.filter(item => item.householdrelationship == 'me');
						let treeData1 = [];
						let grandSons = [];
						let grandPa = []; 
						if (data1.length > 0) {
							let spouse = [];
							if (data1[0]?.partnername) {
								spouse = [{
									name: data1[0]?.partnername
								}]
							}
							let parents = {
								name: data1[0].name || "",
								sex: data1[0].gender || 0, // 1儿子-2女儿；
								generation: 1,
								spouse: spouse,
								children: [this.huzhu],
								huzhu: false
							}
							grandPa = [parents];
							treeData1.push(parents)
						}
						if (data2.length > 0) {
							let wife = [];
							data2.map(item => {
								wife.push({
									name: item.name || ""
								})
							})
							this.huzhu.spouse = wife;
						}

						if (data3.length > 0) {
							let son = [];
							data3.map(item => {
								let spouse = [];
								let children = [];
								if (item.partnername) {
									spouse = [{
										name: item.partnername
									}]
								}
								data5.length > 0 && data5.map(item1 => {
									if (item1.pid == item.id) {
										let spouse = [];

										if (item1.partnername) {
											spouse = [{
												name: item1.partnername
											}]
										}
										let grandSon = {
											name: item1.name || "",
											sex: item1.gender || 0, // 1儿子-2女儿；
											generation: 4,
											spouse: spouse,
											children: [],
											huzhu: false
										}
										children.push(grandSon)
										grandSons.push(grandSon)

									}
								})
								data6.length > 0 && data6.map(item2 => {
									if (item2.pid == item.id) {
										let spouse = [];

										if (item2.partnername) {
											spouse = [{
												name: item2.partnername
											}]
										}
										let grandSon = {
											name: item2.name || "",
											sex: item2.gender || 0, // 1儿子-2女儿；
											generation: 4,
											spouse: [],
											children: [],
											huzhu: false
										}
										children.push(grandSon)
										grandSons.push(grandSon)
									}
								})
								this.huzhu.children.push({
									name: item.name || "",
									sex: item.gender || 0, // 1儿子-2女儿；
									generation: 3,
									spouse: spouse,
									children: children,
									huzhu: false
								})
							})

						}
						if (data4.length > 0) {
							let daughter = [];
							data4.map(item => {
								let spouse = [];
								if (item.partnername) {
									spouse = [{
										name: item.partnername
									}]
								}
								this.huzhu.children.push({
									name: item.name || "",
									sex: item.gender || 0, // 1儿子-2女儿；
									generation: 3,
									spouse: spouse,
									children: [],
									huzhu: false
								})
							})

						}
						if (data7.length > 0) {
							this.huzhu.isAdd = true;
						}
						console.log(this.huzhu,111)
						// this.treeDataS.huzhu = [this.huzhu];
						// this.treeDataS.son = this.huzhu?.children || [];
						// this.treeDataS.grandSon = grandSons;
						let treeData2 = {
							grandPa:grandPa,
							huzhu:[this.huzhu],
							son:this.huzhu?.children || [],
							grandSon:grandSons
						}
						//console.log(treeData2,222)
						this.$set(this, "treeDatas", treeData2)
						
						if (treeData1.length > 0) {
							this.$set(this, "treeData", treeData1)
						} else {
							treeData1.push(this.huzhu)
							this.$set(this, "treeData", treeData1)
						}
						//存储户主家族信息

						uni.setStorageSync("treeData", JSON.stringify(treeData1))

					} else {
						this.treeData = [this.huzhu];
						uni.setStorageSync("treeData", JSON.stringify(this.treeData))
					}
				})
			},
			hideChild() {
				this.isShow = false;
			},
			goYulan() {
				uni.navigateTo({
					url: '/pages/pedigreeMoudle/editPedigree/index'
				});
			},
			submit() {
				this.$refs.message.open()
			},
			dialogConfirm() {
				uni.redirectTo({
					url: '/pages/pedigreeMoudle/goPedigree/index'
				});

			}
		}

	}
</script>

<style lang="scss" scoped>
	.container {
		width: 100%;
		height: 100vh;
		display: flex;
		flex-direction: column;

		.bg {
			width: 100%;
			height: 100vh;
			background-size: 750rpx 100%;
		}
	}

	.foot1 {
		position: fixed;
		bottom: 128rpx;
		left: 0;
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
		height: 160rpx;

		image {
			width: 160rpx;
			height: 160rpx;
		}

		text {
			position: absolute;
			left: calc(50% - 80rpx);
			top: 0;
			width: 160rpx;
			height: 160rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			color: #FEEBD3;
			font-size: 77rpx;
		}
	}

	.foot {
		position: fixed;
		bottom: 20rpx;
		left: 0;
		display: flex;
		align-items: center;
		width: 100%;
		height: 84rpx;
		padding: 0 24rpx;
		justify-content: space-between;
        z-index: 99;
		button {
			width: 224rpx;
			height: 84rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			color: #fff;
			font-size: 32rpx;
			background-color: #FA8C16;
			margin: 0;
		}
	}

	uni-button:after {
		width: 0;
	}
</style>