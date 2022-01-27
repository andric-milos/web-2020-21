Vue.component('home-page', {
    data: function() {
        return {
            restaurants: [],
            restaurantsDisplayed: [],
            types: [],
            name: "",
            address: "",
            type: "",
            showAllTypesOption: undefined,
            nameTmp: "",
            addressTmp: ""
        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>
        
        <div class="d-flex p-2 justify-content-center">
            <div class="d-flex flex-column" style="width: 80%;">
                <div class="d-flex flex-row p-2 justify-content-between">
                    <input type="text" v-model="name" class="p-2" id="name" placeholder="Name" style="width: 38%">
                    <input type="text" v-model="address" class="p-2" id="address" placeholder="Address" style="width: 30%">
                    <select name="type" id="type" v-model="type" style="color: gray; width: 30%;">  <!-- height: 45px; -->
                        <option v-if="showAllTypesOption" value="" style="color: black;">All types</option>
                        <option v-for="t in types" v-bind:value="t" style="color: black;">{{ t }}</option>
                    </select>
                </div>

                <div class="d-flex flex-row border border-dark rounded m-2" v-for="r in restaurantsDisplayed">
                    <img 
                        v-bind:src="'http://localhost:8080/web-2020-21/images/restaurant-logos/' + r.naziv + '.jpg'" 
                        class="border-end border-dark rounded p-2" 
                        style="max-width:300px; max-height:300px; width:300px; height:300px; min-width:300px;" />
                    <div class="d-flex flex-column p-2 justify-content-between" style="width: 100%;"> <!-- style="width: 100%;" -->
                        <div class="d-flex flex-column">
                            <h1><b>{{ r.naziv }}</b></h1>
                            <h4><b>Address:</b> {{ r.lokacija.adresa.ulica }} {{ r.lokacija.adresa.broj }}, {{ r.lokacija.adresa.mesto }} {{ r.lokacija.adresa.postanskiBroj }} </h4>
                            <h4><b>Type:</b> {{ r.tip }} </h4>
                            <h4><b>Rating:</b> <span v-if="r.ocena != 0"> {{ r.ocena | roundTo2Decimals }} </span> <span v-if="r.ocena == 0"">Not rated</span> </h4>
                        </div>
                        <div class="d-flex flex-row justify-content-end">
                            <button 
                                type="button" 
                                class="btn btn-secondary" 
                                style="width:80px;"
                                v-on:click="navigateToRestaurant(r.naziv)"
                            >Order</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div> `,
    methods: {
        navigateToRestaurant(restaurantName) {
            this.$router.push("/restaurant/" + restaurantName);
        },
        transformDiacriticalLettersToNonDiacritical(diacritical) {
            let nonDiacritical = diacritical;

            nonDiacritical = nonDiacritical.replace(/š/g, "s");
            nonDiacritical = nonDiacritical.replace(/đ/g, "dj");
            nonDiacritical = nonDiacritical.replace(/č/g, "c");
            nonDiacritical = nonDiacritical.replace(/ć/g, "c");
            nonDiacritical = nonDiacritical.replace(/ž/g, "z");

            nonDiacritical = nonDiacritical.replace(/Š/g, "S");
            nonDiacritical = nonDiacritical.replace(/Đ/g, "Dj");
            nonDiacritical = nonDiacritical.replace(/Č/g, "C");
            nonDiacritical = nonDiacritical.replace(/Ć/g, "C");
            nonDiacritical = nonDiacritical.replace(/Ž/g, "Z");

            return nonDiacritical;
        }
    },
    mounted() {
        axios.get("rest/restaurant")
            .then(response => {
                if (response.status == 200) {
                    this.restaurants = response.data;
                    this.restaurantsDisplayed = response.data;
                } else {
                    console.log(response);
                }
            })
            .catch(error => {
                console.log(error);
            });

        axios.get("rest/restaurant/types")
            .then(response => {
                if (response.status == 200) {
                    this.types = response.data;
                    this.showAllTypesOption = true;
                } else {
                    console.log(response);
                }
            })
            .catch(error => {
                console.log(error);
            });
    },
    watch: {
        type: function(value) {
            if (!value) {
                document.getElementById("type").style.color = "gray";
            } else {
                document.getElementById("type").style.color = "black";
            }

            axios.get("rest/restaurant/search?type=" + this.type + "&name=" + this.name + "&address=" + this.address)
                .then(response => {
                    if (response.status == 200) {
                        this.restaurantsDisplayed = response.data;
                    } else {
                        console.log(response);
                    }
                })
                .catch(error => {
                    console.log(error);
                });
        },
        name: function(value) {
            this.nameTmp = this.transformDiacriticalLettersToNonDiacritical(this.name);

            axios.get("rest/restaurant/search?type=" + this.type + "&name=" + this.nameTmp + "&address=" + this.addressTmp)
                .then(response => {
                    if (response.status == 200) {
                        this.restaurantsDisplayed = response.data;
                    } else {
                        console.log(response);
                    }
                })
                .catch(error => {
                    console.log(error);
                });
        },
        address: function(value) {
            this.addressTmp = this.transformDiacriticalLettersToNonDiacritical(this.address);

            axios.get("rest/restaurant/search?type=" + this.type + "&name=" + this.nameTmp + "&address=" + this.addressTmp)
                .then(response => {
                    if (response.status == 200) {
                        this.restaurantsDisplayed = response.data;
                    } else {
                        console.log(response);
                    }
                })
                .catch(error => {
                    console.log(error);
                });
        }
    },
    filters: {
        roundTo2Decimals(value) {
            if (!value) 
                return "";

            return Math.round((value + Number.EPSILON) * 100) / 100;
        }
    }
});