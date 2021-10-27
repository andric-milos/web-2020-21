Vue.component('home-page', {
    data: function() {
        return {
            restaurants: []
        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>
        
        <div class="d-flex p-2 justify-content-center">
            <div class="d-flex flex-column">
                <div class="d-flex flex-row border border-dark rounded m-2" v-for="r in restaurants">
                    <img 
                        v-bind:src="'http://localhost:8080/web-2020-21/images/restaurant-logos/' + r.naziv + '.jpg'" 
                        class="border-end border-dark rounded p-2" 
                        style="max-width:300px; max-height:300px; width:300px; height:300px;" />
                    <div class="d-flex flex-column p-2">
                        <h1><b>{{ r.naziv }}</b></h1>
                        <h4><b>Address:</b> {{ r.lokacija.adresa.ulica }} {{ r.lokacija.adresa.broj }}, {{ r.lokacija.adresa.mesto }} {{ r.lokacija.adresa.postanskiBroj }} </h4>
                        <h4><b>Type:</b> {{ r.tip }} </h4>
                    </div>
                </div>
            </div>
        </div>
    </div> `,
    methods: {

    },
    mounted() {
        axios.get("rest/restaurant")
            .then(response => {
                if (response.status == 200) {
                    this.restaurants = response.data;
                } else {
                    console.log(response);
                }
            })
            .catch(error => {
                console.log(error);
            });
    }
});