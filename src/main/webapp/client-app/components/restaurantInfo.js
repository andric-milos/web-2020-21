Vue.component("restaurant-info", {
    data: function() {
        return {
            showRestaurant: undefined,
            showArticles: undefined,
            restaurant: undefined,
            artikli: [],
            articleToEdit: undefined
        }
    },
    template: `
    <div id="page-content-wrapper">
        <div v-if="showRestaurant" class="d-flex flex-row justify-content-center">
            <div class="d-flex flex-column p-2">
                <h1 id="restaurantName" class="p-2 text-center"></h1>

                <div class="d-flex flex-row p-2">
                    <div class="container">
                        <img 
                            id="restaurantLogo" 
                            class="border rounded" 
                            height="auto" 
                            width="100%" 
                            max-width="300" />
                    </div>
                    <div class="container" id="map" height="auto" width="100%" max-width="300"></div>
                </div>

                <!--
                <div class="d-flex flex-row p-2">
                    <img src="http://localhost:8080/web-2020-21/images/restaurant-logos/Caribic.jpg" class="border rounded mt-1 p-2 img-fluid" />
                    <img src="http://localhost:8080/web-2020-21/images/restaurant-logos/Caribic.jpg" class="border rounded mt-1 p-2 img-fluid" />
                </div>
                -->

                <div class="d-flex flex-row p-2">
                    <h1 class="p-2"><b>Articles</b></h1>
                    <div class="d-flex flex-column justify-content-center">
                        <button 
                            type="button" 
                            class="btn btn-secondary btn-sm m-2"
                            style="height:30px;"
                            data-bs-toggle="modal"
                            data-bs-target="#addNewArticleModal"
                        > Add new article </button>
                    </div>

                    <!-- Modal -->
                    <add-new-article-modal v-if="restaurant" modalId="addNewArticleModal" v-bind:restaurantName="restaurant.naziv"></add-new-article-modal>
                </div>
                    
                <div v-if="showArticles" class="d-flex flex-row flex-wrap">
                    <div class="card my-1" style="width: 17rem;" v-for="a in artikli">
                        <img class="card-img-top" v-bind:src="'http://localhost:8080/web-2020-21/images/article-images/' + a.restoran + '-' + a.naziv + '.jpg'" alt="Card image cap">
                        <div class="card-body">
                            <h5 class="card-title">{{ a.naziv }}</h5>
                            <p class="card-text">{{ a.opis }}</p>
                            <div class="d-flex flex-row justify-content-between">
                                <p>{{ a.kolicina }} <span v-if="a.tip =='JELO'">g</span><span v-if="a.tip =='PICE'">ml</span></p>
                                <p>{{ a.cena }} RSD</p>
                            </div>
                            <button 
                                type="button" 
                                class="btn btn-secondary btn-sm" 
                                style="width: 100%;"
                                v-on:click="openEditArticleModal(a)"
                            >Edit</button>
                        </div>
                    </div>

                    <!-- Modal -->
                    <edit-article-modal modalId="editArticleModal" v-bind:restaurantName="restaurant.naziv" v-bind:article="articleToEdit"></edit-article-modal>
                </div>
                <div v-else>
                    <h5 class="p-2 ml-2">No articles.</h5>
                </div> 
            </div>
        </div>
        <div v-else>
            <h1 class="p-2 text-center">No restaurant to show.</h1>
        </div>
    </div> `,
    methods: {
        initializeMapAndRestaurantInfo() {
            document.getElementById("restaurantName").innerHTML = "<b>" + this.restaurant.naziv + "</b>";
            document.getElementById("restaurantLogo").setAttribute("src", "http://localhost:8080/web-2020-21/images/restaurant-logos/" + this.restaurant.naziv + ".jpg");

            // var map = L.map('map').setView([this.restaurant.lokacija.geografskaSirina, this.restaurant.lokacija.geografskaDuzina], 13);
            var map = L.map('map', {
                center: [this.restaurant.lokacija.geografskaSirina, this.restaurant.lokacija.geografskaDuzina],
                zoom: 13
            });

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

            setTimeout(() => {
                map.invalidateSize();           
            }, 0);
        },
        openEditArticleModal(article) {
            this.articleToEdit = article;

            $('#editArticleModal').modal('toggle');
        }
    },
    mounted() {
        axios.get("rest/user/getLoggedInUserData")
            .then(response => {
                if (response.data == "NOT LOGGED IN") {
                    this.showRestaurant = false;
                } else {
                    if (response.data.tipKorisnika == "MENADZER") {
                        axios.get("rest/restaurant/byManager/" + response.data.korisnickoIme)
                            .then(response => {
                                this.restaurant = response.data;
                                this.showRestaurant = true;
                                this.initializeMapAndRestaurantInfo();

                                this.artikli = response.data.artikli;

                                if (this.artikli == [])
                                    this.showArticles = false;
                                else
                                    this.showArticles = true;
                            })
                            .catch(error => {
                                console.log(error);
                                this.showRestaurant = false;
                            });

                        this.showRestaurant = true;
                    } else {
                        this.showRestaurant = false;
                    }
                }
            })
            .catch(error => {
                console.log(error);
            });
    }
});