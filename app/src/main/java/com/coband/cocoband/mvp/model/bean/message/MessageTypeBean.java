package com.coband.cocoband.mvp.model.bean.message;

/**
 * Created by mai on 17-5-17.
 */

public class MessageTypeBean {
    public final static int TYPE_TEXT_MESSAGE = -1;
    public final static int TYPE_IMAGE_MESSAGE = -2;
    public final static int TYPE_AUDIO_MESSAGE = -3;
    public final static int TYPE_VIDEO_MESSAGE = -4;
    public final static int TYPE_LOCATION_MESSAGE = -5;
    public final static int TYPE_GENERAL_FILE_MESSAGE = -6;

    public int _lctype;
    public LcAttrs _lcattrs;
    public String _lctext;
    public LcFile _lcfile;

    public static class LcAttrs {
        public String userAvatarUrl;
        public String userName;
    }

    public static class LcFile {
        public String url;
        public String objId;
        public MetaData metaData;
    }

    public static class MetaData {
        public String name;
        public String format;
        public float duration;
        public int size;
        public float height;
        public float width;
    }

    @Override
    public String toString() {
        return "{\"_lctype\":"+_lctype +","+
                "\"_lctext\":\""+_lctext +"\","+
                "\"_lcattrs\":{"+"\"userAvatarUrl\":\""+_lcattrs.userAvatarUrl+"\",\"userName\":\""+_lcattrs.userName+"\"}"+
                (_lcfile==null?"}":",\"_lcfile\":{\"url\":\""+_lcfile.url+"\",\"objId\":\""+_lcfile.objId+"\""+
                (_lcfile.metaData==null?"}}":",\"metaData\":{\"height\":"+_lcfile.metaData.height+",\"width\":" + _lcfile.metaData.width + "}}}"));// TODO: 17-5-19 metaData
    }
}
