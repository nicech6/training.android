package com.ch.base.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ch.base.R;

public class TitleView extends RelativeLayout {
    private Context context;
    private int mCenterTextSize;
    private int mLeftTextSize;
    private int mRightTextSize;
    private TextView mTvLeft;
    private TextView mTvCenter;
    private TextView mTvRight;
    private String mTextLeft;
    private String mTextCenter;
    private String mTextRight;

    private int mLeftBackDrawableRes;
    private int mCenterDrawableRes;
    private int mRightDrawableRes;

    private int mLeftTextViewColor;
    private int mCenterTextViewColor;
    private int mRightTextViewColor;


    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView, defStyleAttr, 0);
        mLeftTextSize = array.getDimensionPixelSize(R.styleable.TitleView_title_left_size, 14);
        mCenterTextSize = array.getDimensionPixelSize(R.styleable.TitleView_title_center_size, 16);
        mRightTextSize = array.getDimensionPixelSize(R.styleable.TitleView_title_right_size, 14);
        mTextLeft = array.getString(R.styleable.TitleView_title_left_str);
        mTextCenter = array.getString(R.styleable.TitleView_title_center_str);
        mTextRight = array.getString(R.styleable.TitleView_title_right_str);
        mLeftBackDrawableRes = array.getResourceId(R.styleable.TitleView_title_left_drawable, R.mipmap.icon_back);
        mCenterDrawableRes = array.getResourceId(R.styleable.TitleView_title_center_drawable, 0);
        mRightDrawableRes = array.getResourceId(R.styleable.TitleView_title_right_drawable, 0);
        mLeftTextViewColor = array.getColor(R.styleable.TitleView_title_left_color, context.getResources().getColor(R.color.config_color_gray_3));
        mCenterTextViewColor = array.getColor(R.styleable.TitleView_title_center_color, context.getResources().getColor(R.color.config_color_gray_3));
        mRightTextViewColor = array.getColor(R.styleable.TitleView_title_right_color, context.getResources().getColor(R.color.config_color_gray_3));

        array.recycle();
        this.context = context;
        initView(context);
    }

    private void initView(Context context) {
        RelativeLayout rl = new RelativeLayout(context);
        rl.setBackgroundColor(context.getResources().getColor(R.color.config_color_white));
        RelativeLayout.LayoutParams relLayoutParams = (LayoutParams) rl.getLayoutParams();
        if (relLayoutParams == null) {
            relLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        rl.setPadding(16, 0, 16, 0);
        rl.setLayoutParams(relLayoutParams);

        rl.addView(createLeftTv(context));
        rl.addView(createCenterTv(context));
        rl.addView(createRightTv(context));
        setLeftImageView(mLeftBackDrawableRes);
        setLeftText(mTextLeft);
        setCenterImageView(mCenterDrawableRes);
        setCenterText(mTextCenter);
        setRightImageView(mRightDrawableRes);
        setRightText(mTextRight);
        setLeftStyle();
        setCenterStyle();
        setRightTextViewStyle();

        addView(rl);
    }

    private TextView createLeftTv(final Context context) {
        if (mTvLeft == null) {
            mTvLeft = new TextView(context);
            RelativeLayout.LayoutParams layoutParams = (LayoutParams) mTvLeft.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            }
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            mTvLeft.setLayoutParams(layoutParams);
            mTvLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnTitleLeftClick != null) {
                        mOnTitleLeftClick.leftTvClick();
                    } else {
                        if (context instanceof Activity) {
                            if (!((Activity) context).isFinishing()) {
                                ((Activity) context).finish();
                            }
                        }
                    }
                }
            });
        }
        return mTvLeft;
    }

    public void setLeftText(String text) {
        if (!TextUtils.isEmpty(text)) {
            TextView leftTv = createLeftTv(context);
            leftTv.setCompoundDrawables(null, null, null, null);
            leftTv.setText(text);
        }
    }

    private void setLeftStyle() {
        TextView leftTv = createLeftTv(context);
        leftTv.setTextSize(mLeftTextSize);
        leftTv.setTextColor(mLeftTextViewColor);
    }


    public void setLeftImageView(int resourceId) {
        if (resourceId != 0) {
            TextView leftTv = createLeftTv(context);
            leftTv.setText("");
            Drawable drawable = getResources().getDrawable(resourceId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            leftTv.setCompoundDrawables(drawable, null, null, null);
        }
    }

    private TextView createCenterTv(Context context) {
        if (mTvCenter == null) {
            mTvCenter = new TextView(context);
            RelativeLayout.LayoutParams layoutParams = (LayoutParams) mTvCenter.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            }
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            mTvCenter.setLayoutParams(layoutParams);
        }
        return mTvCenter;
    }

    public void setCenterText(String text) {
        if (!TextUtils.isEmpty(text)) {
            TextView centerTv = createCenterTv(context);
            centerTv.setCompoundDrawables(null, null, null, null);
            centerTv.setText(text);
        }
    }

    private void setCenterStyle() {
        TextView centerTv = createCenterTv(context);
        centerTv.setTextSize(mCenterTextSize);
        centerTv.setTextColor(mCenterTextViewColor);
    }

    public void setCenterImageView(int resourceId) {
        if (resourceId != 0) {
            TextView centerTv = createCenterTv(context);
            centerTv.setText("");
            Drawable drawable = getResources().getDrawable(resourceId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            centerTv.setCompoundDrawables(drawable, null, null, null);
        }
    }

    private TextView createRightTv(Context context) {
        if (mTvRight == null) {
            mTvRight = new TextView(context);
            RelativeLayout.LayoutParams layoutParams = (LayoutParams) mTvRight.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            }
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            mTvRight.setLayoutParams(layoutParams);
            if (mOnTitleRightClick != null) {
                mOnTitleRightClick.rightTvClick();
            }
        }
        return mTvRight;
    }

    public void setRightText(String text) {
        if (!TextUtils.isEmpty(text)) {
            TextView rightTv = createRightTv(context);
            rightTv.setCompoundDrawables(null, null, null, null);
            rightTv.setText(text);
        }
    }

    private void setRightTextViewStyle() {
        TextView rightTv = createRightTv(context);
        rightTv.setTextSize(mRightTextSize);
        rightTv.setTextColor(mRightTextViewColor);
    }

    public void setRightImageView(int resourceId) {
        if (resourceId != 0) {
            TextView rightTv = createRightTv(context);
            rightTv.setText("");
            Drawable drawable = getResources().getDrawable(resourceId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            rightTv.setCompoundDrawables(drawable, null, null, null);
        }
    }

    public OnTitleLeftClick mOnTitleLeftClick;

    public OnTitleRightClick mOnTitleRightClick;

    public void setOnTitleLeftClick(OnTitleLeftClick titleLeftClick) {
        this.mOnTitleLeftClick = titleLeftClick;
    }

    public void setOnTitleRightClick(OnTitleRightClick titleRightClick) {
        this.mOnTitleRightClick = titleRightClick;
    }

    public interface OnTitleLeftClick {
        void leftTvClick();
    }

    public interface OnTitleRightClick {
        void rightTvClick();
    }
}
