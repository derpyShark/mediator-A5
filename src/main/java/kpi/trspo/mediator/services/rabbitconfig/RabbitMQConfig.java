package kpi.trspo.mediator.services.rabbitconfig;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    Queue cargoTypeQueue() {
        return new Queue("cargoTypeQueue", false);
    }

    @Bean
    Queue machineryTypeQueue() {
        return new Queue("machineryTypeQueue", false);
    }

    @Bean
    Queue personRoleQueue() {
        return new Queue("personRoleQueue", false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("direct-exchange");
    }

    @Bean
    Binding cargoTypeBinding(Queue cargoTypeQueue, DirectExchange exchange) {
        return BindingBuilder.bind(cargoTypeQueue).to(exchange).with("cargoType");
    }

    @Bean
    Binding machineryTypeBinding(Queue machineryTypeQueue, DirectExchange exchange) {
        return BindingBuilder.bind(machineryTypeQueue).to(exchange).with("machineryType");
    }

    @Bean
    Binding personRoleBinding(Queue personRoleQueue, DirectExchange exchange) {
        return BindingBuilder.bind(personRoleQueue).to(exchange).with("personRole");
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
