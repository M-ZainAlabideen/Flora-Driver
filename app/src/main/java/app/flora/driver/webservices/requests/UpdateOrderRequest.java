package app.flora.driver.webservices.requests;

import com.google.gson.annotations.SerializedName;

public class UpdateOrderRequest {
    @SerializedName("order")
    public UpdateOrderRequest_ updateOrderRequest_ = new UpdateOrderRequest_();

    public class UpdateOrderRequest_ {
        @SerializedName("deliverer_id")
        public String userId;

        @SerializedName("shipping_status")
        public String status;
    }
}
