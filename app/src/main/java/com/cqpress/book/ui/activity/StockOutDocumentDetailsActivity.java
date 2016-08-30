package com.cqpress.book.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cqpress.book.BookApplication;
import com.cqpress.book.R;
import com.cqpress.book.bean.ScanUpLoadRequestVo;
import com.cqpress.book.bean.ScanUpLoadResultVo;
import com.cqpress.book.bean.StockOutDetailVo;
import com.cqpress.book.bean.StockOutRequestDetailVo;
import com.cqpress.book.bean.StockOutResultVo;
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
public class StockOutDocumentDetailsActivity extends BaseActivity {

    @Bind(R.id.tv_header_title)
    TextView tv_header_title;
    @Bind(R.id.radio_scan)
    RadioGroup radio_scan;

    @Bind(R.id.tv_book_epc)
    RadioButton tv_book_epc;

    @Bind(R.id.lv_stock_list)
    ListView lv_stock_list;

    Bundle extras;
    private boolean isPackage = true;
    private final Accompaniment accompaniment = Accompaniment.newInstanceOfResource(this, R.raw.tag_inventoried);
    private ListViewDataAdapter<StockOutDetailVo> listViewDataAdapter;
    private Handler accompainimentsHandler;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.book_out_list_detail_activity;
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

