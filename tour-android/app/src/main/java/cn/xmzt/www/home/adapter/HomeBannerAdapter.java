package cn.xmzt.www.home.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.xmzt.www.R;
import cn.xmzt.www.glide.GlideUtil;
import cn.xmzt.www.home.bean.AdvertiseBean;
import cn.xmzt.www.view.banner.BannerLayout;

import java.util.List;

/**
 * homebanner适配器
 */
public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.MzViewHolder> {

    private Context context;
    private List<AdvertiseBean> datas;
    private BannerLayout.OnBannerItemClickListener onBannerItemClickListener;

    public HomeBannerAdapter(Context context,List<AdvertiseBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    public void setDatas(List<AdvertiseBean> datas){
        this.datas = datas;
        notifyDataSetChanged();
    }
    public AdvertiseBean getItem(int position){
        if(position<datas.size()){
            return datas.get(position);
        }
        return null;
    }
    public void setOnBannerItemClickListener(BannerLayout.OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override
    public HomeBannerAdapter.MzViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MzViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_banner_image, parent, false));
    }

    @Override
    public void onBindViewHolder(HomeBannerAdapter.MzViewHolder holder, int position) {
        holder.setViewData(position);

    }

    @Override
    public int getItemCount() {
        if (datas != null) {
           return datas.size();
        }
       return 0;
    }


    class MzViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        MzViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }

        public void setViewData(int position) {
            this.imageView = imageView;
            if (datas == null || datas.isEmpty())
                return;
            final int P = position % datas.size();
            AdvertiseBean advertiseBean=datas.get(P);
            String url="";
            if(advertiseBean!=null){
                url=advertiseBean.getAdvPic();
            }
            GlideUtil.loadImgRadius(imageView,8,url);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBannerItemClickListener != null) {
                        onBannerItemClickListener.onItemClick(P);
                    }
                }
            });
        }
    }

}
