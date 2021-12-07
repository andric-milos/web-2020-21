Vue.component("shopping-cart-modal", {
    data: function() {
        return {
            cart: undefined,
            restaurant: undefined
        }
    },
    props: {
        modalId : String
    },
    template: `
    <div class="modal fade" v-bind:id="modalId" tabindex="-1" role="dialog" aria-labelledby="shoppingCartModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-scrollable">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="shoppingCartModalLabel"><b>Shopping cart</b></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div v-if="cart" class="d-flex flex-column" style="width: 100%;">
                        <h3 v-if="restaurant" style="text-align:center"> {{ this.restaurant }} </h3>
                        <div class="d-flex flex-row" v-for="a in cart.artikli">
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
                    </div>
                </div>
                <div class="modal-footer" style="justify-content: space-between;">
                    <div><b>Total price: <span v-if="cart"> {{ this.cart.cena }} RSD</span></b></div>
                    <button type="button" class="btn btn-primary">Check-out</button>
                </div>
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
        eventBus.$on('articleAddedToCart', () => {
            axios.get("rest/cart")
                .then(response => {
                    if (response.status == 200) {
                        this.cart = response.data;

                        if (response.data.artikli.length > 0) {
                            this.restaurant = response.data.artikli[0].artikal.restoran;
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