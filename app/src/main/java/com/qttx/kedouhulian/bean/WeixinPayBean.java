package com.qttx.kedouhulian.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author huangyr
 * @date 2018/12/3
 */
public class WeixinPayBean {

    /**
     * prepayid : wx03110118242060429caa6f051442862308
     * appid : wx860b4cdbe018c025
     * partnerid : 1516544441
     * package : Sign=WXPay
     * noncestr : ef65d278792233f1c0dd13f55ac8b63c
     * timestamp : 1543806078
     * sign : F29AA8E5BA2E76EE355ACE04C1512EEA
     */

    private String prepayid;
    private String appid;
    private String partnerid;
    @SerializedName("package")
    private String packageX;
    private String noncestr;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String timestamp;
    private String sign;

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }


    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
