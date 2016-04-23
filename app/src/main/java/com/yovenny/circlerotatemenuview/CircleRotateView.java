package com.yovenny.circlerotatemenuview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

//TODO 运用简单的内部添加控件,控制动画,控制touchEvent实现.辅助控件较多可能存在性能的问题.
//TODO 由于rotateAnimation.RELATIVE_TO_PARENT不能使菜单项围绕中心点旋转,现使用折衷的方式(rotateAnimation.RELATIVE_TO_SELF在菜单项套一层view,但相应的代码会增多)

public class CircleRotateView extends FrameLayout {
    private float mWidth, mCircleWidth, mCircleRadium;//控件的宽,实际circle的宽,实际circle的半径
    private float mMenuHandleStartDegree;//菜单柄的放置角度
    private float mMenuIntervalDegree;//每个菜单项的间隔角度
    private float mCircleHandleStarDegree;//旋转柄的放置角度
    private float mMenuWidth;//每个菜单项的宽高

    private Context mContext;
    private float mCenterX, mCenterY;//中心坐标点
    private float mMinDegree = -360, mMaxDegree = 360;//可旋转的最大最小角度 default
    private boolean mRotateEnable = true;//控件是否可以旋转
    private boolean isMenuShowing;//菜单集是否正在显示
    private OnRotateListener mOnRotateListener;//旋转柄的过程监听


    //内部相关辅助view
    private List<ImageButton> mMenuList = new ArrayList<>();//菜单项list
    private List<RelativeLayout> mMenuRelativeList = new ArrayList<>();//菜单项容器list
    private View mCircleBgView;
    private View mCircleHandleView;
    private RelativeLayout mCircleHandleRelative;

    public CircleRotateView(Context context) {
        super(context);

    }

    public CircleRotateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);

    }

    public CircleRotateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleRotateView);
        mMenuWidth = a.getDimensionPixelOffset(R.styleable.CircleRotateView_menu_width, 120);
        mMenuHandleStartDegree = a.getFloat(R.styleable.CircleRotateView_menu_handle_start_degree, 90);
        mMenuIntervalDegree =  a.getFloat(R.styleable.CircleRotateView_menu_interval_degree, 30);
        mCircleHandleStarDegree =  a.getFloat(R.styleable.CircleRotateView_circle_handle_start_degree, -43);
        mMaxDegree=a.getFloat(R.styleable.CircleRotateView_max_rotate_degree,180);
        mMinDegree=a.getFloat(R.styleable.CircleRotateView_min_rotate_degree,-180);
    }

    //属性初始辅助方法
//=============================================================================================================

    public CircleRotateView setMenuResource(int... resArray) {
        mMenuList.clear();
        mMenuRelativeList.clear();
        for (int i = 0; i < resArray.length; i++) {
            RelativeLayout rl = new RelativeLayout(mContext);
            ImageButton ib = new ImageButton(mContext);
            ib.setClickable(true);
            ib.setBackgroundResource(resArray[i]);
            if (i != 0) {
                rl.setVisibility(INVISIBLE);
            }
            mMenuRelativeList.add(rl);
            mMenuList.add(ib);
        }
        return this;

    }

    public CircleRotateView setMenuClickListener(OnClickListener... clickArray) {
        for (int i = 0; i < mMenuList.size(); i++) {
            mMenuList.get(i).setOnClickListener(clickArray[i]);
        }
        return this;
    }

    public CircleRotateView setCircleBgResource(int circleBgResource) {
        mCircleBgView = new View(mContext);
        mCircleBgView.setClickable(true);
        mCircleBgView.setBackgroundResource(circleBgResource);
        return this;
    }

    public CircleRotateView setCircleHandleView(View v) {
        mCircleHandleView = v;
        mCircleHandleRelative = new RelativeLayout(mContext);
        return this;
    }

    public CircleRotateView setRotateDegree(float maxDegree, float minDegree) {
        mMaxDegree = maxDegree;
        mMinDegree = minDegree;
        return this;
    }

    public CircleRotateView setOnRotateListener(OnRotateListener rotateListener) {
        mOnRotateListener = rotateListener;
        return this;
    }

    public CircleRotateView setRotateHandleStartDegree(float degree){
        mCircleHandleStarDegree=degree;
        return this;
    }

    public CircleRotateView setMenuHandleStartDegree(float degree){
        mMenuHandleStartDegree=degree;
        return this;
    }

    public CircleRotateView setMenuIntervalDegree(float degree){
        mMenuIntervalDegree=degree;
        return this;
    }

    public CircleRotateView setMenuItemWidth(float width){
        mMenuWidth=width;
        return this;
    }




    public interface OnRotateListener {
        void onRotateMin();

        void onRotateMax();

        void onRotate(float degree);
    }

