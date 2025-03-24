package sdu.codeeducationplat.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter; //自动生成getCode()和getDescription()方法

@Getter
public enum CertificationStatus {

    REJECTED(0, "已拒绝"),
    APPROVED(1, "已通过"),
    PENDING(2,"待审核");

    @EnumValue  // MyBatis-Plus 存储时使用该字段的值
    private final int code;  // 数据库存储的值
    private final String description;  // 用于前端显示的解释

    CertificationStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
