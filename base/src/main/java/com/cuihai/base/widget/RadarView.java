package com.cuihai.base.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;


import com.cuihai.base.util.SizeUtils;

import java.util.ArrayList;

/**
 * 雷达图
 */
public class RadarView extends View {

    private int mValueCount = 5;
    double mMaxValue = 100;

    private Paint mPaintPolygon;
    private Paint mPaintLine;
    private Paint mPaintRegion;
    private Paint mPaintTitle;
    private Paint mPaintImage;
    private Path mPathPolygon;
    private Path mPathLine;
    private Path mPathRegion;

    private float mEachAngle = (float) (2 * Math.PI / mValueCount);
    private float mStartAngle = (float) (-Math.PI / 2);
    private float mRadius;
    private int mPolygonNumber = 1;

    private int mCenterX;
    private int mCenterY;

    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Double> mValueList = new ArrayList<>();
    private ArrayList<Integer> mResIdList = new ArrayList<>();

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 设置标题的文字
     */
    public void setTitle(ArrayList<String> titleList) {
        mTitleList = titleList;
    }

    /**
     * 设置标题的图片
     */
    public void setImageRes(ArrayList<Integer> resIdList) {
        mResIdList = resIdList;
    }

    /**
     * 设置标题文字大小，单位 dp
     */
    public void setTitleSize(float dp) {
        mPaintTitle.setTextSize(SizeUtils.dp2px(dp));
    }

    /**
     * 设置数据
     */
    public void setValue(ArrayList<Double> valueList) {
        mValueList = valueList;
        mValueCount = valueList.size();
        mEachAngle = (float) (2 * Math.PI / mValueCount);
    }

    /**
     * 设置最大数值
     */
    public void setMaxValue(double maxValue) {
        mMaxValue = maxValue;
    }

    /**
     * 设置蜘蛛网颜色
     */
    public void setMainPaintColor(int color) {
        mPaintPolygon.setColor(color);
    }

    /**
     * 设置标题颜色
     */
    public void setTextPaintColor(int color) {
        mPaintTitle.setColor(color);
    }

    /**
     * 设置覆盖局域颜色
     */
    public void setValuePaintColor(int color) {
        mPaintRegion.setColor(color);
    }

    /**
     * 设置分割线颜色
     */
    public void setLinePaintColor(int color) {
        mPaintLine.setColor(color);
    }

    /**
     * 设置内圈数量
     */
    public void setInnerNumber(int number) {
        mPolygonNumber = number + 1;
    }

    private void init() {
        mPaintPolygon = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPolygon.setStyle(Paint.Style.STROKE);
        mPaintPolygon.setColor(Color.BLACK);
        mPaintPolygon.setStrokeWidth(1f);

        mPaintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setColor(Color.GRAY);
        mPaintLine.setStrokeWidth(1f);

        mPaintRegion = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintRegion.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintRegion.setColor(Color.WHITE);
        mPaintRegion.setStrokeWidth(1f);

        mPaintTitle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTitle.setStyle(Paint.Style.FILL);
        mPaintTitle.setColor(Color.BLACK);

        mPaintImage = new Paint();
        mPathPolygon = new Path();
        mPathLine = new Path();
        mPathRegion = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenterX = w / 2;
        mCenterY = h / 2;

        mRadius = Math.min(w, h) / 2 * 0.5f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPolygon(canvas);
        drawLine(canvas);
        drawRegion(canvas);
        drawTitle(canvas);
    }

    /**
     * 绘制分值区
     */
    private void drawRegion(Canvas canvas) {
        for (int i = 0; i < mValueCount; i++) {
            float x = (float) (Math.cos(mStartAngle + mEachAngle * i) * mRadius * mValueList.get(i) / mMaxValue);
            float y = (float) (Math.sin(mStartAngle + mEachAngle * i) * mRadius * mValueList.get(i) / mMaxValue);

            if (i == 0) {
                mPathRegion.moveTo(mCenterX + x, mCenterY + y);
            } else {
                mPathRegion.lineTo(mCenterX + x, mCenterY + y);
            }
        }
        mPathRegion.close();

        canvas.drawPath(mPathRegion, mPaintRegion);
    }

    /**
     * 绘制直线
     */
    private void drawLine(Canvas canvas) {
        for (int i = 0; i < mValueCount; i++) {
            float x = (float) (mCenterX + Math.cos(mStartAngle + mEachAngle * i) * mRadius);
            float y = (float) (mCenterY + Math.sin(mStartAngle + mEachAngle * i) * mRadius);

            mPathLine.moveTo(mCenterX, mCenterY);
            mPathLine.lineTo(x, y);

            canvas.drawPath(mPathLine, mPaintLine);
        }
    }

    /**
     * 绘制标题的文字和图片
     */
    private void drawTitle(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mPaintTitle.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        for (int i = 0; i < mValueCount; i++) {
            String text = "";
            Bitmap bitmap = null;

            // 标题文字和图片可能为空
            if (mTitleList.size() > i) {
                text = mTitleList.get(i);
            }
            if (mResIdList.size() > i) {
                bitmap = BitmapFactory.decodeResource(getResources(), mResIdList.get(i));
            }


            // 计算二者所占矩形的长/宽/外接圆的半径
            float rectWidth;
            float rectHeight;
            float textWidth = mPaintTitle.measureText(text);
            if (bitmap == null) {
                rectWidth = textWidth;
                rectHeight = fontHeight;
            } else if (TextUtils.isEmpty(text)) {
                rectWidth = bitmap.getWidth();
                rectHeight = bitmap.getHeight();
            } else {
                rectWidth = Math.max(textWidth, bitmap.getWidth());
                rectHeight = fontHeight + bitmap.getHeight();
            }
            double rectRadius = Math.sqrt(rectWidth * rectWidth + rectHeight * rectHeight);

            // 计算基点为矩形中心
            float x = (float) (mCenterX + (mRadius + rectRadius / 2) * Math.cos(mStartAngle + mEachAngle * i));
            float y = (float) (mCenterY + (mRadius + rectRadius / 2) * Math.sin(mStartAngle + mEachAngle * i));

            // 绘制文字和图片
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, x - bitmap.getWidth() / 2, y - rectHeight / 2, mPaintImage);
            }
            if (!TextUtils.isEmpty(text)) {
                canvas.drawText(text, x - textWidth / 2, y + rectHeight / 2, mPaintTitle);
            }
        }
    }

    /**
     * 绘制多边形
     */
    private void drawPolygon(Canvas canvas) {
        for (int count = 1; count <= mPolygonNumber; count++) {
            float newRadius = mRadius / mPolygonNumber * count;

            mPathPolygon.moveTo((float) (mCenterX + newRadius * Math.cos(mStartAngle)), (float) (mCenterY + newRadius * Math.sin(mStartAngle)));

            for (int i = 1; i < mValueCount; i++) {
                float x = (float) (mCenterX + Math.cos(mStartAngle + mEachAngle * i) * newRadius);
                float y = (float) (mCenterY + Math.sin(mStartAngle + mEachAngle * i) * newRadius);

                mPathPolygon.lineTo(x, y);
            }

            mPathPolygon.close();

            canvas.drawPath(mPathPolygon, mPaintPolygon);

            mPathPolygon.reset();
        }
    }

}