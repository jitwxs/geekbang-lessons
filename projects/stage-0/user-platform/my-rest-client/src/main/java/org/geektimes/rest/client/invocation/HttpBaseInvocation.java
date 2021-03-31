package org.geektimes.rest.client.invocation;

import org.apache.commons.lang.StringUtils;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MultivaluedMap;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author jitwxs
 * @date 2021-03-30 23:28
 */
public abstract class HttpBaseInvocation implements Invocation {
    protected final URL url;
    protected final MultivaluedMap<String, Object> headers;
    protected final Set<String> encoding;

    public HttpBaseInvocation(final URI uri, final MultivaluedMap<String, Object> headers, final Set<String> encoding) {
        this.headers = headers;
        this.encoding = encoding;

        try {
            this.url = uri.toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException();
        }
    }

    protected void fillAttribute(HttpURLConnection connection) {
        for (Map.Entry<String, List<Object>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            for (Object headerValue : entry.getValue()) {
                connection.setRequestProperty(headerName, headerValue.toString());
            }
        }

        connection.setRequestProperty("content-encoding", StringUtils.join(encoding, ","));
    }
}
