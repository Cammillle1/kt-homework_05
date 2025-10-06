package org.example.note

class NoteNotFoundException(message: String) : RuntimeException(message)
class CommentNotFoundException(message: String) : RuntimeException(message)
class AlreadyDeletedException(message: String) : RuntimeException(message)
class IllegalOperationException(message: String) : RuntimeException(message)

