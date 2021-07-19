Vue.component("manager-sidebar", {
    data: function() {
        return {

        }
    },
    template: `
    <div class="bg-secondary border-right" id="sidebar-wrapper">
        <div class="sidebar-heading">Menu</div>
        <div class="list-group list-group-flush">
            <a href="#/manager" class="list-group-item list-group-item-action bg-secondary active">Profile</a>
            <a href="#/manager/changePassword" class="list-group-item list-group-item-action bg-secondary">Change password</a>
            <a href="#/admin/myReastaurant" class="list-group-item list-group-item-action bg-secondary">My restaurant</a>
            <a href="#/admin/orders" class="list-group-item list-group-item-action bg-secondary">Orders</a>
        </div>
    </div> `
});