package com.coband.cocoband.mvp.model.entity;

/**
 * Created by ivan on 17-5-9.
 */

public class BatchUploadResult {
    /**
     * success : {"objectId":"59119a02ac502e450284635d","createdAt":"2017-05-09T10:29:22.688Z"}
     */

    private SuccessBean success;

    public SuccessBean getSuccess() {
        return success;
    }

    public void setSuccess(SuccessBean success) {
        this.success = success;
    }

    public static class SuccessBean {
        /**
         * objectId : 59119a02ac502e450284635d
         * createdAt : 2017-05-09T10:29:22.688Z
         */

        private String objectId;
        private String createdAt;

        public String getObjectId() {
            return objectId;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
}
