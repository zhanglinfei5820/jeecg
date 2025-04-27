<template>
	<view class="container">

		<!-- <view class="my-pedigree flex  flex-direction">
			<uni-section   type="line" title="我的族谱" ></uni-section>
			<view  v-if="isPedigree" class="flex flex-direction">
				<view class="pedigree-info flex justify-between">
					<image class="box-icon" src="../../static/image/defaultUser.jpg" mode="aspectFit"></image>
					<view class="pedigree-info-des">
						<view class="title flex justify-between">
							<text>{{huzhu}}</text>
							<image @click="goShare" src="../../static/image/share.jpg" mode="aspectFit"></image>
						</view>
						<view class="gray size-12">
							<text>电话：</text>
							<text>{{phoneNumber}}</text>
						</view>
						<view class="gray size-12">
							<text>地址：</text>
							<text class="ellipsis">{{address}}</text>
						</view>
					</view>
				</view>
				<view class="people-num flex">
					<view class="num">
						<view class="flex flex-1 align-center flex-direction">
						    <text>{{allNum}}</text>
							<text class="size-12">总人数</text>
						</view>
						<view class="flex flex-1 align-center flex-direction">
						    <text>{{manNum}}</text>
							<text class="size-12">男性</text>
						</view>
						<view class="flex flex-1 align-center flex-direction">
						    <text>{{wumanNum}}</text>
							<text class="size-12">女性</text>
						</view>
						
					</view>
					<button class="btn yulan" @click="toPedigreeLogin">采丁预览</button>
					<button @click="goShare" class="btn caiding">协作采丁</button>
				</view>
			</view>
			<view v-else class="creat-pedigree">
				<view class="tip">
					暂无族谱，请先创建族谱
				</view>
				<button @click="tip">创建族谱</button>
			</view>
			
		</view> -->
   <view class="my-read">
   		<uni-section   type="line" title="我的阅读" ></uni-section>
		 <scroll-view  v-if="bookList.length>0"  class="scroll-view" scroll-x="true">
			 <view class="book-list">
			 	<view class="box" @click="toRead(item)"  v-for="(item,index) in bookList" :key="index">
			 		<image class="book-image" :src="item.novelImage" mode="aspectFill"></image>
			 		<view class="name">
			 			{{item.novelName}}
			 		</view>
			 		<text v-if="index == 0"  class="reading-tip" >正在阅读</text>
			 	</view>
			 </view>
		 </scroll-view>
		 <view v-else class="">
		 	<image class="book-image1" src="https://lidoogroup.com/nobook.png" mode=""></image>
		 </view>
		 
		
   </view>
    
        <view v-if="orderList.length >0"  >
		<view class="order-box" v-for="(item,index) in orderList" :key="index" >
			<uni-section style="margin-bottom: 16rpx;" v-if="index == 0"  type="line" title="我的订单" ></uni-section>
			  <view class="box" >
			  	<image @click="goDes(item)" :src="item.courseImage" class="course-image"></image>
				<view  class="title flex justify-between">
					<text class="title-left ellipsis">{{item.courseName}}</text>
					<view class="price flex" style="align-items: baseline;" >
						<text >实付款</text>
						<text  style="color: #333;" class="size-12">¥</text>
						<text style="color: #333;">{{item.totalfee}}</text>
					</view>
					
				</view>
				<view class="order-time">
					下单时间：{{item.paytime}}
				</view>
				<view class="status-box flex justify-between align-center">
					<view class="status yellow" :class="{'green':item.finstate == 4}">
					<!-- 	<text>已完成</text> -->
						<text>{{item.statusText}}</text>
					</view>
					<view class="refund flex align-center justify-center">
						<image class="tip-img" src="../../static/image/tip.jpg" mode=""></image>
						<view style="color: #777777;" @click="refundTip(item)" >申请退款</view>
					</view>
				</view>
				<view class="btn-all flex ">
					<button class="flex-1"   :class="{ 'active':item.finstate ==0  }" :disabled="item.finstate != 0 " @click="goForm(item.finstate,item.id,item)" >基础表单</button>
					<button class="flex-1"  :class="{ 'active': item.finstate ==2  }" :disabled="item.finstate != 2 " @click="goForm(item.finstate,item.id,item)">问卷详情</button>
					<button class="flex-1" :class="{ 'active':item.finstate == 4 }" :disabled="item.finstate != 4 "  open-type="contact"  >产品明细</button>
					<!-- <button class="flex-1" :class="{ 'active':item.finstate == 4 }" :disabled="item.finstate != 4 "   @click="downloadFile(item.finishtexturl)" >产品明细</button> -->
				</view>
			  </view>
		</view>
        
        </view>
		<view  v-if="orderList.length ==0" class="my-order">
			 <uni-section   type="line" title="我的订单" ></uni-section>	
			 <view class="tip" style="margin-top: 24rpx;">
			 	暂无订单
			 </view>
		</view>
	 

  
 <Cos-Tabbar :current="3" :tabBarList="tabBerLists" />
	</view>

