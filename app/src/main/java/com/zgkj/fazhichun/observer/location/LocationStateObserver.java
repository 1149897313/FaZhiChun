package com.zgkj.fazhichun.observer.location;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/3.
 * Descr:   定位状态的观察者操作类（单例模式）
 */
public class LocationStateObserver {

    private static volatile LocationStateObserver sInstance;


    private OnLocationStateListener mOnLocationStateListener;


    /**
     * 私有化的构造方法
     */
    private LocationStateObserver(){
    }


    /**
     * 返回定位状态的操作者对象
     *
     * @return
     */
    public static LocationStateObserver getInstance(){
        if (sInstance == null){
            synchronized (LocationStateObserver.class){
                if (sInstance == null){
                    sInstance = new LocationStateObserver();
                }
            }
        }
        return sInstance;
    }



    public void addObserver(OnLocationStateListener onLocationStateListener){
        if (mOnLocationStateListener == null){
            mOnLocationStateListener = onLocationStateListener;
        }
    }

    public void removeObserver(){
        if (mOnLocationStateListener != null){
            mOnLocationStateListener = null;
        }

    }


    /**
     * 通知观察者定位成功
     */
    public void notifyObserverLocationSuccess(){
        if (mOnLocationStateListener != null){
            mOnLocationStateListener.onLocationSuccess();
        }
    }

    /**
     * 通知观察者定位失败
     */
    public void notifyObserverLocationFailure(){
        if (mOnLocationStateListener != null){
            mOnLocationStateListener.onLocationFailure();
        }
    }




    /**
     * 定义一个定位状态的监听接口
     */
    public interface OnLocationStateListener {

        /**
         * 定位成功
         */
        void onLocationSuccess();

        /**
         * 定位失败
         */
        void onLocationFailure();

    }


}
