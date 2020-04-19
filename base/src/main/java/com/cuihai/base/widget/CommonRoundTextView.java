package com.cuihai.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.cuihai.base.R;

public class CommonRoundTextView extends AppCompatTextView {
    public CommonRoundTextView(Context context) {
        this(context, null);
    }

    public CommonRoundTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonRoundTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CommonRoundTextView, defStyleAttr, 0);
        if (attributes != null) {

            int rtvBorderWidth = attributes.getDimensionPixelSize(R.styleable.CommonRoundTextView_rtvBorderWidth, 0);
            int rtvBorderColor = attributes.getColor(R.styleable.CommonRoundTextView_rtvBorderColor, Color.BLACK);
            float rtvRadius = attributes.getDimension(R.styleable.CommonRoundTextView_rtvRadius, 0);
            int rtvBgColor = attributes.getColor(R.styleable.CommonRoundTextView_rtvBgColor, Color.WHITE);
            attributes.recycle();

            GradientDrawable gd = new GradientDrawable();//创建drawable
            gd.setColor(rtvBgColor);
            gd.setCornerRadius(rtvRadius);
            if (rtvBorderWidth > 0) {
                gd.setStroke(rtvBorderWidth, rtvBorderColor);
            }

            this.setBackground(gd);
        }
    }
}
