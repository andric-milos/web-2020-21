Vue.component("admin-list-of-users", {
    data: function() {
        return {
            users: [],
            username: "",
            firstname: "",
            lastname: "",
            sortBy: "username",
            sortOrder: "asc",
            checkboxCustomerChecked: true,
            checkboxManagerChecked: true,
            checkboxDelivererChecked: true,
            checkboxAdministratorChecked: true
        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex" id="wrapper">
            <admin-sidebar></admin-sidebar>
            
            <!-- Page Content -->
            <div id="page-content-wrapper">
                <div class="d-flex p-2 justify-content-center">
                    <div class="d-flex flex-column" style="width: 100%;">
                        <div class="d-flex flex-row justify-content-between">
                            <input type="text" class="p-2" placeholder="Username" style="width: 33%;" v-model="username">
                            <input type="text" class="p-2" placeholder="Firstname" style="width: 33%;" v-model="firstname">
                            <input type="text" class="p-2" placeholder="Lastname" style="width: 33%;" v-model="lastname">
                        </div>
                        <div class="d-flex flex-row mt-2">
                            <label class="pt-2 pl-2 pb-2">Sort by: </label>
                            <select name="sortBy" id="sortBy" class="p-2 mx-2" v-model="sortBy">
                                <option value="username">Username</option>
                                <option value="firstname">Firstname</option>
                                <option value="lastname">Lastname</option>
                            </select>
                            <label class="pt-2 pl-2 pb-2">Sort order: </label>
                            <select name="sortOrder" id="sortOrder" class="p-2 mx-2" v-model="sortOrder">
                                <option value="asc">Ascending</option>
                                <option value="desc">Descending</option>
                            </select>
                        </div>
                        <div class="d-flex flex-row my-2">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="" id="checkboxCustomer" v-model="checkboxCustomerChecked" v-bind:checked="checkboxCustomerChecked">
                                <label class="form-check-label" for="checkboxCustomer">Customer</label>
                            </div>
                            <div class="form-check mx-2">
                                <input class="form-check-input" type="checkbox" value="" id="checkboxManager" v-model="checkboxManagerChecked" v-bind:checked="checkboxManagerChecked">
                                <label class="form-check-label" for="checkboxManager">Manager</label>
                            </div>
                            <div class="form-check mx-2">
                                <input class="form-check-input" type="checkbox" value="" id="checkboxDeliverer" v-model="checkboxDelivererChecked" v-bind:checked="checkboxDelivererChecked">
                                <label class="form-check-label" for="checkboxDeliverer">Deliverer</label>
                            </div>
                            <div class="form-check mx-2">
                                <input class="form-check-input" type="checkbox" value="" id="checkboxAdministrator" v-model="checkboxAdministratorChecked" v-bind:checked="checkboxAdministratorChecked">
                                <label class="form-check-label" for="checkboxAdministrator">Administrator</label>
                            </div>
                        </div>
                        <table class="table p-2 mt-2 ml-2 mr-4 table-dark table-striped table-hover">
                            <thead>
                                <tr>
                                    <th scope="col">Username</th>
                                    <th scope="col">Firstname</th>
                                    <th scope="col">Lastname</th>
                                    <th scope="col">Gender</th>
                                    <th scope="col">Date of birth</th>
                                    <th scope="col">Type of user</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="user in users">
                                    <td> {{ user.korisnickoIme }} </td>
                                    <td> {{ user.ime }} </td>
                                    <td> {{ user.prezime }} </td>
                                    <td> {{ user.pol | toEnglish }} </td>
                                    <td> {{ user.datumRodjenja }} </td>
                                    <td> {{ user.tipKorisnika | toEnglish }} </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>
    </div> `,
    mounted() {
        this.loadUsers();
    },
    methods: {
        typesOfUser() {
            const checkboxCustomer = document.getElementById("checkboxCustomer");
            const checkboxManager = document.getElementById("checkboxManager");
            const checkboxDeliverer = document.getElementById("checkboxDeliverer");
            const checkboxAdministrator = document.getElementById("checkboxAdministrator");

            let types = "";

            if (checkboxCustomer.checked) {
                if (types == "") {
                    types += "customer";
                } else {
                    types += ",customer";
                }
            }

            if (checkboxManager.checked) {
                if (types == "") {
                    types += "manager";
                } else {
                    types += ",manager";
                }
            }

            if (checkboxDeliverer.checked) {
                if (types == "") {
                    types += "deliverer";
                } else {
                    types += ",deliverer";
                }
            }

            if (checkboxAdministrator.checked) {
                if (types == "") {
                    types += "administrator";
                } else {
                    types += ",administrator";
                }
            }

            return types;
        },
        loadUsers() {
            let types = this.typesOfUser();

            axios.get("rest/user/search?firstname=" + this.firstname + "&lastname=" + this.lastname + "&username=" + this.username + "&sortBy=" + this.sortBy + "&sortOrder=" + this.sortOrder + "&typeOfUser=" + types)
                .then(response => {
                    if (response.status == 200) {
                        this.users = response.data;
                    } else {
                        console.log(response);
                    }
                })
                .catch(error => {
                    console.log(error);
                });
        }
    },
    watch: {
        username() {
            this.loadUsers();
        },
        firstname() {
            this.loadUsers();
        },
        lastname() {
            this.loadUsers();
        },
        sortBy() {
            this.loadUsers();
        },
        sortOrder() {
            this.loadUsers();
        },
        checkboxCustomerChecked() {
            this.loadUsers();
        },
        checkboxManagerChecked() {
            this.loadUsers();
        },
        checkboxDelivererChecked() {
            this.loadUsers();
        },
        checkboxAdministratorChecked() {
            this.loadUsers();
        }
    },
    filters: {
        toEnglish: function(value) {
            if (!value)
                return "";

            if (value == "KUPAC")
                return "customer";
            if (value == "ADMINISTRATOR")
                return "admin";
            if (value == "MENADZER")
                return "manager";
            if (value == "DOSTAVLJAC")
                return "deliverer";

            if (value == "MUSKO")
                return "male";
            if (value == "ZENSKO")
                return "female";
        }
    }
});