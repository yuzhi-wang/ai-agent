import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import LoveChat from '../views/LoveChat.vue'
import ManusChat from '../views/ManusChat.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: Home },
    { path: '/love', name: 'love', component: LoveChat },
    { path: '/manus', name: 'manus', component: ManusChat },
  ],
})

export default router



