<template>
	<view class="container">
		<view class="flex bg-color-white flex-direction margin-top-ssm gap padding-all-sm">
			<text class="size-20 weight-600">{{title}}</text>
			<text>{{phoneNumber}}(管理员)</text>
			<text>家族人数：{{num}}</text>
		</view>

		<view class="flex bg-color-white flex-direction margin-top-ssm gap padding-all-sm">
			<text class="size-20 weight-600">家族信息</text>
			<text>地区：{{address}}</text>
			<text>创建时间：{{creatTime}}</text>
			<text class="under-line"></text>
			<text class="size-20 weight-600">家族简介</text>
			<text>{{info}}</text>
		</view>

		<view class="foot margin-top-sm">
			<button style="width: 200upx;border-radius: 40upx;margin: 0; position: static;" @click="goAdd">加入</button>
			<button @click="team"  style="width: 200upx;border-radius: 40upx;margin: 0; position: static;">协作采丁</button>
			<button style="width: 200upx;border-radius: 40upx;margin: 0; position: static;"
				@click="creatPedigree">创建新族谱</button>
		</view>

		<uni-popup ref="popup" type="center">
			<view class="popup-content">
				<view class="popup-title">创建族谱</view>
				<view class="popup-input-group">
					<label>户主名称：</label>
					<input type="text" v-model="newPedigree.name" placeholder="请输入户主名称" />
				</view>
				<view class="popup-input-group">
					<label>户主手机号：</label>
					<input type="number" v-model="newPedigree.phoneNum" placeholder="请输入户主手机号" />
				</view>
				<view class="popup-input-group">
					<label>族谱名称：</label>
					<input type="text" v-model="newPedigree.treeName" placeholder="请输入族谱名称" />
				</view>
				<view class="popup-buttons">
					<button type="default" @click="handleCancel">取消</button>
					
					<button type="primary" @click="handleConfirm">确定</button>
				</view>
			</view>
		</uni-popup>
	</view>
</template>
<script>
	import request from '../../../request/index'
    import {getHouse} from "../api/index.js"
	export default {
		data() {
			return {
				phoneNumber: "",
				title: "",
				num: 0,
				address: "",
				creatTime: "",
				createphone:"",
				info: "",
				uid:"",
				nickName:"",
				newPedigree: {
					name: "",
					phoneNum: "",
					treeName: ""
				}
			}
		},
		onLoad(options) {
			
			 if(options?.scene){
				//接收小程序扫码
				 this.uid = options.scene.split("-")[0];
				 this.phoneNumber = options.scene.split("-")[1];
				
			 }
			 else{
				 //分享页面进来的
				 this.uid = options.uid;
				 this.phoneNumber = options.phoneNumber;
			 }
			this.getHouseInfo();	
		},
		//底部加载更多
		onReachBottom() {
		},
		methods: {
		
			//根据户主Id,获取家谱信息
			getPedigree(id){
				request({
					url: "/functionMenu/genealogy/" + id,
					method: "get"
				}).then((res)=>{
					//console.log(res)
					if(res?.data?.data){
						
					    let data1 =res.data.data;
					    this.title = data1?.familyname;
					    this.createphone = data1?.createphone;
							uni.setStorageSync('pedigree', JSON.stringify(res?.data.data));
					}
					
				}).catch((err)=>{
					console.log(err,111)
				})
			},
			isLogin(type){
				let userInfos = JSON.parse(uni.getStorageSync('userInfos') || "{}");
				if (!userInfos || userInfos?.phoneNumber?.length != 11){
					uni.showModal({
						content: '创建族谱需要手机号授权',
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
					//根据uid获取户主信息，跳转到编辑页面
					if(type == "add"){
						uni.navigateTo({
							url: `/pages/pedigreeMoudle/pedigreeRegister/index?name=${this.title}&createphone=${this.createphone}`
						});
					}else{
						uni.navigateTo({
							url: '/pages/pedigreeMoudle/pedigreeRegister/index'
						});
					}
		
				}
							
			},
			goAdd() {
			    this.isLogin("add")
					//根据uid获取户主信息，跳转到编辑页面
			},
			creatPedigree() {
				// this.$refs.popup.open()
				this.isLogin()
			},
			team(){
				uni.redirectTo({
					url: '/pages/pedigreeMoudle/addPedigree/index'
				});
			},
			handleCancel() {
				this.$refs.popup.close()
			},
			handleConfirm() {
				//创建族谱
				// newPedigree:{
				// 				  name:"",
				// 				  phoneNum:"",
				// 				  treeName:""
				// }
				if (this.newPedigree.name == "") {
					uni.showToast({
						title: "请填写姓名"
					})
					return
				}
				if (this.newPedigree.phoneNum == "" || this.newPedigree.phoneNum.length !== 11) {
					uni.showToast({
						title: "请输入正确格式手机号"
					})
					return
				}
				if (this.newPedigree.treeName == "") {
					uni.showToast({
						title: "请填写家族名称"
					})
					return
				}
				request({
					url: "/functionMenu/genealogy/add",
					method: "post",
					data: {
						familyname: this?.newPedigree?.treeName,
					}
				}).then((res) => {
					if (res?.data?.msg) {
						let id = res?.data?.msg;
						//根据族谱ID查看族谱信息，被保存，跳转到新增户主页面
						request({
							url: "/functionMenu/genealogy/list",
							method: "get",
							data: {
								id: id
							}
						}).then((res1) => {

							if (res1?.data?.rows.length > 0) {
								let data1 = res1?.data?.rows.filter(item => item.id == id);

								uni.setStorageSync('pedigree', JSON.stringify(data1[0]));
								uni.navigateTo({
									url: `/pages/pedigreeMoudle/pedigreeRegister/index?userName=${this.newPedigree.name}&phoneNum=${this.newPedigree.phoneNum}`
								});

							}
						})

					} else {
						uni.showToast({
							title: "创建失败"
						})
					}
				})
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
						this.num = man + woman;

					}
				})
			},
			//获取户主信息
			async getHouseInfo(){
							let res = await getHouse(this.uid);
							//console.log(res,111)
							if(res?.data?.rows.length > 0){
									uni.setStorageSync("huzhuInfos", JSON.stringify(res?.data?.rows[0]));
								 this.address = res?.data?.rows[0]?.area;
								 this.phoneNumber = res?.data?.rows[0]?.phone;
								 this.huzhu = res?.data?.rows[0]?.name;
								this.getHouse(res?.data?.rows[0].id);
								this.getPedigree(res?.data?.rows[0].genealogyid)
							}
						},
		}
	}
</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		padding: 20upx;
		width: 100%;
		height: calc(100vh - 88upx);
		background-color: #eee;
	}

	.foot {
		position: fixed;
		bottom: 0;
		left: 0;
		display: flex;
		align-items: center;
		width: 100%;
		height: 100upx;
		padding: 0 20upx;
		justify-content: space-between;
		background-color: #fff;
	}

	.popup-content {
		padding: 10upx;
		width: 600upx;
		background-color: #fff;
	}

	.popup-title {
		font-size: 18px;
		font-weight: bold;
		text-align: center;
		margin-bottom: 20px;
	}

	.popup-input-group {
		margin-bottom: 15px;
	}

	.popup-input-group label {
		display: inline-block;
		width: 200upx;
		vertical-align: top;
	}

	.popup-input-group input {
		// width: calc(100% - 90px);
		// border: 1px solid #ddd;
		// padding: 5px;
		// border-radius: 4px;
	}

	.popup-buttons {
		display: flex;
		justify-content: space-between;
		margin-top: 20px;
	}

	.popup-buttons button {
		width: 45%;
	}
</style>