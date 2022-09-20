# Faculty Course "Web Programming" project

2020-21 faculty course "Web Programming" project - food delivery **_single-page_** web application. <br>
Technologies: _**Java**_ & _**JAX-RS**_; _**Vue.js v2.6**_ & _**Bootstrap v5.0**_ & _**Leaflet**_. <br><br>
Short description and scope of functionalities:
* There are 4 types of roles - administrator, manager, deliverer and customer.
  * All users are loaded, from JSON files, at the initialization point of the application.
  * Administrator can only be created by manually adding him to the serialization JSON file.
  * Administrator creates managers and deliverers.
  * Customers create their accounts by registering using registration form.
  * All users have the option to log in and log out to the application.
* Administrator's job, besides adding new managers and deliverers, is to create new restaurants. 
* Customers can search and filter restaurants, order food from restaurants, cancel an already existing order, leave comments and rate restaurants. Customer also has the option to view all of his/her previously made orders.
* Managers delegate orders to deliverers, approve comments related to their restaurant, create, edit and delete articles within their restaurant. They can also view their restaurant-scoped orders.
* Deliverers can request to deliver an order and later change its status when the order is delivered. They can also view all orders in which they've been involved.
