package cn.com.itcast.sport.config;

import cn.com.itcast.sport.filter.LoginFiler;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @description: 跨域过滤器
 * @author: JIAWEI
 * @date: Created in 2020/3/14 20:42
 * @version: 1.0
 * @modified By:
 */
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {
    public static String ADDRESS_TEST = "*";
    /*设置跨域*/
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*/**").allowedHeaders("*").allowedMethods("*").maxAge(1800).allowedOrigins(ADDRESS_TEST);
    }
    //实现拦截器 要拦截的路径以及不拦截的路径
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(new LoginFiler()).addPathPatterns("/**")
                .excludePathPatterns("/static/**","/error","/loginPage","/isLogin",
                        "/uploadFile","/api/**","/swagger-ui.html",
                        "/webjars/**","/swagger-resources/**","/v2/**");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        config.setCharset(Charset.forName("UTF-8"));
        config.setSerializerFeatures(
                SerializerFeature.WriteClassName,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty

        );
        converter.setFastJsonConfig(config);
        converters.add(converter);
    }
}
