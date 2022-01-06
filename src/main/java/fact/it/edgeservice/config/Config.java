package fact.it.edgeservice.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;

@Configuration
public class Config {

//    @Value("${activemq.broker-url}")
//    private String brokerUrl;
//
//    @Bean
//    public Queue queue() {
//        return new ActiveMQQueue("test.queue");
//    }
//    @Bean
//    public Queue editQuote() {
//        return new ActiveMQQueue("editQuote.queue");
//    }
//    @Bean
//    public Queue createQuote() {
//        return new ActiveMQQueue("createQuote.queue");
//    }
//    @Bean
//    public Queue deleteQuote() {
//        return new ActiveMQQueue("deleteQuote.queue");
//    }
//
//    @Bean
//    public ActiveMQConnectionFactory activeMQConnectionFactory() {
//        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
//        factory.setBrokerURL(brokerUrl);
//        factory.setUserName("admin");
//        factory.setPassword("admin");
//        return factory;
//    }
//
//    @Bean
//    public JmsTemplate jmsTemplate() {
//        return new JmsTemplate(activeMQConnectionFactory());
//    }
}