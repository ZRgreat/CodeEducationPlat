package sdu.codeeducationplat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sdu.codeeducationplat.model.enums.CertificationStatus;
import sdu.codeeducationplat.model.enums.Difficulty;
import sdu.codeeducationplat.model.enums.QuestionType;
import sdu.codeeducationplat.model.enums.RoleEnum;

import java.beans.PropertyEditorSupport;

@Configuration
@ControllerAdvice
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/swagger-resources/**")
                .addResourceLocations("classpath:/META-INF/resources/swagger-resources/");
        registry.addResourceHandler("/v3/api-docs/**")
                .addResourceLocations("classpath:/META-INF/resources/v3/api-docs/");
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 为 CertificationStatus 注册 PropertyEditor
        binder.registerCustomEditor(CertificationStatus.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                System.out.println("Converting CertificationStatus from text: " + text); // 添加日志
                setValue(CertificationStatus.fromValue(text));
            }

            @Override
            public String getAsText() {
                CertificationStatus status = (CertificationStatus) getValue();
                return status != null ? status.getValue() : "";
            }
        });

        // 为 RoleEnum 注册 PropertyEditor
        binder.registerCustomEditor(RoleEnum.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(RoleEnum.fromValue(text));
            }

            @Override
            public String getAsText() {
                RoleEnum role = (RoleEnum) getValue();
                return role != null ? role.getValue() : "";
            }
        });

        // 为 Difficulty 注册 PropertyEditor
        binder.registerCustomEditor(Difficulty.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(Difficulty.fromValue(text));
            }

            @Override
            public String getAsText() {
                Difficulty difficulty = (Difficulty) getValue();
                return difficulty != null ? difficulty.getValue() : "";
            }
        });

        // 为 QuestionType 注册 PropertyEditor
        binder.registerCustomEditor(QuestionType.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(QuestionType.fromValue(text));
            }

            @Override
            public String getAsText() {
                QuestionType type = (QuestionType) getValue();
                return type != null ? type.getValue() : "";
            }
        });
    }
}