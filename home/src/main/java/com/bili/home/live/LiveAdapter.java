package com.bili.home.live;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.bili.base.entity.home.HomeLiveEntity;
import com.bili.base.entity.home.LiveMultiItemEntity;
import com.bili.home.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author: cuihai
 * @description: 类描述
 * @date: 2019/12/20
 * @email: nicech6@163.com
 */
public class LiveAdapter extends BaseMultiItemQuickAdapter<LiveMultiItemEntity, BaseViewHolder> {
    public LiveAdapter(@Nullable List<LiveMultiItemEntity> data) {
        super(data);
        addItemType(LiveMultiItemEntity.BANNER, R.layout.item_live_banner);
        addItemType(LiveMultiItemEntity.LIST, R.layout.item_live);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable LiveMultiItemEntity liveMultiItemEntity) {
        switch (liveMultiItemEntity.getItemType()) {
            case LiveMultiItemEntity.BANNER:
                Banner banner = holder.getView(R.id.banner);
                banner.setImageLoader(new GlideImageLoader());
                banner.setImages(liveMultiItemEntity.mBannerBeans);
                banner.start();
                break;
            case LiveMultiItemEntity.LIST:

                break;
        }
    }

    private class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(((HomeLiveEntity.BannerBean) path).getImg()).into(imageView);
        }
    }
}
