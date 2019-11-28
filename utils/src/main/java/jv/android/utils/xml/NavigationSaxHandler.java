package jv.android.utils.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NavigationSaxHandler extends DefaultHandler
{
//private boolean in_coordinatestag = false;
//private boolean in_descriptiontag = false;
//private boolean in_geometrycollectiontag = false;
//	private boolean in_kmltag = false;
//private boolean in_linestringtag = false;
	private boolean in_nametag = false;
	private boolean in_placemarktag = false;
//private boolean in_pointtag = false;
	private NavigationDataSet navigationDataSet = new NavigationDataSet();

	public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
	{
		if ((this.in_nametag) && (this.in_placemarktag))
		{
			if (this.navigationDataSet.getCurrentPlacemark() == null)
				this.navigationDataSet.setCurrentPlacemark(new Placemark());
			this.navigationDataSet.getCurrentPlacemark().setTitle(new String(paramArrayOfChar, paramInt1, paramInt2));
		}
		while (true)
		{
			return;
/*	if (this.in_descriptiontag)
			{
				if (this.navigationDataSet.getCurrentPlacemark() == null)
					this.navigationDataSet.setCurrentPlacemark(new Placemark());
				this.navigationDataSet.getCurrentPlacemark().setDescription(new String(paramArrayOfChar, paramInt1, paramInt2));
			}
			else if (this.in_coordinatestag)
			{
				if (this.navigationDataSet.getCurrentPlacemark() == null)
					this.navigationDataSet.setCurrentPlacemark(new Placemark());
				this.navigationDataSet.getCurrentPlacemark().setCoordinates(new String(paramArrayOfChar, paramInt1, paramInt2));
			} /**/
		}
	}

	public void endDocument()
			throws SAXException
	{
	}

	public void endElement(String paramString1, String paramString2, String paramString3)
			throws SAXException
	{
//		if (paramString3.equals("kml"))
//			this.in_kmltag = false;
		while (true)
		{
			return;
/*	if (paramString3.equals("Placemark"))
			{
				this.in_placemarktag = false;
				if ("Route".equals(this.navigationDataSet.getCurrentPlacemark().getTitle()))
					this.navigationDataSet.setRoutePlacemark(this.navigationDataSet.getCurrentPlacemark());
				else
					this.navigationDataSet.addCurrentPlacemark();
			}
			else if (paramString3.equals("name"))
			{
				this.in_nametag = false;
			}
			else if (paramString3.equals("description"))
			{
				this.in_descriptiontag = false;
			}
			else if (paramString3.equals("GeometryCollection"))
			{
				this.in_geometrycollectiontag = false;
			}
			else if (paramString3.equals("LineString"))
			{
				this.in_linestringtag = false;
			}
			else if (paramString3.equals("point"))
			{
				this.in_pointtag = false;
			}
			else if (paramString3.equals("coordinates"))
			{
				this.in_coordinatestag = false;
			} /**/
		}
	}

	public NavigationDataSet getParsedData()
	{
		return this.navigationDataSet;
	}

	public void startDocument()
			throws SAXException
	{
		this.navigationDataSet = new NavigationDataSet();
	}

	public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
			throws SAXException
	{
//		if (paramString3.equals("kml"))
//			this.in_kmltag = true;
		while (true)
		{
			return;
			/* (paramString3.equals("Placemark"))
			{
				this.in_placemarktag = true;
				this.navigationDataSet.setCurrentPlacemark(new Placemark());
			}
			else if (paramString3.equals("name"))
			{
				this.in_nametag = true;
			}
			else if (paramString3.equals("description"))
			{
				this.in_descriptiontag = true;
			}
			else if (paramString3.equals("GeometryCollection"))
			{
				this.in_geometrycollectiontag = true;
			}
			else if (paramString3.equals("LineString"))
			{
				this.in_linestringtag = true;
			}
			else if (paramString3.equals("point"))
			{
				this.in_pointtag = true;
			}
			else if (paramString3.equals("coordinates"))
			{
				this.in_coordinatestag = true;
			} /**/
		}
	}
}