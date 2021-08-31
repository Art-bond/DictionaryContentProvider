package ru.bilchuk.dictionary.presentation.wordslist.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.disposables.Disposable;
import ru.bilchuk.dictionary.domain.interactors.IDictionaryInteractor;
import ru.bilchuk.dictionary.domain.models.DictionaryItem;
import ru.bilchuk.dictionary.utils.ISchedulersProvider;

public class DictionaryViewModel extends ViewModel {

    private final IDictionaryInteractor dictionaryInteractor;
    private final ISchedulersProvider schedulersProvider;

    private final MutableLiveData<List<DictionaryItem>> dictionaryItemsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> progressLiveData = new MutableLiveData<>();
    private final MutableLiveData<Throwable> errorLiveData = new MutableLiveData<>();

    private Disposable disposable;

    public DictionaryViewModel(@NonNull IDictionaryInteractor dictionaryInteractor,
                               @NonNull ISchedulersProvider schedulersProvider) {
        this.dictionaryInteractor = dictionaryInteractor;
        this.schedulersProvider = schedulersProvider;
    }

    public void loadDataAsyncRx() {
        disposable = dictionaryInteractor.getWords()
                .doOnSubscribe(disposable -> {
                    progressLiveData.postValue(true);
                })
                .doAfterTerminate(() -> progressLiveData.postValue(false))
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
                .subscribe(dictionaryItemsLiveData::setValue, errorLiveData::setValue);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
    }

    public MutableLiveData<List<DictionaryItem>> getDictionaryItemsLiveData() {
        return dictionaryItemsLiveData;
    }

    public MutableLiveData<Boolean> getProgressLiveData() {
        return progressLiveData;
    }

    public MutableLiveData<Throwable> getErrorLiveData() {
        return errorLiveData;
    }
}
