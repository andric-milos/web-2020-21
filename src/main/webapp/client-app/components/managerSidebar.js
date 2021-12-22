Vue.component("manager-sidebar", {
    data: function() {
        return {
            profileActive: undefined,
            changePasswordActive: undefined,
            myRestaurantActive: undefined,
            ordersActive: undefined,
            requestsActive: undefined
        }
    },
    template: `
    <div class="bg-secondary border-right" id="sidebar-wrapper">
        <div class="sidebar-heading">Menu</div>
        <div class="list-group list-group-flush">
            <a href="#/manager" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: profileActive }">Profile</a>
            <a href="#/manager/changePassword" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: changePasswordActive }">Change password</a>
            <a href="#/manager/myReastaurant" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: myRestaurantActive }">My restaurant</a>
            <a href="#/manager/orders" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: ordersActive }">Orders</a>
            <a href="#/manager/requests" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: requestsActive }">Delivery requests</a>
        </div>
    </div> `,
    created() {
        if (window.location == "http://localhost:8080/web-2020-21/#/manager/changePassword") {
            this.profileActive = false;
            this.changePasswordActive = true;
            this.myRestaurantActive = false;
            this.ordersActive = false;
            this.requestsActive = false;
        } else if (window.location == "http://localhost:8080/web-2020-21/#/manager/myReastaurant") {
            this.profileActive = false;
            this.changePasswordActive = false;
            this.myRestaurantActive = true;
            this.ordersActive = false;
            this.requestsActive = false;
        } else if (window.location == "http://localhost:8080/web-2020-21/#/manager/orders") {
            this.profileActive = false;
            this.changePasswordActive = false;
            this.myRestaurantActive = false;
            this.ordersActive = true;
            this.requestsActive = false;
        } else if (window.location == "http://localhost:8080/web-2020-21/#/manager/requests") {
            this.profileActive = false;
            this.changePasswordActive = false;
            this.myRestaurantActive = false;
            this.ordersActive = false;
            this.requestsActive = true;
        } else { // location == "http://localhost:8080/web-2020-21/#/manager"
            this.profileActive = true;
            this.changePasswordActive = false;
            this.myRestaurantActive = false;
            this.ordersActive = false;
            this.requestsActive = false;
        }
    }
});