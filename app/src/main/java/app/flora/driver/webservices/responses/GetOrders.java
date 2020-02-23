package app.flora.driver.webservices.responses;

import java.util.List;

import app.flora.driver.webservices.models.Order;

public class GetOrders {

        private List<Order> orders;

        public List<Order> getOrders() {
            return orders;
        }

        public void setOrders(List<Order> orders) {
            this.orders = orders;
        }
 }
