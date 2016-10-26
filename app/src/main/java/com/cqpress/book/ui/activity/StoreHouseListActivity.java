package com.cqpress.book.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cqpress.book.BookApplication;
import com.cqpress.book.R;
import com.cqpress.book.bean.ScanUpLoadRequestVo;
import com.cqpress.book.bean.ScanUpLoadResultVo;
import com.cqpress.book.bean.StoreHouseLocationRequestVo;
import com.cqpress.book.bean.StoreHouseLocationVo;
import com.cqpress.book.bean.StoreHouseRequestVo;
import com.cqpress.book.bean.StoreHouseResultVo;
import com.cqpress.book.bean.StoreHouseVo;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

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

    @Bind(R.id.lv_store_house)
    ListView lv_store_house;
    @Bind(R.id.radio_scan)
    RadioGroup radio_scan;
    private boolean isPackage = true;

    private ListViewDataAdapter listViewDataAdapter;
    private String stockDetailId;
    private Myhandler myhandler = new Myhandler();
    private final Accompaniment accompaniment = Accompaniment.newInstanceOfResource(this, R.raw.tag_inventoried);
    private Handler accompainimentsHandler;

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
        accompainimentsHandler = new Handler();
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
                    Toast.makeText(StoreHouseListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        listViewDataAdapter = new ListViewDataAdapter<StoreHouseVo>(new ViewHolderCreator<StoreHouseVo>() {
            @Override
            public ViewHolderBase<StoreHouseVo> createViewHolder(int position) {
                return new ViewHolderBase<StoreHouseVo>() {
                    TextView tv_store_house;
                    TextView tv_store_house_name;
                    TextView tv_book_scan;
                    TextView tv_select_house_location;
                    TextView tv_scan_num;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.store_house_list_item_activity, null);
                        tv_store_house = ButterKnife.findById(view, R.id.tv_store_house);
                        tv_store_house_name = ButterKnife.findById(view, R.id.tv_store_house_name);
                        tv_book_scan = ButterKnife.findById(view, R.id.tv_book_scan);
                        tv_select_house_location = ButterKnife.findById(view, R.id.tv_select_house_location);
                        tv_scan_num = ButterKnife.findById(view, R.id.tv_scan_num);
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
                                                final List<StoreHouseLocationVo> storeHouseLocationVos = body.getData();
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
                                                        tv_store_house_name.setTag(storeHouseLocationVos.get(which).getLocationID());
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

                        //开始扫描
                        tv_book_scan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (tv_book_scan.getText().toString().equals("开始扫描")) {
                                    tv_book_scan.setText(getString(R.string.book_button_text_stop));
                                    final ConcurrentHashMap<String, String> hashMap = new ConcurrentHashMap<>();
                                    BookApplication.uhfInterfaceAsModelC().startInventorySingleTag(
                                            new StUhf.InterrogatorModelC.UmcOnNewUiiInventoried() {

                                                @Override
                                                public void onNewTagInventoried(StUhf.UII uii) {
                                                    if (uii != null) {
                                                        trigTagAccompainiment();//开启扫描声音
                                                        final String uiiStr = DataTransfer.xGetString(uii.getBytes());
//                                                        if (uiiStr.length() > 24) {//判断扫描24位和28位
//                                                            String strUii = uiiStr.substring(4, uiiStr.length());
//                                                            String startStr = strUii.substring(0, 14);
//                                                            String verifyCode = strUii.substring(14, 15);
//                                                            String endStr = strUii.substring(15, strUii.length());
//                                                            int accumulation = DataTransfer.accumulation((startStr + endStr).toCharArray());
//                                                            if (Integer.parseInt("" + verifyCode, 16) != accumulation) {
//                                                                CommonUtils.make(StoreHouseListActivity.this, "标签不是系统内标签");
//                                                                return;
//                                                            }
//                                                        } else {
//                                                            String startStr = uiiStr.substring(0, 14);
//                                                            String verifyCode = uiiStr.substring(14, 15);
//                                                            String endStr = uiiStr.substring(15, uiiStr.length());
//                                                            int accumulation = DataTransfer.accumulation((startStr + endStr).toCharArray());
//                                                            if (Integer.parseInt("" + verifyCode, 16) != accumulation) {
//                                                                CommonUtils.make(StoreHouseListActivity.this, "标签不是系统内标签");
//                                                                return;
//                                                            }
//                                                        }
                                                        if (TextUtils.isEmpty(hashMap.get(uiiStr))) {
                                                            hashMap.put(uiiStr, uiiStr);
                                                            //扫描数量
                                                            int scanCount = 0;
                                                            if (isPackage) {
                                                                if (uiiStr.length() > 24) {//判断扫描24位和28位
                                                                    String strUii = uiiStr.substring(4, uiiStr.length());
                                                                    if (strUii.indexOf("f") != -1) {
                                                                        scanCount = Integer.parseInt(strUii.substring(strUii.length() - 2, strUii.length() - 1),16);
                                                                    }
                                                                } else {
                                                                    scanCount = Integer.parseInt(uiiStr.substring(uiiStr.length() - 2, uiiStr.length() - 1),16);
                                                                }
                                                            }

                                                            //设置扫描数量
                                                            final int finalScanCount = scanCount;
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    if (tv_scan_num.getTag() != null) {
                                                                        int anInt = Integer.parseInt(tv_scan_num.getTag().toString());
                                                                        int totalCount;
                                                                        if (isPackage) {
                                                                            totalCount = finalScanCount + anInt;
                                                                        } else {
                                                                            totalCount = anInt + 1;
                                                                        }
                                                                        tv_scan_num.setTag(totalCount);
                                                                        tv_scan_num.setText("扫描" + totalCount + "本");
                                                                    } else {
                                                                        if (isPackage) {
                                                                            tv_scan_num.setTag(finalScanCount);
                                                                            tv_scan_num.setText("扫描" + finalScanCount + "本");
                                                                        } else {
                                                                            tv_scan_num.setTag(1);
                                                                            tv_scan_num.setText("扫描" + 1 + "本");
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                            ScanUpLoadRequestVo requestVo = new ScanUpLoadRequestVo();
                                                            final ScanUpLoadRequestVo.RequestData requestData = new ScanUpLoadRequestVo.RequestData();
                                                            requestData.setDeviceID(AppPreferences.getString("registerCode"));
                                                            //修复24 或者大于24的捆标和书标编码
                                                            int length = uiiStr.length();
                                                            if (length > 24) {
                                                                String substring = uiiStr.substring(4, uiiStr.length());
                                                                if (isPackage) {
                                                                    requestData.setPackageCode(substring);
                                                                } else {
                                                                    requestData.setEpc(substring);
                                                                }
                                                            } else {
                                                                if (isPackage) {
                                                                    requestData.setPackageCode(uiiStr);
                                                                } else {
                                                                    requestData.setEpc(uiiStr);
                                                                }
                                                            }
                                                            requestData.setStockInDetailID(stockDetailId);
                                                            requestData.setLocationId(tv_store_house_name.getTag().toString());
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
                                                                    ScanUpLoadResultVo upLoadResult = response.body();
                                                                    if (response.isSuccess() && upLoadResult != null) {
                                                                        final ScanUpLoadResultVo.Data upLoadResultVo = upLoadResult.getData();;
                                                                        if (upLoadResultVo.getSurplusAmount() == 0) {
                                                                            runOnUiThread(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    CommonUtils.make(StoreHouseListActivity.this, "入库完成");
                                                                                }
                                                                            });
                                                                        }
                                                                    }else{
                                                                        CommonUtils.make(StoreHouseListActivity.this, upLoadResult.getMessage());
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Throwable t) {

                                                                }
                                                            });
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onEnd(final int errorId) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            switch (StUhf.InterrogatorModelC.UmcErrorCode.ValueOf(errorId)) {
                                                                case Success: {
//                                                            CommonUtils.make(StoreHouseListActivity.this, getString(R.string.idInventoryFinished));
                                                                    hashMap.clear();
                                                                    tv_scan_num.setTag(null);
                                                                    tv_book_scan.setText(getString(R.string.book_button_text_out));
                                                                    break;
                                                                }
                                                                case RftcErrRevPwrLevTooHigh: {
                                                                    CommonUtils.make(StoreHouseListActivity.this, getString(R.string.idInventoryStopped__PleaseMoveTheTagAndTryAgain));
                                                                    break;
                                                                }
                                                                default: {
                                                                    CommonUtils.make(StoreHouseListActivity.this, getString(R.string.idInventoryFinished_Colon) + String.format(Locale.ENGLISH, "0x%x", errorId));
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onNewErrorReport(int errorCode, StUhf.InterrogatorModelC.UmcErrorCode umcErrorCode) {
                                                    if (umcErrorCode != null) {
                                                        CommonUtils.make(StoreHouseListActivity.this, "event:" + umcErrorCode);
                                                    }
                                                }
                                            });
                                } else {
                                    tv_book_scan.setText(getString(R.string.book_button_text_out));
                                    BookApplication.stop();
                                }
                            }
                        });
                    }
                };
            }
        });
        this.lv_store_house.setAdapter(listViewDataAdapter);
//        this.lv_store_house.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView tv_store_house = (TextView) view.findViewById(R.id.tv_store_house);
//                String storeHouseId = tv_store_house.getTag().toString();
//                Bundle bundle = new Bundle();
//                bundle.putString("storeHouseId", storeHouseId);
//                bundle.putString("stockDetailId", stockDetailId);
//                readyGo(StoreHouseLocationListActivity.class, bundle);
//            }
//        });
        getStoreHost(null);
        radio_scan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tv_book_kun:
                        isPackage = true;
                        break;
                    case R.id.tv_book_epc:
                        isPackage = false;
                        break;
                }
            }
        });

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
            String substring = uiiStr.substring(4, uiiStr.length());
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
                    ScanUpLoadResultVo upLoadResult = response.body();
                    final ScanUpLoadResultVo.Data upLoadResultVo = upLoadResult.getData();;
                    if (upLoadResultVo.getSurplusAmount() == 0) {
                        CommonUtils.make(StoreHouseListActivity.this, "入库数量为0");
                        return;
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
        if (uiiStr.toLowerCase().lastIndexOf("f") != -1) {
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
        super.onDestroy();
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
