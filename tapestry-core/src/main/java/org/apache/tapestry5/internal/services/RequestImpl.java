// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.internal.services;

import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

/**
 * Basic implementation of {@link org.apache.tapestry5.services.Request} that wraps around an
 * {@link javax.servlet.http.HttpServletRequest}. This is not threadsafe, nor should it need to be (each Request is
 * handled by its own Thread).
 */
public class RequestImpl implements Request
{
    static final String REQUESTED_WITH_HEADER = "X-Requested-With";

    static final String XML_HTTP_REQUEST = "XMLHttpRequest";

    static final String X_FORWARDED_PROTO_HEADER = "X-Forwarded-Proto";
    static final String X_FORWARDED_PROTO_HTTPS = "https";

    private final HttpServletRequest request;

    private final String applicationCharset;

    private final TapestrySessionFactory sessionFactory;

    private boolean encodingSet;

    private Session session;

    public RequestImpl(
            HttpServletRequest request,
            String applicationCharset,
            TapestrySessionFactory sessionFactory)
    {
        this.request = request;
        this.applicationCharset = applicationCharset;
        this.sessionFactory = sessionFactory;
    }

    public List<String> getParameterNames()
    {
        setupEncoding();

        return InternalUtils.toList(request.getParameterNames());
    }

    public List<String> getHeaderNames()
    {
        return InternalUtils.toList(request.getHeaderNames());
    }

    public String getParameter(String name)
    {
        setupEncoding();

        return request.getParameter(name);
    }

    public String[] getParameters(String name)
    {
        setupEncoding();

        return request.getParameterValues(name);
    }

    public String getHeader(String name)
    {
        return request.getHeader(name);
    }

    public String getPath()
    {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null)
            return request.getServletPath();

        // Websphere 6.1 is a bit wonky (see TAPESTRY-1713), and tends to return the empty string
        // for the servlet path, and return the true path in pathInfo.

        return pathInfo.length() == 0 ? "/" : pathInfo;
    }

    public String getContextPath()
    {
        return request.getContextPath();
    }


    public boolean isSessionInvalidated()
    {
        // Double check to ensure that the session exists, but don't create it.
        if (session == null)
        {
            session = sessionFactory.getSession(false);
        }

        return session != null && session.isInvalidated();
    }

    public Session getSession(boolean create)
    {
        if (session != null && session.isInvalidated())
        {
            session = null;
        }

        if (session == null)
        {
            // TAP5-1489 - Re-storage of session attributes at end of request should be configurable
            session = sessionFactory.getSession(create);
        }

        return session;
    }

    public Locale getLocale()
    {
        return request.getLocale();
    }

    public long getDateHeader(String name)
    {
        return request.getDateHeader(name);
    }

    private void setupEncoding()
    {
        if (encodingSet)
        {
            return;
        }

        // The request may specify an encoding, which is better than the application, which can only
        // guess at what the client is doing!

        String requestEncoding = request.getCharacterEncoding();

        try
        {
            request.setCharacterEncoding(requestEncoding != null ? requestEncoding : applicationCharset);
        } catch (UnsupportedEncodingException ex)
        {
            throw new RuntimeException(ex);
        }

        encodingSet = true;
    }

    public boolean isXHR()
    {
        return XML_HTTP_REQUEST.equals(request.getHeader(REQUESTED_WITH_HEADER));
    }

    public boolean isSecure()
    {
        return request.isSecure() ||
                X_FORWARDED_PROTO_HTTPS.equals(request.getHeader(X_FORWARDED_PROTO_HEADER));
    }

    public boolean isRequestedSessionIdValid()
    {
        return request.isRequestedSessionIdValid();
    }

    public Object getAttribute(String name)
    {
        return request.getAttribute(name);
    }

    public List<String> getAttributeNames()
    {
        setupEncoding();

        return InternalUtils.toList(request.getAttributeNames());
    }

    public void setAttribute(String name, Object value)
    {
        request.setAttribute(name, value);
    }

    public String getMethod()
    {
        return request.getMethod();
    }

    public String getServerName()
    {
        return request.getServerName();
    }

    public int getLocalPort()
    {
        return request.getLocalPort();
    }

    /**
     * @since 5.2.5
     */
    public int getServerPort()
    {
        return request.getServerPort();
    }

    public String getRemoteHost()
    {
        return request.getRemoteHost();
    }

}
