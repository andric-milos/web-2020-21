const HomePage = { template: '<home-page></home-page>' }

const router = new VueRouter({
    mode: 'hash',
    routes: [
        {
            path: '/',
            component: HomePage
        }
    ]
});

var app = new Vue({
    router,
    el: '#indexRouter'
});