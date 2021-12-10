package fact.it.edgeservice.controller;

import fact.it.edgeservice.model.Book;
import fact.it.edgeservice.model.BookQuotes;
import fact.it.edgeservice.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    public BookQuotes getBookQuotesByISBN(@PathVariable String ISBN){
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

    @PutMapping("/bookquotes/quote")
    public Quote edit(@RequestParam String quoteId, @RequestParam String newQuote){
        Quote quote =
                restTemplate.getForObject("http://" + quoteServiceBaseUrl + "/quote/" + quoteId,
                        Quote.class);
        quote.setQuote(newQuote);

        ResponseEntity<Quote> responseEntityReview =
                restTemplate.exchange("http://" + quoteServiceBaseUrl + "/quote/" + quoteId,
                        HttpMethod.PUT, new HttpEntity<>(quote), Quote.class);

        Quote retrievedReview = responseEntityReview.getBody();

        return retrievedReview;
    }

    @DeleteMapping("/bookquotes/quote")
    public ResponseEntity delete(@RequestParam String quoteId){
        restTemplate.delete("http://" + quoteServiceBaseUrl + "/quote/" + quoteId);

        return ResponseEntity.ok().build();
    }
}
