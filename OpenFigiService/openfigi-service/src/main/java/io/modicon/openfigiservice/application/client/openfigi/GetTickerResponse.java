package io.modicon.openfigiservice.application.client.openfigi;

import io.modicon.openfigiservice.domain.model.OpenFigiStock;

import java.util.List;

public record GetTickerResponse(
        List<OpenFigiStock> data,
        String error,
        String warning
) {
}
