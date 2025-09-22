import org.example.Post
import org.example.WallService
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class WallServiceObjectTest {

    @Test
    fun add_shouldAssignNonZeroId() {
        // Очищаем перед тестом
        WallService.clear()

        val post = Post(text = "Тестовая запись")
        val result = WallService.add(post)

        assertNotEquals(0, result.id)
    }

    @Test
    fun update_existingPost_shouldReturnTrue() {
        WallService.clear()

        val post1 = WallService.add(Post(text = "Первая запись"))
        val post2 = WallService.add(Post(text = "Вторая запись"))

        val updatedPost = post2.copy(text = "Обновленная запись")
        val result = WallService.update(updatedPost)

        assertTrue(result)
    }

    @Test
    fun update_nonExistingPost_shouldReturnFalse() {
        WallService.clear()

        WallService.add(Post(text = "Первая запись"))

        val nonExistingPost = Post(id = 999, text = "Несуществующая запись")
        val result = WallService.update(nonExistingPost)

        assertFalse(result)
    }
}