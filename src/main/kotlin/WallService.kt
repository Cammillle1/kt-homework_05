package org.example

object WallService {
    private val posts = mutableListOf<Post>()
    private var nextId = 1
    private val comments = mutableListOf<Comment>()
    private var nextCommentId = 1

    fun add(post: Post): Post {
        val newPost = post.copy(id = nextId++)
        posts.add(newPost)
        return newPost
    }

    fun update(post: Post): Boolean {
        for ((index, existingPost) in posts.withIndex()) {
            if (existingPost.id == post.id) {
                posts[index] = post
                return true
            }
        }
        return false
    }

    fun getAll(): List<Post> {
        return posts.toList()
    }

    fun getById(id: Int): Post? {
        return posts.find { it.id == id }
    }

    fun delete(id: Int): Boolean {
        return posts.removeIf { it.id == id }
    }

    fun createComment(postId: Int, comment: Comment): Comment {
        val postExists = posts.any { it.id == postId }
        if (!postExists) {
            throw PostNotFoundException("Пост с ID $postId не найден")
        }

        val newComment = comment.copy(id = nextCommentId++)
        comments.add(newComment)
        return newComment
    }

    fun clear() {
        posts.clear()
        nextId = 1
    }
}