package app.flora.driver.webservices;

import java.util.ArrayList;

import app.flora.driver.classes.Constants;
import app.flora.driver.webservices.requests.UpdateOrderRequest;
import app.flora.driver.webservices.responses.GetCustomer;
import app.flora.driver.webservices.responses.GetOrders;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebServices {
    @GET("customers/login")
    Call<ResponseBody> LOGIN_CALL(@Header(Constants.ACCEPTED_LANGUAGE) String language,
                                  @Header(Constants.AUTHORIZATION) String Authorization,
                                  @Query("username_or_email") String usernameOrEmail,
                                  @Query("password") String password);


    @PUT("orders/{id}")
    Call<ResponseBody> UPDATE_ORDER_CALL(@Header(Constants.ACCEPTED_LANGUAGE) String language,
                                         @Header(Constants.AUTHORIZATION) String Authorization,
                                         @Path("id") String id,
                                         @Body UpdateOrderRequest request);

    @GET("orders")
    Call<ResponseBody> GET_ORDERS_CALL(@Header(Constants.ACCEPTED_LANGUAGE) String language,
                                       @Header(Constants.AUTHORIZATION) String Authorization,
                                       @Query("deliverer_id") String Id,
                                       @Query("page") int page,
                                       @Query("limit") int limit,
                                       @Query("order_number") String orderNumber,
                                       @Query("shipping_status") String shippingStatus,
                                       @Query("created_at_min") String createdAtMin,
                                       @Query("created_at_max") String createdAtMax);


    @GET("orders/{order_id}/items")
    Call<ResponseBody> GET_ORDER_ITEMS(@Header(Constants.ACCEPTED_LANGUAGE) String language,
                                       @Header(Constants.AUTHORIZATION) String Authorization,
                                       @Path("order_id") String orderId);


    @FormUrlEncoded
    @POST("customers/recoverypassword")
    Call<ResponseBody> FORGET_PASSWORD_CALL(
            @Header(Constants.ACCEPTED_LANGUAGE) String language,
            @Header(Constants.AUTHORIZATION) String Authorization,
            @Field("email") String email);
}
