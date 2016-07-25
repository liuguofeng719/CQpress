package com.cqpress.book.api;

import com.cqpress.book.bean.BaseRequestVo;
import com.cqpress.book.bean.BaseResultVo;
import com.cqpress.book.bean.LoginRequestVo;
import com.cqpress.book.bean.LoginResultVo;
import com.cqpress.book.bean.RegisterAppRequestVo;
import com.cqpress.book.bean.RegisterAppResultVo;
import com.cqpress.book.bean.ScanUpLoadRequestVo;
import com.cqpress.book.bean.ScanUpLoadResultVo;
import com.cqpress.book.bean.StockDocumentCompleteRequestVo;
import com.cqpress.book.bean.StockInDetailVo;
import com.cqpress.book.bean.StockInDocumentVo;
import com.cqpress.book.bean.StockOutDetailVo;
import com.cqpress.book.bean.StockOutRequestDetailVo;
import com.cqpress.book.bean.StockOutRequestVo;
import com.cqpress.book.bean.StockOutResultVo;
import com.cqpress.book.bean.StockOutVo;
import com.cqpress.book.bean.StoreHouseLocationRequestVo;
import com.cqpress.book.bean.StoreHouseLocationVo;
import com.cqpress.book.bean.StoreHouseRequestVo;
import com.cqpress.book.bean.StoreHouseResultVo;
import com.cqpress.book.bean.StoreHouseVo;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by liuguofeng719 on 2016/7/6.
 */
public interface Apis {

    String BASE_URL = "http://123.56.67.64:6789/SubDeviceService.svc/rest/";

    /**
     * 获取出库订单
     *
     * @return
     */
    @POST("AppGetStockOutDocuments")
    Call<StockOutResultVo<StockOutVo>> appGetStockOutDocuments(@Body StockOutRequestVo requestVo);

    /**
     * 出库订单详情
     *
     * @return
     */
    @POST("AppGetStockOutDocumentDetails")
    Call<StockOutResultVo<StockOutDetailVo>> appGetStockOutDocumentDetails(@Body StockOutRequestDetailVo requestVo);

    /**
     * 获取入库订单
     *
     * @return
     */
    @POST("AppGetStockInDocuments")
    Call<StockOutResultVo<StockInDocumentVo>> appGetStockInDocuments(@Body StockOutRequestVo requestVo);

    /**
     * 获取入库订单详情
     *
     * @return
     */
    @POST("AppGetStockInDocumentDetails")
    Call<StockOutResultVo<StockInDetailVo>> appGetStockInDocumentDetails(@Body StockOutRequestDetailVo requestVo);

    /**
     * 扫描上报数据
     *
     * @return
     */
    @POST("AppScanResultUpLoad")
    Call<ScanUpLoadResultVo> appScanResultUpLoad(@Body ScanUpLoadRequestVo requestVo);

    /**
     * 校验签名
     *
     * @param requestVo
     * @return
     */
    @POST("AppGet")
    Call<BaseResultVo> appGet(@Body BaseRequestVo requestVo);

    /**
     * 登陆
     *
     * @param requestVo
     * @return
     */
    @POST("AppUserLogin")
    Call<LoginResultVo> appUserLogin(@Body LoginRequestVo requestVo);

    /**
     * 注册app
     *
     * @param requestVo
     * @return
     */
    @POST("RegisterApp")
    Call<RegisterAppResultVo> registerApp(@Body RegisterAppRequestVo requestVo);

    /**
     * 库房查询（支持分页）
     *
     * @param storeHouseVo
     * @return
     */
    @POST("AppQueryStorehouseList")
    Call<StoreHouseResultVo<StoreHouseVo>> appQueryStorehouseList(@Body StoreHouseRequestVo storeHouseVo);

    /**
     * 根据库房ID查询库位（支持分页）
     *
     * @param queryStorehouseLocationVo
     * @return
     */
    @POST("AppQueryStorehouseLocationsList")
    Call<StoreHouseResultVo<StoreHouseLocationVo>> appQueryStorehouseLocationsList(@Body StoreHouseLocationRequestVo queryStorehouseLocationVo);

    /**
     * 手动设置扫描完成
     * @return
     */
    @POST("AppStockDocumentComplete")
    Call<BaseResultVo> appStockDocumentComplete(@Body StockDocumentCompleteRequestVo requestVo);

}
