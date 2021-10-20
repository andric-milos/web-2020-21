Vue.component("restaurant-info", {
    data: function() {
        return {
            showRestaurant: undefined,
            restaurant: undefined
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

                <div class="d-flex flex-row justify-content-between p-2">
                    <h3 class="p-2"><b>Articles</b></h3>
                    <button 
                        type="button" 
                        class="btn btn-secondary m-2" >
                    Add new article
                    </button>
                </div>
                
                <div class="d-flex flex-row flex-wrap justify-content-between" style="max-width:570px;">
                    <div class="card my-1" style="width: 17rem;">
                        <img class="card-img-top" src="https://imgsv.imaging.nikon.com/lineup/dslr/df/img/sample/img_01.jpg" alt="Card image cap">
                        <div class="card-body">
                            <h5 class="card-title">Artikal 1</h5>
                            <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
                            <div class="d-flex flex-row justify-content-between">
                                <p>250 g</p>
                                <p>500 RSD</p>
                            </div>
                        </div>
                    </div>

                    <div class="card my-1" style="width: 17rem;">
                        <img class="card-img-top" src="https://imgsv.imaging.nikon.com/lineup/dslr/df/img/sample/img_01.jpg" alt="Card image cap">
                        <div class="card-body">
                            <h5 class="card-title">Mali pepsi</h5>
                            <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
                            <div class="d-flex flex-row justify-content-between">
                                <p>100 ml</p>
                                <p>70 RSD</p>
                            </div>
                        </div>
                    </div>

                    <div class="card my-1" style="width: 17rem;">
                        <img class="card-img-top" src="https://imgsv.imaging.nikon.com/lineup/dslr/df/img/sample/img_01.jpg" alt="Card image cap">
                        <div class="card-body">
                            <h5 class="card-title">Parce pice</h5>
                            <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
                            <div class="d-flex flex-row justify-content-between">
                                <p>150 g</p>
                                <p>150 RSD</p>
                            </div>
                        </div>
                    </div>

                    <div class="card my-1" style="width: 17rem;">
                        <img class="card-img-top" src="https://imgsv.imaging.nikon.com/lineup/dslr/df/img/sample/img_01.jpg" alt="Card image cap">
                        <div class="card-body">
                            <h5 class="card-title">Artikal 3</h5>
                            <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
                            <div class="d-flex flex-row justify-content-between">
                                <p>250 g</p>
                                <p>500 RSD</p>
                            </div>
                        </div>
                    </div>

                    <div class="card my-1" style="width: 17rem;">
                        <img class="card-img-top" src="https://imgsv.imaging.nikon.com/lineup/dslr/df/img/sample/img_01.jpg" alt="Card image cap">
                        <div class="card-body">
                            <h5 class="card-title">Artikal 4</h5>
                            <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
                            <div class="d-flex flex-row justify-content-between">
                                <p>250 g</p>
                                <p>500 RSD</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div v-else>
            <h1 class="p-2 text-center">No restaurant to show.</h1>
        </div>
    </div> `,
    methods: {
        initializeMapAndRestaurantInfo() {
            document.getElementById("restaurantName").innerHTML = this.restaurant.naziv;
            document.getElementById("restaurantLogo").setAttribute("src", "http://localhost:8080/web-2020-21/images/restaurant-logos/" + this.restaurant.naziv + ".jpg");

            var map = L.map('map').setView([45.2460312, 19.8359505], 13);

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
        }
    },
    mounted() {
        axios.get("rest/user/getLoggedInUserData")
            .then(response => {
                if (response.data == "NOT LOGGED IN") {
                    this.showRestaurant = false;
                } else {
                    if (response.data.tipKorisnika == "MENADZER") {
                        // novi axios poziv
                        axios.get("rest/restaurant/byManager/" + response.data.korisnickoIme)
                            .then(response => {
                                this.restaurant = response.data;
                                this.showRestaurant = true;
                                this.initializeMapAndRestaurantInfo();
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