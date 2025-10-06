package org.example.services

import org.example.Comment
import org.example.note.AlreadyDeletedException
import org.example.note.CommentNotFoundException
import org.example.note.IllegalOperationException
import org.example.note.Note
import org.example.note.NoteNotFoundException
import java.time.LocalTime

object NoteService {
    private val noteService = EntityService<Note>()
    private val commentService = EntityService<Comment>()

    /*Методы для заметок*/
    fun add(title: String, text: String, date: Int = currentTime()): Note {
        val note = Note(title = title, text = text, date = date)
        return noteService.add(note)
    }

    fun delete(noteId: Int): Boolean {
        // При удалении заметки помечаем как удаленные все её комментарии
        getComments(noteId).forEach { comment ->
            commentService.delete(comment.id)
        }
        return noteService.delete(noteId)
    }

    fun edit(noteId: Int, title: String, text: String): Boolean {
        val existingNote = noteService.getById(noteId)
            ?: throw NoteNotFoundException("Заметка с ID $noteId не найдена")

        if (existingNote.isDeleted) {
            throw AlreadyDeletedException("Нельзя редактировать удаленную заметку")
        }

        val updatedNote = existingNote.copy(title = title, text = text)
        return noteService.update(updatedNote)
    }

    fun get(noteIds: List<Int> = emptyList()): List<Note> {
        return if (noteIds.isEmpty()) {
            noteService.getAll()
        } else {
            noteIds.mapNotNull { noteService.getById(it) }
        }
    }

    fun getById(noteId: Int): Note {
        return noteService.getById(noteId)
            ?: throw NoteNotFoundException("Заметка с ID $noteId не найдена")
    }

    /*Методы для комментариев*/


    /**
     * Добавляет новый комментарий к заметке
     */
    fun createComment(noteId: Int, fromId: Int, text: String, replyTo: Int? = null): Comment {
        val note = noteService.getById(noteId)
            ?: throw NoteNotFoundException("Заметка с ID $noteId не найдена")

        if (!note.commentsAllowed) {
            throw IllegalOperationException("Комментарии к этой заметке запрещены")
        }

        // Проверяем родительский комментарий, если это ответ
        if (replyTo != null) {
            val parentComment = commentService.getById(replyTo)
                ?: throw CommentNotFoundException("Родительский комментарий с ID $replyTo не найден")

            if (parentComment.isDeleted) {
                throw AlreadyDeletedException("Нельзя отвечать на удаленный комментарий")
            }

            if (parentComment.noteId != noteId) {
                throw IllegalOperationException("Родительский комментарий принадлежит другой заметке")
            }
        }

        val comment = Comment(
            noteId = noteId, // Устанавливаем связь с заметкой
            fromId = fromId,
            date = currentTime(),
            text = text,
            replyToComment = replyTo
        )
        return commentService.add(comment)
    }


    /*dsdsds*/


    /**
     * Удаляет комментарий к заметке
     */
    fun deleteComment(commentId: Int): Boolean {
        val comment = commentService.getById(commentId)
            ?: throw CommentNotFoundException("Комментарий с ID $commentId не найден")

        if (comment.isDeleted) {
            throw AlreadyDeletedException("Комментарий уже удален")
        }

        return commentService.delete(commentId)
    }

    /**
     * Редактирует указанный комментарий у заметки
     */
    fun editComment(commentId: Int, text: String): Boolean {
        val existingComment = commentService.getById(commentId)
            ?: throw CommentNotFoundException("Комментарий с ID $commentId не найден")

        if (existingComment.isDeleted) {
            throw AlreadyDeletedException("Нельзя редактировать удаленный комментарий")
        }

        val updatedComment = existingComment.copy(text = text)
        return commentService.update(updatedComment)
    }

    /**
     * Возвращает список комментариев к заметке
     */
    fun getComments(noteId: Int): List<Comment> {
        // Проверяем существование заметки
        noteService.getById(noteId) ?: throw NoteNotFoundException("Заметка с ID $noteId не найдена")

        return commentService.getAll().filter { it.noteId == noteId }
    }

    /**
     * Восстанавливает удалённый комментарий
     */
    fun restoreComment(commentId: Int): Boolean {
        val comment = commentService.getByIdIncludingDeleted(commentId)
            ?: throw CommentNotFoundException("Комментарий с ID $commentId не найден")

        if (!comment.isDeleted) {
            throw IllegalOperationException("Комментарий не был удален")
        }

        // Проверяем, что родительская заметка существует и не удалена
        val note = noteService.getById(comment.noteId ?: -1)
            ?: throw NoteNotFoundException("Родительская заметка не найдена")

        if (note.isDeleted) {
            throw IllegalOperationException("Нельзя восстановить комментарий к удаленной заметке")
        }

        return commentService.restore(commentId)
    }



    fun clear() {
        noteService.clear()
        commentService.clear()
    }

    fun getNotesCount(): Int = noteService.getAll().size

    fun getCommentsCount(): Int = commentService.getAll().size

    fun getDeletedCommentsCount(): Int = commentService.getDeleted().size

    private fun currentTime(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }
}