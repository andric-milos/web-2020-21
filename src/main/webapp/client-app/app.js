const HomePage = { template: '<home-page></home-page>' }
const LoginPage = { template: '<login-page></login-page>' }
const KupacInfo = { template: '<kupac-info></kupac-info>' }

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
        }
    ]
});

var app = new Vue({
    router,
    el: '#indexRouter'
});