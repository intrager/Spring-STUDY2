package com.study.templateapi.global.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

    @Value("${jasypt.password}")
    public String password;

    /**
     * PooledPBEStringEncryptor : 멀티코어 시스템에 해독을 병렬처리하는 Encryptor
     * 보통은 멀티 코어를 사용하니까 StandardPBEString Encryptor 라는 클래스를 사용할 때보다
     * 성능이 좋음
     */
    @Bean
    public PooledPBEStringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setPoolSize(4);   // 머신의 host와 동일하게 설정하는 게 좋음
        encryptor.setPassword(password);
        encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
        return encryptor;
    }
}
