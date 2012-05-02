/*
 * Copyright 2012 paul.
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
package org.codeartisans.android.toolbox.activity;

import android.view.Menu;
import android.view.MenuItem;

import org.codeartisans.android.toolbox.activity.event.OnCreateOptionsMenuEvent;
import org.codeartisans.android.toolbox.activity.event.OnOptionsItemSelectedEvent;
import org.codeartisans.android.toolbox.activity.event.OnOptionsMenuClosedEvent;

public class RoboListActivity
        extends roboguice.activity.RoboListActivity
{

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        boolean result = super.onCreateOptionsMenu( menu );
        eventManager.fire( new OnCreateOptionsMenuEvent( menu ) );
        return result;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        boolean result = super.onOptionsItemSelected( item );
        eventManager.fire( new OnOptionsItemSelectedEvent( item ) );
        return result;
    }

    @Override
    public void onOptionsMenuClosed( Menu menu )
    {
        super.onOptionsMenuClosed( menu );
        eventManager.fire( new OnOptionsMenuClosedEvent( menu ) );
    }

}
