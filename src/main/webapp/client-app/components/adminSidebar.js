Vue.component("admin-sidebar", {
    data: function() {
        return {

        }
    },
    template: `
    <div class="bg-secondary border-right" id="sidebar-wrapper">
        <div class="sidebar-heading">Menu</div>
        <div class="list-group list-group-flush">
            <a href="#/admin" class="list-group-item list-group-item-action bg-secondary active">Profile</a>
            <a href="#/admin/changePassword" class="list-group-item list-group-item-action bg-secondary">Change password</a>
            <a href="#/admin/users" class="list-group-item list-group-item-action bg-secondary">List of all users</a>
            <a href="#/admin/newRestaurant" class="list-group-item list-group-item-action bg-secondary">Add new restaurant</a>
            <a href="#/admin/newManager" class="list-group-item list-group-item-action bg-secondary">Add new manager</a>
            <a href="#/admin/newDeliverer" class="list-group-item list-group-item-action bg-secondary">Add new deliverer</a>
        </div>
    </div> `
});