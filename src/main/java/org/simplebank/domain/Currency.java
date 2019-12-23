package org.simplebank.domain;

public enum Currency {
    EUR("EUR"),
    USD("USD"),
    JPY("JPY"),
    GBP("GBP");

    Currency(String currencyName) {
        this.currencyName = currencyName;
    }

    private String currencyName;

    public String getCurrencyName() {
        return currencyName;
    }

}
