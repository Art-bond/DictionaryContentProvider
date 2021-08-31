package ru.bilchuk.dictionary.domain.interactors;

import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.bilchuk.dictionary.domain.converter.IDictionaryItemConverter;
import ru.bilchuk.dictionary.domain.models.DictionaryItem;
import ru.bilchuk.dictionary.domain.repositories.IDictionaryRepository;

public class DictionaryInteractor implements IDictionaryInteractor {

    private final IDictionaryRepository repository;
    private final IDictionaryItemConverter converter;

    public DictionaryInteractor(@NonNull IDictionaryRepository repository,
                                @NonNull IDictionaryItemConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public Completable add(@NonNull DictionaryItem dictionaryItem) {
        return repository.add(converter.convert(dictionaryItem));
    }

    @Override
    public Single<List<DictionaryItem>> getWords() {
        return repository.getWords()
                .map(converter::reverseList);
    }

    @Override
    public Completable delete(long id) {
        return repository.delete(id);
    }
}
