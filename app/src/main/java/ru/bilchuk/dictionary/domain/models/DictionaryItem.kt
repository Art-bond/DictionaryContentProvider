package ru.bilchuk.dictionary.domain.models


data class DictionaryItem (
    var keyword: String = "",
    var translation: String = "",
    var imageUri: String = "R.drawable.ic_baseline_photo_library_100"
)