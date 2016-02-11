package com.bliksem.scientificunitconverter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UnitListViewAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<UnitListViewRow> listViewRows;

	public UnitListViewAdapter(Context context, ArrayList<UnitListViewRow> listViewRows)
	{
		this.context = context;
		this.listViewRows = listViewRows;
	}

	@Override
	public int getCount()
	{
		return listViewRows.size();
	}

	@Override
	public Object getItem(int position)
	{
		return listViewRows.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.unit_listview_row_relative, null);
		}

		TextView txtNiceName = (TextView) convertView.findViewById(R.id.unit_listview_row_nicename);
		TextView txtSymbol = (TextView) convertView.findViewById(R.id.unit_listview_row_symbol);
		TextView txtResult = (TextView) convertView.findViewById(R.id.unit_listview_row_result);

		String nicename = listViewRows.get(position).getNiceName();
		String symbol = listViewRows.get(position).getSymbol();
		String result = listViewRows.get(position).getResult();

		txtNiceName.setText(nicename);
		txtSymbol.setText(symbol);
		txtResult.setText(result);

		return convertView;
	}

}
