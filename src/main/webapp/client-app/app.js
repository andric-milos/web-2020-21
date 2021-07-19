const HomePage = { template: '<home-page></home-page>' }
const LoginPage = { template: '<login-page></login-page>' }
const KupacInfo = { template: '<kupac-info></kupac-info>' }
const AdminInfo = { template: '<admin-info></admin-info>' }
const ManagerInfo = { template: '<manager-info></manager-info>' }
const DelivererInfo = { template: '<deliverer-info></deliverer-info>' }

const router = new VueRouter({
    mode: 'hash',
    routes: [
        {
            path: '/',
            component: HomePage
        },
        {
            path: '/login',
            component: LoginPage
        },
        {
            path: '/kupac',
            component: KupacInfo
        },
        {
            path: '/admin',
            component: AdminInfo
        },
        {
            path: '/manager',
            component: ManagerInfo
        },
        {
            path: '/deliverer',
            component: DelivererInfo
        }
    ]
});

var app = new Vue({
    router,
    el: '#indexRouter'
});