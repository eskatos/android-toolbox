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
package org.codeartisans.android.toolbox;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import android.content.res.AssetManager;

import org.codeartisans.java.toolbox.io.IO;

public final class Assets
{

    public static String loadAssetAsString( AssetManager assetManager, String assetPath )
            throws IOException
    {
        InputStream input = null;
        try {
            input = assetManager.open( assetPath );
            StringWriter sw = new StringWriter();
            IO.copy( new InputStreamReader( input, "UTF-8" ), sw );
            return sw.toString();
        } finally {
            IO.closeSilently( input );
        }
    }

    private Assets()
    {
    }

}
