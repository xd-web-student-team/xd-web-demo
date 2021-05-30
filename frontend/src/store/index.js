import Vue from 'vue'
import Vuex from 'vuex'

import SockJS from '../utils/sockjs'
import '../utils/stomp'
import { getRequest } from "../utils/api";
import { Notification } from 'element-ui';

Vue.use(Vuex)

const now = new Date();

const store = new Vuex.Store({
  state: sessionStorage.getItem('state') ? JSON.parse(sessionStorage.getItem('state')) : {
    routes: [],
    sessions: {},//聊天记录
    users: [],//用户列表
    currentUser: null,//当前登录用户
    currentSession: { username: '小明', nickname: '小明' },//当前选中的用户，默认为群聊
    currentList: '聊天窗口',//当前聊天窗口列表
    filterKey: '',
    stomp: null,
    isDot: {},//两用户之间是否有未读信息
    errorImgUrl: "@/assets/errorImg.jpg",//错误提示图片
    shotHistory: {}//拍一拍的记录历史
  },
  mutations: {
    initRoutes(state, data) {
      state.routes = data;
    },
    changeCurrentSession(state, currentSession) {
      //切换到当前用户就标识消息已读
      Vue.set(state.isDot, state.currentUser.username + "#" + currentSession.username, false);
      //更新当前选中的用户
      state.currentSession = currentSession;
    },
    //修改当前聊天窗口列表
    changeCurrentList(state, currentList) {
      state.currentList = currentList;
    },
    //保存系统所有用户
    INIT_USER(state, data) {
      state.users = data;
    },
    //请求并保存所有系统用户
    GET_USERS(state) {
      getRequest("/chat/users").then(resp => {
        if (resp) {
          state.users = resp;
        }
      })
    }
  },
  actions: {
    /**
     * 作用：初始化数据
     * action函数接受一个与store实例具有相同方法和属性的context对象
     * @param context
     */
    initData(context) {
      //初始化聊天记录
      context.commit('INIT_DATA')
      //获取用户列表
      context.commit('GET_USERS')
    },
  },
  modules: {
  }
})

store.watch(function (state) {
  return state.sessions
}, function (val) {
  console.log('CHANGE: ', val);
  localStorage.setItem('chat-session', JSON.stringify(val));
}, {
  deep: true/*这个貌似是开启watch监测的判断,官方说明也比较模糊*/
})

export default store;