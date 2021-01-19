package canal;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.client.kafka.KafkaCanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.alibaba.otter.canal.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CanalKafkaClientExample2 {

    protected final static Logger logger  = LoggerFactory.getLogger(CanalKafkaClientExample2.class);

    private KafkaCanalConnector connector;

    private static volatile boolean         running = false;

    private Thread                          thread  = null;

    private Thread.UncaughtExceptionHandler handler = (t, e) -> logger.error("parse events has an error", e);

    public CanalKafkaClientExample2(String zkServers, String servers, String topic, Integer partition, String groupId){
        connector = new KafkaCanalConnector(servers, topic, partition, groupId, null, true);
    }

    public static void main(String[] args) {
        String  topic     = "sw-example-22";
        String  groupId   = "888";
        try {
            final CanalKafkaClientExample2 kafkaCanalClientExample = new CanalKafkaClientExample2("zk-1.cityos.local:2181,zk-2.cityos.local:2181,zk-3.cityos.local:2181",
                    "kafka-1.cityos.local:9092,kafka-2.cityos.local:9092,kafka-3.cityos.local:9092",
                    topic,
                    0,
                    groupId);
            logger.info("## start the kafka consumer: {}-{}", topic, groupId);
            kafkaCanalClientExample.start();
            logger.info("## the canal kafka consumer is running now ......");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    logger.info("## stop the kafka consumer");
                    kafkaCanalClientExample.stop();
                } catch (Throwable e) {
                    logger.warn("##something goes wrong when stopping kafka consumer:", e);
                } finally {
                    logger.info("## kafka consumer is down.");
                }
            }));
            while (running)
                ;
        } catch (Throwable e) {
            logger.error("## Something goes wrong when starting up the kafka consumer:", e);
            System.exit(0);
        }
    }

    public void start() {
        Assert.notNull(connector, "connector is null");
        thread = new Thread(this::process);
        thread.setUncaughtExceptionHandler(handler);
        thread.start();
        running = true;
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    private void process() {
        while (!running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

        while (running) {
            try {
                connector.connect();
                connector.subscribe();
                while (running) {
                    try {
                        List<FlatMessage> messages = connector.getFlatListWithoutAck(100L, TimeUnit.MILLISECONDS);// 获取message
                        if (messages == null) {
                            continue;
                        }
                        for (FlatMessage message : messages) {
                            System.out.println(JSON.toJSONString(message));
                        }
                        connector.ack(); // 提交确认
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        connector.ack();
                    }
                    logger.info("---------------------------------------------");
                    Thread.sleep(1000 * 1);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        connector.unsubscribe();
        connector.disconnect();
    }
}
