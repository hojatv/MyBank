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


public class MybankApiImpl implements MybankApi {

    private final CustomerService customerService;
    private final ServiceFactory serviceFactory;
    private final MoneyService moneyService;
    private final Gson gson = new Gson();
    private final int OK = 200;
    private final int CONFLICT = 409;
    private final int ERROR = 500;

    public MybankApiImpl() {
        serviceFactory = new ServiceFactoryImpl();
        moneyService = serviceFactory.makeMoneyService();
        customerService = serviceFactory.makeCustomerService();
    }


    @Override
    public void registerGetCustomersAPI() {
        get("/mybank/customer-management/customers", (request, response) -> {
            setResponseType(response, "application/json");
            Collection<Customer> customers;
            try {
                customers = customerService.getCustomers();
                setResponseStatus(response, OK);
                return toResponse(customers);
            } catch (Exception ex) {
                response.status(ERROR);
                return toResponse("Problem While getting customers");

            }
        });
    }

    @Override
    public void registerFindBalanceForAccountIdAPI() {
        get("/mybank/transfer-management/balance/:accountId", (request, response) -> {
            setResponseType(response, "application/json");
            String accountId = request.params(":accountId");
            try {
                setResponseStatus(response, OK);
                return toResponse(moneyService.getBalancesByAccountId(accountId));
            } catch (Exception ex) {
                setResponseStatus(response, ERROR);
                return toResponse("Problem While getting balance list for account  : "
                        + accountId + " . More info: " + ex.getMessage());
            }
        });
    }

    @Override
    public void registerTransferAPI() {
        post("/mybank/transfer-management/transfer", (request, response) -> {
            setResponseType(response, "application/json");
            MoneyTransferDTO moneyTransferDTO;
            try {
                moneyTransferDTO = gson.fromJson(request.body(), MoneyTransferDTO.class);
                TransferDetail transferDetail = moneyService.transfer(moneyTransferDTO);
                if (transferDetail.getStatus().equals(Status.SUCCESS)) {
                    setResponseStatus(response, OK);
                    return toResponse("Transfer Completed!");
                } else {
                    setResponseStatus(response, CONFLICT);
                    return toResponse(transferDetail.getErrorMessage());
                }
            } catch (Exception ex) {
                setResponseStatus(response, ERROR);
                return toResponse("Problem While transferring money. More info: ")
                        + ex.getMessage();
            }
        });
    }

    private JsonElement toResponse(Object response) {
        return gson.toJsonTree(response);
    }

    private void setResponseType(spark.Response response, String type) {
        response.type(type);
    }

    private void setResponseStatus(spark.Response response, int i) {
        response.status(i);
    }

}
