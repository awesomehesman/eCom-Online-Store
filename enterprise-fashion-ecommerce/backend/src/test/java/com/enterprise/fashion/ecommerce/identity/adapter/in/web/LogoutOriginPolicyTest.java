package com.enterprise.fashion.ecommerce.identity.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.mock.web.MockHttpServletRequest;
import static org.assertj.core.api.Assertions.assertThat;

class LogoutOriginPolicyTest {
    @ParameterizedTest
    @CsvSource(nullValues = "-", value = {
        "https://shop.example,-,-,true",
        "https://SHOP.example:443,-,same-origin,true",
        "-,https://shop.example/path?q=x,-,true",
        "-,https://shop.example/path,none,true",
        "-,-,same-origin,false",
        "null,https://shop.example/,same-origin,false",
        "https://evil.example,-,-,false",
        "https://shop.example,https://evil.example/,same-origin,false",
        "https://shop.example,-,cross-site,false",
        "https://shop.example,-,same-site,false",
        "https://shop.example,-,none,true",
        "https://shop.example,-,unknown,false",
        "https://shop.example/path,-,-,false",
        "https://shop.example:444,-,-,false",
        "https://shop.example,not-a-url,-,false",
        "https://shop.example,https://user@shop.example/,-,false"
    })
    void enforcesApprovedMatrix(String origin, String referer, String fetch, boolean expected) {
        var request = new MockHttpServletRequest();
        if (origin != null) request.addHeader("Origin", origin);
        if (referer != null) request.addHeader("Referer", referer);
        if (fetch != null) request.addHeader("Sec-Fetch-Site", fetch);
        assertThat(new LogoutOriginPolicy("https://shop.example").allows(request)).isEqualTo(expected);
    }

    @Test
    void failsClosedForConfigurationAndDuplicateSignals() {
        var request = new MockHttpServletRequest();
        request.addHeader("Origin", "https://shop.example");
        request.addHeader("Host", "shop.example");
        request.addHeader("Forwarded", "proto=https;host=shop.example");
        assertThat(new LogoutOriginPolicy("").allows(request)).isFalse();
        assertThat(new LogoutOriginPolicy("http://shop.example").allows(request)).isFalse();
        assertThat(new LogoutOriginPolicy("https://shop.example/").allows(request)).isFalse();
        for (String header : new String[] {"Origin", "Referer", "Sec-Fetch-Site"}) {
            var duplicate = new MockHttpServletRequest();
            duplicate.addHeader("Origin", "https://shop.example");
            duplicate.addHeader(header, "https://shop.example");
            duplicate.addHeader(header, "https://shop.example");
            assertThat(new LogoutOriginPolicy("https://shop.example").allows(duplicate)).isFalse();
        }
    }
}
