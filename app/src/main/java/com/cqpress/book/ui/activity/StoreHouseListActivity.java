package com.cqpress.book.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cqpress.book.R;
import com.cqpress.book.bean.StoreHouseLocationRequestVo;
import com.cqpress.book.bean.StoreHouseLocationVo;
import com.cqpress.book.bean.StoreHouseRequestVo;
import com.cqpress.book.bean.StoreHouseResultVo;
import com.cqpress.book.bean.StoreHouseVo;
import com.cqpress.book.ui.adpater.base.ListViewDataAdapter;
import com.cqpress.book.ui.adpater.base.ViewHolderBase;
import com.cqpress.book.ui.adpater.base.ViewHolderCreator;
import com.cqpress.book.ui.base.BaseActivity;

import java.util.ArrayList;
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
public class StoreHouseListActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.lv_store_house)
    ListView lv_store_house;

    private ListViewDataAdapter listViewDataAdapter;
    private String stockDetailId;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.store_house_list_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_store_house;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        stockDetailId = extras.getString("stockDetailId");
    }

    @Override
    protected void initViewsAndEvents() {

        listViewDataAdapter = new ListViewDataAdapter<StoreHouseVo>(new ViewHolderCreator<StoreHouseVo>() {
            @Override
            public ViewHolderBase<StoreHouseVo> createViewHolder(int position) {
                return new ViewHolderBase<StoreHouseVo>() {
                    TextView tv_store_house;
                    TextView tv_store_house_name;
                    TextView tv_book_scan;
                    TextView tv_select_house_location;
                    TextView tv_store_scan_amount;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.store_house_list_item_activity, null);
                        tv_store_house = ButterKnife.findById(view, R.id.tv_store_house);
                        tv_store_house_name = ButterKnife.findById(view, R.id.tv_store_house_name);
                        tv_book_scan = ButterKnife.findById(view, R.id.tv_book_scan);
                        tv_select_house_location = ButterKnife.findById(view, R.id.tv_select_house_location);
                        tv_store_scan_amount = ButterKnife.findById(view, R.id.tv_store_scan_amount);
                        return view;
                    }

                    @Override
                    public void showData(int position, final StoreHouseVo itemData) {
                        tv_store_house.setText(itemData.getStorehouseName());
                        tv_store_house.setTag(itemData.getStorehouseID());
                        tv_select_house_location.setTag(itemData.getStorehouseID());
                        //选择库位
                        tv_select_house_location.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String houseId = tv_select_house_location.getTag().toString();
                                StoreHouseLocationRequestVo storeHoseLocationVo = new StoreHouseLocationRequestVo();
                                storeHoseLocationVo.setRequestData(houseId);
                                storeHoseLocationVo.setReqMethod("AppQueryStorehouseLocationsList");
                                Call<StoreHouseResultVo<StoreHouseLocationVo>> storeHouse = getApis().appQueryStorehouseLocationsList(storeHoseLocationVo).clone();
                                storeHouse.enqueue(new Callback<StoreHouseResultVo<StoreHouseLocationVo>>() {
                                    @Override
                                    public void onResponse(Response<StoreHouseResultVo<StoreHouseLocationVo>> response, Retrofit retrofit) {
                                        if (response.isSuccess() && response.body() != null) {
                                            StoreHouseResultVo<StoreHouseLocationVo> body = response.body();
                                            if (!response.body().isError()) {
                                                List<StoreHouseLocationVo> storeHouseLocationVos = body.getData();
                                                ArrayList<String> stringList = new ArrayList<String>();
                                                for (StoreHouseLocationVo locationVo : storeHouseLocationVos) {
                                                    stringList.add(locationVo.getLocationName());
                                                }
                                                final String items[] = stringList.toArray(new String[]{});
                                                AlertDialog.Builder builder = new AlertDialog.Builder(StoreHouseListActivity.this);
                                                builder.setTitle("选择库位"); //设置标题
                                                builder.setItems(items, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        tv_store_house_name.setText(items[which]);
                                                        dialog.dismiss();
                                                    }
                                                });
                                                builder.create().show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                    }
                                });
                            }
                        });

                        tv_book_scan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                    }
                };
            }
        });
        this.lv_store_house.setAdapter(listViewDataAdapter);
        this.lv_store_house.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_store_house = (TextView) view.findViewById(R.id.tv_store_house);
                String storeHouseId = tv_store_house.getTag().toString();
                Bundle bundle = new Bundle();
                bundle.putString("storeHouseId", storeHouseId);
                bundle.putString("stockDetailId", stockDetailId);
                readyGo(StoreHouseLocationListActivity.class, bundle);
            }
        });
        getStoreHost(null);

    }

    //获取库房
    private void getStoreHost(String houseName) {
        this.showLoading(getString(R.string.common_loading_message));
        StoreHouseRequestVo storeHoseVo = new StoreHouseRequestVo();
        storeHoseVo.setRequestData(houseName);
        storeHoseVo.setReqMethod("AppQueryStorehouseList");
        Call<StoreHouseResultVo<StoreHouseVo>> storeHouse = getApis().appQueryStorehouseList(storeHoseVo).clone();
        storeHouse.enqueue(new Callback<StoreHouseResultVo<StoreHouseVo>>() {

            @Override
            public void onResponse(Response<StoreHouseResultVo<StoreHouseVo>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null) {
                    StoreHouseResultVo<StoreHouseVo> body = response.body();
                    if (!response.body().isError()) {
                        List<StoreHouseVo> storeHouseVos = body.getData();
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
