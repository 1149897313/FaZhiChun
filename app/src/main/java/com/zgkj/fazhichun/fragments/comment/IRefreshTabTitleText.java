package com.zgkj.fazhichun.fragments.comment;

import com.zgkj.common.app.Fragment;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/3/1.
 * Descr:   定义一个刷新评论页面Tab文字显示的接口
 */

public interface IRefreshTabTitleText {

    /**
     * 回调方法
     *
     * @param fragment
     * @param msgNumber
     */
    void RefreshTabTitleText(Fragment fragment, int msgNumber);


}
