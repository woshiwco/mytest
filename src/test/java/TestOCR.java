import org.junit.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.Base64;
import java.util.UUID;

public class TestOCR {
    private RestTemplate restTemplate = new RestTemplate();
    String img = "C:\\Users\\10753\\Desktop\\屏幕截图 2021-11-10 160337.png";
    // API相关参数
    final String URL = "https://openapi.youdao.com/ocrapi";
    final String APPKEY = "4a836e8207d8898d";
    final String APPSECRET = "R9chrE2lDedtAPb0JQ1bjiqXPO3y0X25";
    final String SIGNTYPE = "v3";
    final String SALT = String.valueOf(UUID.randomUUID());
    final String CURTIME = String.valueOf(Instant.now().getLong(ChronoField.INSTANT_SECONDS));
    final String SIGN;
    final String imageType = "1";
    final String langType = "zh-CHS";
    final String detectType = "10012";
    final String docType = "json";
    // 生成图片Base64
    {
        byte[] data = null;
        try (InputStream in = new FileInputStream(img)) {
            data = new byte[in.available()];
            in.read(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        img = Base64.getEncoder().encodeToString(data);
    }
    // 签名(同翻译)
    {
        String subImg = img.length() <= 20 ? img : img.substring(0, 10) + img.length() + img.substring(img.length() - 10);
        StringBuilder md5StringBuilder = new StringBuilder();
        try {
            byte[] md5 = MessageDigest.getInstance("SHA-256").digest((APPKEY + subImg + SALT + CURTIME + APPSECRET).getBytes(StandardCharsets.UTF_8));
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
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<String, Object>();
        multiValueMap.add("img", img);
        multiValueMap.add("appKey", APPKEY);
        multiValueMap.add("signType", SIGNTYPE);
        multiValueMap.add("curtime", CURTIME);
        multiValueMap.add("salt", SALT);
        multiValueMap.add("sign", SIGN);
        multiValueMap.add("detectType", detectType);
        multiValueMap.add("imageType", imageType);
        multiValueMap.add("langType", langType);
        multiValueMap.add("docType", docType);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<>(multiValueMap, httpHeaders), String.class);
        // 返回结果格式：{"requestId":"<requestId>","errorCode":"<errorCode>","result":"{"orientation":"<图像方向>","regions.dir":"<文本方向>"，
        // "regions.lang":"<langType>","regions.lines.text":"<识别结果>"}"}
        System.out.println(response.getStatusCode() + ";" + response.getBody());
    }
}
