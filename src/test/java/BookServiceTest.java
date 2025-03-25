import com.bulka.dto.BookDTO;
import com.bulka.entity.Book;
import com.bulka.repository.BookRepository;
import com.bulka.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;


    @Test
    void testGetAllBooks() {
        // Arrange
        List<Book> books = new ArrayList<>();
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Книга 1");
        book1.setAuthorId(1L);
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Книга 2");
        book2.setAuthorId(2L);
        books.add(book1);
        books.add(book2);
        when(bookRepository.getAllBooks()).thenReturn(books);

        // Act
        List<BookDTO> bookDTOS = bookService.getAllBooks();

        // Assert
        assertEquals(2, bookDTOS.size());
        assertEquals(1L, bookDTOS.get(0).getId());
        assertEquals("Книга 1", bookDTOS.get(0).getTitle());
        assertEquals(1L, bookDTOS.get(0).getAuthorId());
        assertEquals(2L, bookDTOS.get(1).getId());
        assertEquals("Книга 2", bookDTOS.get(1).getTitle());
        assertEquals(2L, bookDTOS.get(1).getAuthorId());
    }

    @Test
    void testGetBookById() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Книга 1");
        book.setAuthorId(1L);
        when(bookRepository.getBookById(1L)).thenReturn(book);

        // Act
        BookDTO bookDTO = bookService.getBookById(1L);

        // Assert
        assertNotNull(bookDTO);
        assertEquals(1L, bookDTO.getId());
        assertEquals("Книга 1", bookDTO.getTitle());
        assertEquals(1L, bookDTO.getAuthorId());
    }

    @Test
    void testGetBookByIdNotFound() {
        // Arrange
        when(bookRepository.getBookById(1L)).thenReturn(null);

        // Act
        BookDTO bookDTO = bookService.getBookById(1L);

        // Assert
        assertNull(bookDTO);
    }

    @Test
    void testCreateBook() {
        // Arrange
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Книга 1");
        bookDTO.setAuthorId(1L);

        // Act
        bookService.createBook(bookDTO);

        // Assert
        verify(bookRepository, times(1)).createBook(any(Book.class));
    }

    @Test
    void testUpdateBook() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Книга 1");
        book.setAuthorId(1L);
        when(bookRepository.getBookById(1L)).thenReturn(book);
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Книга 2");
        bookDTO.setAuthorId(2L);

        // Act
        bookService.updateBook(bookDTO);

        // Assert
        verify(bookRepository, times(1)).updateBook(any(Book.class));
    }

    @Test
    void testUpdateBookNotFound() {
        // Arrange
        when(bookRepository.getBookById(1L)).thenReturn(null);
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Книга 2");
        bookDTO.setAuthorId(2L);

        // Act и Assert
        assertThrows(RuntimeException.class, () -> bookService.updateBook(bookDTO));
    }

    @Test
    void testDeleteBook() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Книга 1");
        book.setAuthorId(1L);
        when(bookRepository.getBookById(1L)).thenReturn(book);

        // Act
        bookService.deleteBook(1L);

        // Assert
        verify(bookRepository, times(1)).deleteBook(1L);
    }

    @Test
    void testDeleteBookNotFound() {
        // Arrange
        when(bookRepository.getBookById(1L)).thenReturn(null);

        // Act и Assert
        assertThrows(RuntimeException.class, () -> bookService.deleteBook(1L));
    }

}





