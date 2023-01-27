package io.modicon.openfigiservice.application.client.openfigi;

import io.modicon.openfigiservice.domain.model.OpenFigiStock;

import java.util.Set;

public record GetTickerResponse(
        Set<OpenFigiStock> data,
        String error,
        String warning
) {
}
