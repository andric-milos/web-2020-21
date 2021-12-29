Vue.component("manager-comments", {
    data: function() {
        return {
            comments: []
        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex" id="wrapper">
            <manager-sidebar></manager-sidebar>

            <div id="page-content-wrapper">
                <div class="d-flex p-2 justify-content-center">
                    <div v-if="comments" class="d-flex flex-column" style="width: 75%;">
                        <div class="d-flex flex-column border border-dark rounded m-2 bg-light" v-for="c in comments">
                            <label class="p-2 h3" style=""><b>{{ c.kupac }}</b></label>
                            <label class="p-2"><b>Text:</b> {{ c.tekst }}</label>
                            <label class="p-2"><b>Rating:</b> {{ c.ocena }}</label>
                            <div class="d-flex flex-row justify-content-end p-2">
                                <div>
                                    <button type="button" class="btn btn-primary btn-sm mr-1" v-on:click="approve(c.restoran, c.kupac)">Approve</button>
                                    <button type="button" class="btn btn-primary btn-sm ml-1" v-on:click="reject(c.restoran, c.kupac)">Reject</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div> `,
    mounted() {
        axios.get("rest/restaurant/comments/pending")
            .then(response => {
                if (response.status == 200) {
                    this.comments = response.data;
                } else {
                    console.log(response);
                }
            })
            .catch(error => {
                console.log(error);
            });
    },
    methods: {
        approve(restaurantName, customerName) {
            let dto = {
                "kupac" : customerName,
                "restoran" : restaurantName
            };
            
            axios.put("rest/restaurant/comment/approve", dto)
                .then(response => {
                    if (response.status == 200) {
                        alert("You successfully approved " + customerName + "'s comment!");
                        window.location.reload();
                    } else {
                        console.log(response);
                    }
                })
                .catch(error => {
                    console.log(error);
                });
        },
        reject(restaurantName, customerName) {
            let dto = {
                "kupac" : customerName,
                "restoran" : restaurantName
            };

            axios.put("rest/restaurant/comment/reject", dto)
                .then(response => {
                    if (response.status == 200) {
                        alert("You successfully rejected " + customerName + "'s comment!");
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
});