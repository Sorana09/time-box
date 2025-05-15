package com.example.time.box.metrics;

import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.css.Counter;


@RequiredArgsConstructor
@Service
public class ViewUserMetric {
    private final Meter meter;

     public static final AttributeKey<String> USER_ID_ATTRIBUTE = AttributeKey.stringKey("users");

     public void registerViewForUser(Integer userId){
         LongCounter counter = meter.counterBuilder("user.views")
                 .setDescription("views per user")
                 .setUnit("{view}")
                 .build();
         counter.add(1L,Attributes.of(USER_ID_ATTRIBUTE,userId.toString()));
     }

}
