# 從Cookie[]取得想要的cookie

```java
import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.Cookie;
	
    
    private Cookie getCookie(Cookie[] cookies,String name) {
		if(cookies == null || cookies.length == 0) {
			return null;
		}
		Optional<Cookie> findFirst = 
				Arrays.stream(cookies).filter(cookie -> name.equals(cookie.getName())).findFirst();
		if(findFirst.isPresent()) {
			return findFirst.get();
		}
		return null;
	}
```