package sdu.codeeducationplat.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum QuestionType {
    JUDGMENT("JUDGMENT", "判断题"),
    SINGLE("SINGLE", "单选题"),
    MULTI("MULTI", "多选题"),
    BLANK("BLANK", "填空题"),
    CODE_BLANK("CODE_BLANK", "程序填空题"),
    FUNCTION("FUNCTION", "函数题"),
    PROGRAM("PROGRAM", "编程题"),
    SUBJECTIVE("SUBJECTIVE", "主观题");

    @EnumValue // 映射到数据库的值
    private final String value;

    private final String description;

    QuestionType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue // 返回给前端的值
    public String getDescription() {
        return description;
    }

    public static QuestionType fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (QuestionType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown question type: " + value);
    }
}