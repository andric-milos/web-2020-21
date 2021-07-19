Vue.component("admin-info", {
    data: function() {
        return {
            
        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex" id="wrapper">
            <admin-sidebar></admin-sidebar>
            <user-info></user-info>
        </div>
    </div> `,
    methods: {
        
    }
});