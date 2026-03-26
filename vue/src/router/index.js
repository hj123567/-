import Layout from '../layout/Layout.vue'
import {createRouter, createWebHistory} from "vue-router";
import {ElMessage} from "element-plus";


export const constantRoutes = [
    {path: '/Login', name: 'Login', component: () => import("@/views/Login")},
    {
        path: '/Layout', name: 'Layout', component: Layout, children: [
            //
            {path: '/home', name: 'Home', component: () => import("@/views/Home")},
            {path: '/stuInfo', name: 'StuInfo', component: () => import("@/views/StuInfo")},
            {path: '/dormAssign', name: 'DormAssign', component: () => import("@/views/DormAssign")},
            {path: '/dormManagerInfo', name: 'DormManagerInfo', component: () => import("@/views/DormManagerInfo")},
            {path: '/buildingInfo', name: 'BuildingInfo', component: () => import("@/views/BuildingInfo")},
            {path: '/roomInfo', name: 'RoomInfo', component: () => import("@/views/RoomInfo")},
            {path: '/noticeInfo', name: 'NoticeInfo', component: () => import("@/views/NoticeInfo")},
            {path: '/adjustRoomInfo', name: 'AdjustRoomInfo', component: () => import("@/views/AdjustRoomInfo")},
            {path: '/dormAllocation', name: 'DormAllocation', component: () => import("@/views/DormAllocation")},
            {path: '/dormScoreManage', name: 'DormScoreManage', component: () => import("@/views/DormScoreManage")},
            {path: '/repairInfo', name: 'RepairInfo', component: () => import("@/views/RepairInfo")},
            {path: '/visitorInfo', name: 'VisitorInfo', component: () => import("@/views/VisitorInfo")},
            {path: '/utilityConfigManage', name: 'UtilityConfigManage', component: () => import("@/views/UtilityConfigManage")},
            {path: '/utilityMeterReading', name: 'UtilityMeterReading', component: () => import("@/views/UtilityMeterReading")},
            {path: '/utilityBillManage', name: 'UtilityBillManage', component: () => import("@/views/UtilityBillManage")},
            {path: '/myUtilityBill', name: 'MyUtilityBill', component: () => import("@/views/MyUtilityBill")},
            //
            {path: '/myRoomInfo', name: 'MyRoomInfo', component: () => import("@/views/MyRoomInfo")},
            {path: '/myDormScore', name: 'MyDormScore', component: () => import("@/views/MyDormScore")},
            {path: '/applyRepairInfo', name: 'ApplyRepairInfo', component: () => import("@/views/ApplyRepairInfo")},
            {path: '/applyChangeRoom', name: 'ApplyChangeRoom', component: () => import("@/views/ApplyChangeRoom")},

            {path: '/selfInfo', name: 'SelfInfo', component: () => import("@/views/SelfInfo")},
        ]
    },

]
const router = createRouter({
    routes: constantRoutes,
    history: createWebHistory(process.env.BASE_URL)
})
//路由守卫
router.beforeEach((to, from, next) => {
    //to 要访问的路径
    //from 代表从哪个路径跳转而来
    // next 是函数，表示放行
    // next() 放行
    // next('/*') 强制跳转
    const user = window.sessionStorage.getItem('user')
    const identity = window.sessionStorage.getItem('identity')
    
    if (to.path === '/Login') {
        return next();
    }
    if (!user) {
        return next('/Login')
    }
    if (to.path === '/' && user) {
        return next('/home')
    }
    
    // 系统管理员权限控制
    const adminForbiddenPaths = [
        '/dormAllocation',
        '/dormScoreManage',
        '/myRoomInfo',
        '/myDormScore',
        '/myUtilityBill',
        '/applyRepairInfo',
        '/applyChangeRoom',
        '/visitorInfo',
        '/utilityMeterReading',
        '/utilityBillManage'
    ]
    
    // 宿管权限控制
    const dormManagerForbiddenPaths = [
        '/dormManagerInfo',
        '/utilityConfigManage'
    ]
    
    if (identity && JSON.parse(identity) === 'admin' && adminForbiddenPaths.includes(to.path)) {
        ElMessage.warning('系统管理员无权限访问此页面');
        return next('/home');
    }
    
    if (identity && JSON.parse(identity) === 'dormManager' && dormManagerForbiddenPaths.includes(to.path)) {
        ElMessage.warning('宿舍管理员无权限访问此页面');
        return next('/home');
    }
    
    next()
})

export default router
