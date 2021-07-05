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
                alert("sve ok");
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