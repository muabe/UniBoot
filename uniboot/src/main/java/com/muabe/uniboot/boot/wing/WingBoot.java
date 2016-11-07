package com.muabe.uniboot.boot.wing;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.FragmentManager;
import android.util.Log;
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

public class WingBoot extends UniBoot {
    public static int HOME;
    public static int LEFT;
    public static int RIGHT;

    private FrameLayout blur, left_touch, right_touch;
    private WingOption leftWing;
    private WingOption rightWing;

    /************************************** Must implement ************************************/

    public static WingBoot get(Activity activity){
        return UniBoot.get(activity, WingBoot.class);
    }

    public static WingBoot get(UniFragment uniFragment){
        return UniBoot.get(uniFragment.getActivity(), WingBoot.class);
    }

    public static WingBoot putContentView(Activity activity){
        return UniBoot.putContentView(activity, WingBoot.class);
    }


    @Override
    public void onAttach(Activity activity) {
        setCustomLayout(R.layout.uni_boot_frame_sliding_menu_layout);
        WingBoot.HOME = id.home;
        WingBoot.LEFT = id.left;
        WingBoot.RIGHT = id.right;

        blur = (FrameLayout) activity.findViewById(R.id.uni_boot_frame_sliding_menu_blur);
        left_touch = (FrameLayout) activity.findViewById(R.id.uni_boot_frame_sliding_menu_touch_area_left);
        right_touch = (FrameLayout) activity.findViewById(R.id.uni_boot_frame_sliding_menu_touch_area_right);

        leftWing = new WingOption(view.left, left_touch);
        rightWing = new WingOption(view.right, right_touch);

    }

    public static boolean onBackPressed(Activity activity){
        WingBoot wingBoot = WingBoot.get(activity);
        return wingBoot.onBackPressed();
    }

    /************************************** End ************************************/


    private Propose getLeftSldingAnimation() {
        final Propose propose = new Propose(view.left.getContext());

        ObjectAnimator leftSliding = ObjectAnimator.ofFloat(view.left, View.TRANSLATION_X, leftWing.getWidth() - windowSize.x).setDuration(500);
        ObjectAnimator blurAnim = ObjectAnimator.ofFloat(blur, View.ALPHA, 1f, 1f).setDuration(500);
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
                }else{
                    blur.setOnTouchListener(null);
                }
            }
        });
        propose.motionRight.play(leftSliding, leftWing.getWidth()).with(blurAnim);
        propose.motionRight.enableSingleTabUp(false);
        return propose;
    }

    public WingBoot setHomeFragment(UniFragment uniFragment){
        FragmentBuilder.getBuilder(activity)
                .replace(WingBoot.HOME, uniFragment);
        return this;
    }

    public WingBoot setLeftFragment(int width_dp, UniFragment uniFragment){
        leftWing.setWidth((int)(width_dp*activity.getResources().getDisplayMetrics().density));
        leftWing.setEnable(true);
        FragmentBuilder.getBuilder(activity)
                .replace(WingBoot.LEFT, uniFragment);
        return this;
    }

    public WingBoot setLeftFragment(UniFragment uniFragment){
        return this.setLeftFragment(windowSize.x, uniFragment);
    }

    public WingBoot setRightFragment(int width_dp, UniFragment uniFragment){
        rightWing.setWidth((int)(width_dp*activity.getResources().getDisplayMetrics().density));
        rightWing.setEnable(true);
        FragmentBuilder.getBuilder(activity)
                .replace(WingBoot.RIGHT, uniFragment);
        return this;
    }

    public WingBoot setRightFragment(UniFragment uniFragment){
        return this.setRightFragment(windowSize.x, uniFragment);
    }

    public MenuOption getLeft(){
        return leftWing;
    }

    public MenuOption getRight(){
        return rightWing;
    }


    class WingOption implements MenuOption{
        Propose propose;
        private int width;
        private boolean opened = false;
        private boolean enable = false;
        private View menu, touch;

        public WingOption(View menu, View touch){
            this.menu = menu;
            this.touch = touch;
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
                propose = getLeftSldingAnimation();
                touch.setOnTouchListener(propose);
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
            if(propose != null){
                if(!isStartedAnimation()) {
                    propose.motionRight.startAnimation();
                }
            }
        }

        @Override
        public boolean isOpened(){
            return opened;
        }

        @Override
        public void close(){
            if(propose != null){
                if(!isStartedAnimation()) {
                    propose.motionRight.reverseAnimation();
                    opened = false;
                }
            }
        }

        @Override
        public boolean isStartedAnimation(){
            return (propose!=null && propose.motionRight.getStatus().equals(Motion.STATUS.run));
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


    private boolean onBackPressed(){
        FragmentManager fragmentManager = activity.getFragmentManager();
        String leftTag = FragmentBuilder.getDefalutStack(WingBoot.LEFT);
        String rightTag = FragmentBuilder.getDefalutStack(WingBoot.RIGHT);
        String homeTag = FragmentBuilder.getDefalutStack(WingBoot.HOME);
        int leftSize = getBackStackEntryCount(fragmentManager, leftTag);
        int rightSize = getBackStackEntryCount(fragmentManager, rightTag);
        int homeSize = getBackStackEntryCount(fragmentManager, homeTag);
        Log.e("sdf",leftTag+":"+leftSize);
        Log.e("sdf",homeTag+":"+homeSize);
        if(getLeft().isOpened()){
            if(leftSize == 0){
                getLeft().close();
            }else{
                //TODO getCurrentFragment.onbackpressed
                fragmentManager.popBackStackImmediate(leftTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }else if(getRight().isOpened()){
            if(rightSize == 0){
                getRight().close();
            }else{
                //TODO getCurrentFragment.onbackpressed
                fragmentManager.popBackStackImmediate(rightTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }else if(homeSize > 0){
            //TODO getCurrentFragment.onbackpressed
            fragmentManager.popBackStackImmediate(homeTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else{
            return false;
        }
        return true;
    }

    private int getBackStackEntryCount(FragmentManager fragmentManager, String tag) {
        int count = 0;
        int size = fragmentManager.getBackStackEntryCount();
        for ( int entry = 0; entry < size; entry++ ) {
            String name = fragmentManager.getBackStackEntryAt(entry).getName();
            Log.i("dd",tag+":"+name+" "+(entry+1)+"/"+size);
            if ( tag.equals(name) ){
                count++;
            }
        }
        return count;
    }
}
