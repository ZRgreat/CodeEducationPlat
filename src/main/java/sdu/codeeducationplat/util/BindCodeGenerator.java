package sdu.codeeducationplat.util;

import java.security.SecureRandom;
import java.util.Base64;

public class BindCodeGenerator {

    private static final SecureRandom random = new SecureRandom();
    // 生成绑定码
    public static String generateBindCode(int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, length);
    }

    // 教师绑定码，6 位
    public static String generateTeacherBindCode() {
        return generateBindCode(6);
    }

    // 班级绑定码，8 位
    public static String generateClassBindCode() {
        return generateBindCode(8);
    }
}