Vue.component("customer-orders", {
    data: function() {
        return {

        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex" id="wrapper">
            <customer-sidebar></customer-sidebar>
            <orders></orders>
        </div>
    </div> `
});