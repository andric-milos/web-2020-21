Vue.component("navigation-bar", {
    data: function() {
        return {
            loggedIn: false,
            userTypeHref: undefined
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
            <ul class="navbar-nav">
                <li class="nav-item" v-if="loggedIn">
                    <a id="navbar-profile" class="nav-link" v-bind:href="userTypeHref">Profile</a>
                </li>
                <li class="nav-item" v-if="loggedIn">
					<button 
                        type="button" 
                        class="btn btn-primary" 
                        v-on:click.prevent="logout" 
                        style="margin-right: 10px; margin-left: 10px;"
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
                } else if (response.data == "DOSTAVLJAC") {
                    this.loggedIn = true;
                    this.userTypeHref = "#/deliverer";
                } else if (response.data == "MENADZER") {
                    this.loggedIn = true;
                    this.userTypeHref = "#/manager";
                } else if (response.data == "NOT LOGGED IN") {
                    this.loggedIn = false;
                    this.userTypeHref = "#/";
                } else {
                    console.log(response);
                    this.loggedIn = true;
                    this.userTypeHref = "#/";
                }
            })
            .catch(error => {
                console.log(error);
            });
    }
});