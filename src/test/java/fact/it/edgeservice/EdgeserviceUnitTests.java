package fact.it.edgeservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.edgeservice.model.Book;
import fact.it.edgeservice.model.BookQuote;
import fact.it.edgeservice.model.Quote;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EdgeserviceUnitTests {
    @Value("${bookservice.baseurl}")
    private String bookServiceBaseUrl;

    @Value("${quoteservice.baseurl}")
    private String quoteServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    private Book book1 = new Book("Zero to one","687468434568", "Finance");
    private Book book2 = new Book("The LEAN startup", "687468434578", "Finance");

    private Quote quote1 = new Quote("Sales people are like actors", "687468434568");
    private Quote quote2 = new Quote("Do it LEAN", "687468434578");

    private List<Quote> allQuotesForBook1 = Arrays.asList(quote1);
    private List<Quote> allQuotesForBook2 = Arrays.asList(quote2);
    private List<Book> allBooks = Arrays.asList(book1, book2);

    @BeforeEach
    public void initializeMockserver() throws URISyntaxException, JsonProcessingException {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }
    @Test
    public void whenGetBooks_thenReturnFilledBookQuotesJson() throws Exception {

        // GET Book 1 info
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + bookServiceBaseUrl + "/book/")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allBooks))
                );

        mockMvc.perform(get("/books"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Zero to one")))
                .andExpect(jsonPath("$[0].isbn", is("687468434568")))
                .andExpect(jsonPath("$[1].title", is("The LEAN startup")))
                .andExpect(jsonPath("$[1].isbn", is("687468434578")));
    }


    @Test
    public void whenGetBooksByISBN_thenReturnFilledBookQuotesJson() throws Exception {

        // GET Book 1 info
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + bookServiceBaseUrl+ "/book/687468434568")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(book1))
                );


        // GET all quotes for Book 1
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + quoteServiceBaseUrl + "/quote/book/687468434568")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allQuotesForBook1))
                );


        mockMvc.perform(get("/bookquotes/{ISBN}", "687468434568"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookTitle", is("Zero to one")))
                .andExpect(jsonPath("$.isbn", is("687468434568")));
    }
    @Test
    public void whenAddBooks_thenReturnFilledBookQuotesJson() throws Exception {

        BookQuote BookQuote = new BookQuote("new", "687468434568", quote1);

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + quoteServiceBaseUrl + "/quote")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(BookQuote))
                );


        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + bookServiceBaseUrl + "/book/687468434568")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(book1))
                );

//        mockMvc.perform(post("/bookquotes/quote")
//                .param("ISBN", BookQuote.getISBN())
//                .param("bookTitle", BookQuote.getBookTitle())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.bookTitle", is("new")))
//                .andExpect(jsonPath("$.isbn", is("687468434568")))
//                .andExpect(jsonPath("$.quote[0].quote", is("Sales people are like actors")));
//

    }

    @Test
    public void whenUpdateBookQuotes_thenReturnFilledBookQuotesJson() throws Exception {

        BookQuote updatedBookQuote1 = new BookQuote("update", "ISBN1", quote1);


        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + bookServiceBaseUrl + "/book/ISBN1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(updatedBookQuote1))
                );


        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + bookServiceBaseUrl + "/book")))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(updatedBookQuote1))
                );

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + bookServiceBaseUrl + "/book/ISBN1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(book1))
                );

//        mockMvc.perform(put("/bookquotes/quote")
//                .param("ISBN", updatedBookQuote1.getISBN())
//                .param("bookTitle", updatedBookQuote1.getBookTitle().toString())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.bookTitle", is("update")))
//                .andExpect(jsonPath("$.isbn", is("ISBN1")));
//

    }
    @Test
    public void whenDeleteBook_thenReturnStatusOk() throws Exception {

        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://" + quoteServiceBaseUrl + "/quote/1")))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                );

        mockMvc.perform(delete("/bookquotes/quote/{quoteId}", 1))
                .andExpect(status().isOk());
    }
}
