package com.cqpress.book.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cqpress.book.BookApplication;
import com.cqpress.book.R;
import com.cqpress.book.bean.ScanUpLoadRequestVo;
import com.cqpress.book.bean.ScanUpLoadResultVo;
import com.cqpress.book.bean.StoreHouseLocationRequestVo;
import com.cqpress.book.bean.StoreHouseLocationVo;
import com.cqpress.book.bean.StoreHouseResultVo;
import com.cqpress.book.uhf.Accompaniment;
import com.cqpress.book.uhf.DataTransfer;
import com.cqpress.book.uhf.ExceptionForToast;
import com.cqpress.book.ui.adpater.base.ListViewDataAdapter;
import com.cqpress.book.ui.adpater.base.ViewHolderBase;
import com.cqpress.book.ui.adpater.base.ViewHolderCreator;
import com.cqpress.book.ui.base.BaseActivity;
import com.cqpress.book.utils.AppPreferences;
import com.cqpress.book.utils.CommonUtils;
import com.cqpress.book.utils.TLog;
import com.senter.support.openapi.StUhf;

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
public class StoreHouseLocationListActivity extends BaseActivity {

    @Bind(R.id.tv_header_title)
    TextView tv_header_title;

    @Bind(R.id.lv_store_house_location)
    ListView lv_store_house_location;

    private ListViewDataAdapter<StoreHouseLocationVo> listViewDataAdapter;
    private String stockDetailId;
    private String storeHouseId;

