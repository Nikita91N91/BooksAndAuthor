import com.bulka.dto.AuthorDTO;
import com.bulka.repository.AuthorRepository;
import com.bulka.service.AuthorService;
import com.bulka.servlets.AuthorServlet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServletTest {

    @Mock
    private AuthorService authorService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private AuthorServlet authorServlet;

    @Test
    void testDoGetById() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn("1");
        AuthorDTO authorDTO = new AuthorDTO();
        when(authorService.getAuthorById(1L)).thenReturn(authorDTO);
        when(response.getWriter()).thenReturn(writer);

        // Act
        authorServlet.doGet(request, response);

        // Assert
        verify(authorService, times(1)).getAuthorById(1L);
        verify(writer, times(1)).println(Optional.ofNullable(any()));
        verify(writer, times(1)).close();
    }

    @Test
    void testDoGetAll() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn(null);
        List<AuthorDTO> authors = new ArrayList<>();
        authors.add(new AuthorDTO());
        when(authorService.getAllAuthors()).thenReturn(authors);
        when(response.getWriter()).thenReturn(writer);

        // Act
        authorServlet.doGet(request, response);

        // Assert
        verify(authorService, times(1)).getAllAuthors();
        verify(writer, times(1)).println(Optional.ofNullable(any()));
        verify(writer, times(1)).close();
    }

    @Test
    void testDoGetByIdNumberFormatException() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn("abc");

        // Act
        authorServlet.doGet(request, response);

        // Assert
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPost() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("name")).thenReturn("Иван");
        when(request.getParameter("surname")).thenReturn("Иванов");

        // Act
        authorServlet.doPost(request, response);

        // Assert
        verify(authorService, times(1)).createAuthor(any());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPostBadRequest() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("name")).thenReturn(null);
        when(request.getParameter("surname")).thenReturn(null);

        // Act
        authorServlet.doPost(request, response);

        // Assert
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPut() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("name")).thenReturn("Иван");
        when(request.getParameter("surname")).thenReturn("Иванов");

        // Act
        authorServlet.doPut(request, response);

        // Assert
        verify(authorService, times(1)).updateAuthor(any());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPutBadRequest() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("name")).thenReturn(null);
        when(request.getParameter("surname")).thenReturn(null);

        // Act
        authorServlet.doPut(request, response);

        // Assert
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPutNumberFormatException() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn("abc");

        // Act
        authorServlet.doPut(request, response);

        // Assert
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoDelete() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn("1");

        // Act
        authorServlet.doDelete(request, response);

        // Assert
        verify(authorService, times(1)).deleteAuthor(1L);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoDeleteBadRequest() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn(null);

        // Act
        authorServlet.doDelete(request, response);

        // Assert
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoDeleteNumberFormatException() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn("abc");

        // Act
        authorServlet.doDelete(request, response);

        // Assert
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
