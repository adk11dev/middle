package ru.skillbranch.skillarticles.extensions

fun String?.indexesOf(substr: String, ignoreCase: Boolean = true): List<Int> {
    val indexesList: MutableList<Int> = mutableListOf()

    if (this.isNullOrEmpty() || substr.isEmpty()) {
        return indexesList
    }

    var index: Int = 0

    while (index >= 0) {
        index = this.indexOf(substr, index + 1, ignoreCase)
        if (index >= 0)
            indexesList.add(index)
    }

    return indexesList
}