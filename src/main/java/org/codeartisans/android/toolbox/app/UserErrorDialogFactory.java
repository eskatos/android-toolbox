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
package org.codeartisans.android.toolbox.app;

import java.util.Collection;
import java.util.Collections;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

import org.codeartisans.java.toolbox.Strings;

/**
 * A dialog factory that create AlertDialogs that nicely display errors to the user and allow her to report the error.
 */
public class UserErrorDialogFactory
{

    public static <T extends Throwable> AlertDialog show( Context context, String title, T error,
                                                          String positiveButtonLabel, OnClickListener positiveButtonListener,
                                                          String negativeButtonLabel, OnClickListener negativeButtonListener )
    {
        AlertDialog dialog = create( context, title, error,
                                     positiveButtonLabel, positiveButtonListener,
                                     negativeButtonLabel, negativeButtonListener );
        dialog.show();
        return dialog;
    }

    public static <T extends Throwable> AlertDialog show( Context context, String title, Collection<T> errors,
                                                          String positiveButtonLabel, OnClickListener positiveButtonListener,
                                                          String negativeButtonLabel, OnClickListener negativeButtonListener )
    {
        AlertDialog dialog = create( context, title, errors,
                                     positiveButtonLabel, positiveButtonListener,
                                     negativeButtonLabel, negativeButtonListener );
        dialog.show();
        return dialog;
    }

    public static <T extends Throwable> AlertDialog show( Context context, String title, String message,
                                                          String positiveButtonLabel, OnClickListener positiveButtonListener,
                                                          String negativeButtonLabel, OnClickListener negativeButtonListener )
    {
        AlertDialog dialog = create( context, title, message,
                                     positiveButtonLabel, positiveButtonListener,
                                     negativeButtonLabel, negativeButtonListener );
        dialog.show();
        return dialog;
    }

    public static <T extends Throwable> AlertDialog show( Context context, String title, String message,
                                                          String positiveButtonLabel, OnClickListener positiveButtonListener,
                                                          String neutralButtonLabel, OnClickListener neutralButtonListener,
                                                          String negativeButtonLabel, OnClickListener negativeButtonListener )
    {
        AlertDialog dialog = create( context, title, message,
                                     positiveButtonLabel, positiveButtonListener,
                                     neutralButtonLabel, neutralButtonListener,
                                     negativeButtonLabel, negativeButtonListener );
        dialog.show();
        return dialog;
    }

    private static <T extends Throwable> AlertDialog create( Context context, String title, T ex,
                                                             String positiveButtonLabel, OnClickListener positiveButtonListener,
                                                             String negativeButtonLabel, OnClickListener negativeButtonListener )
    {
        return create( context, title, Collections.singleton( ex ),
                       positiveButtonLabel, positiveButtonListener,
                       negativeButtonLabel, negativeButtonListener );
    }

    private static <T extends Throwable> AlertDialog create( Context context, String title, Collection<T> errors,
                                                             String positiveButtonLabel, OnClickListener positiveButtonListener,
                                                             String negativeButtonLabel, OnClickListener negativeButtonListener )
    {
        return create( context, title, errors,
                       positiveButtonLabel, positiveButtonListener,
                       null, null,
                       negativeButtonLabel, negativeButtonListener );
    }

    private static <T extends Throwable> AlertDialog create( Context context, String title, String message,
                                                             String positiveButtonLabel, OnClickListener positiveButtonListener,
                                                             String negativeButtonLabel, OnClickListener negativeButtonListener )
    {
        return create( context, title, message,
                       positiveButtonLabel, positiveButtonListener,
                       null, null,
                       negativeButtonLabel, negativeButtonListener );
    }

    public static <T extends Throwable> AlertDialog create( Context context, String title, T error,
                                                            String positiveButtonLabel, OnClickListener positiveButtonListener,
                                                            String neutralButtonLabel, OnClickListener neutralButtonListener,
                                                            String negativeButtonLabel, OnClickListener negativeButtonListener )
    {
        return create( context, title, Collections.singleton( error ),
                       positiveButtonLabel, positiveButtonListener,
                       neutralButtonLabel, neutralButtonListener,
                       negativeButtonLabel, negativeButtonListener );
    }

    public static <T extends Throwable> AlertDialog create( Context context, String title, Collection<T> errors,
                                                            String positiveButtonLabel, OnClickListener positiveButtonListener,
                                                            String neutralButtonLabel, OnClickListener neutralButtonListener,
                                                            String negativeButtonLabel, OnClickListener negativeButtonListener )
    {
        return create( context, title, buildMessage( errors ),
                       positiveButtonLabel, positiveButtonListener,
                       neutralButtonLabel, neutralButtonListener,
                       negativeButtonLabel, negativeButtonListener );
    }

    public static <T extends Throwable> AlertDialog create( Context context, String title, String message,
                                                            String positiveButtonLabel, OnClickListener positiveButtonListener,
                                                            String neutralButtonLabel, OnClickListener neutralButtonListener,
                                                            String negativeButtonLabel, OnClickListener negativeButtonListener )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setTitle( title ).
                setMessage( message ).
                setCancelable( false );
        if ( !Strings.isEmpty( positiveButtonLabel ) ) {
            builder.setPositiveButton( positiveButtonLabel, positiveButtonListener );
        }
        if ( !Strings.isEmpty( neutralButtonLabel ) ) {
            builder.setNeutralButton( neutralButtonLabel, neutralButtonListener );
        }
        if ( !Strings.isEmpty( negativeButtonLabel ) ) {
            builder.setNegativeButton( negativeButtonLabel, negativeButtonListener );
        }
        return builder.create();
    }

    private static <T extends Throwable> String buildMessage( Collection<T> errors )
    {
        if ( errors.isEmpty() ) {
            return Strings.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for ( Throwable ex : errors ) {
            sb.append( ex.getMessage() ).append( Strings.NEWLINE );
        }
        return sb.toString();
    }

    private UserErrorDialogFactory()
    {
    }

}
