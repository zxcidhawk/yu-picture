import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import UserLoginPage from '@/pages/user/UserLoginPage.vue'
import UserRegisterPage from '@/pages/user/UserRegisterPage.vue'
import UserManagePage from '@/pages/admin/UserManagePage.vue'
import ACCESS_ENUM from '@/access/accessEnum.ts'
import HomePage from '@/pages/HomePage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomePage,
      meta: {
        hideInMenu: false,
      },
    },
    {
      path: '/user/login',
      name: 'userLogin',
      component: UserLoginPage,
      meta: {
        hideInMenu: true,
      },
    },
    {
      path: '/user/register',
      name: 'userRegister',
      component: UserRegisterPage,
      meta: {
        hideInMenu: true,
      },
    },
    {
      path: '/admin/userManage',
      name: 'adminUserManage',
      component: UserManagePage,
      meta: {
        access: ACCESS_ENUM.ADMIN,
      },
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue'),
      meta: {
        hideInMenu: false,
      },
    },
  ],
})

export default router
