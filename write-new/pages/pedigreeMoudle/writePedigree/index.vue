<template>
	<view class="container">

		<view class="margin-top-mid">
			<!-- 基础用法，不包含校验规则 -->
			<!-- <uni-forms ref="baseForm" :modelValue="baseFormData">
				
				<uni-forms-item label-width="90" label="姓名">
					<uni-easyinput v-if="type!='me'"   v-model="baseFormData.name" placeholder="请输入姓名" />
					<text v-else>{{baseFormData.name}}</text>
				</uni-forms-item>

				<uni-forms-item label-width="90" label="性别">
					<uni-data-checkbox v-model="baseFormData.sex" :localdata="sexs"   />
				</uni-forms-item>
				<uni-forms-item v-show="showParent" label-width="90" label="选择父亲">
					<uni-data-select type="line" v-model="baseFormData.parentsId" :localdata="parentList" 
						:clear="false"></uni-data-select>
				</uni-forms-item>

				<uni-forms-item label-width="90" label="排行">
					<uni-data-select type="line" v-model="baseFormData.rank" :localdata="rangeMan" @change="rankChange"
						:clear="false"></uni-data-select>
				</uni-forms-item>
				<uni-forms-item label-width="90" label="婚姻">
					<uni-data-select type="line" v-model="baseFormData.marriage" :localdata="marriage"
						@change="marriageChange" :clear="false"></uni-data-select>
				</uni-forms-item>
				
				<uni-forms-item v-show="isFere" label-width="90" label="伴侣姓名">
					<uni-easyinput v-model="baseFormData.fereInfo.name" placeholder="请输入伴侣的姓名" />
				</uni-forms-item>
				<uni-forms-item v-show="isFere" label="伴侣出生时间">
					<uni-datetime-picker type="date" return-type="timestamp" v-model="baseFormData.fereInfo.startTime" />
				</uni-forms-item>
				
				<uni-forms-item label-width="90" label="常用名">
					<uni-easyinput v-model="baseFormData.chilName" placeholder="(选填)学名、小名、乳名等" />
				</uni-forms-item>
				<uni-forms-item label="出生时间">
					<uni-datetime-picker type="date" return-type="string" v-model="baseFormData.startTime" />
				</uni-forms-item>
				<uni-forms-item label-width="90" label="学历">
					<uni-easyinput v-model="baseFormData.education" placeholder="(选填)" />
				</uni-forms-item>

				<uni-forms-item label-width="90" label="是否健在">
					<uni-data-checkbox v-model="baseFormData.alive" :localdata="alive" />
				</uni-forms-item>
				<uni-forms-item label-width="90" label="简介">
					<uni-easyinput type="textarea"  v-model="baseFormData.introduction" placeholder="(选填)比如品学优良,乐于助人,工作认真,刻苦钻研,曾获得市级XX比赛一等奖等荣誉.热爱家乡,为家乡建设无私奉献,备受邻里亲友尊敬" />
				</uni-forms-item>
			</uni-forms> -->
			<uni-forms ref="baseForm" :modelValue="baseFormData">
			    <view class="box">
			        <view class="title">
			            <image class="require-icon" src="../../../static/image/require.jpg" mode=""></image>
			            <text>姓名：</text>
			        </view>
			        <uni-easyinput v-if="type != 'me'" v-model="baseFormData.name" placeholder="请输入姓名" />
			        <text v-else>{{ baseFormData.name }}</text>
			    </view>
			<view class="box">
			    <view class="title">
			        <image class="require-icon" src="../../../static/image/require.jpg" mode=""></image>
			        <text>曾用名：</text>
			    </view>
			    <uni-easyinput v-model="baseFormData.chilName" placeholder="(选填)学名、小名、乳名等" />
			</view>
			    <view class="box">
			        <view class="title">
			            <image class="require-icon" src="../../../static/image/require.jpg" mode=""></image>
			            <text>性别</text>
			        </view>
			        <uni-data-checkbox selectedColor="#FA8C16" v-model="baseFormData.sex" :localdata="sexs" />
			    </view>
			
			    <view class="box" v-if="showParent">
			        <view class="title">
			            <image class="require-icon" src="../../../static/image/require.jpg" mode=""></image>
			            <text>父亲：</text>
			        </view>
			        <uni-data-select type="line" v-model="baseFormData.parentsId" :localdata="parentList" :clear="false"></uni-data-select>
			    </view>
			
			    <view class="box">
			        <view class="title">
			            <image class="require-icon" src="../../../static/image/require.jpg" mode=""></image>
			            <text>排行：</text>
			        </view>
					 <uni-easyinput v-model="baseFormData.rank" placeholder="请输入排行" />
			    </view>
			
			    <view class="box">
			        <view class="title">
			            <image class="require-icon" src="../../../static/image/require.jpg" mode=""></image>
			            <text>婚姻状况</text>
			        </view>
					  <uni-data-checkbox v-model="baseFormData.marriage" selectedColor="#FA8C16" :localdata="marriage"  @change="marriageChange" />
			    </view>
			    <view class="box">
			        <view class="title">
			            <image class="require-icon" src="../../../static/image/require.jpg" mode=""></image>
			            <text>出生日期：</text>
			        </view>
			      
					   <uni-easyinput v-model="baseFormData.startTime"  placeholder="如1985.9.1"  />
			    </view>
			<view class="box" v-if="isFere">
			    <view class="title">
			        <image class="require-icon" src="../../../static/image/require.jpg" mode=""></image>
			        <text>伴侣姓名：</text>
			    </view>
			    <uni-easyinput v-model="baseFormData.fereInfo.name" placeholder="请输入伴侣的姓名" />
			</view>
			<!-- 			
			<view class="box" v-if="isFere">
			    <view class="title">
			        <image class="require-icon" src="../../../static/image/require.jpg" mode=""></image>
			        <text>伴侣出生日期：</text>
			    </view>
			    <uni-easyinput  v-model="baseFormData.fereInfo.startTime"  placeholder="如1985.9.1"/>
			</view> -->
			    <view class="box">
			        <view class="title">
			         
			            <text>学历：</text>
			        </view>
			        <uni-easyinput v-model="baseFormData.education" placeholder="如本科" />
			    </view>
			
			   
			    <view class="box1">
			        <view class="title">
			            
			            <text>简述：</text>
			        </view>
			        <uni-easyinput type="textarea" v-model="baseFormData.introduction" placeholder="(选填)比如品学优良,乐于助人,工作认真,刻苦钻研,曾获得市级XX比赛一等奖等荣誉.热爱家乡,为家乡建设无私奉献,备受邻里亲友尊敬"  maxlength="1000"  style="height: 434rpx;"  />
					<view class="tip">
						{{baseFormData.introduction.length}}/1000
					</view>
			    </view>
			</uni-forms>

