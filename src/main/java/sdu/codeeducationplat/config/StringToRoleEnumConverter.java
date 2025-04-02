package sdu.codeeducationplat.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sdu.codeeducationplat.model.enums.RoleEnum;

@Component
public class StringToRoleEnumConverter implements Converter<String, RoleEnum> {

    @Override
    public RoleEnum convert(String source) {
        if (source == null) {
            return null;
        }
        try {
            return RoleEnum.fromDescription(source);
        } catch (IllegalArgumentException e) {
            return RoleEnum.fromValue(source);
        }
    }
}