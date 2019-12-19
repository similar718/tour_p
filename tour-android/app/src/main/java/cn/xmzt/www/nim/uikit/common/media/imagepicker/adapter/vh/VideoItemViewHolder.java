package cn.xmzt.www.nim.uikit.common.media.imagepicker.adapter.vh;

import android.view.View;
import android.view.ViewGroup;

import cn.xmzt.www.R;
import cn.xmzt.www.nim.uikit.common.adapter.AdvancedAdapter;
import cn.xmzt.www.nim.uikit.common.media.imagepicker.ImagePicker;
import cn.xmzt.www.nim.uikit.common.media.imagepicker.loader.GlideImageLoader;
import cn.xmzt.www.nim.uikit.common.media.model.GLImage;
import cn.xmzt.www.intercom.common.util.sys.TimeUtil;


/**
 */

public class VideoItemViewHolder extends ItemViewHolder {

    public VideoItemViewHolder(ViewGroup vp, ImagePicker imagePicker, AdvancedAdapter adapter) {
        super(vp, imagePicker, adapter);
    }

    public void clearRequest() {
        ImagePicker.getInstance().getImageLoader().clearRequest(ivThumb);
    }

    @Override
    protected void onBindViewHolder(final SectionModel model) {
        super.onBindViewHolder(model);
        mask.setVisibility(View.VISIBLE);
        videoIcon.setVisibility(View.VISIBLE);
        // 本地视频的缩略图通过MediaStore以bitmap的形式拿到
        // Glide不能加载bitmap，需要自己处理
        // 不要把ivThumb的tag改掉，glide会使用这个字段
        final GLImage GLImage = model.getImage();
        timeMask.setVisibility(View.VISIBLE);
        timeMask.setText(TimeUtil.secToTime((int) (GLImage.getDuration() / 1000f)));
        GlideImageLoader.displayAlbumThumb(ivThumb, GLImage.getPath(), R.drawable.nim_image_default);
    }
}
