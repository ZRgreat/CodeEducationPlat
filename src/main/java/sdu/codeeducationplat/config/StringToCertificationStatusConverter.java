package sdu.codeeducationplat.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sdu.codeeducationplat.model.enums.CertificationStatus;

@Component
public class StringToCertificationStatusConverter implements Converter<String, CertificationStatus> {

    @Override
    public CertificationStatus convert(String source) {
        if (source == null) {
            return null;
        }
        for (CertificationStatus status : CertificationStatus.values()) {
            if (status.getDescription().equals(source)) {
                return status;
            }
            // 同时支持 name() 和 value()
            if (status.name().equals(source) || status.getValue().equals(source)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid CertificationStatus value: " + source);
    }
}