package org.simplebank.services;

import org.simplebank.common.Configs;
import org.simplebank.domain.Currency;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * A simple static currency exchange logic. only for demo!
 */
public class SimpleCurrencyExchange implements CurrencyExchange {

    @Override
    public Float euroPound(Float amount) {
        Float rate = Float.valueOf(Configs.getProperty("euro_to_pound", "1F"));
        return amount / rate;
    }

    @Override
    public Float poundEuro(Float amount) {
        Float rate = Float.valueOf(Configs.getProperty("pound_to_euro", "1F"));
        return amount / rate;
    }

    @Override
    public Float exchange(Float amount, Currency from, Currency to) {
        //TODO
        throw new NotImplementedException();
    }


}
