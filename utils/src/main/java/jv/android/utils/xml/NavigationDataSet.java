package jv.android.utils.xml;

import java.util.ArrayList;
import java.util.Iterator;

public class NavigationDataSet
{
	private Placemark currentPlacemark;
	private ArrayList<Placemark> placemarks = new ArrayList<Placemark>();
	private Placemark routePlacemark;

	public void addCurrentPlacemark()
	{
		this.placemarks.add(this.currentPlacemark);
	}

	public Placemark getCurrentPlacemark()
	{
		return this.currentPlacemark;
	}

	public ArrayList<Placemark> getPlacemarks()
	{
		return this.placemarks;
	}

	public Placemark getRoutePlacemark()
	{
		return this.routePlacemark;
	}

	public void setCurrentPlacemark(Placemark paramPlacemark)
	{
		this.currentPlacemark = paramPlacemark;
	}

	public void setPlacemarks(ArrayList<Placemark> paramArrayList)
	{
		this.placemarks = paramArrayList;
	}

	public void setRoutePlacemark(Placemark paramPlacemark)
	{
		this.routePlacemark = paramPlacemark;
	}

	public String toString()
	{
		String str = "";
		@SuppressWarnings("rawtypes")
		Iterator localIterator = this.placemarks.iterator();
		while (true)
		{
			if (!localIterator.hasNext())
				return str;
			Placemark localPlacemark = (Placemark)localIterator.next();
			str = str + localPlacemark.getTitle() + "\n" + localPlacemark.getDescription() + "\n\n";
		}
	}
}