package canal;

import com.alibaba.otter.canal.client.kafka.KafkaCanalConnector;
import org.apache.kafka.common.TopicPartition;

public class OffsetKafkaCanalConnector extends KafkaCanalConnector {
    public OffsetKafkaCanalConnector(String servers, String topic, Integer partition, String groupId, Integer batchSize, boolean flatMessage) {
        super(servers, topic, partition, groupId, batchSize, flatMessage);
    }
    public void setOffset(String topic, int partition, long offset){
        TopicPartition tp = new TopicPartition(topic, partition);
        this.kafkaConsumer.seek(tp, 1);
    }

    public long getOffset(String topic, int partition) {
        return kafkaConsumer.position(new TopicPartition(topic, partition));
    }
}
