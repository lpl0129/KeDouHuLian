package com.qttx.kedouhulian.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by huang on 2017/3/25.
 */

public class OssBean implements Parcelable {

    public enum TYPE {
        IMAGE, VIDEO, AUDIO, THUMB
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    private long length;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    private long size;

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public String getObjectkey() {
        return objectkey;
    }

    public void setObjectkey(String objectkey) {
        this.objectkey = objectkey;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    private String localUrl;
    private String objectkey;
    private TYPE type;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    private String serverUrl;

    public OssBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.length);
        dest.writeLong(this.size);
        dest.writeString(this.localUrl);
        dest.writeString(this.objectkey);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.serverUrl);
    }

    protected OssBean(Parcel in) {
        this.length = in.readLong();
        this.size = in.readLong();
        this.localUrl = in.readString();
        this.objectkey = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : TYPE.values()[tmpType];
        this.serverUrl = in.readString();
    }

    public static final Creator<OssBean> CREATOR = new Creator<OssBean>() {
        @Override
        public OssBean createFromParcel(Parcel source) {
            return new OssBean(source);
        }

        @Override
        public OssBean[] newArray(int size) {
            return new OssBean[size];
        }
    };
}
