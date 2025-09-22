package org.example

sealed class Attachment {
    abstract val type: String

    data class PhotoAttachment(
        val photo: Photo
    ) : Attachment() {
        override val type: String = "photo"
    }

    data class VideoAttachment(
        val video: Video
    ) : Attachment() {
        override val type: String = "video"
    }

    data class AudioAttachment(
        val audio: Audio
    ) : Attachment() {
        override val type: String = "audio"
    }

    data class LinkAttachment(
        val link: Link
    ) : Attachment() {
        override val type: String = "link"
    }

    data class DocumentAttachment(
        val doc: Document
    ) : Attachment() {
        override val type: String = "doc"
    }
}

data class Photo(
    val id: Int,
    val ownerId: Int,
    val photo130: String? = null,
    val photo604: String? = null,
    val accessKey: String? = null,
    val postId: Int? = null
)

data class Video(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val duration: Int,
    val description: String? = null,
    val accessKey: String? = null
)

data class Audio(
    val id: Int,
    val ownerId: Int,
    val artist: String,
    val title: String,
    val duration: Int,
    val url: String? = null,
    val accessKey: String? = null
)

data class Link(
    val url: String,
    val title: String,
    val caption: String? = null,
    val description: String? = null,
    val photo: Photo? = null
)

data class Document(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val size: Int,
    val ext: String,
    val url: String? = null,
    val accessKey: String? = null
)