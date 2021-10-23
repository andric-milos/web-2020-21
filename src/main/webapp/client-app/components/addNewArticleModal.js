Vue.component("add-new-article-modal", {
    data: function() {
        return {
            articleName: undefined,
            type: undefined,
            image: null,
            description: undefined,
            quantity: undefined,
            price: undefined
        }
    },
    props: {
        modalId : String,
        restaurantName: String
    },
    template: `
    <div class="modal fade" v-bind:id="modalId" tabindex="-1" role="dialog" aria-labelledby="addNewArticleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addNewArticleModalLabel"><b>Add new acrticle</b></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="d-flex flex-column">
                        <label class="p-2"><b>Article name</b></label>
                        <input type="text" class="p-2" id="articleName" v-model="articleName">
                        <label id="nameError" class="p-2 text-danger"></label>

                        <label class="p-2"><b>Type</b></label>
                        <select name="type" class="p-2" id="type" v-model="type">
                            <option value="PICE">Drink</option>
                            <option value="JELO">Food</option>
                        </select>
                        <label id="typeError" class="p-2 text-danger"></label>

                        <label class="p-2"><b>Image</b></label>
                        <input 
                            type="file" 
                            id="image" 
                            accept="image/png, image/jpeg"
                            v-on:change="onImageSelected"
                            ref="imageInput"
                            hidden
                        >
                        <button class="p-2" v-on:click="$refs.imageInput.click()"><b>Select an image</b></button>
                        <img id="imagePreview" class="border rounded mt-1" style="display:flex" height="250" alt="No image selected."/>
                        <label id="imageError" class="p-2 text-danger"></label>

                        <label class="p-2"><b>Description</b></label>
                        <textarea class="p-2" id="description" name="description" v-model="description" rows="5"></textarea>
                        <label id="descriptionError" class="p-2 text-danger"></label>

                        <label class="p-2"><b>Quantity</b></label>
                        <div class="d-flex flex-row">
                            <input type="number" min="1" class="p-2" id="quantity" v-model="quantity">
                            <label id="gOrMl" class="p-2"></label>
                        </div>
                        <label id="quantityError" class="p-2 text-danger"></label>

                        <label class="p-2"><b>Price</b></label>
                        <div class="d-flex flex-row">
                            <input type="number" min="1" class="p-2" id="price" v-model="price">
                            <label class="p-2"><b>RSD</b></label>
                        </div>
                        <label id="priceError" class="p-2 text-danger"></label>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" v-on:click="addNewArticle">Submit</button>
                </div>
            </div>
        </div>
    </div> `,
    methods: {
        onImageSelected(event) {
            if (event.target.files && event.target.files[0]) {
                this.image = event.target.files[0];

                var reader = new FileReader();

                reader.onload = function(e) {
                    document.getElementById("imagePreview").setAttribute("src", e.target.result);
                }

                reader.readAsDataURL(event.target.files[0]);
            } else {
                document.getElementById("imagePreview").removeAttribute("src");
                this.image = null;
            }
        },
        validation() {
            let flag = 0;

            if(!this.articleName) {
                document.getElementById("nameError").innerHTML = "Article name input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("nameError").innerHTML = "";
            }

            if (!this.type) {
                document.getElementById("typeError").innerHTML = "Article type input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("typeError").innerHTML = "";
            }

            if (!this.image) {
                document.getElementById("imageError").innerHTML = "Image hasn't been selected!";
                ++flag;
            } else {
                document.getElementById("imageError").innerHTML = "";
            }

            if (!this.description) {
                document.getElementById("descriptionError").innerHTML = "Description input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("descriptionError").innerHTML = "";
            }

            if (!this.quantity) {
                document.getElementById("quantityError").innerHTML = "Quantity input field cannot be empty!";
                ++flag;
            } else if (this.quantity < 1) {
                document.getElementById("quantityError").innerHTML = "Quantity must be a positive number!";
                ++flag;
            } else {
                document.getElementById("quantityError").innerHTML = "";
            }

            if (!this.price) {
                document.getElementById("priceError").innerHTML = "Price input field cannot be empty!";
                ++flag;
            } else if (this.price < 1) {
                document.getElementById("priceError").innerHTML = "Price must be a positive number!";
                ++flag;
            } else {
                document.getElementById("priceError").innerHTML = "";
            }

            if (!this.restaurantName) { // just in case
                ++flag;
                alert("Restaurant name is null!");
            }

            if (flag)
                return false;
            else
                return true;
        },
        addNewArticle() {
            if (this.validation()) {
                let fd = new FormData();

                fd.append("nazivArtikla", this.articleName);
                fd.append("tipArtikla", this.type);
                fd.append("slikaArtikla", this.image);
                fd.append("opisArtikla", this.description);
                fd.append("kolicina", this.quantity);
                fd.append("cena", this.price);
                fd.append("nazivRestorana", this.restaurantName);

                axios.post("rest/restaurant/addNewArticle", fd)
                    .then(response => {
                        if (response.status == 200) {
                            alert("You successfully added new article: " + this.articleName);
                            window.location.reload();
                        }
                    })
                    .catch(error => {
                        if (error.response.data == "NOT LOGGED IN") {
                            alert("You're not logged in!");
                            this.$router.push("/login");
                        } else if (error.response.data == "NOT MANAGER" || error.response.data == "WRONG MANAGER") {
                            alert("You're not authorized to perform this action!");
                        } else if (error.response.data == "EMPTY FIELDS") {
                            alert("Input fields cannot be empty!");
                        } else if (error.response.data == "INVALID ARTICLE TYPE") {
                            alert("Type of article you entered doesn't exist!");
                        } else if (error.response.data == "INVALID QUANTITY NUMBER") {
                            alert("Quantity must be a positive number!");
                        } else if (error.response.data == "INVALID PRICE NUMBER") {
                            alert("Price must be a positive number!");
                        } else {
                            console.log(error);
                        }
                    });
            } 
        }
    },
    watch: {
        type: function(value) {
            if (value == "PICE")
                document.getElementById("gOrMl").innerHTML = "<b>ml</b>";
            else if (value == "JELO")
                document.getElementById("gOrMl").innerHTML = "<b>g</b>";
            else
                document.getElementById("gOrMl").innerHTML = "";
        }
    }
});