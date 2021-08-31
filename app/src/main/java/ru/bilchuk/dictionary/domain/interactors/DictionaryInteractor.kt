package ru.bilchuk.dictionary.domain.interactors

import ru.bilchuk.dictionary.domain.repositories.IDictionaryRepository
import ru.bilchuk.dictionary.domain.converter.IDictionaryItemConverter
import ru.bilchuk.dictionary.domain.interactors.IDictionaryInteractor
import ru.bilchuk.dictionary.domain.models.DictionaryItem
import io.reactivex.Completable
import io.reactivex.Single
import ru.bilchuk.dictionary.domain.repositories.models.DictionaryItemModel

class DictionaryInteractor(
    private val repository: IDictionaryRepository,
    private val converter: IDictionaryItemConverter
) : IDictionaryInteractor {
    override fun add(dictionaryItem: DictionaryItem): Completable {
        return repository.add(converter.convert(dictionaryItem))
    }

    override fun getWords(): Single<List<DictionaryItem>> {
        return repository.words
            .map { dictionaryItemModel: List<DictionaryItemModel?>? ->
                converter.reverseList(
                    dictionaryItemModel
                )
            }
    }

    override fun delete(id: Long): Completable {
        return repository.delete(id)
    }
}