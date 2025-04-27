<template>
  <div id="mainNovle">
    <div v-show="selectList">
      <div>选择回忆录</div>
      <ul>
        <li v-for="(item, index) in novels" :key="item.id" @click="selectNovel(item)">
          {{ item.name }}
        </li>
      </ul>
    </div>
    <div v-if="nowNovel">
      <div class="header">
        <div>已选择回忆录: {{ selectedNovel.name }}</div>
        <div @click="backList">返回</div>
      </div>
      <ul>
        <li v-for="chapter in selectedNovel.chapters" :key="chapter.id" @click="selectChapter(chapter)">
          {{ chapter.name }}
        </li>
      </ul>
    </div>

    <div v-show="nowChapter">
      <div class="header">
        <h4>当前章节: {{ selectedChapter.name }}</h4>
        <div>
          <span @click="playAudio">{{audioText}}</span>
          <!-- <audio ref="audioElement" src="https://lidoogroup.com/test.mp3"></audio> -->
        </div>
        <div @click="backNovel">返回</div>
      </div>
	  <view class="music-progress-bar" @click="progressClick">
	    <view class="progress-bar-line" :style="{width: playInfo.progressWidth + '%'}" @touchmove="progressMouseMove"
	      @touchend="progressMouseDown"></view>
	  </view>
	  <view class="show_time">
	  				<view>{{playInfo.currentValue}}</view>
	  				<view>{{playInfo.durationValue}}</view>
	  </view>
      <div class="header">
        <button :disabled="selectedChapter.chapter == 1" @click="prePage">上一章</button>
        <button :disabled="selectedChapter.chapter == selectedNovel.chapters.length" @click="nextPage">下一章</button>
      </div>
      <div class="novel-container">
        <div class="novel-text" v-html="novelText"></div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'App',
    data() {
      return {
        novels: [
          { id: 1, name: '回忆录A', chapters: [{ id: 1, name: '第一章', music: "https://lidoogroup.com/test.mp3", chapter: 1 }, { id: 2, name: '第二章', music: "https://lidoogroup.com/test2.mp3", chapter: 2 }] },
          { id: 2, name: '回忆录B', chapters: [{ id: 1, name: '第一章', music: "https://lidoogroup.com/test.mp3", chapter: 1 }, { id: 2, name: '第二章', music: "https://lidoogroup.com/test2.mp3", chapter: 2 }, { id: 3, name: '第三章', music: "https://lidoogroup.com/test3.mp3", chapter: 3 }] }
        ],
        selectedNovel: { id: 1, name: '',  chapters: [] },
        selectedChapter: [{ id: 1, name: '', music: "", chapter: 1 }],
        selectList: true,
		nowChapter:false,
        nowNovel: false,
		paused: true,
		isMove: false,
        novelText: '',
        audioAction: {
          method: 'pause'
        },
        audioText: "语音阅读",
		progressParentWidth: 0,
        playInfo: {
					progressWidth: 2, // 滚动条
					currentTime: 0, // 当前音乐时间s
					currentValue: '00:00', // 转换成时间展示
					duration: 0, // 当前音乐总时间s
					durationValue: '00:00', // 总时间转换展示 
				},
      };
    },
    created() {
		this.$nextTick(async () => {
			const query = uni.createSelectorQuery().in(this);
			query.select('.music-progress-bar').boundingClientRect(data => {
				debugger
				this.progressParentWidth = data ? Math.floor(data.width) : 0;
			}).exec();
		});
    },
			onUnload() {
				// 卸载时关闭监听
				this.$AudioContext.offPlay();
				this.$AudioContext.offPlay();
			},
    methods: {
      async loadNovelText() {
        try {
          const response = await fetch('https://lidoogroup.com/novel.txt');
          const text = await response.text();
          this.novelText = text;
        } catch (error) {
          console.error('Error loading novel text:', error);
        }
      },
	  handlePlay() {
	  				this.$AudioContext.paused ? this.$AudioContext.play() : this.$AudioContext.pause();
	  				this.paused = this.$AudioContext.paused;
	  				this.recursionDeep();
	  			},
	  // 秒转换为分钟
	  			secondsToMinutesWithSeconds(seconds) {
	  				const minutes = Math.floor(seconds / 60);
	  				const remainingSeconds = Math.floor(seconds % 60);
	  				return `${this.padZero(minutes)}:${this.padZero(remainingSeconds)}`;
	  			},
	  			// 补零
	  			padZero(number, length = 2) {
	  				var str = "" + number;
	  				while (str.length < length) {
	  					str = "0" + str;
	  				}
	  				return str;
	  			},
				// 递归循环获取最新音乐进度信息
							recursionDeep() {
								clearTimeout(this.timer);
								if (this.paused) {
									return
								};
								this.timer = setTimeout(() => {
									if (!this.isMove) {
										this.setPlayInfo();
										this.recursionDeep();
									}
								}, 500)
							},
	  // 设置播放对象
	  	    setPlayInfo() {
				    // debugger
	  				const currentTime = this.$AudioContext.currentTime || 0;
	  				const duration = this.$AudioContext.duration || 0;
	  				const progressWidth = duration === 0 ? '2' : Math.floor((currentTime / duration) * 100);
	  				const currentValue = this.secondsToMinutesWithSeconds(currentTime);
	  				const durationValue = this.secondsToMinutesWithSeconds(duration);
	  				this.playInfo = {
	  					currentTime,
	  					duration,
	  					progressWidth,
	  					currentValue,
	  					durationValue
	  				};
	        },
      // 进度条点击事件
			progressClick(event) {
				// debugger
				const {
					x
				} = event.detail;
				const progressWidth = Math.floor(x / this.progressParentWidth * 100);
				this.playInfo.progressWidth = progressWidth > 100 ? 100 : progressWidth;
				console.log("event", event);
				this.progressMouseDown();
			},
      // 音乐进度条移动监听
			progressMouseMove(event) {
				if (!this.$AudioContext.src) {
					return;
				}
				this.isMove = true;
				const {
					pageX
				} = event.changedTouches[0];
				const progressWidth = Math.floor(pageX / this.progressParentWidth * 100);
				this.playInfo.progressWidth = progressWidth > 100 ? 100 : progressWidth;
			},
			// 音乐进度条停止监听
			progressMouseDown(event) {
				// debugger
				this.isMove = false;
				const currentTime = Math.floor(this.$AudioContext.duration * (this.playInfo.progressWidth / 100));
				this.$AudioContext.seek(currentTime);
				this.setPlayInfo();
				if (!this.$AudioContext.paused) {
					this.$AudioContext.pause();
				}
				this.handlePlay();
			},
      selectNovel(novel) {
        this.selectedNovel = novel;
        this.selectList = false;
        this.nowNovel = true;
        this.selectedChapter =  [{ id: 1, name: '', music: "", chapter: 1 }]; // 重置选择的章节
      },
      selectChapter(chapter) {
        console.log(chapter)
        this.selectedChapter = chapter;
        this.loadNovelText(this.selectedNovel);
        this.$AudioContext.src = this.selectedChapter.music;
		this.setPlayInfo();
		this.nowChapter=true;
        this.nowNovel = false; // 隐藏章节列表，展示章节信息
      },
      backList() {
        this.selectList = true;
        this.nowNovel = false; // 确保隐藏小说页面
        this.selectedNovel = { id: 1, name: '',  chapters: [] };
        this.selectedChapter = [{ id: 1, name: '', music: "", chapter: 1 }]; // 可选：重置章节选择
      },
      backNovel() {
        this.nowNovel = true; // 显示小说章节列表
		this.nowChapter=false;
        this.selectedChapter = [{ id: 1, name: '', music: "", chapter: 1 }]; // 重置选择的章节
        this.audioText = "语音阅读"
        this.$AudioContext.pause();
      },
      playAudio() {
        if (this.audioText == "语音阅读") {
          this.handlePlay();
		  this.$AudioContext.onPlay((e) => {
		  				// 开始播放获取音乐信息
		  				this.setPlayInfo();
		  });
          this.audioText = "暂停"
        }
        else if (this.audioText == "暂停") {
          this.$AudioContext.pause();
          this.audioText = "语音阅读"
        }
      },
      prePage() {
        this.audioText = "语音阅读"
        this.$AudioContext.pause();
        this.selectChapter(this.selectedNovel.chapters[this.selectedChapter.chapter - 2])
      },
      nextPage() {
        this.audioText = "语音阅读"
        this.$AudioContext.pause();
        this.selectChapter(this.selectedNovel.chapters[this.selectedChapter.chapter])
      }
    }
  };
