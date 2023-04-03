package io.ruv.parcel.messaging;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("parcel.messaging.workflow")
public class ParcelMessagingProperties {

    private String exchangeName;
    private String routeBase;
    private QueueAndRoute created;
    private QueueAndRoute balanceProcessed;
    private QueueAndRoute cancelled;

    @Data
    public static class QueueAndRoute {

        private String queue;
        private String route;
    }
}
