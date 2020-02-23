package app.flora.driver.webservices.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import app.flora.driver.webservices.models.Customer;
import app.flora.driver.webservices.models.Errors_;

public class GetCustomer {

    @SerializedName("customers")
    private List<Customer> customers = null;

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    @SerializedName("customer")
    private Customer customer = null;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @SerializedName("errors")
    private Errors_ errors;

    public Errors_ getErrors() {
        return errors;
    }

    public void setErrors(Errors_ errors) {
        this.errors = errors;
    }
}
