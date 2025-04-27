// utils/tabBarUtil.js

//户主tab
export const  huzhu = [
      {
        "pagePath": "/pages/home/index",
        "iconPath": "/static/home.jpg",
        "selectedIconPath": "/static/active-home.jpg",
        "iconWidth": "20px",
        "text": "首页",
		"index":"0"
      },
	  {
	    "pagePath": "/pages/memory/index",
	    "iconPath": "/static/read.jpg",
	    "selectedIconPath": "/static/active-read.jpg",
	    "iconWidth": "20px",
	    "text": "我的阅读",
	  		"index":"4"
	  },
      {
        "pagePath": "/pages/my/index",
        "iconPath": "/static/my.jpg",
        "selectedIconPath": "/static/active-my.jpg",
        "iconWidth": "20px",
        "text": "我的",
		"index":"3"
      }
    ]
	
	//operation
	export const operation = [
      {
        "pagePath": "/pages/home/index",
       "iconPath": "/static/home.jpg",
       "selectedIconPath": "/static/active-home.jpg",
        "iconWidth": "20px",
        "text": "首页",
		"index":"0"
      },
	  {
	    "pagePath": "/pages/memory/index",
	    "iconPath": "/static/home.jpg",
	    "selectedIconPath": "/static/active-home.jpg",
	    "iconWidth": "20px",
	    "text": "课程",
	  		"index":"4"
	  },
      {
        "pagePath": "/pages/index/index",
        "iconPath": "/static/process.jpg",
        "selectedIconPath": "/static/active-process.jpg",
        "iconWidth": "20px",
        "text": "任务进度",
		"index":"1"
      },
      {
        "pagePath": "/pages/message/index",
        "iconPath": "/static/message.jpg",
        "selectedIconPath": "/static/active-message.jpg",
        "iconWidth": "20px",
        "text": "预约信息",
		"index":"2"
      },
      {
        "pagePath": "/pages/my/index",
       "iconPath": "/static/my.jpg",
       "selectedIconPath": "/static/active-my.jpg",
        "iconWidth": "20px",
        "text": "我的",
		"index":"3"
      }
    ]
export const tabBar ={operation,huzhu}









