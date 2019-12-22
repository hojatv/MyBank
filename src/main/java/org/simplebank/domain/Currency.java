package org.simplebank.domain;

public enum Currency {
    EUR("EUR"),
    USD("USD"),
    JPY("JPY"),
    GPD("GPD");

    Currency(String currency) {
        this.currency = currency;
    }

    private String currency;

    public String getCurrency() {
        return currency;
    }

}
