package com.cqpress.book.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cqpress.book.BookApplication;
import com.cqpress.book.R;
import com.cqpress.book.bean.StockInDetailVo;
import com.cqpress.book.bean.StockOutRequestDetailVo;
import com.cqpress.book.bean.StockOutResultVo;
import com.cqpress.book.uhf.ExceptionForToast;
import com.cqpress.book.ui.adpater.base.ListViewDataAdapter;
import com.cqpress.book.ui.adpater.base.ViewHolderBase;
import com.cqpress.book.ui.adpater.base.ViewHolderCreator;
import com.cqpress.book.ui.base.BaseActivity;
import com.cqpress.book.utils.TLog;

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
public class StockInDocumentDetailsActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.tv_header_title)
    TextView tv_header_title;
    @Bind(R.id.lv_stock_list)
    ListView lv_stock_list;
    Bundle extras;

    private ListViewDataAdapter<StockInDetailVo> listViewDataAdapter;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.stock_in_list_detail_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_stock_list;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        this.extras = extras;
    }

    @Override
    protected void initViewsAndEvents() {
        this.tv_header_title.setText(getString(R.string.book_out_header_detail_title));
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BookApplication.getUhfWithDetectionAutomaticallyIfNeed();
                try {
                    BookApplication.uhfInit();
                } catch (ExceptionForToast e) {
                    TLog.e(TAG_LOG, e.getMessage());
                    BookApplication.uhfClear();
                    BookApplication.appCfgSaveModelClear();
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        listViewDataAdapter = new ListViewDataAdapter<StockInDetailVo>(new ViewHolderCreator<StockInDetailVo>() {
            @Override
            public ViewHolderBase<StockInDetailVo> createViewHolder(int position) {
                return new ViewHolderBase<StockInDetailVo>() {
                    TextView tv_book_name;
                    TextView tv_book_isbn;
                    TextView tv_storage_location;
                    TextView tv_book_amount;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.stock_in_list_detail_item_activity, null);
                        tv_book_name = ButterKnife.findById(view, R.id.tv_book_name);
                        tv_book_isbn = ButterKnife.findById(view, R.id.tv_book_isbn);
                        tv_book_isbn = ButterKnife.findById(view, R.id.tv_book_isbn);
                        tv_storage_location = ButterKnife.findById(view, R.id.tv_storage_location);
                        tv_book_amount = ButterKnife.findById(view, R.id.tv_book_amount);
                        return view;
                    }

                    @Override
                    public void showData(int position, StockInDetailVo itemData) {
                        tv_book_name.setText(itemData.getBook().getBookName());
                        tv_book_isbn.setText(itemData.getBook().getIsbn());
                        tv_book_amount.setText("" + itemData.getAmount());
                        tv_storage_location.setTag(itemData.getDetailID());
                        tv_storage_location.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("stockDetailId", v.getTag().toString());
                                readyGo(StoreHouseListActivity.class, bundle);
                            }
                        });
                    }
                };
            }
        });
        this.lv_stock_list.setAdapter(listViewDataAdapter);
        getStockDetail(extras.getString("stockInId"));
    }

    private void getStockDetail(String stockInId) {
        this.showLoading(getString(R.string.common_loading_message));
        StockOutRequestDetailVo stockOutRequestVo = new StockOutRequestDetailVo();
        stockOutRequestVo.setRequestData(stockInId);
        stockOutRequestVo.setReqMethod("AppGetStockInDocumentDetails");
        stockOutRequestVo.sign();
        Call<StockOutResultVo<StockInDetailVo>> storeHouse = getApis().appGetStockInDocumentDetails(stockOutRequestVo).clone();
        storeHouse.enqueue(new Callback<StockOutResultVo<StockInDetailVo>>() {

            @Override
            public void onResponse(Response<StockOutResultVo<StockInDetailVo>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null) {
                    StockOutResultVo<StockInDetailVo> body = response.body();
                    if (!response.body().isError()) {
                        List<StockInDetailVo> storeHouseVos = body.getData();
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
