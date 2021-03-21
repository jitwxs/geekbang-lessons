package org.geektimes.web.mvc;

import javax.servlet.descriptor.JspPropertyGroupDescriptor;
import java.util.Collection;
import java.util.Collections;

/**
 * @author jitwxs
 * @date 2021年03月21日 19:55
 */
public class JspPropertyGroupDescriptorImpl implements JspPropertyGroupDescriptor {
    private final String urlPatterns;
    private final String pageEncoding;
    private final String includePreludes;
    private final String includeCodas;
    private final boolean trimDirectiveWhitespaces;

    public JspPropertyGroupDescriptorImpl(String urlPatterns, String pageEncoding, String includePreludes, String includeCodas, boolean trimDirectiveWhitespaces) {
        this.urlPatterns = urlPatterns;
        this.pageEncoding = pageEncoding;
        this.includePreludes = includePreludes;
        this.includeCodas = includeCodas;
        this.trimDirectiveWhitespaces = trimDirectiveWhitespaces;
    }

    @Override
    public Collection<String> getUrlPatterns() {
        return Collections.singletonList(this.urlPatterns);
    }

    @Override
    public String getElIgnored() {
        return null;
    }

    @Override
    public String getPageEncoding() {
        return this.pageEncoding;
    }

    @Override
    public String getScriptingInvalid() {
        return null;
    }

    @Override
    public String getIsXml() {
        return null;
    }

    @Override
    public Collection<String> getIncludePreludes() {
        return Collections.singletonList(this.includePreludes);
    }

    @Override
    public Collection<String> getIncludeCodas() {
        return Collections.singletonList(this.includeCodas);
    }

    @Override
    public String getDeferredSyntaxAllowedAsLiteral() {
        return null;
    }

    @Override
    public String getTrimDirectiveWhitespaces() {
        return this.trimDirectiveWhitespaces ? "true" : "false";
    }

    @Override
    public String getDefaultContentType() {
        return null;
    }

    @Override
    public String getBuffer() {
        return null;
    }

    @Override
    public String getErrorOnUndeclaredNamespace() {
        return null;
    }
}
