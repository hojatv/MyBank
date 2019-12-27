package org.simplebank.services;

import org.simplebank.domain.Currency;

public interface CurrencyExchange {
    Float euroPound(Float amount);
    Float poundEuro(Float amount);
    Float exchange(Float amount, Currency from, Currency to);

}
