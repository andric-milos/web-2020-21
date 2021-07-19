Vue.component("customer-sidebar", {
    data: function() {
        return {

        }
    },
    template: `
    <div class="bg-secondary border-right" id="sidebar-wrapper">
        <div class="sidebar-heading">Menu</div>
        <div class="list-group list-group-flush">
            <a href="#/customer" class="list-group-item list-group-item-action bg-secondary active">Profile</a>
            <a href="#/customer/changePassword" class="list-group-item list-group-item-action bg-secondary">Change password</a>
        </div>
    </div> `
});