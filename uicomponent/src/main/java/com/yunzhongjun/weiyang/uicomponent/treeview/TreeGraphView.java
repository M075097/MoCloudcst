package com.yunzhongjun.weiyang.uicomponent.treeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by wb-zyl208210 on 2017/3/31.
 * set树状数据结构 进来即可
 */
public class TreeGraphView extends ViewGroup {
    //    private TreeHolder treeHolder;
    private int nodeColUnitSize = convertdp2px(30);//列单元尺寸
    private int nodeRowUnitSize = convertdp2px(30);//行单元尺寸
    private double colGapSpaceRadio;

    private double rowGapSpaceRadio;
    private double colGapSapce;//列间距

    private double rowGapSapce;//行间距
    private double subBranchLineXOffsetRadio = 0.2;//单位px 除第一个节点外其他节点的X方向偏移量比例

    private double subBranchLineYOffsetRadio = 0.35;//单位px 线偏移整个节点View上部的比例
    private int colStepSpace;//绘制行步长

    private int rowStepSpace;//绘制列步长
    private Paint mTextPaint;

    private Paint mErrorTextPaint;
    private Paint mLinePaint;
    private Paint mRectPaint;
    private TreeAdapter mTreeAdapter;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mWidth;
    private int mHeight;
    private int mVisableHeight;
    private int mVisableWidth;

    public TreeGraphView(Context context) {
        this(context, null);
    }

