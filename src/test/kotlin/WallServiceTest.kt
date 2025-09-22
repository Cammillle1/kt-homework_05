import org.example.Attachment
import org.example.Audio
import org.example.Document
import org.example.Link
import org.example.Photo
import org.example.Post
import org.example.Video
import org.example.WallService
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.example.Attachment.*

class WallServiceObjectTest {

    @Before
    fun setUp() {
        WallService.clear()
    }

    @Test
    fun add_shouldAssignNonZeroId() {
        val post = Post(text = "Тестовая запись")
        val result = WallService.add(post)

        assertNotEquals(0, result.id)
    }

    @Test
    fun update_existingPost_shouldReturnTrue() {

        val post1 = WallService.add(Post(text = "Первая запись"))
        val post2 = WallService.add(Post(text = "Вторая запись"))

        val updatedPost = post2.copy(text = "Обновленная запись")
        val result = WallService.update(updatedPost)

        assertTrue(result)
    }

    @Test
    fun update_nonExistingPost_shouldReturnFalse() {
        WallService.add(Post(text = "Первая запись"))

        val nonExistingPost = Post(id = 999, text = "Несуществующая запись")
        val result = WallService.update(nonExistingPost)

        assertFalse(result)
    }

    @Test
    fun add_postWithPhotoAttachment_shouldStoreCorrectly() {
        // Arrange
        val photo = Photo(
            id = 1, ownerId = 123, photo130 = "https://vk.com/photo130.jpg", photo604 = "https://vk.com/photo604.jpg"
        )
        val photoAttachment = PhotoAttachment(photo)
        val post = Post(
            text = "Пост с фото", attachments = listOf(photoAttachment)
        )

        // Act
        val result = WallService.add(post)

        // Assert
        assertEquals(1, result.attachments.size)
        assertEquals("photo", result.attachments[0].type)
        assertTrue(result.attachments[0] is PhotoAttachment)
        val attachedPhoto = (result.attachments[0] as PhotoAttachment).photo
        assertEquals(1, attachedPhoto.id)
        assertEquals(123, attachedPhoto.ownerId)
        assertEquals("https://vk.com/photo604.jpg", attachedPhoto.photo604)
    }

    @Test
    fun add_postWithVideoAttachment_shouldStoreCorrectly() {
        // Arrange
        val video = Video(
            id = 2, ownerId = 456, title = "Интересное видео", duration = 180
        )
        val videoAttachment = VideoAttachment(video)
        val post = Post(
            text = "Пост с видео", attachments = listOf(videoAttachment)
        )

        // Act
        val result = WallService.add(post)

        // Assert
        assertEquals(1, result.attachments.size)
        assertEquals("video", result.attachments[0].type)
        assertTrue(result.attachments[0] is VideoAttachment)
        val attachedVideo = (result.attachments[0] as VideoAttachment).video
        assertEquals(2, attachedVideo.id)
        assertEquals("Интересное видео", attachedVideo.title)
        assertEquals(180, attachedVideo.duration)
    }

    @Test
    fun add_postWithAudioAttachment_shouldStoreCorrectly() {
        // Arrange
        val audio = Audio(
            id = 3, ownerId = 789, artist = "The Beatles", title = "Yesterday", duration = 125
        )
        val audioAttachment = AudioAttachment(audio)
        val post = Post(
            text = "Пост с аудио", attachments = listOf(audioAttachment)
        )

        // Act
        val result = WallService.add(post)

        // Assert
        assertEquals(1, result.attachments.size)
        assertEquals("audio", result.attachments[0].type)
        assertTrue(result.attachments[0] is AudioAttachment)
        val attachedAudio = (result.attachments[0] as AudioAttachment).audio
        assertEquals(3, attachedAudio.id)
        assertEquals("The Beatles", attachedAudio.artist)
        assertEquals("Yesterday", attachedAudio.title)
    }

    @Test
    fun add_postWithLinkAttachment_shouldStoreCorrectly() {
        // Arrange
        val link = Link(
            url = "https://kotlinlang.org",
            title = "Официальный сайт Kotlin",
            description = "Язык программирования Kotlin"
        )
        val linkAttachment = Attachment.LinkAttachment(link)
        val post = Post(
            text = "Пост с ссылкой", attachments = listOf(linkAttachment)
        )

        // Act
        val result = WallService.add(post)

        // Assert
        assertEquals(1, result.attachments.size)
        assertEquals("link", result.attachments[0].type)
        assertTrue(result.attachments[0] is Attachment.LinkAttachment)
        val attachedLink = (result.attachments[0] as Attachment.LinkAttachment).link
        assertEquals("https://kotlinlang.org", attachedLink.url)
        assertEquals("Официальный сайт Kotlin", attachedLink.title)
    }

