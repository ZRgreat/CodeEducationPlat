package sdu.codeeducationplat.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum CertificationStatus implements IEnum<String> {
    PENDING("pending", "待审核"),
    APPROVED("approved", "已通过"),
    REJECTED("rejected", "已拒绝");

    @EnumValue
    private final String value;
    private final String description;

    CertificationStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static CertificationStatus fromValue(String value) {
        if (value == null) return null;
        for (CertificationStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid CertificationStatus value: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}