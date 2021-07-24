Vue.component("admin-sidebar", {
    data: function() {
        return {
            profileActive: false,
            changePasswordActive: false,
            usersActive: false,
            newRestaurantActive: false,
            newManagerActive: false,
            newDelivererActive: false
        }
    },
    template: `
    <div class="bg-secondary border-right" id="sidebar-wrapper">
        <div class="sidebar-heading">Menu</div>
        <div class="list-group list-group-flush">
            <a href="#/admin" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: profileActive }">Profile</a>
            <a href="#/admin/changePassword" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: changePasswordActive }">Change password</a>
            <a href="#/admin/users" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: usersActive }">List of all users</a>
            <a href="#/admin/newRestaurant" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: newRestaurantActive }">Add new restaurant</a>
            <a href="#/admin/newManager" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: newManagerActive }">Add new manager</a>
            <a href="#/admin/newDeliverer" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: newDelivererActive }">Add new deliverer</a>
        </div>
    </div> `,
    created() {
        if (window.location == "http://localhost:8080/web-2020-21/#/admin/changePassword") {
            this.profileActive = false;
            this.changePasswordActive = true;
            this.usersActive = false;
            this.newRestaurantActive = false;
            this.newManagerActive = false;
            this.newDelivererActive = false;
        } else if (window.location == "http://localhost:8080/web-2020-21/#/admin/users") {
            this.profileActive = false;
            this.changePasswordActive = false;
            this.usersActive = true;
            this.newRestaurantActive = false;
            this.newManagerActive = false;
            this.newDelivererActive = false;
        } else if (window.location == "http://localhost:8080/web-2020-21/#/admin/newRestaurant") {
            this.profileActive = false;
            this.changePasswordActive = false;
            this.usersActive = false;
            this.newRestaurantActive = true;
            this.newManagerActive = false;
            this.newDelivererActive = false;
        } else if (window.location == "http://localhost:8080/web-2020-21/#/admin/newManager") {
            this.profileActive = false;
            this.changePasswordActive = false;
            this.usersActive = false;
            this.newRestaurantActive = false;
            this.newManagerActive = true;
            this.newDelivererActive = false;
        } else if (window.location == "http://localhost:8080/web-2020-21/#/admin/newDeliverer") {
            this.profileActive = false;
            this.changePasswordActive = false;
            this.usersActive = false;
            this.newRestaurantActive = false;
            this.newManagerActive = false;
            this.newDelivererActive = true;
        } else { // location == "http://localhost:8080/web-2020-21/#/admin"
            this.profileActive = true;
            this.changePasswordActive = false;
            this.usersActive = false;
            this.newRestaurantActive = false;
            this.newManagerActive = false;
            this.newDelivererActive = false;
        }
    }
});