package ru.bilchuk.dictionary.presentation.addword.viewmodel;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.Disposable;
import ru.bilchuk.dictionary.domain.interactors.IDictionaryInteractor;
import ru.bilchuk.dictionary.domain.models.DictionaryItem;
import ru.bilchuk.dictionary.utils.ISchedulersProvider;

public class AddWordViewModel extends ViewModel {

    private final IDictionaryInteractor dictionaryInteractor;
    private final ISchedulersProvider schedulersProvider;

    private final MutableLiveData<Boolean> textAddedLiveData = new MutableLiveData<>();

    private Disposable disposable;

    public AddWordViewModel(@NonNull IDictionaryInteractor dictionaryInteractor,
                            @NonNull ISchedulersProvider schedulersProvider) {
        this.dictionaryInteractor = dictionaryInteractor;
        this.schedulersProvider = schedulersProvider;
    }

    public void addWord(String keyword, String translation) {
        if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(translation)) {
            textAddedLiveData.setValue(false);
        } else {
            DictionaryItem item = new DictionaryItem();
            item.setKeyword(keyword);
            item.setTranslation(translation);
            disposable = dictionaryInteractor.add(item)
                    .subscribeOn(schedulersProvider.io())
                    .observeOn(schedulersProvider.ui())
                    .subscribe(() -> textAddedLiveData.setValue(true), t ->textAddedLiveData.setValue(false));
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

    public MutableLiveData<Boolean> getTextAddedLiveData() {
        return textAddedLiveData;
    }
}
