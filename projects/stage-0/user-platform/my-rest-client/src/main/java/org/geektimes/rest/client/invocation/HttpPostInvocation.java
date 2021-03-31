package org.geektimes.rest.client.invocation;

import org.geektimes.rest.core.DefaultResponse;
import org.geektimes.rest.util.IOUtils;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * @author jitwxs
 * @date 2021-03-30 23:23
 */
public class HttpPostInvocation extends HttpBaseInvocation {
    /**
     * q请求体
     */
    private final Object requestBody;

    public HttpPostInvocation(final URI uri,
                              final MultivaluedMap<String, Object> headers,
                              final Map<String, String> properties,
                              final Set<String> encoding,
                              final Set<MediaType> mediaTypes,
                              final Object requestBody) {
        super(uri, headers, properties, encoding, mediaTypes);
        this.requestBody = requestBody;
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

            super.fillAttribute(connection);

            // send body
            if(requestBody != null) {
                // 任选一个编码发送
                final String oneEncoding = encoding.iterator().next();

                connection.setDoOutput(true);
                IOUtils.writeJson(connection.getOutputStream(), requestBody, oneEncoding);
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
