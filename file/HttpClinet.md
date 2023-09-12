gradle

```
compile group: 'org.apache.httpcomponents', name: 'httpclient', version: '4.5.10'
```

maven

```
<!-- https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient -->
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.10</version>
</dependency>

```


# application/x-www-form-urlencoded (form data傳送方式) POST

```
public static String getContent(String url, Map<String, String> mapdata) {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 創建httppost
        HttpPost httpPost = new HttpPost(url);
        try {
            // 設置提交方式
            httpPost.addHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            // 添加參數
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            if (mapdata.size() != 0) {
                // 將mapdata中的key存在set集合中，通過迭代器取出所有的key，再獲取每一個鍵對應的值
                Set keySet = mapdata.keySet();
                Iterator it = keySet.iterator();
                while (it.hasNext()) {
                    String k = it.next().toString();// key
                    String v = mapdata.get(k);// value
                    nameValuePairs.add(new BasicNameValuePair(k, v));
                }
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            // 執行http請求
            response = httpClient.execute(httpPost);
            // 獲得http響應體
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // 響應的結果
                String content = EntityUtils.toString(entity, "UTF-8");
                return content;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "獲取數據錯誤";
    }
```


# Http Post request (json)

``` java
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";
    private static final String CHARSET = "charset";
    private static final String CHARSET_VALUE = "UTF-8";
    private static final String AUTHORIZATION = "Bearer";
    private static final String CALLAPI_RESULT = "200 完成";

    private Map<String, String> callTSBAPIByHttpClient(String url, String token, String param, BigDecimal pid_num) throws IOException {
        //創建HttpClient
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //創建POST request
        HttpPost httpPost = new HttpPost(url);
        //設定header
        httpPost.setHeader(HttpConnection.CONTENT_TYPE, ExternalProductWriter.CONTENT_TYPE_VALUE);
        httpPost.setHeader(ExternalProductWriter.CHARSET, ExternalProductWriter.CHARSET_VALUE);
        httpPost.setHeader(ExternalProductWriter.AUTHORIZATION, token);
        //設定body參數
        StringEntity stringEntity = new StringEntity(param);
        httpPost.setEntity(stringEntity);
        //執行
        HttpResponse httpResponse = httpclient.execute(httpPost);

        //取得狀態碼
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        //取得body
        HttpEntity entity = httpResponse.getEntity();
        String responseBody = getResponseBodyAsString(entity.getContent(), "UTF-8");
        Map<String, String> map = new HashMap<>();
        map.put("statusCode", String.valueOf(statusCode));
        map.put("responseBody", responseBody);
        return map;
    }

    private static String getResponseBodyAsString(InputStream input, String encode) throws IOException {

        if (StringUtils.isBlank(encode)) {
            encode = "UTF-8";
        }

        StringWriter writer = new StringWriter();
        InputStreamReader reader = new InputStreamReader(input, encode);
        char[] buffer = new char[1024 * 4];

        int n = reader.read(buffer);
        while (n != -1) {
            writer.write(buffer, 0, n);
            n = reader.read(buffer);
        }
        String res = writer.toString();
        writer.close();
        return res;
    }
```
# bypass ssl

```java
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

            }

            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };

        sc.init(null, new TrustManager[] {
                trustManager
        }, null);
        return sc;
    }
```

# httpPost bypass ssl


```java
 public static void main(String[] args) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException {
        String url = "https:/XXX/XXX/api/XXX/XXX";
        Map<String, String> map = new HashMap<String, String>();
        map.put("XX", "XXX");
        map.put("XXX", "XX");
        String encoding = "UTF-8";
        String body = "";
        //採用繞過驗證的方式處理https請求
        SSLContext sslcontext = createIgnoreVerifySSL();

        //設置協議http和https對應的處理socket鏈接工廠物件
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);

        //创建自定义的httpclient对象
        //創建自訂義的httpclient
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();
        //      CloseableHttpClient client = HttpClients.createDefault();

        //創建post方式請求物件
        HttpPost httpPost = new HttpPost(url);

        //裝填參數
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (map != null) {
            for (Entry<String, String> entry : map.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        //設置參數到情求物件中
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
        RequestConfig config = RequestConfig
                .custom()
                .setCookieSpec("XXX")
                .build();

        httpPost.setConfig(config);

        //設置header信息
        //指定報文頭【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        httpPost.setHeader("Cookie", "XXX");
        httpPost.setHeader("Authorization", "XXX");
        //執行請求操作，並拿到結果(同步阻塞)
        CloseableHttpResponse response = client.execute(httpPost);
        //獲取結果實體
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定編碼轉換結果實體為String類型
            body = EntityUtils.toString(entity, "UTF-8");
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        System.out.println(body);
        //        return body;
    }
```

