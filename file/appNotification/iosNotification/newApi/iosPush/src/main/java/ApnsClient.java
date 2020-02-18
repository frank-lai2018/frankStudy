
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.util.resource.Resource;

public class ApnsClient {

    private final static String KEY_STORE_PASSWORD = "a123456";
    private final static URL KEY_STORE_PATH = ApnsClient.class.getResource("COMMON/config/apns.p12");

    //    private final static String URL_FORMATTER = "https://api.push.apple.com/3/device/%s";
    private final static String URL_FORMATTER = "https://api.push.apple.com/3/device/%s";
    private final static String CONTENT_TYPE = "application/json";
    private final static Charset ENCODING = Charset.forName("UTF-8");
    //    private final static String PAYLOAD = "{\"aps\":{\"alert\":{\"title\":\"TEST\",\"body\":\"TEST\"}}}";
    //    private final static String PAYLOAD = "{\"aps\" : {\"content-available\" : 1}}";
    //    private final static String PAYLOAD = "{\"aps\" : {\"content-available\" : 1}}";

    /*HOME*/
    private final static String PAYLOAD = "{\"aps\":{\"alert\":\"只有今天！Pizza Hut享優惠\",\"sound\":\"default\"},\"openView\":{\"view\":\"Home\",\"notificationId\":\"1\"}}";

    public void verify(final String deviceToken) throws Exception {
        final HttpClient httpClient = new HttpClientBuilder()
                .withKeyStoreResource(Resource.newResource(KEY_STORE_PATH))
                .withKeyStorePassword(KEY_STORE_PASSWORD)
                .build();

        try {
            httpClient.start();

            final Request request = httpClient
                    .POST(String.format(URL_FORMATTER, deviceToken))
                    .header("apns-topic", "tw.payeasy.com.NyxAppid")
                    .header("apns-expiration", "0")
                    .header("apns-priority", "10")
                    .header("apns-push-type", "alert")//alert background
                    .content(new StringContentProvider(CONTENT_TYPE, PAYLOAD, ENCODING));

            final ContentResponse response = request.send();

            System.out.println("statusCode = " + response.getStatus() + ", contentBody = " + response.getContentAsString());
            System.out.println("Reason = " + response.getHeaders().get("apns-id") + ", MediaType = " + response.getMediaType());
            System.out.println("Headers = " + response.getHeaders());
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.stop();
        }
    }

    public static void main(final String[] args) throws Exception {
        String aa = "<c10fc24e7 32cbc3a10 5d075e0bdbbe929 074901e3394cf 7fe17ae da3c3d2cc2q>";
        String replaceEach = StringUtils.replaceEach(aa, new String[] {
                " ", "<", ">"
        }, new String[] {
                "", "", ""
        });
        System.out.println("222:" + replaceEach);
        StringBuilder sb = new StringBuilder(aa);
        Pattern p = Pattern.compile(" ");
        Matcher m = p.matcher(aa);
        StringBuffer sbs = new StringBuffer();

        while (m.find()) {
            m.appendReplacement(sbs, "");
        }

        m.appendTail(sbs);
        System.out.println("1111:" + sbs.toString());

        final ApnsClient client = new ApnsClient();
        client.verify("0bf8cc232e46338a4a80026d2e1d9360987dad6b4052ae373da45713953be40b");
    }

}
