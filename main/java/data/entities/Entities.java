package data.entities;

import data.map.Coordinate;

public class Entities
{
	Coordinate myCastle;
	Coordinate enemyCastle;
	Player myPlayer;
	Player enemyPlayer;
	Coordinate treasure;
	
	public Entities(Coordinate myCastle, Coordinate enemyCastle, Player myPlayer, Player enemyPlayer, Coordinate treasure)
	{
		this.myCastle = myCastle;
		this.enemyCastle = enemyCastle;
		this.myPlayer = myPlayer;
		this.enemyPlayer = enemyPlayer;
		this.treasure = treasure;
	}
	
	public void setEntities(Entities newEntities)
	{
		
		this.myCastle = newEntities.myCastle;
		this.enemyCastle = newEntities.enemyCastle;
		this.myPlayer = newEntities.myPlayer;
		this.enemyPlayer = newEntities.enemyPlayer;
		this.treasure = newEntities.treasure;
		
	}
	
	public Coordinate getMyCastle()
	{
		return myCastle;
	}
	
	public void setMyCastle(Coordinate myCastle)
	{
		this.myCastle = myCastle;
	}
	
	public Coordinate getEnemyCastle()
	{
		return enemyCastle;
	}
	
	public void setEnemyCastle(Coordinate enemyCastle)
	{
		this.enemyCastle = enemyCastle;
	}
	
	public Player getMyPlayer()
	{
		return myPlayer;
	}
	
	public void setMyPlayer(Player myPlayer)
	{
		this.myPlayer = myPlayer;
	}
	
	public Player getEnemyPlayer()
	{
		return enemyPlayer;
	}
	
	public void setEnemyPlayer(Player enemyPlayer)
	{
		this.enemyPlayer = enemyPlayer;
	}
	
	public Coordinate getTreasure()
	{
		return treasure;
	}
	
	public void setTreasure(Coordinate treasure)
	{
		this.treasure = treasure;
	}
	
	@Override
	public String toString()
	{
		return "[myCastle=" + myCastle + "\nenemyCastle=" + enemyCastle + "\nmyPlayer=" + myPlayer + "\nenemyPlayer=" + enemyPlayer + "\ntreasure=" + treasure + "]";
	}
	
}
