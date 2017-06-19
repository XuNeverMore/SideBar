package com.nevermore.recy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler_view);


        list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("---------"+i);
        }

        LinearLayoutManager m = new LinearLayoutManager(this);
        rv.setLayoutManager(m);
        rv.addItemDecoration(new ItemD());
        rv.setAdapter(new RAdapter());
        rv.setItemAnimator(new DefaultItemAnimator());
        float padd = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, new DisplayMetrics());
        Log.i(TAG, "ItemD: "+padd);

        SideBar sb = (SideBar) findViewById(R.id.side_bar);
        final TextView tv_show = (TextView) findViewById(R.id.tv_show);

        sb.setOnIndexTouch(new SideBar.OnIndexTouch() {
            @Override
            public void onIndex(int index, String charater) {
                if(tv_show.getVisibility()!=View.VISIBLE){
                    tv_show.setVisibility(View.VISIBLE);
                }
                tv_show.setText(charater);
            }

            @Override
            public void onCancel() {
                tv_show.setVisibility(View.GONE);

            }
        });

    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
             tv =  (TextView) itemView.findViewById(R.id.tv);

        }

    }

    class ItemD extends RecyclerView.ItemDecoration{

        private final Paint paint;
        private final float padd;

        public ItemD() {
            paint = new Paint();
            paint.setColor(Color.RED);
            padd = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, new DisplayMetrics());
//            Log.i(TAG, "ItemD: "+padd);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int childPosition = parent.getChildAdapterPosition(view);
            if(childPosition!=0){
                outRect.top=1;
            }

//            super.getItemOffsets(outRect, view, parent, state);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//            super.onDraw(c, parent, state);

            Rect rect = new Rect();
            rect.left = (int) (parent.getPaddingLeft()+padd);
            Log.i(TAG, "onDraw: "+rect.left);
            rect.right = (int) (parent.getWidth()-parent.getPaddingRight()-padd);
            for (int i = 1; i < parent.getChildCount(); i++) {

                int top = parent.getChildAt(i).getTop();
                rect.bottom = top;
                rect.top = top-1;
                c.drawRect(rect,paint);

            }

        }
    }

    private static final String TAG = "MainActivity";

    private class RAdapter extends RecyclerView.Adapter<ViewHolder>{


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);


            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.tv.setText(list.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, list.get(position), Toast.LENGTH_SHORT).show();
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    list.remove(position);
                    notifyDataSetChanged();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
