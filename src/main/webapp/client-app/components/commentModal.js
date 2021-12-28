Vue.component("comment-modal", {
    data: function() {
        return {
            text: undefined,
            rating: undefined
        }
    },
    props: {
        modalId: String,
        restaurant: String
    },
    template: `
    <div class="modal fade" v-bind:id="modalId" tabindex="-1" role="dialog" aria-labelledby="commentModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="commentModalLabel"><b>Leave a comment</b></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body d-flex flex-column">
                    <div id="commentAlert" class="alert alert-info p-2 mx-2" role="alert" hidden>
                        You already left a comment!
                    </div>

                    <label class="p-2"><b>Text</b></label>
                    <textarea class="p-2 mx-2" name="text" v-model="text" rows="5"></textarea>
                    <label id="commentModalTextError" class="p-2 text-danger"></label>

                    <label class="p-2"><b>Rating</b></label>
                    <input class="mx-2" type="number" id="amount" min="1" max="5" v-model="rating" style="width: 50px;" v-on:keydown="preventTyping">
                    <label id="commentModalRatingError" class="p-2 text-danger"></label>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" v-on:click="comment">Confirm</button>
                </div>
            </div>
        </div>
    </div> `,
    methods: {
        preventTyping(event) {
            event.preventDefault();
        },
        validation() {
            let flag = 0;

            if (!this.text) {
                document.getElementById("commentModalTextError").innerHTML = "Text input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("commentModalTextError").innerHTML = "";
            }

            if (!this.rating) {
                document.getElementById("commentModalRatingError").innerHTML = "Rating input field cannot be empty!";
                ++flag;
            } else if (this.rating < 1 && this.rating > 5) {
                document.getElementById("commentModalRatingError").innerHTML = "Invalid value!";
                ++flag;
            } else {
                document.getElementById("commentModalRatingError").innerHTML = "";
            }

            if (flag)
                return false;
            else
                return true;
        },
        comment() {
            if (this.validation()) {
                let dto = {
                    "restoran" : this.restaurant,
                    "tekst" : this.text,
                    "ocena" : this.rating
                };

                axios.post("rest/restaurant/comment", dto)
                    .then(response => {
                        if (response.status == 200) {
                            alert("You successfully left a comment!");
                            window.location.reload();
                        } else {
                            console.log(response);
                        }
                    })
                    .catch(error => {
                        console.log(error);
                    });
            }
        }
    },
    watch: {
        restaurant(value) {
            axios.get("rest/restaurant/didILeaveACommentAlready/" + this.restaurant)
                .then(response => {
                    if (response.status == 200) {
                        if (!(response.data == "NO")) {
                            this.text = response.data.tekst;
                            this.rating = response.data.ocena;
                            document.getElementById("commentAlert").removeAttribute("hidden");
                        } else {
                            this.text = undefined;
                            this.rating = undefined;
                            document.getElementById("commentAlert").setAttribute("hidden", true);
                        }
                    } else {
                        console.log(response);
                    }
                })
                .catch(error => {
                    console.log(error);
                });
        }
    }
});