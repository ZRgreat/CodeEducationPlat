package sdu.codeeducationplat.util;

import java.security.SecureRandom;
import java.util.Base64;

public class BindCodeGenerator {

    private static final SecureRandom random = new SecureRandom();
    private static final int CODE_LENGTH = 6; // 绑定码长度

    public static String generateBindCode() {
        byte[] bytes = new byte[CODE_LENGTH];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).substring(0, CODE_LENGTH);
    }
}