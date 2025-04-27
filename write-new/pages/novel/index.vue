<template>
	<div class="main-container">
		<div class="novel-container">
			<!-- <div class="novel-text">
				{{novelText}}
			</div> -->
			<div class="novel-text">
				<rich-text :nodes="novelRichText"></rich-text>
			</div>

			<zaudio theme="theme1" themeColor="#42b983" :imgeSet="novelImage" @change-chapter="handleChangeChapter" />
			<div class="bottom-bar">
				<div class="contents" @click="open">
					<image class="tips" src="../../static/mulu.png" mode=""></image>
					<div class="tools">目录</div>
				</div>
				<div class="chapter">
					<div class="title">
						当前章节:{{textName}}
					</div>
				</div>
				<div class="back" @click="goBack">
					<image class="tips" src="../../static/buy.png" mode=""></image>
					<div class="toolsLarge">买同款</div>
				</div>
			</div>
		</div>
		<uni-popup ref="popup" type="bottom" background-color="#E8E3D0" border-radius="10px 10px 0 0" :show="true"
			@close="closePopup">
			<div class="novel-list">
				<div class="novel-img">
					<img :src="novelImage" alt="">
				</div>
				<div class="novel-info">
					<div class="novel-title">
						<div class="novel-name">{{novelName}}</div>
						<image class="tips" src="../../static/close.png" @click="closePopup"></image>
					</div>
					<div class="novel-introduce">连载至30章</div>
				</div>
			</div>
			<div class="content-list">
				<div v-for="(novel, index) in novels" :key="index" class="novel-item" @click="selectNovel(novel)">
					<image v-if="novel.selected" class="select-tips" src="../../static/select.png"></image>
					<span class="chapter-number" :class="{'selected': novel.selected}">{{novel.textday}}</span>
					<span class="chapter-name" :class="{'selected': novel.selected}">{{novel.textname}}</span>
					<!-- <span v-if="novel.selected" class="chapter-percent">读到1%</span> -->
				</div>
			</div>
			<div class="bottom-bar">
				<div class="contents" @click="open">
					<image class="tips" src="../../static/other.png" mode=""></image>
					<div class="tools">目录</div>
				</div>
				<div class="chapter-none"></div>
				<div class="back" @click="closePopup">
					<image class="tips" src="../../static/back.png" mode=""></image>
					<div class="tools">返回</div>
				</div>
			</div>
		</uni-popup>
	</div>
</template>

