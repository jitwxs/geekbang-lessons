package org.geektimes.rest.client.invocation;

import org.geektimes.rest.core.DefaultResponse;
import org.geektimes.rest.util.IOUtils;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * @author jitwxs
 * @date 2021-03-30 23:23
 */
public class HttpPostInvocation extends HttpBaseInvocation {
    private final Entity<?> entity;

    public HttpPostInvocation(final URI uri, final MultivaluedMap<String, Object> headers, final Set<String> encoding, final Entity<?> entity) {
        super(uri, headers, encoding);
        this.entity = entity;
    }

    @Override
    public Invocation property(String name, Object value) {
        return this;
    }

    @Override
    public Response invoke() {
        final DefaultResponse response = invokeLogic();

        response.setEntity(response.readEntity(String.class));

        return response;
    }

    @Override
    public <T> T invoke(Class<T> responseType) {
        return invokeLogic().readEntity(responseType);
    }

    @Override
    public <T> T invoke(GenericType<T> responseType) {
        return invokeLogic().readEntity(responseType);
    }

    @Override
    public Future<Response> submit() {
        return null;
    }

    @Override
    public <T> Future<T> submit(Class<T> responseType) {
        return null;
    }

    @Override
    public <T> Future<T> submit(GenericType<T> responseType) {
        return null;
    }

    @Override
    public <T> Future<T> submit(InvocationCallback<T> callback) {
        return null;
    }

    private DefaultResponse invokeLogic() {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethod.POST);
            fillAttribute(connection);

            // send body
            if(entity != null) {
                connection.setDoOutput(true);
                IOUtils.writeJson(connection.getOutputStream(), entity, encoding.iterator().next());
            }

            int statusCode = connection.getResponseCode();

            DefaultResponse response = new DefaultResponse();
            response.setConnection(connection);
            response.setStatus(statusCode);

            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
