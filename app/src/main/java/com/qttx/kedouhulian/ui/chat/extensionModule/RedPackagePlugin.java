package com.qttx.kedouhulian.ui.chat.extensionModule;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.qttx.kedouhulian.R;
import com.qttx.kedouhulian.ui.chat.ZhuanZhangActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

public class RedPackagePlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.zhuanzhang_btn);
    }

    @Override
    public String obtainTitle(Context context) {
        return "转账";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        Intent intent;
        if ("PRIVATE".equals(rongExtension.getConversationType().toString())){
            intent = new Intent(fragment.getActivity(), ZhuanZhangActivity.class);
            intent.putExtra("id", rongExtension.getTargetId());//聊天ID
            fragment.startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
