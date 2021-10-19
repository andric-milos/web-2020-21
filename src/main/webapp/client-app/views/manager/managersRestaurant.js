Vue.component("managers-restaurant", {
    data: function() {
        return {
            
        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex" id="wrapper">
            <manager-sidebar></manager-sidebar>
            <restaurant-info></restaurant-info>
        </div>
    </div> `,
    methods: {
        
    }
});