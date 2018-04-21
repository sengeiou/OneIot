package cn.leancloud.chatkit.event;

import android.widget.ImageView;

/**
 * Created by mai on 17-6-6.
 */

public class ImageViewClickEvent {
    public ImageViewClickEvent(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    public String url;

    public ImageView imageView;
}
