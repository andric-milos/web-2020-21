Vue.component("navigation-bar", {
    data: function() {
        return {
            loggedIn: true
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
                    <a class="nav-link" href="#/">Profile</a>
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
                // metoda "rest/user/loggedIn" vraća true ili false

                if (response.data) {
                    this.loggedIn = true;
                } else {
                    this.loggedIn = false;
                }
            })
            .catch(error => {
                console.log(error);
            });
    }
});