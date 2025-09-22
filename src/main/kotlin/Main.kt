package org.example

fun main() {
    val newPost = Post(
        ownerId = 123,
        fromId = 456,
        date = 1672531200,
        text = "Привет, это тестовая запись!",
        friendsOnly = true,
        comments = Comments(
            count = 5,
            canPost = true,
            groupsCanPost = false,
            canClose = true,
            canOpen = false
        ),
        likes = Likes(
            count = 10,
            userLikes = false,
            canLike = true,
            canPublish = true
        ),
        reposts = Reposts(
            count = 2,
            userReposted = false
        ),
        views = Views(count = 100),
        postType = "post"
    )

    val addedPost = WallService.add(newPost)
    println("Добавлен пост с ID: ${addedPost.id}")

    val allPosts = WallService.getAll()
    println("Всего постов: ${allPosts.size}")

    val foundPost = WallService.getById(addedPost.id)
    foundPost?.let {
        println("Текст поста: ${it.text}")
        println("Лайков: ${it.likes.count}")
        println("Комментариев: ${it.comments.count}")
    }
}