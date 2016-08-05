package com.cqpress.book.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cqpress.book.R;
import com.cqpress.book.bean.BaseResultVo;
import com.cqpress.book.bean.StockDocumentCompleteRequestVo;
import com.cqpress.book.bean.StockOutRequestVo;
import com.cqpress.book.bean.StockOutResultVo;
import com.cqpress.book.bean.StockOutVo;
import com.cqpress.book.ui.adpater.base.ListViewDataAdapter;
import com.cqpress.book.ui.adpater.base.ViewHolderBase;
import com.cqpress.book.ui.adpater.base.ViewHolderCreator;
import com.cqpress.book.ui.base.BaseActivity;
import com.cqpress.book.utils.AppPreferences;
import com.cqpress.book.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by liuguofeng719 on 2016/7/6.
 */
public class StockOutDocumentsActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.tv_header_title)
    TextView tv_header_title;
    @Bind(R.id.lv_stock_list)
    ListView lv_stock_list;

    private ListViewDataAdapter<StockOutVo> listViewDataAdapter;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.book_out_list_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_stock_list;
    }

    @Override
    protected void initViewsAndEvents() {
        this.tv_header_title.setText(getString(R.string.book_out_header_title));
        listViewDataAdapter = new ListViewDataAdapter<StockOutVo>(new ViewHolderCreator<StockOutVo>() {
            @Override
            public ViewHolderBase<StockOutVo> createViewHolder(int position) {
                return new ViewHolderBase<StockOutVo>() {
                    TextView tv_stock_out_num;
                    TextView tv_stock_out_time;
                    TextView tv_stock_in_complete;
                    TextView tv_show_detail;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.book_out_list_item_activity, null);
                        tv_stock_out_num = ButterKnife.findById(view, R.id.tv_stock_out_num);
                        tv_stock_out_time = ButterKnife.findById(view, R.id.tv_stock_out_time);
                        tv_stock_in_complete = ButterKnife.findById(view, R.id.tv_stock_in_complete);
                        tv_show_detail = ButterKnife.findById(view, R.id.tv_show_detail);
                        return view;
                    }

                    @Override
                    public void showData(int position, StockOutVo itemData) {
                        tv_stock_out_num.setText(itemData.getStockOutNumber());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:ss");
                        try {
                            if (!TextUtils.isEmpty(itemData.getStockOutDate())) {
                                Date date = sdf.parse(itemData.getStockOutDate());
                                tv_stock_out_time.setText(sdf.format(date));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        tv_stock_out_num.setTag(itemData.getStockOutID());
                        tv_stock_in_complete.setTag(itemData.getStockOutID());
                        tv_stock_in_complete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                StockDocumentCompleteRequestVo requestVo = new StockDocumentCompleteRequestVo();
                                StockDocumentCompleteRequestVo.RequestData requestData = new StockDocumentCompleteRequestVo.RequestData();
                                requestData.setDocumentID(v.getTag().toString());
                                requestData.setStockType(1);//出库类型
                                requestVo.setReqMethod("AppStockDocumentComplete");
                                requestVo.setRequestData(requestData);
                                requestVo.sign();
                                Call<BaseResultVo> resultVoCall = getApis().appStockDocumentComplete(requestVo).clone();
                                resultVoCall.enqueue(new Callback<BaseResultVo>() {
                                    @Override
                                    public void onResponse(Response<BaseResultVo> response, Retrofit retrofit) {
                                        if (response.isSuccess() && response.body() != null) {
                                            BaseResultVo baseResultVo = response.body();
                                            if (baseResultVo.getErrCode() == 0) {
                                                CommonUtils.make(StockOutDocumentsActivity.this, "出库成功");
                                                getStock();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

                                    }
                                });
                            }
                        });
                        tv_show_detail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String stockOutId = tv_stock_out_num.getTag().toString();
                                Bundle bundle = new Bundle();
                                bundle.putString("stockOutId", stockOutId);
                                readyGo(StockOutDocumentDetailsActivity.class, bundle);
                            }
                        });
                    }
                };
            }
        });
        this.lv_stock_list.setAdapter(listViewDataAdapter);
        getStock();
    }

    private void getStock() {
        this.showLoading(getString(R.string.common_loading_message));
        StockOutRequestVo stockOutRequestVo = new StockOutRequestVo();
        StockOutRequestVo.RequestData requestData = new StockOutRequestVo.RequestData();
        requestData.setAllot(true);
        requestData.setScanUserId(AppPreferences.getString("userId"));
        requestData.setDeviceID(AppPreferences.getString("registerCode"));
        stockOutRequestVo.setRequestData(requestData);
        stockOutRequestVo.setReqMethod("AppGetStockOutDocuments");
        stockOutRequestVo.sign();
        Call<StockOutResultVo<StockOutVo>> storeHouse = getApis().appGetStockOutDocuments(stockOutRequestVo).clone();
        storeHouse.enqueue(new Callback<StockOutResultVo<StockOutVo>>() {

            @Override
            public void onResponse(Response<StockOutResultVo<StockOutVo>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null) {
                    StockOutResultVo<StockOutVo> body = response.body();
                    if (!response.body().isError()) {
                        List<StockOutVo> storeHouseVos = body.getData();
                        listViewDataAdapter.getDataList().clear();
                        listViewDataAdapter.getDataList().addAll(storeHouseVos);
                        listViewDataAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
            }
        });
    }
}
