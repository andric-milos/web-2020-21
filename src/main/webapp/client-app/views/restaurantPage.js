Vue.component("restaurant-page", {
    data: function() {
        return {
            showRestaurant: undefined,
            restaurant: undefined,
            showArticles: undefined
        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div v-if="showRestaurant" class="d-flex flex-row justify-content-center">
            <div class="d-flex flex-column p-2">
                <h1 class="p-2 text-center"><b>{{ this.restaurant.naziv }}</b></h1>

                <div class="d-flex flex-row justify-content-center">
                    <img 
                        id="restaurantLogo" 
                        v-bind:src="'http://localhost:8080/web-2020-21/images/restaurant-logos/' + this.restaurant.naziv + '.jpg'"
                        class="border rounded mr-1" 
                        style="height: auto; width: auto; min-width: 400px; min-height: 400px; max-width: 400px; max-height: 400px;"
                    />
                    <div class="container" id="map" ref="mapElement" style="height: auto; width: auto; min-width: 400px; min-height: 400px; max-width: 400px; max-height: 400px; margin-left: 0px; margin-right: 0px;"></div>
                </div>

                <h1 class="p-2"><b>Articles</b></h1>

                <div v-if="showArticles" class="d-flex flex-row flex-wrap">
                    <div class="card my-1" style="width: 17rem;" v-for="a in restaurant.artikli">
                        <img class="card-img-top" v-bind:src="'http://localhost:8080/web-2020-21/images/article-images/' + a.restoran + '-' + a.naziv + '.jpg'" alt="Card image cap">
                        <div class="card-body">
                            <h5 class="card-title">{{ a.naziv }}</h5>
                            <p class="card-text">{{ a.opis }}</p>
                            <div class="d-flex flex-row justify-content-between">
                                <p>{{ a.kolicina }} <span v-if="a.tip =='JELO'">g</span><span v-if="a.tip =='PICE'">ml</span></p>
                                <p>{{ a.cena }} RSD</p>
                            </div>
                            <button type="button" class="btn btn-secondary">Add to cart</button>
                        </div>
                    </div>
                </div>
                <div v-else>
                    <h5 class="p-2 ml-2">No articles.</h5>
                </div> 
            </div>
        </div> 
        <div v-else class="d-flex flex-row justify-content-center">
            <h1> Restaurant with name <span class="text-primary"><b>{{ this.$route.params.restaurantName }}</b></span> doesn't exist. </h1>
        </div>
    </div> `,
    methods: {
        initializeMap() {
            //console.log(document.getElementById('map'));

            var map = L.map(this.$refs.mapElement).setView([this.restaurant.lokacija.geografskaSirina, this.restaurant.lokacija.geografskaDuzina], 13);

            L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
                attribution: '',
                maxZoom: 18,
                id: 'mapbox/streets-v11',
                tileSize: 512,
                zoomOffset: -1,
                accessToken: 'pk.eyJ1IjoibWVlbG9zY2giLCJhIjoiY2trcTRkNzl3MGZ6djJvcW4zeHFxeHg0YyJ9.h06Ayx7JxZqxszi6nLpsZw'
            }).addTo(map);

            var marker = L.marker([this.restaurant.lokacija.geografskaSirina, this.restaurant.lokacija.geografskaDuzina], {draggable: false});
            marker.addTo(map);

            /*setTimeout(() => {
                map.invalidateSize();           
            }, 0);*/
        },
        loadData() {
            //console.log(this.$route.params.restaurantName);

            axios.get("rest/restaurant/" + this.$route.params.restaurantName)
                .then(response => {
                    if (response.status == 200) {
                        this.restaurant = response.data;
                        this.showRestaurant = true;

                        if (this.restaurant.artikli == []) {
                            this.showArticles = false;
                        } else {
                            this.showArticles = true;
                        }
                    }
                })
                .catch(error => {
                    console.log(error);
                    this.showRestaurant = false;
                    this.showArticles = false;
                });
        }
    },
    mounted() {
        this.loadData();
    },
    updated() {
        this.$nextTick(() => {
            this.initializeMap();
        });
    }
});