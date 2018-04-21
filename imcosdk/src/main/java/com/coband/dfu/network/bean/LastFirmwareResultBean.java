package com.coband.dfu.network.bean;

import java.util.ArrayList;

/**
 * Created by mai on 17-10-11.
 */

public class LastFirmwareResultBean {
    public int code;
    public ArrayList<LastFirmwareResultBean.PayloadBean> payload;

    public static class PayloadBean{
        public String resourceUrl;
        public String fwType;
        public String version;
        public String description;
        public String md5sum;
        public String sha1sum;
        public String sha256sum;
        public AllFwResultBean.ExtraBean extra;
    }

    public static class ExtraBean{
        public String fileName;
        public String fileSize;
    }
}
