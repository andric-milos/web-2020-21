Vue.component("add-new-manager-modal", {
    data: function() {
        return {
            username: undefined,
            firstname: undefined,
            lastname: undefined,
            gender: undefined,
            date: undefined,
            password: undefined,
            confirm_password: undefined
        }
    },
    props: {
        modalId : String
    },
    template: `
    <div class="modal fade" v-bind:id="modalId" tabindex="-1" role="dialog" aria-labelledby="addNewManagerModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addNewManagerModalLabel"><b>Add new manager</b></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="d-flex flex-column">
                        <label class="p-2"><b>Username</b></label>
                        <input type="text" class="p-2" id="username" v-model="username">
                        <label id="usernameError" class="p-2 text-danger"></label>

                        <label class="p-2"><b>First name</b></label>
                        <input type="text" class="p-2" id="firstname" v-model="firstname">
                        <label id="firstnameError" class="p-2 text-danger"></label>
                        
                        <label class="p-2"><b>Last name</b></label>
                        <input type="text" class="p-2" id="lastname" v-model="lastname">
                        <label id="lastnameError" class="p-2 text-danger"></label>
                        
                        <label class="p-2"><b>Gender</b></label>
                        <select name="gender" class="p-2" id="gender" v-model="gender">
                            <option value="MUSKO">Male</option>
                            <option value="ZENSKO">Female</option>
                        </select>
                        <label id="genderError" class="p-2 text-danger"></label>

                        <label class="p-2"><b>Date of birth</b></label>
                        <input type="date" class="p-2" id="date" v-model="date">
                        <label id="dateError" class="p-2 text-danger"></label>
                        
                        <label class="p-2" for="password"><b>Password</b></label>
                        <input type="password" class="p-2" id="password" v-model="password" placeholder="Password">
                        <label id="passwordError" class="p-2 text-danger"></label>
                        
                        <label class="p-2" for="password"><b>Confirm password</b></label>
                        <input type="password" class="p-2" id="confirm_password" v-model="confirm_password" placeholder="Password">
                        <label id="confirmPasswordError" class="p-2 text-danger"></label>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" v-on:click="addNewManager">Submit</button>
                </div>
            </div>
        </div>
    </div> `,
    methods: {
        addNewManager() {
            if (this.validation()) {
                let dto = {
                    "korisnickoIme" : this.username,
                    "lozinka" : this.password,
                    "potvrda_lozinke" : this.confirm_password,
                    "ime" : this.firstname,
                    "prezime" : this.lastname,
                    "pol" : this.gender,
                    "datumRodjenja" : this.date
                };

                axios.post("rest/user/newManager", dto)
                    .then(response => {
                        if (response.status == 200) {
                            alert("You successfully added " + this.username + " as new manager!");
                            window.location.reload();
                        }
                    })
                    .catch(error => {
                        if (error.response.data == "NOT LOGGED IN") {
                            alert("You're not logged in!");
                            this.$router.push("/login");
                        } else if (error.response.data == "NOT ADMINISTRATOR") {
                            alert("You're not authorized to add new manager!");
                        } else if (error.response.data == "EMPTY FIELDS") {
                            alert("Input fields cannot be empty!");
                        } else if (error.response.data == "INVALID GENDER") {
                            alert("Invalid gender value!");
                        } else if (error.response.data == "INVALID DATE FORMAT") {
                            alert("Invalid date format!");
                        } else if (error.response.data == "USERNAME ALREADY TAKEN") {
                            alert("Username \"" + this.username + "\" is already taken!");
                        } else if (error.response.data == "PASSWORDS DO NOT MATCH") {
                            alert("Password doesn't match with the confirm password you entered!");
                        } else {
                            console.log(error);
                        }
                    });
            }
        },
        validation() {
            let flag = 0;

            if (!this.username) {
                document.getElementById("usernameError").innerHTML = "Username input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("usernameError").innerHTML = "";
            }

            if (!this.password) {
                document.getElementById("passwordError").innerHTML = "Password input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("passwordError").innerHTML = "";
            }

            if (!this.firstname) {
                document.getElementById("firstnameError").innerHTML = "Firstname input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("firstnameError").innerHTML = "";
            }

            if (!this.lastname) {
                document.getElementById("lastnameError").innerHTML = "Lastname input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("lastnameError").innerHTML = "";
            }

            if (!this.gender) {
                document.getElementById("genderError").innerHTML = "Gender input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("genderError").innerHTML = "";
            }

            if (!this.date) {
                document.getElementById("dateError").innerHTML = "Date input field cannot be empty!";
                ++flag;
            } else {
                document.getElementById("dateError").innerHTML = "";
            }

            if (!this.confirm_password) {
                document.getElementById("confirmPasswordError").innerHTML = "Confirm password input field cannot be empty!";
                ++flag;
            } else if (this.password && (this.password != this.confirm_password)) {
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