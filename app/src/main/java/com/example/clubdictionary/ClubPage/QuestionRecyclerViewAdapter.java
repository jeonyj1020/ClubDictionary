package com.example.clubdictionary.ClubPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.R;

import java.util.ArrayList;

public class QuestionRecyclerViewAdapter extends RecyclerView.Adapter<QuestionRecyclerViewAdapter.ViewHolder> {

    //ArrayList<QuestionItem> questionList = new ArrayList<>();
    ArrayList<sampleText> questionList = new ArrayList<>();
    Context mContext;

    public QuestionRecyclerViewAdapter(Context context, ArrayList<sampleText> questionList) {
        this.questionList = questionList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public QuestionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        QuestionRecyclerViewAdapter.ViewHolder viewHolder = new QuestionRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.answerText.setText("A. " + questionList.get(position).getAnswer());
        holder.questionText.setText("Q. " + questionList.get(position).getQuestion());

        //isClub 이부분을 조정해서 다루면 될듯
        /*

        int isWho 해서
        isWho == 0 -> 아무것도 아닌 사람
        isWho == 1 -> 작성자
        isWho == 2 -> 동아리

        하는 식으로 나누고

        1. 작성자인 경우 -> menu_forwriter inflate하면 됨
        2. 동아리인 경우 -> menu_forclub inflate하면 됨
        3. 아무것도 아닌 경우 -> setVisibility GONE으로 하면 됨

         */

        //이건 4,5번째꺼에 메뉴 띄워 보려고 만든거
        if (position == 3) holder.isClub = 1;
        else if (position == 4) holder.isClub = 2;
        else holder.isClub = 0;



        if (holder.isClub == 0) holder.menuButton.setVisibility(View.GONE);
        else {
            holder.menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(mContext, holder.menuButton);

                    MenuInflater menuInflater = popup.getMenuInflater();


                    if (holder.isClub == 1)
                        menuInflater.inflate(R.menu.clubpage_question_menu_forclub, popup.getMenu());
                    else
                        menuInflater.inflate(R.menu.clubpage_question_menu_forwriter, popup.getMenu());

                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.answerIt:
                                    Toast.makeText(mContext, "답변하기 클릭", Toast.LENGTH_SHORT).show();
                                    return true;
                                case R.id.deleteIt:
                                    Toast.makeText(mContext, "삭제하기 클릭", Toast.LENGTH_SHORT).show();
                                    return true;
                                case R.id.reWriteIt:
                                    Toast.makeText(mContext, "수정하기 클릭", Toast.LENGTH_SHORT).show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show();

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView questionText, answerText;
        ImageButton menuButton;
        int isClub;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            menuButton = itemView.findViewById(R.id.threeDotButton);
            questionText = itemView.findViewById(R.id.question);
            answerText = itemView.findViewById(R.id.answer);

        }
    }

}
