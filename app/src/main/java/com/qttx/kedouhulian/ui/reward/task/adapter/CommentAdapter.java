package com.qttx.kedouhulian.ui.reward.task.adapter;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.qttx.kedouhulian.R;
import com.qttx.kedouhulian.bean.TaskMsgBean;
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewAdapter;
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewHolder;

import java.util.List;

public class CommentAdapter extends NestFullViewAdapter<TaskMsgBean> {


    public CommentAdapter( List<TaskMsgBean> result) {
        super(R.layout.forum_list_item_commit_reply, result);
    }

    @Override
    public void onBind(int pos, TaskMsgBean bean, NestFullViewHolder holder) {
        TextView reply_tv = holder.getView(R.id.reply_tv);
        reply_tv.setText("");
        String user_name = bean.getNickname();

        String to_user = bean.getB_nickname() + ":";

        SpannableStringBuilder style = new SpannableStringBuilder(
                user_name);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#5A86F4")),
                0, user_name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        reply_tv.append(style);

        if (TextUtils.isEmpty(bean.getB_nickname())) {//发表的评论
            reply_tv.append(":");
        } else {
            reply_tv.append("回复");
            SpannableStringBuilder tostyle = new SpannableStringBuilder(
                    to_user);
            tostyle.setSpan(new ForegroundColorSpan(Color.parseColor("#5A86F4")),
                    0, to_user.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            reply_tv.append(tostyle);
        }
        reply_tv.append("  " + bean.getContent());

    }

}
