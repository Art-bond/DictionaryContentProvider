package ru.bilchuk.dictionary.domain.repositories.models

import ru.bilchuk.dictionary.R

class DictionaryItemModel {
    var id: Long = 0
    var keyword: String = ""
    var translation: String = ""
    var imageUri: String = "R.drawable.ic_baseline_photo_library_100"
}