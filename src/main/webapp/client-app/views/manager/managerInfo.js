Vue.component("manager-info", {
    data: function() {
        return {
            
        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex" id="wrapper">
            <manager-sidebar></manager-sidebar>
            <user-info></user-info>
        </div>
    </div> `,
    methods: {
        
    }
});