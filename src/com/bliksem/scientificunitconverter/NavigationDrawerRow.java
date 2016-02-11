package com.bliksem.scientificunitconverter;


public class NavigationDrawerRow
{
	private String img;
	private String title;
	private String example;
	
	public NavigationDrawerRow()
	{
	}

	public NavigationDrawerRow(String title, String img)
	{
		this.title = title;
		this.img = img;
	}

	public String getTitle()
	{
		return this.title;
	}

	public String getImg()
	{
		return this.img;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setImg(String img)
	{
		this.img = img;
	}

	public void setExample(String example)
	{
		this.example = example;
	}

	public String getExample()
	{
		return this.example;
	}

}
