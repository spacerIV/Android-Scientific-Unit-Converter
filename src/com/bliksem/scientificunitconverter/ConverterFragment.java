package com.bliksem.scientificunitconverter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import javax.measure.unit.Unit;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class ConverterFragment extends Fragment implements OnItemSelectedListener
{

	TreeMap<String, String> unitSymbols = new TreeMap<String, String>();
	HashMap<String, Unit<?>> unitObjects = new HashMap<String, Unit<?>>();
	TreeMap<String, String> unitNiceNames = new TreeMap<String, String>();
	TreeMap<String, String> unitConversionUnits = new TreeMap<String, String>();
	TreeMap<String, Double> unitTimes = new TreeMap<String, Double>();
	TreeMap<String, Double> unitDivides = new TreeMap<String, Double>();

	private TreeMap<Integer, JSONObject> groupJSONObjects = new TreeMap<Integer, JSONObject>();

	private ArrayList<UnitListViewRow> unitListViewRows = new ArrayList<UnitListViewRow>();
	private UnitListViewAdapter unitListViewAdapter;
	ArrayList<String> niceNamesList;

	private HashMap<String, String> specialChars = new HashMap<String, String>();

	private ArrayList<Unit<?>> spinnerLookup = new ArrayList<Unit<?>>();

	Spinner spinner;
	Integer spinner_position;
	EditText amount;
	ListView listView;

	Converter converter;
	JSONParser jsonParser;
	DataStore dataStore;

	DecimalFormat decimalFormat;

	public ConverterFragment()
	{
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		Bundle b = getArguments();
		Integer unitGroup = b.getInt("unitGroup");

		converter = Converter.getInstance();
		dataStore = DataStore.getInstance();
		jsonParser = JSONParser.getInstance();

		if (!dataStore.isInitDone())
			dataStore.init(getActivity().getApplicationContext());

		groupJSONObjects = dataStore.getGroupJSONObjects();

		try
		{
			dataStore.parseUnits(groupJSONObjects.get(unitGroup));
		} catch (JSONException e)
		{
			e.printStackTrace();
		}

		unitSymbols = dataStore.getUnitSymbols();
		unitNiceNames = dataStore.getUnitNiceNames();
		unitConversionUnits = dataStore.getUnitConversionUnits();
		unitTimes = dataStore.getUnitTimes();
		unitDivides = dataStore.getUnitDivides();

		unitObjects = converter.getAllUnits(unitConversionUnits, unitTimes, unitDivides);

		specialChars.put("[squared]", "\u00B2");
		specialChars.put("[micro]", "\u00B5");
		specialChars.put("[earth]", "\u2080");
		specialChars.put("[degree]", "\u00B0");

		decimalFormat = new DecimalFormat("#.##########");

		View rootView = inflater.inflate(R.layout.acceleration_fragment, container, false);

		amount = (EditText) rootView.findViewById(R.id.amount);
		spinner = (Spinner) rootView.findViewById(R.id.spinner);
		spinner_position = 0;
		listView = (ListView) rootView.findViewById(R.id.listview);

		niceNamesList = new ArrayList<String>();

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		for (String key : unitNiceNames.keySet())
		{
			String s = null;
			for (String k : specialChars.keySet())
			{
				s = unitNiceNames.get(key).replace(k, specialChars.get(k));
			}
			niceNamesList.add(s);
			spinnerLookup.add(unitObjects.get(key));
		}

		amount.addTextChangedListener(new TextWatcher()
		{
			public void afterTextChanged(Editable s)
			{
				// Log.d("VIC", "afterTextChanged: " + s.toString());
				unitListViewAdapter.notifyDataSetChanged();
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}
		});

		this.populateDataSet(spinner_position, Double.parseDouble(amount.getText().toString()));

		spinner.setOnItemSelectedListener(this);

		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, niceNamesList);

		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(spinner_adapter);

		unitListViewAdapter = new UnitListViewAdapter(getActivity().getApplicationContext(), unitListViewRows);

		listView.setAdapter(unitListViewAdapter);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		spinner_position = position;
		Log.d("YOYOOYOY", "Spinner position:" + spinner_position);
		unitListViewRows.clear();
		this.populateDataSet(position, Double.parseDouble(amount.getText().toString()));
		((BaseAdapter) listView.getAdapter()).notifyDataSetChanged(); 
		//unitListViewAdapter.notifyDataSetChanged();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0)
	{
	}

	private void populateDataSet(Integer position, Double amount)
	{


		for (String key : unitNiceNames.keySet())
		{
			String nicename = unitNiceNames.get(key);
			String symbol = unitSymbols.get(key);

			// ///////////
			// convert! //
			// ///////////

			Log.d("VICTOR", niceNamesList.get(position) + " to " + key);
			
			Double d = spinnerLookup.get(position).getConverterTo(unitObjects.get(key)).convert(amount);

			String result = decimalFormat.format(d).toString();

			//Log.d("VICTOR", niceNamesList.get(position) + " to " + unitNiceNames.get(key) + " = " + d.toString());

			// replace special characters
			for (String k : specialChars.keySet())
			{
				nicename = nicename.replace(k, specialChars.get(k));
				symbol = symbol.replace(k, specialChars.get(k));
			}

			unitListViewRows.add(new UnitListViewRow(nicename, symbol, d.toString()));
		//	unitListViewRows.add(new UnitListViewRow(nicename, symbol, result));
		}

	}

}
