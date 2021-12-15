Vue.component("customer-sidebar", {
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
            <a href="#/customer" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: profileActive }">Profile</a>
            <a href="#/customer/changePassword" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: changePasswordActive }">Change password</a>
            <a href="#/customer/orders" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: ordersActive }">My orders</a>
        </div>
    </div> `,
    created() {
        if (window.location == "http://localhost:8080/web-2020-21/#/customer/changePassword") {
            this.profileActive = false;
            this.changePasswordActive = true;
            this.ordersActive = false;
        } else if (window.location == "http://localhost:8080/web-2020-21/#/customer/orders") {
            this.profileActive = false;
            this.changePasswordActive = false;
            this.ordersActive = true;
        } else { // location == "http://localhost:8080/web-2020-21/#/customer"
            this.profileActive = true;
            this.changePasswordActive = false;
            this.ordersActive = false;
        }
    }
});