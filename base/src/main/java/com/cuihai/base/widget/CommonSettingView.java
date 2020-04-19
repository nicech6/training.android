package com.cuihai.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cuihai.base.R;
import com.cuihai.base.util.SizeUtils;


/**
 * @author: cuihai
 * @description: 我的信息页面封装(左边图标 + 文字 中间文字或图标 右边文字 + 图标)
 * @version: V_1.0.0
 * @date: 2019/4/9 19:03
 * @company:
 * @email: nicech6@163.com
 */
public class CommonSettingView extends RelativeLayout {
    private Context mContext;
    private TextView mLeftTextView;
    private TextView mCenterTextView;
    private TextView mRightTextView;
    private String mLeftText;
    private String mCenterText;
    private String mCenterTextHint;
    private String mRightText;
    private int mDrawableRight;
    private int mDrawableLeft;
    private int mDrawableCenter;
    private int mTvColorLeft;
    private int mTvColorCenter;
    private int mLeftTextSize;
    private int mCenterTextSize;
    private int mRightTextSize;
    private int mTvColorRight;
    private int mCenterTextViewMarginLeft;
    private boolean mGoneRightImageView;

    public CommonSettingView(Context context) {
        this(context, null);
    }

    public CommonSettingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonSettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonSettingView);
        mLeftText = a.getString(R.styleable.CommonSettingView_tv_left);
        mCenterText = a.getString(R.styleable.CommonSettingView_tv_center);
        mRightText = a.getString(R.styleable.CommonSettingView_tv_right);
        mCenterTextHint = a.getString(R.styleable.CommonSettingView_tv_center_hint);
        mDrawableRight = a.getResourceId(R.styleable.CommonSettingView_iv_right, 0);
        mDrawableLeft = a.getResourceId(R.styleable.CommonSettingView_iv_left, 0);
        mGoneRightImageView = a.getBoolean(R.styleable.CommonSettingView_iv_right_gone, false);
        mTvColorLeft = a.getColor(R.styleable.CommonSettingView_tv_color_left, getResources().getColor(R.color.config_color_text_33));
        mTvColorCenter = a.getColor(R.styleable.CommonSettingView_tv_color_center, getResources().getColor(R.color.config_color_text_33));
        mTvColorRight = a.getColor(R.styleable.CommonSettingView_tv_right_color, getResources().getColor(R.color.config_color_text_99));
        mLeftTextSize = (int) a.getDimension(R.styleable.CommonSettingView_tv_size_left, 14);
        mCenterTextSize = (int) a.getDimension(R.styleable.CommonSettingView_tv_size_center, 14);
        mRightTextSize = (int) a.getDimension(R.styleable.CommonSettingView_tv_right_size, 14);
        mCenterTextViewMarginLeft = (int) a.getDimension(R.styleable.CommonSettingView_tv_center_margin, 0);
        mDrawableCenter = a.getResourceId(R.styleable.CommonSettingView_iv_center, 0);
        mContext = context;
        a.recycle();
        initView(context);
    }

    private void initView(Context context) {
        RelativeLayout rl = new RelativeLayout(context);
        rl.setBackgroundColor(context.getResources().getColor(R.color.config_color_white));
        RelativeLayout.LayoutParams relLayoutParams = (LayoutParams) rl.getLayoutParams();
        if (relLayoutParams == null) {
            relLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        rl.setPadding(SizeUtils.dp2px(16), 0, SizeUtils.dp2px(16), 0);
        rl.setLayoutParams(relLayoutParams);

        rl.addView(createLeftTv(context));
        rl.addView(createCenterTv(context));
        rl.addView(createRightTv(context));
        createCenterTv(context);
        createLeftTv(context);
        createRightTv(context);
        setLeftTextStyle();
        setCenterTextStyle();
        setRightTextStyle();
        setLeftImg(mDrawableLeft);
        setRightImg(mDrawableRight);
        setCenterImg(mDrawableCenter);
        setTvLeft(mLeftText);
        setTvCenter(mCenterText);
        setTvRight(mRightText);
        setRightGone(mGoneRightImageView);

        addView(rl);
    }

    private TextView createLeftTv(Context context) {
        if (mLeftTextView == null) {
            mLeftTextView = new TextView(context);
            RelativeLayout.LayoutParams layoutParams = (LayoutParams) mLeftTextView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            }
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            mLeftTextView.setLayoutParams(layoutParams);
        }
        return mLeftTextView;
    }

    private void setLeftTextStyle() {
        TextView leftTv = createLeftTv(mContext);
        leftTv.setTextSize(mLeftTextSize);
        leftTv.setTextColor(mTvColorLeft);
    }

    private void setCenterTextStyle() {
        TextView centerTv = createCenterTv(mContext);
        centerTv.setTextSize(mCenterTextSize);
        centerTv.setTextColor(mTvColorCenter);
        centerTv.setHint(mCenterTextHint);
    }

    private void setRightTextStyle() {
        TextView rightTv = createRightTv(mContext);
        rightTv.setTextSize(mRightTextSize);
        rightTv.setTextColor(mTvColorRight);
    }

    private TextView createCenterTv(Context context) {
        if (mCenterTextView == null) {
            mCenterTextView = new TextView(context);
            RelativeLayout.LayoutParams layoutParams = (LayoutParams) mCenterTextView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            }
            layoutParams.leftMargin = (mCenterTextViewMarginLeft);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            mCenterTextView.setLayoutParams(layoutParams);
        }
        return mCenterTextView;
    }

    private TextView createRightTv(Context context) {
        if (mRightTextView == null) {
            mRightTextView = new TextView(context);
            RelativeLayout.LayoutParams layoutParams = (LayoutParams) mRightTextView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            }
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            mRightTextView.setLayoutParams(layoutParams);
        }
        return mRightTextView;
    }


    public void setTvCenter(String text) {
        if (!TextUtils.isEmpty(text)) {
            TextView centerTv = createCenterTv(mContext);
            centerTv.setCompoundDrawables(null, null, null, null);
            centerTv.setText(text);
        }
    }

    public void setTvCenterMargin(int margin) {
        TextView centerTv = createCenterTv(mContext);
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) centerTv.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        layoutParams.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,margin,mContext.getResources().getDisplayMetrics());
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        centerTv.setLayoutParams(layoutParams);
    }

    public void setCenterImg(int resourceId) {
        if (resourceId != 0) {
            TextView centerTv = createCenterTv(mContext);
            centerTv.setText("");
            Drawable drawable = getResources().getDrawable(resourceId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            centerTv.setCompoundDrawables(drawable, null, null, null);
        }
    }

    public void setTvLeft(String text) {
        if (!TextUtils.isEmpty(text)) {
            TextView leftTv = createLeftTv(mContext);
            leftTv.setText(text);
        }
    }

    public void setRightImg(int resourceId) {
        if (resourceId != 0) {
            TextView rightTv = createRightTv(mContext);
            Drawable drawable = getResources().getDrawable(resourceId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            rightTv.setCompoundDrawables(null, null, drawable, null);
            rightTv.setCompoundDrawablePadding(SizeUtils.dp2px(13));
        }
    }

    public void setTvRight(String text) {
        if (!TextUtils.isEmpty(text)) {
            TextView rightTv = createRightTv(mContext);
            rightTv.setText(text);
        }
    }

    public void setRightGone(boolean gone) {
        TextView rightTv = createRightTv(mContext);
        rightTv.setVisibility(gone ? View.GONE : View.VISIBLE);
    }

    public void setLeftImg(int resourceId) {
        TextView leftTv = createLeftTv(mContext);
        if (resourceId == 0) {
            leftTv.setCompoundDrawables(null, null, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(resourceId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            leftTv.setCompoundDrawables(drawable, null, null, null);
            leftTv.setCompoundDrawablePadding(SizeUtils.dp2px(13));
        }
    }
}
