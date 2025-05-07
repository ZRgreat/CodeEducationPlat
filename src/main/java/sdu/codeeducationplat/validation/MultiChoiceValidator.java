package sdu.codeeducationplat.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sdu.codeeducationplat.dto.question.QuestionDTO;
import sdu.codeeducationplat.model.enums.QuestionType;

public class MultiChoiceValidator implements ConstraintValidator<MultiChoiceConstraint, QuestionDTO> {
    @Override
    public boolean isValid(QuestionDTO dto, ConstraintValidatorContext context) {
        if (dto.getType() == null || !dto.getType().equals(QuestionType.MULTI)) {
            return true; // 不是多选题，不需要验证
        }
        if (dto.getChoices() == null || dto.getChoices().isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("多选题必须包含选项")
                    .addPropertyNode("choices")
                    .addConstraintViolation();
            return false;
        }
        long correctCount = dto.getChoices().stream()
                .filter(choice -> choice.getIsCorrect() != null && choice.getIsCorrect())
                .count();
        if (correctCount < 1) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("多选题至少有一个正确选项")
                    .addPropertyNode("choices")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}