</template>

<script>
	import request from '../../request/index'
	import CosTabbar from '../../component/CosTabbar.vue';
	import {
		firstLogin,
		getCode
	} from '../../api/login';
	import {getHouse,getPedigreeInfo} from "../pedigreeMoudle/api/index"
	export default {
		components:{
			CosTabbar
		},
		data() {
			return {
				tabBerLists:[],
				bookList:[],
				orderList:[],
				title: "",
				allNum: 0,
				manNum: 0,
				wumanNum: 0,
				address:"",
				huzhu:"",
				phoneNumber:"",
				worldnum:"",//世数
				isCreat:true, //是否有户主
				isPedigree:false //是否有族谱
			}
		},
		onLoad() {
			uni.hideTabBar()
			//本地获取用户手机号
			let userInfos = uni.getStorageSync('userInfos');
		
			if (!!userInfos) {
				let {
					phoneNumber,
					nickName
				} = JSON.parse(userInfos)
				
				// this.phoneNumber = phoneNumber;
				// this.huzhu = nickName;
			}else{
				this.handfirstLogin()
			}
            
		},
		onShow(){
			this.tabBerLists = uni.getStorageSync('tabBarList') // 自定义的tabbar赋值
			
			//  this.getHouseInfo()
			  this.getOrderList()
			  this.getReadBook()
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
			 goForm(status,orderId,item) {
				
			     if (status == 0) {
			     	uni.navigateTo({
			     		url: `/pages/baseForm/index?orderId=${orderId}`
			     	});
			     } else if (status == 2) {
			     	//以及模板ID
					//console.log(item,item.productinfos)
			     	uni.navigateTo({
			     		url: `/pages/advancedSurvey/index?orderId=${orderId}&id=${item.surveytemplate}&level=${item.level
}`
			     	});
			     }else if(status == 4){
					 this.$refs.popupQr.open()
				 }
			    },
			//获取族谱信息
		async	getPedigree(id){
			      let res = await getPedigreeInfo(id);
				  if(res?.data?.data){
				  	this.isPedigree =true;
				      let data1 =res.data.data;
				      this.title = data1?.familyname;
				   	uni.setStorageSync('pedigree', JSON.stringify(res?.data.data));
				  }else{
				  	this.isPedigree =false;
				  }
			},
			//获取户主信息
		   async getHouseInfo(){
				let res = await getHouse(this.$store.state.userInfos.uid);
				//console.log(res,111)
				if(res?.data?.rows.length > 0){
						uni.setStorageSync("huzhuInfos", JSON.stringify(res?.data?.rows[0]));
					 this.address = res?.data?.rows[0]?.area;
					 this.phoneNumber = res?.data?.rows[0]?.phone;
					 this.huzhu = res?.data?.rows[0]?.name;
					 this.getHouse(res?.data?.rows[0].id);
					 this.getPedigree(res?.data?.rows[0].genealogyid)
				}else{
					this.isPedigree =false;
				}
			},
		  //获取户主亲属信息
		    getHouse(id){
			 
				request({
					url: "/functionMenu/relatives/list",
					method: "get",
					data: {
						householderid: id
					}
				}).then((res)=>{
				//	console.log(res,111)
			if(res?.data?.rows){
	            // this.isCreat = false;
			    // 计算人数
				let data = res?.data?.rows;
				let woman =0;
				let man = 0;
				
				data.map(item =>{
					if(item.gender == "0"){
						man++;
						
						if(!!item.partnername){
							woman++
						}
					}else if(item.gender == "1"){
						woman++;
						if(!!item.partnername){
							man++
							
						}
					}
				})
			//	console.log(man,woman,11)
				this.allNum = man + woman;
				this.manNum = man;
				this.wumanNum = woman;
			   
				}else{
					 //this.isCreat = true;
				}
				})
			},
		    getHuzhu(id){
			 request({
			 	url: "/functionMenu/collectiontask/selecthuzhu",
			 	method: "get",
			 	data: {
			 		"nickName": this.huzhu,
			 		"rolename": "huzhu",
			 		"phonenumber": this.phoneNumber
			 	}
			 }).then((res) => {
			 	// console.log(res, 99)
			 	if (res?.data?.code == 200) {
			 		if (res?.data?.data) {
						this.isCreat = false;
			 			// uni.setStorageSync("huzhuInfos", JSON.stringify(res?.data?.data));
					  this.$store.dispatch("setHuzhu",res?.data?.data);
			 		  this.getHouse(id);
			 		} else {
			 		this.isCreat = true;
			 		}
			 	}
			 
			 })	
			},
			
			toPedigreeLogin() {
				
				// this.$refs.inputDialog.open()
				
				uni.navigateTo({
					url: '/pages/pedigreeMoudle/addPedigree/index'
				});
			},
			goShare(){
				uni.navigateTo({
					url: '/pages/pedigreeMoudle/share/index?name=' + this.title
				});
			},
			//获取订单列表
			getOrderList() {
				this.$request({
					url: "/functionMenu/payorder/list",
					method: "get",
					data: {
					    userid: this.$store.state.userInfos.uid,
						pageNum: 1,
						pageSize: 100
					}
				}).then((res) => {
					this.orderList =[]
					if (res?.data?.rows.length > 0) {
						res?.data?.rows.map((item) => {
							if(item.paystate == 1){
								//console.log(item, 111)
							  let state = item.finstate || "0";
							  let statusText =	this.getStatusText(state)
							  let obj = {
									id: item.id,
									courseName: item.productinfos.pname,
									courseImage: item.productinfos.pimage,
									status: item.paystate,  //0待支付 1支付成功 2支付失败
									finstate:item.finstate || "0", 
									price:item.productinfos.price,
									statusText,
									paytime:item.paytime,
									level:item.productinfos?.level || 1,
									surveytemplate:item.surveytemplate || 1,
									productId:item.productinfos.id,
									totalfee:item.totalfee,
									finishtexturl:item.finishtexturl
								}
								this.orderList.push(obj)
							}
							
						})
					}
				})
			},
			  getStatusText(finstate) {
				//  console.log(finstate,888)
			      switch (finstate) {
			        case "0":
			          return '基础表单填写中';
			        case "1":
			          return '基础表单待审核';
			        case "2":
			          return '高级问卷填写';
			        case "3":
			          return '回忆录续写中';
			        case "4":
			          return '产品已完成';
			        // case "5":
			        //   return '已完成';
			        // case "6":
			        //   return '订单已完成';
			        default:
			          return '未知状态';
			      }
			  },
			  refundTip(item) {
			    if (item.paytime) {
			      const payTime = new Date(item.paytime);
			      const currentTime = new Date();
			      const timeDifference = (currentTime - payTime) / (1000 * 60 * 60 * 24); // 计算时间差，单位为天
			    
			      if (timeDifference > 35) {
			        uni.showModal({
			          content: "请联系客服退款"
			        });
			      } else {
			        uni.showModal({
						  showCancel: false,
			          content: "下单35天后可申请退款"
			        });
			      }
			    }
			  },
			  tip(){
				  // uni.showModal({
					 //   showCancel: false,
				  // 	content:"暂未开通，敬请期待！"
				  // })
				  uni.navigateTo({
				  	url: '/pages/pedigreeMoudle/pedigreeRegister/index'
				  });
			  },
			  //获取我的小说
			  getReadBook(){
				  this.$request({
					  url:"/functionMenu/viewrecord/list",
					  method:"get",
					  data:{
						  userid:this.$store.state.userInfos.uid
					  }
				  }).then((res)=>{
					 
					  this.bookList = [];
					  if(res?.data?.rows.length > 0){
						let  bookList = JSON.parse(res?.data?.rows[0].endviewtype) || [];
						bookList.sort((a, b) => a.novelTime - b.novelTime);
						 this.bookList  = bookList;
					  }
					  // console.log(this.bookList,"read")
				  })
			  },
			  goDes(item){
			  //	console.log(item,11)
			  	uni.navigateTo({
			  		url: '/pages/courseDetail/index?id=' + item.productId + "&orderId=" + item.id + "&level=" + item.level
			  	})
			  },
			  toRead(item){
				  // console.log(item,444)
				  let novelId = JSON.stringify(item.novelId)
				  uni.navigateTo({
				  	url:`/pages/article/index?data=${novelId}`
				  })
			  },
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
						this.tabBerLists = uni.getStorageSync('tabBarList') // 自定义的tabbar赋值
						 //this.getPedigree()
						  this.getOrderList()
						  this.getReadBook()
				
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
				},
				 downloadFile(url) {
					 if(url == ""){
						 uni.showToast({
						     title: '请联系客服获取',
						 });
						 return
					 }
					        // 显示加载提示
					        uni.showLoading({
					            title: '下载中，请稍等',
								mask:true
					        });	
					        uni.downloadFile({
					            url: url,
					            success: (data) => {
					                if (data.statusCode === 200) {
					                    // 隐藏加载提示
					                    uni.hideLoading();
					                    // 下载成功后提示用户
					                    uni.showToast({
					                        title: '文件下载成功，是否打开？',
					                        icon: 'success',
					                        duration: 2000,
					                        success: () => {
					                            // 打开文件
					                            uni.openDocument({
					                                filePath: data.tempFilePath,
					                                success: (res) => {
					                                    console.log('打开文档成功');
					                                },
					                                fail: (err) => {
					                                    console.log('打开文档失败', err);
					                                }
					                            });
					                        }
					                    });
					                }
					            },
					            fail: (err) => {
					                // 隐藏加载提示
					                uni.hideLoading();
					                console.error('下载失败', err);
					                // 下载失败提示用户
					                uni.showToast({
					                    title: '文件下载失败，请l联系客服',
					                    icon: 'error'
					                });
					            }
					        });
					    
				        }
		}

	}
