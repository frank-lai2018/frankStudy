gradle

```
compile 'org.jsoup:jsoup:1.12.1'
```

maven

```
<!-- https://mvnrepository.com/artifact/org.jsoup/jsoup -->
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.12.1</version>
</dependency>

```

使用Jsoup發送http request post(json)

```java

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";
    private static final String CHARSET = "charset";
    private static final String CHARSET_VALUE = "UTF-8";
    private static final String AUTHORIZATION = "Bearer";
    private static final String CALLAPI_RESULT = "200 完成";

// call TSB 商品上架API
    private String callAPI(String url, String token, String param, BigDecimal pid_num) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put(HttpConnection.CONTENT_TYPE, ExternalProductWriter.CONTENT_TYPE_VALUE);
        map.put(ExternalProductWriter.CHARSET, ExternalProductWriter.CHARSET_VALUE);
        map.put(ExternalProductWriter.AUTHORIZATION, token);

        Response execute = Jsoup.connect(url)
                .headers(map)
                .method(Method.POST)
                .requestBody(param)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .execute();
        this.logger.info("pid_num= {} ,statusCode= {} ,body= {}", pid_num, execute.statusCode(), execute.body());
        return execute.statusCode() + execute.body();
        //        return execute.statusCode() == 200 ? ExternalProductWriter.CALLAPI_RESULT : execute.statusCode() + execute.body();
    }
```

# Jsoup body預設為1M，如body超過1M需使用 進行設定


```java
        Response execute = Jsoup.connect(url)
                .headers(map)
                .maxBodySize(1024 * 1024 * 20)//20M，0為無限制
                .method(Method.POST)
                .requestBody(param)
                .ignoreHttpErrors(true)
                .ignoreContentType(true)
                .execute();
```