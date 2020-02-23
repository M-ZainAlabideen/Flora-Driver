package app.flora.driver.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import app.flora.driver.R;
import app.flora.driver.activities.MainActivity;
import app.flora.driver.adapters.OrdersAdapter;
import app.flora.driver.classes.Constants;
import app.flora.driver.classes.GlobalFunctions;
import app.flora.driver.classes.Navigator;
import app.flora.driver.classes.SessionManager;
import app.flora.driver.webservices.RetrofitConfig;
import app.flora.driver.webservices.models.Order;
import app.flora.driver.webservices.requests.UpdateOrderRequest;
import app.flora.driver.webservices.responses.GetOrders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingOrdersFragment extends Fragment {
    public static FragmentActivity activity;
    public static UpcomingOrdersFragment fragment;
    private View childView;
    private BottomSheetDialog feesDialog;
    public SessionManager sessionManager;
    public ArrayList<Order> ordersList = new ArrayList<>();
    public LinearLayoutManager layoutManager;
    public OrdersAdapter ordersAdapter;
    public int pageIndex = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private String filterStatus;
    private String startDateTxt;
    private String endDateTxt;
    private String orderNumber;
    private boolean isStartTime;

    @BindView(R.id.fragment_upcoming_orders_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_upcoming_orders_ll_startDateContainer)
    LinearLayout startDateContainer;
    @BindView(R.id.fragment_upcoming_orders_ll_endDateContainer)
    LinearLayout endDateContainer;
    @BindView(R.id.fragment_upcoming_orders_tv_startDate)
    TextView startDate;
    @BindView(R.id.fragment_upcoming_orders_tv_endDate)
    TextView endDate;
    @BindView(R.id.fragment_upcoming_orders_rv_orders)
    RecyclerView orders;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static UpcomingOrdersFragment newInstance(FragmentActivity activity) {
        fragment = new UpcomingOrdersFragment();
        UpcomingOrdersFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childView = inflater.inflate(R.layout.fragment_upcoming_orders, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (activity == null) {
            activity = getActivity();
        }
        MainActivity.setupAppbar(Constants.ORDERS, null);
        sessionManager = new SessionManager(activity);
        initOrders();
        if (ordersList.size() == 0) {
            getOrders();
        } else {
            loading.setVisibility(View.GONE);
        }
        filterClick();
        search();
        calendar();
        resetClick();
    }

    private void initOrders() {
        layoutManager = new LinearLayoutManager(activity);
        ordersAdapter = new OrdersAdapter(activity, ordersList, true, new OrdersAdapter.OnItemClickListener() {
            @Override
            public void detailsClick(int position) {
                Navigator.loadFragment(activity, ProductsFragment.newInstance(activity, ordersList.get(position).getId(), ordersList.get(position).getShipping_address()), R.id.activity_main_fl_mainAppContainer, true);
            }

            @Override
            public void changeStatusClick(int position, String statusWord) {
                if (statusWord.equalsIgnoreCase(Constants.SHIPPED)) {
                    changeStatus(ordersList.get(position).getId());
                } else {
                    Snackbar.make(childView, getString(R.string.notShipped), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void acceptOrderClick(int position) {
                UpdateOrderRequest request = new UpdateOrderRequest();
                request.updateOrderRequest_.userId = sessionManager.getUserId();
                request.updateOrderRequest_.status = null;
                updateOrderApi(request, ordersList.get(position).getId());
            }
        });
        orders.setLayoutManager(layoutManager);
        orders.setAdapter(ordersAdapter);
    }

    private void getOrders() {
        if (!GlobalFunctions.isNetworkConnected(activity)) {
            Snackbar.make(loading, getString(R.string.noConnection), Snackbar.LENGTH_SHORT).show();
        } else {
            ordersApi();
        }
    }

    private void filterClick() {
        MainActivity.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilter();
            }
        });
    }

    private void showFilter() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
        TextView title = view.findViewById(R.id.bottom_sheet_dialog_tv_title);
        TextView delivered = view.findViewById(R.id.bottom_sheet_dialog_tv_delivered);
        TextView shipped = view.findViewById(R.id.bottom_sheet_dialog_tv_shipped);
        title.setText(getString(R.string.filter));
        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPagination();
                filterStatus = Constants.DELIVERED;
                ordersApi();
                feesDialog.cancel();
            }
        });
        shipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPagination();
                filterStatus = Constants.SHIPPED;
                ordersApi();
                feesDialog.cancel();
            }
        });
        feesDialog = new BottomSheetDialog(activity);
        feesDialog.setContentView(view);
        feesDialog.show();
    }

    private void changeStatus(String orderId) {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
        TextView title = view.findViewById(R.id.bottom_sheet_dialog_tv_title);
        TextView delivered = view.findViewById(R.id.bottom_sheet_dialog_tv_delivered);
        TextView shipped = view.findViewById(R.id.bottom_sheet_dialog_tv_shipped);
        shipped.setVisibility(View.GONE);
        title.setText(getString(R.string.changeStatus));
        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateOrderRequest request = new UpdateOrderRequest();
                request.updateOrderRequest_.userId = null;
                request.updateOrderRequest_.status = Constants.DELIVERED;
                updateOrderApi(request, orderId);
                feesDialog.cancel();
            }
        });
        feesDialog = new BottomSheetDialog(activity);
        feesDialog.setContentView(view);
        feesDialog.show();
    }

    private void search() {
        //change the color of editText in searchView
        EditText searchEditText = (EditText) MainActivity.search.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorPrimaryDark));

        MainActivity.search.setMaxWidth(Integer.MAX_VALUE);
        MainActivity.search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.filter.setVisibility(View.INVISIBLE);
                MainActivity.appbarLogo.setVisibility(View.INVISIBLE);
                MainActivity.back.setVisibility(View.INVISIBLE);
                MainActivity.reset.setVisibility(View.INVISIBLE);
            }
        });
        MainActivity.search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                MainActivity.filter.setVisibility(View.VISIBLE);
                MainActivity.appbarLogo.setVisibility(View.VISIBLE);
                MainActivity.back.setVisibility(View.VISIBLE);
                MainActivity.reset.setVisibility(View.VISIBLE);
                return false;
            }
        });
        MainActivity.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MainActivity.search.onActionViewCollapsed();
                MainActivity.filter.setVisibility(View.VISIBLE);
                MainActivity.appbarLogo.setVisibility(View.VISIBLE);
                MainActivity.back.setVisibility(View.VISIBLE);
                MainActivity.reset.setVisibility(View.VISIBLE);
                initPagination();
                orderNumber = query;
                ordersApi();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    @OnClick(R.id.fragment_upcoming_orders_ll_startDateContainer)
    public void pickStartDate() {
        new DatePickerDialog(activity, R.style.CalendarDialogTheme, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        isStartTime = true;

    }

    @OnClick(R.id.fragment_upcoming_orders_ll_endDateContainer)
    public void pickEndDate() {
        new DatePickerDialog(activity, R.style.CalendarDialogTheme, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        isStartTime = false;
    }

    private void calendar() {
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate(dayOfMonth, monthOfYear + 1, year, isStartTime);
            }

        };
    }

    private void updateDate(int day, int month, int year, boolean isStartTime) {
        String dayTxt = day + "";
        String monthTxt = month + "";
        String yearTxt = year + "";
        if (day < 10)
            dayTxt = "0" + day;
        if (month < 10)
            monthTxt = "0" + month;
        if (isStartTime) {
            startDate.setText(yearTxt + "-" + monthTxt + "-" + dayTxt);
        } else {
            endDate.setText(yearTxt + "-" + monthTxt + "-" + dayTxt);
        }
    }

    private void resetClick() {
        MainActivity.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
    }

    private void reset() {
        initPagination();
        orderNumber = null;
        filterStatus = null;
        startDateTxt = null;
        endDateTxt = null;
        startDate.setText(getString(R.string.startDate));
        endDate.setText(getString(R.string.endDate));
        ordersApi();
    }

    @OnClick(R.id.fragment_upcoming_orders_iv_done)
    public void doneClick() {
        startDateTxt = startDate.getText().toString();
        endDateTxt = endDate.getText().toString();
        if (startDateTxt.equals(getString(R.string.startDate))) {
            Snackbar.make(loading, getString(R.string.selectStartDate), Snackbar.LENGTH_SHORT).show();
        } else if (endDateTxt.equals(getString(R.string.endDate))) {
            Snackbar.make(loading, getString(R.string.selectEndDate), Snackbar.LENGTH_SHORT).show();
        } else {
            initPagination();
            ordersApi();
        }
    }

    private void ordersApi() {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().GET_ORDERS_CALL(sessionManager.getUserLanguage(), Constants.AUTHORIZATION_VALUE, "0",
                pageIndex, 10, orderNumber, filterStatus, startDateTxt, endDateTxt)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int statusCode = response.code();
                        if (statusCode == 200) {
                            handleResponse(response);
                            if (ordersList.size() > 0) {
                                ordersAdapter.notifyDataSetChanged();
                                orders.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                    }

                                    @Override
                                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);
                                        if (!isLastPage) {
                                            int visibleItemCount = layoutManager.getChildCount();

                                            int totalItemCount = ordersAdapter.getItemCount();

                                            int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                                /*isLoading variable used for check if the user send many requests
                                for pagination(make many scrolls in the same time)
                                1- if isLoading true >> there is request already sent so,
                                no more requests till the response of last request coming
                                2- else >> send new request for load more data (News)*/
                                            if (!isLoading) {

                                                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                                    isLoading = true;

                                                    pageIndex = pageIndex + 1;

                                                    getMoreOrders();

                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                isLastPage = true;
                                Snackbar.make(loading, getString(R.string.noOrdersFound), Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            GlobalFunctions.handleError(response, childView);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        GlobalFunctions.EnableLayout(container);
                        GlobalFunctions.generalErrorMessage(activity, childView, loading);
                    }
                });
    }

    private void getMoreOrders() {
        loading.setVisibility(View.VISIBLE);
        RetrofitConfig.getServices().GET_ORDERS_CALL(sessionManager.getUserLanguage(), Constants.AUTHORIZATION_VALUE, "0",
                pageIndex, 10, orderNumber, filterStatus, startDateTxt, endDateTxt)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int statusCode = response.code();
                        if (statusCode == 200) {
                            handleResponse(response);
                            if (ordersList.isEmpty()) {
                                isLastPage = true;
                                pageIndex = pageIndex - 1;
                            }
                        } else {
                            GlobalFunctions.handleError(response, childView);

                        }
                        isLoading = false;
                        ordersAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        GlobalFunctions.EnableLayout(container);
                        GlobalFunctions.generalErrorMessage(activity, childView, loading);
                    }
                });
    }

    private void updateOrderApi(UpdateOrderRequest request, String orderId) {
        loading.setVisibility(View.VISIBLE);
        GlobalFunctions.DisableLayout(container);
        RetrofitConfig.getServices().UPDATE_ORDER_CALL(sessionManager.getUserLanguage()
                , Constants.AUTHORIZATION_VALUE, orderId, request)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        loading.setVisibility(View.GONE);
                        GlobalFunctions.EnableLayout(container);
                        int statusCode = response.code();
                        if (statusCode == 200) {
                            initPagination();
                            initOrders();
                            ordersApi();
                            Snackbar.make(childView,getString(R.string.acceptedSuccessfully),Snackbar.LENGTH_SHORT).show();
                        } else {
                            GlobalFunctions.handleError(response, childView);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        GlobalFunctions.EnableLayout(container);
                        GlobalFunctions.generalErrorMessage(activity, childView, loading);
                    }
                });
    }

    private ArrayList<Order> handleResponse(Response<ResponseBody> response) {
        ResponseBody body = response.body();
        String outResponse = "";
        String jsonResponse = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(body.byteStream()));
            StringBuilder out = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append(newLine);
            }
            outResponse = out.toString();
            jsonResponse = out.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (outResponse != null) {
            outResponse = outResponse.replace("\"", "");
            outResponse = outResponse.replace("\n", "");
            Type type = new TypeToken<GetOrders>() {
            }.getType();
            JsonReader reader = new JsonReader(new StringReader(outResponse));
            reader.setLenient(true);
            GetOrders orders = new Gson().fromJson(jsonResponse, type);
            ordersList.addAll(orders.getOrders());

        }
        return ordersList;
    }

    private void initPagination() {
        ordersList.clear();
        ordersAdapter.notifyDataSetChanged();
        pageIndex = 1;
        isLastPage = false;
        isLoading = false;
    }
}
