import org.apache.tomcat.util.security.MD5Encoder;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.security.provider.MD5;

import java.nio.charset.StandardCharsets;

public class TestTranslate {
    private RestTemplate restTemplate = new RestTemplate();
    String url  = "https://fanyi-api.baidu.com/api/trans/vip/translate";
    String appid = "20211103000990653";
    String privateKey = "Dwx7QkqC7yjR8CRpakI6";
    String salt = "ZDY123456";
    @Test
    public void test() {
    // 单请求参数长度6000bytes， 约2000个字符
        HttpEntity<?> empty = HttpEntity.EMPTY;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<String, Object>();
        String translateContent = "你好，很高兴见到你，你吃饭了吗？";
        multiValueMap.add("q", translateContent);
        multiValueMap.add("from", "auto");
        multiValueMap.add("to", "en");
        multiValueMap.add("appid", appid);
        multiValueMap.add("salt", salt);
        byte[] sign = (appid + translateContent +  salt + privateKey).getBytes();
        multiValueMap.add("sign", MD5Encoder.encode(sign));
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(multiValueMap, httpHeaders), String.class);
        String body = response.getBody();
        System.out.println("body = " + body);

    }


}
