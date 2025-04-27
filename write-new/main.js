import App from './App'
import store from './store';
// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
import request from './request/index';
Vue.config.productionTip = false
App.mpType = 'app'
Vue.prototype.$request = request;
import ZAudio from '@/uni_modules/uni-radio/uniapp-zaudio/index.js'

let zaudio = new ZAudio({
  continuePlay: true, //续播
  autoPlay: true, //自动播放 部分浏览器不支持
});
Vue.prototype.$zaudio = zaudio; //挂载vue原型链上
const app = new Vue({
  ...App,
   store
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif