/************************************************************************
 *                                                                      *
 *  DDDD     SSSS    AAA        Daten- und Systemtechnik Aachen GmbH    *
 *  D   D   SS      A   A       Pascalstrasse 28                        *
 *  D   D    SSS    AAAAA       52076 Aachen-Oberforstbach, Germany     *
 *  D   D      SS   A   A       Telefon: +49 (0)2408 / 9492-0           *
 *  DDDD    SSSS    A   A       Telefax: +49 (0)2408 / 9492-92          *
 *                                                                      *
 *                                                                      *
 *  (c) Copyright by DSA - all rights reserved                          *
 *                                                                      *
 ************************************************************************
 *
 * Initial Creation:
 *    Author      Ada Lezama
 *    Created on  20.10.2013
 *
 ************************************************************************/
package de.dsa.hackathon2013.app;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.dsa.hackaton.R;
import de.dsa.smartdiag.interpreter.ISmartDiagValue;

/**
 * Adapter to populate the Vehicle Diagnostic Values list view.
 *
 * This adapter will handle a list of {@link DiagnosticValues} to display their
 * information
 *
 * The adapter will generate the list based on the layout provided in
 * richlist_vehicle_values.xml
 */
public class DiagnosticValuesListAdapter extends ArrayAdapter<DiagnosticValue> {

    /** The list of {@link ISmartDiagValue} to be diplayed by the adapter. */
    private List<DiagnosticValue> mDiagnosticValues;

    /**
     * Creates a new instance of {@link DiagnosticValuesListAdapter} with the
     * corresponding Diagnostic Values list.
     *
     * @param pContext the context to which the list is associated.
     * @param pDiagValues the list of diagnostic values to be displayed
     */
    public DiagnosticValuesListAdapter(Context pContext, List<DiagnosticValue> pDiagnosticValues) {
        super(pContext, 0, pDiagnosticValues);
        mDiagnosticValues = pDiagnosticValues;
    }

    @Override
    public int getCount() {
        return mDiagnosticValues.size();
    }

    @Override
    public DiagnosticValue getItem(int position) {
        return mDiagnosticValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Holder of the views to be handled by the adapter. This will avoid the
     * call to the method findViewById() for a reused convertView.
     */
    static class ViewHolder {
        public TextView valueName;
        public TextView value;
        public TextView valueUnit;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        ViewHolder viewHolder;

        if (listView == null) {
            // Get the inflater for generating the diagnostic values table
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = inflater.inflate(R.layout.richlist_vehicle_values, null);

            // Create the view holder to recycle the views
            viewHolder = new ViewHolder();
            viewHolder.valueName = (TextView)listView.findViewById(R.id.valueName);
            viewHolder.value = (TextView)listView.findViewById(R.id.value);
            viewHolder.valueUnit = (TextView)listView.findViewById(R.id.unit);
            listView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder)listView.getTag();
        configureViews(position, viewHolder);
        return listView;
    }

    /**
     * Configure the views with the information of the diagnostic values.
     *
     * @param pPosition the position of the current diagnostic value
     * @param pViewHolder the holder of the recycled view
     */
    private void configureViews(final int pPosition, ViewHolder pViewHolder) {
        // Set the values into the text view
        DiagnosticValue diagValue = mDiagnosticValues.get(pPosition);
        pViewHolder.valueName.setText(diagValue.getName());
        pViewHolder.value.setText(diagValue.getValue());
        pViewHolder.valueUnit.setText(diagValue.getUnit());
        pViewHolder.valueName.setId(pPosition);
    }
}
