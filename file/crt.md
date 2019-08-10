# **windows JAVA 匯入憑證方式**

指令:

keytool -import -v -trustcacerts -alias "憑證名子" -file "需匯入憑證的位子" -keystore "JRE cacerts 位子"

```console
keytool -import -v -trustcacerts -alias "opendata_cwb_gov_tw.crt" -file "opendata_cwb_gov_tw.crt" -keystore "C:\Program Files\Java\jre1.8.0_161\lib\security\cacerts"
```

![013](images/pic013.png)