</script>

<style>
  #mainNovle {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    cursor: pointer;
    margin: 10px 10px;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
  }

  li:hover {
    background-color: #f0f0f0;
  }

  .header {
    display: flex;
    justify-content: space-between;
    padding: 20px;
  }

  .novel-container {
    padding: 16px;
    font-size: 16px;
    line-height: 1.5;
    background-color: #f5f5f5;
    color: #333;
  }

  .novel-text {
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
    /* 优化移动端滚动 */
    height: 100vh;
    /* 视口高度，可根据需要调整 */
  }

  .music-progress-bar {
    position: relative;
    width: 100%;
    height: 6rpx;
    border-radius: 6rpx;
    background-color: #f3e7d9;
	.progress-bar-line {
	      position: absolute;
	      top: 0%;
	      left: 0%;
	      width: 2%;
	      height: 100%;
	      border-radius: 6rpx;
	      background-color: #e1964b;
	    }
	
	    .progress-bar-line::after {
	      content: "";
	      display: inline-block;
	      position: absolute;
	      right: 0%;
	      top: 50%;
	      transform: translateY(-50%);
	      width: 20rpx;
	      height: 20rpx;
	      background-color: #fff;
	      border-radius: 50%;
	    }
  }
  
  .show_time {
  		width: 100%;
  		display: flex;
  		justify-content: space-between;
  		margin-top: 12rpx;
  	}
</style>