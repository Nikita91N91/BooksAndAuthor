import com.bulka.entity.Book;
import com.bulka.repository.BookRepository;
import com.bulka.repository.RepositoryException;
import org.junit.jupiter.api.AfterEach;
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

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class BookRepositoryTest {

    @Container
    private PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private BookRepository bookRepository;

    @BeforeEach
    void setUp() throws SQLException {
        String jdbcUrl = database.getJdbcUrl();
        String username = database.getUsername();
        String password = database.getPassword();
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        bookRepository = new BookRepository(connection);
        // Создаем таблицу books
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS books (id SERIAL PRIMARY KEY, title VARCHAR(255), author_id INTEGER)");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Удаляем таблицу books
        try (Connection connection = DriverManager.getConnection(database.getJdbcUrl(), database.getUsername(), database.getPassword());
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS books");
        }
    }

    @Test
    void testGetAllBooks() {
        // Добавляем несколько книг
        Book book1 = new Book();
        book1.setTitle("Книга 1");
        book1.setAuthorId(1L);
        bookRepository.createBook(book1);

        Book book2 = new Book();
        book2.setTitle("Книга 2");
        book2.setAuthorId(2L);
        bookRepository.createBook(book2);

        // Получаем все книги
        List<Book> books = bookRepository.getAllBooks();
        assertEquals(2, books.size());
    }

    @Test
    void testGetBookById() {
        // Добавляем книгу
        Book book = new Book();
        book.setTitle("Книга");
        book.setAuthorId(1L);
        bookRepository.createBook(book);

        // Получаем книгу по id
        Book bookById = bookRepository.getBookById(1L);
        assertNotNull(bookById);
        assertEquals("Книга", bookById.getTitle());
    }

    @Test
    void testCreateBook() {
        // Создаем книгу
        Book book = new Book();
        book.setTitle("Книга");
        book.setAuthorId(1L);
        bookRepository.createBook(book);

        // Проверяем, что книга создана
        List<Book> books = bookRepository.getAllBooks();
        assertEquals(1, books.size());
    }

    @Test
    void testUpdateBook() {
        // Создаем книгу
        Book book = new Book();
        book.setTitle("Книга");
        book.setAuthorId(1L);
        bookRepository.createBook(book);

        // Получаем id созданной книги
        List<Book> books = bookRepository.getAllBooks();
        Book createdBook = books.get(0);
        Long id = createdBook.getId();

        // Обновляем книгу
        createdBook.setTitle("Новая книга");
        createdBook.setAuthorId(2L);
        bookRepository.updateBook(createdBook);

        // Проверяем, что книга обновлена
        Book updatedBook = bookRepository.getBookById(id);
        assertEquals("Новая книга", updatedBook.getTitle());
    }

    @Test
    void testDeleteBook() {
        // Создаем книгу
        Book book = new Book();
        book.setTitle("Книга");
        book.setAuthorId(1L);
        bookRepository.createBook(book);

        // Удаляем книгу
        bookRepository.deleteBook(1L);

        // Проверяем, что книга удалена
        List<Book> books = bookRepository.getAllBooks();
        assertEquals(0, books.size());
    }
    @Test
    void testRepositoryException() {
        // Создаем книгу с пустым названием
        Book book = new Book();
        book.setTitle("");
        book.setAuthorId(1L);

        // Пытаемся создать книгу
        assertThrows(RepositoryException.class, () -> bookRepository.createBook(book));
    }
}
