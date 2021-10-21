package fact.it.edgeservice.controller;

import fact.it.edgeservice.model.Book;
import fact.it.edgeservice.model.BookQuotes;
import fact.it.edgeservice.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.List;

@RestController
public class BookQuoteController {
    @Value("${bookservice.baseurl}")
    private String bookServiceBaseUrl;

    @Value("${quoteservice.baseurl}")
    private String quoteServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/bookquotes/{ISBN}")
    public BookQuotes getRankingsByUserId(@PathVariable String ISBN){

        Book book =
                restTemplate.getForObject("http://" + bookServiceBaseUrl + "/book/{ISBN}",
                        Book.class, ISBN);

        ResponseEntity<List<Quote>> responseEntityReviews =
                restTemplate.exchange("http://" + quoteServiceBaseUrl + "/quote/{ISBN}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Quote>>() {
                        }, ISBN);

        List<Quote> quotes = responseEntityReviews.getBody();

        return new BookQuotes(book.getTitle(),book.getISBN(), quotes);
    }
}
