import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/dashboard'
        },
        {
            path: '/',
            component: resolve => require(['../components/common/Home.vue'], resolve),
            meta: { title: '自述文件' },
            children:[
                {
                    path: '/dashboard',
                    component: resolve => require(['../components/page/Dashboard.vue'], resolve),
                    meta: { title: '仪表盘' }
                },
                {
                    path: '/request',
                    component: resolve => require(['../components/page/Request.vue'], resolve),
                    meta: { title: '请求查询' }
                },
                {
                    path: '/method',
                    component: resolve => require(['../components/page/Method.vue'], resolve),
                    meta: { title: '方法查询' }
                },
                {
                    path: '/logger',
                    component: resolve => require(['../components/page/Logger.vue'], resolve),
                    meta: { title: 'Logger查询' }
                },
                {
                    path: '/table',
                    component: resolve => require(['../components/page/BaseTable.vue'], resolve),
                    meta: { title: '基础表格' }
                },
                {
                    path: '/tabs',
                    component: resolve => require(['../components/page/Tabs.vue'], resolve),
                    meta: { title: 'tab选项卡' }
                },
                {
                    // 应用列表
                    path: '/appList',
                    component: resolve => require(['../components/page/AppList.vue'], resolve),
                    meta: { title: '应用列表' }
                },
                {
                    // 权限页面
                    path: '/permission',
                    component: resolve => require(['../components/page/Permission.vue'], resolve),
                    meta: { title: '权限测试', permission: true }
                },
                {
                    path: '/404',
                    component: resolve => require(['../components/page/404.vue'], resolve),
                    meta: { title: '404' }
                },
                {
                    path: '/403',
                    component: resolve => require(['../components/page/403.vue'], resolve),
                    meta: { title: '403' }
                },
                {
                    path: '/sql',
                    component: resolve => require(['../components/page/Sql.vue'], resolve),
                    meta: { title: 'SQL查询' }
                },
                {
                    path: '/tx',
                    component: resolve => require(['../components/page/Tx.vue'], resolve),
                    meta: { title: '事务查询' }
                }
            ]
        },
        {
            path: '/login',
            component: resolve => require(['../components/page/Login.vue'], resolve)
        },
        {
            path: '*',
            redirect: '/404'
        }
    ]
})
