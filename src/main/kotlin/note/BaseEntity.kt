package org.example.note

abstract class BaseEntity(
    override val id: Int = 0
) : Entity {
    var isDeleted: Boolean = false
        protected set

    fun delete() {
        isDeleted = true
    }

    fun restore() {
        isDeleted = false
    }
}