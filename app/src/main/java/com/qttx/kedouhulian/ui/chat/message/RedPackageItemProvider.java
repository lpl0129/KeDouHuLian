package com.qttx.kedouhulian.ui.chat.message;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qttx.kedouhulian.R;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

@ProviderTag(messageContent = RedMessage.class, showProgress = false, showReadState = true)
public class RedPackageItemProvider extends IContainerItemProvider.MessageProvider<RedMessage> {

    private static class ViewHolder {
        LinearLayout redBagLl;
        TextView blessWordsTv;
        TextView bless_money_tv;
        TextView redBagTypeTv;
        TextView leftTv;
        TextView rightTv;
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.red_package_message_layout, null);
        ViewHolder holder = new ViewHolder();
        holder.redBagLl = (LinearLayout) view.findViewById(R.id.red_bag_layout);
        holder.blessWordsTv = (TextView) view.findViewById(R.id.bless_words_tv);
        holder.bless_money_tv = (TextView) view.findViewById(R.id.bless_money_tv);

        holder.redBagTypeTv= (TextView) view.findViewById(R.id.red_bag_type_tv);
        holder.leftTv = (TextView) view.findViewById(R.id.left_place_tv);
        holder.rightTv = (TextView) view.findViewById(R.id.right_place_tv);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, RedMessage redMessage, UIMessage uiMessage) {
        final ViewHolder holder = (ViewHolder) view.getTag();

        //
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(uiMessage.getTargetId());
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的

            holder.redBagLl.setBackgroundResource(R.drawable.message_red_package_bg_right);
            holder.leftTv.setVisibility(View.GONE);
            holder.rightTv.setVisibility(View.VISIBLE);
            holder.blessWordsTv.setText("转账给"+userInfo.getName());
        } else {
            holder.redBagLl.setBackgroundResource(R.drawable.message_red_package_bg_left);
            holder.leftTv.setVisibility(View.VISIBLE);
            holder.rightTv.setVisibility(View.GONE);
            holder.blessWordsTv.setText("收到来自"+userInfo.getName()+"的转账");
        }
        holder.bless_money_tv.setText("¥"+redMessage.getMoney());

//        String packetType = redMessage.getPacketType();
//        if ("2".equals(packetType)) {
//            holder.redBagTypeTv.setText("普通红包");
//        } else if ("1".equals(packetType)) {
//            holder.redBagTypeTv.setText("群红包");
//        }
    }

    @Override
    public Spannable getContentSummary(RedMessage redMessage) {
        return new SpannableString("转账");
    }

    @Override
    public void onItemClick(View view, int i, RedMessage redMessage, UIMessage uiMessage) {
//        String redId = redMessage.getRedId();
        //成功状态:1=您已经领取过红包了,2=红包已经过期,3=红包已经领完,4=可以领取,5=没有红包可以领取了,6=您没有权限领取这个红包
//        if (!TextUtils.isEmpty(redId)) {
//            Intent intent = new Intent(view.getContext(), ShowRedActivity.class);
//            intent.putExtra("red_id", redId);
//            intent.putExtra("packet_type", redMessage.getPacketType());
//            intent.putExtra("sender_id", uiMessage.getSenderUserId());
//            intent.putExtra("bless_words", redMessage.getDescribe());
//            intent.putExtra("chat_type", uiMessage.getConversationType().getName());
//            view.getContext().startActivity(intent);
//        }
    }

    @Override
    public void onItemLongClick(View view, int i, RedMessage redMessage, UIMessage uiMessage) {

    }

}
