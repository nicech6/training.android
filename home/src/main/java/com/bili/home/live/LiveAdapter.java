package com.bili.home.live;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bili.base.entity.home.HomeLiveEntity;
import com.bili.base.entity.home.LiveMultiItemEntity;
import com.bili.base.widget.GlideUtil;
import com.bili.base.widget.RecycleGridDivider;
import com.bili.home.R;
import com.bili.home.databinding.ItemLiveBannerBinding;
import com.bili.home.databinding.ItemLiveListBinding;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
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
        addItemType(LiveMultiItemEntity.LIST, R.layout.item_live_list);
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @NotNull
    @Override
    protected BaseViewHolder createBaseViewHolder(@NotNull ViewGroup parent, int layoutResId) {
        return super.createBaseViewHolder(parent, layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, @Nullable LiveMultiItemEntity entity) {
        switch (entity.getItemType()) {
            case LiveMultiItemEntity.BANNER:
                ItemLiveBannerBinding bannerBinding = holder.getBinding();
                bannerBinding.banner.setImageLoader(new GlideImageLoader());
                bannerBinding.banner.setImages(entity.mBannerBeans);
                bannerBinding.banner.start();
                break;
            case LiveMultiItemEntity.LIST:
                ItemLiveListBinding liveListBinding = holder.getBinding();
                LiveChildAdapter childAdapter = new LiveChildAdapter(entity.mPartitionsBean.getLives());
                liveListBinding.rv.setAdapter(childAdapter);
                liveListBinding.rv.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
                if (liveListBinding.rv.getItemDecorationCount() <= 0) {
                    liveListBinding.rv.addItemDecoration(new RecycleGridDivider(10));
                }
                liveListBinding.tvType.setText(entity.mPartitionsBean.getPartition().getName());
                break;
        }
    }

    private class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            GlideUtil.displayRound(imageView, ((HomeLiveEntity.BannerBean) path).getImg(), 10);
        }
    }
}
