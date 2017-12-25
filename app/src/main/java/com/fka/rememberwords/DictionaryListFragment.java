package com.fka.rememberwords;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fka.rememberwords.data.realm.DictionaryRealm;
import com.fka.rememberwords.data.realm.RealmController;
import com.fka.rememberwords.dialogs.DeleteDictionaryFragment;
import com.fka.rememberwords.dialogs.EditDictionaryFragment;
import com.fka.rememberwords.dialogs.NewDictionaryFragment;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

//фрагмент со списком словарей и кнопкой добовления нового словоря

public class DictionaryListFragment extends Fragment {
    private static final String DIALOG_NEW_DICTIONARY = "DialogNewDictionary";
    private static final String DIALOG_DELETE_POPUP = "DialogDeletePopup";
    private static final String DIALOG_RENAME_POPUP = "DialogRenamePopup";

    private static final int REQUEST_TITLE = 0;
    private static final int REQUEST_DELETE = 1;
    private static final int REQUEST_RENAME = 2;

    private RecyclerView dictionariesRecyclerView;
    private FloatingActionButton addFAB;
    private DictionaryRecyclerAdapter adapter;

    private Button rememberBtn;
    private Button repeatButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fargment_dictionaries, container, false);
        View v = inflater.inflate(R.layout.fragment_main_test, container, false);

        addFAB = (FloatingActionButton) v.findViewById(R.id.addFAB);
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                NewDictionaryFragment dialog = new NewDictionaryFragment();
                dialog.setTargetFragment(DictionaryListFragment.this, REQUEST_TITLE);
                dialog.show(manager, DIALOG_NEW_DICTIONARY);
            }
        });

        dictionariesRecyclerView = (RecyclerView) v.findViewById(R.id.dictionariesRecyclerView);
        dictionariesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        dictionariesRecyclerView.addItemDecoration(new ItemDivider(getActivity()));

        rememberBtn = (Button) v.findViewById(R.id.start_remember_fragment_button);
        rememberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.fragment_container, new RememberFragment())
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });

        repeatButton = (Button) v.findViewById(R.id.start_repeat_fragment_button);
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.fragment_container, new RepeatFragment())
                        .addToBackStack("dictionary_fragment_stack")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });

        updateUI();

        return v;
    }

    //возвращается результат от диалогов
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){ //если результат не OK, нечиго не происходит
            return;
        }

        if (requestCode == REQUEST_TITLE) { //результат от NewDictionaryFragment
            updateUI();
        }

        if (requestCode == REQUEST_DELETE) {  //результат от DeleteDictionaryFragment
            updateUI();
        }

        if (requestCode == REQUEST_RENAME) { //результат от RenameDictionaryFragment
            updateUI();
        }
    }

    //обновление списка словарей
    private void updateUI () {
        adapter = new DictionaryRecyclerAdapter(new RealmController().getDictionariesInfo());
        dictionariesRecyclerView.setAdapter(adapter);

    }

    //ViewHolder для RecyclerView
    private class DictionaryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        public TextView titleDictionaryTextView;
        public TextView countWordsDictionaryTextView;
        public TextView repeatWordsDictionaryTextView;
        public ImageButton menuDictionaryButton;
        public int id;

        public DictionaryHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);      //устоновка Listener для элемента списка

            titleDictionaryTextView = (TextView) itemView.findViewById(R.id.titleDictionary);
            countWordsDictionaryTextView = (TextView) itemView.findViewById(R.id.countWordsDictionary);
            repeatWordsDictionaryTextView = (TextView) itemView.findViewById(R.id.repeatDictionary);

            menuDictionaryButton = (ImageButton) itemView.findViewById(R.id.menuDictionary);
            menuDictionaryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(menuDictionaryButton, id);
                }
            });
        }

        @Override
        public void onClick(View view) {
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.fragment_container, WordsListFragment.newInstance(id))
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

        }
    }

    //Адаптер для RealmRecyclerView
    private class DictionaryRecyclerAdapter extends RealmRecyclerViewAdapter<DictionaryRealm, DictionaryHolder>{


        public DictionaryRecyclerAdapter(@Nullable OrderedRealmCollection<DictionaryRealm> data) {
            super(data, true);
        }

        @Override
        public DictionaryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.dictionary_item, parent, false);
            return new DictionaryHolder(view);
        }

        @Override
        public void onBindViewHolder(DictionaryHolder holder, int position) {
            final DictionaryRealm dictionary = getItem(position);
            holder.titleDictionaryTextView.setText(dictionary.getDictionaryTitle());
            holder.id = dictionary.getId();
        }
    }


    //всплывающее меню для словаря
    private void showPopupMenu (final View view, final int id) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.didctionary_popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FragmentManager manager = getFragmentManager();
                switch (item.getItemId()){
                    case R.id.rename_dictionary_popup_menu:     //диалог преименования словаря
                        DictionaryRealm dictionary = new RealmController().getDictionaryById(id);
                        String title = dictionary.getDictionaryTitle();
                        EditDictionaryFragment dialogRename = EditDictionaryFragment.newInstance(title, id);
                        dialogRename.setTargetFragment(DictionaryListFragment.this, REQUEST_RENAME);
                        dialogRename.show(manager, DIALOG_RENAME_POPUP);
                        return true;
                    case R.id.delete_dictionary_popup_menu:     //диалог удаления словаря
                        DeleteDictionaryFragment dialogDelete = DeleteDictionaryFragment.newInstance(id);
                        dialogDelete.setTargetFragment(DictionaryListFragment.this, REQUEST_DELETE);
                        dialogDelete.show(manager, DIALOG_DELETE_POPUP);
                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        new RealmController().closeRealm();
    }
}
