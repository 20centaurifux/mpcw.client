package de.dixieflatline.mpcw.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version
{
	int major;
	int minor;
	int revision;
	
	public Version(int major, int minor, int revision)
	{
		this.major = major;
		this.minor = minor;
		this.revision = revision;
	}
	
	public static final Version parse(String version) throws InvalidVersionFormatException
	{
		Pattern pattern = Pattern.compile("^OK MPD (\\d+)\\.(\\d+)\\.(\\d+)$");
		Matcher matcher = pattern.matcher(version);
		
		if(matcher.find())
		{
			int major = Integer.parseInt(matcher.group(1));
			int minor = Integer.parseInt(matcher.group(2));
			int revision = Integer.parseInt(matcher.group(3));
			
			return new Version(major, minor, revision);
		}
		
		throw new InvalidVersionFormatException();
	}
	
	@Override
	public String toString()
	{
		return String.format("%d.%d.%d", major, minor, revision);
	}
}