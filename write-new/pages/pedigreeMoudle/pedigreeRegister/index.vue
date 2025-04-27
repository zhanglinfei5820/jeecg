<template>
	<view class="container">
		
		<view class="flex justify-center align-center " style="height: 108rpx; font-size:36rpx; color: #333;margin-top: 80rpx;">
			采丁录入
		</view>
	
		<view style="margin-top: 54rpx;">
			<!-- 基础用法，不包含校验规则 -->
			<uni-forms ref="baseForm" :modelValue="baseFormData">
				<view  class="box">
					<view class="title">
						<image class="require-icon"  src="../../../static/image/require.jpg" mode=""></image>
						<text>族谱名称：</text>
					</view>
					<uni-easyinput v-if="!pedigreeName" v-model="baseFormData.pedigreeName" placeholder="如金陵王氏(地域+姓氏)" />
					<text v-else>{{baseFormData.pedigreeName}}</text>
				
				</view>
				<view class="box">
					<view class="title">
						<image class="require-icon"  src="../../../static/image/require.jpg" mode=""></image>
						<text>姓氏：</text>
					</view>
					<uni-easyinput v-model="baseFormData.surname" placeholder="请输入姓氏" />
					
				</view>
				<view class="box">
					<view class="title">
						<image class="require-icon"  src="../../../static/image/require.jpg" mode=""></image>
						<text>户主姓名：</text>
					</view>
					<uni-easyinput v-model="baseFormData.name" placeholder="请输入户主名" />
					
				</view>
			
				<view class="box">
					<view class="title">
						<image class="require-icon"  src="../../../static/image/require.jpg" mode=""></image>
						<text>户主性别</text>
					</view>
					<uni-data-checkbox selectedColor="#FA8C16"  v-model="baseFormData.sex" :localdata="sexs" />
				</view>
				<view class="box">
					<view class="title">
						<image class="require-icon"  src="../../../static/image/require.jpg" mode=""></image>
						<text>手机号码：</text>
					</view>
					<uni-easyinput  type="number" v-model="baseFormData.phoneNum" placeholder="请输入手机号码" />
				
				</view>
				<view class="box">
					<view class="title">
						<!-- <image class="require-icon"  src="../../../static/image/require.jpg" mode=""></image> -->
						<text>推荐人手机号码：</text>
					</view>
					<uni-easyinput  v-if="!createphone" type="number" v-model="baseFormData.createphone" placeholder="请输入推荐人手机号码(选填)" />
					<text v-else>{{baseFormData.createphone}}</text>
				
				</view>

				<view class="box">
					<view class="title">
						<image class="require-icon"  src="../../../static/image/require.jpg" mode=""></image>
						<text>籍贯：</text>
					</view>
					<uni-easyinput v-model="baseFormData.city" placeholder="请输入籍贯" />
				</view>

			</uni-forms>
			<button class="submit" type="primary" @click="submit()">下一步，完善家庭成员信息</button>
		</view>
	</view>
</template>

