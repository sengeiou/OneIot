package com.coband.dfu.network.bean;

/**
 * Created by mai on 17-7-10.
 */

public class FwResultBean {
    public int code;
    public PayloadBean payload;
    public String errorStr;

    public static class PayloadBean {
        public String resourceUrl;
        public String fwType;
        public String md5sum;
        public String sha1sum;
        public String sha256sum;
        public String description;
        public String version;
        public ExtraBean extra;
    }

    public static class ExtraBean {
        public String fileName;
        public String fileSize;
    }
}
