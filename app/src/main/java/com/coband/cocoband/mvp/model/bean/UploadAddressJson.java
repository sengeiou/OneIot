package com.coband.cocoband.mvp.model.bean;

/**
 * Created by imco on 17-3-27.
 */

public class UploadAddressJson {
    /**
     * resp_msg : {"ret_code":"0","error_info":"OK"}
     * qrticket : http://we.qq.com/d/AQAtIgS4Xx_NJGlTkw49grfaA1JOlawPpHSbA0e5
     */

    private RespMsgBean resp_msg;
    private String qrticket;

    public RespMsgBean getResp_msg() {
        return resp_msg;
    }

    public void setResp_msg(RespMsgBean resp_msg) {
        this.resp_msg = resp_msg;
    }

    public String getQrticket() {
        return qrticket;
    }

    public void setQrticket(String qrticket) {
        this.qrticket = qrticket;
    }

    public static class RespMsgBean {
        /**
         * ret_code : 0
         * error_info : OK
         */

        private String ret_code;
        private String error_info;

        public String getRet_code() {
            return ret_code;
        }

        public void setRet_code(String ret_code) {
            this.ret_code = ret_code;
        }

        public String getError_info() {
            return error_info;
        }

        public void setError_info(String error_info) {
            this.error_info = error_info;
        }
    }
}
