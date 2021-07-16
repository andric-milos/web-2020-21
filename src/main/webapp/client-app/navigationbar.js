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
                        // kada se izlogujemo, trebalo bi da nestane logout dugme iz navbara i obrnuto ...
                        alert("You successfully logged out!");
                        this.$router.push("/");
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
        
    }
});