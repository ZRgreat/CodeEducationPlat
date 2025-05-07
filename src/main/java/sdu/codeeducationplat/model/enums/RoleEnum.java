package sdu.codeeducationplat.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum RoleEnum {
    USER("user", "用户"),
    STUDENT("student", "学生"),
    TEACHER("teacher", "教师"),
    ADMIN("admin", "管理员");

    @EnumValue
    private final String value;
    private final String description;

    RoleEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RoleEnum fromValue(String value) {
        if (value == null) return null;
        for (RoleEnum role : values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role value: " + value);
    }

    public static RoleEnum fromDescription(String description) {
        if (description == null) return null;
        for (RoleEnum role : values()) {
            if (role.description.equals(description)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role description: " + description);
    }

    @Override
    public String toString() {
        return value;
    }
}