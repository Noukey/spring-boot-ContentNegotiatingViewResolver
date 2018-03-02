package thymeleaf;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

	private static final String UTF8 = "UTF-8";

	private String[] array(String... args) {
		return args;
	}

	/**
	 * Total customization - see below for explanation.
	 */
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		// Simple strategy: only path extension is taken into account
		configurer.favorPathExtension(true).ignoreAcceptHeader(true).useJaf(false)
				.defaultContentType(MediaType.TEXT_HTML).mediaType("html", MediaType.TEXT_HTML)
				.mediaType("json", MediaType.APPLICATION_JSON);
	}

	@Bean
	public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager,
			JsonViewResolver jsonViewResolver, ThymeleafViewResolver thymeleafViewResolver) {
		List<ViewResolver> resolvers = new ArrayList<ViewResolver>();
		resolvers.add(jsonViewResolver);
		resolvers.add(thymeleafViewResolver);

		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		resolver.setContentNegotiationManager(manager);
		resolver.setViewResolvers(resolvers);
		return resolver;
	}

	@Bean
	public ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setPrefix("classpath:/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setCharacterEncoding("utf-8");
		templateResolver.setOrder(1);

		templateResolver.setCacheable(false);
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		return templateEngine;
	}

	@Bean
	public ThymeleafViewResolver htmlViewResolver(SpringTemplateEngine templateEngine) {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine);
		resolver.setContentType("text/html");
		resolver.setCharacterEncoding(UTF8);
		resolver.setViewNames(array("*.html"));
		resolver.setCache(false);
		return resolver;
	}

	@Bean
	public JsonViewResolver getJsonViewResolver() {
		return new JsonViewResolver();
	}

	private static class JsonViewResolver implements ViewResolver {

		@Override
		public View resolveViewName(String arg0, Locale arg1) throws Exception {
			MappingJackson2JsonView view = new MappingJackson2JsonView();
			view.setPrettyPrint(true);
			return view;
		}

	}

}