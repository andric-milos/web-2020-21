Vue.component("shopping-cart", {
    data: function() {
        return {
            cart: {
                artikli: [],
                cena: 0,
                kupac: null
            },
            restaurant: undefined,
            loggedIn: undefined,
            cartEmpty: undefined
        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex p-2 justify-content-center">
            <div v-if="!cartEmpty" class="d-flex flex-column" style="width: 50%;">
                <div v-if="restaurant" class="d-flex flex-row justify-content-center">
                    <h1> {{this.restaurant}} </h1>
                    <label class="pt-4">[<a v-bind:href="'http://localhost:8080/web-2020-21/#/restaurant/' + this.restaurant">Click here to order more</a>]</label>
                </div>
                <div class="d-flex flex-row border" v-for="a in cart.artikli">
                    <img class="border" style="width: 50%; height: auto;" v-bind:src="'http://localhost:8080/web-2020-21/images/article-images/' + a.artikal.restoran + '-' + a.artikal.naziv + '.jpg'" alt="No image">
                    <div class="d-flex flex-column p-2">
                        <label><b> {{ a.artikal.naziv }} </b></label>
                        <label>Restaurant: {{ a.artikal.restoran }}</label>
                        <label>Description: {{ a.artikal.opis }}</label>
                        <label>Price: {{ a.artikal.cena }} RSD</label>
                        <label>Quantity: {{ a.artikal.kolicina }} <span v-if="a.artikal.tip == 'JELO'">g</span><span v-if="a.artikal.tip == 'PICE'">ml</span></label>
                        <label>Amount: {{ a.koliko }}</label>
                    </div>
                </div>
                <button v-if="loggedIn && !cartEmpty" type="button" class="btn btn-primary mt-2">Send</button>
                <div v-if="!loggedIn && !cartEmpty" class="d-flex flex-column">
                    <label><b>Note:</b> you're not logged in! You must be logged in to confirm an order.</label>
                    <label><a href="http://localhost:8080/web-2020-21/#/register">Click here</a> to register if you don't have an account.</label>
                    <label><a href="http://localhost:8080/web-2020-21/#/login">Click here</a> to log in.</label>
                </div>
            </div>
            <div v-if="cartEmpty">
                <h1>Cart is empty.</h1>
            </div>
        </div>
    </div> `,
    created() {
        axios.get("rest/cart")
            .then(response => {
                if (response.status == 200) {
                    this.cart = response.data;

                    if (response.data.artikli.length > 0) {
                        this.restaurant = response.data.artikli[0].artikal.restoran;
                        this.cartEmpty = false;
                    } else {
                        this.restaurant = "";
                        this.cartEmpty = true;
                    }
                } else {
                    console.log(response);
                }
            })
            .catch(error => {
                console.log(error);
            });

        axios.get("rest/user/loggedIn")
            .then(response => {
                if (response.status == 200) {
                    if (response.data == "NOT LOGGED IN") {
                        this.loggedIn = false;
                    } else {
                        this.loggedIn = true;
                    }
                } else {
                    console.log(response);
                }
            })
            .catch(error => {
                console.log(error);
            });
    },
    mounted() {
        eventBus.$on('cartUpdated', () => {
            axios.get("rest/cart")
                .then(response => {
                    if (response.status == 200) {
                        this.cart = response.data;

                        if (response.data.artikli.length > 0) {
                            this.restaurant = response.data.artikli[0].artikal.restoran;
                            this.cartEmpty = false;
                        } else {
                            this.restaurant = "";
                            this.cartEmpty = true;
                        }
                    } else {
                        console.log(response);
                    }
                })
                .catch(error => {
                    console.log(error);
                });
        });
    }
});