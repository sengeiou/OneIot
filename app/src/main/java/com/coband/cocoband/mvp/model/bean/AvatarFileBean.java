package com.coband.cocoband.mvp.model.bean;

/**
 * Created by tgc on 17-10-13.
 */

public class AvatarFileBean {
    private AvatarFile avatarFile;

    public AvatarFile getAvatarFile() {
        return avatarFile;
    }

    public void setAvatarFile(AvatarFile avatarFile) {
        this.avatarFile = avatarFile;
    }

    public static class AvatarFile {
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
