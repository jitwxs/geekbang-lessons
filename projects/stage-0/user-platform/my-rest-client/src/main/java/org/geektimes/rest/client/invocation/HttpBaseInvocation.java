package org.geektimes.rest.client.invocation;

import org.apache.commons.lang.StringUtils;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author jitwxs
 * @date 2021-03-30 23:28
 */
public abstract class HttpBaseInvocation implements Invocation {
    protected final URL url;
    protected final MultivaluedMap<String, Object> headers;
    protected final Map<String, String> properties;
    protected final Set<String> encoding;
    protected final Set<MediaType> mediaTypes;

    /**
     * 构造方法
     *
     * @param uri 请求路径
     * @param headers 请求头
     * @param properties 请求 params
     * @param encoding 请求编码
     */
    public HttpBaseInvocation(final URI uri,
                              final MultivaluedMap<String, Object> headers,
                              final Map<String, String> properties,
                              final Set<String> encoding,
                              final Set<MediaType> mediaTypes) {
        this.headers = headers;
        this.properties = properties;
        this.encoding = encoding;
        this.mediaTypes = mediaTypes;

        try {
            this.url = uri.toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException();
        }
    }

    protected void fillAttribute(HttpURLConnection connection) {
        // 设置 header
        for (Map.Entry<String, List<Object>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            for (Object headerValue : entry.getValue()) {
                connection.setRequestProperty(headerName, headerValue.toString());
            }
        }

        // 设置 properties
        properties.forEach(connection::setRequestProperty);
        connection.setRequestProperty("content-type", StringUtils.join(mediaTypes.stream().map(MediaType::toString).collect(Collectors.toList()), ","));
        connection.setRequestProperty("content-encoding", StringUtils.join(encoding, ","));
    }
}
