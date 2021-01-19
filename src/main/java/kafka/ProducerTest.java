package kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerTest {
    public static final String topic = "5608a44f-41b0-46cc-a4fc-5f3f6ab214f0";

    public static void main(String[] args) {
        Properties p = new Properties();
        p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.241.241.17:9092,10.241.241.18:9092,10.241.241.32:9092");//kafka地址，多个地址用逗号分割
        p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        AtomicInteger count = new AtomicInteger(0);
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(p);
        for (int i = 0; i < 10; i++) {
            new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < 20; i++) {
                        String msg = "Hello," + count.incrementAndGet();
                        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, msg);
                        kafkaProducer.send(record, new Callback() {
                            @Override
                            public void onCompletion(RecordMetadata metadata, Exception exception) {
                                System.out.println("offset:" + metadata.offset() + "  checksum:" + metadata.checksum() + " partition:" + metadata.partition());
                            }
                        });
                        System.out.println("消息发送成功:" + msg);
                    }
                    kafkaProducer.flush();
                }
            }.start();
        }
//        try {
//            while (true) {
//                String msg = "Hello," + new Random().nextInt(100);
//                ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, msg);
//                kafkaProducer.send(record);
//                System.out.println("消息发送成功:" + msg);
//                Thread.sleep(2000);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            kafkaProducer.close();
//        }
    }
}
