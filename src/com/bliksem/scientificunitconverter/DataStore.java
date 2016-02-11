package com.bliksem.scientificunitconverter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.measure.unit.Unit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class DataStore
{
	Context context;

	private TreeMap<Integer, String> groupNames = new TreeMap<Integer, String>();
	private TreeMap<Integer, String> groupIcons = new TreeMap<Integer, String>();
	private TreeMap<Integer, JSONObject> groupJSONObjects = new TreeMap<Integer, JSONObject>();

	TreeMap<String, String> unitSymbols = new TreeMap<String, String>();
	HashMap<String, Unit<?>> unitObjects = new HashMap<String, Unit<?>>();
	TreeMap<String, String> unitNiceNames = new TreeMap<String, String>();
	TreeMap<String, String> unitConversionUnits = new TreeMap<String, String>();
	TreeMap<String, Double> unitTimes = new TreeMap<String, Double>();
	TreeMap<String, Double> unitDivides = new TreeMap<String, Double>();

	private boolean initDone = false;

	public static DataStore getInstance()
	{
		return INSTANCE;
	}

	private static final DataStore INSTANCE = new DataStore();

	public void init(Context c)
	{
		if (context == null)
		{
			context = c;
		}

		try
		{
			parse_asset_files();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		initDone = true;
	}

	private void parse_asset_files() throws IOException, JSONException
	{

		JSONParser jsonparser = JSONParser.getInstance();
		Pattern p = Pattern.compile("\\w+\\.json");
		AssetManager assetManager = context.getAssets();
		String[] files = assetManager.list("");

		Arrays.sort(files);

		int num = 0;
		for (String s : files)
		{
			Matcher m = p.matcher(s);

			if (m.matches())
			{
				// group JSONObjects

				InputStream is = assetManager.open(s);
				JSONObject jsonObject = jsonparser.getJSONObject(is);
				groupJSONObjects.put(num, jsonObject);

				// group name & icon string

				JSONArray g = jsonObject.getJSONArray("group");
				groupNames.put(num, g.getJSONObject(0).getString("group"));
				groupIcons.put(num, g.getJSONObject(0).getString("icon"));

			}

			num++;
		}

		for (Integer key : groupNames.keySet())
		{
		//	Log.d("VIC", "NAME: " + key + " " + groupNames.get(key));
		//	Log.d("VIC", "ICON: " + key + " " + groupIcons.get(key));
		}
	}

	public TreeMap<Integer, String> getGroupNames()
	{
		return groupNames;
	}

	public TreeMap<Integer, String> getGroupIcons()
	{
		return groupIcons;
	}

	public TreeMap<Integer, JSONObject> getGroupJSONObjects()
	{
		return groupJSONObjects;
	}

	public boolean isInitDone()
	{
		return initDone;
	}

	public TreeMap<String, String> getUnitSymbols()
	{

		return unitSymbols;
	}

	public void parseUnits(JSONObject j) throws JSONException
	{

		unitNiceNames.clear();
		unitSymbols.clear();
		unitConversionUnits.clear();
		unitTimes.clear();
		unitDivides.clear();
		
		JSONArray units = j.getJSONArray("units");

		for (int x = 0; x < units.length(); x++)
		{
			JSONObject unit = (JSONObject) units.get(x);
			String name = unit.getString("name");
			String symbol = unit.getString("symbol");
			String niceName = unit.getString("nicename");
			String conversionUnit = unit.getString("conversionunit");
			String times = unit.getString("times");
			String divide = unit.getString("divide");


			//Log.d("VIC", name + " " + symbol + " " + niceName + " " + conversionUnit + " " + times);

			unitNiceNames.put(name, niceName);
			unitSymbols.put(name, symbol);

			if (conversionUnit.length() > 0)
				unitConversionUnits.put(name, conversionUnit);

			if (times.length() > 0)
				unitTimes.put(name, Double.parseDouble(times));
			
			if (divide.length() > 0)
				unitDivides.put(name,  Double.parseDouble(divide));

		}
	}

	public TreeMap<String, String> getUnitNiceNames()
	{
		return unitNiceNames;
	}

	public TreeMap<String, String> getUnitConversionUnits()
	{

		return unitConversionUnits;
	}

	public TreeMap<String, Double> getUnitTimes()
	{
		return unitTimes;
	}
	
	public TreeMap<String, Double> getUnitDivides()
	{
		return unitDivides;
	}


}
