package org.example.services

import org.example.Comment
import org.example.note.BaseEntity
import org.example.note.Note

class EntityService<T : BaseEntity> {
    private val entities = mutableListOf<T>()
    private var nextId = 1

    fun add(entity: T): T {
        val newEntity = entity.copyWithId(nextId++) as T
        entities.add(newEntity)
        return newEntity
    }

    fun getById(id: Int): T? {
        return entities.find { it.id == id && !it.isDeleted }
    }

    fun getByIdIncludingDeleted(id: Int): T? {
        return entities.find { it.id == id }
    }

    fun getAll(): List<T> {
        return entities.filter { !it.isDeleted }
    }

    fun getAllIncludingDeleted(): List<T> {
        return entities.toList()
    }

    fun getDeleted(): List<T> {
        return entities.filter { it.isDeleted }
    }


    fun update(entity: T): Boolean {
        val existingEntity = entities.find { it.id == entity.id && !it.isDeleted }
        return if (existingEntity != null) {
            val index = entities.indexOf(existingEntity)
            entities[index] = entity
            true
        } else {
            false
        }
    }

    fun delete(id: Int): Boolean {
        val entity = entities.find { it.id == id && !it.isDeleted }
        return if (entity != null) {
            entity.delete()
            true
        } else {
            false
        }
    }

    fun restore(id: Int): Boolean {
        val entity = entities.find { it.id == id && it.isDeleted }
        return if (entity != null) {
            entity.restore()
            true
        } else {
            false
        }
    }

    fun clear() {
        entities.clear()
        nextId = 1
    }

    private fun T.copyWithId(newId: Int): T {
        return when (this) {
            is Note -> this.copy(id = newId) as T
            is Comment -> this.copy(id = newId) as T
            else -> throw IllegalArgumentException("Unsupported entity type")
        }
    }
}

