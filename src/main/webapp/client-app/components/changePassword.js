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

                <label class="p-2"><b>Enter your new password</b></label>
                <input type="password" class="p-2" id="new_password" v-model="new_password">

                <label class="p-2"><b>Confirm password</b></label>
                <input type="password" class="p-2" id="confirm_password" v-model="confirm_password">

                <br>
                <button type="submit" class="btn btn-primary" v-on:click.prevent="change">Confirm</button>
            </form>
        </div>
    </div> `,
    methods: {
        change() {

        },
        validation() {

        }
    }
});