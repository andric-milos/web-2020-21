Vue.component("customer-info", {
    data: function() {
        return {
            
        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex" id="wrapper">
            <customer-sidebar></customer-sidebar>
            <user-info></user-info>
        </div>
    </div> `,
    methods: {
        
    }
});