package fact.it.edgeservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class BookQuoteController {
    @Value("${bookservice.baseurl}")
    private String reviewServiceBaseUrl;

    @Value("${quoteservice.baseurl}")
    private String bookInfoServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;
}
