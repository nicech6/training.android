package com.bili.home.live;

import androidx.databinding.DataBindingUtil;

import com.bili.base.entity.home.HomeLiveEntity;
import com.bili.base.widget.GlideRoundTransform;
import com.bili.home.R;
import com.bili.home.databinding.ItemLiveBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LiveChildAdapter extends BaseQuickAdapter<HomeLiveEntity.PartitionsBean.LivesBeanXX, BaseViewHolder> {
    public LiveChildAdapter(@Nullable List<HomeLiveEntity.PartitionsBean.LivesBeanXX> data) {
        super(R.layout.item_live, data);
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, @Nullable HomeLiveEntity.PartitionsBean.LivesBeanXX beanXX) {
        ItemLiveBinding binding = baseViewHolder.getBinding();
        binding.tvOnline.setText(beanXX.getOnline() + "");
        binding.tvName.setText(beanXX.getArea_v2_name());
        binding.tv2.setText(beanXX.getTitle());
        Glide.with(getContext())
                .load(beanXX.getCover())
                .transform(new CenterCrop(), new GlideRoundTransform(12))
                .into(binding.ivCover);
    }
}
