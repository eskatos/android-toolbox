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
package org.codeartisans.android.toolbox.widget;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.RelativeLayout;

/**
 * RelativeLayout that provide a recursive Checkable behaviour.
 */
public class CheckableRelativeLayout
        extends RelativeLayout
        implements Checkable
{

    private boolean isChecked;

    private List<Checkable> checkableViews;

    public CheckableRelativeLayout( Context context )
    {
        super( context );
    }

    public CheckableRelativeLayout( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }

    public CheckableRelativeLayout( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    /*
     * @see android.widget.Checkable#isChecked()
     */
    @Override
    public boolean isChecked()
    {
        return isChecked;
    }

    /*
     * @see android.widget.Checkable#setChecked(boolean)
     */
    @Override
    public void setChecked( boolean isChecked )
    {
        this.isChecked = isChecked;
        for ( Checkable c : checkableViews ) {
            c.setChecked( isChecked );
        }
    }

    /*
     * @see android.widget.Checkable#toggle()
     */
    @Override
    public void toggle()
    {
        this.isChecked = !this.isChecked;
        for ( Checkable c : checkableViews ) {
            c.toggle();
        }
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        final int childCount = this.getChildCount();
        for ( int index = 0; index < childCount; ++index ) {
            findCheckableChildren( this.getChildAt( index ) );
        }
    }

    /**
     * Add to our checkable list all the children of the view that implement the
     * interface Checkable
     */
    private void findCheckableChildren( View view )
    {
        if ( view instanceof Checkable ) {
            checkableViews.add( ( Checkable ) view );
        }
        if ( view instanceof ViewGroup ) {
            final ViewGroup viewGroup = ( ViewGroup ) view;
            final int childCount = viewGroup.getChildCount();
            for ( int index = 0; index < childCount; ++index ) {
                findCheckableChildren( viewGroup.getChildAt( index ) );
            }
        }
    }

}
