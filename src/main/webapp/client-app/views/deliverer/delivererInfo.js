Vue.component("deliverer-info", {
    data: function() {
        return {
            
        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex" id="wrapper">
            <deliverer-sidebar></deliverer-sidebar>
            <user-info></user-info>
        </div>
    </div> `,
    methods: {
        
    }
});