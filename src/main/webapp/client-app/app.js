const HomePage = { template: '<home-page></home-page>' }
const LoginPage = { template: '<login-page></login-page>' }
const RegisterPage = { template: '<registration-page></registration-page>' }

const CustomerInfo = { template: '<customer-info></customer-info>' }
const CustomerChangePasswordPage = { template: '<customer-change-password></customer-change-password>' }

const AdminInfo = { template: '<admin-info></admin-info>' }
const ListOfUsers = { template: '<admin-list-of-users></admin-list-of-users>' }
const AdminChangePasswordPage = { template: '<admin-change-password></admin-change-password>' }
const AddNewManager = { template: '<admin-new-manager></admin-new-manager>' }
const AddNewDeliverer = { template: '<admin-new-deliverer></admin-new-deliverer>' }

const ManagerInfo = { template: '<manager-info></manager-info>' }
const ManagerChangePasswordPage = { template: '<manager-change-password></manager-change-password>' }

const DelivererInfo = { template: '<deliverer-info></deliverer-info>' }
const DelivererChangePasswordPage = { template: '<deliverer-change-password></deliverer-change-password>' }


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
            path: '/register',
            component: RegisterPage
        },
        {
            path: '/customer',
            component: CustomerInfo
        },
        {
            path: '/customer/changePassword',
            component: CustomerChangePasswordPage
        },
        {
            path: '/admin',
            component: AdminInfo
        },
        {
            path: '/admin/users',
            component: ListOfUsers
        },
        {
            path: '/admin/changePassword',
            component: AdminChangePasswordPage
        },
        {
            path: '/admin/newManager',
            component: AddNewManager
        },
        {
            path: '/admin/newDeliverer',
            component: AddNewDeliverer
        },
        {
            path: '/manager',
            component: ManagerInfo
        },
        {
            path: '/manager/changePassword',
            component: ManagerChangePasswordPage
        },
        {
            path: '/deliverer',
            component: DelivererInfo
        },
        {
            path: '/deliverer/changePassword',
            component: DelivererChangePasswordPage
        }
    ]
});

var app = new Vue({
    router,
    el: '#indexRouter'
});