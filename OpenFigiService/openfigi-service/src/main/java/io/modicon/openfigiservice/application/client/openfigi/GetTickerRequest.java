package io.modicon.openfigiservice.application.client.openfigi;

public record GetTickerRequest(
        String idType,
        String idValue,
        String exchCode
) {
}
