package cn.xmzt.www.nim.im.chatroom.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.xmzt.www.R;
import cn.xmzt.www.nim.im.chatroom.helper.ChatRoomHelper;
import cn.xmzt.www.nim.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import cn.xmzt.www.intercom.business.session.viewholder.BaseViewHolder;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;

/**
 * 聊天室列表数据适配器
 * Created by huangjun on 2016/12/9.
 */
public class ChatRoomListAdapter extends BaseQuickAdapter<ChatRoomInfo, BaseViewHolder> {
    private final static int COUNT_LIMIT = 10000;

    public ChatRoomListAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.chat_room_item, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, ChatRoomInfo room, int position, boolean isScrolling) {
        // bg
        holder.getConvertView().setBackgroundResource(R.drawable.nim_list_item_bg_selecter);
        // cover
        ImageView coverImage = holder.getView(R.id.cover_image);
        ChatRoomHelper.setCoverImage(room.getRoomId(), coverImage, false);
        // name
        holder.setText(R.id.tv_name, room.getName());
        // online count
        TextView onlineCountText = holder.getView(R.id.tv_online_count);
        setOnlineCount(onlineCountText, room);
    }

    private void setOnlineCount(TextView onlineCountText, ChatRoomInfo room) {
        if (room.getOnlineUserCount() < COUNT_LIMIT) {
            onlineCountText.setText(String.valueOf(room.getOnlineUserCount()));
        } else if (room.getOnlineUserCount() >= COUNT_LIMIT) {
            onlineCountText.setText(String.format("%.1f", room.getOnlineUserCount() / (float) COUNT_LIMIT) + "万");
        }
    }
}
