package org.geektimes.rest.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geektimes.rest.bean.RestResponse;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class RestClientDemo {

    /**
     * Request org.geektimes.projects.user.web.controller.TestGetController
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

    /**
     * Request org.geektimes.projects.user.web.controller.TestPostController
     */
    @Test
    public void testPost() throws IOException {
        RestResponse request = new RestResponse();
        request.setCode(123);
        request.setMessage("Hello Server");

        final Variant variant = new Variant(MediaType.APPLICATION_JSON_TYPE, Locale.CHINA, StandardCharsets.UTF_8.toString());

        final Entity<RestResponse> entity = Entity.entity(request, variant);

        Client client = ClientBuilder.newClient();

        final RestResponse response = client
                .target("http://127.0.0.1:8080/api/testPost")
                .request()
                .post(entity, RestResponse.class);

        Assert.assertEquals( new ObjectMapper().writeValueAsString(request), response.getMessage());
    }
}
