Vue.component("user-info", {
    data: function() {
        return {
            username: undefined,
            firstname: undefined,
            lastname:undefined,
            gender: undefined,
            date: undefined,

            firstname_backup: undefined,
            lastname_backup: undefined,
            gender_backup: undefined,
            date_backup: undefined
        }
    },
    template: `
    <div id="page-content-wrapper">
        <div class="d-flex p-2 justify-content-center">
            <div class="d-flex flex-column col-sm-4">
                <h1 class="p-2">Profile</h1>

                <label class="p-2"><b>Username</b></label>
                <input type="text" class="p-2" id="username" v-model="username" disabled>

                <label class="p-2"><b>First name</b></label>
                <input type="text" class="p-2" id="firstname" v-model="firstname" disabled>
                <label id="firstnameError" class="p-2 text-danger"></label>

                <label class="p-2"><b>Last name</b></label>
                <input type="text" class="p-2" id="lastname" v-model="lastname" disabled>
                <label id="lastnameError" class="p-2 text-danger"></label>

                <label class="p-2"><b>Date of birth</b></label>
                <input type="date" class="p-2" id="date" v-model="date" disabled>
                <label id="dateError" class="p-2 text-danger"></label>

                <label class="p-2"><b>Gender</b></label>
                <select name="gender" class="p-2" id="gender" v-model="gender" disabled>
                    <option value="MUSKO">Male</option>
                    <option value="ZENSKO">Female</option>
                </select>
                <label id="genderError" class="p-2 text-danger"></label>

                <div id="div-submit-cancel" class="row py-2" hidden>
                    <div class="col">
                        <button 
                            class="btn btn-primary" 
                            type="button" 
                            style="width:100%;" 
                            v-on:click="update"
                        >Submit</button>
                    </div>
                    <div class="col">
                        <button 
                            class="btn btn-primary" 
                            type="button" 
                            style="width:100%;" 
                            v-on:click="cancel"
                        >Cancel</button>
                    </div>
                </div>
                <div id="div-edit" class="row py-2">
                    <div class="col">
                        <button 
                            class="btn btn-primary" 
                            type="button" 
                            style="width:100%;" 
                            v-on:click="edit"
                        >Edit</button>
                    </div>
                </div>
            </div>
        </div>
    </div> `,
    methods: {
        edit() {
            document.getElementById("firstname").removeAttribute("disabled");
            document.getElementById("lastname").removeAttribute("disabled");
            document.getElementById("gender").removeAttribute("disabled");
            document.getElementById("date").removeAttribute("disabled");
            document.getElementById("div-edit").setAttribute("hidden", true);
            document.getElementById("div-submit-cancel").removeAttribute("hidden");
        },
        update() {
            if (this.validation()) {
                var dto = {
                    "korisnickoIme" : this.username,
                    "ime" : this.firstname,
                    "prezime" : this.lastname,
                    "pol" : this.gender,
                    "datumRodjenja" : this.date
                };

                axios.put("rest/user/update", dto)
                    .then(response => {
                        if (response.status == 200) {
                            alert("You successfully updated your info!");
                            window.location.reload();
                        }
                    })
                    .catch(error => {
                        if (error.response.data == "NOT LOGGED IN") {
                            alert("You're not logged in!");
                            this.$router.push("/login");
                        } else if (error.response.data == "EMPTY FIELDS") {
                            alert("Input fields cannot be empty!");
                        } else if (error.response.data == "INVALID GENDER") {
                            alert("Invalid gender value!");
                        } else if (error.response.data == "INVALID DATE FORMAT") {
                            alert("Invalid date format!");
                        } else if (error.response.data == "USER DOES NOT EXIST") {
                            alert("User with username \"" + this.username + "\" doesn't exist!");
                        } else if (error.response.data == "CANNOT CHANGE OTHERS' DATA") {
                            alert("You are not authorized to change info of other users!");
                        } else {
                            console.log(error);
                        }
                    });
            }
        },
        cancel() {
            this.firstname = this.firstname_backup;
            this.lastname = this.lastname_backup;
            this.gender = this.gender_backup;
            this.date = this.date_backup;

            document.getElementById("firstname").setAttribute("disabled", true);
            document.getElementById("lastname").setAttribute("disabled", true);
            document.getElementById("gender").setAttribute("disabled", true);
            document.getElementById("date").setAttribute("disabled", true);
            document.getElementById("div-edit").removeAttribute("hidden");
            document.getElementById("div-submit-cancel").setAttribute("hidden", true);
        },
        validation() {
            let flag = 0;

            if (!this.username) {
                ++flag;
                alert("You're not logged in!");
                this.$router.push("/login");
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

            if (flag)
                return false;
            else
                return true;
        }
    },
    mounted() {
        axios.get("rest/user/getLoggedInUserData")
            .then(response => {
                if (response.data == "NOT LOGGED IN") {
                    console.log("Couldn't fetch user's data because user is not logged in!");
                } else {
                    this.username = response.data.korisnickoIme;
                    this.firstname = response.data.ime;
                    this.lastname = response.data.prezime;
                    this.gender = response.data.pol;
                    this.date = response.data.datumRodjenja;

                    this.firstname_backup = response.data.ime;
                    this.lastname_backup = response.data.prezime;
                    this.gender_backup = response.data.pol;
                    this.date_backup = response.data.datumRodjenja;
                }
            })
            .catch(error => {
                console.log(error);
            });
    }
});