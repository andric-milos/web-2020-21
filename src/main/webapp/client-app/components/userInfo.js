Vue.component("user-info", {
    data: function() {
        return {
            username: undefined,
            firstname: undefined,
            lastname:undefined,
            gender: undefined,

            firstname_backup: undefined,
            lastname_backup: undefined,
            gender_backup: undefined
        }
    },
    template: `
    <div id="page-content-wrapper">
        <div class="d-flex p-2 justify-content-center">
            <div class="d-flex flex-column col-sm-4">
                <h1 class="p-2">Profile</h1>

                <label class="p-2"><b>Username</b></label>
                <input type="text" class="p-2" id="username" v-model="username" disabled>

                <label class="p-2"><b>First Name</b></label>
                <input type="text" class="p-2" id="firstname" v-model="firstname" disabled>

                <label class="p-2"><b>Last name</b></label>
                <input type="text" class="p-2" id="lastname" v-model="lastname" disabled>

                <label class="p-2"><b>Gender</b></label>
                <select name="gender" class="p-2" id="gender" v-model="gender" disabled>
                    <option value="MUSKO">Male</option>
                    <option value="ZENSKO">Female</option>
                </select>

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
            document.getElementById("div-edit").setAttribute("hidden", true);
            document.getElementById("div-submit-cancel").removeAttribute("hidden");
        },
        update() {
            document.getElementById("firstname").setAttribute("disabled", true);
            document.getElementById("lastname").setAttribute("disabled", true);
            document.getElementById("gender").setAttribute("disabled", true);
            document.getElementById("div-edit").removeAttribute("hidden");
            document.getElementById("div-submit-cancel").setAttribute("hidden", true);  
        },
        cancel() {
            this.firstname = this.firstname_backup;
            this.lastname = this.lastname_backup;
            this.gender = this.gender_backup;

            document.getElementById("firstname").setAttribute("disabled", true);
            document.getElementById("lastname").setAttribute("disabled", true);
            document.getElementById("gender").setAttribute("disabled", true);
            document.getElementById("div-edit").removeAttribute("hidden");
            document.getElementById("div-submit-cancel").setAttribute("hidden", true);
        },
        validation() {
            
        }
    }
});