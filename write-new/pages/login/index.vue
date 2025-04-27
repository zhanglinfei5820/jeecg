<template>
	<view class="container">

		<image class="background" src="../../static/loginBg.png"></image>
		<view class="company">

			<view class="logo">

				<image class="company-icon" src="../../static/loginIcon.png" mode=""></image>

			</view>

			<view class="info">

				<view class="title">你诉说我采写</view>

				<view class="name">往事总在回忆时赋予意义～</view>

			</view>

			 <view class="" v-if="radioChecked">
			 	<button class="login-button"  open-type="getPhoneNumber"
			 		@getphonenumber="handleGetPhoneNumber">手机号一键登录</button>   
			 </view>
			 <view class="" v-else="radioChecked">
			 	<button class="login-button"  @click="getSQ"
			 		>手机号一键登录</button>   
			 </view>
				

		</view>

		<view class="agreement">

			<view class="radio">

				<radio value="radio" @click=" this.radioChecked = !this.radioChecked" :checked="radioChecked"
					color="rgba(22, 119, 255, 1)" style="transform:scale(0.7)">
				</radio>
			</view>
			<view>已阅读并同意</view>
			<view  @click="userAgreement" style="color:rgba(22, 119, 255, 1);">《用户信息采集告知书》</view>

			<view>与</view>

			<view @click="privacyAgreement"  style="color:rgba(22, 119, 255, 1);">《隐私政策》</view>

		</view>
		<uni-popup ref="popupPrivacy" type="bottom" border-radius="10px 10px 0 0" background-color="#fff">
					
					<view class="privacy-container">
						<rich-text :nodes="privacy.active"></rich-text>
					</view>
				</uni-popup>
	</view>
		
	</view>
	
</template>


<script>
import request from '../../request/index'

