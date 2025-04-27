<template>
	<view class="container bg-color-eeeceb">
		<div class="head">
			<view class="search flex size-18">
				<uni-data-select class="select-process" type="line" v-model="value" :localdata="range" @change="change"
					:clear="false"></uni-data-select>
				<view class="search-content">
					<uni-icons color="#aaa" size="20" type="search"></uni-icons>
					<input class="search-input" type="text" :value="inputClearValue" placeholder="请输入任务名称"
						@input="clearInput" />
					<icon v-if="showClearIcon" @click="clearIcon" type="clear" size="20" style="margin-right: 10upx;" />
				</view>
				<button class="search-btn" @click="search">搜索</button>
			</view>
			
		</div>
		<div class="content">
			<div v-for="(item, index) in dataList" :key="index" class="box">
				<div class="box-status">
					<image v-if="item.workstatus=='已完成'" src="../../static/image/done.png" mode=""></image>
					<image v-if="item.workstatus=='进行中'" src="../../static/image/do.png" mode=""></image>
				</div>
				<div class="row">
					<view class="box-left flex align-center ">
						<image class="box-icon" src="../../static/image/task.png" mode="aspectFit"></image>
						<view class="name">
							任务名称
						</view>
					</view>
					
					<view class="box-content">
						
						<view class="des">
							{{item.workname}}
						</view>
					</view>
				</div>
				<div class="row">
					
					<view class="box-left flex align-center ">
						<image class="box-icon" src="../../static/image/code.jpg" mode="aspectFit"></image>
						<view class="name">
							合同编号
						</view>
					</view>
					
					<view class="box-content">
						
						<view class="des">
							{{item.contractcode}}
						</view>
					</view>
				</div>
				<div class="row">
					<view class="box-left flex align-center ">						<image class="box-icon" src="../../static/image/user.png" mode="aspectFit"></image>
						<view class="name">
							写手名称
						</view>					</view>
					
					<view class="box-content">
						
						<view class="des">
							{{item.writer}}
						</view>
					</view>
				</div>
				<div class="row">
					<view class="box-left flex align-center ">						<image class="box-icon" src="../../static/image/state.png" mode="aspectFit"></image>
						<view class="name">
							验收状态
						</view>					</view>
					
					<view class="box-content">
						
						<view :class="['des', item.workstatus === '已完成' ? 'completed' : 'in-progress']">
							{{item.acceptstatus}}
						</view>
					</view>
				</div>
				<div class="row">
					<view class="box-left flex align-center ">						<image class="box-icon" src="../../static/image/progress.png" mode="aspectFit"></image>
						<view class="name">
							节点状态
						</view>					</view>
					
					<view class="box-content">
					
						<view :class="['des', item.workstatus === '已完成' ? 'completed' : 'in-progress']">
							{{item.planstatus}}
						</view>
					</view>
				</div>
				<div class="row" style="border:none">
					<view class="box-left flex align-center ">						<image class="box-icon" src="../../static/image/time.png" mode="aspectFit"></image>
						<view class="name">
							结束时间
						</view>					</view>
					
					<view class="box-content">
					
						<view class="des">
							{{item.endtime}}
						</view>
					</view>
				</div>
			</div>
			<uni-load-more :status="status"></uni-load-more>
		</div>
   <Cos-Tabbar :current="1" :tabBarList="tabBerLists" />
	</view>
