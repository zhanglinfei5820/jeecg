/**
 * 路由拦截，通常也是登录拦截
 * 可以设置路由白名单，或者黑名单，看业务需要选哪一个
 * 我这里应为大部分都可以随便进入，所以使用黑名单
 */
import { useUserStore } from '@/store'
import { needLoginPages as _needLoginPages, getNeedLoginPages } from '@/utils'

// TODO Check
const loginRoute = '/pages/login/login'

// 白名单路由，不需要登录就可以访问
const whiteList = ['/pages/login/login', '/pages/index/index']

const isLogined = () => {
  const userStore = useUserStore()
  return userStore.isLogined
}

const isDev = import.meta.env.DEV

// 黑名单登录拦截器 - （适用于大部分页面不需要登录，少部分页面需要登录）
const navigateToInterceptor = {
  // 注意，这里的url是 '/' 开头的，如 '/pages/index/index'，跟 'pages.json' 里面的 path 不同
  invoke({ url }: { url: string }) {
    // console.log(url) // /pages/route-interceptor/index?name=feige&age=30
    const path = url.split('?')[0]
    
    // 白名单直接放行
    if (whiteList.includes(path)) {
      return true
    }
    
    // 检查登录状态
    const hasLogin = isLogined()
    if (hasLogin) {
      return true
    }

    // 未登录且不在白名单中，跳转到登录页
    const redirectRoute = `${loginRoute}?redirect=${encodeURIComponent(url)}`
    uni.navigateTo({ url: redirectRoute })
    return false
  },
}

export const routeInterceptor = {
  install() {
    uni.addInterceptor('navigateTo', navigateToInterceptor)
    uni.addInterceptor('reLaunch', navigateToInterceptor)
    uni.addInterceptor('redirectTo', navigateToInterceptor)
    uni.addInterceptor('switchTab', navigateToInterceptor)
  },
}
