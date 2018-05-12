package com.zgkj.common.widgets.recycler;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/28.
 * Descr:   实现对头部和尾部视图进行包装的适配器类
 */

public class HeaderAndFooterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 定义常量
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;


    // 定义存放头部视图的集合
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    // 定义存放尾部视图的集合
    private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();


    // 定义一个内部的适配器对象
    private RecyclerView.Adapter mInnerAdapter;


    /**
     * 构造方法，初始化内部适配器
     *
     * @param innerAdapter
     */
    public HeaderAndFooterWrapper(RecyclerView.Adapter innerAdapter) {
        mInnerAdapter = innerAdapter;
    }


    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPosition(position)) {        // 如果是头部视图类型
            // 返回对应的键名
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPosition(position)) {  // 如果是尾部视图类型
            return mFooterViews.keyAt(position - getHeaderViewCount() - getInnerAdapterItemCount());
        }
        // 否则返回内部适配器对象的视图类型
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return new WrapperViewHolder(mHeaderViews.get(viewType));
        } else if (mFooterViews.get(viewType) != null) {
            return new WrapperViewHolder(mFooterViews.get(viewType));
        }
        // 否则内部适配器调度自己的创建ViewHolder的方法
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPosition(position)) {
            return;
        }
        if (isFooterViewPosition(position)) {
            return;
        }
        // 内部适配器的索引位置的开始点在头部视图索引之后
        mInnerAdapter.onBindViewHolder(holder, position - getHeaderViewCount());
    }

    @Override
    public int getItemCount() {
        return getHeaderViewCount() + getInnerAdapterItemCount() + getFooterViewCount();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        // 内部适配器对象调用自己对应的方法
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        // 如果是网格布局
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    int itemViewType = getItemViewType(position);

                    if (mHeaderViews.get(itemViewType) != null) {

                        return gridLayoutManager.getSpanCount();
                    } else if (mFooterViews.get(itemViewType) != null) {

                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        mInnerAdapter.onViewAttachedToWindow(holder);

        int position = holder.getAdapterPosition();

        if (isHeaderViewPosition(position) || isFooterViewPosition(position)) {

            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();

            // 如果是瀑布流布局
            if (layoutParams != null &&
                    layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams staggeredLayoutParams
                        = (StaggeredGridLayoutManager.LayoutParams) layoutParams;

                // 设置占满
                staggeredLayoutParams.setFullSpan(true);
            }
        }

    }

    /**
     * 返回内部适配器对象适配器数据的个数
     *
     * @return
     */
    private int getInnerAdapterItemCount() {

        return mInnerAdapter == null ? 0 : mInnerAdapter.getItemCount();
    }


    /**
     * 返回头部布局视图的个数
     *
     * @return
     */
    private int getHeaderViewCount() {

        return mHeaderViews.size();
    }

    /**
     * 返回尾部视图的个数
     *
     * @return
     */
    private int getFooterViewCount() {

        return mFooterViews.size();
    }


    /**
     * 判断是否为头视图对应的索引值
     *
     * @param position 当前适配器适配数据对应的索引值
     * @return
     */
    private boolean isHeaderViewPosition(int position) {

        return position < getHeaderViewCount();
    }


    /**
     * 判断是否为尾部视图对应的索引值
     *
     * @param position
     * @return
     */
    private boolean isFooterViewPosition(int position) {

        return position >= getHeaderViewCount() + getInnerAdapterItemCount();
    }


    /**
     * 添加头部视图的方法
     *
     * @param headerView
     */
    public void addHeaderView(View headerView) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, headerView);
    }


    public void addFooterView(View footerView) {
        mFooterViews.put(mFooterViews.size() + BASE_ITEM_TYPE_FOOTER, footerView);
    }


    private static class WrapperViewHolder extends RecyclerView.ViewHolder {

        public WrapperViewHolder(View itemView) {
            super(itemView);
        }
    }


}
