package com.bulka.servlets;

import com.bulka.dto.BookDTO;
import com.bulka.repository.BookRepository;
import com.bulka.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BookServletTest {

    @Mock
    private BookService bookService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter writer;

    @InjectMocks
    private BookServlet bookServlet;


    @BeforeEach
    public void setUp() throws Exception {
        //...
        when(response.getWriter()).thenReturn(writer);
        //...
    }

    @Test
    void testDoGetById() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn("1");
        BookDTO bookDTO = new BookDTO();
        when(bookService.getBookById(1L)).thenReturn(bookDTO);

        // Act
        bookServlet.doGet(request, response);

        // Assert
        verify(bookService, times(1)).getBookById(1L);
        verify(writer, times(1)).println(bookDTO);
        verify(writer, times(1)).close();
    }

    @Test
    void testDoGetAll() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn(null);
        List<BookDTO> books = new ArrayList<>();
        books.add(new BookDTO());
        when(bookService.getAllBooks()).thenReturn(books);

        // Act
        bookServlet.doGet(request, response);

        // Assert
        verify(bookService, times(1)).getAllBooks();
        verify(writer, times(1)).println(books);
        verify(writer, times(1)).close();
    }

    @Test
    void testDoGetByIdNumberFormatException() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn("abc");

        // Act
        bookServlet.doGet(request, response);

        // Assert
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPost() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("title")).thenReturn("Title");
        when(request.getParameter("authorId")).thenReturn("1");

        // Act
        bookServlet.doPost(request, response);

        // Assert
        verify(bookService, times(1)).createBook(any());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPostBadRequest() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("title")).thenReturn(null);
        when(request.getParameter("authorId")).thenReturn(null);

        // Act
        bookServlet.doPost(request, response);

        // Assert
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPostNumberFormatException() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("title")).thenReturn("Title");
        when(request.getParameter("authorId")).thenReturn("abc");

        // Act
        bookServlet.doPost(request, response);

        // Assert
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPut() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("title")).thenReturn("Title");
        when(request.getParameter("authorId")).thenReturn("1");

        // Act
        bookServlet.doPut(request, response);

        // Assert
        verify(bookService, times(1)).updateBook(any());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPutBadRequest() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("title")).thenReturn(null);
        when(request.getParameter("authorId")).thenReturn(null);

        // Act
        bookServlet.doPut(request, response);

        // Assert
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoPutNumberFormatException() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn("abc");
        when(request.getParameter("title")).thenReturn("Title");
        when(request.getParameter("authorId")).thenReturn("1");

        // Act
        bookServlet.doPut(request, response);

        // Assert
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoDelete() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn("1");

        // Act
        bookServlet.doDelete(request, response);

        // Assert
        verify(bookService, times(1)).deleteBook(1L);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void testDoDeleteBadRequest() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn(null);

        // Act
        bookServlet.doDelete(request, response);

        // Assert
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testDoDeleteNumberFormatException() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("id")).thenReturn("abc");

        // Act
        bookServlet.doDelete(request, response);

        // Assert
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}