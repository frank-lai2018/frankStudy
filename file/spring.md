# 1.獲取ApplicationContext 方式

  a.使用WebApplicationContextUtils

```java
ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
```

b.

# 處理fileUpload IE getOriginalFilename 取檔名跟其他瀏覽器不一樣問題


```java
	@RequestMapping(value = "/apple",method = RequestMethod.POST)
	public void file(MultipartFile  apple) {
		System.err.println(apple.getOriginalFilename());
		String fileName = apple.getOriginalFilename();
		
		int startIndex = fileName.replaceAll("\\\\", "/").lastIndexOf("/");
		fileName = fileName.substring(startIndex + 1);
		System.err.println("111111111::"+fileName);
		System.err.println(apple.getName());
	}
```



