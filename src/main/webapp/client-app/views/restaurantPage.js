Vue.component("restaurant-page", {
    data: function() {
        return {
            showRestaurant: undefined,
            restaurant: undefined,
            showArticles: undefined,
            articleToAddToCart: undefined,
            mapInitialized: undefined
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

                <!-- Modal -->
                <add-to-cart-modal v-if="showArticles" modalId="addToCartModal" v-bind:restaurantName="restaurant.naziv" v-bind:article="articleToAddToCart"></add-to-cart-modal>

                <div class="d-flex flex-row justify-content-center">
                    <div class="d-flex flex-column" style="width: 90%;">
                        <h3 class="p-2"><b>Articles</b></h3>
                        <div v-if="showArticles" class="d-flex flex-row flex-wrap" style="width: 100%;">
                            <div class="card my-1" style="width: 25%;" v-for="a in restaurant.artikli">
                                <img class="card-img-top" v-bind:src="'http://localhost:8080/web-2020-21/images/article-images/' + a.restoran + '-' + a.naziv + '.jpg'" alt="Card image cap">
                                <div class="card-body d-flex flex-column justify-content-between">
                                    <div class="d-flex flex-column">
                                        <h5 class="card-title">{{ a.naziv }}</h5>
                                        <p class="card-text">{{ a.opis }}</p>
                                    </div>
                                    <div class="d-flex flex-column">
                                        <div class="d-flex flex-row justify-content-between">
                                            <p>{{ a.kolicina }} <span v-if="a.tip =='JELO'">g</span><span v-if="a.tip =='PICE'">ml</span></p>
                                            <p>{{ a.cena }} RSD</p>
                                        </div>
                                        <button 
                                            type="button" 
                                            class="btn btn-secondary"
                                            v-on:click="openAddToCartModal(a)"
                                        >Add to cart</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div v-else>
                            <h5 class="p-2 ml-2">No articles.</h5>
                        </div>
                    </div>
                </div>
            </div>
        </div> 
        <div v-else class="d-flex flex-row justify-content-center">
            <h1> Restaurant with name <span class="text-primary"><b>{{ this.$route.params.restaurantName }}</b></span> doesn't exist. </h1>
        </div>
    </div> `,
    methods: {
        initializeMap() {
            if (!this.mapInitialized) {
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

                this.mapInitialized = true;
            }
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
        },
        openAddToCartModal(article) {
            this.articleToAddToCart = article;

            $('#addToCartModal').modal('toggle');
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