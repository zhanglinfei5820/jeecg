<!-- 使用 type="home" 属性设置首页，其他页面不需要设置，默认为page；推荐使用json5，更强大，且允许注释 -->
<route lang="json5" type="home">
{
  layout: 'default',
  style: {
    navigationStyle: 'custom',
    navigationBarTitleText: '首页',
  },
}
</route>
<template>
  <view class="page-container">
    <view class="banner">
      <swiper class="swiper" circular :indicator-dots="true" :autoplay="true" :interval="3000" :duration="1000">
        <swiper-item v-for="(item, index) in bannerList" :key="index">
          <image :src="item.image" mode="aspectFill" class="banner-image" />
        </swiper-item>
      </swiper>
    </view>
    
    <view class="content">
      <view class="title">欢迎使用网约车广告小程序</view>
      <view class="description">
        这是一个专业的网约车广告投放平台，为您的车辆提供全方位的广告展示服务。
        我们提供精准的广告投放、实时数据统计、高效的管理系统，让您的广告投放更加简单高效。
      </view>
    </view>
  </view>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { TestEnum } from '@/typings'
import { us, os } from '@/common/work'
// 获取当前运行平台
import PLATFORM from '@/utils/platform'
import { cache, getFileAccessHttpUrl, hasRoute } from '@/common/uitls'
import { onLaunch, onShow, onHide, onLoad, onReady } from '@dcloudio/uni-app'
import { useToast, useMessage, useNotify } from 'wot-design-uni'
import { useRouter } from '@/plugin/uni-mini-router'
import Grid from '@/components/Grid/Grid.vue'

import {
  ACCESS_TOKEN,
  USER_NAME,
  USER_INFO,
  APP_ROUTE,
  APP_CONFIG,
  HOME_CONFIG_EXPIRED_TIME,
} from '@/common/constants'
import { http } from '@/utils/http'

defineOptions({
  name: 'index',
  options: {
    // apply-shared‌：当前页面样式会影响到子组件样式.(小程序)
    // shared‌：当前页面样式影响到子组件，子组件样式也会影响到当前页面.(小程序)
    styleIsolation: 'shared',
  },
})
const toast = useToast()
const router = useRouter()
// 获取屏幕边界到安全区域距离
const { safeAreaInsets } = uni.getSystemInfoSync()
const isLocalConfig = getApp().globalData.isLocalConfig
const carouselList = ref([])
const swiperList = ref([])
const middleApps = ref([])
const usList = ref([])
const osList = ref([])
const msgCount = ref(0)
const dot = ref({ mailHome: false })
const goPage = (item) => {
  let page = item.routeIndex
  console.log('-----------page------------', page)
  if (!page) {
    toast.info('该功能暂未实现')
  } else {
    if (['other', 'common'].includes(page)) {
      goPageMore(page)
      return
    }
    if (page === 'annotationList') {
      msgCount.value = 0
    }
    dot.value[page] = false
    if (page.indexOf('/app/online') == 0) {
      let code = page.substring(page.lastIndexOf('/') + 1)
      let real = { desformCode: code, desformName: item.title }
      uni.navigateTo({
        url: '/pages/check/onlineForm/add?item=' + encodeURIComponent(JSON.stringify(real)),
      })
    } else if (page.indexOf('/app/desform') == 0) {
      let code = page.substring(page.lastIndexOf('/') + 1)
      let real = { desformCode: code, desformName: item.title }
      uni.navigateTo({
        url: '/pages/check/designForm/designForm?item=' + encodeURIComponent(JSON.stringify(real)),
      })
    } else {
      if (!hasRoute({ name: page })) {
        router.replace({ name: 'demo', params: { backRouteName: 'index' } })
      } else {
        router.replace({ name: page, params: { backRouteName: 'index' } })
      }
    }
  }
}
const getAppConfigRoute = () => {
  //判断是否过期
  let config = cache(APP_CONFIG)
  if (config) {
    homeConfig()
  } else {
    //更新首页配置
    http.get('/eoa/sysAppConfig/queryAppConfigRoute').then((res: any) => {
      console.log('更新首页配置res', res)
      let result = res
      if (result.success) {
        cache(APP_ROUTE, result.result.route, HOME_CONFIG_EXPIRED_TIME)
        cache(APP_CONFIG, result.result.config, HOME_CONFIG_EXPIRED_TIME)
        homeConfig()
      }
    })
  }
}
const homeConfig = () => {
  var indexRouteList = cache(APP_ROUTE)
  var appConfig = cache(APP_CONFIG)
  usList.value = indexRouteList.filter((item) => item.type == 'common')
  osList.value = indexRouteList.filter((item) => item.type == 'other')
  middleApps.value = indexRouteList.filter((item) => item.type == 'approve')
  let carouselImgStr = appConfig[0].carouselImgJson
  var carouselImgArr = carouselImgStr && carouselImgStr.length > 0 ? carouselImgStr.split(',') : []
  carouselList.value = carouselImgArr
}
const goPageMore = (page) => {
  // router.replace({ path: `/pages/more/more`, query: { type: page } })
  router.replace({ name: 'more', params: { backRouteName: 'index', type: page } })
}
onLoad(() => {
  console.log('index页面：onLoad')
})
onReady(() => {
  console.log('index页面：onReady')
})

