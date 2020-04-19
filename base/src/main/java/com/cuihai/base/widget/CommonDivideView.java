package com.cuihai.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.cuihai.base.R;
import com.cuihai.base.util.SizeUtils;


/**
 * @author: cuihai
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2019/4/11 19:29
 * @company:
 * @email: nicech6@163.com
 */
public class CommonDivideView extends View {
    private int mLineColor;

    public CommonDivideView(Context context) {
        this(context, null);
    }

    public CommonDivideView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonDivideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CommonDivideView);
        mLineColor = a.getColor(R.styleable.CommonDivideView_line_bg_color, context.getResources().getColor(R.color.config_color_line_d8));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        int lineWidth = getWidth();
        int lineHeight = getHeight();
        paint.setStrokeWidth(SizeUtils.dp2px(lineHeight));
        paint.setColor(mLineColor);
        canvas.drawLine(0, lineHeight / 2, lineWidth, lineHeight / 2, paint);
    }
}