package com.fka.rememberwords;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fka.rememberwords.data.realm.RealmController;
import com.fka.rememberwords.data.realm.WordRealm;

import java.util.ArrayList;
import java.util.List;

import in.arjsna.swipecardlib.SwipeCardView;

/**
 * Created by 074FrantsuzovKA on 30.11.2017.
 */

public class RememberFragment extends Fragment {
    private CardAdapter adapter;
    private SwipeCardView swipeCardView;
    private List<WordRealm> wordsForRemember;
    private boolean isLastCard = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remember, container, false);

        swipeCardView = (SwipeCardView) view.findViewById(R.id.swipeCardView);
        wordsForRemember = new RealmController().getWordsForRemember();

        adapter = new CardAdapter(getActivity(), (ArrayList<WordRealm>) wordsForRemember);
        swipeCardView.setAdapter(adapter);
        swipeCardView.setFlingListener(new SwipeCardView.OnCardFlingListener() {
            @Override
            public void onCardExitLeft(Object dataObject) {
                isLastCard = false;
            }

            @Override
            public void onCardExitRight(Object dataObject) {

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                isLastCard = true;
                Toast.makeText(getActivity(), "Скоро конец", Toast.LENGTH_SHORT).show();
                wordsForRemember.addAll(new RealmController().getWordsForRemember());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }

            @Override
            public void onCardExitTop(Object dataObject) {
                isLastCard = false;
            }

            @Override
            public void onCardExitBottom(Object dataObject) {
                isLastCard = false;
            }
        });

        swipeCardView.setOnItemClickListener(new SwipeCardView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(getActivity(), "Click on Crad", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private class CardAdapter extends ArrayAdapter<WordRealm> {
        private final ArrayList<WordRealm> words;
        private final LayoutInflater layoutInflater;
        private Button setChecked;

        public CardAdapter(@NonNull Context context, ArrayList<WordRealm> words) {
            super(context, -1);
            this.words = words;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            final WordRealm word = words.get(position);
            View view;

            if (convertView == null){
                view = layoutInflater.inflate(R.layout.remember_card, parent, false);
            } else {
                view = convertView;
            }

            ((TextView) view.findViewById(R.id.card_word_title)).setText(word.getWordTitle());
            ((TextView) view.findViewById(R.id.card_word_translation)).setText(word.getTranslation());
            setChecked = (Button) view.findViewById(R.id.buttonRemember);
            setChecked.setOnClickListener(null);

            setChecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new RealmController().setCheckedForWord(word, false);
                    if (isLastCard){
                        wordsForRemember.remove(wordsForRemember.size() - 1);
                    }
                    adapter.notifyDataSetChanged();
                    swipeCardView.throwLeft();
                }
            });


            return view;
        }

        @Nullable
        @Override
        public WordRealm getItem(int position) {
            return words.get(position);
        }

        @Override
        public int getCount() {
            return words.size();
        }
    }
}

