package sdu.codeeducationplat.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
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

    @Override
    public String toString() {
        return value;
    }
}