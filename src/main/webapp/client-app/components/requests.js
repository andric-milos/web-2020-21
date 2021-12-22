Vue.component("requests", {
    data: function() {
        return {
            requests: [],
            showTable: undefined
        }
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
                            <th scope="col">Deliverer</th>
                            <th scope="col">Request date</th>
                            <th scope="col">Order date</th>
                            <th scope="col">Price</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="r in requests">
                            <td> {{ r.id_porudzbine }} </td>
                            <td> {{ r.kupac }} </td>
                            <td> {{ r.dostavljac }} </td>
                            <td> {{ r.datumZahteva | transformMillisecondsToDate }} </td>
                            <td> {{ r.datumPorudzbine | transformMillisecondsToDate }} </td>
                            <td> {{ r.cena }} RSD</td>
                            <td>
                                
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div> `,
    created() {
        axios.get("rest/order/managersRequests")
            .then(response => {
                if (response.status == 200) {
                    this.requests = response.data;

                    if (this.requests.length > 0) {
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