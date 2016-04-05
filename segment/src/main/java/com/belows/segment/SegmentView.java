package com.belows.segment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author belows
 *         Created by belows on 16/3/25.
 */
public class SegmentView extends LinearLayout {

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    /**
     * the color of rect line and the separate line
     */
    private int mLineColor;

    /**
     * the width of line, default is 2px
     */
    private int mLineWidth;

    /**
     * the background color of the selected item
     */
    private int mSelectedColor;

    /**
     * the background color of the unSelected item
     */
    private int mColor;

    /**
     * the text size
     */
    private float mTextSize;

    /**
     * the normal text color
     */
    private int mTextColor;

    /**
     * the selected item's text color
     */
    private int mSelectedTextColor;

    /**
     * the rect radius
     */
    private float mRadius;

    /**
     *
     */
    private float mRadiusBuffer[];

    /**
     * the paint for rect line
     */
    private Paint mPaint;

    private OnItemClickListener mItemClickListener;

    private List<Item> mItemList = new ArrayList<>();


    public SegmentView(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs   orientation is horizontal
     *                init rect line color, default is Color.Blue
     *                init the selected item's background color, default is Color.Blue
     *                init text size, default is 14sp
     *                init normal text color, default is Color.White
     *                init selected text color, default is Color.Blue
     *                item's width is the average of the whole width
     */
    public SegmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SegmentView);
        mLineColor = typedArray.getColor(R.styleable.SegmentView_sv_line_color, Color.BLUE);
        mLineWidth = typedArray.getDimensionPixelSize(R.styleable.SegmentView_sv_line_width, 2);
        mSelectedColor = typedArray.getColor(R.styleable.SegmentView_sv_selected_bg_color, Color.BLUE);
        mTextSize = typedArray.getDimension(R.styleable.SegmentView_sv_text_size, 14);
        mTextColor = typedArray.getColor(R.styleable.SegmentView_sv_text_color, Color.BLUE);
        mSelectedTextColor = typedArray.getColor(R.styleable.SegmentView_sv_selected_text_color, Color.WHITE);
        mRadius = typedArray.getDimension(R.styleable.SegmentView_sv_bg_radius, 20);
        mColor = typedArray.getColor(R.styleable.SegmentView_sv_bg_color, Color.WHITE);
        typedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setColor(mLineColor);

        setWillNotDraw(false);

