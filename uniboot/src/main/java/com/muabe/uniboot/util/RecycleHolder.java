package com.muabe.uniboot.util;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-06-13
 */
public class RecycleHolder {

}

//        extends RecyclerView.ViewHolder implements HolderInterface{
//    public HashMap<Integer, View> views = new HashMap();
//    private Finder finder;
//    private View layout;
//
//    public RecycleHolder(View itemView) {
//        super(itemView);
//        initFinder(itemView);
//        this.layout = itemView;
//    }
//
//    private void initFinder(final View finder){
//        this.finder = new Finder() {
//            @Override
//            public View findViewById(int id) {
//                return finder.findViewById(id);
//            }
//        };
//    }
//
//    public View getView(int id){
//        if(views.containsKey(id)) {
//            return views.GET(id);
//        }else{
//            View view = finder.findViewById(id);
//            views.put(id, view);
//            return view;
//        }
//    }
//
//    public View getLayout(){
//        return layout;
//    }
//
//    public TextView getTextView(int id){
//        return (TextView) getView(id);
//    }
//
//    public ImageView getImageView(int id){
//        return (ImageView) getView(id);
//    }
//
//    public EditText getEditText(int id){
//        return (EditText) getView(id);
//    }
//
//    public Button getButton(int id){
//        return (Button) getView(id);
//    }
//
//    public ViewGroup getViewGroup(int id){
//        return (ViewGroup) getView(id);
//    }
//
//    public LinearLayout getLinearLayout(int id){
//        return (LinearLayout) getView(id);
//    }
//
//    public FrameLayout getFrameLayout(int id){
//        return (FrameLayout) getView(id);
//    }
//
//
//}
