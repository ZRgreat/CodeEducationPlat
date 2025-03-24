package sdu.codeeducationplat;

import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@OpenAPIDefinition(info = @Info(title = "CodeEducationPlat API", version = "1.0", description = "API for Code Education Platform"))
@MapperScan("sdu.codeeducationplat.mapper")
@EnableTransactionManagement
public class CodeEducationPlatApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeEducationPlatApplication.class, args);
    }

}