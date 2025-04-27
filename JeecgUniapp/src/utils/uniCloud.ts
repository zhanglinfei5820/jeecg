export function initUniCloudConfig() {
  try {
    // @ts-ignore
    if (!uni.uniCloud) {
      // @ts-ignore
      uni.uniCloud = {
        uniIdRouter: {
          loginPage: '/pages/login/index',
          needLogin: [
            /^\/pages\/index/,
            /^\/pages\/work/,
            /^\/pages\/user/,
          ],
          resToLogin: true
        }
      };
    } else {
      // @ts-ignore
      uni.uniCloud.uniIdRouter = {
        loginPage: '/pages/login/index',
        needLogin: [
          /^\/pages\/index/,
          /^\/pages\/work/,
          /^\/pages\/user/,
        ],
        resToLogin: true
      };
    }
  } catch (e) {
    console.warn('uniCloud initialization warning:', e);
  }
} 