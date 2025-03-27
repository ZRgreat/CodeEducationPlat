package sdu.codeeducationplat.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter; //自动生成getCode()和getDescription()方法

@Getter
public enum CertificationStatus {

    PENDING("PENDING", "待审核"),
    APPROVED("APPROVED", "已通过"),
    REJECTED("REJECTED", "已拒绝");

    @EnumValue
    private final String value;
    private final String description;

    CertificationStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
