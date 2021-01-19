//package canal;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.MapperFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.jd.cityos.framework.data.kafka.model.MessageEntity;
//import org.apache.kafka.common.errors.SerializationException;
//import org.apache.kafka.common.serialization.Deserializer;
//import org.springframework.util.Assert;
//
//import java.util.Map;
//
//public class CustomJsonDeserializer implements Deserializer {
//
//  protected final ObjectMapper objectMapper;
//
//  public CustomJsonDeserializer() {
//    this(new ObjectMapper());
//    this.objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
//    this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//  }
//
//  public CustomJsonDeserializer(ObjectMapper objectMapper) {
//    Assert.notNull(objectMapper, "'objectMapper' must not be null.");
//    this.objectMapper = objectMapper;
//  }
//
//  @Override
//  public void configure(Map configs, boolean isKey) {
//
//  }
//
//  @Override
//  public MessageEntity deserialize(String topic, byte[] data) {
//    try {
//      return objectMapper.readValue(data, MessageEntity.class);
//    } catch (Exception e) {
//      throw new SerializationException(
//          "Can't serialize data [" + data + "] for topic [" + topic + "]", e);
//    }
//  }
//
//  @Override
//  public void close() {
//
//  }
//}
