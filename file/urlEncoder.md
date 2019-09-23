1.Java 本身的 **java.net.URLEncoder** 與 **java.net.URLDecoder**可以進行URL的百分比編碼跟解碼

java.net.URLEncoder:

```java
    @Test
    public void test() {
        try {
            String encode = URLEncoder.encode("\"家電\"", "UTF-8");
            System.out.println(encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
```



輸出結果:

``` 
%22%E5%AE%B6%E9%9B%BB%22
```

java.net.URLDecoder:

```java
    @Test
    public void test1() {
        try {
            String decode = URLDecoder.decode("%22%E5%AE%B6%E9%9B%BB%22", "UTF-8");
            System.out.println(decode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
```

輸出結果:

```
"家電"
```

