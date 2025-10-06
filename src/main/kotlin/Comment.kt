package org.example

import org.example.note.BaseEntity

data class Comment(
    override val id: Int = 0,
    val fromId: Int,
    val date: Int,
    val text: String,
    val donut: Donut? = null,
    val replyToUser: Int? = null,
    val replyToComment: Int? = null,
    val attachments: List<Attachment> = emptyList(),
    val parentsStack: List<Int> = emptyList(),
    val thread: Thread? = null,
    //поле для работы с заметками
    val noteId: Int? = null
) : BaseEntity(id)


data class Donut(
    val isDon: Boolean = false,
    val placeholder: String? = null
)

// Класс для ветки комментариев
data class Thread(
    val count: Int = 0,
    val items: List<Comment> = emptyList(),
    val canPost: Boolean = false,
    val showReplyButton: Boolean = false,
    val groupsCanPost: Boolean = false
)



