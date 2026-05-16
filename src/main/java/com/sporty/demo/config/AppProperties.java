package com.sporty.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Kafka kafka = new Kafka();
    private final Rocketmq rocketmq = new Rocketmq();

    @Getter
    @Setter
    public static class Kafka {
        private final Topic topic = new Topic();

        @Getter
        @Setter
        public static class Topic {
            private String eventOutcomes = "event-outcomes";
        }
    }

    @Getter
    @Setter
    public static class Rocketmq {
        private final Topic topic = new Topic();

        @Getter
        @Setter
        public static class Topic {
            private String betSettlements = "bet-settlements";
        }
    }
}
