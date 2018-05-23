package com.zgkj.common.widgets.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.zgkj.common.R;
import com.zgkj.common.widgets.recycler.callback.AdapterCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Descr:   RecyclerView自定义适配器的封装类
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2017-12-6.
 */

public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder<T>>
        implements AdapterCallback<T>,
        View.OnClickListener,
        View.OnLongClickListener {

    /**
     * 需要适配的数据集合对象
     */
    private final List<T> mDataList;
    private List<T> temp;

    // 创建item行的点击和长按的事件监听对象
    private AdapterListener mAdapterListener;


    /**
     * 构造函数
     */
    public RecyclerViewAdapter() {
        this(null);
    }

    public RecyclerViewAdapter(AdapterListener<T> adapterListener) {
        this(new ArrayList<T>(), adapterListener);
    }

    public RecyclerViewAdapter(List<T> dataList, AdapterListener<T> adapterListener) {
        this.mDataList = dataList;
        this.temp=dataList;
        this.mAdapterListener = adapterListener;
    }


    /**
     * 返回ItemView的类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * 获取Item布局类型的抽象方法，此方法由继承的子类去实现
     *
     * @param position 当前item行的索引值
     * @param data     当前item类型对应的数据
     * @return item视图布局XML文件对应的ID，用于创建ViewHolder
     */
    protected abstract int getItemViewType(int position, T data);

    /**
     * 创建一个ViewHolder对象
     *
     * @param parent   RecyclerView
     * @param viewType 界面的类型，这里简化约定为Item项XML布局文件的ID
     * @return
     */
    @Override
    public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建一个布局加载器的对象
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 加载Item布局文件
        View view = inflater.inflate(viewType, parent, false);

        // 通过抽象方法得到一个ViewHolder对象
        ViewHolder<T> holder = getViewHolder(view, viewType);

        // 初始化ViewHolder数据更新的回调接口对象
        holder.setAdapterCallback(this);

        // 设置view的Tag为ViewHolder，进行双向绑定
        view.setTag(R.id.tag_recycler_holder, holder);

        // Item根据自身需求判断是否为view视图对象设置点击事件
        if (holder.isNeedClick()){
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }
        // 返回一个自定义的ViewHolder的对象
        return holder;
    }


    /**
     * 创建一个自定义ViewHolder对象的方法，由继承的子类去实现
     *
     * @param view     当前Item的布局视图
     * @param viewType 布局类型，其实就是XML布局文件的ID
     * @return
     */
    protected abstract ViewHolder<T> getViewHolder(View view, int viewType);


    /**
     * 绑定数据到一个ViewHolder上面
     *
     * @param holder   需要绑定数据的ViewHolder对象
     * @param position 当前item行的索引值
     */
    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position) {
        // 得到需要绑定的数据
        T data = mDataList.get(position);
        // 触发ViewHolder对象绑定数据的方法，进行数据绑定
        holder.bind(data,position);
    }

    /**
     * 获取当前适配器item行的个数
     *
     * @return 当前适配器item行的个数
     */
    @Override
    public int getItemCount() {

        return mDataList.size();
    }


    /**
     * 返回整个数据集合对象
     *
     * @return 集合对象
     */
    public List<T> getDataList() {

        return mDataList;
    }


    /**
     * 添加一条数据并通知更新
     *
     * @param data 需要添加的数据
     */
    public void add(T data) {

        mDataList.add(data);

        // 通知适配器刷新显示
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 添加一堆数据（数组存放）
     *
     * @param datas 需要添加的数据对象
     */
    public void add(T... datas) {
        // 校验数据
        if (datas != null && datas.length > 0) {

            int startPosition = mDataList.size();

            Collections.addAll(mDataList, datas);

            // 通知适配器刷新显示
            notifyItemRangeInserted(startPosition, datas.length);
        }
    }


    /**
     * 添加一堆数据（集合存放）
     *
     * @param datas 需要添加的集合对象
     */
    public void add(Collection<T> datas) {
        // 校验数据
        if (datas != null && datas.size() > 0) {
            int startPosition = mDataList.size();

            for (T t : mDataList) {
                if (datas.contains(t)) {
                    datas.remove(t);
                }
            }
            mDataList.addAll(datas);

            // 通知适配器刷新数据
            notifyItemRangeInserted(startPosition, datas.size());
        }

    }


    /**
     * 替换为一个新的集合，里面包含了清空
     *
     * @param datas 需要添加的集合对象
     */
    public void replace(Collection<T> datas) {
        // 校验数据
        // 如果需要替换的数据集合为空或者无数据则直接返回
        if (datas == null || datas.size() <= 0) {
            return;
        }
        // 开始替换数据
        mDataList.clear();
        mDataList.addAll(datas);

        // 通知适配器刷新数据
        notifyDataSetChanged();
    }

    /**
     * 删除集合数据的操作
     */
    public void clear() {
        if (mDataList != null) {
            if (mDataList.size() > 0) {
                mDataList.clear();
            }
            notifyDataSetChanged();
        }
    }


    /**
     * item点击事件
     *
     * @param view 当前item的视图对象
     */
    @Override
    public void onClick(View view) {
        // 获取ViewHolder的实例对象
        ViewHolder holder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);

        if (mAdapterListener != null) {
            // 得到当前item的索引值
            int position = holder.getAdapterPosition();
            // 回调方法
            mAdapterListener.onItemClick(holder, mDataList.get(position));
        }
    }

    /**
     * item长按事件
     *
     * @param view 当前item的视图对象
     * @return
     */
    @Override
    public boolean onLongClick(View view) {
        // 获取ViewHolder的实例对象
        ViewHolder<T> holder = (ViewHolder<T>) view.getTag(R.id.tag_recycler_holder);

        if (mAdapterListener != null) {
            // 得到当前item的索引值
            int position = holder.getAdapterPosition();
            // 回调方法
            mAdapterListener.onItemLongClick(holder, mDataList.get(position));

            return true;
        }

        return false;
    }


    /**
     * 更新item行数据的显示
     *
     * @param data   需要更新显示的泛型数据
     * @param holder 当前适配的ViewHolder对象
     */
    @Override
    public void update(T data, ViewHolder<T> holder) {
        // 得到当前item的索引值
        int position = holder.getAdapterPosition();
        // 如果存在显示的行
        if (position >= 0) {
            // 进行数据的移除与更新
            mDataList.remove(position);
            mDataList.add(position, data);

            // 通知适配器刷新数据显示
            notifyDataSetChanged();
        }

    }

    /**
     * 初始化适配器对象的方法
     *
     * @param adapterListener
     */
    public void setAdapterListener(AdapterListener<T> adapterListener) {
        mAdapterListener = adapterListener;
    }


    /**
     * 内部自定义的监听器
     *
     * @param <T> 泛型数据
     */
    public interface AdapterListener<T> {

        /**
         * item点击的时候触发
         *
         * @param holder
         * @param data
         */
        void onItemClick(ViewHolder<T> holder, T data);

        /**
         * item长按的时候触发
         *
         * @param holder
         * @param data
         */
        void onItemLongClick(ViewHolder<T> holder, T data);
    }


    /**
     * 内部自定义的ViewHolder抽象类
     *
     * @param <T> 泛型的数据类型
     */
    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {
        // 上下文对象
        protected Context mContext;

        // 当前显示的布局
        protected View mItemView;

        // 需要显示的数据
        protected T mData;

        // 创建数据更新的回调监听器的对象
        private AdapterCallback<T> mAdapterCallback;

        // 当前的Item的索引
        protected int mPosition;

        /**
         * 构造方法
         *
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mContext = itemView.getContext();
            mPosition = getAdapterPosition();
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data 被绑定的数据对象
         */
        void bind(T data,int position) {
            mData = data;

            // 调用触发绑定事件的方法
            onBind(data,position);

        }

        /**
         * 当触发绑定数据的时候的抽象回调方法，
         * 必须复写该方法（此方法是显示控件与需要适配的数据进行绑定时执行的方法）
         *
         * @param data 被绑定的数据对象
         */
        protected abstract void onBind(T data,int position);
        /**
         * 更新数据（ViewHolder自己对自己的数据进行更新操作）
         *
         * @param data 需要更新的数据对象
         */
        public void updateData(T data) {

            if (mAdapterCallback != null) {
                mAdapterCallback.update(data, this);
            }
        }


        /**
         * 初始化回调接口对象
         *
         * @param adapterCallback 回调接口对象
         */
        public void setAdapterCallback(AdapterCallback<T> adapterCallback) {
            mAdapterCallback = adapterCallback;
        }


        /**
         * Item项是否能够点击
         *
         * @return
         */
        public abstract boolean isNeedClick();

    }


    /**
     * 对回调接口对象AdapterListener监听器做一次实现
     *
     * @param <T> 泛型数据
     */
    public static abstract class AdapterListenerImpl<T> implements AdapterListener<T> {

        @Override
        public void onItemClick(ViewHolder<T> holder, T data) {

        }

        @Override
        public void onItemLongClick(ViewHolder<T> holder, T data) {

        }
    }


}