        if (!TextUtils.isEmpty(AppPreferences.getString("customerType"))) {
            String customerType = AppPreferences.getString("customerType");
            if ("1".equals(customerType)) {
                tv_book_epc.setVisibility(View.INVISIBLE);
            }
        }


        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BookApplication.getUhfWithDetectionAutomaticallyIfNeed();
                try {
                    BookApplication.uhfInit();
                } catch (ExceptionForToast e) {
//                    e.printStackTrace();
                    BookApplication.uhfClear();
                    BookApplication.appCfgSaveModelClear();
                    Toast.makeText(StockOutDocumentDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    finish();
                }
            }
        });
        accompainimentsHandler = new Handler();

        this.tv_header_title.setText(extras.getString("title"));
        listViewDataAdapter = new ListViewDataAdapter<StockOutDetailVo>(new ViewHolderCreator<StockOutDetailVo>() {

            @Override
            public ViewHolderBase<StockOutDetailVo> createViewHolder(int position) {

                return new ViewHolderBase<StockOutDetailVo>() {
                    TextView tv_book_name;
                    TextView tv_book_isbn;
                    TextView tv_book_scan;
                    TextView tv_book_amount;
                    TextView tv_scan_num;
                    TextView tv_number;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.book_out_list_detail_item_activity, null);
                        tv_book_name = ButterKnife.findById(view, R.id.tv_book_name);
                        tv_book_isbn = ButterKnife.findById(view, R.id.tv_book_isbn);
                        tv_book_scan = ButterKnife.findById(view, R.id.tv_book_scan);
                        tv_book_amount = ButterKnife.findById(view, R.id.tv_book_amount);
                        tv_scan_num = ButterKnife.findById(view, R.id.tv_scan_num);
                        tv_number = ButterKnife.findById(view, R.id.tv_number);
                        return view;
                    }

                    @Override
                    public void showData(int position, StockOutDetailVo itemData) {
                        tv_book_name.setText(itemData.getBook().getBookName());
                        tv_book_isbn.setText(itemData.getBook().getIsbn());
                        tv_book_scan.setTag(itemData.getDetailID() + "," + itemData.getAmount());
                        if ("2".equals(extras.getString("type"))) {
                            tv_book_amount.setText("已核查" + itemData.getAmount() + "本");
                        } else {
                            tv_book_amount.setText("出库" + itemData.getAmount() + "本");
                        }
                        tv_book_scan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                final ConcurrentHashMap<String, String> hashMap = new ConcurrentHashMap<String, String>();
                                if (tv_book_scan.getText().toString().equals("开始扫描")) {
                                    tv_book_scan.setText(getString(R.string.book_button_text_stop));
                                    final String[] vTag = v.getTag().toString().split(",");
                                    final String detailId = vTag[0];//出库详情编码
                                    final String outAmount = vTag[1];//出库数量

                                    BookApplication.uhfInterfaceAsModelC().startInventorySingleTag(
                                            new StUhf.InterrogatorModelC.UmcOnNewUiiInventoried() {
                                                @Override
                                                public void onNewTagInventoried(StUhf.UII uii) {
                                                    try {
                                                        if (uii != null) {
                                                            trigTagAccompainiment();//开启扫描声音

                                                            final String uiiStr = DataTransfer.xGetString(uii.getBytes());
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    CommonUtils.make(StockOutDocumentDetailsActivity.this, uiiStr);
                                                                }
                                                            });
//                                                        if (uiiStr.length() > 24) {//判断扫描24位和28位
//                                                            String strUii = uiiStr.substring(4, uiiStr.length());
//                                                            String startStr = strUii.substring(0, 14);
//                                                            String verifyCode = strUii.substring(14, 15);
//                                                            String endStr = strUii.substring(15, strUii.length());
//                                                            int accumulation = DataTransfer.accumulation((startStr + endStr).toCharArray());
//                                                            if (Integer.parseInt("" + verifyCode, 16) != accumulation) {
//                                                                CommonUtils.make(StockOutDocumentDetailsActivity.this, "标签不是系统内标签");
//                                                                return;
//                                                            }
//                                                        } else {
//                                                            String startStr = uiiStr.substring(0, 14);
//                                                            String verifyCode = uiiStr.substring(14, 15);
//                                                            String endStr = uiiStr.substring(15, uiiStr.length());
//                                                            int accumulation = DataTransfer.accumulation((startStr + endStr).toCharArray());
//                                                            if (Integer.parseInt("" + verifyCode, 16) != accumulation) {
//                                                                CommonUtils.make(StockOutDocumentDetailsActivity.this, "标签不是系统内标签");
//                                                                return;
//                                                            }
//                                                        }
                                                            if (TextUtils.isEmpty(hashMap.get(uiiStr))) {
                                                                hashMap.put(uiiStr, uiiStr);
//                                                                //扫描数量
//                                                                int scanCount = 0;
//                                                                if (isPackage) {
//                                                                    if (uiiStr.length() > 24) {//判断扫描24位和28位
//                                                                        String strUii = uiiStr.substring(4, uiiStr.length());
//                                                                        if (strUii.indexOf("f") != -1) {
//                                                                            scanCount = Integer.parseInt(strUii.substring(strUii.length() - 2, strUii.length() - 1), 16);
//                                                                        }
//                                                                    } else {
//                                                                        scanCount = Integer.parseInt(uiiStr.substring(uiiStr.length() - 2, uiiStr.length() - 1), 16);
//                                                                    }
//                                                                }
//
//                                                                //设置扫描数量
//                                                                final int finalScanCount = scanCount;
//                                                                runOnUiThread(new Runnable() {
//                                                                    @Override
//                                                                    public void run() {
//                                                                        if (tv_scan_num.getTag() != null) {
//                                                                            int anInt = Integer.parseInt(tv_scan_num.getTag().toString());
//                                                                            int totalCount;
//                                                                            if (isPackage) {
//                                                                                totalCount = finalScanCount + anInt;
//                                                                            } else {
//                                                                                totalCount = anInt + 1;
//                                                                            }
//                                                                            tv_scan_num.setTag(totalCount);
//                                                                            tv_scan_num.setText("扫描" + totalCount + "本");
//                                                                        } else {
//                                                                            if (isPackage) {
//                                                                                tv_scan_num.setTag(finalScanCount);
//                                                                                tv_scan_num.setText("扫描" + finalScanCount + "本");
//                                                                            } else {
//                                                                                tv_scan_num.setTag(1);
//                                                                                tv_scan_num.setText("扫描" + 1 + "本");
//                                                                            }
//                                                                        }
//                                                                    }
//                                                                });

                                                                ScanUpLoadRequestVo requestVo = new ScanUpLoadRequestVo();
                                                                final ScanUpLoadRequestVo.RequestData requestData = new ScanUpLoadRequestVo.RequestData();
                                                                requestData.setDeviceID(AppPreferences.getString("registerCode"));//获取设备ID

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

                                                                requestData.setStockOutDetailID(detailId);
                                                                requestData.setUserID(AppPreferences.getString("userId"));
                                                                if ("1".equals(extras.getString("type"))) {
                                                                    requestData.setStockType("1");//拣货出库
                                                                } else {
                                                                    requestData.setStockType("2");//核查
                                                                }

                                                                requestVo.setRequestData(requestData);
                                                                requestVo.setReqMethod("AppScanResultUpLoad");
                                                                requestVo.sign();
                                                                Call<ScanUpLoadResultVo> scanUpLoadResultVoCall = getApis().appScanResultUpLoad(requestVo).clone();
                                                                scanUpLoadResultVoCall.enqueue(new Callback<ScanUpLoadResultVo>() {
                                                                    @Override
                                                                    public void onResponse(Response<ScanUpLoadResultVo> response, Retrofit retrofit) {
                                                                        TLog.d(TAG_LOG, response.message());
                                                                        if (response.isSuccess() && response.body() != null) {
                                                                            final ScanUpLoadResultVo upLoadResultVo = response.body();
                                                                            if (upLoadResultVo.getSurplusAmount() == 0) {
                                                                                BookApplication.stop();
                                                                                getStockDetail(extras.getString("stockOutId"));//如果扫描完成重新拉取数据
                                                                            } else {
                                                                                final int lastAmount = Integer.parseInt(outAmount) - upLoadResultVo.getSurplusAmount();
                                                                                runOnUiThread(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        tv_scan_num.setText("扫描" + lastAmount + "本");
                                                                                    }
                                                                                });
                                                                                runOnUiThread(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        tv_number.setText("剩余" + upLoadResultVo.getSurplusAmount() + "本");
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(final Throwable t) {
                                                                        runOnUiThread(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                CommonUtils.make(StockOutDocumentDetailsActivity.this, t.getMessage());
                                                                            }
                                                                        });

                                                                    }
                                                                });
                                                            }
                                                        }
                                                    } catch (final Exception e) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                CommonUtils.make(StockOutDocumentDetailsActivity.this, e.getMessage());
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onEnd(final int errorId) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            switch (StUhf.InterrogatorModelC.UmcErrorCode.ValueOf(errorId)) {
                                                                case Success: {
                                                                    hashMap.clear();
                                                                    tv_scan_num.setTag(null);
//                                                                    CommonUtils.make(StockOutDocumentDetailsActivity.this, getString(R.string.idInventoryFinished));
                                                                    tv_book_scan.setText(getString(R.string.book_button_text_out));
                                                                    break;
                                                                }
                                                                case RftcErrRevPwrLevTooHigh: {
                                                                    CommonUtils.make(StockOutDocumentDetailsActivity.this, getString(R.string.idInventoryStopped__PleaseMoveTheTagAndTryAgain));
                                                                    break;
                                                                }
                                                                default: {
                                                                    CommonUtils.make(StockOutDocumentDetailsActivity.this, getString(R.string.idInventoryFinished_Colon) + String.format(Locale.ENGLISH, "0x%x", errorId));
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    });

                                                }

                                                @Override
                                                public void onNewErrorReport(int errorCode, StUhf.InterrogatorModelC.UmcErrorCode umcErrorCode) {
                                                    if (umcErrorCode != null) {
                                                        CommonUtils.make(StockOutDocumentDetailsActivity.this, "event:" + umcErrorCode);
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
        this.lv_stock_list.setAdapter(listViewDataAdapter);
        getStockDetail(extras.getString("stockOutId"));

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

    protected StUhf.UII startInventorySingleStep() {
        return BookApplication.uhfInterfaceAsModelC().inventorySingleStep();
    }

    private void getStockDetail(String stockId) {
        this.showLoading(getString(R.string.common_loading_message));
        StockOutRequestDetailVo stockOutRequestVo = new StockOutRequestDetailVo();
        StockOutRequestDetailVo.RequestData requestData = new StockOutRequestDetailVo.RequestData();
        if ("2".equals(extras.getString("type"))) {
            requestData.setAduit(true);//获取核查列表详情
        } else {
            requestData.setAduit(false);//获取拣货列表详情
        }
        requestData.setStockOutDocmentID(stockId);
        stockOutRequestVo.setRequestData(requestData);
        stockOutRequestVo.setReqMethod("AppGetStockOutDocumentDetails");
        stockOutRequestVo.sign();
        Call<StockOutResultVo<StockOutDetailVo>> storeHouse = getApis().appGetStockOutDocumentDetails(stockOutRequestVo).clone();
        storeHouse.enqueue(new Callback<StockOutResultVo<StockOutDetailVo>>() {

            @Override
            public void onResponse(Response<StockOutResultVo<StockOutDetailVo>> response, Retrofit retrofit) {
                hideLoading();
                if (response.isSuccess() && response.body() != null) {
                    StockOutResultVo<StockOutDetailVo> body = response.body();
                    if (!response.body().isError()) {
                        List<StockOutDetailVo> storeHouseVos = body.getData();
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