    private final Accompaniment accompaniment = Accompaniment.newInstanceOfResource(this, R.raw.tag_inventoried);
    private Handler accompainimentsHandler;
    private Dialog dialog;
    private Myhandler myhandler = new Myhandler();

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.store_house_list_detail_activity;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        stockDetailId = extras.getString("stockDetailId");
        storeHouseId = extras.getString("storeHouseId");
    }

    @Override
    protected View getLoadingTargetView() {
        return lv_store_house_location;
    }

    @Override
    protected void initViewsAndEvents() {
        tv_header_title.setText(getString(R.string.house_location_detail_header_title));
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BookApplication.getUhfWithDetectionAutomaticallyIfNeed();
                try {
                    BookApplication.uhfInit();
                } catch (ExceptionForToast e) {
                    e.printStackTrace();
                    BookApplication.uhfClear();
                    BookApplication.appCfgSaveModelClear();
                    Toast.makeText(StoreHouseLocationListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        accompainimentsHandler = new Handler();
        listViewDataAdapter = new ListViewDataAdapter<StoreHouseLocationVo>(new ViewHolderCreator<StoreHouseLocationVo>() {
            @Override
            public ViewHolderBase<StoreHouseLocationVo> createViewHolder(int position) {
                return new ViewHolderBase<StoreHouseLocationVo>() {
                    TextView tv_store_house;
                    TextView tv_store_house_location;
                    TextView tv_book_scan;
                    LinearLayout ly_house_location_item;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.store_house_list_detail_item_activity, null);
                        tv_store_house = ButterKnife.findById(view, R.id.tv_store_house);
                        tv_store_house_location = ButterKnife.findById(view, R.id.tv_store_house_location);
                        ly_house_location_item = ButterKnife.findById(view, R.id.ly_house_location_item);
                        tv_book_scan = ButterKnife.findById(view, R.id.tv_book_scan);
                        return view;
                    }

                    @Override
                    public void showData(int position, StoreHouseLocationVo itemData) {
                        tv_store_house.setText(itemData.getStorehouseName());
                        tv_store_house_location.setText(itemData.getLocationName());
                        tv_book_scan.setTag(itemData.getLocationID());
                        tv_book_scan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog = CommonUtils.showDialog(StoreHouseLocationListActivity.this, "正在扫描");
                                dialog.show();
                                final String vTag = v.getTag().toString();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        StUhf.UII uii = startInventorySingleStep();
                                        if (uii != null) {
                                            //触发铃声
                                            trigTagAccompainiment();
                                            //获取16进制
                                            String uiiStr = DataTransfer.xGetString(uii.getBytes());
                                            TLog.d(TAG_LOG, "StockInDocument======" + uiiStr.toString());

                                            Message msg = Message.obtain();
                                            Bundle data = new Bundle();
                                            data.putString("locationId", vTag);
                                            data.putString("uiiStr", uiiStr);
                                            data.putString("stockDetailId", stockDetailId);
                                            msg.setData(data);
                                            myhandler.sendMessage(msg);
                                        } else {
                                            CommonUtils.make(StoreHouseLocationListActivity.this, "请重新扫描");
                                            CommonUtils.dismiss(dialog);
                                        }
                                    }
                                }).start();
                                TLog.d(TAG_LOG, v.getTag().toString());
                            }
                        });

                    }
                };
            }
        });
        this.lv_store_house_location.setAdapter(listViewDataAdapter);
        getStoreHouseLocation(storeHouseId);
    }

    protected StUhf.UII startInventorySingleStep() {
        return BookApplication.uhfInterfaceAsModelC().inventorySingleStep();
    }

    class Myhandler extends Handler {

        public Myhandler() {
        }

        public Myhandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CommonUtils.dismiss(dialog);
            if (msg != null) {
                Bundle msgData = msg.getData();
                scanStockOut(msgData);
            }
        }
    }

    private void scanStockOut(Bundle msgData) {
        String uiiStr = msgData.getString("uiiStr").trim();
        String locationId = msgData.getString("locationId");
        String stockDetailId = msgData.getString("stockDetailId");
        ScanUpLoadRequestVo requestVo = new ScanUpLoadRequestVo();
        final ScanUpLoadRequestVo.RequestData requestData = new ScanUpLoadRequestVo.RequestData();
        requestData.setDeviceID(AppPreferences.getString("registerCode"));
        //查找字符串
        int length = uiiStr.length();

        if (length > 24) {
            String substring = uiiStr.substring(5, uiiStr.length() + 1);
            commUII(substring, requestData);
        } else {
            commUII(uiiStr, requestData);
        }

        requestData.setStockInDetailID(stockDetailId);
        requestData.setLocationId(locationId);
        requestData.setUserID(AppPreferences.getString("userId"));
        requestData.setStockType("0");//入库
        requestVo.setRequestData(requestData);
        requestVo.setReqMethod("AppScanResultUpLoad");
        requestVo.sign();
        Call<ScanUpLoadResultVo> scanUpLoadResultVoCall = getApis().appScanResultUpLoad(requestVo).clone();
        scanUpLoadResultVoCall.enqueue(new Callback<ScanUpLoadResultVo>() {
            @Override
            public void onResponse(Response<ScanUpLoadResultVo> response, Retrofit retrofit) {
                TLog.d(TAG_LOG, response.message());
                if (response.isSuccess() && response.body() != null) {
                    ScanUpLoadResultVo upLoadResultVo = response.body();
                    if (upLoadResultVo.getSurplusAmount() == 0) {
                        CommonUtils.make(StoreHouseLocationListActivity.this, "出库数量剩余数量为0");
                        return;
                    }
                    if (!upLoadResultVo.isError()) {
                        CommonUtils.make(StoreHouseLocationListActivity.this, "扫描成功");
                    }
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * 判断是否是捆标和epc
     *
     * @param uiiStr
     * @param requestData
     */
    private void commUII(String uiiStr, ScanUpLoadRequestVo.RequestData requestData) {
        //查找字符串
        if (uiiStr.toLowerCase().lastIndexOf("ff") != -1) {
            requestData.setPackageCode(uiiStr);
        } else {
            requestData.setEpc(uiiStr);
        }
    }

    private final Runnable accompainimentRunnable = new Runnable() {
        @Override
        public void run() {
            accompaniment.start();
        }
    };

    private void trigTagAccompainiment() {
        accompainimentsHandler.post(accompainimentRunnable);
    }


    @Override
    protected void onDestroy() {
        accompaniment.release();
        if (dialog != null) {
            CommonUtils.dismiss(dialog);
        }
        super.onDestroy();
    }

    private void getStoreHouseLocation(String houseId) {
        this.showLoading(getString(R.string.common_loading_message));
        StoreHouseLocationRequestVo storeHoseLocationVo = new StoreHouseLocationRequestVo();
        storeHoseLocationVo.setRequestData(houseId);
        storeHoseLocationVo.setReqMethod("AppQueryStorehouseLocationsList");
        Call<StoreHouseResultVo<StoreHouseLocationVo>> storeHouse = getApis().appQueryStorehouseLocationsList(storeHoseLocationVo).clone();
        storeHouse.enqueue(new Callback<StoreHouseResultVo<StoreHouseLocationVo>>() {

            @Override
            public void onResponse(Response<StoreHouseResultVo<StoreHouseLocationVo>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null) {
                    StoreHouseResultVo<StoreHouseLocationVo> body = response.body();
                    if (!response.body().isError()) {
                        List<StoreHouseLocationVo> storeHouseLocationVos = body.getData();
                        listViewDataAdapter.getDataList().clear();
                        listViewDataAdapter.getDataList().addAll(storeHouseLocationVos);
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
