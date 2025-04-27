<route lang="json5" type="page">
  {
    style: {
      navigationStyle: 'custom',
      navigationBarTitleText: '',
    },
  }
  </route>
  
  <template>
    <PageLayout :navbarShow="false">
      <view class="page-container">
        <view class="text-center">
          <image src="/static/images/icon-login-logo.png" mode="aspectFit" class="logo"></image>
          <view class="title">出行传媒</view>
          <view class="enter-area">
            <view class="account-login-area">
              <view class="box account">
                <image src="/static/images/icon-login-user.png" class="input-icon"></image>
                <input class="uni-input" placeholder="请输入用户名" placeholder-class="input-placeholder" v-model.trim="userName" />
              </view>
              <view class="box password">
                <image src="/static/images/icon-login-password.png" class="input-icon"></image>
                <input
                  class="uni-input"
                  placeholder="请输入密码"
                  placeholder-class="input-placeholder"
                  password
                  v-model.trim="password"
                />
              </view>
            </view>
          </view>
          <view class="agreement-area">
            <view class="checkbox-wrapper" @click="toggleAgreement">
              <view class="custom-checkbox" :class="{ 'checked': isAgree }">
                <view v-if="isAgree" class="check-icon"></view>
              </view>
            </view>
            <text class="agreement-text">登录代表您同意</text>
            <text class="link-text" @click="showServiceTerms">《服务条款》</text>
            <text class="agreement-text">和</text>
            <text class="link-text" @click="showPrivacyPolicy">《隐私策略》</text>
          </view>
          <view class="btn-area">
            <button class="login-btn" :disabled="!isAgree" @click="hanldeLogin">
              {{ loading ? '登录中...' : '登录' }}
            </button>
          </view>
          <view class="bottom-tip">
            如未开通账号，请联系管理员
          </view>
        </view>
        <wd-notify />
      </view>

      <!-- 服务条款弹窗 -->
      <wd-popup v-model="showServiceTermsPopup" :close-on-click-modal="false">
        <view class="popup-content">
          <view class="popup-title">服务条款</view>
          <scroll-view class="popup-scroll" scroll-y>
            <view class="terms-content">
              <view class="terms-title">一、服务条款的确认和接纳</view>
              <view class="terms-text">本应用的各项内容和服务的所有权归本公司拥有。用户在接受本服务之前，请务必仔细阅读本条款。用户使用服务的行为将视为对本条款的接受，并同意接受本条款的各项内容的约束。</view>
              <view class="terms-title">二、服务内容及使用须知</view>
              <view class="terms-text">1. 用户在使用本服务时，必须遵守中华人民共和国相关法律法规的规定，不得利用本服务从事违法违规行为。</view>
              <view class="terms-text">2. 用户须对自己在使用本服务过程中的行为承担法律责任。</view>
              <view class="terms-text">3. 用户不得利用本服务制作、上传、复制、发布、传播或者转载如下内容：
                · 反对宪法所确定的基本原则的；
                · 危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；
                · 损害国家荣誉和利益的；
                · 煽动民族仇恨、民族歧视，破坏民族团结的；
                · 破坏国家宗教政策，宣扬邪教和封建迷信的。</view>
            </view>
          </scroll-view>
          <button class="popup-btn" @click="closeServiceTerms">关闭</button>
        </view>
      </wd-popup>

      <!-- 隐私政策弹窗 -->
      <wd-popup v-model="showPrivacyPolicyPopup" :close-on-click-modal="false">
        <view class="popup-content">
          <view class="popup-title">隐私政策</view>
          <scroll-view class="popup-scroll" scroll-y>
            <view class="terms-content">
              <view class="terms-title">一、信息收集</view>
              <view class="terms-text">我们收集信息是为了向您提供更好更优的服务，我们收集信息的途径有：
                1. 您向我们提供的信息；
                2. 在您使用我们的服务时获取的信息；
                3. 我们从第三方获取的您的信息。</view>
              <view class="terms-title">二、信息使用</view>
              <view class="terms-text">我们可能将收集的信息用于下列用途：
                1. 向您提供服务；
                2. 帮助我们设计新服务，改善现有服务；
                3. 使我们更加了解您如何接入和使用我们的服务；
                4. 用于身份验证、客户服务、安全防范、诈骗监测、存档和备份用途；
                5. 保障我们服务的安全性。</view>
              <view class="terms-title">三、信息保护</view>
              <view class="terms-text">我们使用各种安全技术和程序，以防信息的丢失、不当使用、未经授权阅览或披露。例如，在某些服务中，我们将利用加密技术（例如SSL）来保护您提供的个人信息。</view>
            </view>
          </scroll-view>
          <button class="popup-btn" @click="closePrivacyPolicy">关闭</button>
        </view>
      </wd-popup>
    </PageLayout>
  </template>
  
  <script lang="ts" setup>
  import { ref } from 'vue'
  import { useNotify, useToast } from 'wot-design-uni'
  import { useUserStore } from '@/store/user'
  import { http } from '@/utils/http'
  import {
    ACCESS_TOKEN,
    USER_NAME,
    USER_INFO,
    APP_ROUTE,
    APP_CONFIG,
    HOME_CONFIG_EXPIRED_TIME,
    HOME_PAGE,
  } from '@/common/constants'
  import { cache, getFileAccessHttpUrl } from '@/common/uitls'
  import { useRouter } from '@/plugin/uni-mini-router'
  import { useParamsStore } from '@/store/page-params'
  
  const isAgree = ref(false)
  const toast = useToast()
  const loading = ref(false)
  const userName = ref('')
  const password = ref('')
  const router = useRouter()
  const userStore = useUserStore()
  const showServiceTermsPopup = ref(false)
  const showPrivacyPolicyPopup = ref(false)
  
  const toggleAgreement = () => {
    isAgree.value = !isAgree.value
  }
  
  const showServiceTerms = () => {
    showServiceTermsPopup.value = true
  }
  
  const closeServiceTerms = () => {
    showServiceTermsPopup.value = false
  }
  
  const showPrivacyPolicy = () => {
    showPrivacyPolicyPopup.value = true
  }
  
  const closePrivacyPolicy = () => {
    showPrivacyPolicyPopup.value = false
  }
  
  const hanldeLogin = () => {
    if (!isAgree.value) {
      toast.warning('请先同意服务条款和隐私策略')
      return
    }
    
    if (userName.value.length === 0) {
      toast.warning('请输入用户名')
      return
    }
    if (password.value.length === 0) {
      toast.warning('请输入密码')
      return
    }
    loading.value = true
    http
      .post('/sys/mLogin', { username: userName.value, password: password.value })
      .then((res: any) => {
        if (res.success) {
          const { result } = res
          const userInfo = result.userInfo
          userStore.setUserInfo({
            ...userInfo,
            token: result.token,
            userid: userInfo.id,
            username: userInfo.username,
            realname: userInfo.realname,
            avatar: userInfo.avatar,
            tenantId: userInfo.loginTenantId,
            localStorageTime: +new Date(),
          })
          router.pushTab({ path: HOME_PAGE })
        } else {
          toast.warning(res.message)
        }
      })
      .finally(() => {
        loading.value = false
      })
  }
  
  if (import.meta.env.MODE === 'development') {
    userName.value = 'admin'
    password.value = '123456'
  }
  
  if (import.meta.env.MODE === 'production') {
    userName.value = 'jeecg'
    password.value = 'jeecg#123456'
  }
  </script>
  
  <style lang="scss" scoped>
  .page-container {
    padding: 0 40rpx;
    padding-top: 120rpx;
    position: relative;
    font-size: 28rpx;
    background-color: #f5f5f5;
    min-height: 100vh;
    
    .logo {
      width: 180rpx;
      height: 180rpx;
      margin-bottom: 30rpx;
    }
    
    .title {
      font-size: 48rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 100rpx;
      letter-spacing: 2rpx;
    }
    
    .enter-area {
      width: 100%;
      margin: 0 auto 40rpx;
      
      .box {
        display: flex;
        align-items: center;
        height: 100rpx;
        background-color: #fff;
        border-radius: 8rpx;
        padding: 0 30rpx;
        margin-bottom: 30rpx;
        
        .input-icon {
          width: 40rpx;
          height: 40rpx;
          margin-right: 20rpx;
        }
        
        .uni-input {
          flex: 1;
          font-size: 32rpx;
          color: #333;
          text-align: left;
        }
      }
    }
    
    .agreement-area {
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 40rpx;
      font-size: 24rpx;
      
      .checkbox-wrapper {
        margin-right: 8rpx;
        padding: 10rpx;
      }
      
      .custom-checkbox {
        width: 32rpx;
        height: 32rpx;
        border: 2rpx solid #CCCCCC;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        
        &.checked {
          background-color: #F29600;
          border-color: #F29600;
        }
        
        .check-icon {
          width: 16rpx;
          height: 8rpx;
          border: 2rpx solid #fff;
          border-top: none;
          border-right: none;
          transform: rotate(-45deg);
          margin-top: -2rpx;
        }
      }
      
      .agreement-text {
        color: #666;
      }
      
      .link-text {
        color: #79BFF2;
      }
    }
    
    .btn-area {
      padding: 0 20rpx;
      
      .login-btn {
        width: 100%;
        height: 88rpx;
        line-height: 88rpx;
        background: #1B3C91;
        color: #fff;
        font-size: 32rpx;
        border-radius: 8rpx;
        
        &[disabled] {
          opacity: 0.6;
        }
      }
    }
    
    .bottom-tip {
      margin-top: 40rpx;
      font-size: 24rpx;
      color: #999;
      text-align: center;
    }
  }
  
  .popup-content {
    background: #fff;
    border-radius: 16rpx;
    width: 600rpx;
    padding: 30rpx;
    
    .popup-title {
      font-size: 36rpx;
      font-weight: 500;
      text-align: center;
      margin-bottom: 30rpx;
    }
    
    .popup-scroll {
      height: 600rpx;
      padding: 0 20rpx;
    }
    
    .terms-content {
      .terms-title {
        font-size: 32rpx;
        font-weight: 500;
        margin-bottom: 20rpx;
        color: #333;
      }
      
      .terms-text {
        font-size: 28rpx;
        color: #666;
        line-height: 1.6;
        margin-bottom: 30rpx;
        white-space: pre-line;
      }
    }
    
    .popup-btn {
      margin-top: 30rpx;
      background: #1B3C91;
      color: #fff;
      border-radius: 8rpx;
      font-size: 32rpx;
    }
  }
  
  .input-placeholder {
    color: #999;
    font-size: 32rpx;
    text-align: left;
  }
  </style>
  