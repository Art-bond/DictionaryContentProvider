package ru.bilchuk.dictionary.presentation.wordslist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import ru.bilchuk.dictionary.domain.interactors.IDictionaryInteractor
import ru.bilchuk.dictionary.domain.models.DictionaryItem
import ru.bilchuk.dictionary.utils.ISchedulersProvider

class DictionaryViewModel(
    private val dictionaryInteractor: IDictionaryInteractor,
    private val schedulersProvider: ISchedulersProvider
) : ViewModel() {
    val dictionaryItemsLiveData = MutableLiveData<List<DictionaryItem>>()
    val progressLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<Throwable>()
    private var disposable: Disposable? = null
    fun loadDataAsyncRx() {
        disposable = dictionaryInteractor.words
            .doOnSubscribe { disposable: Disposable? -> progressLiveData.postValue(true) }
            .doAfterTerminate { progressLiveData.postValue(false) }
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.ui())
            .subscribe({ value: List<DictionaryItem> -> dictionaryItemsLiveData.setValue(value) }) { value: Throwable ->
                errorLiveData.setValue(
                    value
                )
            }
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null && !disposable!!.isDisposed) {
            disposable!!.dispose()
            disposable = null
        }
    }
}