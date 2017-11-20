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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fka.rememberwords.dialogs.DeleteWordFragment;
import com.fka.rememberwords.dialogs.NewWordFragment;
import com.fka.rememberwords.data.WordLab;
import com.fka.rememberwords.objects.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//фрагмент со списком слов и кнопкой добовления нового слова

public class WordsListFragment extends Fragment {
    private static final String DIALOG_NEW_WORD = "DialogNewWord";
    private static final String DIALOG_DELETE_WORD_POPUP = "DialogDeleteWordPopup";
    private static final String DIALOG_EDIT_WORD_POPUP = "DialogEditWordPopup";

    private static final String ARG_ID_DICTIONARY = "id_dictionary";

    private static final int REQUEST_WORD = 0;
    private static final int REQUEST_WORD_DELETE = 1;
//    private static final int REQUEST_RENAME = 2;

    private RecyclerView wordsRecyclerView;
    private FloatingActionButton addWordFAB;
    private WordAdapter adapter;
    private UUID idDictionary;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static WordsListFragment newInstance(UUID id) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_ID_DICTIONARY, id);

        WordsListFragment fragment = new WordsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fargment_words, container, false);
        final UUID id = (UUID) getArguments().getSerializable(ARG_ID_DICTIONARY);
        idDictionary = id;

        addWordFAB = (FloatingActionButton) v.findViewById(R.id.addWordFAB);
        addWordFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                NewWordFragment dialog = new NewWordFragment();
                dialog.setTargetFragment(WordsListFragment.this, REQUEST_WORD);
                dialog.show(manager, DIALOG_NEW_WORD);

//                Word word = new Word(id, "Test slovo4", "perevod1");
//                List<Word> words = WordLab.getWordLab(getActivity()).getWords();
//
//                for (int i = 0; i < words.size(); i++) {
//                    String wordString = words.get(i).getWord();
//                    if (wordString.equals(word.getWord())){
//                        Toast.makeText(getActivity(), "Слово уже существует", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//                WordLab.getWordLab(getActivity()).addWord(word);
//            updateUI();
            }
        });

        wordsRecyclerView = (RecyclerView) v.findViewById(R.id.wordsRecyclerView);
        wordsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        wordsRecyclerView.addItemDecoration(new ItemDivider(getActivity()));

        updateUI();

        return v;
    }

    //возвращается результат от диалогов
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){ //если результат не OK, нечиго не происходит
            return;
        }

        if (requestCode == REQUEST_WORD) { //результат от NewWordFragment, возвращается наименование нового словаря
            //создается новое слово
            String title = data.getStringExtra(NewWordFragment.EXTRA_TITLE);
            String translation = data.getStringExtra(NewWordFragment.EXTRA_TRANSLATION);
            Word newWord = new Word(idDictionary, title, translation);
            WordLab.getWordLab(getActivity()).addWord(newWord);
            updateUI();
        }

        if (requestCode == REQUEST_WORD_DELETE) {
            //удалить выбранное слово
            String wordString = (String) data.getSerializableExtra(DeleteWordFragment.EXTRA_DEL_WORD);
            Word word = WordLab.getWordLab(getActivity()).getWord(wordString);
            WordLab.getWordLab(getActivity()).deleteWord(word);
            updateUI();
        }
//
//        if (requestCode == REQUEST_RENAME) {
//            //переиминовать выбранный словарь
//            String title = (String) data.getSerializableExtra(RenameDictionaryFragment.EXTRA_TITLE);
//            UUID id = (UUID) data.getSerializableExtra(RenameDictionaryFragment.EXTRA_RENAME_UUID);
//            Dictionary dictionary = DictionaryLab.getDictionaryLab(getActivity()).getDictionary(id);
//            dictionary.setTitle(title);
//            DictionaryLab.getDictionaryLab(getActivity()).updateDictionary(dictionary);
//            updateUI();
//        }
    }

    //обновление списка словарей
    private void updateUI () {
        WordLab wordLab = WordLab.getWordLab(getActivity());
        List<Word> words = wordLab.getWords();
        List<Word> curWords = new ArrayList<>();

        for (Word word : words){
            if (word.getId().toString().equals(idDictionary.toString())){
                curWords.add(word);
            }
        }

        adapter = new WordAdapter(curWords);
        wordsRecyclerView.setAdapter(adapter);
    }

    //ViewHolder для RecyclerView
    private class WordHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView wordTextView;
        public TextView translationTextView;
        public ImageButton menuWordButton;
        public Word word;

        public WordHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);      //устоновка Listener для элемента списка

            wordTextView = (TextView) itemView.findViewById(R.id.wordTextView);
            translationTextView = (TextView) itemView.findViewById(R.id.translationTextView);

            menuWordButton = (ImageButton) itemView.findViewById(R.id.menuWord);
            menuWordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(menuWordButton, word);
                }
            });
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), "Клик на слове", Toast.LENGTH_SHORT).show();
        }
    }

    //Adapter для RecyclerView
    private class WordAdapter extends RecyclerView.Adapter<WordHolder>{
        private List<Word> words;

        public WordAdapter(List<Word> words) {
            this.words = words;
        }

        @Override
        public WordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.word_item, parent, false);
            return new WordHolder(view);
        }

        @Override
        public void onBindViewHolder(WordHolder holder, int position) {
            Word word = words.get(position);
            holder.wordTextView.setText(word.getWord());
            holder.translationTextView.setText(word.getTranslation());
            holder.word = word;
        }

        @Override
        public int getItemCount() {
            return words.size();
        }
    }

    //всплывающее меню для слова
 //   private void showPopupMenu (final View view, final UUID id) {
    private void showPopupMenu (final View view, final Word word) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.word_popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentManager manager = getFragmentManager();
                switch (item.getItemId()){
                    case R.id.change_word_popup_menu:     //диалог преименования словаря
//                        Dictionary dictionary = DictionaryLab.getDictionaryLab(getActivity()).getDictionary(id);
//                        String title = dictionary.getTitle();
//                        RenameDictionaryFragment dialogRename = RenameDictionaryFragment.newInstance(title, id);
//                        dialogRename.setTargetFragment(WordsListFragment.this, REQUEST_RENAME);
//                        dialogRename.show(manager, DIALOG_RENAME_POPUP);
                        return true;
                    case R.id.delete_word_popup_menu:     //диалог удаления словаря
                        DeleteWordFragment dialogWordDelete = DeleteWordFragment.newInstance(word.getWord());
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
