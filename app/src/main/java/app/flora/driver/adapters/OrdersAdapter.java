package app.flora.driver.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.flora.driver.R;
import app.flora.driver.classes.Constants;
import app.flora.driver.classes.GlobalFunctions;
import app.flora.driver.classes.SessionManager;
import app.flora.driver.webservices.models.Order;
import butterknife.BindView;
import butterknife.ButterKnife;


public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.viewHolder> {
    Context context;
    SessionManager sessionManager;
    ArrayList<Order> ordersList;
    boolean isUpcoming;
    OnItemClickListener listener;

    public OrdersAdapter(Context context, ArrayList<Order> ordersList, boolean isUpcoming, OnItemClickListener listener) {
        this.context = context;
        this.ordersList = ordersList;
        this.isUpcoming = isUpcoming;
        this.listener = listener;
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_order_tv_date)
        TextView date;
        @BindView(R.id.item_order_tv_orderNumber)
        TextView orderNumber;
        @BindView(R.id.item_order_tv_clientAddress)
        TextView clientAddress;
        //        @BindView(R.id.item_order_tv_vendorAddress)
//        TextView vendorAddress;
        @BindView(R.id.item_order_tv_orderStatus)
        TextView orderStatus;
        @BindView(R.id.item_order_tv_acceptOrder)
        TextView acceptOrder;
        @BindView(R.id.item_order_iv_arrow)
        ImageView arrow;
        @BindView(R.id.item_order_v_details)
        View details;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    @NonNull
    @Override
    public OrdersAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_order, viewGroup, false);
        sessionManager = new SessionManager(context);
        return new OrdersAdapter.viewHolder(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.viewHolder viewHolder, final int position) {
            if (ordersList.get(position).getDeliverer_id() == 0 && !(ordersList.get(position).shipping_status.equalsIgnoreCase(Constants.DELIVERED))) {
                viewHolder.acceptOrder.setVisibility(View.VISIBLE);
            } else {
                viewHolder.acceptOrder.setVisibility(View.GONE);
            }


        viewHolder.orderNumber.setText("#" + String.valueOf(ordersList.get(position).getId()));
        viewHolder.date.setText(GlobalFunctions.formatDateAndTime(ordersList.get(position).getCreated_on_utc()));
        if (ordersList.get(position).getShipping_address() != null) {
            viewHolder.clientAddress.setText(setAddress(ordersList.get(position).getShipping_address().getBlock(),
                    ordersList.get(position).getShipping_address().getAddress1(),
                    ordersList.get(position).getShipping_address().getProvince(),
                    ordersList.get(position).getShipping_address().getCountry(),
                    ordersList.get(position).getShipping_address().getAddress2()));
        } else {
            viewHolder.clientAddress.setText("");
        }
        viewHolder.orderStatus.setText(ordersList.get(position).shipping_status);
        if (sessionManager.getUserLanguage().equals(Constants.AR)) {
            viewHolder.arrow.setRotation(180);
        }
        viewHolder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.detailsClick(position);
            }
        });
        viewHolder.orderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeStatusClick(position, ordersList.get(position).shipping_status);
            }
        });
        viewHolder.acceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.acceptOrderClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public interface OnItemClickListener {
        public void detailsClick(int position);

        public void changeStatusClick(int position, String statusWord);

        public void acceptOrderClick(int position);
    }

    private String setAddress(String block, String street, String area, String country, String extraDetails) {
        String address = block + ", " + street + ", " + area + ", " + country + ", " + extraDetails;
        if (address != null)
            return address.replace("null, ", "").replace("null", "").replace(" ,null", "");
        else
            return "";
    }
}

