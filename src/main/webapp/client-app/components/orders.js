Vue.component("orders", {
    data: function() {
        return {
            orders: []
        }
    },
    template: `
    <div id="page-content-wrapper">
        <div class="d-flex p-2 justify-content-center">
            <div class="d-flex flex-column" style="width: 100%;">
                <table class="table p-2 mt-2 ml-2 mr-4 table-dark table-striped table-hover">
                    <thead>
                        <tr>
                            <th scope="col">Id</th>
                            <th scope="col">Date</th>
                            <th scope="col">Status</th>
                            <th scope="col">Restaurant</th>
                            <th scope="col">Price</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="order in orders">
                            <td> {{ order.id }} </td>
                            <td> {{ order.vremePorudzbine | transformMillisecondsToDate }} </td>
                            <td> {{ order.status }} </td>
                            <td> {{ order.restoran }} </td>
                            <td> {{ order.cena }} RSD</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div> `,
    created() {
        axios.get("rest/order/customer")
            .then(response => {
                if (response.status == 200) {
                    this.orders = response.data;
                } else {
                    console.log(response);
                }
            })
            .catch(error => {
                console.log(error);
            });
    },
    filters: {
        transformMillisecondsToDate: function(value) {
            if (!value) {
                return '';
            }

            date = new Date(value);

            return date.getDate() + "-" + date.getMonth() + "-" + date.getFullYear() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
        }
    }
});