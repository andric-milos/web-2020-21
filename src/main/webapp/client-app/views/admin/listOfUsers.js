Vue.component("admin-list-of-users", {
    data: function() {
        return {
            users: [],
            display: [],
            search: "",
            filter: "username"
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
                        <div class="row">
                            <div class="col-sm-8">
                                <input 
                                    type="text" 
                                    class="p-2" 
                                    placeholder="Search" 
                                    style="width: 100%;"
                                    v-model="search"
                                    id="search"
                                >
                            </div>
                            <div class="col-sm-2 p-0">
                                <label 
                                    class="pt-2 pl-2 pb-2" 
                                    style="width: 100%; text-align: right;"
                                >Filter by: </label>
                            </div>
                            <div class="col-sm-2">
                                <select name="filter" id="filter" class="p-2" v-model="filter">
                                    <option value="username">Username</option>
                                    <option value="firstname">Firstname</option>
                                    <option value="lastname">Lastname</option>
                                </select>
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
                                <tr v-for="user in display">
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
        axios.get("rest/user/all")
            .then(response => {
                if (response.status == 200) {
                    this.users = response.data;
                    this.display = response.data;
                } else {
                    console.log(response);
                }
            })
            .catch(error => {
                console.log(error);
            });
    },
    watch: {
        search: function(value) {
            if (value == "") {
                this.display = this.users;
            } else {
                this.display = [];

                if (this.filter == "username") {
                    for (let i = 0; i < this.users.length; i++) {
                        let usrnm = this.users[i].korisnickoIme;
                        if (usrnm.toLowerCase().startsWith(value.toLowerCase())) {
                            this.display.push(this.users[i]);
                        }
                    }
                }

                if (this.filter == "firstname") {
                    for (let i = 0; i < this.users.length; i++) {
                        let frstnm = this.users[i].ime;
                        if (frstnm.toLowerCase().startsWith(value.toLowerCase())) {
                            this.display.push(this.users[i]);
                        }
                    }
                }

                if (this.filter == "lastname") {
                    for (let i = 0; i < this.users.length; i++) {
                        let lstnm = this.users[i].prezime;
                        if (lstnm.toLowerCase().startsWith(value.toLowerCase())) {
                            this.display.push(this.users[i]);
                        }
                    }
                }
            }
        },
        filter: function(value) {
            this.search = "";
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