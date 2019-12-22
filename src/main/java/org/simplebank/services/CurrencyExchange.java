package org.simplebank.services;

import org.simplebank.domain.Currency;

public interface CurrencyExchange {
    public Float euroPound(Float amount);
    public Float poundEuro(Float amount);
    public Float exchange(Float amount, Currency from, Currency to);

}
