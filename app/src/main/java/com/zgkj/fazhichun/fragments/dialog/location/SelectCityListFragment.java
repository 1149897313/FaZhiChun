package com.zgkj.fazhichun.fragments.dialog.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.recycler.decoration.SpaceItemDecoration;
import com.zgkj.common.widgets.text.DrawableCenterTextView;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.factory.net.NewOkHttpClient;
import com.zgkj.fazhichun.City;
import com.zgkj.fazhichun.CityListAdapter;
import com.zgkj.fazhichun.DBHelper;
import com.zgkj.fazhichun.DatabaseHelper;
import com.zgkj.fazhichun.DensityUtil;
import com.zgkj.fazhichun.LetterListView;
import com.zgkj.fazhichun.PingYinUtil;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.TestActivity;
import com.zgkj.fazhichun.adapter.location.CityPickerAdapter;
import com.zgkj.fazhichun.entity.CityInfo;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.Callback;
import okhttp3.FormBody;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Descr:  选择城市
 */

public class SelectCityListFragment extends DialogFragment implements LetterListView.OnTouchingLetterChangedListener, AbsListView.OnScrollListener,View.OnClickListener{

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




    private ListView city_container;
    private LetterListView letter_container;

    private List<City> allCities = new ArrayList<>();
    private List<City> hotCities = new ArrayList<>();
    private List<String> historyCities = new ArrayList<>();
    private List<City> citiesData;
    private Map<String, Integer> letterIndex = new HashMap<>();
    private CityListAdapter cityListAdapter;


    private TextView letterOverlay; // 对话框首字母textview
    private OverlayThread overlayThread; // 显示首字母对话框
    private DatabaseHelper databaseHelper;

    private boolean isScroll;
    private boolean isOverlayReady;
    private Handler handler;

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

        // 初始化显示控件
        initWidgets(view);
        // 初始化数据
        initDatas();
        getCity();


    }

    private void getCity(){

        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        new NewOkHttpClient().init(formBody,"/v1/city/city-list").enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                if(response.code()==200){
                    try {
                        Gson gson = new Gson();
                        Type type = new TypeToken<RspModel<List<CityInfo>>>() {
                        }.getType();
                        String data=response.body().string();
                        String value="\\],\"[a-zA-Z]\":\\[";
                        data= data.replaceAll(value,",").replace("{\"A\":","").replace("]}","]");
//                        for (int i = 0; i < data.length(); i += 4000) {
//                            //当前截取的长度<总长度则继续截取最大的长度来打印
//                            if (i + 4000 < data.length()) {
//                                Log.i("msg" + i, data.substring(i, i + 4000));
//                            } else {
//                                //当前截取的长度已经超过了总长度，则打印出剩下的全部信息
//                                Log.i("msg" + i, data.substring(i, data.length()));
//                            }
//                        }
                        RspModel<List<City>> rspModel=gson.fromJson(data,type);
                        System.out.println(rspModel.toString());
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void initWidgets(View view) {
        mBackView = view.findViewById(R.id.back);
        mLocationCity = view.findViewById(R.id.location_city);
        city_container = (ListView) view.findViewById(R.id.city_container);
        letter_container = (LetterListView) view.findViewById(R.id.letter_container);


        mBackView.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        databaseHelper = new DatabaseHelper(getActivity());
        handler = new Handler();
        initCity();
        initHotCity();
        initHistoryCity();
        setupView();
        initOverlay();
    }


    private void initCity() {
        City city = new City("定位", "0"); // 当前定位城市
        allCities.add(city);
        city = new City("最近", "1"); // 最近访问的城市
        allCities.add(city);
        city = new City("热门", "2"); // 热门城市
        allCities.add(city);
        city = new City("全部", "3"); // 全部城市
        allCities.add(city);
        citiesData = getCityList();
        allCities.addAll(citiesData);
    }

    /**
     * 热门城市
     */
    public void initHotCity() {
        City city = new City("北京", "2");
        hotCities.add(city);
        city = new City("上海", "2");
        hotCities.add(city);
        city = new City("广州", "2");
        hotCities.add(city);
        city = new City("深圳", "2");
        hotCities.add(city);
        city = new City("武汉", "2");
        hotCities.add(city);
        city = new City("天津", "2");
        hotCities.add(city);
        city = new City("西安", "2");
        hotCities.add(city);
        city = new City("南京", "2");
        hotCities.add(city);
        city = new City("杭州", "2");
        hotCities.add(city);
        city = new City("成都", "2");
        hotCities.add(city);
        city = new City("重庆", "2");
        hotCities.add(city);
    }

    private void initHistoryCity() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from recent_city order by date desc limit 0, 3", null);
        while (cursor.moveToNext()) {
            historyCities.add(cursor.getString(1));
        }
        cursor.close();
        db.close();
    }

    public void addHistoryCity(String name) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from recent_city where name = '" + name + "'", null);
        if (cursor.getCount() > 0) {
            db.delete("recent_city", "name = ?", new String[]{name});
        }
        db.execSQL("insert into recent_city(name, date) values('" + name + "', " + System.currentTimeMillis() + ")");
        db.close();
    }


    private ArrayList<City> getCityList() {
        DBHelper dbHelper = new DBHelper(getContext());
        ArrayList<City> list = new ArrayList<>();
        try {
            dbHelper.createDataBase();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from city", null);
            City city;
            while (cursor.moveToNext()) {
                city = new City(cursor.getString(1), cursor.getString(2));
                list.add(city);
            }
            cursor.close();
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(list, comparator);
        return list;
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

        cityListAdapter = new CityListAdapter(getContext(), allCities, hotCities, historyCities, letterIndex);
        city_container.setAdapter(cityListAdapter);
    }

    private void setupActionBar() {
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
            String name = allCities.get(firstVisibleItem).getName();
            String pinyin = allCities.get(firstVisibleItem).getPinyin();
            if (firstVisibleItem < 4) {
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
