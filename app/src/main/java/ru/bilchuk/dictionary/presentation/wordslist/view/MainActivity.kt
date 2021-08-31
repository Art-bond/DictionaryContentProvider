package ru.bilchuk.dictionary.presentation.wordslist.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ru.bilchuk.dictionary.R;
import ru.bilchuk.dictionary.data.repositories.DictionaryRepository;
import ru.bilchuk.dictionary.databinding.ActivityMainBinding;
import ru.bilchuk.dictionary.domain.converter.DictionaryItemConverter;
import ru.bilchuk.dictionary.domain.interactors.DictionaryInteractor;
import ru.bilchuk.dictionary.domain.interactors.IDictionaryInteractor;
import ru.bilchuk.dictionary.domain.models.DictionaryItem;
import ru.bilchuk.dictionary.domain.repositories.IDictionaryRepository;
import ru.bilchuk.dictionary.presentation.addword.view.AddWordActivity;
import ru.bilchuk.dictionary.presentation.wordslist.view.adapter.DictionaryAdapter;
import ru.bilchuk.dictionary.presentation.wordslist.viewmodel.DictionaryViewModel;
import ru.bilchuk.dictionary.utils.ISchedulersProvider;
import ru.bilchuk.dictionary.utils.SchedulersProvider;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DictionaryViewModel viewModel;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fab.setOnClickListener(v -> startAddWordActivity());

        initUI();
        createViewModel();
        observeLiveData();
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewModel.loadDataAsyncRx();
    }

    private void initUI() {
        DividerItemDecoration divider =
                new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.divider);
        if (drawable != null) {
            divider.setDrawable(drawable);
        }

        binding.recyclerView.addItemDecoration(divider);
    }

    private void createViewModel() {
        IDictionaryRepository dictionaryRepository = new DictionaryRepository(getApplicationContext());
        IDictionaryInteractor dictionaryInteractor = new DictionaryInteractor(dictionaryRepository, new DictionaryItemConverter());
        ISchedulersProvider schedulersProvider = new SchedulersProvider();

        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new DictionaryViewModel(dictionaryInteractor, schedulersProvider);
            }
        }).get(DictionaryViewModel.class);
    }

    private void observeLiveData() {
        viewModel.getProgressLiveData().observe(this, this::showProgress);
        viewModel.getDictionaryItemsLiveData().observe(this, this::showWords);
        viewModel.getErrorLiveData().observe(this, this::showError);
    }

    private void showProgress(boolean isVisible) {
        Log.i(TAG, "showProgress called with param = " + isVisible);

        binding.progressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void showWords(@NonNull List<DictionaryItem> dictionaryItems) {
        Log.d(TAG, "showData called dictionaryItems size = " + dictionaryItems.size());

        DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(dictionaryItems);

        binding.recyclerView.setAdapter(dictionaryAdapter);
    }

    private void showError(@NonNull Throwable throwable) {
        Log.e(TAG, "showError called with error = " + throwable.toString());

        Snackbar.make(binding.getRoot(), throwable.toString(), Snackbar.LENGTH_LONG).show();
    }

    private void startAddWordActivity() {
        startActivity(new Intent(this, AddWordActivity.class));
    }
}