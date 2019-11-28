package jv.android.utils.latlonconvert;

import java.text.DecimalFormat;

public class Decimal2DMS {
	public static DecimalFormat df = new DecimalFormat("#0.00000");
	public double dd;
	public int degree;
	public int minute;
	public double second;

	public Decimal2DMS(double paramDouble)
	{
		this.dd = paramDouble;
	}

	public static Decimal2DMS fromDdToDms(double paramDouble)
	{
		boolean bool = paramDouble < 0.0D;
		Decimal2DMS localGeoCoordinate = null;
		if (bool)
		{
			localGeoCoordinate = new Decimal2DMS(paramDouble);
			localGeoCoordinate.degree = ((int)paramDouble);
			double d = 60.0D * (paramDouble - localGeoCoordinate.degree);
			localGeoCoordinate.minute = ((int)d);
			localGeoCoordinate.second = (60.0D * (d - localGeoCoordinate.minute));
		}
		return localGeoCoordinate;
	}

	public static String fromDmsToDd(int paramInt1, int paramInt2, double paramDouble)
	{
		double d = (paramDouble + paramInt2 * 60) / 3600.0D;
		return df.format(d + paramInt1);
	}
}
