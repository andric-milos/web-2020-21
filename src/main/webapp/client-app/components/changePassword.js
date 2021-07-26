Vue.component("change-password", {
    data: function() {
        return {
            password: undefined,
            new_password: undefined,
            confirm_password: undefined
        }
    },
    template: `
    <div id="page-content-wrapper">
        <div class="d-flex p-2 justify-content-center">
            <form accept-charset="UTF-8" class="d-flex flex-column col-sm-4">
                <h1 class="p-2">Change password</h1>

                <label class="p-2"><b>Enter your current password</b></label>
                <input type="password" class="p-2" id="password" v-model="password">
                <label id="passwordError" class="p-2 text-danger"></label>

                <label class="p-2"><b>Enter your new password</b></label>
                <input type="password" class="p-2" id="new_password" v-model="new_password">
                <label id="newPasswordError" class="p-2 text-danger"></label>

                <label class="p-2"><b>Confirm password</b></label>
                <input type="password" class="p-2" id="confirm_password" v-model="confirm_password">
                <label id="confirmPasswordError" class="p-2 text-danger"></label>

                <br>
                <button type="submit" class="btn btn-primary" v-on:click.prevent="change">Confirm</button>
            </form>
        </div>
    </div> `,
    methods: {
        change() {
            if (this.validation()) {
                var dto = {
                    "lozinka" : this.password,
                    "nova_lozinka" : this.new_password,
                    "potvrda_nove_lozinke" : this.confirm_password
                };

                axios.put("rest/user/changePassword", dto)
                    .then(response => {
                        if (response.status == 200) {
                            alert("You successfully changed your password!");
                            window.location.reload();
                        }
                    })
                    .catch(error => {
                        if (error.response.data == "NOT LOGGED IN") {
                            alert("You're not logged in!");
                            this.$router.push("/login");
                        } else if (error.response.data == "EMPTY FIELDS") {
                            alert("Input fields cannot be empty!");
                        } else if (error.response.data == "WRONG PASSWORD") {
                            alert("Password you entered is incorrect!");
                        } else if (error.response.data == "PASSWORDS DO NOT MATCH") {
                            alert("New password doesn't match with the confirm password you entered!");
                        } else if (error.response.data == "SOMETHING WENT WRONG") {
                            alert("Something went wrong!");
                        } else {
                            console.log(error);
                        }
                    });
            }
        },
        validation() {
            let flag = 0;

            if (!this.password) {
                document.getElementById("passwordError").innerHTML = "Password input field cannot be empty!";

                ++flag;
            } else {
                document.getElementById("passwordError").innerHTML = "";
            }

            if (!this.new_password) {
                document.getElementById("newPasswordError").innerHTML = "New password input field cannot be empty!";

                ++flag;
            } else {
                document.getElementById("newPasswordError").innerHTML = "";
            }

            if (!this.confirm_password) {
                document.getElementById("confirmPasswordError").innerHTML = "Confirm password input field cannot be empty!";

                ++flag;
            } else if (this.new_password && (this.new_password != this.confirm_password)) {
                document.getElementById("confirmPasswordError").innerHTML = "Passwords do not match!";
                
                ++flag;
            } else {
                document.getElementById("confirmPasswordError").innerHTML = "";
            }

            if (flag)
                return false;
            else
                return true;
        }
    }
});