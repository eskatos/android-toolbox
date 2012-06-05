package org.codeartisans.android.toolbox.os;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public abstract class AsyncTaskWithMessageDialog<Params extends Object, Progress extends Object, Result extends Object>
        extends AsyncTask<Params, Progress, Result>
{

    protected final Context context;

    private final String initialMessage;

    protected ProgressDialog wait = null;

    public AsyncTaskWithMessageDialog( Context context, String initialMessage )
    {
        this.context = context;
        this.initialMessage = initialMessage;
    }

    @Override
    protected final void onPreExecute()
    {
        wait = ProgressDialog.show( context, "", initialMessage, true, false );
        super.onPreExecute();
    }

    @Override
    protected final void onPostExecute( Result result )
    {
        super.onPostExecute( result );
        beforeDialogDismiss( result );
        wait.dismiss();
        afterDialogDismiss( result );
    }

    @Override
    protected final void onProgressUpdate( Progress... values )
    {
        wait.setMessage( values[0].toString() );
    }

    /**
     * This method is called on the UI thread.
     */
    protected void beforeDialogDismiss( Result result )
    {
    }

    /**
     * This method is called on the UI thread.
     */
    protected void afterDialogDismiss( Result result )
    {
    }

}
