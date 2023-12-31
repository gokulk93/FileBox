package com.tectot.filebox.configurations;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("filebox")
@Data
public class FileBoxConfig {

    private String environment;
    private Aws aws;

    @Data
    public static class Aws {
        private String endpoint;
        private String accessKey;
        private String secret;
        private String region;
    }
}
