package io.modicon.priceservice.application.service;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SchedulerService {

    private final PriceSetterService priceSetterService;

    @Scheduled(fixedDelay = 5000)
    public void updatePrices() {
        priceSetterService.updatePrices();
    }
}