</script>

<style lang="scss" scoped>
	.container {
		width: 100%;
		 height: calc(100% - 188rpx);
		display: flex;
		 flex-direction: column;
        background-color: #EEECEB;
		padding: 0 24rpx;
		gap: 30rpx;
		padding-bottom: 188rpx;
		min-height: 100vh;
		.my-pedigree {
			padding: 24rpx 16rpx;
			background-color: #fff;
			border-radius: 24rpx;
            gap: 24rpx;
			.pedigree-info{
				height: 144rpx;
				.box-icon{
					height: 144rpx;
					width: 144rpx;
					margin-right: 16rpx;
				}
				.pedigree-info-des{
					flex:1;
					gap: 8rpx;
					display: flex;
                    flex-direction: column;
					.title{
						height: 48rpx;
						font-size: 36rpx;
						color: #333;
						image{
							width: 48rpx;
							height: 48rpx;
						}
					}
				}
			}
			.people-num{
				gap: 14rpx;
				height: 84rpx;
				margin-top: 24rpx;
				.num{
					width: 270rpx;
					display: flex;
					font-size: 36rpx;
					color: #777;
				}
				.btn{
					width: 184rpx;
					height: 82rpx;
					display: flex;
                    align-items: center;
					justify-content: center;
					padding: 0;
					background-color: #fff;
					color: #333;
					font-size: 36rpx;
					border: 1px solid #aaaaaa;
				}
				.caiding{
					border: 1px solid #fa8c16;
					color:#fa8c16
				}
				
			}
			.creat-pedigree{
				gap: 24rpx;
				background-color: #fff;
				
				button{
					height: 108rpx;
					background-color: #fa8c16;
					font-size: 48rpx;
					color: #fff;
					margin-top:24rpx;
				}
			}
		}
	
	    .my-read{
			padding: 24rpx 16rpx;
			background-color: #fff;
			border-radius: 24rpx;
			.scroll-view {
			  white-space: nowrap;
			  width: 100%;
			  overflow: hidden;
			  margin-top: 24rpx;
			  .book-list {
			    display: inline-flex;
				.box {
				  display: inline-block;
				  margin-right: 24rpx;
				  position: relative;
				  width: 180rpx;
				  .book-image {
				    width: 180rpx;
				    height: 232rpx;
				    border-radius: 16rpx;
				  }
				  
				  .name {
				    font-size: 32rpx;
				    color: #333;
				    margin-top: 14rpx;
					width: 180rpx;
					 word-wrap: break-word;
					  word-break: break-all; 
					  overflow: hidden; /* 防止文本溢出 */
					   white-space: normal; /* 允许文本换行 */
					
				  }
				  
				  .reading-tip {
				    position: absolute;
				    top: 182rpx;
				    right: 0;
					height: 50rpx;
				    background-color: rgba(0, 0, 0, 0.3);
				    color: #fff;
				    font-size: 24rpx;
				    padding: 4rpx 8rpx;
					line-height: 45rpx;
				    border-bottom-right-radius: 16rpx;
				    border-top-left-radius: 16rpx;
				  }
				}
			
			  }
			}
			.book-image1{
				width: 180rpx;
				height: 232rpx;
				border-radius: 16rpx;
			}
			
		}
		.my-order{
			padding: 24rpx 16rpx;
			background-color: #fff;
			border-radius: 24rpx;
			margin-bottom: 24rpx;
		}
		.order-box{
			padding: 16rpx 16rpx;
			background-color: #fff;
			border-radius: 24rpx;
			margin-bottom: 24rpx;
			.box{
				.course-image {
					width: 100%;
					height: 328rpx;
					border-radius: 16rpx;
					margin-bottom: 24rpx;
				}
				.title{
					.title-left{
						flex: 1;
						color: #333;
                        font-size: 36rpx;
					}
					.price{
						display: flex;
						justify-content: flex-end;
						width: 240rpx;
						font-size: 36rpx;
						color: #777;
						margin-bottom: 24rpx;
					}
				}
				.status-box{
					display: flex;
					height: 66rpx;
					.status{
						font-size: 36rpx;
						// color: #777;
						width: 486rpx;
						gap: 24rpx;
						background: #EEECEB;
						border-radius: 8rpx;
						height: 66rpx;
						display: flex;
						align-items: center;
						padding-left: 8rpx;
					}
					.refund{
						height: 66rpx;
						.tip-img{
							width: 40rpx;
							height: 40rpx;
							margin-right: 8rpx;
						}
					}
				}
				.btn-all{
					gap: 24rpx;
					height: 88rpx;
					margin-top: 24rpx;
					button{
						font-size: 40rpx;
						color: #aaaaaa;
						border: 1px solid #aaaaaa;
						background: #fff;
					}
					.active{
						background-color: #FA8C16;
						color: #fff;
						border:none;
					}
				}
			}
		}
		.order-time{
			color: #777;
			font-size: 32rpx;
			height: 44rpx;
			display: flex;
			align-items: center;
			margin-bottom: 24rpx;
		}
		.tip{
			color: #aaaaaa;
			font-size: 36rpx;
		}
		.yellow{
			color: #FAAD14;
		}
		.green{
			color: #52C41A;
		}
		
		
	}
	
	
</style>