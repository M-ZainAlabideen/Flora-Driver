package app.flora.driver.webservices.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import app.flora.driver.webservices.models.Product;

public class GetProducts {
    @SerializedName("order_items")
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
