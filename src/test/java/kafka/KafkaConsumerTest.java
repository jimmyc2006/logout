package kafka;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Slf4j
public class KafkaConsumerTest {

    private KafkaConsumer<String, String> init(boolean autoCommit, String groupId) {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "10.241.241.17:9092,10.241.241.18:9092,10.241.241.32:9092");
        if (StringUtils.isBlank(groupId)) {
            props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test");
        } else {
            props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        }
        if (autoCommit) {
            props.setProperty("enable.auto.commit", "true");
        } else {
            props.setProperty("enable.auto.commit", "false");
        }
        props.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10");
        props.setProperty("auto.offset.reset", "earliest");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return new KafkaConsumer<>(props);
    }

    /**
     * 自动commit并不是想象的那样，poll一次以后立即提交,应该是在下一次poll的时候提交当前的offset
     */
    @Test
    public void testPollAutoCommit() {
        KafkaConsumer<String, String> kafkaConsumer = this.init(true, "vvvv2");
        kafkaConsumer.subscribe(Arrays.asList("my-topic"));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            log.info("records:" + records.count());
            for (ConsumerRecord<String, String> record : records) {
                log.info("topic:{}, partition:{}, offset:{}, timestamp:{}, key:{}, value:{}",
                        record.topic(), record.partition(), record.offset(), record.timestamp(), record.key(), record.value());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                log.info("start sleep 3333333333333333333");
                Thread.sleep(1000 * 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 每次执行的时候，当前的连接调用poll的时候，是基于上次offset的
     * 调用kafkaConsumer.commitSync()的时候，直接提交的是本次获取到的最大值,比如本次获取0-9的offset
     * 处理0以后，直接提交，下次也会从10开始取,类似于批量提交
     */
    @Test
    public void testPollNotAutoCommit() {
        KafkaConsumer<String, String> kafkaConsumer = this.init(false, "tttt2");
        kafkaConsumer.subscribe(Arrays.asList("my-topic"));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            if (records.isEmpty()) {
                log.info("there is no records.");
            } else {
                log.info("count:" + records.count());
            }
            for (ConsumerRecord<String, String> record : records) {
                log.info("topic:{}, partition:{}, offset:{}, timestamp:{}, key:{}, value:{}",
                        record.topic(), record.partition(), record.offset(), record.timestamp(), record.key(), record.value());
                log.info("committed {} -> {}", record.topic(), record.partition());
//                kafkaConsumer.committed(new TopicPartition(record.topic(), record.partition()));
                kafkaConsumer.commitSync();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000 * 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 提交某个点位,分条提交,这种策略会比自动提交多提交
     * 本次提交的值就是下一次获取的起始offset,所以提交的时候，进行了+1的操作
     */
    @Test
    public void testPollNotAutoCommit2() {
        KafkaConsumer<String, String> kafkaConsumer = this.init(false, "manual_2");
        kafkaConsumer.subscribe(Arrays.asList("my-topic"));
        try {
            while(true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (TopicPartition partition : records.partitions()) {
                    List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                    for (ConsumerRecord<String, String> record : partitionRecords) {
                        log.info("topic:{}, offset:{}, key:{}, value:{}", record.topic(), record.offset(), record.key(), record.value());
                        log.info("commitSync");
                        long lastOffset = record.offset();
                        kafkaConsumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } finally {
            kafkaConsumer.close();
        }
    }

    /**
     * 指定消费的分区
     */
    @Test
    public void testAssignPartition() {
        KafkaConsumer<String, String> kafkaConsumer = this.init(true, "manual_2");
        String topic = AdminClientTest.MUL_PAR_TOPIC;
        TopicPartition partition0 = new TopicPartition(topic, 1);
        TopicPartition partition1 = new TopicPartition(topic, 2);
        // 监听某些分区
        kafkaConsumer.assign(Arrays.asList(partition0, partition1));
//        kafkaConsumer.subscribe(Arrays.asList("my-topic"));
        try {
            while(true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (ConsumerRecord record : records) {
                    log.info("topic:{}, partition:{}, offset:{}, key:{}, value:{}", record.topic(), record.partition(), record.offset(), record.key(), record.value());
                }
                try {
                    Thread.sleep(1000 * 3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            kafkaConsumer.close();
        }
    }

    @Test
    public void testFetchAllPartitions() {
        KafkaConsumer<String, String> kafkaConsumer = this.init(true, "manual_2");
        List<PartitionInfo> partitionInfos = kafkaConsumer.partitionsFor(AdminClientTest.MUL_PAR_TOPIC);
        log.info("" + partitionInfos);
    }
}
