Vue.component("manager-delivery-requests", {
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex" id="wrapper">
            <manager-sidebar></manager-sidebar>
            <requests></requests>
        </div>
    </div> `
});