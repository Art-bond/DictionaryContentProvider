package ru.bilchuk.dictionary.domain.converter

import ru.bilchuk.dictionary.domain.models.DictionaryItem
import ru.bilchuk.dictionary.domain.repositories.models.DictionaryItemModel
import java.util.*

class DictionaryItemConverter : IDictionaryItemConverter {
    override fun convert(dictionaryItem: DictionaryItem): DictionaryItemModel {
        val dictionaryItemModel = DictionaryItemModel()
        dictionaryItemModel.keyword = dictionaryItem.keyword
        dictionaryItemModel.translation = dictionaryItem.translation
        dictionaryItemModel.imageUri = dictionaryItem.imageUri
        return dictionaryItemModel
    }

    override fun reverse(dictionaryItemModel: DictionaryItemModel): DictionaryItem {
        val dictionaryItem = DictionaryItem()
        dictionaryItem.keyword = dictionaryItemModel.keyword
        dictionaryItem.translation = dictionaryItemModel.translation
        dictionaryItem.imageUri = dictionaryItemModel.imageUri
        return dictionaryItem
    }

    override fun reverseList(dictionaryItemModel: List<DictionaryItemModel>): List<DictionaryItem> {
        val reverseList: MutableList<DictionaryItem> = ArrayList()
        for (model in dictionaryItemModel) {
            reverseList.add(reverse(model))
        }
        return reverseList
    }
}