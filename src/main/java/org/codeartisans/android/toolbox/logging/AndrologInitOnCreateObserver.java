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
package org.codeartisans.android.toolbox.logging;

import android.content.Context;

import roboguice.activity.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.akquinet.android.androlog.Log;

/**
 * Init Androlog logging system OnCreateEvent.
 * 
 * Add the following code to all the entry point of the application:
 * 
 * <pre>
 * @Inject
 * private AndrologInitOnCreateObserver andrologInitOnCreateObserver;
 * </pre>
 * 
 * Injecting the component there will cause auto-wiring of the 
 * {@link AndrologInitOnCreateObserver#handleOnCreate(roboguice.activity.event.OnCreateEvent) } method.
 */
@ContextSingleton
public class AndrologInitOnCreateObserver
{

    @Inject
    private Context context;

    @Inject( optional = true )
    @Named( "androlog.filename" )
    private String andrologFileName;

    public void handleOnCreate( @Observes OnCreateEvent event )
    {
        Log.init( context, andrologFileName );
        Log.i( "Androlog Logging and Reporting System Initialized" );
    }

}
