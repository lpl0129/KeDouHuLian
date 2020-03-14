package com.qttx.kedouhulian.receiver;

import android.content.Context;
import android.content.Intent;

import com.qttx.kedouhulian.ui.chat.ChatApplyActivity;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.push.PushType;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;


public class RongImNotificationReceiver extends PushMessageReceiver {

    @Override
    public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {

        String id = pushNotificationMessage
                .getConversationType().getName();
        if ("SYSTEM".equalsIgnoreCase(id)) {
            RongIM.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.SYSTEM, pushNotificationMessage.getTargetId(), null);

            Intent intent = new Intent(context, ChatApplyActivity.class);
            context.startActivity(intent);
            return true;
        }

        return false;
    }
}
