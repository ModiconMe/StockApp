package io.modicon.moexservice.application.service;

import io.modicon.moexservice.domain.model.Bond;

import java.util.List;

public interface BondParser {
    List<Bond> parse(String ratesAsString);
}