if (isLocalConfig) {
  usList.value = us.data.map((item) => ({
    ...item,
    text: item.title,
    img: item.icon,
    itemKey: item.routeIndex,
  }))
  osList.value = os.data.map((item) => ({
    ...item,
    text: item.title,
    img: item.icon,
    itemKey: item.routeIndex,
  }))
  usList.value.push({
    text: '更多',
    img: '/static/index/128/more.png',
    routeIndex: 'other',
    itemKey: 'other',
  })
  osList.value.push({
    text: '更多',
    img: '/static/index/128/more.png',
    routeIndex: 'common',
    itemKey: 'common',
  })
  middleApps.value = [
    {
      icon: 'https://static.jeecg.com/upload/test/line2_icon1_1595818065964.png',
      title: '审批',
      subTitle: '个人审批',
      routeIndex: 'paper',
    },
    {
      icon: 'https://static.jeecg.com/upload/test/line2_icon2_1595818070168.png',
      title: '审批稿',
      subTitle: '审批草稿箱',
      routeIndex: 'draft',
    },
  ]
  swiperList.value = [
    {
      id: 1,
      type: 'image',
      url: 'https://static.jeecg.com/upload/test/banner0_1595850438042.jpeg',
      link: '',
    },
    {
      id: 2,
      type: 'image',
      url: 'https://static.jeecg.com/upload/test/banner2_1595818081327.jpg',
      link: '',
    },
    {
      id: 3,
      type: 'image',
      url: 'https://static.jeecg.com/upload/test/oabanner-2_1595648520760.png',
      link: '',
    },
    {
      id: 4,
      type: 'image',
      url: 'https://static.jeecg.com/upload/test/banner5_1595818089013.jpeg',
      link: '',
    },
  ]
} else {
  getAppConfigRoute()
}

const bannerList = ref([
  {
    image: 'https://picsum.photos/800/400?random=1',
  },
  {
    image: 'https://picsum.photos/800/400?random=2',
  },
  {
    image: 'https://picsum.photos/800/400?random=3',
  }
])
</script>

<style lang="scss" scoped>
.page-container {
  min-height: calc(100vh - 50px); /* 减去tabBar的高度 */
  background-color: #f5f5f5;
  padding-bottom: 50px; /* 为tabBar留出空间 */
}

.banner {
  width: 100%;
  height: 400rpx;
  
  .swiper {
    width: 100%;
    height: 100%;
  }
  
  .banner-image {
    width: 100%;
    height: 100%;
  }
}

.content {
  padding: 30rpx;
  
  .title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
  }
  
  .description {
    font-size: 28rpx;
    color: #666;
    line-height: 1.6;
  }
}
</style>