    @Test
    fun add_postWithDocumentAttachment_shouldStoreCorrectly() {
        // Arrange
        val document = Document(
            id = 4, ownerId = 111, title = "Техническое задание.pdf", size = 2048000, ext = "pdf"
        )
        val documentAttachment = Attachment.DocumentAttachment(document)
        val post = Post(
            text = "Пост с документом", attachments = listOf(documentAttachment)
        )

        // Act
        val result = WallService.add(post)

        // Assert
        assertEquals(1, result.attachments.size)
        assertEquals("doc", result.attachments[0].type)
        assertTrue(result.attachments[0] is Attachment.DocumentAttachment)
        val attachedDoc = (result.attachments[0] as Attachment.DocumentAttachment).doc
        assertEquals(4, attachedDoc.id)
        assertEquals("Техническое задание.pdf", attachedDoc.title)
        assertEquals("pdf", attachedDoc.ext)
    }

    @Test
    fun add_postWithMultipleAttachments_shouldStoreAll() {
        // Arrange
        val photo = Photo(id = 1, ownerId = 123, photo604 = "photo_url")
        val video = Video(id = 2, ownerId = 123, title = "Video", duration = 60)
        val audio = Audio(id = 3, ownerId = 123, artist = "Artist", title = "Song", duration = 120)

        val attachments = listOf(
            Attachment.PhotoAttachment(photo), Attachment.VideoAttachment(video), Attachment.AudioAttachment(audio)
        )

        val post = Post(
            text = "Пост с несколькими вложениями", attachments = attachments
        )

        // Act
        val result = WallService.add(post)

        // Assert
        assertEquals(3, result.attachments.size)
        assertEquals("photo", result.attachments[0].type)
        assertEquals("video", result.attachments[1].type)
        assertEquals("audio", result.attachments[2].type)
    }

    @Test
    fun update_postWithAttachments_shouldUpdateCorrectly() {
        // Arrange
        val originalPhoto = Photo(id = 1, ownerId = 123, photo604 = "old_photo.jpg")
        val originalPost = WallService.add(
            Post(
                text = "Оригинальный пост", attachments = listOf(Attachment.PhotoAttachment(originalPhoto))
            )
        )

        val newPhoto = Photo(id = 1, ownerId = 123, photo604 = "new_photo.jpg")
        val updatedPost = originalPost.copy(
            text = "Обновленный пост", attachments = listOf(Attachment.PhotoAttachment(newPhoto))
        )

        // Act
        val updateResult = WallService.update(updatedPost)
        val retrievedPost = WallService.getById(originalPost.id)

        // Assert
        assertTrue(updateResult)
        assertNotNull(retrievedPost)
        assertEquals("Обновленный пост", retrievedPost!!.text)
        assertEquals(1, retrievedPost.attachments.size)
        val retrievedPhoto = (retrievedPost.attachments[0] as Attachment.PhotoAttachment).photo
        assertEquals("new_photo.jpg", retrievedPhoto.photo604)
    }

    @Test
    fun add_postWithEmptyAttachments_shouldWorkCorrectly() {
        // Arrange
        val post = Post(
            text = "Пост без вложений", attachments = emptyList() // явно пустой список
        )

        // Act
        val result = WallService.add(post)

        // Assert
        assertTrue(result.attachments.isEmpty())
        assertEquals("Пост без вложений", result.text)
    }

    @Test
    fun getById_postWithAttachments_shouldReturnCorrectData() {
        // Arrange
        val document = Document(
            id = 5, ownerId = 999, title = "Тестовый документ.txt", size = 1024, ext = "txt"
        )
        val post = Post(
            text = "Пост для поиска", attachments = listOf(Attachment.DocumentAttachment(document))
        )
        val addedPost = WallService.add(post)

        // Act
        val retrievedPost = WallService.getById(addedPost.id)

        // Assert
        assertNotNull(retrievedPost)
        assertEquals("Пост для поиска", retrievedPost!!.text)
        assertEquals(1, retrievedPost.attachments.size)
        assertEquals("doc", retrievedPost.attachments[0].type)
    }

    @Test
    fun attachments_shouldHaveCorrectTypes() {
        // Arrange
        val photoAttachment = Attachment.PhotoAttachment(Photo(id = 1, ownerId = 1, photo604 = ""))
        val videoAttachment = Attachment.VideoAttachment(Video(id = 1, ownerId = 1, title = "", duration = 0))
        val audioAttachment =
            Attachment.AudioAttachment(Audio(id = 1, ownerId = 1, artist = "", title = "", duration = 0))
        val linkAttachment = Attachment.LinkAttachment(Link(url = "", title = ""))
        val docAttachment = Attachment.DocumentAttachment(Document(id = 1, ownerId = 1, title = "", size = 0, ext = ""))

        // Assert
        assertEquals("photo", photoAttachment.type)
        assertEquals("video", videoAttachment.type)
        assertEquals("audio", audioAttachment.type)
        assertEquals("link", linkAttachment.type)
        assertEquals("doc", docAttachment.type)
    }
}