        initBackground();
    }

    private void initBackground() {
        mRadiusBuffer = new float[]{mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius};
        RectF rectF = new RectF(mLineWidth, mLineWidth, mLineWidth, mLineWidth);

        mRadius -= mLineWidth;
        float mBuffer[] = new float[]{mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius};
        RoundRectShape shape = new RoundRectShape(mRadiusBuffer, rectF, mBuffer);
        ShapeDrawable drawable = new ShapeDrawable(shape);
        drawable.getPaint().setColor(mLineColor);
        drawable.getPaint().setAntiAlias(true);
        drawable.getPaint().setStrokeWidth(mLineWidth);
        setBackgroundDrawable(drawable);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public Item newItem() {
        return new Item(getContext());
    }

    public void addItem(Item item) {
        mItemList.add(item);
    }

    public void removeAllItems() {
        mItemList.clear();
    }

    public void notifyDataChanged() {
        removeAllViews();
        for (int i = 0, size = mItemList.size(); i < size; ++i) {
            Item item = mItemList.get(i);
            if (item.mCustomView != null) {
                setOnClickListener(item.mCustomView, i);
                addView(item.mCustomView);
            } else if (item.mItemView != null) {
                setOnClickListener(item.mItemView, i);
                addView(item.mItemView);
                if (i == 0) {
                    item.mItemView.setBackgroundDrawable(getLeftDrawable());
                } else if (i == size - 1) {
                    item.mItemView.setBackgroundDrawable(getRightDrawable());
                } else {
                    item.mItemView.setBackgroundDrawable(getMiddleDrawable());

                }
            }
            if (i != size - 1) {
                addView(newSeparateLine(getContext()));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    private StateListDrawable getRoundItemDrawable(float[] radiusBuffer) {
        int selected = android.R.attr.state_selected;
        RoundRectShape shape = new RoundRectShape(radiusBuffer, null, null);
        ShapeDrawable selectedDrawable = new ShapeDrawable(shape);
        selectedDrawable.getPaint().setAntiAlias(true);
        selectedDrawable.getPaint().setColor(mSelectedColor);

        ShapeDrawable normalDrawable = new ShapeDrawable(shape);
        normalDrawable.getPaint().setAntiAlias(true);
        normalDrawable.getPaint().setColor(mColor);

        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(new int[]{selected}, selectedDrawable);
        listDrawable.addState(new int[]{-selected}, normalDrawable);
        return listDrawable;
    }

    private StateListDrawable getLeftDrawable() {
        float[] radiusBuffer = new float[]{mRadius, mRadius, 0, 0, 0, 0, mRadius, mRadius};
        return getRoundItemDrawable(radiusBuffer);
    }

    private StateListDrawable getMiddleDrawable() {
        float[] radiusBuffer = new float[]{0, 0, 0, 0, 0, 0, 0, 0};
        return getRoundItemDrawable(radiusBuffer);
    }

    private StateListDrawable getRightDrawable() {
        float[] radiusBuffer = new float[]{0, 0, mRadius, mRadius, mRadius, mRadius, 0, 0};
        return getRoundItemDrawable(radiusBuffer);
    }

    public void setCurrentItem(int position) {
        if (position < 0 || position >= mItemList.size()) {
            return;
        }
        for (Item item : mItemList) {
            if (item.mCustomView != null) {
                item.mCustomView.setSelected(false);
            }
            if (item.mItemView != null) {
                item.mItemView.setSelected(false);
            }
        }
        Item currentItem = mItemList.get(position);
        if (currentItem != null && currentItem.mCustomView != null) {
            currentItem.mCustomView.setSelected(true);
        }
        if (currentItem != null && currentItem.mItemView != null) {
            currentItem.mItemView.setSelected(true);
        }
    }

    private void setOnClickListener(final View view, final int position) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position, view);
                }
                setCurrentItem(position);
            }
        });
    }

    private View newSeparateLine(Context context) {
        View view = new View(context);
        view.setLayoutParams(new LayoutParams(mLineWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setBackgroundColor(mLineColor);
        return view;
    }

    public class Item {

        private View mCustomView;
        private ItemView mItemView;

        private Context mContext;

        private ColorStateList mTextColorList;

        private void initColorList() {
            int selected = android.R.attr.state_selected;
            mTextColorList = new ColorStateList(new int[][]{{selected}, {-selected}}, new int[]{mSelectedTextColor, mTextColor});

        }

        private LayoutParams newLayoutParams() {
            LayoutParams lp = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            return lp;
        }

        private void checkItemView() {
            if (mItemView == null) {
                mItemView = new ItemView(mContext);

                if (mTextColorList == null) {
                    initColorList();
                }
                mItemView.mTextView.setTextColor(mTextColorList);
                mItemView.mTextView.setTextSize(mTextSize);
                mItemView.setLayoutParams(newLayoutParams());
            }
        }

        public Item(Context context) {
            mContext = context;
        }

        public Item setCustomView(View customView) {
            mCustomView = customView;
            mCustomView.setLayoutParams(newLayoutParams());
            return this;
        }

        public Item setIcon(int drawableId) {
            checkItemView();
            mItemView.mIconView.setImageResource(drawableId);
            return this;
        }

        public Item setText(int strId) {
            checkItemView();
            mItemView.mTextView.setText(strId);
            return this;
        }

        public Item setText(String str) {
            checkItemView();
            mItemView.mTextView.setText(str);
            return this;
        }
    }

    /**
     * the default item view class, and you can custom the item view
     * the default orientation is horizontal
     */
    private static class ItemView extends LinearLayout {

        private ImageView mIconView;
        private TextView mTextView;

        public ItemView(Context context) {
            this(context, null);
        }

        public ItemView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setOrientation(HORIZONTAL);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
            lp.weight = 1;
            mIconView = new ImageView(context);
            mIconView.setLayoutParams(lp);

            lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
            lp.weight = 1;
            mTextView = new TextView(context);
            mTextView.setLayoutParams(lp);

            addView(mIconView);
            addView(mTextView);
        }
    }
}