<script>
	import zaudio from '@/uni_modules/uni-radio/uniapp-zaudio/zaudio.vue';

	export default {
		components: {
			zaudio: zaudio
		},
		data() {
			return {
				audioSrc: [{
					src: "",
					title: "test",
					singer: "test1",
					coverImgUrl: "",
				}],
				novels: [],
				novelText: "",
				novelUrl: "",
				startChapter: 0,
				novelId: "",
				novelName: "",
				readStaus: [],
				novelUser: "",
				textName: "",
				novelImage: "",
				novelRichText: "",
			};
		},
		methods: {
			selectNovel(novel) {
				this.novels.forEach((item, index) => {
					if (item.textday == novel.textday) {
						item.selected = true;
						this.startChapter = index;
					} else {
						item.selected = false;
					}
				});
				this.updateChapter();
				this.closePopup();
			},
			goBack() {
				// debugger
				uni.navigateTo({
					url: '/pages/courseDetail/index?id=2'
				})
			},
			handleChangeChapter(e) {
				const increment = e === 1 ? 1 : -1;
				const newChapter = this.startChapter + increment;

				if (newChapter >= 0 && newChapter < this.novels.length) {
					// this.chcekNovel(newChapter);
					this.startChapter = newChapter;
					this.novels.forEach(item => {
						item.selected = false;
					});
					this.novels[this.startChapter].selected = true;
					this.$zaudio.operate();
					this.updateChapter();
				} else {
					const message = e === 1 ? '已经是最后一章了' : '已经是第一章了';
					uni.showToast({
						title: message,
						icon: 'fail',
						duration: 2000
					});
				}
			},
			updateChapter() {
				// debugger
				this.novelUrl = JSON.parse(this.novels[this.startChapter].textaddress)[0].fileurl;
				this.novelRichText = this.novels[this.startChapter].fuwenben;
				var index = this.novelUrl.indexOf("8888") + 4; // 找到8888的索引并加4
				this.novelUrl = "https://lidoogroup.com/novel" + this.novelUrl.substring(index);
				this.audioSrc[0].src = JSON.parse(this.novels[this.startChapter].audioaddress)[0].musicurl;
				this.textName = this.novels[this.startChapter].textday;
				this.requestNovel();
				console.log(this.audioSrc)
				this.$zaudio.setAudio(this.audioSrc);
				this.$zaudio.setRender(0);
			},
			open() {
				this.$refs.popup.open('bottom');
			},
			closePopup() {
				this.$refs.popup.close();
			},
			requestNovel() {
				var self = this;
				uni.request({
					url: self.novelUrl,
					method: "get",
					success(response) {
						self.novelText = response.data;
						if (response.data.code == 401) {
							uni.navigateTo({
								url: "/pages/login/index"
							});
						}
					},
					fail(err) {
						if (err.data?.code === 401) {
							uni.navigateTo({
								url: "/pages/login/index"
							});
						} else if (err.errMsg.includes('request:fail')) {
							uni.showToast({
								title: '网络异常',
								icon: "error",
								duration: 2000
							});
						} else {
							uni.showToast({
								title: '未知异常',
								duration: 2000
							});
						}
					},
				});
			},
			loadNovel() {
				this.updateChapter();
			},
			chcekNovel(e) {
				let userInfos = this.$store.state.userInfos
				// console.log(userInfos,888)
				if (e >= 3) {
					if (!userInfos || userInfos?.phoneNumber?.length != 11) {
						uni.showModal({
							content: '下单需要手机号授权',
							confirmText: '去登录',
							success: (res) => {
								if (res.confirm) {
									// 保存当前页面的路径和参数
									const currentPage = getCurrentPages().pop();
									const route = currentPage.route;
									const options = currentPage.options;
									uni.setStorageSync('redirectInfo', {
										route,
										options
									});
									uni.navigateTo({
										url: '/pages/login/index'
									});

								}
							}
						});
					}
					else{
						return
					}
				}
				else{
					return 
				}
			},
		},
		onLoad(options) {
			// debugger
			let self = this;
			let listId = JSON.parse(options.data);
			let data = {}
			this.$request({
				url: "/functionMenu/textinfo/" + listId,
				method: "get",
			}).then((res) => {
				data = res.data.data
				self.novelImage = data.imgurl
				self.audioSrc[0].coverImgUrl = self.novelImage
				self.novelId = data.id;
				self.novels = data.textaddressList.map(item => {
					item.selected = false;
					return item;
				});
				self.novels = self.novels.sort((a, b) => {
					return a.textday - b.textday;
				});
				self.novelName = data.textname;
				let userInfos = JSON.parse(uni.getStorageSync('userInfos'));
				this.novelUser = userInfos.uid;

				this.$request({
					url: "/functionMenu/viewrecord/list",
					method: "get",
					data: {
						userid: userInfos.uid
					}
				}).then((res) => {
					console.log("result", res)
					if (res.data.rows.length == 0) {
						self.startChapter = 0;
						self.novels[self.startChapter].selected = true
					} else {
						var list = JSON.parse(res.data.rows[0].endviewtype);
						self.readStaus = res.data.rows;
						list.forEach(item => {
							if (item.novelId == data.id) {
								self.startChapter = item.startChapter;
								self.novels[self.startChapter].selected = true
							}
						});
					}
					// self.chcekNovel(self.startChapter);
					self.loadNovel();
				}).catch(err => console.error('加载错误', err));
			})
			console.log(data)
		},
		beforeDestroy() {
			// debugger
			let self = this;
			let postData = [{
				novelId: this.novelId,
				novelName: this.novelName,
				startChapter: this.startChapter,
				novelImage: this.novelImage,
				novelTime: new Date().getTime(),
			}];
			uni.navigateBack({
				delta: 1,
				success() {
					if (self.readStaus.length == 0) {
						self.$request({
							url: "/functionMenu/viewrecord/add",
							method: "post",
							data: {
								userid: self.novelUser,
								endviewtype: JSON.stringify(postData)
							}
						}).catch(err => console.error('记录错误', err));
					} else {
						let list = JSON.parse(self.readStaus[0].endviewtype)
						let check = true;
						list.forEach(item => {
							if (item.novelId == self.novelId) {
								item.startChapter = self.startChapter;
								item.novelImage = self.novelImage;
								item.novelTime = new Date().getTime();
								check = false;
							}
						});
						if (check) {
							list.push(postData[0]);
						};
						self.$request({
							url: "/functionMenu/viewrecord/edit",
							method: "post",
							data: {
								id: self.readStaus[0].id,
								endviewtype: JSON.stringify(list)
							}
						}).catch(err => console.error('记录错误', err));
					}
					console.log('返回成功');
				},
				fail(err) {
					console.error('返回失败', err);
				}
			});

		}
	};
