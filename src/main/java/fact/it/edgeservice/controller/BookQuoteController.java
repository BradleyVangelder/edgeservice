package fact.it.edgeservice.controller;

import fact.it.edgeservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import javax.jms.Queue;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class BookQuoteController {
    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    Queue queue;

    @GetMapping("message/{message}")
    public String publish(@PathVariable("message")
                          final String message) {

        jmsTemplate.convertAndSend(queue, message);

        return "Published Successfully";
    }
    @Value("${bookservice.baseurl}")
    private String bookServiceBaseUrl;

    @Value("${quoteservice.baseurl}")
    private String quoteServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/books")
    public List<Book> getBooks(){
        ResponseEntity<List<Book>> responseEntityReviews =
                restTemplate.exchange("http://" + bookServiceBaseUrl + "/book/",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>() {
                        });

        List<Book> books = responseEntityReviews.getBody();

        return books;
    }

    @GetMapping("/bookquotes/{ISBN}")
    public BookQuotes getBookQuotesByISBN(@PathVariable String ISBN){
        Book book =
                restTemplate.getForObject("http://" + bookServiceBaseUrl + "/book/{ISBN}",
                        Book.class, ISBN);

        ResponseEntity<List<Quote>> responseEntityReviews =
                restTemplate.exchange("http://" + quoteServiceBaseUrl + "/quote/book/{ISBN}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Quote>>() {
                        }, ISBN);

        List<Quote> quotes = responseEntityReviews.getBody();

        return new BookQuotes(book.getTitle(),book.getISBN(), quotes);
    }

    @GetMapping("/bookquotes/getrandombookquote")
    public BookQuote getRandomBookQuote(){
        Book book =
                restTemplate.getForObject("http://" + bookServiceBaseUrl + "/book/random",
                        Book.class);

        Quote quote =
                restTemplate.getForObject("http://" + quoteServiceBaseUrl + "/quote/random/{ISBN}",
                        Quote.class, book.getISBN());

        return new BookQuote(book.getTitle(), book.getISBN(), quote);
    }

    @GetMapping("/bookquotes/beforeguess")
    public Quote getRandomBookBeforeguess(){
        Book book =
                restTemplate.getForObject("http://" + bookServiceBaseUrl + "/book/random",
                        Book.class);

        Quote quote =
                restTemplate.getForObject("http://" + quoteServiceBaseUrl + "/quote/random/{ISBN}",
                        Quote.class, book.getISBN());

        return quote;
    }

    @GetMapping("/bookquotes/guess/{quoteId}/{bookTitleGuess}")
    public Boolean guessQuoteByTitle(@PathVariable String quoteId, @PathVariable String bookTitleGuess){
        Book book =
                restTemplate.getForObject("http://" + bookServiceBaseUrl + "/book/guess/{bookTitleGuess}",
                        Book.class, bookTitleGuess);

        Quote quote =
                restTemplate.getForObject("http://" + quoteServiceBaseUrl + "/quote/" + quoteId,
                        Quote.class);

        return book.getISBN().equals(quote.getISBN());
    }

    @PutMapping("/bookquotes/quote")
    public Quote edit(@RequestParam String id, @RequestParam String quote){
        Quote foundQuote =
                restTemplate.getForObject("http://" + quoteServiceBaseUrl + "/quote/{id}",
                        Quote.class, id);
        foundQuote.setQuote(quote);

        ResponseEntity<Quote> responseEntityReview =
                restTemplate.exchange("http://" + quoteServiceBaseUrl + "/quote/" + id,
                        HttpMethod.PUT, new HttpEntity<>(foundQuote), Quote.class);

        Quote retrievedReview = responseEntityReview.getBody();

        return retrievedReview;
    }

    @PutMapping("/bookquotes/book/{bookId}")
    public Book editBook(@PathVariable Long bookId, @RequestParam String title, @RequestParam String isbn){
        Book book =
                restTemplate.getForObject("http://" + bookServiceBaseUrl + "/book/" + bookId,
                        Book.class);
        book.setTitle(title);
        book.setISBN(isbn);

        ResponseEntity<Book> responseEntityReview =
                restTemplate.exchange("http://" + bookServiceBaseUrl + "/book/" + bookId,
                        HttpMethod.PUT, new HttpEntity<>(book), Book.class);

        Book retrievedReview = responseEntityReview.getBody();

        return retrievedReview;
    }

    @PostMapping("/bookquotes/quote")
    public Quote editBook(@RequestParam String isbn, @RequestParam String quote){

        Quote newQuote =
                restTemplate.postForObject("http://" + quoteServiceBaseUrl + "/quote",
                        new Quote(quote, isbn),Quote.class);

        return newQuote;
    }

    @DeleteMapping("/bookquotes/quote/{quoteId}")
    public ResponseEntity delete(@PathVariable String quoteId) {
        restTemplate.delete("http://" + quoteServiceBaseUrl + "/quote/" + quoteId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bookquotes/book/{bookId}")
    public ResponseEntity deleteBook(@PathVariable Long bookId) {
        restTemplate.delete("http://" + bookServiceBaseUrl + "/book/" + bookId);

        return ResponseEntity.ok().build();
    }
}
