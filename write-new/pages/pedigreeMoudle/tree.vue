<template>
	<view class="main">
		<!-- <movable-area class="tree" :style="'width:'+screen.width+'px;height:'+(screen.height - 60) +'px;'">
			<movable-view inertia class="canvas"  :style="'width:'+screen.width*14+'px;height:'+screen.height*14+'px;'"  direction="all" out-of-bounds :x="-screen.width*6.5">
				
			</movable-view>
		</movable-area> -->
		<view class="box1">
			<view class="grandSon" v-for="(item,index) in treeDatas.grandSon" :key="index">
				<image v-if="item.sex == 0" src="https://lidoogroup.com/grandson-active.png" mode=""></image>
				<image v-if="item.sex == 1" src="https://lidoogroup.com/granddaughter-active.png" mode=""></image>
			</view>
		</view>
		<view class="box2">
			<view class="son" v-for="(item,index) in treeDatas.son" :key="index">
				<image v-if="item.spouse.length > 0" src="static/sonMerry.jpg" mode=""></image>
				<image v-else-if="item.spouse.length ==0 && item.sex == 0" src="https://lidoogroup.com/son-active.png" mode=""></image>
				<image v-else-if="item.spouse.length ==0 && item.sex == 1" src="https://lidoogroup.com/daughter-active.png" mode=""></image>
			</view>
		</view>
		<view class="box3">
			<image v-if="treeDatas.huzhu.length > 0" src="https://lidoogroup.com/parent-active.png" mode=""></image>
			<image v-if="treeDatas.huzhu.length > 0 && treeDatas.huzhu[0].spouse.length > 0" src="https://lidoogroup.com/mom-active.png" mode=""></image>
		</view>
		<view class="box4">
			<image v-if="treeDatas.grandPa.length > 0" src="./static/grand.jpg" mode=""></image>
			
		</view>

	</view>
</template>

<script>

	export default {
		name: 'genealogical-tree',
		
		props: {
			treeDatas: {
				type: Object,
				default: ()=>{
					return {
					 "grandPa":[],
					 "huzhu":[{
						 spouse:[]
					 }],
					 "son":[],
					 "grandSon":[]
				}
				}
			}
		},
		 watch: {
		    treeDatas: {
		     	handler(newData, old) {
		     		console.log(newData,old,66)
		     		// if(Array.isArray(newData)){
		     		// 	//console.log(newData)
		     			
		     		// }
		     	},
		      immediate: true,
		      deep: true
		    }
		  },
		data() {
			return {
				screen: {
					height: 1920,
					width: 1080
				},
				treeData: [],
			 
			}
		},
		created(){
			// if(this.dataArr&&this.dataArr.length!=0){
			// 	this.treeData = this.dataArr;
			// }
		},
		mounted() {
			// let system = uni.getSystemInfoSync()
			// this.screen = {
			// 	height: system.screenHeight,
			// 	width: system.screenWidth
			// }
			 
		},
		methods: {
			// onChange(e) {
			// 	console.log(e)
			// }
		}
	}
</script>

<style lang="scss" scoped>
	 .main{
		 width: 100%;
		 height: 100vh;
		 position: absolute;
		 top: 0;
		 left: 0;
		 width: 100%;
		 height: 100% !important;
		 z-index: 11;
		.box1{
			margin-top: 27%;
			width: 100%;
			height: 180rpx;
			display: flex;
			align-items: center;
			justify-content: space-around;
			margin-bottom: 16rpx;
			overflow: auto;
			image{
				width: 180rpx;
				height: 180rpx;
			}
		} 
		.box2{
			width: 100%;
			height: 180rpx;
			display: flex;
			align-items: center;
			justify-content: space-around;
			margin-bottom: 16rpx;
			overflow: auto;
			image{
				width: 180rpx;
				height: 180rpx;
			}
		} 
		.box3{
			width: 100%;
			height: 220rpx;
			display: flex;
			align-items: center;
			margin-bottom: 16rpx;
			padding-left: 260rpx;
			image{
				width: 220rpx;
				height: 220rpx;
			}
		} 
		.box4{
			width: 100%;
			height: 220rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-bottom: 16rpx;
			image{
				width: 220rpx;
				height: 220rpx;
			}
		} 
	 }
	
</style>