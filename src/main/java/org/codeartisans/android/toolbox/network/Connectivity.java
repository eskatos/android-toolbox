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
package org.codeartisans.android.toolbox.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import roboguice.inject.ContextSingleton;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Check for Android device connectivity state.
 * 
 * You will need the following permission:
 * <pre>
 * &lt;uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /&gt;
 * </pre>
 */
@ContextSingleton
public class Connectivity
{

    @Inject
    private Context context;

    @Inject( optional = true )
    @Named( "connectivity.roaming" )
    private Boolean roaming = Boolean.FALSE;

    public boolean isOnline()
    {
        ConnectivityManager connectivityManager = ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if ( info == null || !info.isConnected() ) {
            return false;
        }
        if ( info.isRoaming() ) {
            return roaming;
        }
        return true;
    }

    public static enum ConnectivityState
    {

        WIFI,
        MOBILE,
        OFFLINE

    }

    public ConnectivityState getConnectivityState()
    {
        ConnectivityManager connectivityManager = ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        State mobile = connectivityManager.getNetworkInfo( 0 ).getState();
        State wifi = connectivityManager.getNetworkInfo( 1 ).getState();
        if ( mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING ) {
            return ConnectivityState.MOBILE;
        } else if ( wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING ) {
            return ConnectivityState.WIFI;
        }
        return ConnectivityState.OFFLINE;
    }

}
