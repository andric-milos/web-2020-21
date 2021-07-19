Vue.component("kupac-sidebar", {
    data: function() {
        return {

        }
    },
    template: `
    <div class="bg-secondary border-right" id="sidebar-wrapper">
        <div class="sidebar-heading">Menu</div>
        <div class="list-group list-group-flush">
            <a href="#/kupac" class="list-group-item list-group-item-action bg-secondary active">Profile</a>
            <a href="#/kupac/changePassword" class="list-group-item list-group-item-action bg-secondary">Change password</a>
        </div>
    </div> `
});