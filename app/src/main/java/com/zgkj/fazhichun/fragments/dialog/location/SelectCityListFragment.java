package com.zgkj.fazhichun.fragments.dialog.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;
import com.zgkj.common.http.AsyncHttpPostFormData;
import com.zgkj.common.http.AsyncHttpResponse;
import com.zgkj.common.http.AsyncOkHttpClient;
import com.zgkj.common.http.AsyncResponseHandler;
import com.zgkj.common.utils.DensityUtil;
import com.zgkj.common.utils.PingYinUtil;
import com.zgkj.common.widgets.load.LoadFactory;
import com.zgkj.common.widgets.load.core.LoadManager;
import com.zgkj.common.widgets.load.view.AbsView;
import com.zgkj.common.widgets.text.DrawableCenterTextView;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.fazhichun.App;
import com.zgkj.common.widgets.LetterListView;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.city.CityListAdapter;
import com.zgkj.fazhichun.entity.city.City;
import com.zgkj.fazhichun.entity.city.CityInfo;
import com.zgkj.fazhichun.view.EmptyView;
import com.zgkj.fazhichun.view.LoadingView;
import com.zgkj.fazhichun.view.NetErrorView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Descr:  选择城市
 */

public class SelectCityListFragment extends DialogFragment implements LetterListView.OnTouchingLetterChangedListener, AbsListView.OnScrollListener, View.OnClickListener {

    /**
     * UI
     */
    private ImageView mBackView;
    private DrawableCenterTextView mLocationCity;

    /**
     * DATA
     */
    // 沉浸式状态栏
    private ImmersionBar mImmersionBar;
    // 创建一个加载管理对象
    private LoadManager mLoadManager;


    private ListView city_container;
    private LetterListView letter_container;
    private RelativeLayout city_content;

    private List<CityInfo> allCities = new ArrayList<>();
    private Map<String, Integer> letterIndex = new HashMap<>();
    private CityListAdapter cityListAdapter;


    private TextView letterOverlay; // 对话框首字母textview
    private OverlayThread overlayThread; // 显示首字母对话框

    private boolean isScroll;
    private boolean isOverlayReady;

    /**
     * 显示城市选择的碎片
     *
     * @return
     */
    public static SelectCityListFragment newInstance() {
        SelectCityListFragment selectCityListFragment = new SelectCityListFragment();
        return selectCityListFragment;
    }


    @SuppressLint("NewApi")
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        window.setWindowAnimations(R.style.CityPickerDialogFragmentAnimStyle);

