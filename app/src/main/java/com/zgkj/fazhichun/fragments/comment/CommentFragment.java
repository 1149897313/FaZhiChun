package com.zgkj.fazhichun.fragments.comment;

import com.zgkj.common.app.Fragment;
import com.zgkj.fazhichun.R;

/**
 * Author:  bozaixing.
 * Email:   654152983@qq.com.
 * Date:    2018/4/20.
 * Descr:   用户进行评论界面的碎片显示对象
 */
public class CommentFragment extends Fragment {



    public static CommentFragment newInstance(){
        CommentFragment fragment = new CommentFragment();

        return fragment;
    }


    @Override
    protected int getLayoutSourceId() {

        return R.layout.fragment_comment;


    }
}
