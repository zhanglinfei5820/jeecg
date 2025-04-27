<template>
	<view class="container2">
		
		<Load v-if="questions.length == 0"></Load>
		
			<image class="background-image" src="https://lidoogroup.com/bg1.png" mode="aspectFill"></image>
		<view  class="progress-bar">
			<view style="height: 44rpx;font-size: 40rpx;color: #333;margin-bottom: 8rpx; ">
				<text style="margin-right: 10rpx;">答题进度</text>
				<text>{{currentQuestionIndex + 1}}/</text>
				<text style="color: #777;font-size: 32rpx;">{{total}}</text>
			</view>
			<progress class="process" :percent="progressPercent" activeColor="#FA8C16" backgroundColor="#fff"
				borderRadius="15" strokeWidth="8" />
		</view>
		<swiper class="swiper" duration="500" :current="currentQuestionIndex" @change="onSwiperChange">
			<swiper-item v-for="(question, index) in questions" :key="index">
				<view class="question-text">
					<text>{{ index + 1 }}.{{ question.questioncontent }}</text>
					<text style="color: rgb(250, 140, 22);font-size: 36rpx;" v-if="question.wstate == 1">(必填)</text>
					<text style="color: #FFDC90;font-size: 36rpx;" v-else>(选填)</text>
				</view>
				<textarea class="answer-input" maxlength="-1" v-model="questions[index].answer" placeholder="请输入您的答案" />

				<button class="save" @click="saveAnswersToLocalStorage(false,true)">保存</button>
				<view class="tip">
					<image src="../../static/image/tip.jpg" mode=""></image>
					<view class="input-tip">请手机安装讯飞输入法语音输入</view>
				</view>

			</swiper-item>
		</swiper>
		<view class="bottom-bar">

			<view class="navigation-buttons">
				<button style="background-color: #FFEBD5;color: #FA8C16;border: 1px solid #FA8C16;"
					@click="prevQuestion" :disabled="currentQuestionIndex === 0">上一题</button>

				<button style="background-color: #FFDC90;color: #FA8C16;" @click="allQuestionsList">目录</button>
				<button v-if="currentQuestionIndex !== questions.length - 1"
					style="background-color: #FA8C16;color: #fff;" @click="nextQuestion(questions)"
					:disabled="currentQuestionIndex === questions.length - 1">下一题</button>
				<button v-else style="background-color: #FA8C16;color: #fff;"
					@click="saveAnswersToLocalStorage(true)">提交</button>
			</view>

		</view>
		<uni-popup ref="popup" type="bottom" border-radius="10px 10px 0 0" background-color="#fff">
			<view class="popup-container">
				<view class="popup-title">
					<view>
						<text>调查问卷</text>
						<text style="color: #777;margin-left: 16rpx;">(一共{{total}}题)</text>
					</view>
					<uni-icons size="20" @click="close" color="#000" type="closeempty"></uni-icons>
				</view>
				<view class="popup-content">
					<view class="popup-box" v-for="(question, index) in questions" :key="index"
						@click="selectPage(index)">
						<image class="require" v-if="question.wstate == 1" src="../../static/image/require.jpg" mode="">
						</image>
						<text class="content ellipsis"
							:class="{'active-conten':question.answer !=''}">{{index +1}}、{{ question.questioncontent }}</text>
						<image class="select" v-if="currentQuestionIndex == index"
							src="../../static/image/actionIndex.jpg" mode=""></image>
					</view>
				</view>
			</view>
		</uni-popup>

	</view>
</template>

