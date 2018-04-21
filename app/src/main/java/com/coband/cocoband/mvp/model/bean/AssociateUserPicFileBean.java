package com.coband.cocoband.mvp.model.bean;

/**
 * Created by tgc on 17-5-25.
 */

public class AssociateUserPicFileBean {
    private String name;
    private Picture picture;

    private static class Picture {
        public String __type = "File";
        public String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }
}
