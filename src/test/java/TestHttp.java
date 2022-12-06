import okhttp3.*;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TestHttp {
    @org.junit.Test
    public void testJdkHttp() {
        HttpURLConnection urlConnection = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            // 创建连接
            URL url = new URL("http://localhost:8829/third/test.do?name=wcc");
            // 设置连接
            urlConnection = (HttpURLConnection) url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.12.65", 1984)));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            urlConnection.setRequestMethod("GET");
            // urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            // 连接
            urlConnection.connect();
            // 传输参数
            outputStream = urlConnection.getOutputStream();
            outputStream.write("name=wee".getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            // 获取返回值
            inputStream = urlConnection.getInputStream();
            isr = new InputStreamReader(inputStream);
            br = new BufferedReader(isr);
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if (line == null) break;
                stringBuilder.append(line);
            }
            System.out.println(urlConnection.getResponseCode() + String.valueOf(stringBuilder));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) outputStream.close();
                if (br != null) br.close();
                if (isr != null) isr.close();
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (urlConnection != null) urlConnection.disconnect();
        }
    }


    /*    @org.junit.TestHttp
        public void testHttpClientHttp() throws Exception {
            // 创建连接客户端
            CloseableHttpClient client = HttpClientBuilder.create()
                    // .setProxy(new HttpHost("192.168.12.65", 1984, "http"))
                    // .setSSLSocketFactory(getMySSLFactory(false/true)) // SSL证书不验证/验证
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(3000).build()).build();
            // 创建请求URI
            URI uri = new URIBuilder("https://144.7.122.146:8104/subsys_monitor_keysite/load_infoQuery_district_monitor_permission.action")
                    .setParameters(new BasicNameValuePair("name", "wc")).build();
            // 创建GET请求
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
            httpGet.setConfig(RequestConfig.custom()
                    .setConnectTimeout(5000)
                    .setMaxRedirects(5).build());
            httpGet.setHeader("Cookie", "JSESSIONID=DB4510B62C91E3C6D8FA841C7BBF458D");
            // 创建POST请求，设置请求体参数
            HttpPost httpPost = new HttpPost(uri);
            // httpPost.setHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(ContentType.APPLICATION_FORM_URLENCODED));
            // httpPost.setEntity(new StringEntity("name=wcc&name=wee", StandardCharsets.UTF_8));
            // 文件上传无需设置ContentType
            MultipartEntityBuilder multipart = MultipartEntityBuilder.create();
            multipart.addBinaryBody("fileKey", new File("C:\\Users\\10753\\Desktop\\test.png"), ContentType.DEFAULT_BINARY, URLEncoder.encode("测试.png", "UTF-8"));
            multipart.addTextBody("name", "wcc");
            httpPost.setEntity(multipart.build());
            // 发送请求获取响应
            CloseableHttpResponse response = null;
            try {
                response = client.execute(httpGet);
                // 获取响应状态码、响应体
                HttpEntity responseEntity = response.getEntity();
                StatusLine statusLine = response.getStatusLine();
                System.out.println("statusLine = " + statusLine.getStatusCode() + EntityUtils.toString(responseEntity, StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (client != null) client.close();
                    if (response != null) response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public LayeredConnectionSocketFactory getMySSLFactory(boolean verify) throws Exception {
            X509TrustManager trustManager;
            if (verify) {
                // 获取客户端证书
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                Certificate certificate = certificateFactory.generateCertificate(this.getClass().getClassLoader().getResourceAsStream("client/ds.crt"));
                // 获取公钥
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null);
                keyStore.setCertificateEntry(UUID.randomUUID().toString(), certificate);
                // 验证
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                    throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
                }
                trustManager = (X509TrustManager) trustManagers[0];
            } else {
                trustManager = new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                };
            }
            // 生成SSL/TLS上下文，返回SSL工厂
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            return new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        }*/
    public X509TrustManager getMyTrustManger(boolean verify) throws Exception {
        X509TrustManager trustManager = null;
        if (verify) {
            // 获取客户端证书
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certificateFactory.generateCertificate(this.getClass().getClassLoader().getResourceAsStream("client/ds.crt"));
            // 获取公钥
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            keyStore.setCertificateEntry(UUID.randomUUID().toString(), certificate);
            // 验证
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            trustManager = (X509TrustManager) trustManagers[0];
        } else {
            trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
        }
        return trustManager;
    }
    public SSLSocketFactory getMySSLSocketFactory(boolean verify) throws Exception {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[]{getMyTrustManger(verify)}, new SecureRandom());
        return sslContext.getSocketFactory();
    }
    @org.junit.Test
    public void testOkHttp() throws Exception {
        //创建客户端
        OkHttpClient client = new OkHttpClient.Builder()
                // .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.4.128", 1984)))
                .sslSocketFactory(getMySSLSocketFactory(false), getMyTrustManger(false))
                .hostnameVerifier((s, sslSession) -> true)
                .connectTimeout(3000, TimeUnit.MILLISECONDS).build();
        //创建URL请求和GET请求
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .addHeader("Cookie", "JSESSIONID=DB4510B62C91E3C6D8FA841C7BBF458D")
                .url("https://144.7.122.146:8104/subsys_monitor_keysite/load_infoQuery_district_monitor_permission.action").build();
        Request get = new Request.Builder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .url("http://localhost:8829/third/test.do?name=wcc")
                .get().build();
        // 创建POST请求
        RequestBody requestBody = RequestBody.create("age=12", MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"));
        FormBody formBody = new FormBody.Builder().add("age", "12").build();
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addFormDataPart("age", "12")
                .addFormDataPart("fileKey", URLEncoder.encode("测试截图.png", "utf-8"),
                        RequestBody.create(new File("C:\\Users\\10753\\Desktop\\屏幕截图 2021-10-16 154610.png"), MediaType.parse("multipart/form-data"))).build();
        Request post = new Request.Builder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .url("http://localhost:8829/third/test.do?name=wcc")
                .post(multipartBody).build();
        //执行请求，返回响应
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            System.out.println(response.code() + ":" + response.body().string());
        }
    }
}