<script>
	import {createPedigree,createHouse,addFamily} from "../api/index.js"
	export default {
		data() {
			return {
				title: '',
				pedigreeName:"",   //族谱名称
				createphone:"", 
				baseFormData: {
					 name: '',
					city: '', //籍贯
					phoneNum: "",
					sex: 0, //性别
					// generation: "", //世
					pedigreeName:"",   //族谱名称
					surname:"", //姓氏
					createphone:"", // 推荐人手机号
				},
				// 单选数据源
				sexs: [{
					text: '男',
					value: 0
				}, {
					text: '女',
					value: 1
				}],
				isRes:true
			}
		},
		onLoad(options) {
			
			
			// this.baseFormData.name = options.userName;
			// this.baseFormData.phoneNum = options.phoneNum;
			
			// let pedigree = 	JSON.parse(uni.getStorageSync("pedigree") || "{}");
			
			 this.pedigreeName = options?.name;
			 this.createphone = options.createphone;
			 
			 this.baseFormData.pedigreeName =options?.name;
			  this.baseFormData.createphone =options?.createphone;
			// this.genealogyid = pedigree?.id;
		},
		 onBackPress() {
		    // 阻止默认的返回行为
		    return true;
		  },
		methods: {

			async  submit() {
				if(this.baseFormData.pedigreeName == ""){
					uni.showToast({
						title: "请填写族谱名称"
					})
					return
				}
				if(this.baseFormData.surname == ""){
					uni.showToast({
						title: "请填写姓氏"
					})
					return
				}
				if(this.baseFormData.name == ""){
					uni.showToast({
						title: "请填写户主名称"
					})
					return
				}
			
				if(this.baseFormData.phoneNum == "" || this.baseFormData.phoneNum.length != 11 ){
					uni.showToast({
						title: "请填写正确手机号"
					})
					return
				}
				
				if(this.baseFormData.city == ""){
					uni.showToast({
						title: "请填写籍贯"
					})
					return
				}
				if(this.baseFormData.createphone ){
					if(this.baseFormData.createphone.length !== 11 ){
					uni.showToast({
						title: "请填写正确推荐人手机号"
					})
					return	
					}
				}
				if(this.isRes){
					this.isRes=false
				}else{
					return
				}
			//先创建族谱，再创建户主
			let parms ={
					  createby:this.$store.state.userInfos.uid,  //创建人ID
					  familyname: this.baseFormData.pedigreeName, //族谱名称
					  surname:this.baseFormData.surname,
					  createphone:this.baseFormData.createphone || this.baseFormData.phoneNum,
						//
				  }
			      let res1 = await createPedigree(parms)
					  
				  if(res1?.data?.msg){
					  //存储族谱信息
					  parms.id = res1?.data?.msg;
					  uni.setStorageSync('pedigree', JSON.stringify(parms));
				  //新增户主	
				  // let data1 = {
				  // 		"nickName": this.baseFormData.name,
				  // 		"rolename": "huzhu",
				  // 		"phonenumber": this.baseFormData.phoneNum,
				  // 		"roleIds":['5'],
				  // 		"sex":this.baseFormData.sex,
				  // 		"area":this.baseFormData.city,
				  // 		"genealogyid":parms.id,
				  // 	}
				  let data1 = {
				  		"name": this.baseFormData.name,
				  		"createid": this.$store.state.userInfos.uid,
				  		"phone": this.baseFormData.phoneNum,
				  		"sex":this.baseFormData.sex,
				  		"area":this.baseFormData.city,
				  		"genealogyid":parms.id,
				  	}
				  let res2 = await createHouse(data1);
				 // console.log(res2,11)
				  if(res2?.data?.code == 200){
					  data1.id = res2?.data?.msg;
				  	uni.setStorageSync("huzhuInfos", JSON.stringify(data1));
					let data3 = {
							name:this.baseFormData?.name,
							gender:this.baseFormData?.sex,
							householderid:res2?.data?.msg,
							householdrelationship:"me"
						}
						 //家庭成员添加户主
					let res3 = await addFamily(data3);
					if(res3?.data?.code ==200){
						this.isRes=true
					 uni.redirectTo({
					 	url: '/pages/pedigreeMoudle/addPedigree/index'
					 });
					}else{
						this.isRes=true
					}
				
				  
				  }else{
					  this.isRes=true
					  uni.showToast({
					  	title:"创建户主失败"
					  })
				  }
				  }else{
					  this.isRes=true
					  uni.showToast({
					  	title:"族谱名字重复"
					  })
				  }
			},
			
		}
	}
</script>

<style lang="scss" scoped>
	.container {
		width: 100%;
		// height: calc(100vh - 88upx);
		height: 100vh;
		display: flex;
		flex-direction: column;
        background: #EEECEB;
		 padding:0 16rpx;
		.box{
			padding:0 24rpx;
			display: flex;
			
			align-items: center;
			height: 74rpx;
			margin-bottom: 32rpx;
			.title{
				font-size: 36rpx;
				color: #333;
				display: flex;
				align-items: center;
				.require-icon {
					width: 18rpx;
					height: 18rpx;
					margin-right: 6rpx;
				}
			}
			.uni-easyinput{
				color: #333 !important;
				font-size: 36rpx !important;
				.uni-easyinput__placeholder-class{
					font-size: 36rpx;
				}
			}
		::v-deep .uni-easyinput__content-input{
				padding-left: 16rpx !important;
				font-size: 36rpx;
			}
			::v-deep .checklist-group{
				gap:92rpx;
				margin-left: 24rpx;
				font-size: 36rpx !important;
			}
		}
	}
 .submit{
	
	 height: 108rpx;
	 margin-top: 136rpx;
	 background-color: #FA8C16;
	 font-size: 48rpx;
	 color: #fff;
 }
	::v-deep .is-direction-left {

		align-items: center;
	}

	::v-deep .view__label {
		width: 160upx;
	}
</style>