Vue.component("orders", {
    data: function() {
        return {
            orders: [],
            showTable: undefined
        }
    },
    props: {
        typeOfUser: String
    },
    template: `
    <div id="page-content-wrapper">
        <div class="d-flex p-2 justify-content-center">
            <div v-if="showTable" class="d-flex flex-column" style="width: 100%;">
                <table class="table p-2 mt-2 ml-2 mr-4 table-dark table-striped table-hover">
                    <thead>
                        <tr>
                            <th scope="col">Id</th>
                            <th scope="col">Customer</th>
                            <th scope="col">Date</th>
                            <th scope="col">Status</th>
                            <th scope="col">Restaurant</th>
                            <th scope="col">Price</th>
                            <th scope="col"></th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="order in orders">
                            <td> {{ order.id }} </td>
                            <td> {{ order.kupac }} </td>
                            <td> {{ order.vremePorudzbine | transformMillisecondsToDate }} </td>
                            <td> {{ order.status }} </td>
                            <td> {{ order.restoran }} </td>
                            <td> {{ order.cena }} RSD</td>
                            <td><button v-if="order.status == 'OBRADA' && typeOfUser == 'customer'" type="button" class="btn btn-primary py-0" v-on:click="cancelOrder(order.id)">Cancel</button></td>
                            <td><button v-if="order.status == 'OBRADA' && typeOfUser == 'manager'" type="button" class="btn btn-primary py-0">Change to "In process"</button></td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div v-else class="d-flex flex-column">
                <h1>No orders.</h1>
            </div>
        </div>
    </div> `,
    created() {
        if (this.typeOfUser == "customer") {
            axios.get("rest/order/customer")
                .then(response => {
                    if (response.status == 200) {
                        this.orders = response.data;

                        if (this.orders.length > 0) {
                            this.showTable = true;
                        } else {
                            this.showTable = false;
                        } 
                    } else {
                        console.log(response);
                    }
                })
                .catch(error => {
                    console.log(error);
                });
        } else if (this.typeOfUser == "manager") {
            axios.get("rest/order/manager")
                .then(response => {
                    if (response.status == 200) {
                        this.orders = response.data;

                        if (this.orders.length > 0) {
                            this.showTable = true;
                        } else {
                            this.showTable = false;
                        } 
                    } else {
                        console.log(response);
                    }
                })
                .catch(error => {
                    console.log(error);
                });
        }
        
    },
    filters: {
        transformMillisecondsToDate: function(value) {
            if (!value) {
                return '';
            }

            date = new Date(value);

            return date.getDate() + "-" + date.getMonth() + "-" + date.getFullYear() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
        }
    },
    methods: {
        cancelOrder(id) {
            axios.put("rest/order/cancel/" + id)
                .then(response => {
                    if (response.status == 200) {
                        alert ("You successfully canceled order with the id: " + id);
                        window.location.reload();
                    } else {
                        console.log(response);
                    }
                })
                .catch(error => {
                    console.log(error);
                });
        }
    }
});