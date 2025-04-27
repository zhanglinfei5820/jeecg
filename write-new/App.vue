<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style lang='scss'>
	@import '@/uni_modules/uni-scss/index.scss';

	/*每个页面公共css */
	view,
	scroll-view,
	swiper,
	swiper-item,
	cover-view,
	cover-image,
	icon,
	text,
	rich-text,
	progress,
	button,
	checkbox,
	form,
	input,
	label,
	radio,
	slider,
	switch,
	textarea,
	navigator,
	audio,
	camera,
	image,
	video {
		box-sizing: border-box;
	}
	
	::-webkit-scrollbar {
	    display: none;
	    /* 对于Webkit浏览器 */
	}
	/* button样式改写 */
	uni-button,
	button {
		/* height: 80upx;
		line-height: 80upx; */
		font-size: 24rpx;
		font-weight: normal;
        border: none;
		display: flex;
		align-items: center;
        justify-content: center;
		padding: 0;
	}
	uni-button:after {
		width: 0;
		border: none;
	}
	button:after {
		width: 0;
		border: none;
	}

	.swiper  .wx-swiper-dot {
		height: 8rpx;
		width: 8rpx;
	}
	
	.swiper  .wx-swiper-dot-active {
		width: 28rpx; /* 高亮时的宽度 */
		background-color: #fff; /* 高亮时的颜色 */
		border-radius: 8rpx; /* 高亮时的圆角 */
	}
	.font-pingfang{
		font-family: PingFang SC;
	}
	/* input 样式 */
	.input-placeholder {
		color: #999999;
	}
	
	.placeholder {
		color: #999999;
	}
	.flex {
	  display: flex;
	}
	.flex-direction {
	  flex-direction: column;
	}
	
	.flex-wrap {
	  flex-wrap: wrap;
	}
	
	.align-start {
	  align-items: flex-start;
	}
	
	.align-end {
	  align-items: flex-end;
	}
	
	.align-center {
	  align-items: center;
	}
	
	.align-stretch {
	  align-items: stretch;
	}
	
	.self-start {
	  align-self: flex-start;
	}
	
	.self-center {
	  align-self: flex-center;
	}
	
	.self-end {
	  align-self: flex-end;
	}
	
	.self-stretch {
	  align-self: stretch;
	}
	
	.align-stretch {
	  align-items: stretch;
	}
	
	.justify-start {
	  justify-content: flex-start;
	}
	
	.justify-end {
	  justify-content: flex-end;
	}
	
	.justify-center {
	  justify-content: center;
	}
	
	.justify-between {
	  justify-content: space-between;
	}
	
	.justify-around {
	  justify-content: space-around;
	}
	.padding-mid {
		padding: 0 20upx;
	}
	.padding-sm {
		padding:0 10upx;
	}
	.padding-all-sm {
		padding:10upx;
	}
	.padding-vertical-sm {
		padding:10upx 0;
	}
	.margin-top-mid{
		margin-top: 40upx;
	}
	.margin-top-sm{
		margin-top: 20upx;
	}
	.margin-top-ssm{
		margin-top: 10upx;
	}
	.white{
		color: #fff;
	}
	.gray{
		color: #777;
	}
	.333{
		color:#333;
	}
	.size-10{
		font-size: 20rpx;
	}
	.size-12{
		font-size: 24rpx;
	}
	.size-18{
		font-size: 36rpx;
	}
	.size-20{
		font-size: 40rpx;
	}
	.size-24{
		font-size: 48upx;
	}
	.flex-1{
		flex: 1;
	}
	.bg-color-blue{
		background-color: #55aaff;
	}
	.bg-color-white{
		background-color: #fff;
	}
	.bg-color-888{
		background-color: #888888;
	}
	.bg-color-eeeceb{
		background-color: #eeeceb;
	}
	.blue{
		color: #55aaff;
	}
	.weight-500{
		font-weight: 500;
	}
	.weight-600{
		font-weight: 600;
	}
	.ellipsis {  
	    white-space: nowrap;  
	    overflow: hidden;  
	    text-overflow: ellipsis;  
	  }  
	  .gap{
		  gap:20upx
	  }
	  .bordr-radius{
		  border-radius: 10upx;
	  }
	  .under-line{
		  height: 2upx;
		  background-color: #ccc;
	  }
</style>
