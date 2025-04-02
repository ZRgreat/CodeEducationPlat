package sdu.codeeducationplat.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter; //自动生成getCode()和getDescription()方法

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
    public String getDescription() {
        return description;
    }
}
