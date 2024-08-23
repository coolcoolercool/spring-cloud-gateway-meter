package com.zzw.gateway.filter;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class PrometheusGlobalFilter implements GlobalFilter, Ordered {
    private final MeterRegistry meterRegistry;

    @Autowired
    public PrometheusGlobalFilter(MeterRegistry meterRegistry){
        this.meterRegistry = meterRegistry;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("========================PrometheusGlobalFilter filter begin========================");
        Counter.builder("gateway.requests.total")
                .description("The total received requests")
                .tag("method", exchange.getRequest().getMethod().name())
                .tag("path", exchange.getRequest().getPath().toString())
                .register(this.meterRegistry)
                .increment();

        Timer.builder("gateway.requests.average")
                .description("The average received requests")
                .tag("method", exchange.getRequest().getMethod().name())
                .tag("path", exchange.getRequest().getPath().toString())
                .register(this.meterRegistry);

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    if(exchange.getResponse().getStatusCode() != null){
                        int statusCode = exchange.getResponse().getStatusCode().value();
                        log.info("Response StatusCode: {}",statusCode );
                        Counter.builder("gateway.responses.total")
                                .description("The total handled responses")
                                .tag("status", Integer.toString(statusCode))
                                .register(this.meterRegistry)
                                .increment();
                    }
                    log.info("========================PrometheusGlobalFilter filter end========================");
                }));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
