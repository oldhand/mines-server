package com.mines.server.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher; // 用于组合加密器和填充
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Base64;

import java.nio.charset.StandardCharsets;
import java.security.Security;

public class AesUtils {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final int BLOCK_SIZE = 16; // AES块大小固定16字节

    /**
     * 加密方法（3参数：明文、密钥、IV）
     */
    public static String encrypt(String plaintext, String key, String iv) throws Exception {
        try {
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);
            // 校验密钥和IV长度
            if (keyBytes.length != 16) {
                throw new IllegalArgumentException("AES-128密钥必须为16字节");
            }
            if (ivBytes.length != BLOCK_SIZE) {
                throw new IllegalArgumentException("IV必须为16字节");
            }

            // 1. 使用PaddedBufferedBlockCipher组合加密模式和填充方式（关键修正）
            PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
                    new CBCBlockCipher(new AESEngine()), // 加密模式：CBC
                    new PKCS7Padding() // 填充方式：PKCS#7（这里指定填充，而非ParametersWithIV）
            );

            // 2. ParametersWithIV仅传递密钥和IV（无需填充参数）
            cipher.init(true, new ParametersWithIV(
                    new KeyParameter(keyBytes), // 密钥
                    ivBytes // IV向量（仅2个参数，匹配构造器）
            ));

            // 3. 处理加密
            byte[] data = plaintext.getBytes(StandardCharsets.UTF_8);
            byte[] output = new byte[cipher.getOutputSize(data.length)];
            int length = cipher.processBytes(data, 0, data.length, output, 0);
            length += cipher.doFinal(output, length);

            return Base64.toBase64String(output, 0, length);
        } catch (Exception e) {
            throw new Exception("AES加密异常：" + e.getMessage(), e);
        }
    }

    /**
     * 解密方法（3参数：密文、密钥、IV）
     */
    public static String decrypt(String encryptedData, String key, String iv) throws Exception {
        try {
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);
            // 校验密钥和IV长度
            if (keyBytes.length != 16) {
                throw new IllegalArgumentException("AES-128密钥必须为16字节");
            }
            if (ivBytes.length != BLOCK_SIZE) {
                throw new IllegalArgumentException("IV必须为16字节");
            }

            // 1. 同样使用PaddedBufferedBlockCipher组合解密模式和填充
            PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
                    new CBCBlockCipher(new AESEngine()),
                    new PKCS7Padding()
            );

            // 2. ParametersWithIV仅传递密钥和IV
            cipher.init(false, new ParametersWithIV(
                    new KeyParameter(keyBytes),
                    ivBytes
            ));

            // 3. 处理解密
            byte[] cipherText = Base64.decode(encryptedData);
            byte[] output = new byte[cipher.getOutputSize(cipherText.length)];
            int length = cipher.processBytes(cipherText, 0, cipherText.length, output, 0);
            length += cipher.doFinal(output, length);

            return new String(output, 0, length, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new Exception("AES解密异常：" + e.getMessage(), e);
        }
    }
}
