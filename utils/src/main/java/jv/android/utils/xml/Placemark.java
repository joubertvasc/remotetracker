package jv.android.utils.xml;

public class Placemark
{
	String address;
	String coordinates;
	String description;
	String title;

	public String getAddress()
	{
		return this.address;
	}

	public String getCoordinates()
	{
		return this.coordinates;
	}

	public String getDescription()
	{
		return this.description;
	}

	public String getTitle()
	{
		return this.title;
	}

	public void setAddress(String paramString)
	{
		this.address = paramString;
	}

	public void setCoordinates(String paramString)
	{
		this.coordinates = paramString;
	}

	public void setDescription(String paramString)
	{
		this.description = paramString;
	}

	public void setTitle(String paramString)
	{
		this.title = paramString;
	}
}