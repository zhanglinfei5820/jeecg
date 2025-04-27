module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'https://120.27.215.56:9021/', // 你的目标服务器地址
        changeOrigin: true,            // 改变请求的源
        pathRewrite: {                 // 重写路径
          '^/api': ''                  // 替换掉 '/api'
        }
      }
    }
  }
};