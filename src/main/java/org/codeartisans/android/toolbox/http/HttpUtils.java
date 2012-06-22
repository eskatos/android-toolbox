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

import java.lang.reflect.Constructor;

import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;

import roboguice.inject.ContextSingleton;

import com.google.inject.Inject;

import de.akquinet.android.androlog.Log;

import org.codeartisans.java.toolbox.ObjectHolder;

@ContextSingleton
public class HttpUtils
{

    @Inject
    private Context context;

    private String userAgent = null;

    public synchronized String getUserAgent()
    {
        if ( userAgent == null ) {
            userAgent = loadUserAgent();
        }
        return userAgent;
    }

    private String loadUserAgent()
    {
        try {
            Constructor<WebSettings> constructor = WebSettings.class.getDeclaredConstructor( Context.class, WebView.class );
            constructor.setAccessible( true );
            try {
                WebSettings settings = constructor.newInstance( context, null );
                return settings.getUserAgentString();
            } finally {
                constructor.setAccessible( false );
            }
        } catch ( Exception e ) {
            String ua;
            if ( Thread.currentThread().getName().equalsIgnoreCase( "main" ) ) {
                WebView m_webview = new WebView( context );
                ua = m_webview.getSettings().getUserAgentString();
            } else {
                final ObjectHolder<String> userAgentHolder = new ObjectHolder<String>();
                Thread thread = new Thread()
                {

                    @Override
                    public void run()
                    {
                        // Looper.prepare();
                        WebView m_webview = new WebView( context );
                        String userAgent = m_webview.getSettings().getUserAgentString();
                        userAgentHolder.setHolded( userAgent );
                        // Looper.loop();
                    }

                };
                thread.start();
                try {
                    thread.join();
                } catch ( InterruptedException ex ) {
                    Log.w( "Working thread for user agent detection fallback was interrupted", ex );
                }
                return userAgentHolder.getHolded();
            }
            return ua;
        }
    }

}
