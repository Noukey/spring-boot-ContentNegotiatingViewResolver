# spring-boot-ContentNegotiatingViewResolver
spring-boot 多视图示例，同一个函数通过访问后缀调用不同视图解析器。

```java
@Controller
public class helloController {
	
	@RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
}
``` 
同一个函数不同的访问方式返回不同的视图。
`http://localhost:8080/greeting` `http://localhost:8080/greeting.html`  返回html  Hello, World!
`http://localhost:8080/greeting.json` 返回json视图
```
{
  "name" : "World"
}
```
