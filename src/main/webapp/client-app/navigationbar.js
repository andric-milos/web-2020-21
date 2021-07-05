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
            alert("not implemented yet!");
        }
    },
    beforeMount() {
        
    }
});