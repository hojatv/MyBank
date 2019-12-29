package org.simplebank.repository.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import okhttp3.*;
import org.hamcrest.core.Is;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.simplebank.controller.ControllerFactory;
import org.simplebank.controller.MybankApi;
import org.simplebank.controller.impl.ControllerFactoryImpl;
import org.simplebank.controller.impl.MybankApiImpl;
import org.simplebank.domain.Status;
import org.simplebank.util.DataSetup;
import spark.Spark;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.simplebank.util.DataSetup.clearDataSet;

public class MybankTransferApiIntegrationTest {
    private static final String TRANSFER_URL = "http://localhost:4567/mybank/transfer-management/transfer";
    private static final String GET_CUSTOMERS_URL = "http://localhost:4567/mybank/customer-management/customers";
    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final Gson gson = new Gson();

    @BeforeClass
    public static void setup(){
        ControllerFactory controllerFactory = new ControllerFactoryImpl();
        MybankApi mybankApi = controllerFactory.makeMyBank();
        DataSetup.populateData();
        mybankApi.transfer();
        mybankApi.findBalanceForAccountId();
        mybankApi.getCustomers();
    }

    @Test
    public void getCustomersShouldReturnCustomersList() throws IOException {
        Request request = new Request.Builder()
                .url(GET_CUSTOMERS_URL)
                .get()
                .build();
        Response httpResponse = client.newCall(request).execute();
        org.simplebank.domain.Response response = gson.fromJson(Objects.requireNonNull(httpResponse.body()).string(), org.simplebank.domain.Response.class);
        assertEquals(response.getStatus(), Status.SUCCESS);
        Assert.assertThat((((JsonArray) response.getData()).size()), Is.is(4));
    }

    @Test
    public void getBalanceShouldReturnCustomersBalanceList() throws IOException {
        Integer customerId = 1001;
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(4567)
                .addPathSegment("mybank")
                .addPathSegment("transfer-management")
                .addPathSegment("balance")
                .addPathSegment(String.valueOf(customerId))
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();
        Response httpResponse = client.newCall(request).execute();
        org.simplebank.domain.Response response = gson.fromJson(Objects.requireNonNull(httpResponse.body()).string(), org.simplebank.domain.Response.class);
        assertEquals(response.getStatus(), Status.SUCCESS);
        Assert.assertThat((((JsonArray) response.getData()).size()), Is.is(2));
    }

    @Test
    public void getBalanceForWrongAccountShouldReturnEmpty() throws IOException {
        Integer accountId = 9999;
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("localhost")
                .port(4567)
                .addPathSegment("mybank")
                .addPathSegment("transfer-management")
                .addPathSegment("balance")
                .addPathSegment(String.valueOf(accountId))
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();
        Response httpResponse = client.newCall(request).execute();
        org.simplebank.domain.Response response = gson.fromJson(Objects.requireNonNull(httpResponse.body()).string(), org.simplebank.domain.Response.class);
        assertEquals(response.getStatus(), Status.SUCCESS);
        Assert.assertThat((((JsonArray) response.getData()).size()), Is.is(0));
    }

    @Test
    public void validTransfer() throws IOException {
        // form parameters
        String json = "\t{\n" +
                "\t\t\"sourceAccountId\" : 1001,\n" +
                "\t\t\"destinationAccountId\" : 1002,\n" +
                "\t\t\"amount\" : 10,\n" +
                "\t\t\"currency\": \"GBP\",\n" +
                "\t\t\"etag\": 82734923487\n" +
                "\t}";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(TRANSFER_URL)
                .post(body)
                .build();
        Response httpResponse = client.newCall(request).execute();
        org.simplebank.domain.Response response = gson.fromJson(Objects.requireNonNull(httpResponse.body()).string(), org.simplebank.domain.Response.class);
        assertEquals(response.getStatus(), Status.SUCCESS);
    }

    @Test
    public void lowBalanceTransferShouldFail() throws IOException {

        // form parameters
        String json = "\t{\n" +
                "\t\t\"sourceAccountId\" : 1001,\n" +
                "\t\t\"destinationAccountId\" : 1002,\n" +
                "\t\t\"amount\" : 1000,\n" +
                "\t\t\"currency\": \"GBP\",\n" +
                "\t\t\"etag\": 82734923487\n" +
                "\t}";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(TRANSFER_URL)
                .post(body)
                .build();
        Response httpResponse = client.newCall(request).execute();
        org.simplebank.domain.Response response = gson.fromJson(Objects.requireNonNull(httpResponse.body()).string(), org.simplebank.domain.Response.class);
        assertEquals(response.getStatus(), Status.ERROR);
        assertEquals(response.getData().getAsString(), "Transfer incomplete. Please check accountNumber, balance and eTag.");
    }

    @Test
    public void invalidEtagTransferShouldFail() throws IOException {

        // form parameters
        String json = "\t{\n" +
                "\t\t\"sourceAccountId\" : 1001,\n" +
                "\t\t\"destinationAccountId\" : 1002,\n" +
                "\t\t\"amount\" : 10,\n" +
                "\t\t\"currency\": \"GBP\",\n" +
                "\t\t\"etag\": 9999999999\n" +
                "\t}";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(TRANSFER_URL)
                .post(body)
                .build();
        Response httpResponse = client.newCall(request).execute();
        org.simplebank.domain.Response response = gson.fromJson(Objects.requireNonNull(httpResponse.body()).string(), org.simplebank.domain.Response.class);
        assertEquals(response.getStatus(), Status.ERROR);
        assertEquals(response.getData().getAsString(), "Transfer incomplete. Please check accountNumber, balance and eTag.");
    }

    @Test
    public void invalidReceiverCurrencyTransferShouldFail() throws IOException {

        // form parameters
        String json = "\t{\n" +
                "\t\t\"sourceAccountId\" : 1001,\n" +
                "\t\t\"destinationAccountId\" : 1002,\n" +
                "\t\t\"amount\" : 10,\n" +
                "\t\t\"currency\": \"EUR\",\n" +
                "\t\t\"etag\": 12345654321\n" +
                "\t}";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(TRANSFER_URL)
                .post(body)
                .build();
        Response httpResponse = client.newCall(request).execute();
        org.simplebank.domain.Response response = gson.fromJson(Objects.requireNonNull(httpResponse.body()).string(), org.simplebank.domain.Response.class);
        assertEquals(response.getStatus(), Status.ERROR);
        assertEquals(response.getData().getAsString(), "Receiver has not balance in EUR");
    }

    @AfterClass
    public static void killServer() {
        clearDataSet();
        Spark.stop();
    }

}
