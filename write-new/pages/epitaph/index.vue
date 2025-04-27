
<template>
    <view class="container">
        <!-- 其他内容 -->
        <view class="comments-container">
            <view 
                v-for="(comment, index) in comments" 
                :key="index" 
                class="comment-box"
                :style="{ marginLeft: getRandomMarginLeft() + 'rpx' }"
            >
                {{ comment.text }}
            </view>
        </view>
        <!-- 其他内容 -->
    </view>
</template>



<script>
	export default {
		data() {
			return {
				   comments: [
				            { text: '这是一个留言示例1' },
				            { text: '这是一个留言示例2' },
				            { text: '这是一个留言示例3' }
				        ]
			}
		},
		onLoad() {
			
		},
		methods: {
		    getRandomMarginLeft() {
		        // 假设屏幕宽度为750rpx，留言框宽度为380rpx
		        const screenWidth = 750;
		        const commentWidth = 380;
		        // 计算最大可移动范围
		        const maxMarginLeft = screenWidth - commentWidth;
		        // 生成随机数
		        return Math.floor(Math.random() * maxMarginLeft);
		    },
		   
		}
	}
</script>

<style scoped lang="scss">
	.comments-container {
	    display: flex;
	    flex-direction: column;
	    align-items: flex-start; // 确保留言框从左侧开始排列
	}
	
	.comment-box {
	    width: 380rpx;
	    padding: 20rpx;
	    background-color: #fff;
	    border-radius: 16rpx;
	    margin-bottom: 20rpx;
	    box-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.1);
	}
</style>