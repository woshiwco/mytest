import org.junit.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.UUID;

public class TestTranslate2 {
    private RestTemplate restTemplate = new RestTemplate();
    String translateContent = "你好，很高兴见到你。";
    // API相关参数
    final String URL = "https://openapi.youdao.com/api";
    final String APPKEY = "4a836e8207d8898d";
    final String APPSECRET = "R9chrE2lDedtAPb0JQ1bjiqXPO3y0X25";
    final String SIGNTYPE = "v3";
    final String SALT = String.valueOf(UUID.randomUUID());
    final String CURTIME = String.valueOf(Instant.now().getLong(ChronoField.INSTANT_SECONDS));
    final String SIGN;
    {
        String subTranslateContent = translateContent.length() <= 20 ? translateContent : translateContent.substring(0, 10) + translateContent.length() + translateContent.substring(translateContent.length() - 10);
        StringBuilder md5StringBuilder = new StringBuilder();
        // md5签名
        try {
            byte[] md5 = MessageDigest.getInstance("SHA-256").digest((APPKEY + subTranslateContent + SALT + CURTIME + APPSECRET).getBytes(StandardCharsets.UTF_8));
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
    public void youDaoTranslate() throws NoSuchAlgorithmException {
        HttpEntity<?> empty = HttpEntity.EMPTY;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<String, Object>();
        multiValueMap.add("q", translateContent);
        multiValueMap.add("from", "auto"); // 中文：zh-CHS
        multiValueMap.add("to", "en");
        multiValueMap.add("appKey", APPKEY);
        multiValueMap.add("signType", SIGNTYPE);
        multiValueMap.add("curtime", CURTIME);
        multiValueMap.add("salt", SALT);
        multiValueMap.add("sign", SIGN);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<>(multiValueMap, httpHeaders), String.class);
        // 结果格式{"requestId":"<requestId>","query":"<原文>","translation":["<翻译结果>"],"l":"<from2to>","isWord":true/false,"tSpeakUrl":"<翻译后发音地址>","speakUrl":"<原文发音地址>",
        // "errorCode":"<errorCode>","dict":"<dict>","webdict":"<webdict>"}
        System.out.println(response.getStatusCode() + ";" + response.getBody());
    }

}
