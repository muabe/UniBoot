package com.muabe.uniboot.boot.wing;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.markjmind.propose.Motion;
import com.markjmind.propose.Propose;
import com.markjmind.propose.listener.MotionListener;
import com.markjmind.propose.listener.ProposeListener;
import com.markjmind.uni.UniFragment;
import com.markjmind.uni.boot.FragmentBuilder;
import com.markjmind.uni.boot.UniBoot;
import com.muabe.uniboot.R;

/**
 * Created by MarkJ on 2016-10-29.
 */

public class MenuBoot extends UniBoot {
    public static int HOME;
    public static int LEFT;
    public static int RIGHT;

    private FrameLayout blur, left_touch, right_touch;
    private WingOption leftWing;
    private WingOption rightWing;

    /************************************** Must implement ************************************/

    public static MenuBoot get(Activity activity){
        return UniBoot.get(activity, MenuBoot.class);
    }

    public static MenuBoot get(UniFragment uniFragment){
        return UniBoot.get(uniFragment.getActivity(), MenuBoot.class);
    }

    public static MenuBoot putContentView(Activity activity){
        return UniBoot.putContentView(activity, MenuBoot.class);
    }


    @Override
    public void onAttach(Activity activity) {
        setCustomLayout(R.layout.uni_boot_frame_sliding_menu_layout);
        MenuBoot.HOME = id.home;
        MenuBoot.LEFT = id.left;
        MenuBoot.RIGHT = id.right;

        blur = (FrameLayout) activity.findViewById(R.id.uni_boot_frame_sliding_menu_blur);
        left_touch = (FrameLayout) activity.findViewById(R.id.uni_boot_frame_sliding_menu_touch_area_left);
        right_touch = (FrameLayout) activity.findViewById(R.id.uni_boot_frame_sliding_menu_touch_area_right);

        leftWing = new WingOption(view.left, left_touch, true);
        rightWing = new WingOption(view.right, right_touch, false);

        addBackPressObserver(MenuBoot.LEFT, new BackPressObserver() {
            @Override
            public boolean isBackPressed(int stackCount, UniBoot.BackPressAdapter backPressAdapter) {
                if(getLeft().isOpened()){
                    if(stackCount == 0){
                        getLeft().close();
                    }else{
                        backPressAdapter.backPress();
                    }
                    return true;
                }else{
                    return false;
                }

            }
        });

        addBackPressObserver(MenuBoot.RIGHT, new BackPressObserver() {
            @Override
            public boolean isBackPressed(int stackCount, UniBoot.BackPressAdapter backPressAdapter) {
                if(getRight().isOpened()){
                    if(stackCount == 0){
                        getRight().close();
                    }else{
                        backPressAdapter.backPress();
                    }
                    return true;
                }else{
                    return false;
                }

            }
        });

        addBackPressObserver(MenuBoot.HOME, new BackPressObserver() {
            @Override
            public boolean isBackPressed(int stackCount, UniBoot.BackPressAdapter backPressAdapter) {
                if(stackCount > 0){
                    backPressAdapter.backPress();
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    public static boolean onBackPressed(Activity activity){
        MenuBoot boot = MenuBoot.get(activity);
        return boot.onBackPressed();
    }


    public MenuBoot initHomeFragment(UniFragment uniFragment){
        FragmentBuilder.getBuilder(activity)
                .setHistory(false)
                .setAllowingStateLoss(true)
                .replace(MenuBoot.HOME, uniFragment);
        FragmentBuilder.getBuilder(activity).clearHistory(MenuBoot.HOME);
        return this;
    }

    private MenuBoot initLeft(int width_dp, UniFragment uniFragment){
        leftWing.setWidth(width_dp);
        leftWing.setEnable(true);
        FragmentBuilder.getBuilder(activity)
                .setHistory(false)
                .setAllowingStateLoss(true)
                .replace(MenuBoot.LEFT, uniFragment);
        FragmentBuilder.getBuilder(activity).clearHistory(MenuBoot.HOME);
        return this;
    }

    public MenuBoot initLeftFragment(int width_dp, UniFragment uniFragment){
        return initLeft((int)(width_dp*activity.getResources().getDisplayMetrics().density), uniFragment);
    }

    public MenuBoot initLeftFragment(UniFragment uniFragment){
        return this.initLeft(windowSize.x, uniFragment);
    }

    private MenuBoot initRight(int width_dp, UniFragment uniFragment){
        rightWing.setWidth((int)(width_dp));
        rightWing.setEnable(true);
        FragmentBuilder.getBuilder(activity)
                .setHistory(false)
                .setAllowingStateLoss(true)
                .replace(MenuBoot.RIGHT, uniFragment);
        FragmentBuilder.getBuilder(activity).clearHistory(MenuBoot.HOME);
        return this;
    }

    public MenuBoot initRightFragment(int width_dp, UniFragment uniFragment){
        return initRight((int)(width_dp*activity.getResources().getDisplayMetrics().density), uniFragment);
    }

    public MenuBoot initRightFragment(UniFragment uniFragment){
        return this.initRight(windowSize.x, uniFragment);
    }

    public MenuOption getLeft(){
        return leftWing;
    }

    public MenuOption getRight(){
        return rightWing;
    }


    class WingOption implements MenuOption{
        AnimationInfo animationInfo;
        private int width;
        private boolean opened = false;
        private boolean enable = false;
        private View menu, touch;
        private boolean isLeft;

        public WingOption(View menu, View touch, boolean isLeft){
            this.menu = menu;
            this.touch = touch;
            this.isLeft = isLeft;
            setEnable(false);
        }

        @Override
        public int getWidth(){
            return width;
        }

        @Override
        public void setEnable(boolean enable){
            this.enable = enable;
            if(enable){
                touch.setVisibility(View.VISIBLE);
                menu.setVisibility(View.VISIBLE);
                animationInfo = new AnimationInfo(isLeft);
                touch.setOnTouchListener(animationInfo.propose);
            }else{
                touch.setVisibility(View.GONE);
                menu.setVisibility(View.GONE);
            }
        }

        @Override
        public void setWidth(int width){
            this.width = width;
        }

        @Override
        public void open(){
            if(animationInfo != null){
                if(!isStartedAnimation()) {
                    animationInfo.motion.startAnimation();
                }
            }
        }

        @Override
        public boolean isOpened(){
            return opened;
        }

        @Override
        public void close(){
            if(animationInfo != null){
                if(!isStartedAnimation()) {
                    animationInfo.motion.reverseAnimation();
                    opened = false;
                }
            }
        }

        @Override
        public boolean isStartedAnimation(){
            return (animationInfo !=null && animationInfo.motion.getStatus().equals(Motion.STATUS.run));
        }

        protected void setOpened(boolean isOpend){
            this.opened = isOpend;
            if(isOpend){
                if(leftWing.enable) {
                    left_touch.setVisibility(View.GONE);
                }
                if(rightWing.enable) {
                    right_touch.setVisibility(View.GONE);
                }
            }else{
                if(leftWing.enable){
                    left_touch.setVisibility(View.VISIBLE);
                }
                if(rightWing.enable){
                    right_touch.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public interface StateChangeListener{
        void onOpening();
        void onOpened();
        void onClosing();
        void onClosed();
    }

    private class AnimationInfo{
        public Propose propose;
        public Motion motion;

        public AnimationInfo(boolean isLeft) {
            if(isLeft){
                this.propose = getLeftSldingAnimation();
                this.motion = propose.motionRight;
            }else {
                this.propose = getRightSldingAnimation();
                this.motion = propose.motionLeft;
            }
        }

        private Propose getLeftSldingAnimation() {
            final Propose propose = new Propose(view.left.getContext());

            ObjectAnimator leftSliding = ObjectAnimator.ofFloat(view.left, View.TRANSLATION_X, -windowSize.x, leftWing.getWidth()-windowSize.x).setDuration(500);
            ObjectAnimator blurAnim = ObjectAnimator.ofFloat(blur, View.ALPHA, 0f, 1f).setDuration(500);
            propose.setFlingMinAccelerator(3.0f);

            propose.motionRight.setOnMotionListener(new MotionListener() {
                @Override
                public void onStart(boolean isForward) {
                    if(rightWing.enable) {
                        right_touch.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onScroll(long l, long l1, boolean b) {

                }

                @Override
                public void onEnd(boolean isForward) {
                    if(!propose.motionRight.getStatus().equals(Motion.STATUS.run)){

                    }
                }
            });

            propose.setOnProposeListener(new ProposeListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onScroll(int i, long l, long l1) {

                }

                @Override
                public void onEnd() {
                    boolean isOpen = !(propose.motionRight.getCurrDuration()==0);
                    leftWing.setOpened(isOpen);
                    blur.setClickable(false);
                    if(isOpen) {
                        blur.setOnTouchListener(propose);
                        propose.motionRight.enableSingleTabUp(true);
                    }else{
                        blur.setOnTouchListener(null);
                        propose.motionRight.enableSingleTabUp(false);
                    }
                }
            });
            propose.motionRight.play(leftSliding, leftWing.getWidth()).with(blurAnim);
            propose.motionRight.enableSingleTabUp(false);
            return propose;
        }

        private Propose getRightSldingAnimation() {
            final Propose propose = new Propose(view.right.getContext());

            ObjectAnimator leftSliding = ObjectAnimator.ofFloat(view.right, View.TRANSLATION_X, windowSize.x, windowSize.x-rightWing.getWidth()).setDuration(500);
            ObjectAnimator blurAnim = ObjectAnimator.ofFloat(blur, View.ALPHA, 0f, 1f).setDuration(500);
            propose.setFlingMinAccelerator(3.0f);

            propose.motionLeft.setOnMotionListener(new MotionListener() {
                @Override
                public void onStart(boolean isForward) {
                    if(leftWing.enable) {
                        left_touch.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onScroll(long l, long l1, boolean b) {

                }

                @Override
                public void onEnd(boolean isForward) {
                    if(!propose.motionLeft.getStatus().equals(Motion.STATUS.run)){

                    }
                }
            });

            propose.setOnProposeListener(new ProposeListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onScroll(int i, long l, long l1) {

                }

                @Override
                public void onEnd() {
                    boolean isOpen = !(propose.motionLeft.getCurrDuration()==0);
                    rightWing.setOpened(isOpen);
                    blur.setClickable(false);
                    if(isOpen) {
                        blur.setOnTouchListener(propose);
                        propose.motionLeft.enableSingleTabUp(true);
                    }else{
                        blur.setOnTouchListener(null);
                        propose.motionLeft.enableSingleTabUp(false);
                    }
                }
            });
            propose.motionLeft.play(leftSliding, rightWing.getWidth()).with(blurAnim);
            propose.motionLeft.enableSingleTabUp(false);
            return propose;
        }
    }

}
