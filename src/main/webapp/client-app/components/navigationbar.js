Vue.component("navigation-bar", {
    data: function() {
        return {
            loggedIn: false,
            userTypeHref: undefined,
            numberOfArticlesInShoppingCart: 0,
            showCartButton: undefined
        }
    },
    template: `
    <div>
		<nav class="navbar navbar-expand-lg navbar-dark bg-dark justify-content-between">
			<ul class="navbar-nav">
				<li class="nav-item">
					<a class="nav-link" href="#/">Home</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="#/login">Login</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="#/register">Register</a>
                </li>
            </ul>
            <ul class="navbar-nav" style="margin-right: 10px; margin-left: 10px;">
                <li class="nav-item" v-if="loggedIn">
                    <a id="navbar-profile" class="nav-link" v-bind:href="userTypeHref">Profile</a>
                </li>
                <li>
                    <!-- Modal -->
                    <shopping-cart-modal modalId="shoppingCartModal"></shopping-cart-modal>

                    <button 
                        v-if="showCartButton"
                        type="button" 
                        class="btn btn-secondary" 
                        style="margin-left: 10px;"
                        data-bs-toggle="tooltip"
                        data-bs-placement="bottom"
                        title="Shopping cart"
                        v-on:click="showShoppingCart"
                    >
                        Cart
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-cart3" viewBox="0 0 16 16">
                            <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .49.598l-1 5a.5.5 0 0 1-.465.401l-9.397.472L4.415 11H13a.5.5 0 0 1 0 1H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5zM3.102 4l.84 4.479 9.144-.459L13.89 4H3.102zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
                        </svg>
                        <span id="numberOfItemsInCart" class="badge bg-warning">{{ this.numberOfArticlesInShoppingCart }}</span>
                    </button>
                </li>
                <li class="nav-item" v-if="loggedIn">
					<button 
                        type="button" 
                        class="btn btn-primary" 
                        v-on:click.prevent="logout"
                        style="margin-left: 10px;"
                    >Logout</button>
				</li>
            </ul>
		</nav> 
    </div> `,
    methods: {
        logout() {
            axios.post("rest/user/logout")
                .then(response => {
                    if (response.status == 200) {
                        alert("You successfully logged out!");

                        if (window.location == "http://localhost:8080/web-2020-21/#/") {
                            window.location.reload();
                        } else {
                            this.$router.push("/");
                        }
                    } else {
                        console.log(response);
                    }
                })
                .catch(error => {
                    if (error.response.data == "NOT LOGGED IN") {
                        alert("You're not logged in!");
                    } else {
                        console.log(error.response.data);
                    }
                });
        },
        showShoppingCart() {
            $('#shoppingCartModal').modal('toggle');
        }
    },
    beforeMount() {
        axios.get("rest/user/loggedIn")
            .then(response => {
                if (response.data == "ADMINISTRATOR") {
                    this.loggedIn = true;
                    this.userTypeHref = "#/admin";
                } else if (response.data == "KUPAC") {
                    this.loggedIn = true;
                    this.userTypeHref = "#/customer";
                    this.showCartButton = true;
                } else if (response.data == "DOSTAVLJAC") {
                    this.loggedIn = true;
                    this.userTypeHref = "#/deliverer";
                } else if (response.data == "MENADZER") {
                    this.loggedIn = true;
                    this.userTypeHref = "#/manager";
                } else if (response.data == "NOT LOGGED IN") {
                    this.loggedIn = false;
                    this.userTypeHref = "#/";
                    this.showCartButton = true;
                } else {
                    console.log(response);
                    this.loggedIn = true;
                    this.userTypeHref = "#/";
                }
            })
            .catch(error => {
                console.log(error);
            });
    },
    created() {
        axios.get("rest/cart/numberOfArticles")
            .then(response => {
                if (response.status == 200) {
                    this.numberOfArticlesInShoppingCart = response.data;
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
            axios.get("rest/cart/numberOfArticles")
                .then(response => {
                    if (response.status == 200) {
                        this.numberOfArticlesInShoppingCart = response.data;
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