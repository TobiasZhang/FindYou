package widget;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/25 0025.
 */
public class MyLinearLayout extends LinearLayout {
    public List<List<View>> mAllChildViews = new ArrayList<>();
    private List<Integer> mLineHeight  = new ArrayList<>();

    public MyLinearLayout(Context context) {
        this(context,null);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //如果当前ViewGroup的宽高为wrap_content的情况
        int width = 0;//自己测量的 宽度
        int height = 0;//自己测量的高度
        int lineWidth = 0;
        int lineHeight = 0;
        int childCount = getChildCount();
        for(int i = 0;i<childCount;i++){
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            int childWidth = child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin ;
            int childHeight = child.getMeasuredHeight()+lp.topMargin+lp.topMargin;
            if(lineWidth+childWidth>sizeWidth){
                width = Math.max(lineWidth,width);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
            }else{
                lineWidth+=childWidth;
                lineHeight = Math.max(lineHeight,childHeight);
            }
            if(i==childCount-1){
                width = Math.max(width,lineWidth);
                height += lineHeight;
            }
            setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width,
                    modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllChildViews.clear();
        mLineHeight.clear();
        int width = getWidth();
        int lineHeight = 0;
        int lineWidth = 0;
        List<View> lineViews = new ArrayList<View>();
        int childCount = getChildCount();
        for(int i =0;i<childCount;i++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if(childWidth+lineWidth+lp.leftMargin+lp.rightMargin>width){
                mLineHeight.add(lineHeight);
                mAllChildViews.add(lineViews);
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                lineViews = new ArrayList();

            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }
        mLineHeight.add(lineHeight);
        mAllChildViews.add(lineViews);
        int left = 0;
        int top = 0;
        //获取行数
        int lineCount = mAllChildViews.size();
        for(int i = 0; i < lineCount; i ++){
            //当前行的views和高度
            lineViews = mAllChildViews.get(i);
            lineHeight = mLineHeight.get(i);
            for(int j = 0; j < lineViews.size(); j ++){
                View child = lineViews.get(j);
                //判断是否显示
                if(child.getVisibility() == View.GONE){
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int cLeft = left + lp.leftMargin;
                int cTop = top + lp.topMargin;
                int cRight = cLeft + child.getMeasuredWidth();
                int cBottom = cTop + child.getMeasuredHeight();
                //进行子View进行布局
                child.layout(cLeft, cTop, cRight, cBottom);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            left = 0;
            top += lineHeight;
        }

    }
    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    public ViewGroup.LayoutParams getLayoutParams(AttributeSet atr) {
        return new MarginLayoutParams(getContext(),atr);
    }
}
