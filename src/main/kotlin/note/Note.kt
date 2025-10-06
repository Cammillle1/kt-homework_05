package org.example.note

data class Note(
    override val id: Int = 0,
    val title: String,
    val text: String,
    val date: Int,
    val commentsAllowed: Boolean = true // разрешены ли комментарии
) : BaseEntity(id)
