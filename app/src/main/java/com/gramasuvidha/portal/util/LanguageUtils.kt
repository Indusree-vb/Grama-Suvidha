package com.gramasuvidha.portal.util

import com.gramasuvidha.portal.data.model.Language
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object LanguageUtils {
    private val _currentLanguage = MutableStateFlow(Language.ENGLISH)
    val currentLanguage: StateFlow<Language> = _currentLanguage

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == Language.ENGLISH) {
            Language.KANNADA
        } else {
            Language.ENGLISH
        }
    }

    fun setLanguage(language: Language) {
        _currentLanguage.value = language
    }
}
