Vue.component("add-to-cart-modal", {
    data: function() {
        return {
            articleName: undefined,
            type: undefined,
            image: null,
            description: undefined,
            quantity: undefined,
            price: undefined,
            amount: 1
        }
    },
    props: {
        modalId : String,
        restaurantName: String,
        article: Object
    },
    template: `
    <div class="modal fade" v-bind:id="modalId" tabindex="-1" role="dialog" aria-labelledby="addToCartModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addToCartModalLabel"><b>Add to cart</b></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <h4><b>{{ this.articleName }}</b></h4>
                    <img id="articleImagePreview" class="border rounded mt-1" style="display:flex" height="250" alt="No image."/>
                    <label><b>Description: {{ this.description }}</b></label><br>
                    <label><b>Quantity: {{ this.quantity }} <span v-if="this.type =='JELO'">g</span><span v-if="this.type =='PICE'">ml</span></b></label><br>
                    <label><b>Price: {{ this.price }} RSD</b></label><br>
                    <label><b>Amount:</b></label>
                    <input type="number" id="amount" min="1" max="50" v-model="amount" style="width: 50px;">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" v-on:click="addToCart">Confirm</button>
                </div>
            </div>
        </div>
    </div>
    `,
    watch: {
        article: function() {
            if (this.article != undefined) {
                this.articleName = this.article.naziv;
                this.type = this.article.tip;
                this.description = this.article.opis;
                this.quantity = this.article.kolicina;
                this.price = this.article.cena;
                this.amount = 1;

                document.getElementById("articleImagePreview").setAttribute("src", "http://localhost:8080/web-2020-21/images/article-images/" + this.article.restoran + "-" + this.article.naziv + ".jpg");
            }
        }
    },
    methods: {
        addToCart() {
            //eventBus.$emit('wtf');
            //console.log("addToCart: wtf");

            let dto = {
                "restoran" : this.restaurantName,
                "naziv" : this.articleName,
                "koliko" : this.amount
            };

            axios.post("rest/cart", dto)
                .then(response => {
                    if (response.status == 200) {
                        eventBus.$emit('articleAddedToCart');
                        $('#' + this.modalId).modal('hide');
                    } else {
                        console.log(response);
                    }
                })
                .catch(error => {
                    if (error.response.data == "RESTAURANT DOES NOT EXIST") {
                        alert("Restaurant " + this.restaurantName +  " doesn't exist!");
                    } else if (error.response.data == "ARTICLE DOES NOT EXIST") {
                        alert("Article " + this.articleName + " doesn't exist!");
                    } else if (error.response.data == "INVALID AMOUNT") {
                        alert("Invalid amount!");
                    } else if (error.response.data == "CAN'T MIX RESTAURANTS") {
                        alert("Can't order from multiple restaurants at the same time!");
                    } else {
                        console.log(error.response.data);
                    }
                });
        }
    }
});