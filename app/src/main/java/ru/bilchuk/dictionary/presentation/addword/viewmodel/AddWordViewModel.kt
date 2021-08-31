package ru.bilchuk.dictionary.presentation.addword.viewmodel

import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import ru.bilchuk.dictionary.domain.interactors.IDictionaryInteractor
import ru.bilchuk.dictionary.domain.models.DictionaryItem
import ru.bilchuk.dictionary.utils.ISchedulersProvider

class AddWordViewModel(
    private val dictionaryInteractor: IDictionaryInteractor,
    private val schedulersProvider: ISchedulersProvider
) : ViewModel() {
    val textAddedLiveData = MutableLiveData<Boolean>()
    private var disposable: Disposable? = null

    fun addWord(keyword: String, translation: String, image: Uri) {
        if (TextUtils.isEmpty(keyword) || TextUtils.isEmpty(translation)) {
            textAddedLiveData.setValue(false)
        } else {
            val item = DictionaryItem()
            item.keyword = keyword
            item.translation = translation
            item.imageUri = image.toString()
            disposable = dictionaryInteractor.add(item)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
                .subscribe(
                    { textAddedLiveData.setValue(true) }
                )
                { t: Throwable? ->
                    textAddedLiveData.setValue(false)
                }
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