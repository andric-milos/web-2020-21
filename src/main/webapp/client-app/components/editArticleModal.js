Vue.component("edit-article-modal", {
    data: function() {
        return {
            articleName: undefined,
            type: undefined,
            image: null,
            description: undefined,
            quantity: undefined,
            price: undefined,
            previousArticleName: undefined,
            imageChanged: false
        }
    },
    props: {
        modalId : String,
        restaurantName: String,
        article: Object
    },
    template: `
    <div class="modal fade" v-bind:id="modalId" tabindex="-1" role="dialog" aria-labelledby="editArticleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editArticleModalLabel"><b>Edit article</b></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="d-flex flex-column">
                        <label class="p-2"><b>Article name</b></label>
                        <input type="text" class="p-2" v-model="articleName">
                        <label id="editArticleModalnameError" class="p-2 text-danger"></label>

                        <label class="p-2"><b>Type</b></label>
                        <select name="type" class="p-2" v-model="type">
                            <option value="PICE">Drink</option>
                            <option value="JELO">Food</option>
                        </select>
                        <label id="editArticleModaltypeError" class="p-2 text-danger"></label>

                        <label class="p-2"><b>Image</b></label>
                        <input 
                            type="file" 
                            accept="image/png, image/jpeg"
                            v-on:change="onImageSelected"
                            ref="imageInput"
                            hidden
                        >
                        <button 
                            type="button"
                            class="p-2 btn btn-secondary" 
                            v-on:click="$refs.imageInput.click()"
                        > Select an image </button>
                        <img id="editArtileModalImagePreview" class="border rounded mt-1" style="display:flex" height="250" alt="No image selected."/>
                        <label id="editArticleModalimageError" class="p-2 text-danger"></label>

                        <label class="p-2"><b>Description</b></label>
                        <textarea class="p-2" name="description" v-model="description" rows="5"></textarea>
                        <label id="editArticleModaldescriptionError" class="p-2 text-danger"></label>

                        <label class="p-2"><b>Quantity</b></label>
                        <div class="d-flex flex-row">
                            <input type="number" min="1" class="p-2" v-model="quantity">
                            <label id="gramsOrMilliliters" class="p-2"></label>
                        </div>
                        <label id="editArticleModalquantityError" class="p-2 text-danger"></label>

                        <label class="p-2"><b>Price</b></label>
                        <div class="d-flex flex-row">
                            <input type="number" min="1" class="p-2" v-model="price">
                            <label class="p-2"><b>RSD</b></label>
                        </div>
                        <label id="editArticleModalpriceError" class="p-2 text-danger"></label>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" v-on:click="editArticle">Submit</button>
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
                    document.getElementById("editArtileModalImagePreview").setAttribute("src", e.target.result);
                }

                reader.readAsDataURL(event.target.files[0]);
                this.imageChanged = true;
            } else {
                document.getElementById("editArtileModalImagePreview").removeAttribute("src");
                this.image = null;
            }
        },
        validation() {
            let flag = 0;

            if(!this.articleName) {
                document.getElementById("editArticleModalnameError").innerHTML = "Article name input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("editArticleModalnameError").innerHTML = "";
            }

            if (!this.type) {
                document.getElementById("editArticleModaltypeError").innerHTML = "Article type input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("editArticleModaltypeError").innerHTML = "";
            }

            if (!this.image) {
                if (this.imageChanged) {
                    document.getElementById("editArticleModalimageError").innerHTML = "Image hasn't been selected!";
                    ++flag;
                }
            } else {
                document.getElementById("editArticleModalimageError").innerHTML = "";
            }

            if (!this.description) {
                document.getElementById("editArticleModaldescriptionError").innerHTML = "Description input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("editArticleModaldescriptionError").innerHTML = "";
            }

            if (!this.quantity) {
                document.getElementById("editArticleModalquantityError").innerHTML = "Quantity input field cannot be empty!";
                ++flag;
            } else if (this.quantity < 1) {
                document.getElementById("editArticleModalquantityError").innerHTML = "Quantity must be a positive number!";
                ++flag;
            } else {
                document.getElementById("editArticleModalquantityError").innerHTML = "";
            }

            if (!this.price) {
                document.getElementById("editArticleModalpriceError").innerHTML = "Price input field cannot be empty!";
                ++flag;
            } else if (this.price < 1) {
                document.getElementById("editArticleModalpriceError").innerHTML = "Price must be a positive number!";
                ++flag;
            } else {
                document.getElementById("editArticleModalpriceError").innerHTML = "";
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
        editArticle() {
            if (this.validation()) {
                let fd = new FormData();

                fd.append("stariNazivArtikla", this.previousArticleName);
                fd.append("noviNazivArtikla", this.articleName);
                fd.append("tipArtikla", this.type);
                fd.append("slikaArtikla", this.image);
                fd.append("opisArtikla", this.description);
                fd.append("kolicina", this.quantity);
                fd.append("cena", this.price);
                fd.append("nazivRestorana", this.restaurantName);
                fd.append("slikaMenjana", this.imageChanged);

                // console.log(this.image);

                axios.put("rest/restaurant/editArticle", fd)
                    .then(response => {
                        if (response.status == 200) {
                            alert("You successfully edited article: " + this.previousArticleName);
                            window.location.reload();
                        } else {
                            console.log(response);
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
                        } else if (error.response.data == "RESTAURANT DOES NOT EXIST") {
                            alert("Restaurant " + this.restaurantName + " doesn't exist!");
                        } else if (error.response.data == "ARTICLE NAME TAKEN") {
                            alert("Article with name " + this.articleName + " already exists!");
                        } else if (error.response.data == "FILE ERROR") {
                            alert("Problem with uploading an image file occurred!");
                        } else {
                            console.log(error);
                        }
                    });
            }
        }
    },
    watch: {
        article: function() {
            if (this.article != undefined) {
                this.articleName = this.article.naziv;
                this.previousArticleName = this.article.naziv;
                this.type = this.article.tip;
                this.description = this.article.opis;
                this.quantity = this.article.kolicina;
                this.price = this.article.cena;

                
                document.getElementById("editArtileModalImagePreview").setAttribute("src", "http://localhost:8080/web-2020-21/images/article-images/" + this.article.restoran + "-" + this.article.naziv + ".jpg");
                
                /*
                axios.get("http://localhost:8080/web-2020-21/images/article-images/" + this.article.restoran + "-" + this.article.naziv + ".jpg")
                    .then(response => {
                        // this.image = response.data;

                        // this.image = new Blob([response.data]);
                        // console.log([response.data]);

                        let blob = new Blob([response.data], {type: 'image/jpeg'});
                        console.log(blob);

                        *//*
                        let blob = new Blob([response.data]);
                        console.log(blob);

                        let reader = new FileReader();
                        reader.readAsDataURL(blob);

                        var self = this;
                        reader.onload = function() {
                            self.image = reader.result;
                        }
                        *//*

                        this.image = new File([blob], "image123.jpg", { type: 'image/jpeg' });
                    })
                    .catch(error => {
                        console.log(error);
                    });
                */
            }  
        },
        type: function(value) {
            if (value == "PICE")
                document.getElementById("gramsOrMilliliters").innerHTML = "<b>ml</b>";
            else if (value == "JELO")
                document.getElementById("gramsOrMilliliters").innerHTML = "<b>g</b>";
            else
                document.getElementById("gramsOrMilliliters").innerHTML = "";
        }
    }
});