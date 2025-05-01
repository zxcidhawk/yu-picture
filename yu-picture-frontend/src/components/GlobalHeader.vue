<template>
  <div id="globalHeader">
    <a-row :wrap="false">
      <a-col flex="200px">
        <router-link to="/">
          <div class="title-bar">
            <img class="logo" src="../assets/logo.png" alt="logo" />
            <div class="title">鱼皮云图库</div>
          </div>
        </router-link>
      </a-col>
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="current"
          mode="horizontal"
          @click="doMenuClick"
          :items="menus"
        />
      </a-col>
      <a-col flex="120px">
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser.id">
            <a-dropdown>
              <a-space>
                <a-avatar :src="loginUserStore.loginUser.userAvatar" />
                {{ loginUserStore.loginUser.userName ?? '无名' }}
              </a-space>
              <template #overlay>
                <a-menu>
                  <a-menu-item @click="doLogout">
                    <LogoutOutlined />
                    退出登录
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
          <div v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>

      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import { computed, h, ref } from 'vue'
import { HomeOutlined } from '@ant-design/icons-vue'
import { type MenuProps, message } from 'ant-design-vue'
import { type RouteRecordRaw, useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import { userLogoutUsingPost } from '@/api/userController.ts'
import checkAccess from '@/access/checkAccess.ts'

const loginUserStore  = useLoginUserStore()

// 菜单列表
const originMenus = [
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/admin/userManage',
    label: '用户管理',
    title: '用户管理',
  },
  {
    key: 'others',
    label: h('a', { href: 'https://www.codefather.cn', target: '_blank' }, '编程导航'),
    title: '编程导航',
  },
  {
    key: '/about',
    label: '关于',
    title: '关于',
  }
]

const menuToRouteItem = (menu: any): RouteRecordRaw => {
  // 获取所有路由
  const routes = router.getRoutes()
  // 根据菜单的key查找对应的路由
  const route = routes.find((route) => route.path === menu.key)
  // 如果找到对应路由则返回，否则返回一个默认的空路由对象
  return route || ({} as RouteRecordRaw)
}

// 过滤菜单项
const filterMenus = (menus = [] as MenuProps['items']) => {
  // 过滤条件是一个回调函数 (menu) => { ... }, 返回 true 表示保留该菜单项，返回 false 表示过滤掉该菜单项
  return menus?.filter((menu) => {
    // 通过menu的key值找到对应的路由字段
    const item = menuToRouteItem(menu)

    // 如果是菜单项中没有对应的路由，就说明该菜单是自定义的，予以保留，返回true
    if (!item.path) {
      return true
    }

    // 如果有hideInMenu标记为true，则隐藏
    if (item.meta?.hideInMenu) {
      return false
    }

    // 根据权限过滤菜单，有权限则返回true，会保留该菜单
    return checkAccess(loginUserStore.loginUser, item.meta?.access as string)
  })
}

// 展示在菜单的路由数组
const menus = computed(() => {
  return filterMenus(originMenus)
})


const router = useRouter()
const current = ref<string[]>([''])

const doMenuClick = ({ key }) => {
  router.push({
    path: key,
  })
}

router.afterEach((to) => {
  current.value = [to.path]
})



// 用户注销
const doLogout = async () => {
  const res = await userLogoutUsingPost()
  console.log(res)
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({
      userName: '未登录',
    })
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败，' + res.data.message)
  }
}

</script>

<style scoped>
#globalHeader .title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: black;
  font-size: 18px;
  margin-left: 16px;
}

.logo {
  height: 48px;
}
</style>

