package com.example.amap.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amap.R;
import com.example.amap.custom.MyToast;
import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.OnItemMovedListener;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.util.ArrayList;

/**
 * Created by Zeashon on 2015/5/10.
 */
public class MakeRoadActivity extends Activity {
    Button search;
    Button search2;
    Button rount;
    TextView editText;
    TextView editText2;
    Button buttonadd;
    MyToast toast;
    DynamicListView listView;
    ArrayAdapter<String> adapter;
    private static final int INITIAL_DELAY_MILLIS = 300;

    private int mNewItemCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_road);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//        fullSetWay page
        Button fullSetWayBtn =(Button) findViewById(R.id.fullSetWay);
        fullSetWayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeRoadActivity.this, FullSetWayActivity.class);
                startActivity(intent);
            }
        });
        Button btn = (Button)findViewById(R.id.makeRoad_GoBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeRoadActivity.this.finish();
            }
        });
        search=(Button)findViewById(R.id.search);
        search2=(Button)findViewById(R.id.search2);

        editText=(TextView)findViewById(R.id.editText);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeRoadActivity.this, SearchActivity.class);
                intent.putExtra("currentsearch", editText.getText().toString());
                intent.putExtra("currentnum", -2);
                startActivityForResult(intent, 1);
            }
        });
        editText2=(TextView)findViewById(R.id.editText2);
        search2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeRoadActivity.this, SearchActivity.class);
                intent.putExtra("currentsearch", editText2.getText().toString());
                intent.putExtra("currentnum", -1);
                startActivityForResult(intent, 1);
            }
        });
        /*
        this is opensource
         */
        listView = (DynamicListView) findViewById(R.id.dynamiclistview);
