package com.muabe.uniboot.boot.wing;


import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;

import com.markjmind.uni.UniFragment;
import com.markjmind.uni.boot.FragmentBuilder;
import com.markjmind.uni.boot.UniBoot;
import com.muabe.propose.Motion;
import com.muabe.propose.Propose;
import com.muabe.uniboot.R;

/**
 * Created by MarkJ on 2016-10-29.
 */

public class MenuBoot extends UniBoot {
    public static int HOME;
    public static int LEFT;
    public static int RIGHT;

    private FrameLayout menu_drag, blur, left_touch, right_touch;

    private WingOption leftWing;
    private WingOption rightWing;

    /************************************** Must implement ************************************/

    public static MenuBoot get(FragmentActivity activity){
        return UniBoot.get(activity, MenuBoot.class);
    }

    public static MenuBoot get(UniFragment uniFragment){
        return UniBoot.get(uniFragment.getActivity(), MenuBoot.class);
    }

    public static MenuBoot putContentView(FragmentActivity activity){
        return UniBoot.putContentView(activity, MenuBoot.class);
    }


    @Override
    public void onAttach(FragmentActivity activity) {
        setCustomLayout(R.layout.uni_boot_frame_sliding_menu_layout);
        MenuBoot.HOME = id.home;
        MenuBoot.LEFT = id.left;
        MenuBoot.RIGHT = id.right;

        blur = activity.findViewById(R.id.uni_boot_frame_sliding_menu_blur);
        menu_drag = activity.findViewById(id.menu_drag);
        left_touch = activity.findViewById(R.id.uni_boot_frame_sliding_menu_touch_area_left);
        left_touch = activity.findViewById(R.id.uni_boot_frame_sliding_menu_touch_area_left);
        right_touch = activity.findViewById(R.id.uni_boot_frame_sliding_menu_touch_area_right);

        leftWing = new WingOption(view.left, left_touch, true);
        rightWing = new WingOption(view.right, right_touch, false);

        addBackPressObserver(MenuBoot.LEFT, new BackPressObserver() {
            @Override
            public boolean isBackPressed(int stackCount, UniBoot.BackPressAdapter backPressAdapter) {
                if(getLeft().isOpened()){
                    if(stackCount == 0){
                        getLeft().close();
                    }else{
                        backPressAdapter.backPressOfParents();
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
                        backPressAdapter.backPressOfParents();
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
                    backPressAdapter.backPressOfParents();
                    return true;
                }else{
                    return false;
                }
            }
        });
    }

    public static boolean onBackPressed(FragmentActivity activity){
        MenuBoot boot = MenuBoot.get(activity);
        return boot.onBackPressed();
    }


    public MenuBoot initHomeFragment(UniFragment uniFragment){
        FragmentBuilder.getBuilder(activity).popBackStackClear(true, MenuBoot.HOME);
        FragmentBuilder.getBuilder(activity)
                .setHistory(false)
                .setAllowingStateLoss(true)
                .replace(MenuBoot.HOME, uniFragment);

        return this;
    }

    private MenuBoot initMenu(int menu, WingOption wing,  int width_dp, UniFragment uniFragment){
        wing.setWidth(width_dp);
        wing.setEnable(true);
        FragmentBuilder.getBuilder(activity).popBackStackClear(true, menu);
        FragmentBuilder.getBuilder(activity)
                .setHistory(false)
                .setAllowingStateLoss(true)
                .replace(menu, uniFragment);
        return this;
    }


    public MenuBoot initLeftFragment(int width_dp, UniFragment uniFragment){
        return initMenu(MenuBoot.LEFT, leftWing, (int)(width_dp*activity.getResources().getDisplayMetrics().density), uniFragment);
    }

    public MenuBoot initLeftFragment(UniFragment uniFragment){
        return initMenu(MenuBoot.LEFT, leftWing, windowSize.x, uniFragment);
    }


    public MenuBoot initRightFragment(int width_dp, UniFragment uniFragment){
        return initMenu(MenuBoot.RIGHT, rightWing, (int)(width_dp*activity.getResources().getDisplayMetrics().density), uniFragment);
    }

    public MenuBoot initRightFragment(UniFragment uniFragment){
        return initMenu(MenuBoot.RIGHT, rightWing,windowSize.x, uniFragment);
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
                menu.getLayoutParams().width = width;
                menu.setX(-width);
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
                    animationInfo.motion.animate();

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
                    animationInfo.motion.reverseAnimate();
                    opened = false;
                }
            }
        }

        @Override
        public boolean isStartedAnimation(){
            return (animationInfo !=null && animationInfo.motion.isRun());
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
        public Motion motionRight;
        public Motion motionLeft;

        public AnimationInfo(boolean isLeft) {

            if(isLeft){
                this.motion = new Motion(Motion.RIGHT);
                this.propose = getLeftSldingAnimation();

            }else {
                this.motion = new Motion(Motion.LEFT);
                this.propose = getRightSldingAnimation();

            }
            propose.addMotion(motion);
        }

        private Propose getLeftSldingAnimation() {
            final Propose propose = new Propose(view.left.getContext());
            propose.setAutoCancel(false);

            ObjectAnimator leftSliding = ObjectAnimator.ofFloat(view.left, View.TRANSLATION_X, -leftWing.getWidth(), 0).setDuration(500);
            ObjectAnimator blurAnim = ObjectAnimator.ofFloat(blur, View.ALPHA, 0f, 1f).setDuration(500);
//            propose.setFlingMinAccelerator(3.0f);

            motion.setMotionListener(new MotionListener() {
                @Override
                public void onStart(Motion motion) {
                    if(rightWing.enable) {
                        right_touch.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onScroll(Motion motion, long currDuration, long totalDuration) {

                }

                @Override
                public void onEnd(Motion motion) {
//                    if(!motion.getStatus().equals(Motion.STATUS.run)){
//
//                    }
                }
            });

            propose.setProposeListener(new ProposeListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onScroll(Motion motion, long currDuration, long totalDuration) {

                }

                @Override
                public void onEnd() {
                    boolean isOpen = !(motion.getCurrDuration()==0);
                    leftWing.setOpened(isOpen);
                    blur.setClickable(false);
                    if(isOpen) {
                        menu_drag.setClickable(true);
                        menu_drag.setOnTouchListener(propose);
                        motion.enableSingleTabUp(true);
                    }else{
                        menu_drag.setClickable(false);
                        menu_drag.setOnTouchListener(null);
                        motion.enableSingleTabUp(false);
                    }
                }
            });
            motion.play(leftSliding, leftWing.getWidth()).with(blurAnim);
            motion.enableSingleTabUp(false);
            return propose;
        }

        private Propose getRightSldingAnimation() {
            final Propose propose = new Propose(view.right.getContext());
            propose.setAutoCancel(false);

            ObjectAnimator leftSliding = ObjectAnimator.ofFloat(view.right, View.TRANSLATION_X, windowSize.x, windowSize.x-rightWing.getWidth()).setDuration(500);
            ObjectAnimator blurAnim = ObjectAnimator.ofFloat(blur, View.ALPHA, 0f, 1f).setDuration(500);
//            propose.setFlingMinAccelerator(3.0f);

            motion.setMotionListener(new MotionListener() {
                @Override
                public void onStart(Motion motion) {
                    if(leftWing.enable) {
                        left_touch.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onScroll(Motion motion, long currDuration, long totalDuration) {

                }

                @Override
                public void onEnd(Motion motion) {
//                    if(!motion.getStatus().equals(Motion.STATUS.run)){
//
//                    }
                }
            });

            propose.setProposeListener(new ProposeListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onScroll(Motion motion, long currDuration, long totalDuration) {

                }

                @Override
                public void onEnd() {
                    boolean isOpen = !(motion.getCurrDuration()==0);
                    rightWing.setOpened(isOpen);
                    blur.setClickable(false);
                    if(isOpen) {
                        menu_drag.setClickable(true);
                        menu_drag.setOnTouchListener(propose);
                        motion.enableSingleTabUp(true);
                    }else{
                        menu_drag.setClickable(false);
                        menu_drag.setOnTouchListener(null);
                        motion.enableSingleTabUp(false);
                    }
                }
            });
            motion.play(leftSliding, rightWing.getWidth()).with(blurAnim);
            motion.enableSingleTabUp(false);
            return propose;
        }
    }

}