<view class="flex justify-between align-center foot">
	<view class="del" @click="del">
		<image src="../static/del.jpg" mode="aspectFill"></image>
		<text>删除</text>
	</view>
	<button  type="primary" @click="submit('baseForm')">保存信息</button>
</view>
		</view>
	</view>
</template>

<script>
	import request from '../../../request/index'
	import {delFamily} from "../api/index.js"
	export default {
		data() {
			return {
				type:"", // 当前身份信息
				id:"",//户主ID
				isEdit:false,//是否是编辑
                isFere:false, //如果已婚显示自己的伴侣
				showParent:false,//如果是孙子孙女需要显示父亲的列表
				userId:"", // 用户id
				isSubmit:true, // 是否允许提交
				baseFormData: {
					fereInfo:{
						name:"",
						startTime: "", // 出生日期
					},
					name:"",
					chilName:"", //小名
					sex: 0, //性别
					rank: "",  //排行
					marriage: "", //婚姻
					startTime: "", // 出生日期
					education: "", //学历
					alive: 0,   //健在
					introduction:"", //简介
					parents:'',
					parentsId:"",
				},
				// 单选数据源
				sexs: [{
					text: '男',
					value: 0
				}, {
					text: '女',
					value: 1
				}],
				rangeMan:[],
				marriage: [{
						value: 0,
						text: "已婚"
					},
					{
						value: 1,
						text: "未婚"
					}
				],
				parentList: []
			}
		},
		onLoad(options) {
          console.log(options);
		  
		  //获取户主家族信息，遍历所有儿子的ID
		   let treeDataList = JSON.parse( uni.getStorageSync("treeDataList") || "{}");
		   let parets = treeDataList.filter(item=>item.householdrelationship == 'son' && item.marriage ==  0)
		  parets.length > 0 && parets.map(item=>{
			   this.parentList.push({
				   value: item.id,
				   text: item.name,
				   id:item.id
			   })
		   })
		  
		  this.type = options.type;
		  this.id = options.id;
		  this.baseFormData.sex = Number(options.sex);
		  this.baseFormData.name = options.name || "";
		  this.rangeMan = this.baseFormData.sex == 1 ? this.rangeWuman :  this.rangeMan1;
		   this.isEdit = options.isEdit || false;
		   this.userId = options.userId || "";
		  if(options.type == "grandson" || options.type == "granddaughter" ){
			  this.showParent = true;
		  }
		  if(options.type == "me" || options.type == "wife" ){
		  			  this.isFere = false
		  }
		 
		  if(options.isEdit){
			  //编辑，根据id获取用户信息
			  request({
				  url:"/functionMenu/relatives/" + options.userId,
				  method:"get"
			  }).then((res)=>{
				
				  if(res?.data?.data){
					  let data = res?.data?.data;
					this.baseFormData = {
					fereInfo:{
						name:data.partnername,
						startTime: "", // 出生日期
					},
					name:data.name,
					chilName:data.commonname, //小名
					sex: Number(options.sex), //性别
					rank: data.ranking,  //排行
					marriage: Number(data.marriage), //婚姻
					// startTime: this.timestampToDateString(Number(data.birtherday)), // 出生日期
					startTime: data.birtherday, // 出生日期
					education: data.education, //学历
					alive: Number(data.isalive),   //健在
					introduction:data.introduction || "", //简介
					parentsId:data.pid || "",
				}
				  }
			  })
			  
			  
		  }
		  
		},
		methods: {
            sexChange(val){
				console.log(val,11)
            	this.rangeMan = val.detail.value == 1 ? this.rangeWuman :  this.rangeMan1;
            },
			//排行选择
			rankChange() {
                 
			},
			marriageChange(e){
		   //  console.log(e,111)
				if(e.detail.value == 0 && (this.type !=='me' && this.type != "wife")){
					this.isFere = true
				}else{
					this.isFere = false
				}
			},
		 async	del(){
			 console.log(this.userId,this.isEdit)
		   //如果是编辑，删除这一条，如果是新增则重置
		   if(this.isEdit){
			  //  console.log(res,111)
				if(this.type == "me" || this.type == "son"){
					uni.showModal({
						content:"爸爸和儿子不允许删除",
						
					})
				     return false;
				}
			   
			   let res = await delFamily(this.userId)
			 
			   if(res?.data?.code == 200){
				   uni.showToast({
				   	title:"删除成功"
				   })
			   }
			   setTimeout(()=>{
				   uni.redirectTo({
				   	url: '/pages/pedigreeMoudle/addPedigree/index'
				   });
			   },100)
		   }else{
			   this.baseFormData= {
			   	fereInfo:{
			   		name:"",
			   		startTime: "", // 出生日期
			   	},
			   	name:"",
			   	chilName:"", //小名
			   	sex: 0, //性别
			   	rank: "",  //排行
			   	marriage: "", //婚姻
			   	startTime: "", // 出生日期
			   	education: "", //学历
			   	alive: 0,   //健在
			   	introduction:"", //简介
			   	parents:'',
			   	parentsId:"",
			   }
			   uni.redirectTo({
			   	url: '/pages/pedigreeMoudle/addPedigree/index'
			   });
		   }
			},
			submit(ref) {
				
				if(this.baseFormData.name == ""){
					uni.showToast({
						title: "请填写姓名"
					})
					return
				}
				if(!this.baseFormData.rank  && this.baseFormData.rank !="0" ){
					uni.showToast({
						title: "请选择排行"
					})
					return
				}
				if(!this.baseFormData.marriage && this.baseFormData.marriage != "0" ){
					uni.showToast({
						title: "请选择婚姻"
					})
					return
				}
				if(this.baseFormData.marriage == 0 && !this.baseFormData.fereInfo.name && this.type !="me" && this.type !="wife"){
					uni.showToast({
						title: "请填写伴侣姓名"
					})
					return
				}
				if(this.baseFormData.startTime == ""){
					uni.showToast({
						title: "请选择出生日期"
					})
					return
				}
				
				if((this.type == "grandson" || this.type == "granddaughter" ) && !this.baseFormData.parentsId){
					uni.showToast({
						title: "请选择父亲"
					})
					return
				}
				if(!this.isSubmit){
					return false
				}else{
					this.isSubmit = false
				}
				
				
				let data1 = {
						birtherday:this.baseFormData?.startTime,
						name:this.baseFormData?.name,
						commonname:this.baseFormData?.chilName,
						education:this.baseFormData?.education,
						gender:this.baseFormData?.sex,
						householderid:this.id,
						householdrelationship:this.type,
						introduction:this.baseFormData?.introduction,
						isalive:this.baseFormData?.alive,
						marriage:this.baseFormData?.marriage,
						ranking:this.baseFormData?.rank,
						partnername:this.baseFormData.fereInfo.name,
					    pid:this.baseFormData?.parentsId || "",
					}
				if(this.isEdit){
					//修改一条
					let data2 ={...data1,id:this.userId};
									request({
										url:"/functionMenu/relatives/edit",
										method:"post",
										data:data2
									}).then((res)=>{
									//	console.log(res,999)
										if(res?.data?.code ==200){
										  uni.redirectTo({
										  	url: '/pages/pedigreeMoudle/addPedigree/index'
										  });	
										}
										
									}).finally(()=>{
										this.isSubmit = true
									})
				}else{
					//新增一条
									request({
										url:"/functionMenu/relatives/add",
										method:"post",
										data:data1
									}).then((res)=>{
									//	console.log(res,999)
										if(res?.data?.code ==200){
										  uni.redirectTo({
										  	url: '/pages/pedigreeMoudle/addPedigree/index'
										  });	
										}
						}).finally(()=>{
										this.isSubmit = true
									})
				}
			},
			//时间戳转换成时间格式
		 timestampToDateString(timestamp) {
			    // 创建一个 Date 对象
			    const date = new Date(timestamp);
			
			    const year = date.getFullYear();
			    const month = String(date.getMonth() + 1).padStart(2, '0'); 
			    const day = String(date.getDate()).padStart(2, '0'); 
			    return `${year}-${month}-${day}`;
			}
		}
	}
