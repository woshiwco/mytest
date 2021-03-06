import org.junit.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TestTranslate {
    private RestTemplate restTemplate = new RestTemplate();
    // 单请求参数长度6000bytes， 约2000个字符
    String translateContent = "你好，很高兴见到你。";
    // API相关参数
    final String URL = "https://fanyi-api.baidu.com/api/trans/vip/translate";
    final String APPID = "20211103000990653";
    final String PRIVATEKEY = "Dwx7QkqC7yjR8CRpakI6";
    final String SALT = "ZDY123456";
    final String SIGN;
    {
        String subTranslateContent = translateContent.length() <= 20 ? translateContent : translateContent.substring(0, 10) + translateContent.length() + translateContent.substring(translateContent.length() - 10);
        StringBuilder md5StringBuilder = new StringBuilder();;
        // md5签名
        try {
            byte[] md5 = MessageDigest.getInstance("MD5").digest((APPID + translateContent + SALT + PRIVATEKEY).getBytes(StandardCharsets.UTF_8));

            for (byte b : md5) {
                int i = b & 0xff;
                if (i < 0x10) md5StringBuilder.append("0");
                md5StringBuilder.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SIGN = String.valueOf(md5StringBuilder);
    }
    @Test
    public void baiduTranslate() throws NoSuchAlgorithmException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<String, Object>();
        multiValueMap.add("q", translateContent);
        multiValueMap.add("from", "auto");
        multiValueMap.add("to", "en");
        multiValueMap.add("appid", APPID);
        multiValueMap.add("salt", SALT);
        multiValueMap.add("sign", SIGN);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<>(multiValueMap, httpHeaders), String.class);
        // 结果格式{"from":"<from>","to":"<to>","trans_result":[{"src":"<srcUnicode编码>","dst":"<dst>"]}
        System.out.println(response.getStatusCode() + ";" + response.getBody());
    }
}
