package co.martinbrown.example.gesturedetector;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GestureDetectorExampleActivity extends Activity implements OnTouchListener {

    GestureDetector detector;
    int swipe = 0;
    MyAdapter myAdapter;

    class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
        }

        public MyAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View v = super.getView(position, convertView, parent);

            if(v != null) {
                v.setOnTouchListener(GestureDetectorExampleActivity.this);
            }

            return v;
        }

    };

    class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {

            Toast.makeText(getApplicationContext(), "You double tapped", Toast.LENGTH_SHORT).show();

            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {

            if (e1.getX() - e2.getX() > 50
                    && Math.abs(velocityX) > 50) {
                swipe = -1;

            }

            else if (e2.getX() - e1.getX() > 50
                    && Math.abs(velocityX) > 50) {
                swipe = 1;
            }

            return true;
        }
    }

    ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        lv = (ListView) findViewById(R.id.listView1);

        String[] sList = getResources().getStringArray(R.array.bucket_list);

        List<String> myList = new LinkedList<String>();
        for(int i = 0; i < sList.length; i++) {
            myList.add(sList[i]);
        }

        myAdapter = new MyAdapter(this, android.R.layout.simple_list_item_1, myList);

        lv.setAdapter(myAdapter);

        detector = new GestureDetector(this, new MyGestureDetector());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (detector.onTouchEvent(event))
            return true;
        else
            return false;
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {

        if(detector.onTouchEvent(event)) {

            if(swipe == 1) {

                Toast.makeText(this, "left-to-right", Toast.LENGTH_SHORT).show();


                myAdapter.remove(((TextView)v).getText().toString());

                myAdapter.notifyDataSetChanged();


            }
        }

        swipe = 0;

        return true;
    }
}