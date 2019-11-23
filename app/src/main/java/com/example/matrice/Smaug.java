package com.example.matrice;

import java.util.HashMap;

public class Smaug extends HashMap<String, Object>
{
	private static final Smaug ourInstance = new Smaug();
	
	public static Smaug getInstance()
	{
		return ourInstance;
	}
}
