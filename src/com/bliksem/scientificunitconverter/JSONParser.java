package com.bliksem.scientificunitconverter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser
{

	private JSONParser()
	{
	}

	public static JSONParser getInstance()
	{
		return INSTANCE;
	}

	private static final JSONParser INSTANCE = new JSONParser();

	public JSONObject getJSONObject(InputStream is)
	{

		JSONObject jsonObject = null;
		String s = null;

		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			is.close();
			s = sb.toString();
		} catch (Exception e)
		{
			Log.e("VICERROR", "Error converting result " + e.toString());
		}
		
		try
		{
			jsonObject = new JSONObject(s);
		} catch (JSONException e)
		{
			Log.e("VICERROR", "Error parsing data " + e.toString());
		}

		return jsonObject;

	}

}
