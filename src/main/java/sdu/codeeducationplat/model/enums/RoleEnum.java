package sdu.codeeducationplat.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum RoleEnum {
    USER("user"),  // 普通用户
    STUDENT("student"),
    TEACHER("teacher");  // 教师
    @EnumValue // 标记存储到数据库的字段
    private final String value;

    RoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RoleEnum fromValue(String value) {
        for (RoleEnum role : values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}