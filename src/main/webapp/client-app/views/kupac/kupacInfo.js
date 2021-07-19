Vue.component("kupac-info", {
    data: function() {
        return {
            
        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex" id="wrapper">
            <kupac-sidebar></kupac-sidebar>
            <user-info></user-info>
        </div>
    </div> `,
    methods: {
        
    }
});