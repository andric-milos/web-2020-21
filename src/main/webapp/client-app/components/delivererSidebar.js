Vue.component("deliverer-sidebar", {
    data: function() {
        return {

        }
    },
    template: `
    <div class="bg-secondary border-right" id="sidebar-wrapper">
        <div class="sidebar-heading">Menu</div>
        <div class="list-group list-group-flush">
            <a href="#/deliverer" class="list-group-item list-group-item-action bg-secondary active">Profile</a>
            <a href="#/deliverer/changePassword" class="list-group-item list-group-item-action bg-secondary">Change password</a>
            <a href="#/deliverer/orders" class="list-group-item list-group-item-action bg-secondary">Orders</a>
        </div>
    </div> `
});