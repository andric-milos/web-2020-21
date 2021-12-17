Vue.component("manager-orders", {
    data: function() {
        return {

        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex" id="wrapper">
            <manager-sidebar></manager-sidebar>
            <orders typeOfUser="manager"></orders>
        </div>
    </div> `
});