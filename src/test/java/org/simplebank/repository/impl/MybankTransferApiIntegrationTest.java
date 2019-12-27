package org.simplebank.repository.impl;

import com.google.gson.Gson;
import okhttp3.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.simplebank.domain.MoneyTransferDTO;
import org.simplebank.domain.Status;
import org.simplebank.domain.TransferDetail;
import org.simplebank.services.MoneyService;
import spark.Spark;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static spark.Spark.post;

public class MybankTransferApiIntegrationTest {
    private static final String TRANSFER_URL = "http://localhost:4567/mybank/transfer-management/transfer";
    private final OkHttpClient client = new OkHttpClient();
    private static final MoneyService moneyService = new MoneyService();
    private static final HibernateH2SessionFactory hibernateSessionFactory = new HibernateH2SessionFactory();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final Gson gson = new Gson();

    @BeforeClass
    public static void setup() throws SQLException {
        hibernateSessionFactory.populateData();
        post("/mybank/transfer-management/transfer", (request, response) -> {
            response.type("application/json");
            MoneyTransferDTO moneyTransferDTO;
            try {
                moneyTransferDTO = new Gson().fromJson(request.body(), MoneyTransferDTO.class);
                TransferDetail transferDetail = moneyService.transfer(moneyTransferDTO);
                if (transferDetail.getStatus().equals(Status.SUCCESS)) {
                    return new Gson().toJson(new org.simplebank.domain.Response(Status.SUCCESS, new Gson().toJsonTree(transferDetail)));
                } else {
                    return new Gson().toJson(new org.simplebank.domain.Response(Status.ERROR, new Gson().toJsonTree(transferDetail.getErrorMessage())));
                }
            } catch (Exception ex) {
                return new Gson().toJsonTree(new org.simplebank.domain.Response(Status.ERROR, "Problem While transferring money. More info: ")
                        + ex.getMessage());
            }
        });
    }

    @AfterClass
    public static void killServer() {
        Spark.stop();
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
        assertEquals(response.getStatus(),Status.SUCCESS);
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
        assertEquals(response.getStatus(),Status.ERROR);
        assertEquals(response.getData().getAsString(),"Transfer incomplete. Please check accountNumber, balance and eTag.");
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
        assertEquals(response.getStatus(),Status.ERROR);
        assertEquals(response.getData().getAsString(),"Transfer incomplete. Please check accountNumber, balance and eTag.");
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
        assertEquals(response.getStatus(),Status.ERROR);
        assertEquals(response.getData().getAsString(),"Receiver has not balance in EUR");

    }
}
