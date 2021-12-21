Vue.component("deliverer-sidebar", {
    data: function() {
        return {
            profileActive: undefined,
            changePasswordActive: undefined,
            ordersActive: undefined
        }
    },
    template: `
    <div class="bg-secondary border-right" id="sidebar-wrapper">
        <div class="sidebar-heading">Menu</div>
        <div class="list-group list-group-flush">
            <a href="#/deliverer" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: profileActive }">Profile</a>
            <a href="#/deliverer/changePassword" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: changePasswordActive }">Change password</a>
            <a href="#/deliverer/orders" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: ordersActive }">Orders</a>
        </div>
    </div> `,
    created() {
        if (window.location == "http://localhost:8080/web-2020-21/#/deliverer/changePassword") {
            this.profileActive = false;
            this.changePasswordActive = true;
            this.ordersActive = false;
        } else if (window.location == "http://localhost:8080/web-2020-21/#/deliverer/orders") {
            this.profileActive = false;
            this.changePasswordActive = false;
            this.ordersActive = true;
        } else { // location == "http://localhost:8080/web-2020-21/#/deliverer"
            this.profileActive = true;
            this.changePasswordActive = false;
            this.ordersActive = false;
        }
    }
});