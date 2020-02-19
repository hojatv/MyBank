package org.simplebank.controller.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.simplebank.domain.*;
import org.simplebank.controller.MybankApi;
import org.simplebank.services.CustomerService;
import org.simplebank.services.MoneyService;
import org.simplebank.services.ServiceFactory;
import org.simplebank.services.impl.ServiceFactoryImpl;
import spark.ResponseTransformer;

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
    private Gson gson = new Gson();

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
                response.status(200);
                //response.body(String.valueOf(toJsonElement(new Response(Status.SUCCESS, gson.toJsonTree(customers)))));
                response.body(String.valueOf(toJsonElement(customers)));
            } catch (Exception ex) {
                response.status(500);
                response.body(String.valueOf(toJsonElement(new Response(Status.ERROR, "Problem While getting customers"))));
            }
            return response.body();
        }, new JsonTransformer());
    }

    @Override
    public void findBalanceForAccountId() {
        get("/mybank/transfer-management/balance/:accountId", (request, response) -> {
            response.type("application/json");
            String accountId = request.params(":accountId");
            try {
                JsonElement data = toJsonElement(moneyService.getBalancesByAccountId(accountId));
                if (data.getAsJsonArray().size() > 0) {
                    response.status(200);
                    response.body(String.valueOf(data));

                } else {
                    response.status(404);
                    response.body("NotFound");
                }
            } catch (Exception ex) {
                response.status(500);
                response.body("Problem While getting balance list for account  : "
                        + accountId + " . More info: " + ex.getMessage());
            }
            return response.body();
        }, new JsonTransformer());

    }

    @Override
    public void transfer() {
        post("/mybank/transfer-management/transfer", (request, response) -> {
            response.type("application/json");
            MoneyTransferDTO moneyTransferDTO;
            try {
                moneyTransferDTO = gson.fromJson(request.body(), MoneyTransferDTO.class);
                TransferDetail transferDetail = moneyService.transfer(moneyTransferDTO);
                if (transferDetail.getStatus().equals(Status.SUCCESS)) {
                    response.status(200);
                    response.body(String.valueOf(new Response(Status.SUCCESS, gson.toJsonTree(transferDetail))));
                } else {
                    response.status(500);
                    response.body(String.valueOf(new Response(Status.ERROR, gson.toJsonTree(transferDetail.getErrorMessage()))));
                }
            } catch (Exception ex) {
                response.status(500);
                response.body(new Response(Status.ERROR, "Problem While transferring money. More info: ")
                        + ex.getMessage());
            }
            return response.body();
        }, new JsonTransformer());
    }

    private JsonElement toJsonElement(Object response) {
        return gson.toJsonTree(response);
    }

    class JsonTransformer implements ResponseTransformer {

        private Gson gson = new Gson();

        @Override
        public String render(Object model) {
            return gson.toJson(model);
        }

    }
}
