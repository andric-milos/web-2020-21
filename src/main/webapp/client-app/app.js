const HomePage = { template: '<home-page></home-page>' }
const LoginPage = { template: '<login-page></login-page>' }
const RegisterPage = { template: '<registration-page></registration-page>' }

const CustomerInfo = { template: '<customer-info></customer-info>' }

const AdminInfo = { template: '<admin-info></admin-info>' }
const ListOfUsers = { template: '<admin-list-of-users></admin-list-of-users>' }

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
            path: '/customer',
            component: CustomerInfo
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
        },
        {
            path: '/register',
            component: RegisterPage
        },
        {
            path: '/admin/users',
            component: ListOfUsers
        }
    ]
});

var app = new Vue({
    router,
    el: '#indexRouter'
});