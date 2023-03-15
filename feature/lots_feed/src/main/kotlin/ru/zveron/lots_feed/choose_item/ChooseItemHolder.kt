package ru.zveron.lots_feed.choose_item

class ChooseItemHolder {
    private var _currentItemItemProvider: ChooseItemItemProvider? = null
    val currentItemItemProvider get() = _currentItemItemProvider!!

    fun setCurrentItemItemProvider(provider: ChooseItemItemProvider) {
        _currentItemItemProvider = provider
    }
}