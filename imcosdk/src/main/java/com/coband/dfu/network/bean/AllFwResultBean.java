package com.coband.dfu.network.bean;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mai on 17-7-10.
 */

public class AllFwResultBean {
    public int code;
    public ArrayList<PayloadBean> payload;
    public String errorStr;

    public static class PayloadBean{
        public String resourceUrl;
        public String vendor;
        public String fwType;
        public String model;
        public long filesize;
        public String md5sum;
        public String sha1sum;
        public String sha256sum;
        public Date addedDate;
        public String description;
        public String version;
        public ExtraBean extra;
    }

    public static class ExtraBean{
        public String firmwareVersion;
        public String platform;
    }
}
