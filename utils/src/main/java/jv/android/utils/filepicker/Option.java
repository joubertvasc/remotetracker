package jv.android.utils.filepicker;

public class Option implements Comparable<Option>{
	private String name;
	private String data;
	private String path;
	private boolean isDirectory;

	public Option(String n,String d,String p, boolean isDirectory)
	{
		name = n;
		data = d;
		path = p;
		this.isDirectory = isDirectory;
	}
	public String getName()
	{
		return name;
	}
	public String getData()
	{
		return data;
	}
	public String getPath()
	{
		return path;
	}
	@Override
	public int compareTo(Option o) {
		if(this.name != null)
			return this.name.toLowerCase().compareTo(o.getName().toLowerCase()); 
		else 
			throw new IllegalArgumentException();
	}
	public boolean isDirectory() {
		return isDirectory;
	}
	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}
}
