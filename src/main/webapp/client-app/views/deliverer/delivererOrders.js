Vue.component("deliverer-orders", {
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex" id="wrapper">
            <deliverer-sidebar></deliverer-sidebar>
            <orders typeOfUser="deliverer"></orders>
        </div>
    </div> `
});