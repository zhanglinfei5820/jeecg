<template>
	<view class="container">
	
	  <view class="" v-for="(item,key) in list"  :key="key">
		 
	  	 <view v-for="(it,index) in item" :key="index"  class="box">
	  	 	
			  <view class="row flex justify-between">
				  <view class="">
					    <view class="flex align-center justify-center">第{{ key }}世</view>
					<!-- <text class="size-12 gray">{{it.nameInfo}}</text> -->
				  </view>
				  <view class="flex align-center" @click="edit(it)">
					
					 <image style="width: 48rpx;height: 48rpx;margin-right: 10rpx;" src="../static/edit.jpg" mode="aspectFill"></image>
					  <text style="font-size: 36rpx;">编辑</text>
				  </view>
			  </view>
			  <view class="row">
			  	{{it.name}}
			  </view>
			 <view class="row1">
			  	  {{it.info}}
			  </view>
			  
			  <view v-if="it.partnername" class="row2">
			  	<text style="color: #333;">配偶：</text>
				<text>{{it.partnername}}</text>
			  </view>
	  	 </view>
	  </view>
	 
	</view>

</template>

<script>
	import request from '../../../request/index'
	
	export default {
		
		data() {
			return {
				title: "琅琊王氏族谱",
				list:{
					// "一":[{
					// 	name:"王三",
					// 	info:"生于1986年，本科，爱岗敬业，曾获得2018感到中国特别奖",
					// 	partnername:"张姨",
					// 	nameInfo:""
					// }],
					// "二":[{
					// 	name:"王三",
					// 	info:"生于1986年，本科，爱岗敬业，曾获得2018感到中国特别奖",
					// 	partnername:"张姨",
					// 	nameInfo:""
					// },
					// {
					// 	name:"王二",
					// 	info:"生于1986年，本科，爱岗敬业，曾获得2018感到中国特别奖",
					// 	partnername:"张姨",
					// 	nameInfo:""
					// }
					// ],
					// "三":[{
					// 	name:"王三",
					// 	info:"生于1986年，本科，爱岗敬业，曾获得2018感到中国特别奖",
					// 	partnername:"张姨",
					// 	nameInfo:""
					// }]
				},
				}
		},
		onLoad() {
      let pedigreeInfo = JSON.parse(uni.getStorageSync("pedigree") || "{}");
			
		uni.setNavigationBarTitle({
			title: pedigreeInfo?.familyname
		});
		this.getData();
		},
		methods: {
			getData(){
				//获取户主家族信息，遍历所有儿子的ID
		 let treeDataList = JSON.parse(uni.getStorageSync("treeDataList") || "{}");
				 //获取第一代信息
			let	 firstList =  treeDataList.filter(item => {
				return item.householdrelationship == 'parent'
			});
				 //获取第二代信息
			let	 twoList =  treeDataList.filter(item => {
				return item.householdrelationship == 'me' ||item.householdrelationship == 'wife'
			} )
				 //获取第三代信息
				let threeList  = treeDataList.filter(item=>item.householdrelationship == 'daughter' || item.householdrelationship == 'son' )
			//获取第四代信息
		  let	fourList  = treeDataList.filter(item=>item.householdrelationship == 'granddaughter' || item.householdrelationship == 'grandson' )
				 
				 let obj ={};
				 if(firstList.length > 0){
					 let arr = this.mege(firstList);
					 obj["一"] = arr;
				 }
				 if(twoList.length > 0){
				 					 let arr = this.mege(twoList,'two');
				 					 obj["二"] = arr;
				 }
				 if(threeList.length > 0){
				 					 let arr = this.mege(threeList);
				 					 obj["三"] = arr;
				 }
				 if(fourList.length > 0){
				 					 let arr = this.mege(fourList);
				 					 obj["四"] = arr;
				 }
				// console.log(obj,99)
				 this.list = obj;
				 
			},
			//拼接合并
			mege(data,type){
				let arr = [];
				data.map(item => {
					// name:"王三",
					// info:"生于1986年，本科，爱岗敬业，曾获得2018感到中国特别奖"
					// let year = this.timestampToYear(item?.birtherday);
						// let year = item?.birtherday.split("-")[0];
						let year = item?.birtherday;
					let education = item.education ? `${item.education}` : "" ;
					let introduction = item.introduction ? `,${item.introduction}` : "" ;
					
					let info = year ? `生于${year}，${education}${introduction}` : ""
					let nameInfo = "";
					if(type == "two"){
						if(item.householdrelationship == "me"){
							nameInfo = `(户主)`
						}
						else if(item.householdrelationship == "wife"){
						nameInfo = `(户主配偶)`	
						}
					}
					arr.push({
						...item,
						name:item.name,
						info:info,
						nameInfo:nameInfo
					})
				})
				return arr
			},
		timestampToYear(timestamp) {
			
			    // 创建一个 Date 对象
			    const date = new Date(Number(timestamp));
			  
			    // 获取年份
			    const year = date.getFullYear();
			    
			    // 返回年份字符串（你可以根据需要格式化输出）
			    return year;
			},
			edit(it){
				//console.log(it,999)
				uni.navigateTo({
					url: `/pages/pedigreeMoudle/writePedigree/index?type=${it.householdrelationship}&name=${it.name}&id=${it.householderid}&sex=${it.gender}&isEdit=true&userId=${it.id}`
				});
			},
            goEdit(){
				uni.redirectTo({
					url: '/pages/pedigreeMoudle/addPedigree/index'
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
		background-color: #EEECEB;
		padding: 22rpx  24rpx;
		overflow: scroll;
		.box{
			width: 100%;
			background-color: #fff;
			border-radius: 16rpx;
			font-size: 36rpx;
			color: #333;
			padding:24rpx 18rpx;
			margin-bottom: 32rpx;
			.row{
				height: 68rpx;
				margin-bottom: 8rpx;
				font-size: 48rpx;
			}
			.row1{
				// height: 50rpx;
				margin-bottom: 8rpx;
				color: #777;
				padding: 7rpx 0;
			}
			.row2{
				height: 56rpx;
				color: #777;
			}
		}
	}
	

</style>