</script>

<style>
	.main-container {
		height: 100vh;
	}

	.bottom-bar {
		height: 100rpx;
		display: flex;
		gap: 15rpx;
		justify-content: space-around;
		margin-bottom: 20rpx;
	}

	.contents,
	.back {
		height: 100rpx;
		display: flex;
		align-items: center;
		flex-direction: column;
		width: 208rpx;
	}

	.chapter {
		height: 90rpx;
		width: 312rpx;
		border: 4rpx solid #6D5D43;
		border-radius: 50rpx;
	}

	.chapter-none {
		height: 90rpx;
		width: 312rpx;
	}

	.title {
		font-size: 32rpx;
		color: #000;
		text-align: center;
		margin-top: 25rpx;
		font-weight: 500;
	}

	.tips {
		width: 48rpx;
		height: 48rpx;
		margin-top: 10rpx;
	}

	.toolsLarge {
		width: 120rpx;
		height: 42rpx;
		margin-top: 10rpx;
	}

	.tools {
		width: 80rpx;
		height: 42rpx;
		margin-top: 10rpx;
	}

	.main-container {
		background-color: #E8E3D0;
	}

	.novel-list {
		display: flex;
		align-items: center;
		justify-content: flex-start;
		gap: 16rpx;
		margin-bottom: 16rpx;
		margin-top: 16rpx;
	}

	.novel-img {
		width: 84rpx;
		height: 112rpx;
		border-radius: 18rpx;
		overflow: hidden;
		margin-left: 24rpx;
	}

	.novel-img img {
		width: 100%;
		height: 100%;
		object-fit: cover;
	}

	.novel-info {
		width: 650rpx;
		height: 128rpx;
	}

	.novel-title {
		width: 610rpx;
		height: 50rpx;
		display: flex;
		justify-content: space-between;
	}

	.novel-name {
		font-size: 36rpx;
		font-weight: 600;
		color: #000;
		width: 420rpx;
		height: 50rpx;
		margin-top: 10rpx;
	}

	.novel-introduce {
		font-size: 30rpx;
		color: #000;
		margin-top: 10rpx;
	}

	.content-list {
		height: 660rpx;
		overflow-y: auto;
		padding: 30rpx;
	}

	.novel-item {
		height: 115rpx;
		border-bottom: 2rpx solid #77777736;
		display: flex;
		align-items: center;
	}

	.chapter-number,
	.chapter-name {
		font-size: 36rpx;
		color: #333;
		margin-left: 20rpx;
	}

	.select-tips {
		width: 36rpx;
		height: 36rpx;
		margin-right: 14rpx;
	}

	.chapter-percent {
		font-size: 26rpx;
		color: #777;
		margin-left: 260rpx;
	}

	.selected {
		color: #BD8C38;
	}

	.novel-text {
		overflow-y: auto;
		-webkit-overflow-scrolling: touch;
		height: 85vh;
		padding: 30rpx;
	}
</style>