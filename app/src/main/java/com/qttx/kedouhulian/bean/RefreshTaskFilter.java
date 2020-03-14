package com.qttx.kedouhulian.bean;

/**
 * @author huangyr
 * @date 2019/6/11 0011
 */
public class RefreshTaskFilter {

    public RefreshTaskFilter(String key) {
        setKey(key);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;
}
