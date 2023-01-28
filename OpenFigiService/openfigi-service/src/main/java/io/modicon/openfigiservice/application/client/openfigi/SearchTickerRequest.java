package io.modicon.openfigiservice.application.client.openfigi;

public record SearchTickerRequest(
        String query,
        String exchCode
) {
}
