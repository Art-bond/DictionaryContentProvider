package ru.bilchuk.dictionary.domain.converter;

import java.util.ArrayList;
import java.util.List;

import ru.bilchuk.dictionary.domain.models.DictionaryItem;
import ru.bilchuk.dictionary.domain.repositories.models.DictionaryItemModel;

public class DictionaryItemConverter implements IDictionaryItemConverter {

    @Override
    public DictionaryItemModel convert(DictionaryItem dictionaryItem) {
        DictionaryItemModel dictionaryItemModel = new DictionaryItemModel();
        dictionaryItemModel.setKeyword(dictionaryItem.getKeyword());
        dictionaryItemModel.setTranslation(dictionaryItem.getTranslation());

        return dictionaryItemModel;
    }

    @Override
    public DictionaryItem reverse(DictionaryItemModel dictionaryItemModel) {
        DictionaryItem dictionaryItem = new DictionaryItem();
        dictionaryItem.setKeyword(dictionaryItemModel.getKeyword());
        dictionaryItem.setTranslation(dictionaryItemModel.getTranslation());

        return dictionaryItem;
    }

    @Override
    public List<DictionaryItem> reverseList(List<DictionaryItemModel> dictionaryItemModel) {
        List<DictionaryItem> reverseList = new ArrayList<>();
        for (DictionaryItemModel model : dictionaryItemModel) {
            reverseList.add(reverse(model));
        }
        return reverseList;
    }
}
