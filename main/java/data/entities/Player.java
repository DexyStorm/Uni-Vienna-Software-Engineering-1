package data.entities;

import data.map.Coordinate;
import messagesbase.messagesfromserver.PlayerState;

public class Player
{
	boolean treasureCollected;
	Coordinate location;
	
	public Player(Coordinate location, boolean treasureCollected)
	{
		this.location = location;
		this.treasureCollected = treasureCollected;
	}
	
	public void updateTreasure(PlayerState externalPlayer)
	{
		
		if (this.treasureCollected == false)
		{
			if (externalPlayer.hasCollectedTreasure() == true)
			{
				this.treasureCollected = true;
				
			}
			
		}
		
	}
	
	public Coordinate getLocation()
	{
		return location;
	}
	
	public void setLocation(Coordinate location)
	{
		this.location = location;
	}
	
	public void setTreasureCollected(boolean treasureCollected)
	{
		this.treasureCollected = treasureCollected;
	}
	
	public boolean ifTreasureCollected()
	{
		return treasureCollected;
	}
	
	@Override
	public String toString()
	{
		return "[location=" + location + ", treasureCollected=" + treasureCollected + "]";
	}
	
}