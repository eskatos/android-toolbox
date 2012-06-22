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

import android.graphics.Bitmap;
import android.os.Message;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;

import de.akquinet.android.androlog.Log;

public class WebChromeSupport
{

    public static class AutoQuotaGrowWebChromeClient
            extends WebChromeClient
    {

        private final int factor;

        public AutoQuotaGrowWebChromeClient( int factor )
        {
            this.factor = factor;
        }

        @Override
        public void onReachedMaxAppCacheSize( long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater )
        {
            quotaUpdater.updateQuota( spaceNeeded * factor );
        }

    }

    public static class ConsoleLogWebChromeClient
            extends WebChromeClient
    {

        @Override
        public boolean onConsoleMessage( ConsoleMessage consoleMessage )
        {
            if ( consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.ERROR ) {
                android.util.Log.e( "JSConsole:", buildConsoleMessageLog( consoleMessage ) );
            } else {
                android.util.Log.d( "JSConsole:", buildConsoleMessageLog( consoleMessage ) );
            }
            return true;
        }

    }

    public static class ConsoleAndrologWebChromeClient
            extends WebChromeClient
    {

        @Override
        public boolean onConsoleMessage( ConsoleMessage consoleMessage )
        {
            if ( consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.ERROR ) {
                Log.e( "JSConsole:", buildConsoleMessageLog( consoleMessage ) );
            } else {
                Log.d( "JSConsole:", buildConsoleMessageLog( consoleMessage ) );
            }
            return true;
        }

    }

    private static String buildConsoleMessageLog( ConsoleMessage consoleMessage )
    {
        StringBuilder msg = new StringBuilder();
        msg.append( consoleMessage.messageLevel().name() ).
                append( '\t' ).append( consoleMessage.message() ).
                append( '\t' ).append( consoleMessage.sourceId() ).append( " (" ).append( consoleMessage.lineNumber() ).append( ")\n" );
        return msg.toString();
    }

