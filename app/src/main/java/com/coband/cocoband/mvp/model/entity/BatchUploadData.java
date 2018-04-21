package com.coband.cocoband.mvp.model.entity;

import java.util.List;

/**
 * Created by ivan on 17-5-9.
 * see <link>https://leancloud.cn/docs/rest_api.html#批量操作</link>
 */

public class BatchUploadData {


    private List<RequestsBean> requests;

    public List<RequestsBean> getRequests() {
        return requests;
    }

    public void setRequests(List<RequestsBean> requests) {
        this.requests = requests;
    }

    public static class RequestsBean<T> {

        private String method;
        private String path;
        private T body;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public T getBody() {
            return body;
        }

        public void setBody(T body) {
            this.body = body;
        }
    }
}
