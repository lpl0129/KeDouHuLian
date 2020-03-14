package com.qttx.kedouhulian.ui.chat.message;

import android.os.Parcel;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.DestructionTag;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

//RED
@MessageTag(value = "KD:TransferMsg", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
@DestructionTag
public class RedMessage extends MessageContent {

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    private String money;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.money);
    }

    public RedMessage() {
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.putOpt("money", this.getMoney());

        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected RedMessage(Parcel in) {
        this.money = in.readString();
    }

    public static final Creator<RedMessage> CREATOR = new Creator<RedMessage>() {
        @Override
        public RedMessage createFromParcel(Parcel source) {
            return new RedMessage(source);
        }

        @Override
        public RedMessage[] newArray(int size) {
            return new RedMessage[size];
        }
    };

    public RedMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("money"))
                money = jsonObj.optString("money");

        } catch (JSONException e) {
        }

    }
}
