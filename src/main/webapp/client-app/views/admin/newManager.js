Vue.component("admin-new-manager", {
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex" id="wrapper">
            <admin-sidebar></admin-sidebar>
            <new-user title="manager"></new-user>
        </div>
    </div>`
});