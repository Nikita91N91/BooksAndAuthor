import com.bulka.dto.AuthorDTO;
import com.bulka.entity.Author;
import com.bulka.repository.AuthorRepository;
import com.bulka.service.AuthorService;
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
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void testGetAllAuthors() {
        // Arrange
        Author author = new Author();
        author.setId(1L);
        author.setName("Иван");
        author.setSurname("Иванов");
        Author author2 = new Author();
        author2.setId(2L);
        author2.setName("Петр");
        author2.setSurname("Петров");
        List<Author> authors = new ArrayList<>();
        authors.add(author);
        authors.add(author2);
        when(authorRepository.getAllAuthors()).thenReturn(authors);

        // Act
        List<AuthorDTO> authorDTOS = authorService.getAllAuthors();

        // Assert
        assertEquals(2, authorDTOS.size());
        assertEquals(1L, authorDTOS.get(0).getId());
        assertEquals("Иван", authorDTOS.get(0).getName());
        assertEquals("Иванов", authorDTOS.get(0).getSurname());
        assertEquals(2L, authorDTOS.get(1).getId());
        assertEquals("Петр", authorDTOS.get(1).getName());
        assertEquals("Петров", authorDTOS.get(1).getSurname());
    }

    @Test
    void testGetAuthorById() {
        // Arrange
        Author author = new Author();
        author.setId(1L);
        author.setName("Иван");
        author.setSurname("Иванов");
        when(authorRepository.getAuthorById(1L)).thenReturn(author);

        // Act
        AuthorDTO authorDTO = authorService.getAuthorById(1L);

        // Assert
        assertNotNull(authorDTO);
        assertEquals(1L, authorDTO.getId());
        assertEquals("Иван", authorDTO.getName());
        assertEquals("Иванов", authorDTO.getSurname());
    }

    @Test
    void testGetAuthorByIdNotFound() {
        // Arrange
        when(authorRepository.getAuthorById(1L)).thenReturn(null);

        // Act
        AuthorDTO authorDTO = authorService.getAuthorById(1L);

        // Assert
        assertNull(authorDTO);
    }

    @Test
    void testCreateAuthor() {
        // Arrange
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("Иван");
        authorDTO.setSurname("Иванов");
        when(authorRepository.getAuthorByNameAndSurname("Иван", "Иванов")).thenReturn(null);

        // Act
        authorService.createAuthor(authorDTO);

        // Assert
        verify(authorRepository, times(1)).createAuthor(any(Author.class));
    }

    @Test
    void testCreateAuthorAlreadyExists() {
        // Arrange
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName("Иван");
        authorDTO.setSurname("Иванов");
        when(authorRepository.getAuthorByNameAndSurname("Иван", "Иванов")).thenReturn(new Author());

        // Act и Assert
        assertThrows(RuntimeException.class, () -> authorService.createAuthor(authorDTO));
    }

    @Test
    void testUpdateAuthor() {
        // Arrange
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setName("Иван");
        authorDTO.setSurname("Иванов");

        Author author = new Author();
        author.setId(1L);
        author.setName("Иван");
        author.setSurname("Иванов");
        when(authorRepository.getAuthorById(1L)).thenReturn(author);

        // Act
        authorService.updateAuthor(authorDTO);

        // Assert
        verify(authorRepository, times(1)).updateAuthor(any(Author.class));
    }

    @Test
    void testUpdateAuthorNotFound() {
        // Arrange
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(1L);
        authorDTO.setName("Иван");
        authorDTO.setSurname("Иванов");
        when(authorRepository.getAuthorById(1L)).thenReturn(null);

        // Act и Assert
        assertThrows(RuntimeException.class, () -> authorService.updateAuthor(authorDTO));
    }

    @Test
    void testDeleteAuthor() {
        // Arrange
        Author author = new Author();
        author.setId(1L);
        author.setName("Иван");
        author.setSurname("Иванов");
        when(authorRepository.getAuthorById(1L)).thenReturn(author);

        // Act
        authorService.deleteAuthor(1L);

        // Assert
        verify(authorRepository, times(1)).deleteAuthor(1L);
    }

    @Test
    void testDeleteAuthorNotFound() {
        // Arrange
        when(authorRepository.getAuthorById(1L)).thenReturn(null);

        // Act и Assert
        assertThrows(RuntimeException.class, () -> authorService.deleteAuthor(1L));
    }
}