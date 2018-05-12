package com.zgkj.fazhichun.fragments.dialog.location;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;
import com.zgkj.common.widgets.recycler.RecyclerViewAdapter;
import com.zgkj.common.widgets.recycler.decoration.SpaceItemDecoration;
import com.zgkj.common.widgets.text.DrawableCenterTextView;
import com.zgkj.factory.model.api.RspModel;
import com.zgkj.factory.model.api.home.City;
import com.zgkj.factory.net.NewOkHttpClient;
import com.zgkj.fazhichun.R;
import com.zgkj.fazhichun.adapter.location.CityPickerAdapter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.FormBody;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/22.
 * Descr:   城市选择DialogFragment碎片的定义
 */

public class CityPickerDialogFragment extends DialogFragment implements View.OnClickListener {

    /**
     * UI
     */
    private ImageView mBackView;
    private DrawableCenterTextView mLocationCity;
    private RecyclerView mRecyclerView;

    /**
     * DATA
     */
    // 沉浸式状态栏
    private ImmersionBar mImmersionBar;
    private CityPickerAdapter mCityPickerAdapter;


    /**
     * 显示城市选择的碎片
     *
     * @return
     */
    public static CityPickerDialogFragment newInstance() {
        CityPickerDialogFragment cityPickerDialogFragment = new CityPickerDialogFragment();
        return cityPickerDialogFragment;
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
        View view = inflater.inflate(R.layout.dialog_city_picker, container, false);
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

                System.out.println("----response.body().string():"+response.toString());
                System.out.println("----response.body().string():"+response.body().string());
                if(response.code()==200){
//                    try {
//                        Gson gson = new Gson();
//                        Type type = new TypeToken<RspModel<List<City>>>() {
//                        }.getType();
//                        RspModel<List<City>> rspModel=gson.fromJson(response.body().string(),type);
//                        System.out.println(rspModel.toString());
//
//                    } catch (JsonSyntaxException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
    }


    private void initWidgets(View view) {
        mBackView = view.findViewById(R.id.back);
        mLocationCity = view.findViewById(R.id.location_city);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        mBackView.setOnClickListener(this);
        mLocationCity.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(getContext(), 10));
        mCityPickerAdapter = new CityPickerAdapter(new RecyclerViewAdapter.AdapterListenerImpl<String>() {
            @Override
            public void onItemClick(RecyclerViewAdapter.ViewHolder<String> holder, String data) {
                super.onItemClick(holder, data);
            }
        });
        mRecyclerView.setAdapter(mCityPickerAdapter);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {

        loadCity();
    }


    private void loadCity() {
        List<String> list = new ArrayList<>();
        list.add("上海");
        list.add("北京");
        list.add("广州");
        list.add("深圳");
        list.add("杭州");
        list.add("西安");
        list.add("成都");
        list.add("武汉");
        list.add("南京");
        list.add("天津");

        mCityPickerAdapter.replace(list);
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


}
