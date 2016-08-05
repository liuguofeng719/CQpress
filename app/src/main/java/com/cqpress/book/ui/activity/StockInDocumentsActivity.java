package com.cqpress.book.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cqpress.book.R;
import com.cqpress.book.bean.BaseResultVo;
import com.cqpress.book.bean.StockDocumentCompleteRequestVo;
import com.cqpress.book.bean.StockInDocumentVo;
import com.cqpress.book.bean.StockOutRequestVo;
import com.cqpress.book.bean.StockOutResultVo;
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
public class StockInDocumentsActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.tv_header_title)
    TextView tv_header_title;
    @Bind(R.id.lv_stock_list)
    ListView lv_stock_list;

    private ListViewDataAdapter<StockInDocumentVo> listViewDataAdapter;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.stock_in_documents_list_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_stock_list;
    }

    @Override
    protected void initViewsAndEvents() {
        this.tv_header_title.setText(getString(R.string.book_in_header_title));
        listViewDataAdapter = new ListViewDataAdapter<StockInDocumentVo>(new ViewHolderCreator<StockInDocumentVo>() {
            @Override
            public ViewHolderBase<StockInDocumentVo> createViewHolder(int position) {
                return new ViewHolderBase<StockInDocumentVo>() {
                    TextView tv_stock_in_number;
                    TextView tv_stock_in_time;
                    TextView tv_stock_in_amount;
                    TextView tv_stock_in_complete;
                    TextView tv_show_detail;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.stock_in_documents_item_activity, null);
                        tv_stock_in_number = ButterKnife.findById(view, R.id.tv_stock_in_number);
                        tv_stock_in_time = ButterKnife.findById(view, R.id.tv_stock_in_time);
                        tv_stock_in_amount = ButterKnife.findById(view, R.id.tv_stock_in_amount);
                        tv_stock_in_complete = ButterKnife.findById(view, R.id.tv_stock_in_complete);
                        tv_show_detail = ButterKnife.findById(view, R.id.tv_show_detail);
                        return view;
                    }

                    @Override
                    public void showData(int position, StockInDocumentVo itemData) {
                        tv_stock_in_number.setText(itemData.getStockInNumber());
                        tv_stock_in_amount.setText("" + itemData.getStockInAmount() + "本");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:ss");
                        try {
                            Date date = sdf.parse(itemData.getStockInDate());
                            tv_stock_in_time.setText(sdf.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        tv_stock_in_number.setTag(itemData.getStockInID());
                        tv_stock_in_complete.setTag(itemData.getStockInID());
                        tv_stock_in_complete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                StockDocumentCompleteRequestVo requestVo = new StockDocumentCompleteRequestVo();
                                StockDocumentCompleteRequestVo.RequestData requestData = new StockDocumentCompleteRequestVo.RequestData();
                                requestData.setDocumentID(v.getTag().toString());
                                requestData.setStockType(0);//入库类型
                                requestVo.setRequestData(requestData);
                                requestVo.setReqMethod("AppStockDocumentComplete");
                                requestVo.sign();
                                Call<BaseResultVo> resultVoCall = getApis().appStockDocumentComplete(requestVo).clone();
                                resultVoCall.enqueue(new Callback<BaseResultVo>() {
                                    @Override
                                    public void onResponse(Response<BaseResultVo> response, Retrofit retrofit) {
                                        if (response.isSuccess() && response.body() != null) {
                                            BaseResultVo baseResultVo = response.body();
                                            if (baseResultVo.getErrCode() == 0) {
                                                CommonUtils.make(StockInDocumentsActivity.this, "入库成功");
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
                                String stockInId = tv_stock_in_number.getTag().toString();
                                Bundle bundle = new Bundle();
                                bundle.putString("stockInId", stockInId);
                                readyGo(StockInDocumentDetailsActivity.class, bundle);
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
        stockOutRequestVo.setReqMethod("AppGetStockInDocuments");
        stockOutRequestVo.sign();
        Call<StockOutResultVo<StockInDocumentVo>> storeHouse = getApis().appGetStockInDocuments(stockOutRequestVo).clone();
        storeHouse.enqueue(new Callback<StockOutResultVo<StockInDocumentVo>>() {
            @Override
            public void onResponse(Response<StockOutResultVo<StockInDocumentVo>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null) {
                    StockOutResultVo<StockInDocumentVo> body = response.body();
                    if (!response.body().isError()) {
                        List<StockInDocumentVo> storeHouseVos = body.getData();
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
