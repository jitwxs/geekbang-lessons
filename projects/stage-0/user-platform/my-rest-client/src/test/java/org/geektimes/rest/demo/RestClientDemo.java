package org.geektimes.rest.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geektimes.rest.bean.RestResponse;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class RestClientDemo {

    /**
     * org.geektimes.projects.user.web.controller.TestGetController
     */
    @Test
    public void testGet() throws IOException {
        Client client = ClientBuilder.newClient();
        Response response = client
                .target("http://127.0.0.1:8080/api/testGet")      // WebTarget
                .request() // Invocation.Builder
                .get();                                     //  Response

        final ObjectMapper objectMapper = new ObjectMapper();
        final RestResponse restResponse = objectMapper.readValue(response.readEntity(String.class), RestResponse.class);

        Assert.assertEquals("Hello Get!", restResponse.getMessage());
    }
}
