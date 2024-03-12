package com.bartipablo.rest.query;

import com.bartipablo.rest.utils.QueryLimitExceeded;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RateLimit {

    private final Bucket bucket;

    public RateLimit() {
        Bandwidth limit = Bandwidth.classic(30, Refill.greedy(30, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    public void checkLimit() {
        if (!bucket.tryConsume(1)) throw new QueryLimitExceeded("Query limit exceeded");
    }
}


