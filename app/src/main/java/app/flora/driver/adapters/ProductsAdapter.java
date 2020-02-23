package app.flora.driver.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import app.flora.driver.R;
import app.flora.driver.classes.Constants;
import app.flora.driver.classes.FixControl;
import app.flora.driver.classes.GlobalFunctions;
import app.flora.driver.classes.Navigator;
import app.flora.driver.classes.SessionManager;
import app.flora.driver.fragments.ProductDetailsFragment;
import app.flora.driver.webservices.models.Product;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.viewHolder> {
    Context context;
    SessionManager sessionManager;
    ArrayList<Product> productsList;
    ProductsAdapter.OnItemClickListener listener;

    public ProductsAdapter(Context context, ArrayList<Product> productsList, ProductsAdapter.OnItemClickListener listener) {
        this.context = context;
        this.productsList = productsList;
        this.listener = listener;
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_product_riv_productImg)
        RoundedImageView productImg;
        @BindView(R.id.item_product_tv_date)
        TextView date;
        @BindView(R.id.item_product_tv_pieces)
        TextView pieces;
        @BindView(R.id.item_product_tv_price)
        TextView price;
        @BindView(R.id.item_product_tv_productName)
        TextView productName;
        @BindView(R.id.item_product_iv_arrow)
        ImageView arrow;
        @BindView(R.id.item_product_v_details)
        View details;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    @NonNull
    @Override
    public ProductsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_product, viewGroup, false);
        sessionManager = new SessionManager(context);
        return new ProductsAdapter.viewHolder(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.viewHolder viewHolder, final int position) {
        int Width = FixControl.getImageWidth(context, R.drawable.list_noimg);
        int Height = FixControl.getImageHeight(context, R.drawable.list_noimg);
        viewHolder.productImg.getLayoutParams().height = Height;
        viewHolder.productImg.getLayoutParams().width = Width;
        if (productsList.get(position).productDetails.images.get(0).src != null
                && !productsList.get(position).productDetails.images.get(0).src.matches("")
                && !productsList.get(position).productDetails.images.get(0).src.isEmpty()) {
            Glide.with(context.getApplicationContext()).load(productsList.get(position).productDetails.images.get(0).src)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.list_noimg))
                    .into(viewHolder.productImg);

            viewHolder.date.setText(GlobalFunctions.formatDate(productsList.get(position).productDetails.createdDateUTC));
            viewHolder.price.setText(productsList.get(position).productDetails.formattedPrice);
            viewHolder.pieces.setText(productsList.get(position).quantity + " " +context.getString(R.string.pieces));
            String name = productsList.get(position).productDetails.localizedNames.get(0).localizedName;
            if (name.length() > 25)
                viewHolder.productName.setText(name.substring(0, 24) + "...");
            else
                viewHolder.productName.setText(name);

            if (sessionManager.getUserLanguage().equals(Constants.AR)) {
                viewHolder.arrow.setRotation(180);
            }

            viewHolder.details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.detailsClick(position);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public interface OnItemClickListener {
        public void detailsClick(int position);
    }

}

