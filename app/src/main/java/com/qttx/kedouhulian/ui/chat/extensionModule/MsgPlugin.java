package com.qttx.kedouhulian.ui.chat.extensionModule;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.qttx.kedouhulian.R;
import com.qttx.kedouhulian.ui.chat.ChatCustomMsgActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

public class MsgPlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.zidingyi_btn);
    }

    @Override
    public String obtainTitle(Context context) {
        return "自定义消息";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        Intent intent;
        intent = new Intent(fragment.getActivity(), ChatCustomMsgActivity.class);
        intent.putExtra("id", rongExtension.getTargetId());//聊天ID
        fragment.getActivity().startActivityForResult(intent,200);
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
