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
package org.codeartisans.android.toolbox.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.client.cache.HttpCacheUpdateCallback;
import org.apache.http.client.cache.HttpCacheUpdateException;
import org.apache.http.impl.client.cache.CacheConfig;
import org.codeartisans.java.toolbox.io.Files;
import org.codeartisans.java.toolbox.io.IO;

/**
 * TODO Limit cache size: find a viable strategy balancing size coercion and performance.
 */
public class FileHttpCacheStorage
        implements HttpCacheStorage
{

    private final CacheConfig cacheConfig;

    private final File cacheDir;

    private final Long cacheSize;

    private boolean cacheDirOk = false;

    public FileHttpCacheStorage( CacheConfig cacheConfig, File cacheDir, Long cacheSize )
    {
        this.cacheConfig = cacheConfig;
        this.cacheDir = cacheDir;
        this.cacheSize = cacheSize;
    }

    @Override
    public synchronized void putEntry( String key, HttpCacheEntry entry )
            throws IOException
    {
        File entryFile = getEntryFile( key );
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream( entryFile );
            oos = new ObjectOutputStream( fos );
            oos.writeObject( entry );
        } finally {
            IO.closeSilently( oos );
            IO.closeSilently( fos );
        }
    }

    @Override
    public synchronized HttpCacheEntry getEntry( String key )
            throws IOException
    {
        File entryFile = getEntryFile( key );
        if ( !entryFile.exists() ) {
            return null;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream( entryFile );
            ois = new ObjectInputStream( fis );
            HttpCacheEntry entry = ( HttpCacheEntry ) ois.readObject();
            return entry;
        } catch ( ClassCastException ex ) {
            throw new IOException( "Unable to desirialize HttpCacheEntry for key: '" + key + "'", ex );
        } catch ( ClassNotFoundException ex ) {
            throw new IOException( "Unable to desirialize HttpCacheEntry for key: '" + key + "'", ex );
        } finally {
            IO.closeSilently( ois );
            IO.closeSilently( fis );
        }
    }

    @Override
    public synchronized void removeEntry( String key )
            throws IOException
    {
        File entryFile = getEntryFile( key );
        if ( entryFile.exists() ) {
            Files.delete( entryFile );
        }
    }

    @Override
    public synchronized void updateEntry( String key, HttpCacheUpdateCallback callback )
            throws IOException, HttpCacheUpdateException
    {
        HttpCacheEntry updatedEntry = callback.update( getEntry( key ) );
        if ( updatedEntry == null ) {
            removeEntry( key );
        } else {
            putEntry( key, updatedEntry );
        }
    }

    private File getEntryFile( String key )
            throws IOException
    {
        return new File( ensureCacheDir(), key );
    }

    private synchronized File ensureCacheDir()
            throws IOException
    {
        if ( !cacheDirOk ) {
            if ( this.cacheDir.exists() && !this.cacheDir.isDirectory() ) {
                throw new IOException( "Cache dir '" + this.cacheDir.getAbsolutePath() + "' exist and is not a directory, cannot continue!" );
            }
            if ( !this.cacheDir.exists() ) {
                Files.mkdir( this.cacheDir );
            }
            cacheDirOk = true;
        }
        return cacheDir;
    }

}
