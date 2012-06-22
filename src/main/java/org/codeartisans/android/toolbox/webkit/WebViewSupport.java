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
package org.codeartisans.android.toolbox.webkit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.codeartisans.java.toolbox.Strings;

public class WebViewSupport
{

    public static class JSInjectWebViewClient
            extends WebViewClient
    {

        private final String javascript;

        public JSInjectWebViewClient( String javascript )
        {
            this.javascript = javascript;
        }

        @Override
        public void onPageFinished( WebView view, String url )
        {
            if ( !Strings.isEmpty( javascript ) ) {
                view.loadUrl( "javascript:" + javascript );
            }
            super.onPageFinished( view, url );
        }

    }

    public static class LinksHandlerWebViewClient
            extends WebViewClient
    {

        private final Context context;

        public LinksHandlerWebViewClient( Context context )
        {
            this.context = context;
        }

        /**
         * Handle inner links.
         */
        @Override
        public boolean shouldOverrideUrlLoading( WebView view, String url )
        {
            if ( url.startsWith( "tel:" ) ) {

                // Phone URL, launch the dialer
                Intent intent = new Intent( Intent.ACTION_DIAL, Uri.parse( url ) );
                context.startActivity( intent );
                return true;

            } else if ( url.startsWith( "mailto:" ) ) {

                // MailTo URL, launch default email client
                sendEmail( url );
                return true;

            } else if ( url.startsWith( "http:" ) || url.startsWith( "https:" ) ) {

                // Non-local http or https URL, launch defaut web browser
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                context.startActivity( intent );
                return true;

            } else {

                // Launch in-view
                return false;

            }
        }

        /**
         * Send a mail using the default email client.
         */
        private void sendEmail( String mailToUrl )
        {
            MailTo mt = MailTo.parse( mailToUrl );
            Intent intent = new Intent( Intent.ACTION_SEND );
            intent.setType( "plain/text" );
            intent.putExtra( Intent.EXTRA_EMAIL, new String[]{ mt.getTo() } );
            intent.putExtra( Intent.EXTRA_SUBJECT, mt.getSubject() );
            intent.putExtra( Intent.EXTRA_CC, mt.getCc() );
            intent.putExtra( Intent.EXTRA_TEXT, mt.getBody() );
            context.startActivity( intent );
        }

    }

    public static class ChainedWebViewClient
            extends WebViewClient
    {

        private final WebViewClient[] chain;

        public ChainedWebViewClient( WebViewClient... chain )
        {
            this.chain = chain;
        }

        @Override
        public void doUpdateVisitedHistory( WebView view, String url, boolean isReload )
        {
            for ( WebViewClient chained : chain ) {
                chained.doUpdateVisitedHistory( view, url, isReload );
            }
            super.doUpdateVisitedHistory( view, url, isReload );
        }

        @Override
        public void onFormResubmission( WebView view, Message dontResend, Message resend )
        {
            for ( WebViewClient chained : chain ) {
                chained.onFormResubmission( view, dontResend, resend );
            }
            super.onFormResubmission( view, dontResend, resend );
        }

        @Override
        public void onLoadResource( WebView view, String url )
        {
            for ( WebViewClient chained : chain ) {
                chained.onLoadResource( view, url );
            }
            super.onLoadResource( view, url );
        }

        @Override
        public void onPageFinished( WebView view, String url )
        {
            for ( WebViewClient chained : chain ) {
                chained.onPageFinished( view, url );
            }
            super.onPageFinished( view, url );
        }

        @Override
        public void onPageStarted( WebView view, String url, Bitmap favicon )
        {
            for ( WebViewClient chained : chain ) {
                chained.onPageStarted( view, url, favicon );
            }
            super.onPageStarted( view, url, favicon );
        }

        @Override
        public void onReceivedError( WebView view, int errorCode, String description, String failingUrl )
        {
            for ( WebViewClient chained : chain ) {
                chained.onReceivedError( view, errorCode, description, failingUrl );
            }
            super.onReceivedError( view, errorCode, description, failingUrl );
        }

        @Override
        public void onReceivedHttpAuthRequest( WebView view, HttpAuthHandler handler, String host, String realm )
        {
            for ( WebViewClient chained : chain ) {
                chained.onReceivedHttpAuthRequest( view, handler, host, realm );
            }
            super.onReceivedHttpAuthRequest( view, handler, host, realm );
        }

        @Override
        public void onReceivedSslError( WebView view, SslErrorHandler handler, SslError error )
        {
            for ( WebViewClient chained : chain ) {
                chained.onReceivedSslError( view, handler, error );
            }
            super.onReceivedSslError( view, handler, error );
        }

        @Override
        public void onScaleChanged( WebView view, float oldScale, float newScale )
        {
            for ( WebViewClient chained : chain ) {
                chained.onScaleChanged( view, oldScale, newScale );
            }
            super.onScaleChanged( view, oldScale, newScale );
        }

        @Override
        public void onTooManyRedirects( WebView view, Message cancelMsg, Message continueMsg )
        {
            for ( WebViewClient chained : chain ) {
                chained.onTooManyRedirects( view, cancelMsg, continueMsg );
            }
            super.onTooManyRedirects( view, cancelMsg, continueMsg );
        }

        @Override
        public void onUnhandledKeyEvent( WebView view, KeyEvent event )
        {
            for ( WebViewClient chained : chain ) {
                chained.onUnhandledKeyEvent( view, event );
            }
            super.onUnhandledKeyEvent( view, event );
        }

        @Override
        public boolean shouldOverrideKeyEvent( WebView view, KeyEvent event )
        {
            for ( WebViewClient chained : chain ) {
                if ( chained.shouldOverrideKeyEvent( view, event ) ) {
                    return true;
                }
            }
            return super.shouldOverrideKeyEvent( view, event );
        }

        @Override
        public boolean shouldOverrideUrlLoading( WebView view, String url )
        {
            for ( WebViewClient chained : chain ) {
                if ( chained.shouldOverrideUrlLoading( view, url ) ) {
                    return true;
                }
            }
            return super.shouldOverrideUrlLoading( view, url );
        }

    }

    private WebViewSupport()
    {
    }

}