//=============================================================================================================

//	已知圆心，半径，角度，求圆上的点坐标
//	圆点坐标：(x0,y0)
//	半径：r
//	角度：a0
//
//	则圆上任一点为：（x1,y1）
//	x1   =   x0   +   r   *   cos(ao   *   3.14   /180   )
//	y1   =   y0   +   r   *   sin(ao   *   3.14   /180   )

    public void checkMenuBounds() {
        addCircleBg();

        if (mCircleHandleView != null) {
            //添加菜单单项
            float x1 = (float) (mCenterX + mCircleRadium * Math.cos(mCircleHandleStarDegree * Math.PI / 180));
            float y1 = (float) (mCenterY + mCircleRadium * Math.sin(mCircleHandleStarDegree * Math.PI / 180));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = (int) (x1 - mMenuWidth / 2);
            params.topMargin = (int) (y1 - mMenuWidth / 2);
            if (mCircleHandleView.getParent() != null) {
                ((ViewGroup) mCircleHandleView.getParent()).removeAllViews();
            }
            mCircleHandleRelative.addView(mCircleHandleView, params);

            //添加容器
            LayoutParams containerParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(mCircleHandleRelative, containerParams);
            initHandleRotate(mCircleHandleRelative, mCircleHandleView);
        }

        for (int i = 0; i < mMenuList.size(); i++) {
            //添加菜单单项
            float x1 = (float) (mCenterX + mCircleRadium * Math.cos(mMenuHandleStartDegree + (i * mMenuIntervalDegree) * Math.PI / 180));
            float y1 = (float) (mCenterY + mCircleRadium * Math.sin(mMenuHandleStartDegree + (i * mMenuIntervalDegree) * Math.PI / 180));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) mMenuWidth, (int) mMenuWidth);
            params.leftMargin = (int) (x1 - mMenuWidth / 2);
            params.topMargin = (int) (y1 - mMenuWidth / 2);
            if (mMenuList.get(i).getParent() != null) {
                ((ViewGroup) mMenuList.get(i).getParent()).removeAllViews();
            }
            mMenuRelativeList.get(i).addView(mMenuList.get(i), params);

            //添加容器
            LayoutParams containerParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(mMenuRelativeList.get(i), containerParams);
        }

    }

    private void addCircleBg() {
        if (mCircleBgView != null) {
            LayoutParams params = new LayoutParams((int) mCircleWidth, (int) mCircleWidth);
            params.topMargin = (int) mMenuWidth / 2;
            params.leftMargin = (int) mMenuWidth / 2;
            if (mCircleBgView.getParent() != null) {
                ((ViewGroup) mCircleBgView.getParent()).removeAllViews();
            }
            addView(mCircleBgView, params);
        }

    }


    //处理旋转柄的事件处理
    private void initHandleRotate(View container, final View handle) {
        /**
         * TODO rotation 两个方法,区别在于松开的恢复旋转的方向不同.rotation(4prams) fixed direction:rotation(6prams) base you rotate direction
         */
        container.setOnTouchListener(new OnTouchListener() {
            private float curX;
            private float curY;
            private float saveX;
            private float saveY;
            private float fromDegree;
            private float toDegree;
            //private float originDegree;
            private RotateAnimation circleRotate;
            private RotateAnimation handleRotate;

            private boolean isConsume;//标记本次事件已经废除.


            public void updateDegreeRange() {
                //更正末尾值
                if (toDegree > mMaxDegree) {
                    toDegree = mMaxDegree;
                } else if (toDegree < mMinDegree) {
                    toDegree = mMinDegree;
                }
                mMaxDegree = mMaxDegree - toDegree;
                mMinDegree = mMinDegree - toDegree;
            }


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!mRotateEnable) {
                    return false;
                }
                curX = event.getX(0);
                curY = event.getY(0);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                      按下,判断是否为handle的位置
                        Rect outRect = new Rect();
                        handle.getHitRect(outRect);
                        if (!outRect.contains((int) curX, (int) curY)) return false;
//                        originDegree = rotation(mCenterX,mCenterY,curX,curY);
                        fromDegree = 0;
                        isConsume=false;
                        saveX = curX;
                        saveY = curY;
                        break;
                    case MotionEvent.ACTION_UP:
                        circleRotate = AnimationUtil.getRotateAnimation(fromDegree % 360, 0, 500);
                        startAnimation(circleRotate);

                        handleRotate = AnimationUtil.getRotateAnimation(-fromDegree % 360, 0, 500);
                        handle.startAnimation(handleRotate);
                        mMenuList.get(0).startAnimation(handleRotate);

                        updateDegreeRange();//更新最大最小的旋转角度
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if(isConsume) return true;
                        float degree = (float) rotation(mCenterX, mCenterY, saveX, saveY, curX, curY);
                        if (Float.compare(degree, Float.NaN) == 0) return false;
                        toDegree = fromDegree + degree;
//                        Log.i("Circle","toDegree"+toDegree+"mMinDegree"+mMinDegree+"mMaxDegree"+mMaxDegree);
//                       float toDegree = rotation(mCenterX,mCenterY,curX,curY)-originDegree;//
                        //超出最大最小 进行事件回调
                        if (toDegree < mMinDegree) {
                            if (mOnRotateListener != null) {
                                mOnRotateListener.onRotateMin();
                            }
                            isConsume=true;
                            return true;
                        }
                        if (toDegree > mMaxDegree) {
                            if (mOnRotateListener != null) {
                                mOnRotateListener.onRotateMax();
                            }
                            isConsume=true;
                            return true;
                        }
                        if (mOnRotateListener != null) {
                            mOnRotateListener.onRotate(toDegree);
                        }

                        circleRotate = AnimationUtil.getRotateAnimation(fromDegree, toDegree);
                        startAnimation(circleRotate);

                        handleRotate = AnimationUtil.getRotateAnimation(-fromDegree, -toDegree);
                        handle.startAnimation(handleRotate);
                        mMenuList.get(0).startAnimation(handleRotate);
                        fromDegree = toDegree;
                        saveX = curX;
                        saveY = curY;
                        break;
                }

                //判断菜单是否正在显示
                if (isMenuShowing)
                    hideMenu();

                return true;
            }
        });
    }


    // 取旋转角度
    private float rotation(float x1, float y1, float x2, float y2) {
        double delta_x = x1 - x2;
        double delta_y = y1 - y2;
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }


    // 取旋转角度,根据象限
    private double rotation(float centerX, float centerY, float saveX, float saveY, float curX, float curY) {
        double a = Math.sqrt((saveX - curX) * (saveX - curX) + (saveY - curY) * (saveY - curY));
        double b = Math.sqrt((centerX - curX) * (centerX - curX) + (centerY - curY) * (centerY - curY));
        double c = Math.sqrt((saveX - centerX) * (saveX - centerX) + (saveY - centerY) * (saveY - centerY));
        double cosA = (b * b + c * c - a * a) / (2 * b * c);
        double arcA = Math.acos(cosA);
        double degree = arcA * 180 / Math.PI;
        if (saveY < centerY && curY < centerY) {
            if (saveX < centerX && curX > centerX) {
                return degree;
            } else if (saveX >= centerX && curX <= centerX) {
                return -degree;
            }
        }
        if (saveY > centerY && curY > centerY) {
            if (saveX < centerX && curX > centerX) {
                return -degree;
            } else if (saveX > centerX && curX < centerX) {
                return degree;
            }
        }
        if (saveX < centerX && curX < centerX) {
            if (saveY < centerY && curY > centerY) {
                return -degree;
            } else if (saveY > centerY && curY < centerY) {
                return degree;
            }
        }
        if (saveX > centerX && curX > centerX) {
            if (saveY > centerY && curY < centerY) {
                return -degree;
            } else if (saveY < centerY && curY > centerY) {
                return degree;
            }
        }
        float tanB = (saveY - centerY) / (saveX - centerX);
        float tanC = (curY - centerY) / (curX - centerX);
        if ((saveX > centerX && saveY > centerY && curX > centerX && curY > centerY && tanB > tanC)
                || (saveX > centerX && saveY < centerY && curX > centerX && curY < centerY && tanB > tanC)
                || (saveX < centerX && saveY < centerY && curX < centerX && curY < centerY && tanB > tanC)
                || (saveX < centerX && saveY > centerY && curX < centerX && curY > centerY && tanB > tanC))
            return -degree;
        return degree;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = right - left;
        mCenterX = mCenterY = mWidth / 2;
        mCircleWidth = mWidth - mMenuWidth;
        mCircleRadium = mCircleWidth / 2;
        checkMenuBounds();
    }

    public boolean isRotateEnable() {
        return mRotateEnable;
    }

    public void setRotateEnable(boolean rotateEnable) {
        mRotateEnable = rotateEnable;
    }


    //展示菜单
    public void showMenu() {
        isMenuShowing = true;
        for (int i = 1; i < mMenuRelativeList.size(); i++) {
            RotateAnimation rotateAnimation = AnimationUtil.getRotateAnimation(-(mMenuIntervalDegree * i), 0, 0.5f, 0.5f, (100 + 150 * (i - 1)));
            AlphaAnimation alphaAnimation = AnimationUtil.getAlphaAnimation(0.5f, 1, 500 - (100 * (i - 1)));
            AnimationSet animationSet = AnimationUtil.getAnimationSet(rotateAnimation, alphaAnimation);
            animationSet.setFillAfter(true);
            mMenuRelativeList.get(i).startAnimation(animationSet);
        }
    }

    //隐藏菜单
    public void hideMenu() {
        isMenuShowing = false;
        for (int i = 1; i < mMenuRelativeList.size(); i++) {
            RotateAnimation rotateAnimation = AnimationUtil.getRotateAnimation(0, -(mMenuIntervalDegree * i), 0.5f, 0.5f, (100 + 150 * (i - 1)));
            AlphaAnimation alphaAnimation = AnimationUtil.getAlphaAnimation(1, 0.5f, 500 - (100 * (i - 1)));
            AnimationSet animationSet = AnimationUtil.getAnimationSet(rotateAnimation, alphaAnimation);
            mMenuRelativeList.get(i).startAnimation(animationSet);
        }
    }

    //切换菜单
    public void toggleMenu() {
        if (isMenuShowing) {
            hideMenu();
        } else {
            showMenu();
        }
    }


    /**
     * 简单获取anima的封装
     */
    public static class AnimationUtil {

        public static RotateAnimation getRotateAnimation(float from, float to, float pivotXValue,
                                                         float pivotYValue, int duration) {
            RotateAnimation animation = new RotateAnimation(from, to,
                    Animation.RELATIVE_TO_SELF, pivotXValue, Animation.RELATIVE_TO_SELF,
                    pivotYValue);
            animation.setDuration(duration);
            return animation;
        }

        public static RotateAnimation getRotateAnimation(float from, float to, int duration) {
            RotateAnimation animation = getRotateAnimation(from,to,0.5f,0.5f,duration);
            return animation;
        }

        public static RotateAnimation getRotateAnimation(float from, float to) {
            RotateAnimation animation = new RotateAnimation(from, to,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            animation.setFillAfter(true);
            return animation;
        }

        public static AlphaAnimation getAlphaAnimation(float from, float to, int duration) {
            AlphaAnimation animation = new AlphaAnimation(from, to);
            animation.setDuration(duration);
            return animation;
        }

        public static AnimationSet getAnimationSet(Animation... animArray) {
            AnimationSet animationSet = new AnimationSet(false);
            for (Animation animation : animArray) {
                animationSet.addAnimation(animation);
            }
            return animationSet;

        }
    }



}
