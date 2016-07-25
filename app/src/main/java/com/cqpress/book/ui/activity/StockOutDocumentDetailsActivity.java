package com.cqpress.book.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.concurrent.atomic.AtomicInteger;

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

    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.tv_header_title)
    TextView tv_header_title;
    @Bind(R.id.lv_stock_list)
    ListView lv_stock_list;
    Bundle extras;

    private final Accompaniment accompaniment = Accompaniment.newInstanceOfResource(this, R.raw.tag_inventoried);

    private ListViewDataAdapter<StockOutDetailVo> listViewDataAdapter;
    private Handler accompainimentsHandler;
    private MyHandler myhandler = new MyHandler();

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

        this.tv_header_title.setText(getString(R.string.book_out_header_detail_title));
        listViewDataAdapter = new ListViewDataAdapter<StockOutDetailVo>(new ViewHolderCreator<StockOutDetailVo>() {

            @Override
            public ViewHolderBase<StockOutDetailVo> createViewHolder(int position) {
                return new ViewHolderBase<StockOutDetailVo>() {
                    TextView tv_book_name;
                    TextView tv_book_isbn;
                    TextView tv_book_scan;
                    TextView tv_book_amount;
                    TextView tv_scan_num;

                    @Override
                    public View createView(LayoutInflater layoutInflater) {
                        View view = layoutInflater.inflate(R.layout.book_out_list_detail_item_activity, null);
                        tv_book_name = ButterKnife.findById(view, R.id.tv_book_name);
                        tv_book_isbn = ButterKnife.findById(view, R.id.tv_book_isbn);
                        tv_book_scan = ButterKnife.findById(view, R.id.tv_book_scan);
                        tv_book_amount = ButterKnife.findById(view, R.id.tv_book_amount);
                        tv_scan_num = ButterKnife.findById(view, R.id.tv_scan_num);
                        return view;
                    }

                    @Override
                    public void showData(int position, StockOutDetailVo itemData) {
                        tv_book_name.setText(itemData.getBook().getBookName());
                        tv_book_isbn.setText(itemData.getBook().getIsbn());
                        tv_book_scan.setTag(itemData.getStockOutID());
                        tv_book_amount.setText("出库数量" + itemData.getAmount());
                        tv_book_scan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                if (tv_book_scan.getText().toString().equals("开始扫描")) {
                                    tv_book_scan.setText(getString(R.string.book_button_text_stop));
                                    final String vTag = v.getTag().toString();
                                    boolean antiCollision = BookApplication.uhfInterfaceAsModelC().startInventorySingleTag(new StUhf.InterrogatorModelC.UmcOnNewUiiInventoried() {
                                        final AtomicInteger atomicInteger = new AtomicInteger();

                                        @Override
                                        public void onNewTagInventoried(StUhf.UII uii) {
                                            if (uii != null) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        atomicInteger.decrementAndGet();
                                                        tv_scan_num.setText("已扫描数量" + atomicInteger.get());
                                                    }
                                                });
                                                String uiiStr = DataTransfer.xGetString(uii.getBytes());
                                                Message msg = Message.obtain();
                                                Bundle data = new Bundle();
                                                data.putString("stockOutId", vTag);
                                                data.putString("uiiStr", uiiStr);
                                                msg.setData(data);
                                                myhandler.sendMessage(msg);
                                            }
                                        }

                                        @Override
                                        public void onEnd(final int errorId) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    switch (StUhf.InterrogatorModelC.UmcErrorCode.ValueOf(errorId)) {
                                                        case Success: {
                                                            showToast(getString(R.string.idInventoryFinished));
                                                            break;
                                                        }
                                                        case RftcErrRevPwrLevTooHigh: {
                                                            showToast(getString(R.string.idInventoryStopped__PleaseMoveTheTagAndTryAgain));
                                                            break;
                                                        }
                                                        default: {
                                                            showToast(getString(R.string.idInventoryFinished_Colon) + String.format(Locale.ENGLISH, "0x%x", errorId));
                                                            break;
                                                        }
                                                    }
                                                }
                                            });

                                        }

                                        @Override
                                        public void onNewErrorReport(int errorCode, StUhf.InterrogatorModelC.UmcErrorCode umcErrorCode) {
                                            if (umcErrorCode != null) {
                                                showToast("event:" + umcErrorCode);
                                            }
                                        }
                                    });
                                    if (antiCollision) {
                                        BookApplication.stop();
                                    }
                                } else {
                                    tv_book_scan.setText(getString(R.string.book_button_text_out));
                                    BookApplication.stop();
                                }

//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        StUhf.UII uii = startInventorySingleStep();
//                                        if (uii != null) {
//                                            //触发铃声
//                                            trigTagAccompainiment();
//                                            //获取16进制
//                                            String uiiStr = DataTransfer.xGetString(uii.getBytes());
//                                            TLog.d(TAG_LOG, "StockOutDocument======" + uiiStr.toString());
//                                            Message msg = Message.obtain();
//                                            Bundle data = new Bundle();
//                                            data.putString("stockOutId", vTag);
//                                            data.putString("uiiStr", uiiStr);
//                                            msg.setData(data);
//                                            myhandler.sendMessage(msg);
//                                        } else {
//                                            CommonUtils.make(StockOutDocumentDetailsActivity.this, "请重新扫描");
//                                        }

//                            }
//                                }).start();

                            }
                        });
                    }
                };
            }
        });
        this.lv_stock_list.setAdapter(listViewDataAdapter);
        getStockDetail(extras.getString("stockOutId"));
    }

    class MyHandler extends Handler {

        public MyHandler() {
        }

        public MyHandler(Looper looper) {
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
        String stockOutId = msgData.getString("stockOutId");
        ScanUpLoadRequestVo requestVo = new ScanUpLoadRequestVo();
        final ScanUpLoadRequestVo.RequestData requestData = new ScanUpLoadRequestVo.RequestData();
        requestData.setDeviceID(AppPreferences.getString("registerCode"));
        //查找字符串
        if (uiiStr.toLowerCase().lastIndexOf("ff") != -1) {
            requestData.setPackageCode(uiiStr);
        } else {
            requestData.setEpc(uiiStr);
        }

        requestData.setStockOutDetailID(stockOutId);
        requestData.setUserID(AppPreferences.getString("userId"));
        requestData.setStockType("1");//出库
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
                        BookApplication.stop();
                        CommonUtils.make(StockOutDocumentDetailsActivity.this, "出库数量剩余数量为0");
                        getStockDetail(extras.getString("stockOutId"));//如果扫描完成重新拉取数据
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
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
        stockOutRequestVo.setRequestData(stockId);
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