export default {
  data() {
    return {
      radioChecked: false,
      phoneNum: "",
      isFirst: false,
	  privacy:{
		  active:"",
		  user:"",
		  yinsi:""
	  }
    }
  },
  onLoad() {
	  // this.login()
	  // return false
	  this.getPrivacy()
    // let radioChecked = uni.getStorageSync("radioChecked")
    // if (radioChecked) {
    //   this.radioChecked = true;
    // }
    // let userInfos = uni.getStorageSync('userInfos');
    // if (userInfos) {
    //   const {phoneNumber } = JSON.parse(userInfos)
    //   //this.password = password;
    //   this.phoneNum = phoneNumber;
    //   this.getWechatPayParamsSecond(this.phoneNum)
    // } else {
    //   this.isFirst = true
    // }
  },
  methods: {
    async handleLogin(code, phoneNum, count, openid) {
      try {
        const res = await request({
          url: `/weixin/wxgetall`,
          method: 'get',
          data: {
            code,
            phonecode: phoneNum,
            count,
            // openid
          }
        })
        if (res?.data?.code == 200) {
          const token = res?.data?.token;
          this.$store.dispatch("setToken", token);
          const userInfos = {
            password: this.password,
            username: res?.data?.userinfo?.phonenumber,
            nickName: res?.data?.userinfo?.nickName,
            phoneNumber: res?.data?.userinfo?.phonenumber,
            uid: res?.data?.userinfo?.userId,
            role: res?.data?.userinfo?.rolename,
			openid:""
            //openid: res?.data?.userinfo?.openid
          }
          this.$store.dispatch("setUser", userInfos);
          uni.setStorageSync("radioChecked", true)
          if (userInfos.role == "operation" || userInfos.role == "huzhu") {
			   // 获取之前保存的页面路径和参数
			          const redirectInfo = uni.getStorageSync('redirectInfo');
			          if (redirectInfo) {
			              uni.reLaunch({
			                  url: `/${redirectInfo.route}?${Object.keys(redirectInfo.options).map(key => `${key}=${redirectInfo.options[key]}`).join('&')}`
			              });
			              // 清除存储的状态
			              uni.removeStorageSync('redirectInfo');
			          } else {
			              // 如果没有保存的页面路径，则跳转到首页或其他默认页面
			              uni.reLaunch({
			                  url: '/pages/home/index'
			              });
			          }
			  
			  
            // uni.switchTab({ url: '/pages/home/index' })
          } else {
            uni.showToast({ title: "您暂不是客户" })
			this.isFirst=true;
          }
        } else {
          uni.showToast({ title: "您暂不是客户" })
		  this.isFirst=true;
        }
      } catch (err) {
        uni.showToast({ title: '请求失败', icon: 'none' });
		this.isFirst=true;
      }
    },
    async getWechatPayParams(code, code1) {
      if (!this.radioChecked) {
        uni.showModal({ content: "请阅读并同意《用户信息采集告知书》与《隐私政策》" })
        return
      }
      await this.handleLogin(code1, code, "1")
    },
    async getWechatPayParamsSecond(phoneNum) {
      if (!this.radioChecked) {
        uni.showModal({ content: "请阅读并同意《用户信息采集告知书》与《隐私政策》" })
        return
      }
      const code = await this.getCode();
      await this.handleLogin(code, phoneNum, "2")
    },
    async handleGetPhoneNumber(e) {
      if (!this.radioChecked) {
        uni.showModal({ content: "请阅读并同意《用户信息采集告知书》与《隐私政策》" })
        return
      }
      if (e.detail.errMsg === 'getPhoneNumber:ok') {
        const { encryptedData, iv, code } = e.detail;
        const code1 = await this.getCode();
        this.getWechatPayParams(code, code1);
      } else {
        uni.showToast({ title: '用户拒绝授权', icon: 'none' });
      }
    },
    async getCode() {
      return new Promise((resolve, reject) => {
        uni.login({
          provider: 'weixin',
          success: (res) => {
            if (res.errMsg === 'login:ok') {
              resolve(res.code);
            } else {
              reject(new Error('获取code失败'));
            }
          },
          fail: (err) => {
            reject(new Error('获取code失败'));
          }
        });
      });
    },
	getSQ(){
		if (!this.radioChecked) {
		  uni.showModal({ content: "请阅读并同意《用户信息采集告知书》与《隐私政策》" })
		  }
	},
	//获取隐私协议
	getPrivacy(){
		this.$request({
			url: "/functionMenu/poster/list",
			method: "get",
			data: {
				module: "协议"
			}
		}).then((res)=>{
			//console.log(res,111)
			if(res?.data?.rows.length > 0){
				let  data = res?.data?.rows;
				data.map((item)=>{
					if(item.module == "隐私协议"){
						this.privacy.yinsi = item.content
					}
					else if(item.module == "用户协议"){
						this.privacy.user = item.content
					}
				})
			}
		})
	},
	privacyAgreement(){
		
	  this.privacy.active = this.privacy.yinsi;
	  this.$refs.popupPrivacy.open()
	},
	userAgreement(){
	  this.privacy.active = this.privacy.user;
	  this.$refs.popupPrivacy.open()
	},
    login() {
      request({
        url: "/login",
        method: "post",
        data: {
          password: "123456",
          username: "18755710562",
          type: "weixin"
        }
      }).then((res) => {
        if (res?.data?.code == 200) {
          const token = res?.data?.token;
          this.$store.dispatch("setToken", token);
          const userInfos = {
            
            username: this.phoneNum,
            nickName: res?.data?.userinfo?.nickName,
            phoneNumber: res?.data?.userinfo?.phonenumber,
            uid: res?.data?.userinfo?.userId,
            role: res?.data?.userinfo?.rolename,
          }
          this.$store.dispatch("setUser", userInfos);
          uni.setStorageSync("radioChecked", true)
          if (userInfos.role == "operation" || userInfos.role == "huzhu") {
            uni.switchTab({ url: '/pages/home/index' })
          } else {
            uni.showToast({ title: "您暂不是客户" })
          }
        } else {
          uni.showToast({ title: "您暂不是客户" })
        }
      }).catch(() => {
        uni.showToast({ title: "登录失败" })
		
      })
    }
  }
}
</script>
<style lang="scss" scoped>
	.container {

		width: 100%;

		height: 100vh;

		position: absolute;



		.background {

			width: 100%;

			height: calc(100vh - 13%);

		}



		.company {

			position: absolute;

			top: 0;

			left: 0;

			width: 100%;

			box-sizing: border-box;

			padding: 0 84rpx;

			display: flex;

			align-items: center;

			flex-direction: column;

			height: 100%;

			padding-top: 26vh;

		}



		.logo {

			width: 108rpx;

			height: 180rpx;

			display: flex;

			align-items: center;

			justify-content: center;

			/* margin-bottom: 26rpx; */

		}



		.logo image {

			width: 108rpx;

			height: 180rpx;

			object-fit: cover;

		}



		.info {

			width: 100%;

			height: 176rpx;

			display: flex;

			align-items: center;

			padding: 8rpx 4rpx;

			flex-direction: column;

			font-size: 48rpx;

			font-family: PingFang SC;

			font-weight: 500;

			color: #333333;

		}



		.title {

			height: 68rpx;

			margin-bottom: 12rpx;

		}



		.name {

			height: 40rpx;

			font-size: 32rpx;

			font-weight: 400;

			color: rgba(0, 0, 0, 0.65);

		}



		.login-button {

			/* position: absolute;

  bottom: 8%;

  left: 50%;

  transform: translateX(-50%); */

			width: 460rpx;

			display: flex;

			align-items: center;

			justify-content: center;

			height: 96rpx;

			background: #FA8C16;

			border-radius: 16rpx;

			color: #ffffff;

			font-family: Roboto;

			font-size: 32rpx;

			font-weight: 400;



		}



		.tip {

			position: absolute;

			bottom: 64rpx;

			left: 47rpx;

			width: 656rpx;

			display: flex;

			flex-direction: column;

			font-family: PingFang SC;

			font-size: 32rpx;

			font-weight: 400;

			text-align: center;

			color: #00000073;

		}
.privacy-container{
	 padding: 24rpx;
	  max-height: 80vh; /* 设置最大高度，可以根据需要调整 */
	  overflow-y: auto; /* 溢出时显示滚动条 */
}
		.tel {
			color: #148EFF;
		}
		.agreement {
			position: absolute;
			display: flex;
			align-items: center;
			justify-content: center;
			height: 40rpx;
			font-family: PingFang SC;
			font-size: 28rpx;
			font-weight: 400;
			color: rgba(153, 153, 153, 1);
			box-sizing: border-box;
			bottom: 200rpx;
			left: 80rpx;
            flex-wrap: wrap;
		}



		.radio {

			display: flex;

		}



		.box {

			padding: 20upx;

			border-bottom: 1upx solid #eee;

		}

	}



	.phone {



		position: absolute;



		bottom: 8%;



		left: 50%;



		transform: translateX(-50%);



		width: 70%;



		display: flex;



		align-items: center;



		justify-content: center;



		height: 80rpx;



		background-color: #4CD964;



		border-radius: 10rpx;



		color: #ffffff;



	}
</style>