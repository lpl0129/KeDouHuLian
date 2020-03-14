package com.qttx.kedouhulian.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.qttx.kedouhulian.bean.RefreshTaskFilter;
import com.qttx.kedouhulian.ui.chat.ChatApplyActivity;
import com.qttx.kedouhulian.ui.pond.MyPondActivity;
import com.qttx.kedouhulian.ui.reward.buunty.BountyDetailActivity;
import com.qttx.kedouhulian.ui.reward.buunty.MyBountyActivity;
import com.qttx.kedouhulian.ui.reward.redpacket.MyRedPacketActivity;
import com.qttx.kedouhulian.ui.reward.task.MyTaskActivity;
import com.qttx.kedouhulian.ui.reward.task.TaskHomeDetailActivity;
import com.qttx.kedouhulian.ui.trade.MyTradeActivity;
import com.qttx.kedouhulian.utils.Utils;
import com.qttx.kedouhulian.utils.UtilsKt;
import com.stay.toolslibrary.utils.LogUtils;
import com.stay.toolslibrary.utils.RxBus;

import cn.jpush.android.api.JPushInterface;
import io.rong.imageloader.utils.L;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
    private int notifyId = 0;
    private Context context;
    private NotificationManager manager;
    private IReceive iReceive;

    public void setiReceive(IReceive iReceive) {
        this.iReceive = iReceive;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (manager == null) {
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            if (iReceive != null) {
                iReceive.onReveice(regId);
            }
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {//推送下来的通知
            String msg = bundle.getString(JPushInterface.EXTRA_EXTRA);
            //收到推送
            try {
                JSONObject object = new JSONObject(msg);
                LogUtils.e("tag", object.toString());
                String type = object.optString("type");
                if (!TextUtils.isEmpty(type)) {
                    RxBus
                            .getDefault()
                            .post(new RefreshTaskFilter(type));
//                    EventBus.getDefault()
//                            .post(new RefreshTaskFilter(type));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {//打开了通知
            String msg = bundle.getString(JPushInterface.EXTRA_EXTRA);

            //收到推送
            try {
                JSONObject object = new JSONObject(msg);
                LogUtils.e("tag", object.toString());

                String type = object.optString("type");
                Intent intent1 = new Intent();
                Class<?> c = null;
                switch (type) {
                    case  "trade_update_money":
                        c = MyTradeActivity.class;
                        intent1.putExtra("index", 1);
                        break;
                    case "bounty_fired":
                        //悬赏
                        c = MyBountyActivity.class;
                        intent1.putExtra("index", 1);
                        break;
                    case "bounty_score":
                        c = MyBountyActivity.class;
                        intent1.putExtra("index", 1);
                        break;
                    case "bounty_commission":
                        c = MyBountyActivity.class;
                        intent1.putExtra("index", 1);
                        break;
                    case "bounty_hire":
                        c = MyBountyActivity.class;
                        intent1.putExtra("index", 1);
                        break;

                    case "red_commission":
                        c = MyRedPacketActivity.class;
                        break;
                    case "red_lower_level":
                        c = MyRedPacketActivity.class;
                        break;
                    case "red_score":
                        c = MyRedPacketActivity.class;
                        break;
                    case "trade_buy":
                        c = MyTradeActivity.class;
                        break;
                    case "trade_cancel":
                        c = MyTradeActivity.class;
                        break;
                    case "pool_income":
                        c = MyPondActivity.class;
                        break;
                    case "pool_score":
                        c = MyPondActivity.class;
                        break;
                    case "pool_bebuy":
                        c = MyPondActivity.class;
                        break;
                    case "task_false":
                        c = MyTaskActivity.class;
                        intent1.putExtra("index", 1);
                        break;
                    case "task_lower_level":
                        c = MyTaskActivity.class;
                        intent1.putExtra("index", 1);
                        break;
                    case "task_score":
                        c = MyTaskActivity.class;
                        intent1.putExtra("index", 1);
                        break;
                    case "task_commission":
                        c = MyTaskActivity.class;
                        intent1.putExtra("index", 1);
                        break;
                    case "task_submit":
                        c = MyTaskActivity.class;
                        break;
                    case "task_xiugai":
                        c = MyTaskActivity.class;
                        intent1.putExtra("index", 1);
                        break;
                    case "task_message":
                        c = TaskHomeDetailActivity.class;
                        String taskid=object.optString("task_id");
                        intent1.putExtra("id", taskid);
                        break;
                    case "bounty_message":
                        c = BountyDetailActivity.class;
                        String bounty=object.optString("bounty_id");
                        intent1.putExtra("id", bounty);
                        break;
                    case "group_apply":
                        c = ChatApplyActivity.class;
                        String group_id=object.optString("group_id");
                        intent1.putExtra("id", group_id);
                        break;
                    case "bounty_apply_commission":
                        c = ChatApplyActivity.class;
                        String bounty_id=object.optString("bounty_id");
                        String bounty_name=object.optString("bounty_name");
                        RongIM.getInstance().startConversation(context, Conversation.ConversationType.PRIVATE, bounty_id, bounty_name);
                        break;
                }
                if (c != null && UtilsKt.checkLogin(context)) {
                    intent1.setClass(context, c);
                    context.startActivity(intent1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//
        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + ": " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
