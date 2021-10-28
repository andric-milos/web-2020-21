Vue.component("new-restaurant", {
    data: function() {
        return {
            pinned: undefined,

            latitude: undefined,
            longitude: undefined,

            street: undefined,
            streetNumber: undefined,
            city: undefined,
            postalCode: undefined,

            restaurantName: undefined,
            type: undefined,
            manager: undefined,
            logo: null,

            types: [],
            managers: []
        }
    },
    template: `
    <div id="page-content-wrapper">
        <div class="d-flex p-2 justify-content-center">
            <div class="d-flex flex-column col-sm-4">
                <h1 class="p-2">Add new restaurant</h1>

                <label class="p-2"><b>Restaurant name</b></label>
                <input type="text" class="p-2" id="restaurantName" v-model="restaurantName">
                <label id="nameError" class="p-2 text-danger"></label>

                <label class="p-2"><b>Type of restaurant</b></label>
                <select name="type" class="p-2" id="type" v-model="type">
                    <option v-for="t in types" v-bind:value="t">{{ t }}</option>
                </select>
                <label id="typeError" class="p-2 text-danger"></label>

                <label class="p-2"><b>Location</b></label>
                <div id="map" style="height: 300px;"></div> <!-- class="p-2" -->
                <label id="mapError" class="p-2 text-danger"></label>

                <br>
                <div class="row p-2 justify-content-between">
                    <label class="col-5"><b>Street</b></label>
                    <label class="col-5" style="text-align: right;"><b>St. number</b></label>
                </div>
                <div class="row px-2 justify-content-between">
                    <input type="text" class="p-2 col-8" id="street" v-model="street">
                    <input type="number" min="1" class="p-2 col-3" id="streetNumber" v-model="streetNumber">
                </div>
                <label id="streetError" class="p-2 text-danger"></label>

                <label class="p-2"><b>City</b></label>
                <input type="text" class="p-2" id="city" v-model="city">
                <label id="cityError" class="p-2 text-danger"></label>

                <label class="p-2"><b>Postal code</b></label>
                <input type="number" min="1" class="p-2" id="postalCode" v-model="postalCode">
                <label id="postalCodeError" class="p-2 text-danger"></label>
                
                <label class="p-2"><b>Logo</b></label>
                <input 
                    type="file" 
                    id="logo" 
                    accept="image/png, image/jpeg"
                    v-on:change="onLogoSelected"
                    ref="logoInput"
                    hidden
                >
                <button 
                    type="button"
                    class="p-2 btn btn-secondary" 
                    v-on:click="$refs.logoInput.click()"
                > Select an image </button>
                <img id="logoPreview" class="border rounded mt-1" style="display:flex" height="200" alt="No image selected."/>
                <label id="logoError" class="p-2 text-danger"></label>

                <div class="d-flex justify-content-between p-2">
                    <label><b>Manager</b></label>
                    <div class="d-flex flex-column justify-content-center">
                        <button 
                            type="button" 
                            class="btn btn-secondary btn-sm"
                            style="height:30px;"
                            data-bs-toggle="modal"
                            data-bs-target="#addNewManagerModal"
                        > Add new manager </button>
                    </div>

                    <!-- Modal -->
                    <add-new-manager-modal modalId="addNewManagerModal"></add-new-manager-modal>
                </div>
                
                <select name="manager" class="p-2" id="manager" v-model="manager">
                    <option v-for="m in managers" v-bind:value="m.korisnickoIme">{{ m | fullName }}</option>
                </select>
                <label id="managerError" class="p-2 text-danger"></label>

                <br>
                <button type="submit" class="btn btn-primary" v-on:click.prevent="addRestaurant">Confirm</button>
            </div>
        </div>
    </div> `,
    methods: {
        onLogoSelected(event) {
            if (event.target.files && event.target.files[0]) {
                this.logo = event.target.files[0];

                var reader = new FileReader();

                reader.onload = function(e) {
                    document.getElementById("logoPreview").setAttribute("src", e.target.result);
                }

                reader.readAsDataURL(event.target.files[0]);
            } else {
                document.getElementById("logoPreview").removeAttribute("src");
                this.logo = null;
            }
        },
        addRestaurant() {
            if (this.validation()) {
                let fd = new FormData();

                fd.append("nazivRestorana", this.restaurantName);
                fd.append("tipRestorana", this.type);
                fd.append("geoSirina", this.latitude);
                fd.append("geoDuzina", this.longitude);
                fd.append("ulica", this.street);
                fd.append("broj", this.streetNumber);
                fd.append("mesto", this.city);
                fd.append("postanskiBroj", this.postalCode);
                fd.append("menadzer", this.manager);
                fd.append("logo", this.logo);

                /*
                let config = {
                    headers: {
                        'Content-Type' : 'multipart/form-data'
                    }
                };
                */

                axios.post("rest/restaurant", fd)
                    .then(response => {
                        if (response.status == 200) {
                            alert("You successfully added new restaurant: " + this.restaurantName);
                            window.location.reload();
                        } else {
                            console.log(response);
                        }
                    })
                    .catch(error => {
                        if (error.response.data == "NOT LOGGED IN") {
                            alert("You're not logged in!");
                            this.$router.push("/login");
                        } else if (error.response.data == "NOT ADMINISTRATOR") {
                            alert("You're not authorized to add new restaurant!");
                        } else if (error.response.data == "EMPTY FIELDS") {
                            alert("Input fields cannot be empty!");
                        } else if (error.response.data == "INVALID RESTAURANT TYPE") {
                            alert("Type of restaurant you entered doesn't exist!");
                        } else if (error.response.data == "MANAGER DOES NOT EXIST") {
                            alert("Manager you selected doesn't exist!");
                        } else if (error.response.data == "MANAGER ALREADY TAKEN") {
                            alert("Manager you selected is already taken!");
                        } else if (error.response.data == "INVALID STREET NUMBER") {
                            alert("Street number must be a positive number!");
                        } else if (error.response.data == "INVALID POSTAL CODE") {
                            alert("Postal code must be a positive number!");
                        } else {
                            console.log(error);
                        }
                    });
            }
        },
        validation() {
            let flag = 0;

            if (!this.restaurantName) {
                document.getElementById("nameError").innerHTML = "Restaurant name input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("nameError").innerHTML = "";
            }

            if (!this.type) {
                document.getElementById("typeError").innerHTML = "Restaurant type input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("typeError").innerHTML = "";
            }

            if (!this.manager) {
                document.getElementById("managerError").innerHTML = "Manager input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("managerError").innerHTML = "";
            }

            if (!this.street) {
                document.getElementById("streetError").innerHTML = "Street input field cannot be empty!";
                ++flag;
            } else if (!this.streetNumber) {
                document.getElementById("streetError").innerHTML = "Street number input field cannot be empty!";
                ++flag;
            } else if (this.streetNumber < 1) {
                document.getElementById("streetError").innerHTML = "Street number must be positive!";
                ++flag;
            } else {
                document.getElementById("streetError").innerHTML = "";
            }

            if (!this.city) {
                document.getElementById("cityError").innerHTML = "City input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("cityError").innerHTML = "";
            }

            if (!this.postalCode) {
                document.getElementById("postalCodeError").innerHTML = "Postal code input field cannot be empty!";
                ++flag;
            } else if (this.postalCode < 1) {
                document.getElementById("postalCodeError").innerHTML = "Postal code must be a positive number!";
                ++flag;
            } else {
                document.getElementById("postalCodeError").innerHTML = "";
            }

            if (!this.pinned) {
                document.getElementById("mapError").innerHTML = "You have not chosen the location!";
                ++flag;
            } else {
                document.getElementById("mapError").innerHTML = "";
            }

            if (!this.logo) {
                document.getElementById("logoError").innerHTML = "Logo hasn't been selected!";
                ++flag;
            } else {
                document.getElementById("logoError").innerHTML = "";
            }

            if (flag)
                return false;
            else
                return true;
        }
    },
    mounted() {
        axios.get("rest/restaurant/types")
            .then(response => {
                if (response.status == 200) {
                    this.types = response.data;
                } else {
                    console.log(response);
                }
            })
            .catch(error => {
                console.log(error);
            });

        axios.get("rest/user/allAvailableManagers")
            .then(response => {
                if (response.status == 200) {
                    this.managers = response.data;
                } else {
                    console.log(response);
                }
            })
            .catch(error => {
                console.log(error);
            });

        var map = L.map('map').setView([45.2460312, 19.8359505], 13);

        L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
            attribution: 'Click on the map to pin a location. Drag the marker to adjust the location. Click on the marker to remove it.',
            maxZoom: 18,
            id: 'mapbox/streets-v11',
            tileSize: 512,
            zoomOffset: -1,
            accessToken: 'pk.eyJ1IjoibWVlbG9zY2giLCJhIjoiY2trcTRkNzl3MGZ6djJvcW4zeHFxeHg0YyJ9.h06Ayx7JxZqxszi6nLpsZw'
        }).addTo(map);

        /*
        marker = L.marker([45.2460312, 19.8359505], { draggable: true });
        marker.addTo(map);
        */

        var self = this;

        map.on('click', function(e) {
            if (!self.pinned) {
                var marker = L.marker([e.latlng.lat, e.latlng.lng], {draggable: true});
                marker.on('click', function(e) {
                    // console.log("Marker at " + marker._latlng + " removed");
                    map.removeLayer(marker);
                    self.pinned = false;
                    self.latitude = undefined;
                    self.longitude = undefined;
                });

                marker.addTo(map);
                self.pinned = true;
                self.latitude = marker.getLatLng().lat;
                self.longitude = marker.getLatLng().lng;
                // console.log("Marker created at " + e.latlng);
            }
        });
    },
    filters: {
        fullName(value) {
            if (!value)
                return "";

            return value.ime + " " + value.prezime + " [username: " + value.korisnickoIme + "]";
        }
    }
});