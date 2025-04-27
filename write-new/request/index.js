// 全局请求封装
// const base_url = 'https://query.asilu.com/api';
// const base_url = "http://" + window.location.host + "/api";
  // const base_url = "http://192.168.1.190:9022";
 //   const base_url = "http://112.124.20.89:9021";
   //const base_url = "http://112.124.20.89:9022"; // 测试
   const  base_url = "https://lidoogroup.com/huiyilu"  //正式
// 请求超出时间
const timeout = 10000;

// 需要修改token，和根据实际修改请求头
export default (params) => {
  //console.log(2222222, JSON.stringify(params))
  let url = params.url;
  let method = params.method || "get";
  let data = params.data || {};

  const token = uni.getStorageSync("token");

  //console.log(token,'token')
  // if (token) {
  //   // 将token添加到请求headers中
  //   config.headers.Authorization = `Bearer ${token}`;
  // }
  let header = null;
  if (token && url !== "/login") {
    header = {
      "content-type": "application/json;charset=UTF-8",
      "Access-Control-Allow-0rigin": "*",
      Authorization: `Bearer ${token}`,
      ...params.header,
    };
  } else {
    header = {
      "content-type": "application/json;charset=UTF-8",
      "Access-Control-Allow-0rigin": "*",
      ...params.header,
    };
  }

  // const cookie = uni.getStorageSync("cookie");
  // const isIos = uni.getSystemInfoSync().platform == 'ios'
  // console.log(999999,cookie,isIos)
  // if (cookie && isIos) {
  // 	// uni.showToast({
  // 	// 	title: 'cookie:'+ cookie.split(";")[0],
  // 	// 	duration: 2000
  // 	// })
  //     // 将token添加到请求headers中
  // 	header.cookie = cookie.split(";")[0];
  // }
  // if (method == "post" && url !=="/functionMenu/answer/addAll") {
  // 	header = {
  // 		// ...header,
  // 		'Content-Type': 'application/x-www-form-urlencoded',
  // 	};
  // }
  //console.log(header,99)
  return new Promise((resolve, reject) => {
    uni.request({
      url: base_url + url,
      method: method,
      header: header,
      data: data,
      success(response) {
        const res = response;
        // 根据返回的状态码做出对应的操作
        //获取成功
        //console.log("response", res);

        //如果鉴权失败跳转到登录页面
        if (response?.data?.code == 401) {
          //	console.log(1114)
          uni.navigateTo({
            url: "/pages/login/index",
          });
        } else {
          //console.log(res,999)
          resolve(res);
        }
      },
      fail(err) {
        //	console.log(err, 'err')
        //如果鉴权失败跳转到登录页面
        if (err?.data?.code == 401) {
        //  console.log(1114);
          uni.navigateTo({
            url: "/pages/login/index",
          });
        }
        //console.log("err", JSON.stringify(err))
        // dd.alert({content:'接口请求错误：'+JSON.stringify(err)})
        if (err?.errMsg?.indexOf("request:fail") !== -1) {
          uni.showToast({
            title: "网络异常",
            icon: "error",
            duration: 2000,
          });
        } else {
          uni.showToast({
            title: "未知异常",
            duration: 2000,
          });
        }
        reject(err);
      },
      // complete() {
      // 	// 不管成功还是失败都会执行
      // 	uni.hideLoading();
      // 	uni.hideToast();
      // }
    });
  }).catch(() => {});
};
