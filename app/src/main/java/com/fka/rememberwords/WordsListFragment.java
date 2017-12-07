package com.fka.rememberwords;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fka.rememberwords.data.realm.DictionaryRealm;
import com.fka.rememberwords.data.realm.RealmController;
import com.fka.rememberwords.data.realm.WordRealm;
import com.fka.rememberwords.dialogs.EditWordFragment;
import com.fka.rememberwords.dialogs.DeleteWordFragment;
import com.fka.rememberwords.dialogs.NewWordFragment;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

//фрагмент со списком слов и кнопкой добовления нового слова

public class WordsListFragment extends Fragment {
    private static final String DIALOG_NEW_WORD = "DialogNewWord";
    private static final String DIALOG_DELETE_WORD_POPUP = "DialogDeleteWordPopup";
    private static final String DIALOG_EDIT_WORD_POPUP = "DialogEditWordPopup";

    private static final String ARG_ID_DICTIONARY = "id_dictionary";

    private static final int REQUEST_WORD = 0;
    private static final int REQUEST_WORD_DELETE = 1;
    private static final int REQUEST_EDIT_WORD = 2;

    private RecyclerView wordsRecyclerView;
    private FloatingActionButton addWordFAB;
    private WordRecyclerAdapter adapter;
    private int idDictionary;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static WordsListFragment newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt(ARG_ID_DICTIONARY, id);

        WordsListFragment fragment = new WordsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fargment_words, container, false);
        final int id = getArguments().getInt(ARG_ID_DICTIONARY);
        idDictionary = id;

        addWordFAB = (FloatingActionButton) v.findViewById(R.id.addWordFAB);
        addWordFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                NewWordFragment dialog = NewWordFragment.newInstance(idDictionary);
                dialog.setTargetFragment(WordsListFragment.this, REQUEST_WORD);
                dialog.show(manager, DIALOG_NEW_WORD);
            }
        });

        wordsRecyclerView = (RecyclerView) v.findViewById(R.id.wordsRecyclerView);
        wordsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        wordsRecyclerView.addItemDecoration(new ItemDivider(getActivity()));

        updateUI();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    //возвращается результат от диалогов
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){ //если результат не OK, нечиго не происходит
            return;
        }

        if (requestCode == REQUEST_WORD) { //результат от NewWordFragment
            updateUI();
        }

        if (requestCode == REQUEST_WORD_DELETE) {
            updateUI();
        }

        if (requestCode == REQUEST_EDIT_WORD) { //результат от EditWordFragment
            updateUI();
        }
    }

    //обновление списка словарей
    private void updateUI () {
            adapter = new WordRecyclerAdapter(new RealmController().getWordsInfoByParent(DictionaryRealm.class, idDictionary));
            wordsRecyclerView.setAdapter(adapter);
    }

    //ViewHolder для RecyclerView
    private class WordHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private int wordId;
        private TextView wordTextView;
        private TextView translationTextView;
        private ImageButton menuWordButton;
        private CheckBox wordCheckBox;

        public WordHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);      //устоновка Listener для элемента списка

            wordTextView = (TextView) itemView.findViewById(R.id.wordTextView);
            translationTextView = (TextView) itemView.findViewById(R.id.translationTextView);

            menuWordButton = (ImageButton) itemView.findViewById(R.id.menuWord);
            menuWordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   showPopupMenu(menuWordButton, wordId);
                }
            });

            wordCheckBox = (CheckBox) itemView.findViewById(R.id.wordCheckBox);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "Learn "+new RealmController().getWordById(wordId).isLearn()
                    + "/nRemember " + new RealmController().getWordById(wordId).isRemember(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class WordRecyclerAdapter extends RealmRecyclerViewAdapter<WordRealm, WordHolder>{

        public WordRecyclerAdapter(@Nullable OrderedRealmCollection<WordRealm> data) {
            super(data, true);
        }


        @Override
        public WordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.word_item, parent, false);
            return new WordHolder(view);
        }

        @Override
        public void onBindViewHolder(WordHolder holder, int position) {
            final WordRealm word = getItem(position);
            holder.wordId = word.getWordId();
            holder.wordTextView.setText(word.getWordTitle());
            holder.translationTextView.setText(word.getTranslation());
            holder.wordCheckBox.setOnCheckedChangeListener(null);
            holder.wordCheckBox.setChecked(word.isRemember());
            holder.wordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //new RealmController().setLearnForWord(word, isChecked);
                    new RealmController().setRememberForWord(word, isChecked);
                }
            });
        }
    }

    //всплывающее меню для слова
    private void showPopupMenu (final View view, final int wordId) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.word_popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentManager manager = getFragmentManager();
                switch (item.getItemId()){
                    case R.id.change_word_popup_menu:     //диалог преименования слова
                        EditWordFragment dialogChangeWord = EditWordFragment.newInstance(wordId);
                        dialogChangeWord.setTargetFragment(WordsListFragment.this, REQUEST_EDIT_WORD);
                        dialogChangeWord.show(manager, DIALOG_EDIT_WORD_POPUP);
                        return true;
                    case R.id.delete_word_popup_menu:     //диалог удаления слова
                        DeleteWordFragment dialogWordDelete = DeleteWordFragment.newInstance(wordId);
                        dialogWordDelete.setTargetFragment(WordsListFragment.this, REQUEST_WORD_DELETE);
                        dialogWordDelete.show(manager, DIALOG_DELETE_WORD_POPUP);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();

    }
}
