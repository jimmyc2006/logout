package kafka;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
public class KafkaProducerTest {

    private KafkaProducer<String, String> init() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.241.241.17:9092,10.241.241.18:9092,10.241.241.32:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(props);
    }

    @Test
    public void test() {
        KafkaProducer<String, String> kafkaProducer = this.init();
        for (int i = 0; i < 10; i++) {

            Future<RecordMetadata> send = kafkaProducer.
                    send(new ProducerRecord<String, String>(AdminClientTest.MUL_PAR_TOPIC, 8,
                            Integer.toString(i) + "_0_key", Integer.toString(i) + "_0_" + System.currentTimeMillis()));
            try {
                RecordMetadata recordMetadata = send.get();
                log.info("topic:{}, partition:{}, offset:{}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
//                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                log.error("", e);
            } catch (ExecutionException e) {
                log.error("", e);
            }
        }
        kafkaProducer.close();
    }


}
