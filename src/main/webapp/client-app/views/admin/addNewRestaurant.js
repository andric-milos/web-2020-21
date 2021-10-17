Vue.component("admin-new-restaurant", {
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex" id="wrapper">
            <admin-sidebar></admin-sidebar>
            <new-restaurant></new-restaurant>
        </div>
    </div> `
});