<script>
	import Load from '../../component/Load.vue';
	export default {
        components:{
			Load,
		},
		data() {
			return {
				currentQuestionIndex: 0,
				orderId: "",
				id: "",
				level: "",
				pageNum: 1,
				pageSize: 50,
				pageNum1: 1,
				pageSize1: 30,
				total: 0,
				questions: [
					// { text: '问题1：您对课程的满意度如何？' },
					// { text: '问题2：您认为课程的内容是否丰富？' },
					// { text: '问题3：您对讲师的讲解是否满意？' },
					// { text: '问题4：您有什么建议或意见？' }
				]
			};
		},
		computed: {
			progressPercent() {
				return ((this.currentQuestionIndex + 1) / this.questions.length) * 100;
			}
		},
		onLoad(options) {
			//console.log(options,11111)
			this.orderId = options.orderId;
			this.id = options.id; // 模板ID
			this.level = options.level; //1显示30道题，2显示所有

			
			// this.getList()  
		},
		onShow(){
			//判断本地是否存取问题和答案，没有则获取
			this.loadSavedAnswers();
		},
		created() {

		},
		mounted() {
			this.saveInterval = setInterval(() => {
				this.saveAnswersToLocalStorage(false,false);
			}, 10 * 60 * 1000); // 每5分钟保存一次
		},
		destroyed() {
			clearInterval(this.saveInterval)
		},
		beforeDestroy() {
			//销毁前，保存答案，如果是提交则不需要
			this.saveAnswersToLocalStorage(false,false);
			
		},
		methods: {
			allQuestionsList() {
				//查看所有的目录
				this.$refs.popup.open()
			},
			close() {
				this.$refs.popup.close()
			},
			//目录选中跳转
			selectPage(index) {
				// if(index <= this.currentQuestionIndex){
				// 	this.currentQuestionIndex =index;
				// 	this.close()
				// }
				this.currentQuestionIndex = index;
				this.close()
			},
			saveAnswersToLocalStorage(type,isToast) {
				const savedData = {
					orderId: this.orderId,
					questions: JSON.stringify(this.questions)
				}

				uni.setStorageSync('savedAnswers', JSON.stringify(savedData));

				if (!!type) {
					this.delAllQuestions("submit")
				} else {
					if(!!isToast){
						this.delAllQuestions("save",true)
					}else{
						this.delAllQuestions("save",false)
					}
					
				}

			},
			//第一次进入查找问题
			getList() {
				this.$request({
					url: "/functionMenu/surveyquestion/list",
					method: "get",
					data: {
						surveytemplate: this.id,
						pageNum: this.pageNum,
						pageSize: this.pageSize
					}
				}).then((res) => {
					//console.log(res,111)
					if (res?.data?.rows.length > 0) {
						let data1 = res?.data?.rows;
						//console.log(res?.data?.total,111)

						let userid = this.$store.state.userInfos.uid;
						data1.map((item, index) => {

							let obj = {
								"questioncontent": item.questioncontent,
								"questionid": item.id,
								"questionnumber": item.questionnum,
								"template": item.surveytemplate,
								"userid": userid,
								"answer": "",
								"orderid": this.orderId,
								"wstate": item.wstate || 1, //1必填2选填
							}
							if (this.level == "1") {
								if (index < 26) {

									this.questions.push(obj)
								}

							} else {
								this.questions.push(obj)
							}


						})

						if ((Number(res?.data?.total) > this.pageNum * this.pageSize) && this.level != "1") {

							this.pageNum++;
							this.total = res?.data?.total;
							this.getList();
						} else {
							this.total = res?.data?.total > 26 ? 26 : res?.data?.total;
						//	this.historyAnswerList();
						}
						// this.historyAnswerList();
					}
				})
			},
			//删除所有答案

			delAllQuestions(type,isToast) {
				
				this.$request({
					url:"/functionMenu/answer/removeByorderid?orderid=" + this.orderId,
					method:"get"
				}).then((res)=>{
					//删除所有后，再新增
					//console.log(res,"del")

					if(res?.data?.code == 200 ||res?.data?.msg =="该答案数据全部清空，删除失败！"){
					 if(type == "submit"){
					 	this.submit("submit")
					 }else{
						 if(!!isToast){
						   this.submit("edit",true)	 
						 }else{
							 this.submit("edit",false)	 
						 }
					 	
					 }	
					}

				})
			},
			//继续答题，查找以前答题记录
			historyAnswerList() {
				this.$request({
					url: "/functionMenu/answer/list",
					method: "get",
					data: {
						orderid: this.orderId,
						// pageNum: this.pageNum1,
						// pageSize: this.pageSize1
					}
				}).then((res) => {
					//console.log(res,"答案列表")
					if (res.data.rows.length > 0) {
					   this.questions = JSON.parse(res.data.rows[0].answer);
					   this.total = this.questions.length;
						let lastAnswerIndex = 0;
					
						//查找最后答题的索引
						for (let i = this.questions.length - 1; i >= 0; i--) {
							if (this.questions[i]?.answer) {
								
								lastAnswerIndex = i;
								break;
							}
						}
						this.currentQuestionIndex += lastAnswerIndex
					}else{
						this.getList()
					}
					//查找第几个索引没有回答直接跳到对应索引
				}).catch(()=>{
					this.getList()
				})
			},

			onSwiperChange(e) {
				this.currentQuestionIndex = e.detail.current;
			},

			prevQuestion() {
				if (this.currentQuestionIndex > 0) {
					this.currentQuestionIndex--;
				}
			},
			nextQuestion(item) {
				// console.log(item,222)
				//如果当前信息没有填写禁止下一页
				// if(this.questions[this.currentQuestionIndex].answer == "" && this.questions[this.currentQuestionIndex].wstate == 1){
				// 	uni.showToast({
				// 		title:"请回答后再进入下一题"
				// 	})
				// 	return false
				// }
				if (this.currentQuestionIndex < this.questions.length - 1) {
					this.currentQuestionIndex++;
				}
			},
			submit(type,isToast) {
				if (type == "submit") {
					//遍历，如果有必填没填则跳到必填页面
					const hasUnansweredRequired = this.questions.some((item, index) => {
						if (item.answer == "" && item.wstate == "1") {
							uni.showToast({
								title: `请填写第${index + 1}题内容`
							});
							this.currentQuestionIndex = index;
							return true; // 停止遍历
						}
						return false;
					});
					if (hasUnansweredRequired) {
						return; // 停止后续逻辑
					}
				}
				let type1 = type =="submit"  ?  "全部" :"save" 
				// console.log(type1,111)
				//  return
				this.$request({
					url: `/functionMenu/answer/add?type=${type1}&orderid=${this.orderId}`,
					method: "post",
					data: {
						answer: JSON.stringify(this.questions),
						orderid: this.orderId,
					}
				}).then((res) => {
					//console.log(res,999)
					if (type =="submit") {
						
						uni.showToast({
							title: "提交成功"
						})

						setTimeout(()=>{
							uni.switchTab({
								url: "/pages/my/index"
							})
						},1000)
					} else {
						if(isToast){
							uni.showToast({
								title: "保存成功"
							})
						}
						
					}

				})
			},
			loadSavedAnswers() {
				const savedData = uni.getStorageSync('savedAnswers') || "{}";
				const parsedData = JSON.parse(savedData);
			
				if (!!parsedData && parsedData?.orderId === this.orderId) {
					let questions = JSON.parse(parsedData.questions);
				
					if (questions.length > 0) {
						this.questions = questions;
						this.total = questions.length;
						let lastAnswerIndex = 0;
						//查找最后答题的索引
						for (let i = questions.length - 1; i >= 0; i--) {
							if (questions[i].answer) {
								lastAnswerIndex = i;
								break;
							}
						}
						this.currentQuestionIndex = lastAnswerIndex
					}
					else {
					//this.getList()
					this.historyAnswerList()
				}

				} else {
					// this.getList()
					this.historyAnswerList()
				}
			},

		}
	};