//        listView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.activity_dynamiclistview_header, listView, false));

        /* Setup the adapter */
        adapter = new MyListAdapter(this);
        SimpleSwipeUndoAdapter simpleSwipeUndoAdapter = new SimpleSwipeUndoAdapter(adapter, this, new MyOnDismissCallback(adapter));
        AlphaInAnimationAdapter animAdapter = new AlphaInAnimationAdapter(simpleSwipeUndoAdapter);
        animAdapter.setAbsListView(listView);
        assert animAdapter.getViewAnimator() != null;
        animAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
        listView.setAdapter(animAdapter);

        /* Enable drag and drop functionality */
        listView.enableDragAndDrop();
        listView.setDraggableManager(new TouchViewDraggableManager(R.id.list_row_draganddrop_touchview));
        listView.setOnItemMovedListener(new MyOnItemMovedListener(adapter));
        listView.setOnItemLongClickListener(new MyOnItemLongClickListener(listView));

        /* Enable swipe to dismiss */
        listView.enableSimpleSwipeUndo();

        /* Add new items on item click */
        listView.setOnItemClickListener(new MyOnItemClickListener(listView));
        buttonadd=(Button)findViewById(R.id.buttonadd);
        buttonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.add(getBaseContext().getString(R.string.newly_added_item, mNewItemCount));
                mNewItemCount++;
            }
        });
        rount=(Button)findViewById(R.id.rount);
        rount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start=editText.getText().toString();
                String end=editText2.getText().toString();
                if(start==null||"".equals(start)){
                    if(toast == null) {
                    toast = MyToast.makeText(getApplicationContext(), R.string.start_noset, 1);
                    }
                    toast.show();
                    return;
                }
                else if(end==null||"".equals(end)){
                    if(toast == null) {
                        toast = MyToast.makeText(getApplicationContext(), R.string.end_noset, 1);
                    }
                    toast.show();
                    return;
                }
                else{
                    Bundle bundle=new Bundle();
                    bundle.putString("start", start);
                    bundle.putString("end", end);
                    ArrayList<String> mids=new ArrayList<String>();
                     mids=(ArrayList<String>)adapter.getItems();
                    bundle.putStringArrayList("mids",mids);
                    Intent intent=getIntent();
                    intent.putExtras(bundle);
                    MakeRoadActivity.this.setResult(1, intent);
                    MakeRoadActivity.this.finish();
                }
            }
        });
    }
    @Override
    protected void onPostCreate (Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        String mstart=this.getIntent().getStringExtra("mstart");
        if(!"".equals(mstart)&&mstart!=null){
            editText.setText(mstart);
            if(getResources().getString(R.string.mylocation).equals(mstart)){
                editText.setTextColor(getResources().getColor(R.color.novoda_blue));
            }
        }
        else if(this.getIntent().getBooleanExtra("islocation", false)==true){
            editText.setText(getResources().getString(R.string.mylocation));
            editText.setTextColor(getResources().getColor(R.color.novoda_blue));
        }
        String mend=this.getIntent().getStringExtra("mend");
        if(!"".equals(mend)&&mend!=null){
            editText2.setText(mend);
        }
        ArrayList<String> mmid=this.getIntent().getStringArrayListExtra("mmid");
        if(mmid!=null&&mmid.size()>0){
            for(int i=0;i<mmid.size();i++){
                adapter.add(mmid.get(i));
                mNewItemCount++;
            }
        }
    }
    public void onActivityResult(int requestCode,int resultCode,Intent intent){
        Bundle bundle=null;
        try{
            bundle=intent.getExtras();
        }
        catch(Exception e){
            return;
        }


        if(requestCode==1&&resultCode==0){
            String search=bundle.getString("searchkey");
            int currentedit=intent.getIntExtra("currentnum",-2);
//            Log.i("zjx","currentnum:"+currentedit);
            if(currentedit==-2){
                editText.setText(search);
                if(getResources().getString(R.string.mylocation).equals(search)){
                    editText.setTextColor(getResources().getColor(R.color.novoda_blue));
                }
            }
            else if(currentedit==-1){
                editText2.setText(search);
                if(getResources().getString(R.string.mylocation).equals(search)){
                    editText2.setTextColor(getResources().getColor(R.color.novoda_blue));
                }
            }
            else {
//                intent.putExtra("currentsearch", adapter.getItem(position));
//                intent.putExtra("currentnum",position);
//                startActivityForResult(intent, 1);
//                editText2.setText(search);
//                if(getResources().getString(R.string.mylocation).equals(search)){
//                    editText2.setTextColor(getResources().getColor(R.color.novoda_blue));
//                }
                adapter.remove(currentedit);
//                adapter.add(currentedit, search);
                listView.insert(currentedit, search);

            }
        }
    }
    private static class MyListAdapter extends ArrayAdapter<String> implements UndoAdapter {

        private final Context mContext;

        MyListAdapter(final Context context) {
            mContext = context;
//            for (int i = 0; i < 20; i++) {
//                add(mContext.getString(R.string.row_number, i));
//            }
        }

        @Override
        public long getItemId(final int position) {
            return getItem(position).hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.list_row_dynamiclistview, parent, false);
            }

            ((TextView) view.findViewById(R.id.list_row_draganddrop_textview)).setText(getItem(position));

            return view;
        }

        @Override
        public View getUndoView(final int position, final View convertView,  final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.undo_row, parent, false);
            }
            return view;
        }

        @Override
        public View getUndoClickView( final View view) {
            return view.findViewById(R.id.undo_row_undobutton);
        }
    }

    private static class MyOnItemLongClickListener implements AdapterView.OnItemLongClickListener {

        private final DynamicListView mListView;

        MyOnItemLongClickListener(final DynamicListView listView) {
            mListView = listView;
        }

        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            if (mListView != null) {
                mListView.startDragging(position - mListView.getHeaderViewsCount());
            }
            return true;
        }
    }

    private class MyOnDismissCallback implements OnDismissCallback {

        private final ArrayAdapter<String> mAdapter;

        private Toast mToast;

        MyOnDismissCallback(final ArrayAdapter<String> adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onDismiss( final ViewGroup listView,  final int[] reverseSortedPositions) {
            for (int position : reverseSortedPositions) {
                mAdapter.remove(position);
            }

//            if (mToast != null) {
//                mToast.cancel();
//            }
//            mToast = Toast.makeText(
//                    MakeRoadActivity.this,
//                    getString(R.string.removed_positions, Arrays.toString(reverseSortedPositions)),
//                    Toast.LENGTH_LONG
//            );
//            mToast.show();
        }
    }

    private class MyOnItemMovedListener implements OnItemMovedListener {

        private final ArrayAdapter<String> mAdapter;

        private Toast mToast;

        MyOnItemMovedListener(final ArrayAdapter<String> adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onItemMoved(final int originalPosition, final int newPosition) {
//            if (mToast != null) {
//                mToast.cancel();
//            }
//
//            mToast = Toast.makeText(getApplicationContext(), getString(R.string.moved, mAdapter.getItem(newPosition), newPosition), Toast.LENGTH_SHORT);
//            mToast.show();
        }
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        private final DynamicListView mListView;

        MyOnItemClickListener(final DynamicListView listView) {
            mListView = listView;
        }

        @Override
        public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
//            mListView.insert(position+1, getString(R.string.newly_added_item, mNewItemCount));
//            mNewItemCount++;
            Intent intent = new Intent(MakeRoadActivity.this, SearchActivity.class);
            intent.putExtra("currentsearch", adapter.getItem(position));
            intent.putExtra("currentnum",position);
            startActivityForResult(intent, 1);
        }
    }
}