        View decorView = window.getDecorView();
        decorView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        decorView.setPadding(0, 0, 0, 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 设置无标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 初始化显示视图
        View view = inflater.inflate(R.layout.dialog_select_city, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 设置沉浸式状态栏
        mImmersionBar = ImmersionBar.with(this, getDialog())
                .statusBarColor(R.color.colorPrimary)
                .statusBarDarkFont(true);
        mImmersionBar.init();
        handler = new Handler();
        // 初始化显示控件
        initWidgets(view);
        // 初始化数据
        initDatas();
        getCity();


    }

    /**
     * 城市获取
     */
    private void getCity() {
        AsyncOkHttpClient okHttpClient = new AsyncOkHttpClient();
        AsyncHttpPostFormData AsyncHttpPostFormData = new AsyncHttpPostFormData();
        okHttpClient.post("/v1/city/city-list", AsyncHttpPostFormData, new AsyncResponseHandler() {
            @Override
            public void onFailure(IOException e) {
                mLoadManager.showStateView(NetErrorView.class);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(AsyncHttpResponse response) {
                Log.i("CITY", "onSuccess:城市获取" + response.toString());
                Type type = new TypeToken<RspModel<List<CityInfo>>>() {
                }.getType();
                List<CityInfo> value = getAnalysis(response, type, "城市获取");
                for(int i=0;i<value.size();i++){
                    value.get(i).setRegion_pinyin(PingYinUtil.getPingYin(value.get(i).getRegion_name()));
                }
                allCities.addAll(value);
                if (value != null && !"[]".equals(value.toString())) {
                    cityListAdapter = new CityListAdapter(getContext(), allCities, letterIndex);
                    city_container.setAdapter(cityListAdapter);
                } else {
                    mLoadManager.showStateView(EmptyView.class);
                }
            }
        });
    }

    /**
     * 数据解析
     *
     * @param response
     * @param type
     * @param log
     * @param <T>
     * @return
     */
    private <T> T getAnalysis(AsyncHttpResponse response, Type type, String log) {
        switch (response.getCode()) {
            case 200:
                try {
                    Gson gson = new Gson();
                    String data = response.getBody();
                    String value = "\\],\"[a-zA-Z]\":\\[";
                    data = data.replaceAll(value, ",").replace("{\"A\":", "").replace("]}", "]");
                    for (int i = 0; i < data.length(); i += 4000) {
                        //当前截取的长度<总长度则继续截取最大的长度来打印
                        if (i + 4000 < data.length()) {
                            Log.i("msg" + i, data.substring(i, i + 4000));
                        } else {
                            //当前截取的长度已经超过了总长度，则打印出剩下的全部信息
                            Log.i("msg" + i, data.substring(i, data.length()));
                        }
                    }
                    RspModel<T> rspModel = gson.fromJson(data, type);
                    Log.i("CITY", "onSuccess: " + log + rspModel.toString());
                    switch (rspModel.getCode()) {
                        case 1:
                            mLoadManager.showStateView(EmptyView.class);
                            break;
                        case 200:
                            mLoadManager.showSuccessView();
                            return rspModel.getData();
                        default:
                            App.showMessage("错误码：" + rspModel.getCode());
                            mLoadManager.showStateView(EmptyView.class);
                            break;
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                break;
        }
        return null;
    }


    private void initWidgets(View view) {
        mBackView = view.findViewById(R.id.back);
        mLocationCity = view.findViewById(R.id.location_city);
        city_content=view.findViewById(R.id.city_content);
        city_container = (ListView) view.findViewById(R.id.city_container);
        letter_container = (LetterListView) view.findViewById(R.id.letter_container);
        mBackView.setOnClickListener(this);

        // 初始化页面加载管理器
        mLoadManager = LoadFactory.getInstance().register(city_content, new AbsView.OnViewChildClickListener() {
            @Override
            public void onChildClick(View view) {
                // 显示加载中
                mLoadManager.showStateView(LoadingView.class);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        initCity();
        setupView();
        initOverlay();
    }


    private void initCity() {
        CityInfo city = new CityInfo("定位", "0"); // 当前定位城市
        allCities.add(city);
        city = new CityInfo("全部", "1"); // 全部城市
        allCities.add(city);
    }

    /**
     * a-z排序
     */
    Comparator comparator = new Comparator<City>() {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }
        }
    };

    private void setupView() {
        city_container.setOnScrollListener(this);
        letter_container.setOnTouchingLetterChangedListener(this);
    }


    // 初始化汉语拼音首字母弹出提示框
    private void initOverlay() {
        overlayThread = new OverlayThread();
        isOverlayReady = true;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        letterOverlay = (TextView) inflater.inflate(R.layout.v_letter_overlay, null);
        letterOverlay.setVisibility(View.INVISIBLE);

        int width = DensityUtil.dp2px(getContext(), 65);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                width, width,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        WindowManager windowManager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(letterOverlay, lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                // 关闭dialog
                close();
                break;
            case R.id.location_city:


                break;
            default:
                break;
        }
    }

    /**
     * 实现关闭dialog的方法
     */
    private void close() {
        if (getDialog() != null && getDialog().isShowing()) {
            dismiss();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 销毁沉浸式状态栏对象
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING) {
            isScroll = true;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!isScroll) {
            return;
        }
        if (isOverlayReady) {
            String text;
            String name = allCities.get(firstVisibleItem).getRegion_name();
            String pinyin = allCities.get(firstVisibleItem).getRegion_pinyin();
            if (firstVisibleItem < 2) {
                text = name;
            } else {
                text = PingYinUtil.converterToFirstSpell(pinyin).substring(0, 1).toUpperCase();
            }
            Pattern pattern = Pattern.compile("^[A-Za-z]+$");
            if (pattern.matcher(text).matches()) {
                letterOverlay.setTextSize(40);
            } else {
                letterOverlay.setTextSize(20);
            }
            letterOverlay.setText(text);
            letterOverlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            // 延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 1000);
        }
    }

    private Handler handler;

    @Override
    public void onTouchingLetterChanged(String s) {
        isScroll = false;
        if (letterIndex.get(s) != null) {
            int position = letterIndex.get(s);
            city_container.setSelection(position);
            Pattern pattern = Pattern.compile("^[A-Za-z]+$");
            if (pattern.matcher(s).matches()) {
                letterOverlay.setTextSize(40);
            } else {
                letterOverlay.setTextSize(20);
            }
            letterOverlay.setText(s);
            letterOverlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            handler.postDelayed(overlayThread, 1000);
        }
    }

    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            letterOverlay.setVisibility(View.GONE);
        }
    }
}