</script>

<style scoped lang="scss">
	.container2 {
		display: flex;
		flex-direction: column;
		// height: calc(100vh - 88rpx);
		height: 100vh;
		overflow: hidden;
		padding: 16rpx 24rpx;
		background-color: #ffebd5;
		// background:url("../../static/image/bg.png") center no-repeat;
		// background-size: 750rpx;
	}

	.background-image {
		position: absolute;
		top: -80rpx;
		left: 0;
		width: 100%;
		height: 100%;
		z-index: 0;
	}

	.progress-bar {
		width: 300rpx;
		margin-bottom: 20%;
		position: relative;
		z-index: 1;

		.process {
			width: 260rpx;
			height: 16rpx;
			border-radius: 50rpx;

		}
	}

	.swiper {
		width: 100%;
		height: 70%;
		padding: 38rpx 38rpx 0;
		background-color: #fff;
		display: flex;
		position: relative;
	}



	.question-number {
		font-size: 32rpx;
		margin-bottom: 20rpx;
		display: flex;

	}

	.question-text {
		font-size: 36rpx;
		margin-bottom: 20rpx;
		color: #333;
		font-size: 40rpx;
		min-height: 100rpx;
		display: block;
	}

	.answer-input {
		width: 100%;
		min-height: 55.5%;
		padding: 20rpx;
		background-color: #F2F2F2;
		border-radius: 12rpx;
		font-size: 36rpx;
		flex: 1;
		margin-top: 24rpx;
	}

	.tip {
		height: 50rpx;
		color: #aaaaaa;
		font-size: 36rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-top: 24rpx;

		image {
			height: 48rpx;
			width: 48rpx;
			margin-right: 18rpx;
		}
	}

	.save {
		position: absolute;
		right: 0;
		bottom: 24rpx;
		height: 60rpx;
		width: 140rpx;
		margin-top: 24rpx;
		font-size: 36rpx;
		color: #FA8C16;
		background-color: rgba(247, 152, 69, 0.15);
		margin: 0;

	}


	.bottom-bar {

		// background-color: #f5f5f5;
		width: 100%;
		position: fixed;
		bottom: 50rpx;
		padding: 0 24rpx;
		left: 0;

	}



	.navigation-buttons {
		display: flex;
		justify-content: space-between;
		width: 100%;

		button {
			width: 226rpx;
			height: 84rpx;
			background-color: #1aad19;
			color: white;
			border: none;
			border-radius: 16rpx;
			font-size: 36rpx;
			display: flex;
			align-items: center;
			justify-content: center;

		}

		button:disabled {
			background-color: #ccc;
		}

	}

	.popup-container {
		height: 85vh;
		padding: 14rpx 24rpx;

		.popup-title {
			height: 96rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			font-size: 36rpx;
			color: #333;

		}

		.popup-content {
			padding: 0 25rpx;
			height: calc(85vh - 116rpx);
			overflow-y: scroll;

			.popup-box {
				display: flex;
				align-items: center;
				height: 98rpx;
				border-bottom: 1px solid #E6E6E6;

				.require {
					width: 14rpx;
					height: 14rpx;
					margin-right: 18rpx;
				}

				.content {
					font-size: 36rpx;
					color: #aaaaaa;
					width: 560rpx;
				}

				.active-conten {
					color: #52C41A;
				}

				.select {
					height: 36rpx;
					width: 36rpx;
				}
			}
		}
	}
</style>