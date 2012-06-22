/*
 * Copyright 2012 Paul Merlin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codeartisans.android.toolbox.http;

import java.io.IOException;

import android.content.Context;
import android.net.http.AndroidHttpClient;

import roboguice.inject.ContextSingleton;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

/**
 * A HttpClient delegating to a configured AndroidHttpClient per context.
 */
@ContextSingleton
public class HttpClient
        implements org.apache.http.client.HttpClient
{

    public static final String HTTP_CACHE_SIZE_NAME = "android-toolbox:http-client:http-cache-size";

    @Inject
    protected Context context;

    @Inject
    protected HttpUtils httpUtils;

    /**
     * Set to 0 to disable caching.
     */
    @Inject( optional = true )
    @Named( HTTP_CACHE_SIZE_NAME )
    private Long httpCacheSize = Long.valueOf( 10 * 1024 * 1024 ); // 10 MiB

    private org.apache.http.client.HttpClient delegateHttpClient;

    protected synchronized org.apache.http.client.HttpClient ensureDelegate()
    {
        if ( delegateHttpClient == null ) {

            AndroidHttpClient androidClient = AndroidHttpClient.newInstance( httpUtils.getUserAgent(), context );

/* TODO Fix HttpClient headache.
            if ( httpCacheSize > 0 ) {

                CacheConfig cacheConfig = new CacheConfig();
                FileHttpCacheStorage cacheStorage = new FileHttpCacheStorage( cacheConfig, new File( context.getCacheDir(), "android-toolbox-http-cache" ), httpCacheSize );
                CachingHttpClient cachingClient = new CachingHttpClient( androidClient, cacheStorage, cacheConfig );
                delegateHttpClient = cachingClient;

            } else {
*/
            
                delegateHttpClient = androidClient;

/* TODO Fix HttpClient headache.
            }
 */
        }
        return delegateHttpClient;
    }

    @Override
    public HttpParams getParams()
    {
        return ensureDelegate().getParams();
    }

    @Override
    public ClientConnectionManager getConnectionManager()
    {
        return ensureDelegate().getConnectionManager();
    }

    @Override
    public HttpResponse execute( HttpUriRequest hur )
            throws IOException, ClientProtocolException
    {
        return ensureDelegate().execute( hur );
    }

    @Override
    public HttpResponse execute( HttpUriRequest hur, HttpContext hc )
            throws IOException, ClientProtocolException
    {
        return ensureDelegate().execute( hur, hc );
    }

    @Override
    public HttpResponse execute( HttpHost hh, HttpRequest hr )
            throws IOException, ClientProtocolException
    {
        return ensureDelegate().execute( hh, hr );
    }

    @Override
    public HttpResponse execute( HttpHost hh, HttpRequest hr, HttpContext hc )
            throws IOException, ClientProtocolException
    {
        return ensureDelegate().execute( hh, hr, hc );
    }

    @Override
    public <T> T execute( HttpUriRequest hur, ResponseHandler<? extends T> rh )
            throws IOException, ClientProtocolException
    {
        return ensureDelegate().execute( hur, rh );
    }

    @Override
    public <T> T execute( HttpUriRequest hur, ResponseHandler<? extends T> rh, HttpContext hc )
            throws IOException, ClientProtocolException
    {
        return ensureDelegate().execute( hur, rh, hc );
    }

    @Override
    public <T> T execute( HttpHost hh, HttpRequest hr, ResponseHandler<? extends T> rh )
            throws IOException, ClientProtocolException
    {
        return ensureDelegate().execute( hh, hr, rh );
    }

    @Override
    public <T> T execute( HttpHost hh, HttpRequest hr, ResponseHandler<? extends T> rh, HttpContext hc )
            throws IOException, ClientProtocolException
    {
        return ensureDelegate().execute( hh, hr, rh, hc );
    }

}
