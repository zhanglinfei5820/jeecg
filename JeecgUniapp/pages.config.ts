import { defineUniPages } from '@uni-helper/vite-plugin-uni-pages'

export default defineUniPages({
  globalStyle: {
    navigationStyle: 'default',
    navigationBarTitleText: 'uniapp',
    navigationBarBackgroundColor: '#f8f8f8',
    navigationBarTextStyle: 'black',
    backgroundColor: '#FFFFFF',
  },
  easycom: {
    autoscan: true,
    custom: {
      '^wd-(.*)': 'wot-design-uni/components/wd-$1/wd-$1.vue',
      '^(?!z-paging-refresh|z-paging-load-more)z-paging(.*)':
        'z-paging/components/z-paging$1/z-paging$1.vue',
      '^echarts': '@echarts/echarts.vue',
      '^echarts/components': '@echarts/components.vue',
      '^echarts/charts': '@echarts/charts.vue',
      '^echarts/core': '@echarts/core.vue',
      '^echarts/features': '@echarts/features.vue',
      '^echarts/guage': '@echarts/guage.vue',
      '^echarts/map': '@echarts/map.vue',
      

    },
  },
  tabBar: {
    color: '#aaa',
    selectedColor: '#39b54a',
    backgroundColor: '#F8F8F8',
    borderStyle: 'black',
    height: '50px',
    fontSize: '11px',
    iconWidth: '24px',
    spacing: '3px',
    list: [
      {
        iconPath: 'static/tabbar/tabbar-message-2.png',
        selectedIconPath: 'static/tabbar/tabbar-message.png',
        pagePath: 'pages/index/index',
        text: '首页',
      },
      {
        iconPath: 'static/tabbar/tabbar-home-2.png',
        selectedIconPath: 'static/tabbar/tabbar-home.png',
        pagePath: 'pages/statistics/index',
        text: '统计',
      },
	  {
	    iconPath: 'static/tabbar/tabbar-workHome-2.png',
	    selectedIconPath: 'static/tabbar/tabbar-workHome.png',
	    pagePath: 'pages/vehicle/vehicle',
	    text: '车辆管理',
	  },
      {
        iconPath: 'static/tabbar/tabbar-workHome-2.png',
        selectedIconPath: 'static/tabbar/tabbar-workHome.png',
        pagePath: 'pages/ad/adInspection',
        text: '广告维护',
      },
      {
        iconPath: 'static/tabbar/tabbar-user-2.png',
        selectedIconPath: 'static/tabbar/tabbar-user.png',
        pagePath: 'pages/user/people',
        text: '个人',
      },
    ],
  },
})
