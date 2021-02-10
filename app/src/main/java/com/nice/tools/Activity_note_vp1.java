package com.nice.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

public class Activity_note_vp1 extends Fragment {
    private List<Fruit> fruitList = new ArrayList<>();
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(inflater.getContext(), R.layout.activity_note_viewpage, null);
        intdata();
        return view;
    }

    private void intdata() {

        OkGo.<String>get("http://www.tbook.top/iso/ntools/note/gg.html")
                .tag(this)
                .cacheKey("cacheKey")
                .cacheMode(CacheMode.NO_CACHE)
                .cacheTime(2000)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        String content = response.body();
                        String[] sl = mode.Sl(content, "￥");
                        for ( String str : sl ) {
                            String title = str.substring(str.indexOf("<Title>") + "<Title>".length(), str.lastIndexOf("</Title>"));
                            String sut1 = str.substring(str.indexOf("<show>") + "<show>".length(), str.lastIndexOf("</show>"));
                            String time = str.substring(str.indexOf("<time>") + "<time>".length(), str.lastIndexOf("</time>"));
                            String show = mode.Sr(sut1, "</br>", "\n");
                            Fruit data = new Fruit(title, time, show);
                            fruitList.add(data);

                        }
                        RecyclerView recyclerView = view.findViewById(R.id.note_viewpager_rec);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        FruitAdapter adapter = new FruitAdapter(fruitList);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private class Fruit {
        private final String title;
        private final String show;
        private final String time;

        public Fruit(String title, String time, String show) {
            this.title = title;
            this.time = time;
            this.show = show;
        }

        public String getTitle() {
            return title;
        }

        public String getShow() {
            return show;
        }

        public String getTime() {
            return time;
        }
    }


    public static class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

        private List<Fruit> mFruitList;

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView item_note_card_title;
            TextView item_note_card_subtitle;
            TextView item_note_card_show;
            CardView item_note_card;

            public ViewHolder(View view) {
                super(view);
                item_note_card_title = view.findViewById(R.id.item_note_card_title);
                item_note_card_subtitle = view.findViewById(R.id.item_note_card_subtitle);
                item_note_card_show = view.findViewById(R.id.item_note_card_show);
                item_note_card = view.findViewById(R.id.item_note_card);
            }

        }

        public FruitAdapter(List<Fruit> fruitList) {
            mFruitList = fruitList;
        }

        @Override

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            final Fruit fruit = mFruitList.get(position);
            holder.item_note_card_title.setText(fruit.getTitle());
            holder.item_note_card_show.setText(fruit.getShow());
            holder.item_note_card_subtitle.setText(fruit.getTime());
            holder.item_note_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    View bottom_dialog_massage_view = View.inflate( holder.item_note_card.getContext(), R.layout.bottom_dialog_massage, null);
                    final BottomSheetDialog bottom_dialog_massage = new BottomSheetDialog(holder.item_note_card.getContext(), R.style.BottomSheetDialog);
                    bottom_dialog_massage.setContentView(bottom_dialog_massage_view);
                    //底部弹窗展开
                    BottomSheetBehavior mDialogBehavior = BottomSheetBehavior.from((View) bottom_dialog_massage_view.getParent());
                    mDialogBehavior.setPeekHeight(mode.getResolution(holder.item_note_card.getContext(), "h"));

                    bottom_dialog_massage.show();
                    //标题
                    final ImageView bottom_dialog_massage_icon = bottom_dialog_massage_view.findViewById(R.id.bottom_dialog_massage_icon);
                    final TextView bottom_dialog_massage_title = bottom_dialog_massage_view.findViewById(R.id.bottom_dialog_massage_title);
                    final TextView bottom_dialog_massage_show = bottom_dialog_massage_view.findViewById(R.id.bottom_dialog_massage_show);

                    final MaterialButton bottom_dialog_massage_button = bottom_dialog_massage_view.findViewById(R.id.bottom_dialog_massage_button);

                    //style
                    bottom_dialog_massage_title.setText(fruit.getTitle());
                    bottom_dialog_massage_show.setText(fruit.getShow());
                    bottom_dialog_massage_button.setText("关闭");

                    //关闭弹窗
                    bottom_dialog_massage_view.findViewById(R.id.bottom_dialog_massage_close_icon).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottom_dialog_massage.dismiss();
                        }
                    });
                    bottom_dialog_massage_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            bottom_dialog_massage.dismiss();
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return mFruitList.size();
        }
    }

}
