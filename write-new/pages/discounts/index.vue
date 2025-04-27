
<template>
	<view class="container">
	   <image  v-for="(item,index) in banners" :src="item.simage" mode="aspectFill"  :key="index"></image>
	</view>

</template>

<script>
	export default {
		data() {
			return {
				banners:[]
			}
		},
		onLoad() {
			this.discounts()
		},
		methods:{
			discounts(){
				this.$request({
					url: "/functionMenu/poster/list",
					method: "get",
					data: {
						module: "惠"
					}
				}).then((res)=>{
					if (res?.data?.rows.length > 0) {
						this.banners = res?.data?.rows;
						// console.log(banners,77)
						//this.bannerFamilyTree =banners;
					
					}
				})
			},
		}
		
	}
</script>

<style scoped lang="scss">
	.container {
		width: 100%;
		height: 100vh;
		overflow: hidden;
		padding: 24rpx;
		image {
			width: 100%;
			height: 320rpx;
			border-radius: 16rpx;
			margin-bottom: 32rpx;
		}
	}



	
</style>