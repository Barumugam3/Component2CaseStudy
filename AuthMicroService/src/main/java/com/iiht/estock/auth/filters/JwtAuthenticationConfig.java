package com.iiht.estock.auth.filters;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

@Data
@ToString
public class JwtAuthenticationConfig {

    @Value(value = "${api.security.jwt.url:/login}")
    private String url;

    @Value(value = "${api.security.jwt.header:Authorization}")
    private String header;

    @Value(value = "${api.security.jwt.prefix:Bearer}")
    private String prefix;

    @Value(value = "${api.security.jwt.expiration:#{24*60*60}}")
    private int expiration;

    @Value(value = "${api.security.jwt.secret}")
    private String secret;
}
