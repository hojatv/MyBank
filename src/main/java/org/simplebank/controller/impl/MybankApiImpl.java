package org.simplebank.controller.impl;

import com.google.gson.Gson;
import org.simplebank.controller.MybankApi;
import org.simplebank.domain.Customer;
import org.simplebank.services.CustomerService;
import org.simplebank.common.Response;
import org.simplebank.common.Status;

import java.util.Collection;

import static org.simplebank.common.Configs.getProperty;
import static spark.Spark.get;

public class MybankApiImpl implements MybankApi {

    private final CustomerService customerService;

    public MybankApiImpl() {
        customerService = new CustomerService();
    }

    @Override
    public void balanceHandler() {
    }


    @Override
    public void getAllCustomers() {
        get(getProperty("getAllCustomersPath"), (request, response) -> {
            response.type("application/json");
            Collection<Customer> customers;
            try {
                customers = customerService.getCustomers();
            } catch (Exception ex) {
                return new Gson().toJson(new Response(Status.ERROR, "Problem While getting customers"));

            }
            return new Gson().toJson(new Response(Status.SUCCESS, new Gson().toJsonTree(customers)));
        });
    }
}
