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
package org.codeartisans.android.toolbox.os;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.os.AsyncTask;

/**
 * Generic {@link AsyncTask} result wrapper that provides error handling.
 *
 * @param ResultType Type of the {@link AsyncTask} result.
 * @param ErrorType Type of the eventual error.
 */
public class AsyncTaskResult<ResultType, ErrorType extends Throwable>
{

    private final ResultType result;

    private final List<ErrorType> errors = new ArrayList<ErrorType>();

    public AsyncTaskResult( ResultType result )
    {
        this( result, ( ErrorType ) null );
    }

    public AsyncTaskResult( ErrorType... errors )
    {
        this( null, errors );
    }

    public AsyncTaskResult( ResultType result, ErrorType... errors )
    {
        this( result, Arrays.asList( errors ) );
    }

    public AsyncTaskResult( ResultType result, Collection<ErrorType> errors )
    {
        super();
        this.result = result;
        if ( errors != null ) {
            this.errors.addAll( errors );
        }
    }

    public ResultType getResult()
    {
        return result;
    }

    public boolean hasError()
    {
        return !errors.isEmpty();
    }

    public List<ErrorType> getErrors()
    {
        return errors;
    }

}
