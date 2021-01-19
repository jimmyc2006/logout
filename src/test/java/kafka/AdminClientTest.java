package kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreatePartitionsResult;
import org.apache.kafka.clients.admin.NewPartitions;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Test;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Slf4j
public class AdminClientTest {

    public static final String MUL_PAR_TOPIC = "mpTopic";

    private AdminClient createAdminClient() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "10.241.241.17:9092,10.241.241.18:9092,10.241.241.32:9092");
        props.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10");
        props.setProperty("auto.offset.reset", "earliest");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return AdminClient.create(props);
    }

    @Test
    public void testCreate() throws ExecutionException, InterruptedException {
        try (AdminClient admin = this.createAdminClient()) {
            CreatePartitionsResult mpTopic = admin.createPartitions(Collections.singletonMap(MUL_PAR_TOPIC, NewPartitions.increaseTo(4)));
        }
    }
}
