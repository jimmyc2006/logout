package canal;

import com.alibaba.otter.canal.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class KafkaClientTest {
    Logger log = LoggerFactory.getLogger(KafkaClientTest.class);
    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(KafkaClientTest.class);
        Properties properties = new Properties();
        properties.put("group.id", "123");
        Properties props = new Properties();
        props.putAll(properties);
        props.put("bootstrap.servers", "0910.241.241.17:9092,10.241.241.18:9092,10.241.241.32:92");
//        props.put("group.id", "default");
        props.put("enable.auto.commit", "false");
        props.put("max.partition.fetch.bytes", "8388608");
        props.put("auto.offset.reset", "earliest");
        props.put("max.poll.records", "50");
        props.put("session.timeout.ms", "60000");
        props.put("key.deserializer", StringDeserializer.class);
        props.put("value.deserializer", StringDeserializer.class);
        KafkaConsumer consumer = new KafkaConsumer(props);
        List<String> topics = Arrays.asList("cityos_metadata_fulltext");
        consumer.subscribe(topics);
        while (true) {
            try {
                ConsumerRecords poll = consumer.poll(100);
                System.out.println("?>>>>>>>>>>>>>>>>>:" + poll);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
