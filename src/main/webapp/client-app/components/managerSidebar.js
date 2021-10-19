Vue.component("manager-sidebar", {
    data: function() {
        return {
            profileActive: undefined,
            changePasswordActive: undefined,
            myRestaurantActive: undefined
        }
    },
    template: `
    <div class="bg-secondary border-right" id="sidebar-wrapper">
        <div class="sidebar-heading">Menu</div>
        <div class="list-group list-group-flush">
            <a href="#/manager" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: profileActive }">Profile</a>
            <a href="#/manager/changePassword" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: changePasswordActive }">Change password</a>
            <a href="#/manager/myReastaurant" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: myRestaurantActive }">My restaurant</a>
            <a href="#/manager/orders" class="list-group-item list-group-item-action bg-secondary">Orders</a>
        </div>
    </div> `,
    created() {
        if (window.location == "http://localhost:8080/web-2020-21/#/manager/changePassword") {
            this.profileActive = false;
            this.changePasswordActive = true;
            this.myRestaurantActive = false;
        } else if (window.location == "http://localhost:8080/web-2020-21/#/manager/myReastaurant") {
            this.profileActive = false;
            this.changePasswordActive = false;
            this.myRestaurantActive = true;
        } else { // location == "http://localhost:8080/web-2020-21/#/manager"
            this.profileActive = true;
            this.changePasswordActive = false;
            this.myRestaurantActive = false;
        }
    }
});