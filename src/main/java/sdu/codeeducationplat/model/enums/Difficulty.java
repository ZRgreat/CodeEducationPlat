package sdu.codeeducationplat.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Difficulty {
    EASY("EASY", "简单"),
    MEDIUM("MEDIUM", "中等"),
    HARD("HARD", "困难");

    @EnumValue
    private final String value;

    private final String description;

    Difficulty(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Difficulty fromValue(String value) {
        if (value == null) return null;
        for (Difficulty difficulty : values()) {
            if (difficulty.value.equalsIgnoreCase(value)) {
                return difficulty;
            }
        }
        throw new IllegalArgumentException("Unknown difficulty: " + value);
    }
}