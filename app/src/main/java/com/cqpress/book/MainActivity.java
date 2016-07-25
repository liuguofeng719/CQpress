package com.cqpress.book;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cqpress.book.uhf.DataTransfer;
import com.cqpress.book.uhf.ExceptionForToast;
import com.senter.support.openapi.StUhf;
import com.senter.support.openapi.StUhf.AccessPassword;
import com.senter.support.openapi.StUhf.Bank;
import com.senter.support.openapi.StUhf.UII;

public class MainActivity extends Activity {

    private Button uhf_code_button;
    private TextView uhf_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uhf_activity);
        uhf_code = (TextView) findViewById(R.id.uhf_code);
        uhf_code_button = (Button) findViewById(R.id.uhf_code_button);
        uhf_code_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBtnRead();
            }
        });

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
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, 0, 0, "clear remembered model");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getGroupId()) {
            case 0: {
                switch (item.getItemId()) {
                    case 0: {
                        switch (item.getOrder()) {
                            case 0: {
                                BookApplication.uhfUninit();
                                BookApplication.uhfClear();
                                BookApplication.appCfgSaveModelClear();
                                System.exit(0);
                                finish();
                                return true;
                            }
                            default:
                                break;
                        }
                        break;
                    }
                    default:
                        break;
                }
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onBtnRead() {

        final Bank bank;
        final int ptr;
        final int cnt;
        bank = Bank.ValueOf(1);
        try {
            ptr = Integer.valueOf(2);
        } catch (Exception e) {
            Toast.makeText(this, "Please input correct read address",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            cnt = Integer.valueOf(6);
        } catch (Exception e) {
            Toast.makeText(this, "Please input correct read length",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        onRead(bank, ptr, cnt);
    }

    protected void onRead(final Bank bank, final int ptr, final int cnt) {
        //第一种方式
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                StUhf.Result.ReadResult readResult = BookApplication.uhfInterfaceAsModelC().readDataFromSingleTag(getAccessPassword(), bank, ptr, cnt);
//                UII uii = readResult.getUii();
//
//                byte[] dataShow = readResult.getData();
//                if (dataShow == null) {
//                    dataShow = new byte[]{};
//                }
//                String string = uii != null ? DataTransfer.xGetString(uii.getBytes()) : "unknown" + "\r\n" + dataShow.length / 2 + " " + DataTransfer.xGetString(dataShow);
//                uhf_code.setText(string);
//            }
//        });

        //第二种方式
        BookApplication.uhfInterfaceAsModelC().startInventorySingleTag(new StUhf.InterrogatorModelC.UmcOnNewUiiInventoried() {
            @Override
            public void onNewTagInventoried(UII uii) {
                addTag(uii);
            }
            @Override
            public void onEnd(int errorId) {

            }
        });
    }

    private void addTag(final UII uii) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String string = DataTransfer.xGetString(uii.getBytes());
                uhf_code.setText(string);
            }
        });
    }

    public AccessPassword getAccessPassword() {
        String pwd = "00000000";
        pwd = pwd.replaceAll(" ", "");
        if (pwd.length() == 8) {
            byte[] bs = DataTransfer.getBytesByHexString(pwd);
            if (bs == null || bs.length != 4) {
                return null;
            }
            AccessPassword ap = AccessPassword.getNewInstance(bs);
            return ap;
        }
        return null;
    }
}
