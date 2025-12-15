package com.mines.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * AES加密配置（文档要求：AES-128-CBC-PKCS7Padding，IV为16个0）
 */
@Component
@ConfigurationProperties(prefix = "aes")
public class AesConfig {
    private int keySize; // 128
    private int blockSize; // 16
    private String mode; // CBC
    private String iv; // 16个0的向量
    private String padding; // PKCS7Padding
    private String appId; // PKCS7Padding
    private Map<String, String> serviceKeyMap; // serviceId与secretKey映射

    // Getter和Setter
    public int getKeySize() { return keySize; }
    public void setKeySize(int keySize) { this.keySize = keySize; }
    public int getBlockSize() { return blockSize; }
    public void setBlockSize(int blockSize) { this.blockSize = blockSize; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public String getIv() { return iv; }
    public void setIv(String iv) { this.iv = iv; }
    public String getPadding() { return padding; }
    public void setPadding(String padding) { this.padding = padding; }
    public String getAppId() { return appId; }
    public void setAppId(String appId) { this.appId = appId; }
    public Map<String, String> getServiceKeyMap() { return serviceKeyMap; }
    public void setServiceKeyMap(Map<String, String> serviceKeyMap) { this.serviceKeyMap = serviceKeyMap; }
}
