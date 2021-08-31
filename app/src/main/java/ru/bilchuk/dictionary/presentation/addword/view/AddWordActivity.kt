package ru.bilchuk.dictionary.presentation.addword.view;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import ru.bilchuk.dictionary.R;
import ru.bilchuk.dictionary.data.repositories.DictionaryRepository;
import ru.bilchuk.dictionary.databinding.ActivityAddWordBinding;
import ru.bilchuk.dictionary.domain.converter.DictionaryItemConverter;
import ru.bilchuk.dictionary.domain.interactors.DictionaryInteractor;
import ru.bilchuk.dictionary.domain.interactors.IDictionaryInteractor;
import ru.bilchuk.dictionary.domain.repositories.IDictionaryRepository;
import ru.bilchuk.dictionary.presentation.addword.viewmodel.AddWordViewModel;
import ru.bilchuk.dictionary.utils.ISchedulersProvider;
import ru.bilchuk.dictionary.utils.SchedulersProvider;

public class AddWordActivity extends AppCompatActivity {

    private ActivityAddWordBinding binding;
    private AddWordViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddWordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initUI();

        createViewModel();
        observeLiveData();

        binding.buttonAdd.setOnClickListener(v -> viewModel.addWord(binding.keywordEditText.getText().toString(),
                binding.translationEditText.getText().toString()));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUI() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void createViewModel() {
        IDictionaryRepository dictionaryRepository = new DictionaryRepository(getApplicationContext());
        IDictionaryInteractor dictionaryInteractor = new DictionaryInteractor(dictionaryRepository, new DictionaryItemConverter());
        ISchedulersProvider schedulersProvider = new SchedulersProvider();

        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new AddWordViewModel(dictionaryInteractor, schedulersProvider);
            }
        }).get(AddWordViewModel.class);
    }

    private void observeLiveData() {
        viewModel.getTextAddedLiveData().observe(this, this::processLiveData);
    }

    private void processLiveData(boolean result) {
        if (result) {
            finish();
        } else {
            Snackbar.make(binding.getRoot(), getString(R.string.input_error), Snackbar.LENGTH_SHORT).show();
        }
    }
}
