# 1. Jackson  json 和 Object、List、Map 的互轉

```java
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MainTest {
    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        // User Object 轉 json
        User user1 = new User(123, "John");
        String json = objectMapper.writeValueAsString(user1);

        // json 轉 User Object
        User user2 = objectMapper.readValue(json, User.class);

        // List<User> 轉 json
        List<User> ulist = new ArrayList<>();
        User user4 = new User(123, "John");
        ulist.add(user4);
        String ujson = objectMapper.writeValueAsString(ulist);

        // json 轉 List<User>
        List<User> urlist = objectMapper.readValue(ujson, new TypeReference<List<User>>() {});

        // Map<String, User> 轉 json
        HashMap<String, User> umap = new HashMap<>();
        User user3 = new User(123, "John");
        umap.put("John", user3);
        String mjson2 = objectMapper.writeValueAsString(umap);

        // json 轉 Map<String, User>
        Map<String, User> urMap = objectMapper.readValue(mjson2, new TypeReference<HashMap<String, User>>() {});
    }
}

```

# 如果不是每個屬性都是必要值，可以在CLASS上加上@JsonIgnoreProperties(ignoreUnknown = true) 註解，這樣有值就會注入，沒有即不會注入不會抱錯
# 如果某個物件屬性，不需轉乘JSON，只需在屬性上加上 @JsonIgnore(true) 預設為true 不轉換 false 轉換

EX:
![060](images/pic060.png)


