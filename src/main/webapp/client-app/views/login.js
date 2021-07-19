Vue.component("login-page", {
    data: function() {
        return {
            username: undefined,
            password: undefined
        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex p-2 justify-content-center">
            <form accept-charset="UTF-8" class="d-flex flex-column col-sm-4">
                <h1 class="p-2">Login</h1>

                <label class="p-2"><b>Username</b></label>
                <input type="text" class="p-2" id="username" v-model="username">
                <label id="usernameError" class="p-2 text-danger"></label>

                <label class="p-2" for="password"><b>Password</b></label>
                <input type="password" class="p-2" id="password" v-model="password" placeholder="Password">
                <label id="passwordError" class="p-2 text-danger"></label>
                
                <br>
                <button type="submit" class="btn btn-primary" v-on:click.prevent="login">Confirm</button>
            </form>
        </div>
    </div> `,
    methods: {
        login() {
            if (this.validation()) {
                var user = {
                    "korisnickoIme" : this.username,
                    "lozinka" : this.password
                };

                axios.post("rest/user/login", user)
                    .then(response => {
                        if (response.status == 200) {
                            // dodati mozda ime, prezime i tako neke stvari u localStorage?
                            // inace treba redirekcija na korisnikov profil, za sada ce biti na /home
                            this.$router.push("/");
                            // window.location.href = "#/home";
                        } else {
                            console.log(response);
                        }
                    })
                    .catch(error => {
                        if (error.response.data == "ALREADY LOGGED IN") {
                            alert("You're already logged in!");
                            // redirekcija na home
                        } else if (error.response.data == "INVALID") {
                            alert("Input fields cannot be empty!");
                        } else if (error.response.data == "USER DOES NOT EXIST") {
                            alert("User with username " + this.username + " doesn't exist!");
                        } else if (error.response.data == "WRONG PASSWORD") {
                            alert("Password you entered is incorrect!");
                        } else {
                            console.log(error);
                        }
                    });
            } else {
                alert("nije ok");
            }
        },
        validation() {
            let flag = 0;

            if (this.username == undefined || this.username == ""){
                document.getElementById("usernameError").innerHTML = "Username input field cannot be empty!";

                ++flag;
            } else {
                document.getElementById("usernameError").innerHTML = "";
            }

            if (this.password == undefined || this.password == ""){
                document.getElementById("passwordError").innerHTML = "Password input field cannot be empty!";

                ++flag;
            } else {
                document.getElementById("passwordError").innerHTML = "";
            }

            if (flag)
                return false;
            else
                return true;
        }
    }
});