</template>
<script>
	import request from '../../request/index'
    import CosTabbar from '../../component/CosTabbar.vue';
	export default {
		components:{
			CosTabbar
		},
		data() {
			return {
				tabBerLists:[],
				value: "",// 任务状态
				inputClearValue: "", // 搜索内容
				showClearIcon: false, //清除图标
				page: 1,
				pageSize: 10,
				status: "more", // 当前数据状态
				range: [{
						value: "",
						text: "全部"
					},
					{
						value: "进行中",
						text: "进行中"
					},
					{
						value: "已完成",
						text: "已完成"
					},
				],
				dataList: [
					// {
					// 	workstatus: "1",
					// 	workname: "谢老师回忆录",
					// 	contractcode: "SSASJ-1212K",
					// 	writer: "王庄",
					// 	phone: "1212121212",
					// 	acceptstatus: "第一笔款已打",
					// 	approvalstatus: "第四次文稿已经验收",
					// 	endtime: "2024.9.30"
					// },
				]
			}
		},
		onLoad() {
			// 隐藏原生的tabbar,有自定义tabbar的页面都要写一遍
			uni.hideTabBar()
			this.getlist({
				page: 1,
				pageSize: 10,
				workstatus: this.value == 0 ? "" : this.value,
				workname: this.inputClearValue,
			})
           
		},
		onShow() {
			this.tabBerLists = uni.getStorageSync('tabBarList') // 自定义的tabbar赋值
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
		//底部加载更多
		onReachBottom() {

			if (this.status == 'more') {
				this.getlist({
					page: this.page,
					pageSize: 10,
					workstatus: this.value == 0 ? "" : this.value,
					workname: this.inputClearValue,
				})
			}

		},
		methods: {
			change(e) {
				console.log("e:", e);
				this.value = e;
				this.page = 1;
				this.dataList = []
				this.getlist({
					page: 1,
					pageSize: 10,
					workstatus: this.value == 0 ? "" : this.value,
					workname: this.inputClearValue,
				})
			},
			//获取列表

			getlist(parm) {
				let userInfos = uni.getStorageSync('userInfos')
				let uid = "";
				if (!!userInfos) {
					uid = JSON.parse(userInfos).uid
				} else {
					uni.showToast({
						title: "没有找到用户id"
					})
					return
				}
				let parm1 = {
					...parm,
					customid: uid
				}
				//console.log(parm1, "188")
				request({
					url: "/work/list",
					method: "get",
					data: parm1
				}).then((res) => {
					//console.log(res, "88")
					this.status = res.data.total > this.page * this.pageSize ? "more" : "noMore"
					if (res.data.code == "200") {
						this.dataList.push(...res.data.rows);
						this.page++
					}
				}).catch((err) => {
					//console.log(err)
					return this.dataList

				})
			},
			clearInput: function(event) {
				this.inputClearValue = event.detail.value;
				if (event.detail.value.length > 0) {
					this.showClearIcon = true;
				} else {
					this.showClearIcon = false;
				}
			},
			search() {
				//console.log(11)
				this.dataList = []
				this.page = 1;
				this.getlist({
					page: this.page,
					pageSize: 10,
					workstatus: this.value == 0 ? "" : this.value,
					workname: this.inputClearValue,
				})
			},
			clearIcon: function() {
				this.inputClearValue = '';
				this.showClearIcon = false;
				this.dataList = []
				this.page = 1;
				this.getlist({
					page: this.page,
					pageSize: 10,
					workstatus: this.value == 0 ? "" : this.value,
					workname: "",
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		// align-items: center;
		// justify-content: center;
		padding:0 24rpx;
		width: 100%;
		height: calc(100vh - 120rpx);
		gap:24rpx;
		.head {
			width: calc(100% - 48rpx);
			display: flex;
			position: fixed;
			top: 0px;
			left: 0;
			height: 64rpx;
			z-index: 99;
			margin: 0 24rpx;
			box-sizing: border-box;

			.search {
				 width: 100%;
				background: #fff;
				// border: 1px solid #e5e5e5;
				border-radius: 8rpx;
				display: flex;
				align-items: center;

				.select-process {
					width: 166rpx;
					flex: none;
                   
					
					::v-deep .uni-select {
						border: none;
						border-radius: 0;
						height: 64rpx;
						margin-left: 16rpx;
						font-size: 36rpx;
						padding: 0 4rpx 0 0;
						 color:#333;
						border-right: 1px solid #e5e5e5;
					}
				}

				.search-content {
					width: 426rpx;
					display: flex;
					flex-direction: row;
					align-items: center;
                    margin-left: 24rpx;
					
					.uniui-search {
						padding: 0 16rpx;
						width: 64rpx;
					}

					.search-input {
						font-size: 36rpx;
						flex: 1;
					}
				}
			}

			.search-btn {
				background-color: #FA8C16;
				width: 108rpx;
				height: 56rpx;
				border-radius: 6rpx;
				padding: 0;
				display: flex;
				align-items: center;
				justify-content: center;
				flex: 1;
				margin-right: 4rpx;
				color: #fff;
			}
		}

		.content {
			width: 100%;
			height: calc(100vh - 86rpx);
			overflow-y: auto;
			margin-top: 86upx;
			font-size: 26upx;

			.box {
				width: 100%;
				background: #fff;
				padding: 0 24rpx;
				box-sizing: border-box;
				position: relative;
				margin-bottom: 24rpx;
				font-size: 34rpx;
                font-family: PingFang SC;
				.box-status {
					position: absolute;
					top: 16rpx;
					right: 16rpx;

					image {
						width: 156rpx;
						height: 156rpx;
					}
				}
                .box-left{
					width: 216rpx;
					color: #777777;
					border-right: 1px solid #e5e5e5;
				}
				.row {
					display: flex;
					flex-direction: row;
					align-items: center;
					// height: 80upx;
					padding: 24rpx 0;
					border-bottom: 1px solid #e5e5e5;
					
					.box-icon {
						display: flex;
						width: 48rpx;
						height: 48rpx;
						margin-right: 8upx;
					}

					.box-content {
						display: flex;
						flex-direction: row;
						align-items: center;

					
						.des {
							color: #333333;
							margin-left: 18rpx;
						}
					    .completed{
							color: #52C41A;
						}
						.in-progress{
							color: #FAAD14;
						}
					}
				}
			}
		}
	}
</style>