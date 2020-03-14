package com.stay.toolslibrary.library.update;

import java.io.Serializable;

/**
 * 软件更新实体类
 */

public class ApkUpdate implements Serializable {

    private String apk;
    private String versionNo;

    public boolean isForceUpdate() {
        return isForceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        isForceUpdate = forceUpdate;
    }

    private boolean isForceUpdate;
    private String remark;
    private int upgradeNum;

    public ApkUpdate() {
    }

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }


    public int getUpgradeNum() {
        return upgradeNum;
    }

    public void setUpgradeNum(int upgradeNum) {
        this.upgradeNum = upgradeNum;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
