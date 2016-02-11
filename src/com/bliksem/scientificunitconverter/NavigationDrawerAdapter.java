package com.bliksem.scientificunitconverter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joanzapata.android.iconify.Iconify;

public class NavigationDrawerAdapter extends BaseAdapter
{

	private Context context;
	private ArrayList<NavigationDrawerRow> navDrawerRows;

	public NavigationDrawerAdapter(Context context, ArrayList<NavigationDrawerRow> navDrawerItems)
	{
		this.context = context;
		this.navDrawerRows = navDrawerItems;	
	}

	@Override
	public int getCount()
	{
		return navDrawerRows.size();
	}

	@Override
	public Object getItem(int position)
	{
		return navDrawerRows.get(position);
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
			convertView = mInflater.inflate(R.layout.navigation_drawer_row, null);
		}

		TextView txtImg = (TextView) convertView.findViewById(R.id.navdrawerrow_image);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.navdrawerrow_title);
		TextView txtExample = (TextView) convertView.findViewById(R.id.navdrawerrow_example);

		txtImg.setText("{" + navDrawerRows.get(position).getImg() + "}");
		txtTitle.setText(navDrawerRows.get(position).getTitle());
		txtExample.setText("example, example, example");

		Iconify.addIcons(txtImg);

		return convertView;
	}

}
