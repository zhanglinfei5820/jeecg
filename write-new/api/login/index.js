import request from '../../request/index.js';
 //第一次登录
  export  async function firstLogin(code) {
	 const res = await  request({
		   url: `/weixin/wxgetall`,
		   method: 'get',
		   data: {
		     code,
		     count:"-1"
		   }
	   })
	   return res
   }
   //二次登录
   export  async function secondLogin(code,phoneNum) {
   	 const res = await  request({
   		   url: `/weixin/wxgetall`,
   		   method: 'get',
   		   data: {
   		     code,
			 phonecode:phoneNum,
   		     count:"2"
   		   }
   	   })
   	   return res
    }
	
    //获取微信登录code
   export async function getCode() {
			  return new Promise((resolve, reject) => {
			    uni.login({
			      provider: 'weixin',
			      success: (res) => {
			        if (res.errMsg === 'login:ok') {
			          resolve(res.code);
			        } else {
						  resolve("");
			        //  reject(new Error('获取code失败'));
			        }
			      },
			      fail: (err) => {
					  resolve("");
			       // reject(new Error('获取code失败'));
			      }
			    });
			  });
			}
   
 