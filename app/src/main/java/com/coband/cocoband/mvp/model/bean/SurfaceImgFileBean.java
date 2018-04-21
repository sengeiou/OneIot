package com.coband.cocoband.mvp.model.bean;

/**
 * Created by tgc on 17-10-13.
 */

public class SurfaceImgFileBean {
    private SurfaceFile surfaceFile;

    public SurfaceFile getSurfaceFile() {
        return surfaceFile;
    }

    public void setSurfaceFile(SurfaceFile surfaceFile) {
        this.surfaceFile = surfaceFile;
    }

    public static class SurfaceFile {
        private String id;
        private String __type = "File";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
