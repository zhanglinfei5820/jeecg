<template>
	<view class="imt-audio" :class="[`${theme}`]" v-if="audiolist.length > 0">
		<template v-if="theme == 'theme3'">
			<slider
				class="audio-slider"
				:activeColor="themeColor"
				block-size="0"
				:value="renderData('current_value')"
				:max="renderData('duration_value')"
				@change="change"
				:disabled="!renderIsPlay"
			></slider>

			<view class="top">
				<view class="audio-control-wrapper">
					<image :src="renderData('coverImgUrl')" mode="aspectFill" class="cover" :class="{ on: !renderData('paused') }"></image>
					
					<image :src="require('./static/loading.png')" v-if="loading" class="play loading"></image>
					<template v-else>
						<image :src="require('./static/playbtn.png')" alt="" @click="operate" class="play" v-if="renderData('paused')"></image>
						<image :src="require('./static/pausebtn.png')" alt="" @click="operate" class="play" v-else></image>
					</template>
				</view>
			</view>
			<view class="audio-wrapper">
				<view class="titlebox">
					<view class="title">{{ renderData('title') }}</view>
					<view class="singer">{{ renderData('singer') }}</view>
				</view>

				<view class="slidebox">
					<view>{{ renderData('current') }}/ {{ renderData('duration') }}</view>
					<view>
						<text @click="changeplay(-1)">上一首</text>
						<text @click="changeplay(1)">下一首</text>
					</view>
				</view>
			</view>
		</template>

		<template v-if="theme == 'theme2'">
			<view class="top">
				<view class="audio-control-wrapper" :style="{backgroundImage: `url(${renderData('coverImgUrl')})`}">
					<image :src="require('./static/loading.png')" v-if="loading" class="play loading"></image>
					<template v-else>
						<image :src="require('./static/playbtn.png')" alt="" @click="operate" class="play" v-if="renderData('paused')"></image>
						<image :src="require('./static/pausebtn.png')" alt="" @click="operate" class="play" v-else></image>
					</template>
				</view>

				<view>
					<view class="title">
						<text>{{ renderData('title') }}</text>
						<view class="audio-number">{{ renderData('current') }}/{{ renderData('duration') }}</view>
					</view>
					<view class="singer">{{ renderData('singer') }}</view>
				</view>
			</view>
		</template>

		<template v-if="theme == 'theme1'">
			<view class="audio-wrapper">
				<view class="audio-number" @click="emitChangeChapter(-1)">上一章</view>
				<slider
					class="audio-slider"
					:activeColor="themeColor"
					block-size="16"
					:value="renderData('current_value')"
					:max="renderData('duration_value')"
					@change="change"
					:disabled="!renderIsPlay"
				></slider>
				<view class="audio-number" @click="emitChangeChapter(1)">下一章</view>
			</view>
            <movable-area class="movable-area" @click="operate" v-if="renderData('paused')">
                <movable-view class="movable-view" :x="x" :y="y" direction="all">
                  <image  style="width: 96rpx;height: 96rpx;border-radius: 48rpx;margin-top:6rpx;margin-left:6rpx" :src="require('./static/listen.png')"></image>
            	   <!-- <image  style="width: 68rpx;height: 68rpx;border-radius: 34rpx;margin-bottom:16rpx;margin-left:24rpx" @click="operate" v-if="renderData('paused')" src="../../../static/start.png"></image>
				   <image  style="width: 68rpx;height: 68rpx;border-radius: 34rpx;margin-bottom:16rpx;margin-left:24rpx" @click="operate" v-else src="../../../static/stop.png"></image>
				  <image  style="width: 40rpx;height: 40rpx;border-radius: 20rpx;margin-bottom:30rpx;margin-left:24rpx" src="../../../static/close1.png"></image> -->
            	</movable-view>
            </movable-area>
		</template>
	</view>
</template>

<script>
export default {
	props: {
		theme: {
			type: String, // 主题 'theme1' or 'theme2'
			default: 'theme1'
		},
		themeColor: {
			type: String,
			default: '#42b983'
		},
		imgeSet:{
			type: String,
			default: 'https://lidoogroup.com/11.png'
		}
	},
	data() {
		return {
			playinfo: this.$zaudio.playinfo,
			audiolist: this.$zaudio.audiolist,
			paused: this.$zaudio.paused,
			renderIsPlay: this.$zaudio.renderIsPlay,
			audio: this.$zaudio.renderinfo,
			loading: this.$zaudio.loading,
			action: Symbol('zaudio'),
			x:10,
			y:350,
			isCollapsed: false,
		};
	},
	computed: {
		renderData() {
			return name => {
				if (!this.renderIsPlay) {
					if (name == 'paused') {
						return true;
					}
					
					return this.audio[name];
				} else {
					if (name == 'paused') {
						return this.paused;
					}
					
					return this.playinfo[name];
				}
			};
		}
	},

	mounted() {
		this.$nextTick(() => {	
			console.log("loadSuccess")
			let action = this.action;
			this.$zaudio.syncStateOn(action, ({ audiolist, paused, playinfo, renderIsPlay, renderinfo, loading }) => {
				this.audiolist = audiolist;
				this.paused = paused;
				this.playinfo = playinfo;
				this.renderIsPlay = renderIsPlay;
				this.audio = renderinfo;
				this.loading = loading;
			}); 
			 
 		});
	},
	methods: {
		//播放or暂停
		operate() {
			this.$zaudio.operate();
		},
		//进度拖到
		change(event) {
			if (this.renderIsPlay) {
				this.$zaudio.seek(event.detail.value);
			}
		},
		//快进
		stepPlay(value) {
			this.$zaudio.stepPlay(value);
		},
		//切歌
		changeplay(count) {
			this.$zaudio.changeplay(count);
		},
		emitChangeChapter(direction) {
					this.$emit('change-chapter', direction);
		},
	},
	beforeDestroy() {
		//组件卸载时卸载业务逻辑
		let action = this.action;
		this.$zaudio.syncStateOff(action)
	}
};
</script>

<style scoped lang="scss">
.movable-area {
	  height: 95vh;
	  width: 750rpx;
	  top: 0;
	  position: fixed;
	  pointer-events: none;
	  z-index: 9999;
	}
.movable-view {
	  width: 108rpx;
	  height: 108rpx;
	  pointer-events: auto;
	  border-radius: 55rpx;
}
.movable-off{
	  width: 108rpx;
	  height: 108rpx;
	  background-color: #6D5D43;
	  pointer-events: auto;
	  border-radius: 55rpx;
}
@import './index.scss';
//  #ifdef MP-WEIXIN
.theme3 .audio-slider {
	margin-top: -8px !important;
}
// #endif
</style>
