package com.example.clubdictionary.Category;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.MainActivity;
import com.example.clubdictionary.R;

import java.util.ArrayList;
import java.util.List;

public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    private List<Item> data;
    Context context;

    public ExpandableListAdapter(List<Item> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int) (18 * dp);
        int subItemPaddingTopAndBottom = (int) (5 * dp);
        switch (type) {
            case HEADER:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.major_category_item, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
                LayoutInflater inflater2 = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater2.inflate(R.layout.minor_category_item, parent, false);
                ListChildViewHolder childViewHolder = new ListChildViewHolder(view);
                return childViewHolder;
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Item item = data.get(position);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.refferalItem = item;
                //refferalItem 안에는 position번째 view 가 들어 있음
                itemController.header_title.setText(item.text);

                //일단 header_title은 얘로 설정

                //아이템의 자식이 없으면
                if (item.invisibleChildren == null) {
                    itemController.btn_expand_toggle.setImageResource(R.drawable.icon_parent_selected);
                } else {
                    // 자식이 있으면
                    itemController.btn_expand_toggle.setImageResource(R.drawable.icon_parent);
                }
                itemController.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.icon_parent);
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.icon_parent_selected);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;
            case CHILD:
                final ListChildViewHolder childItemController =(ListChildViewHolder) holder;
                childItemController.minorTitle.setText(item.text);

                childItemController.minor_item_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle result = new Bundle();
                        result.putString("minor", item.minor_category);
                        ClubListFragment clubListFragment = new ClubListFragment();
                        clubListFragment.setArguments(result);
                        MainActivity activity = (MainActivity) context;
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, clubListFragment).addToBackStack(null).commit();
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView header_title;
        public ImageView btn_expand_toggle;
        public Item refferalItem;
        public LinearLayout itemLayout;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.header_title);
            btn_expand_toggle = (ImageView) itemView.findViewById(R.id.btn_expand_toggle);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.major_item_layout);

        }
    }

    private static class ListChildViewHolder extends RecyclerView.ViewHolder {
        public TextView minorTitle;
        public LinearLayout minor_item_layout;

        public ListChildViewHolder(View itemView) {
            super(itemView);
            minorTitle = (TextView) itemView.findViewById(R.id.minorTitle);
            minor_item_layout = (LinearLayout) itemView.findViewById(R.id.minor_item_layout);
        }
    }


    public static class Item {
        public int type;
        public String text;
        public String minor_category;
        public List<Item> invisibleChildren;

        public Item(int type, String text, String minor_category) {
            this.type = type;
            this.text = text;
            this.minor_category = minor_category;
        }

        public Item() {
        }

        public Item(int type, String text) {
            this.type = type;
            this.text = text;
        }
    }
}
