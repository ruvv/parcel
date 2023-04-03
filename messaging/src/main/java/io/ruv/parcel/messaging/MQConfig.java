package io.ruv.parcel.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ParcelMessagingProperties.class)
@RequiredArgsConstructor
public class MQConfig {

    private final ParcelMessagingProperties properties;

    @Bean
    public TopicExchange parcelWorkflowExchange() {

        return ExchangeBuilder.topicExchange(properties.getExchangeName())
                .durable(true)
                .build();
    }

    @Bean
    public Queue parcelCreated() {

        return QueueBuilder.durable(properties.getCreated().getQueue()).build();
    }

    @Bean
    public Binding parcelCreatedBinding() {

        return BindingBuilder.bind(parcelCreated())
                .to(parcelWorkflowExchange())
                .with(properties.getCreated().getRoute());
    }

    @Bean
    public Queue parcelBalanceProcessed() {

        return QueueBuilder.durable(properties.getBalanceProcessed().getQueue()).build();
    }

    @Bean
    public Binding parcelBalanceProcessedBinding() {

        return BindingBuilder.bind(parcelBalanceProcessed())
                .to(parcelWorkflowExchange())
                .with(properties.getBalanceProcessed().getRoute());
    }

    @Bean
    public Queue parcelCancelled() {

        return QueueBuilder.durable(properties.getCancelled().getQueue()).build();
    }

    @Bean
    public Binding parcelCancelledBinding() {

        return BindingBuilder.bind(parcelCancelled())
                .to(parcelWorkflowExchange())
                .with(properties.getCancelled().getRoute());
    }
}
