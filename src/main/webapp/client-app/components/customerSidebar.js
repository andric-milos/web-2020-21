Vue.component("customer-sidebar", {
    data: function() {
        return {
            profileActive: undefined,
            changePasswordActive: undefined
        }
    },
    template: `
    <div class="bg-secondary border-right" id="sidebar-wrapper">
        <div class="sidebar-heading">Menu</div>
        <div class="list-group list-group-flush">
            <a href="#/customer" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: profileActive }">Profile</a>
            <a href="#/customer/changePassword" class="list-group-item list-group-item-action bg-secondary" v-bind:class="{ active: changePasswordActive }">Change password</a>
        </div>
    </div> `,
    created() {
        if (window.location == "http://localhost:8080/web-2020-21/#/customer/changePassword") {
            this.profileActive = false;
            this.changePasswordActive = true;
        } else { // location == "http://localhost:8080/web-2020-21/#/customer"
            this.profileActive = true;
            this.changePasswordActive = false;
        }
    }
});