    public static class ChainedWebChromeClient
            extends WebChromeClient
    {

        private final WebChromeClient[] chain;

        public ChainedWebChromeClient( WebChromeClient... chain )
        {
            this.chain = chain;
        }

        @Override
        public void onCloseWindow( WebView window )
        {
            for ( WebChromeClient chained : chain ) {
                chained.onCloseWindow( window );
            }
            super.onCloseWindow( window );
        }

        @Override
        public boolean onConsoleMessage( ConsoleMessage consoleMessage )
        {
            for ( WebChromeClient chained : chain ) {
                if ( chained.onConsoleMessage( consoleMessage ) ) {
                    return true;
                }
            }
            return super.onConsoleMessage( consoleMessage );
        }

        @Override
        public void onConsoleMessage( String message, int lineNumber, String sourceID )
        {
            for ( WebChromeClient chained : chain ) {
                chained.onConsoleMessage( null );
            }
            super.onConsoleMessage( message, lineNumber, sourceID );
        }

        @Override
        public boolean onCreateWindow( WebView view, boolean dialog, boolean userGesture, Message resultMsg )
        {
            for ( WebChromeClient chained : chain ) {
                if ( chained.onCreateWindow( view, dialog, userGesture, resultMsg ) ) {
                    return true;
                }
            }
            return super.onCreateWindow( view, dialog, userGesture, resultMsg );
        }

        @Override
        public void onExceededDatabaseQuota( String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, QuotaUpdater quotaUpdater )
        {
            for ( WebChromeClient chained : chain ) {
                chained.onExceededDatabaseQuota( url, databaseIdentifier, currentQuota, estimatedSize, totalUsedQuota, quotaUpdater );
            }
            super.onExceededDatabaseQuota( url, databaseIdentifier, currentQuota, estimatedSize, totalUsedQuota, quotaUpdater );
        }

        @Override
        public void onGeolocationPermissionsHidePrompt()
        {
            for ( WebChromeClient chained : chain ) {
                chained.onGeolocationPermissionsHidePrompt();
            }
            super.onGeolocationPermissionsHidePrompt();
        }

        @Override
        public void onGeolocationPermissionsShowPrompt( String origin, Callback callback )
        {
            for ( WebChromeClient chained : chain ) {
                chained.onGeolocationPermissionsShowPrompt( origin, callback );
            }
            super.onGeolocationPermissionsShowPrompt( origin, callback );
        }

        @Override
        public void onHideCustomView()
        {
            for ( WebChromeClient chained : chain ) {
                chained.onHideCustomView();
            }
            super.onHideCustomView();
        }

        @Override
        public boolean onJsAlert( WebView view, String url, String message, JsResult result )
        {
            for ( WebChromeClient chained : chain ) {
                if ( chained.onJsAlert( view, url, message, result ) ) {
                    return true;
                }
            }
            return super.onJsAlert( view, url, message, result );
        }

        @Override
        public boolean onJsBeforeUnload( WebView view, String url, String message, JsResult result )
        {
            for ( WebChromeClient chained : chain ) {
                if ( chained.onJsBeforeUnload( view, url, message, result ) ) {
                    return true;
                }
            }
            return super.onJsBeforeUnload( view, url, message, result );
        }

        @Override
        public boolean onJsConfirm( WebView view, String url, String message, JsResult result )
        {
            for ( WebChromeClient chained : chain ) {
                if ( chained.onJsConfirm( view, url, message, result ) ) {
                    return true;
                }
            }
            return super.onJsConfirm( view, url, message, result );
        }

        @Override
        public boolean onJsPrompt( WebView view, String url, String message, String defaultValue, JsPromptResult result )
        {
            for ( WebChromeClient chained : chain ) {
                if ( chained.onJsPrompt( view, url, message, defaultValue, result ) ) {
                    return true;
                }
            }
            return super.onJsPrompt( view, url, message, defaultValue, result );
        }

        @Override
        public boolean onJsTimeout()
        {
            for ( WebChromeClient chained : chain ) {
                if ( chained.onJsTimeout() ) {
                    return true;
                }
            }
            return super.onJsTimeout();
        }

        @Override
        public void onProgressChanged( WebView view, int newProgress )
        {
            for ( WebChromeClient chained : chain ) {
                chained.onProgressChanged( view, newProgress );
            }
            super.onProgressChanged( view, newProgress );
        }

        @Override
        public void onReachedMaxAppCacheSize( long spaceNeeded, long totalUsedQuota, QuotaUpdater quotaUpdater )
        {
            for ( WebChromeClient chained : chain ) {
                chained.onReachedMaxAppCacheSize( spaceNeeded, totalUsedQuota, quotaUpdater );
            }
            super.onReachedMaxAppCacheSize( spaceNeeded, totalUsedQuota, quotaUpdater );
        }

        @Override
        public void onReceivedIcon( WebView view, Bitmap icon )
        {
            for ( WebChromeClient chained : chain ) {
                chained.onReceivedIcon( view, icon );
            }
            super.onReceivedIcon( view, icon );
        }

        @Override
        public void onReceivedTitle( WebView view, String title )
        {
            for ( WebChromeClient chained : chain ) {
                chained.onReceivedTitle( view, title );
            }
            super.onReceivedTitle( view, title );
        }

        @Override
        public void onReceivedTouchIconUrl( WebView view, String url, boolean precomposed )
        {
            for ( WebChromeClient chained : chain ) {
                chained.onReceivedTouchIconUrl( view, url, precomposed );
            }
            super.onReceivedTouchIconUrl( view, url, precomposed );
        }

        @Override
        public void onRequestFocus( WebView view )
        {
            for ( WebChromeClient chained : chain ) {
                chained.onRequestFocus( view );
            }
            super.onRequestFocus( view );
        }

        @Override
        public void onShowCustomView( View view, CustomViewCallback callback )
        {
            for ( WebChromeClient chained : chain ) {
                chained.onShowCustomView( view, callback );
            }
            super.onShowCustomView( view, callback );
        }

    }

    private WebChromeSupport()
    {
    }

}
