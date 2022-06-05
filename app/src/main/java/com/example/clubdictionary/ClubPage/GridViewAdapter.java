package com.example.clubdictionary.ClubPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.clubdictionary.R;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    ArrayList<PostItem> items = new ArrayList<PostItem>();

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(PostItem item){
        items.add(item);
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final Context context = viewGroup.getContext();
        final PostItem postItem = items.get(i);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.square_image_with_menu, viewGroup, false);

            ImageView img = view.findViewById(R.id.square_image_icon_with_menu);
            //ImageButton ib = view.findViewById(R.id.item_dropdown_menu);

            ImageView threeDotBtn = view.findViewById(R.id.item_dropdown_menu);

            img.setImageResource(postItem.getResId());

            threeDotBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(context, threeDotBtn);
                    MenuInflater menuInflater = popup.getMenuInflater();
                    menuInflater.inflate(R.menu.clubpage_post_menu_forclub, popup.getMenu());
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            switch (menuItem.getItemId()) {
                                case R.id.fixIt:
                                    Toast.makeText(context, "수정하기 클릭", Toast.LENGTH_SHORT).show();
                                    return true;
                                case R.id.deleteIt:
                                    Toast.makeText(context, "삭제하기 클릭", Toast.LENGTH_SHORT).show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                }
            });


/*            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popup = new PopupMenu(context, viewGroup);

                    MenuInflater menuInflater = popup.getMenuInflater();
                }
            });*/

        }
        else{
            View change_view = new View(context);
            change_view = (View) view;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                Toast.makeText(context, i + "번째 post", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }
}
