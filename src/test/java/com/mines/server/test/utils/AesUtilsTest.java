package com.mines.server.test.utils;

import com.mines.server.util.AesUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class AesUtilsTest {

    private static final String AES_KEY = "minesv6key123456"; // 16位密钥（符合AES-128）
    private static final String PLAINTEXT = "{\"deviceId\":\"test001\",\"value\":25.6}"; // 测试明文
    private static final String iv = "0000000000000000";

    @Test
    public void testEncryptDecrypt() throws Exception {
        // 加密
        String encrypted = AesUtils.encrypt(PLAINTEXT, AES_KEY,iv);
        assertNotNull(encrypted);

        // 解密（验证一致性）
        String decrypted = AesUtils.decrypt(encrypted, AES_KEY,iv);
        assertEquals(PLAINTEXT, decrypted);
    }

    @Test(expected = Exception.class)
    public void testDecrypt_InvalidKey() throws Exception {
        // 异常场景：密钥错误
        String encrypted = AesUtils.encrypt(PLAINTEXT, AES_KEY,iv);
        AesUtils.decrypt(encrypted, "wrongkey123456",iv); // 错误密钥，预期抛出异常
    }

    @Test(expected = Exception.class)
    public void testEncrypt_InvalidKeyLength() throws Exception {
        // 异常场景：密钥长度不足16位（AES-128要求16位）
        AesUtils.encrypt(PLAINTEXT, "shortkey",iv); // 8位密钥，预期抛出异常
    }
}
