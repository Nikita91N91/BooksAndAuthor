import com.bulka.entity.Author;
import com.bulka.repository.AuthorRepository;
import com.bulka.utils.DatabaseConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class AuthorRepositoryTest {

    @Container
    private static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private Connection connection;
    private AuthorRepository authorRepository;

    @BeforeAll
    public static void initDatabase() {
        database = new PostgreSQLContainer<>("postgres")
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test")
                .withReuse(true);
        database.start();
        try (Connection connection = DriverManager.getConnection(database.getJdbcUrl(), database.getUsername(), database.getPassword())) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS authors (id SERIAL PRIMARY KEY, name VARCHAR(255), surname VARCHAR(255))");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection(database.getJdbcUrl(), database.getUsername(), database.getPassword());
        authorRepository = new AuthorRepository(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.close();
    }

    @Test
    void testGetAllAuthors() {
        // Создайте авторов
        Author author1 = new Author();
        author1.setName("Иван");
        author1.setSurname("Иванов");
        authorRepository.createAuthor(author1);

        Author author2 = new Author();
        author2.setName("Петр");
        author2.setSurname("Петров");
        authorRepository.createAuthor(author2);

        // Получите всех авторов
        List<Author> authors = authorRepository.getAllAuthors();

        // Проверьте, что авторов 2
        assertEquals(2, authors.size());
    }

    @Test
    void testGetAuthorById() {
        // Создайте автора
        Author author = new Author();
        author.setName("Иван");
        author.setSurname("Иванов");
        authorRepository.createAuthor(author);

        // Получите автора по идентификатору
        Author authorById = authorRepository.getAuthorById(author.getId());

        // Проверьте, что автор найден
        assertNotNull(authorById);
        assertEquals(author.getName(), authorById.getName());
        assertEquals(author.getSurname(), authorById.getSurname());
    }

    @Test
    void testCreateAuthor() {
        // Создайте автора
        Author author = new Author();
        author.setName("Иван");
        author.setSurname("Иванов");
        authorRepository.createAuthor(author);

        // Проверьте, что автор создан
        assertNotNull(author.getId());
    }

    @Test
    void testUpdateAuthor() {
        // Создайте автора
        Author author = new Author();
        author.setName("Иван");
        author.setSurname("Иванов");
        authorRepository.createAuthor(author);

        // Обновите автора
        author.setName("Петр");
        author.setSurname("Петров");
        authorRepository.updateAuthor(author);

        // Получите обновленного автора
        Author updatedAuthor = authorRepository.getAuthorById(author.getId());

        // Проверьте, что автор обновлен
        assertEquals(author.getName(), updatedAuthor.getName());
        assertEquals(author.getSurname(), updatedAuthor.getSurname());
    }

    @Test
    void testDeleteAuthor() {
        // Создайте автора
        Author author = new Author();
        author.setName("Иван");
        author.setSurname("Иванов");
        authorRepository.createAuthor(author);

        // Удалите автора
        authorRepository.deleteAuthor(author.getId());

        // Проверьте, что автор удален
        assertNull(authorRepository.getAuthorById(author.getId()));
    }

    @Test
    void testDeleteAllAuthors() {
        // Создайте авторов
        Author author1 = new Author();
        author1.setName("Иван");
        author1.setSurname("Иванов");
        authorRepository.createAuthor(author1);

        Author author2 = new Author();
        author2.setName("Петр");
        author2.setSurname("Петров");
        authorRepository.createAuthor(author2);

        // Удалите всех авторов
        authorRepository.deleteAllAuthors();

        // Проверьте, что авторов нет
        assertTrue(authorRepository.getAllAuthors().isEmpty());
    }
    @Test
    void testGetAuthorByNameAndSurname() {
        // Создаем автора
        Author author = new Author();
        author.setName("Иван");
        author.setSurname("Иванов");
        authorRepository.createAuthor(author);

        // Получаем автора по имени и фамилии
        Author foundAuthor = authorRepository.getAuthorByNameAndSurname("Иван", "Иванов");

        // Проверяем, что автор найден
        assertNotNull(foundAuthor);
        assertEquals("Иван", foundAuthor.getName());
        assertEquals("Иванов", foundAuthor.getSurname());
    }

    @Test
    void testGetAuthorByNameAndSurnameNotFound() {
        // Генерируем уникальное имя и фамилию
        String uniqueName = UUID.randomUUID().toString();
        String uniqueSurname = UUID.randomUUID().toString();

        // Получаем автора по имени и фамилии, которого не существует
        Author foundAuthor = authorRepository.getAuthorByNameAndSurname(uniqueName, uniqueSurname);

        // Проверяем, что автор не найден
        assertNull(foundAuthor);
    }

    @Test
    void testGetAuthorByNameAndSurnameEmptyName() {
        // Получаем автора по пустому имени и фамилии
        Author foundAuthor = authorRepository.getAuthorByNameAndSurname("", "Иванов");

        // Проверяем, что автор не найден
        assertNull(foundAuthor);
    }

    @Test
    void testGetAuthorByNameAndSurnameEmptySurname() {
        // Получаем автора по имени и пустой фамилии
        Author foundAuthor = authorRepository.getAuthorByNameAndSurname("Иван", "");

        // Проверяем, что автор не найден
        assertNull(foundAuthor);
    }

    @Test
    void testGetAuthorByNameAndSurnameNullName() {
        // Получаем автора по null имени и фамилии
        Author foundAuthor = authorRepository.getAuthorByNameAndSurname(null, "Иванов");

        // Проверяем, что автор не найден
        assertNull(foundAuthor);
    }

    @Test
    void testGetAuthorByNameAndSurnameNullSurname() {
        // Получаем автора по имени и null фамилии
        Author foundAuthor = authorRepository.getAuthorByNameAndSurname("Иван", null);

        // Проверяем, что автор не найден
        assertNull(foundAuthor);
    }
}