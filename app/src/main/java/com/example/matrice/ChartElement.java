package com.example.matrice;

public class ChartElement
{
	public String username;
	public String xp;
	public String hp;
	
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username=username; }
	public String getXp() { return xp; }
	public void setXp(String xp) { this.xp=xp; }
	public String getHp() { return hp; }
	public void setHp(String hp) { this.hp=hp; }
	
	public ChartElement()
	{
		username = "(no name)";
		xp = "0";
		hp = "100";
	}
}
