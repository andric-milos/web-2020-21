Vue.component("restaurant-info", {
    data: function() {
        return {
            
        }
    },
    template: `
    <div id="page-content-wrapper">
        <div class="d-flex flex-row justify-content-center">
            <div class="d-flex flex-column p-2">
                <h1 class="p-2 text-center">Ime restorana</h1>

                <div class="d-flex flex-row p-2">
                    <div class="container">
                        <img src="https://www.alltimelow.com/sites/g/files/g2000006681/f/styles/new_res_custom_user_iphone_portrait_1x/public/Sample-image10-highres.jpg?itok=pHvSQPEY" 
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
                    <button type="button" class="btn btn-secondary m-2">Add new article</button>
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
    </div> `,
    methods: {
        
    },
    mounted() {
        var map = L.map('map').setView([45.2460312, 19.8359505], 13);

        L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
            attribution: '',
            maxZoom: 18,
            id: 'mapbox/streets-v11',
            tileSize: 512,
            zoomOffset: -1,
            accessToken: 'pk.eyJ1IjoibWVlbG9zY2giLCJhIjoiY2trcTRkNzl3MGZ6djJvcW4zeHFxeHg0YyJ9.h06Ayx7JxZqxszi6nLpsZw'
        }).addTo(map);

        var marker = L.marker([45.2460312, 19.8359505], {draggable: false});
        marker.addTo(map);
    }
});