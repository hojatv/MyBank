package org.simplebank.controller.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.simplebank.domain.*;
import org.simplebank.controller.MybankApi;
import org.simplebank.services.CustomerService;
import org.simplebank.services.MoneyService;
import org.simplebank.services.ServiceFactory;
import org.simplebank.services.impl.ServiceFactoryImpl;

import java.util.Collection;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Demo implementation of Mybank using Spark Framework
 */
public class MybankApiImpl implements MybankApi {

    private final CustomerService customerService;
    private final ServiceFactory serviceFactory;
    private final MoneyService moneyService;

    public MybankApiImpl() {
        serviceFactory = new ServiceFactoryImpl();
        moneyService = serviceFactory.makeMoneyService();
        customerService = serviceFactory.makeCustomerService();
    }


    @Override
    public void getCustomers() {
        get("/mybank/customer-management/customers", (request, response) -> {
            response.type("application/json");
            Collection<Customer> customers;
            try {
                customers = customerService.getCustomers();
            } catch (Exception ex) {
                return toJsonElement(new Response(Status.ERROR, "Problem While getting customers"));

            }
            return toJsonElement(new Response(Status.SUCCESS, new Gson().toJsonTree(customers)));
        });
    }

    @Override
    public void findBalanceForAccountId() {
        get("/mybank/transfer-management/balance/:accountId", (request, response) -> {
            response.type("application/json");
            String accountId = request.params(":accountId");
            try {
                JsonElement data = toJsonElement(moneyService.getBalancesByAccountId(accountId));
                return toJsonElement(new Response(Status.SUCCESS, data));
            } catch (Exception ex) {
                return toJsonElement(new Response(Status.ERROR, "Problem While getting balance list for account  : "
                        + accountId + " . More info: " + ex.getMessage()));
            }
        });
    }

    @Override
    public void transfer() {
        post("/mybank/transfer-management/transfer", (request, response) -> {
            response.type("application/json");
            MoneyTransferDTO moneyTransferDTO;
            try {
                moneyTransferDTO = new Gson().fromJson(request.body(), MoneyTransferDTO.class);
                TransferDetail transferDetail = moneyService.transfer(moneyTransferDTO);
                if (transferDetail.getStatus().equals(Status.SUCCESS)) {
                    return new Gson().toJson(new Response(Status.SUCCESS, new Gson().toJsonTree(transferDetail)));
                } else {
                    return new Gson().toJson(new Response(Status.ERROR, new Gson().toJsonTree(transferDetail.getErrorMessage())));
                }
            } catch (Exception ex) {
                return toJsonElement(new Response(Status.ERROR, "Problem While transferring money. More info: ")
                        + ex.getMessage());
            }
        });
    }

    private JsonElement toJsonElement(Object response) {
        return new Gson().toJsonTree(response);
    }
}