</script>

<style lang="scss" scoped>
	.container {
		width: 100%;
		// height: calc(100vh - 88upx);
		height: 100%;
		display: flex;
		flex-direction: column;
		
        background: #EEECEB;
         padding:0 16rpx;
		 overflow: scroll;
		 padding-bottom: 180rpx;
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
			
		}
		.box1{
			padding:0 24rpx;
			display: flex;
			flex-direction: column;
			position: relative;
			.title{
				font-size: 36rpx;
				color: #333;
				display: flex;
				align-items: center;
				margin-bottom: 8rpx;
			}
			::v-deep .uni-easyinput__content-textarea{
				min-height: 434rpx;
			}
			.tip{
				height: 50rpx;
				position: absolute;
				right: 40rpx;
				bottom: 16rpx;
				color: #aaa;
				font-size: 36rpx;
			}
		}
		.foot{
			position: fixed;
			bottom: 0;
			left: 0;
			width: 100%;
			height: 148rpx;
			background-color: #fff;
			padding: 16rpx;
			display: flex;
			z-index: 99;
			.del{
				width: 68rpx;
				color: #333;
				font-size: 34rpx;
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: center;
				margin-right: 24rpx;
				image{
					width: 60rpx;
					height: 60rpx;
					margin-bottom: 8rpx;
				}
			}
			button{
				height: 108rpx;
				background-color: #FA8C16;
				border-radius: 16rpx;
				font-size: 48rpx;
				width: 626rpx;
			}
		}
		::v-deep .uni-easyinput{
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
				gap:72rpx;
				margin-left: 24rpx;
				font-size: 36rpx !important;
			}
			::v-deep .uni-select {
				width: 538rpx;
				background-color: #fff;
			}
	}
</style>