    public TreeGraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TreeGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        initPaint();
        WindowManager windowManager = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        mScreenWidth = windowManager.getDefaultDisplay().getWidth();
        mScreenHeight = windowManager.getDefaultDisplay().getHeight();
    }

    private void initPaint() {
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setTextSize(convertdp2px(5));
        mTextPaint.setAntiAlias(true);

        mErrorTextPaint = new Paint();
        mErrorTextPaint.setColor(Color.GRAY);
        mErrorTextPaint.setTextSize(convertdp2px(15));
        mErrorTextPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(Color.GRAY);
        mLinePaint.setStrokeWidth(2);

        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setColor(Color.GREEN);
        mRectPaint.setStrokeWidth(2);
        mRectPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (this.mTreeAdapter != null && mTreeAdapter.getTreeNodeBean() != null&&mTreeAdapter.getNodeView()!=null) {//TODO 完善
            View nodeView = mTreeAdapter.getNodeView();
            nodeView.measure(0, 0);
            nodeColUnitSize = nodeView.getMeasuredWidth();
            nodeRowUnitSize = nodeView.getMeasuredHeight();
            Log.d("GView", "nodeColUnitSize=" + nodeColUnitSize + "==nodeRowUnitSize==" + nodeRowUnitSize);
            // init 行列间距
            colGapSpaceRadio = 1;//列间距比例 相对节点宽度
            rowGapSpaceRadio = 0.5;//行间距比例 相对于节点高度
            colStepSpace = (int) (nodeColUnitSize + nodeColUnitSize * colGapSpaceRadio);//计算列步长
            rowStepSpace = (int) (nodeRowUnitSize + nodeRowUnitSize * rowGapSpaceRadio);//计算行步长

            mWidth = (mTreeAdapter.getColNum() - 1) * colStepSpace + nodeColUnitSize;
            if (heightMode != MeasureSpec.EXACTLY) {
                mHeight = (int) (mTreeAdapter.getRowNum() * rowStepSpace - nodeRowUnitSize * rowGapSpaceRadio);
            }
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        } else {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.AT_MOST);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.AT_MOST);
        }
       /* widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);*/
        Log.d("GView", "mWidth=" + mWidth + "==mHeight==" + mHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        calculateVisableArea();
        if (mTreeAdapter != null && mTreeAdapter.getTreeNodeBean() != null) {
            layoutTree(mTreeAdapter.getTreeNodeBean());
        }
    }

    //计算当前view 可显示 的区域
    private void calculateVisableArea() {
        int[] mPosition = new int[2];
        this.getLocationOnScreen(mPosition);
        int rightMargin = 0;
        int bottomMargin = 0;
        try {
            if (this.getLayoutParams() instanceof FrameLayout.LayoutParams) {
                rightMargin = ((FrameLayout.LayoutParams) this.getLayoutParams()).rightMargin;
                bottomMargin = ((FrameLayout.LayoutParams) this.getLayoutParams()).bottomMargin;
            } else if (this.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                rightMargin = ((LinearLayout.LayoutParams) this.getLayoutParams()).rightMargin;
                bottomMargin = ((LinearLayout.LayoutParams) this.getLayoutParams()).bottomMargin;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {//此处-5dp只是为了 可以有个间距
            mVisableWidth = mScreenWidth - mPosition[0] - rightMargin - convertdp2px(5);
            mVisableHeight = mScreenHeight - mPosition[1] - bottomMargin - convertdp2px(5);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTreeAdapter != null && mTreeAdapter.getTreeNodeBean() != null) {
            drawTree(canvas, mTreeAdapter.getTreeNodeBean());
        } else {
            String desText = "无法生成拓扑关系!";
            canvas.drawText(desText, 0, desText.length(), (this.getWidth() - desText.length() * convertdp2px(15)) / 2, this.getHeight() / 2, mErrorTextPaint);
        }
    }

    float startX = 0;
    float startY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = event.getRawX();
                float currentY = event.getRawY();
                int difX = (int) (startX - currentX);
                int difY = (int) (startY - currentY);
                //Log.d("scrool", difX + "---" + difY);
                if ((difX + getScrollX()) < 0 || (difX + getScrollX()) > (this.getMeasuredWidth() - mVisableWidth)) {
                    difX = 0;
                }
                if ((difY + getScrollY()) < 0 || (difY + getScrollY()) > (this.getMeasuredHeight() - mVisableHeight)) {
                    difY = 0;
                }
               /* if ((difX + getScrollX()) < 0) {
                    difX = 0;
                }
                if ((difY + getScrollY()) < 0) {
                    difY = 0;
                }*/
                this.scrollBy(difX, difY);
                startX = currentX;
                startY = currentY;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_SCROLL:
                break;
        }
        return true;
    }

    public void layoutTree(TreeNode treeNode) {//布节点
        if (mTreeAdapter != null) {
            View nodeView = mTreeAdapter.getNodeView();//treadapter 中自定义的view
            mTreeAdapter.bindView(nodeView, (INodeModule) treeNode.getNodeModule());
            if (nodeView != null) {
                this.addView(nodeView);// bug 不添加 不显示
                nodeView.measure(0, 0);// bug 不测量 不显示
                nodeView.layout((treeNode.x * colStepSpace), (treeNode.y * rowStepSpace), (treeNode.x * colStepSpace + nodeColUnitSize), (treeNode.y * rowStepSpace + nodeRowUnitSize));
            }
        }
        Log.d("GViewLayout", "XX=" + (treeNode.x * colStepSpace) + "==YY==" + (treeNode.y * rowStepSpace));
        if (treeNode.getSubTreeNodeList() != null && treeNode.getSubTreeNodeList().size() > 0) {
            for (int i = 0; i < treeNode.getSubTreeNodeList().size(); i++) {
                List<TreeNode> subTreeNodeList = treeNode.getSubTreeNodeList();
                TreeNode subNode = subTreeNodeList.get(i);
                layoutTree(subNode);
            }
        }
    }

    private void drawTree(Canvas canvas, TreeNode treeNode) {//划线
        Log.d("GViewDraw", "XX=" + (treeNode.x * colStepSpace) + "==YY==" + (treeNode.y * rowStepSpace));
        if (treeNode.getSubTreeNodeList() != null && treeNode.getSubTreeNodeList().size() > 0) {
            for (int i = 0; i < treeNode.getSubTreeNodeList().size(); i++) {
                List<TreeNode> subTreeNodeList = treeNode.getSubTreeNodeList();
                TreeNode subNode = subTreeNodeList.get(i);
                drawTree(canvas, subNode);
                if (i == 0) {
                    canvas.drawLine((nodeColUnitSize + treeNode.x * colStepSpace), (float) (subNode.y * rowStepSpace + nodeRowUnitSize * subBranchLineYOffsetRadio), subNode.x * colStepSpace, (float) (subNode.y * rowStepSpace + nodeRowUnitSize * subBranchLineYOffsetRadio), mLinePaint);
                } else {
                    canvas.drawLine((float) (nodeColUnitSize * (1 + subBranchLineXOffsetRadio) + treeNode.x * colStepSpace), (float) (subNode.y * rowStepSpace + nodeRowUnitSize * subBranchLineYOffsetRadio), subNode.x * colStepSpace, (float) (subNode.y * rowStepSpace + nodeRowUnitSize * subBranchLineYOffsetRadio), mLinePaint);
                }
                if (i == treeNode.getSubTreeNodeList().size() - 1 && i != 0) {//TODO i是否需要判断
                    canvas.drawLine((float) (treeNode.x * colStepSpace + nodeColUnitSize * (1 + subBranchLineXOffsetRadio)), (float) (treeNode.y * rowStepSpace + nodeRowUnitSize * subBranchLineYOffsetRadio), (float) (treeNode.x * colStepSpace + nodeColUnitSize * (1 + subBranchLineXOffsetRadio)), (float) (subNode.y * rowStepSpace + nodeRowUnitSize * subBranchLineYOffsetRadio), mLinePaint);
                }
            }
        }
    }

    public void setTreeAdapter(TreeAdapter treeAdapter) {
        this.mTreeAdapter = treeAdapter;
        this.mTreeAdapter.attatchTreeView(this);//绑定view
        this.requestLayout();
    }

    public TreeAdapter getTreeAdapter() {
        return mTreeAdapter;
    }

    //数据变化 重新绘制
    public void notifyDataSetChange() {
        this.requestLayout();
    }

    public int convertdp2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public float convertpx2dp(int pxValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return pxValue / scale;
    }
}
