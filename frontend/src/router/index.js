import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/chat/Login'
import AdminLogin from '../views/admin/AdminLogin'
import Register from '../views/chat/Register'
import ChatRoom from '../views/chat/ChatRoom'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login,
    hidden: true
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    hidden: true
  },
  {
    path: '/adminlogin',
    name: 'AdminLogin',
    component: AdminLogin,
    hidden: true
  },
  {
    path: '/chatroom',
    name: 'ChatRoom',
    component: ChatRoom,
    hidden: true
  }
]
//解决重复访问路由地址报错
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
};

const router = new VueRouter({
  routes
})

export default router
