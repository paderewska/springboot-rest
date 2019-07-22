package com.springboot.rest.services;

import com.springboot.rest.entities.Site;
import com.springboot.rest.json.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GeocoderService {

    private static final String BASE =
            "https://maps.googleapis.com/maps/api/geocode/json";
    private static final String KEY =
            "AIzaSyDw_d6dfxDEI7MAvqfGXEIsEMwjC1PWRno";

    private RestTemplate restTemplate;

    @Autowired
    public GeocoderService(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    private String encodeString(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public Site getLatLng(String... adress) {
        String encodedAddress = Stream.of(adress)
                .map(this::encodeString)
                .collect(Collectors.joining(","));
        String url = String.format("%s?address=%s&key=%s", BASE, encodedAddress, KEY);
        Response response = restTemplate.getForObject(url, Response.class);
        return new Site(response.getFormattedAddress(),
                response.getLocation().getLat(),
                response.getLocation().getLng());
    }
}
