import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.junit.Test;

public class TestJSON {
    @Test
    public void test() throws JsonProcessingException {
        String jsonString = "{\"name\": \"wcc\", \"age\": 21, \"man\": true, \"marks\": [100, 90, 85]}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        // 获取属性值的几种方式
        String asText = jsonNode.get("name").asText();  // 类型不符尽量强转，不能转则为基本值
        String textValue = jsonNode.get("name").textValue(); // 类型不符则为基本值
        String findValue = jsonNode.findValue("name").asText();
        String findPath = jsonNode.findPath("name").asText();
        String path = jsonNode.path("name").asText();
        // 遍历
        jsonNode.fieldNames().forEachRemaining(s -> {
            JsonNodeType nodeType = jsonNode.get(s).getNodeType();
            switch (String.valueOf(nodeType)) {
                case "STRING": {
                    System.out.println(s + ":" + jsonNode.get(s).asText());
                } break;
                case "NUMBER": {
                    System.out.println(s + ":" + jsonNode.get(s).asInt());
                } break;
                case "ARRAY": {
                    jsonNode.get(s).elements().forEachRemaining(jsonNode1 -> System.out.println(jsonNode1.asInt()));
                } break;
                case "BOOLEAN": {
                    System.out.println(jsonNode.get(s).asBoolean());
                }
            }
        });
    }
}
