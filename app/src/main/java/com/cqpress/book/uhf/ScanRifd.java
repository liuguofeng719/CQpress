package com.cqpress.book.uhf;

import android.content.Context;

import com.cqpress.book.BookApplication;
import com.cqpress.book.R;
import com.senter.support.openapi.StUhf;

/**
 * Created by liuguofeng719 on 2016/7/12.
 */
public class ScanRifd {

    private final Accompaniment accompaniment;

    public ScanRifd(Context context) {
        //扫描成功的声音
        accompaniment = Accompaniment.newInstanceOfResource(context, R.raw.tag_inventoried);
    }

    /**
     * 获取
     * @return
     */
    public String getUii() {
        StUhf.UII uii = BookApplication.uhfInterfaceAsModelC().inventorySingleStep();
        return DataTransfer.xGetString(uii.getBytes());
    }

}
