Vue.component("registration-page", {
    data: function() {
        return {
            username: undefined,
            password: undefined,
            firstname: undefined,
            lastname: undefined,
            gender: undefined,
            date: undefined,
            confirm_password: undefined
        }
    },
    template: `
    <div>
        <navigation-bar></navigation-bar>

        <div class="d-flex p-2 justify-content-center">
            <form accept-charset="UTF-8" class="d-flex flex-column col-sm-4">
                <h1 class="p-2">Register</h1>

                <label class="p-2"><b>Username</b></label>
                <input type="text" class="p-2" id="username" v-model="username">
                
                <label class="p-2"><b>First name</b></label>
                <input type="text" class="p-2" id="firstname" v-model="firstname">
                
                <label class="p-2"><b>Last name</b></label>
                <input type="text" class="p-2" id="lastname" v-model="lastname">
                
                <label class="p-2"><b>Gender</b></label>
                <select name="gender" class="p-2" id="gender" v-model="gender">
                    <option value="MUSKO">Male</option>
                    <option value="ZENSKO">Female</option>
                </select>

                <label class="p-2"><b>Date of birth</b></label>
                <input type="date" class="p-2" id="date" v-model="date">
                
                <label class="p-2" for="password"><b>Password</b></label>
                <input type="password" class="p-2" id="password" v-model="password" placeholder="Password">
                
                <label class="p-2" for="password"><b>Confirm password</b></label>
                <input type="password" class="p-2" id="confirm_password" v-model="confirm_password" placeholder="Password">
                
                <br>
                <button type="submit" class="btn btn-primary" v-on:click.prevent="register">Confirm</button>
            </form>
        </div>
    </div> `
});