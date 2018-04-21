package com.coband.cocoband.mvp.model.entity.response;

/**
 * Created by ivan on 3/7/18.
 */

public class RegisterResult {
    /**
     * code : 0
     * message : ok
     * payload : {"uid":101010111,"expire":1516976964,"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTY5NDE2MjMsImlkIjoiYWRtaW4iLCJvcmlnX2lhdCI6MTUxNjkzODAyM30.oCb3AylMhK7CnyGk16eX0J0atH5jngrzdEuSL9VBbL4"}
     */

    private int code;
    private String message;
    private PayloadBean payload;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PayloadBean getPayload() {
        return payload;
    }

    public void setPayload(PayloadBean payload) {
        this.payload = payload;
    }

    public static class PayloadBean {
        /**
         * uid : 101010111
         * expire : 1516976964
         * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTY5NDE2MjMsImlkIjoiYWRtaW4iLCJvcmlnX2lhdCI6MTUxNjkzODAyM30.oCb3AylMhK7CnyGk16eX0J0atH5jngrzdEuSL9VBbL4
         */

        private String uid;
        private int expire;
        private String token;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getExpire() {
            return expire;
        }

        public void setExpire(int expire) {
            this.expire = expire;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
