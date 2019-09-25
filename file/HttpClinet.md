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

Http Post request (json)

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

