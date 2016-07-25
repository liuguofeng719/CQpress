package com.cqpress.book;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.cqpress.book.uhf.Configuration;
import com.cqpress.book.uhf.ExceptionForToast;
import com.cqpress.book.utils.CrashHandler;
import com.cqpress.book.utils.TLog;
import com.senter.support.openapi.StUhf;

public class BookApplication extends Application {

    public static BookApplication bookApplication;
    public static final String TAG = "MainApp";
    private static StUhf uhf;
    private static Configuration mAppConfiguration;

    @Override
    public void onCreate() {
        super.onCreate();
        bookApplication = this;
        //TODO 禁用和启用Log
        TLog.enableLog();
//        TLog.disableLog();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
    }

    public static BookApplication getBookApplication() {
        return bookApplication;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    /**
     * create a uhf instance with the specified model if need
     */
    public static StUhf getUhf(StUhf.InterrogatorModel interrogatorModel) {
        if (uhf == null) {
            uhf = StUhf.getUhfInstance(interrogatorModel);
            uhfInterfaceAsModel = interrogatorModel;
        }
        return uhf;
    }

    public static StUhf getUhfWithDetectionAutomaticallyIfNeed() {
        if (uhf == null) {
            try {
                StUhf rf = null;
                if (appCfgSavedModel() == null) {
                    rf = StUhf.getUhfInstance();
                } else {
                    rf = StUhf.getUhfInstance(appCfgSavedModel());
                }

                StUhf.InterrogatorModel model = rf.getInterrogatorModel();
                uhf = rf;
                uhfInterfaceAsModel = model;
                appCfgSaveModel(model);
            } catch (Exception e) {
                TLog.e(TAG, e.getMessage());
            }
        }
        return uhf;
    }

    public static StUhf uhf() {
        return uhf;
    }

    public static void uhfInit() throws ExceptionForToast {
        Log.i(TAG, "App.uhfInit()");
        if (uhf == null) {
            throw new ExceptionForToast("no device found ,so can not init it ");
        }
        boolean inited = uhf.init();
        if (inited == false) {
            throw new ExceptionForToast(
                    "can not init uhf module,please try again");
        }
    }

    public static void uhfUninit() {
        Log.i(TAG, "App.uhfUninit()");
        if (uhf == null) {
            return;
        }
        Log.i(TAG, "App.uhfUninit().uninit");
        uhf.uninit();
    }

    public static void uhfClear() {
        uhf = null;
        uhfInterfaceAsModel = null;
    }

    private static StUhf.InterrogatorModel uhfInterfaceAsModel;

    public static StUhf.InterrogatorModel uhfInterfaceAsModel() {
        if (uhf == null || uhfInterfaceAsModel == null) {
            throw new IllegalStateException();
        }
        return uhfInterfaceAsModel;
    }


    public static StUhf.InterrogatorModelC uhfInterfaceAsModelC() {
        assetUhfInstanceObtained();
        assert (uhfInterfaceAsModel() == StUhf.InterrogatorModel.InterrogatorModelC);
        return uhf.getInterrogatorAs(StUhf.InterrogatorModelC.class);
    }


    private static void assetUhfInstanceObtained() {
        if (uhf == null || uhfInterfaceAsModel == null) {
            throw new IllegalStateException();
        }
    }

    /**
     * stop the operation excuting by module,three times if need.
     */
    public static boolean stop() {
        if (uhf != null) {
            if (uhf.isFunctionSupported(StUhf.Function.StopOperation)) {
                for (int i = 0; i < 3; i++) {
                    if (uhf().stopOperation()) {
                        return true;
                    }
                }
                return false;
            }
        }
        return true;
    }

    /**
     * clear both mask settings
     */
    public static void clearMaskSettings() {
        if (uhf.isFunctionSupported(StUhf.Function.DisableMaskSettings)) {
            uhf.disableMaskSettings();
        }
    }

    public static final StUhf.InterrogatorModel appCfgSavedModel() {
        String modelName = appConfiguration().getString("modelName", "");
        if (modelName.length() != 0) {
            try {
                return StUhf.InterrogatorModel.valueOf(modelName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static final void appCfgSaveModelClear() {
        appConfiguration().setString("modelName", "");
    }

    public static final void appCfgSaveModel(StUhf.InterrogatorModel model) {
        if (model == null) {
            throw new NullPointerException();
        }
        appConfiguration().setString("modelName", model.name());
    }

    private static final Configuration appConfiguration() {
        if (mAppConfiguration == null) {
            mAppConfiguration = new Configuration(bookApplication, "settings",
                    Context.MODE_PRIVATE);
        }
        return mAppConfiguration;
    }

}
