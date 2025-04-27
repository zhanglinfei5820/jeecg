// src/store/index.js
import Vue from 'vue';
import Vuex from 'vuex';
import {tabBar} from '../utils/tabBar.js' 
Vue.use(Vuex);

const state = {
  // 初始化状态
  userInfo: JSON.parse(uni.getStorageSync('userInfo') || "{}"),
  userInfos: JSON.parse(uni.getStorageSync('userInfos') || "{}"),
  token: uni.getStorageSync('token') || null,
  huzhuInfos:JSON.parse(uni.getStorageSync("huzhuInfos") || "{}"),
  tabBarList: [],
};

const mutations = {
  SET_USER(state, userInfos) {
     state.userInfos = userInfos;
	 uni.setStorageSync('userInfos', JSON.stringify(userInfos));
    
	if(userInfos.role[0]){
		state.tabBarList = tabBar[userInfos.role];
		
	    uni.setStorageSync('tabBarList', tabBar[userInfos.role]) //
	}else{
		//默认户主
		state.tabBarList =  tabBar["huzhu"];
		
		uni.setStorageSync('tabBarList', tabBar["huzhu"])
	}
  },
  SET_HUZHU(state, huzhuInfos){
	  state.huzhuInfos = huzhuInfos;
	  uni.setStorageSync('huzhuInfos', JSON.stringify(huzhuInfos));
  },
  SET_TOKEN(state, token) {
    state.token = token;
    uni.setStorageSync('token', token);
  }
};

const actions = {
  setUser({ commit }, userInfos) {
	//  console.log(userInfos,999)
    commit('SET_USER', userInfos);
  },
  setHuzhu({ commit }, huzhuInfos) {
    commit('SET_HUZHU', huzhuInfos);
  },
  setToken({ commit }, token) {
    commit('SET_TOKEN', token);
  }
};

const getters = {
  getUser: state => state.user,
  getToken: state => state.token
};

export default new Vuex.Store({
  state,
  mutations,
  actions,
  getters
});