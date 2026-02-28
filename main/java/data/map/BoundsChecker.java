package data.map;

import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.map.fullMap.InternalFullMap;
import enums.Direction;

/**
 * There are 2 halfmaps inside one fullmap, mine and the enemy's. Because it can
 * be quite challenging to know which map starts and stops where, i've created
 * the BoundsChecker. This class is supposed to help with the bounds and answer
 * questions like "where is my map?/what direction is the enemy's map?/how big
 * is the map?" The fullmap is either 19x4 or 9x9 (starting to count at 0x0 from
 * the top-left), which means one halfmap is (0-9,0-4) and the other one is
 * (10-19, 0-4) OR one halfmap is (0-9,0-4) and the other one is (0-9,5-9)
 */
public class BoundsChecker
{
	
	private final static Logger logger = LoggerFactory.getLogger(BoundsChecker.class);
	
	private int myHalfMapBeginX = 0;
	private int myHalfMapEndX = 0;
	private int myHalfMapBeginY = 0;
	private int myHalfMapEndY = 0;
	private int enemyHalfMapBeginX = 0;
	private int enemyHalfMapEndX = 0;
	private int enemyHalfMapBeginY = 0;
	private int enemyHalfMapEndY = 0;
	
	private int maxX;
	private int maxY;
	
	public void updateBounds(InternalFullMap internalFullMap)
	{
		int maxX = 0;
		int maxY = 0;
		
		for (Entry<Coordinate, InternalNodeData> eachNode : internalFullMap.getMapData().entrySet())
		{
			if (eachNode.getKey().getX() > maxX)
			{
				maxX = eachNode.getKey().getX();
			}
			if (eachNode.getKey().getY() > maxY)
			{
				maxY = eachNode.getKey().getY();
			}
		}
		
		// if the internalFullMap's size is 50, then the enemy has already sent in
		// his halfmap. this should only happen during the exchange of the maps
		if (internalFullMap.getMapData().size() == 50)
		{
			
			if (maxX == 19)
			{
				this.maxX = 19;
				this.maxY = 4;
				
				logger.info("My half is top/left. Enemy half is right");
				// enemy is right
				enemyHalfMapBeginX = 10;
				enemyHalfMapEndX = 19;
				enemyHalfMapBeginY = 0;
				enemyHalfMapEndY = 4;
				
				// this makes me top/left
				myHalfMapBeginX = 0;
				myHalfMapEndX = 9;
				myHalfMapBeginY = 0;
				myHalfMapEndY = 4;
			}
			else if (maxY == 9)
			{
				this.maxX = 9;
				this.maxY = 9;
				
				logger.info("My half is top/left. Enemy half is down");
				// enemy is down
				enemyHalfMapBeginX = 0;
				enemyHalfMapEndX = 9;
				enemyHalfMapBeginY = 5;
				enemyHalfMapEndY = 9;
				
				// this makes me top/left
				myHalfMapBeginX = 0;
				myHalfMapEndX = 9;
				myHalfMapBeginY = 0;
				myHalfMapEndY = 4;
			}
			else
			{
				logger.info("Enemy half is top/left. Couldn't infer my half yet.");
				// if the enemy is neither right nor at the bottom
				// then that makes him top/left
				enemyHalfMapBeginX = 0;
				enemyHalfMapEndX = 9;
				enemyHalfMapBeginY = 0;
				enemyHalfMapEndY = 4;
				
				// can't infer where i am
				// because i could either be at the right or bottom
			}
		}
		else
		{
			// both half maps are in the internalFullMap
			for (Entry<Coordinate, InternalNodeData> eachNode : internalFullMap.getMapData().entrySet())
			{
				
				if (eachNode.getKey().equals(internalFullMap.getEntities().getMyCastle()))
				{
					
					Coordinate location = eachNode.getKey();
					
					if (location.getY() > 4) // it's down
					{
						logger.info("My half is down. Enemy Half is top/left");
						
						this.maxX = 9;
						this.maxY = 9;
						
						myHalfMapBeginY = 5;
						myHalfMapEndY = 9;
						enemyHalfMapBeginY = 0;
						enemyHalfMapEndY = 4;
						
						enemyHalfMapBeginX = 0;
						enemyHalfMapEndX = 9;
						myHalfMapBeginX = 0;
						myHalfMapEndX = 9;
						
					}
					
					else if (location.getX() > 9) //
					{
						logger.info("My half is right. Enemy Half is top/left");
						
						maxX = 19;
						maxY = 4;
						
						myHalfMapBeginY = 0;
						myHalfMapEndY = 4;
						enemyHalfMapBeginY = 0;
						enemyHalfMapEndY = 4;
						
						myHalfMapBeginX = 10;
						myHalfMapEndX = 19;
						enemyHalfMapBeginX = 0;
						enemyHalfMapEndX = 9;
					}
					else // (it's up left)
					{
						myHalfMapBeginY = 0;
						myHalfMapEndY = 4;
						myHalfMapBeginX = 0;
						myHalfMapEndX = 9;
						
						if (maxX == 19)
						{
							logger.info("My half is top/left. Enemy Half is right");
							
							maxX = 19;
							maxY = 4;
							
							enemyHalfMapBeginX = 10;
							enemyHalfMapEndX = 19;
							enemyHalfMapBeginY = 0;
							enemyHalfMapEndY = 4;
						}
						if (maxY == 9)
						{
							logger.info("My half is top/left. Enemy Half is down");
							
							maxX = 9;
							maxY = 9;
							
							enemyHalfMapBeginX = 0;
							enemyHalfMapEndX = 9;
							enemyHalfMapBeginY = 5;
							enemyHalfMapEndY = 9;
						}
						
					}
					
				}
			}
		}
		
	}
	
	public Direction getEnemyHalfMapDirection()
	{
		if (this.enemyHalfMapBeginX == 0 && this.enemyHalfMapBeginY == 0)
		{
			if (this.myHalfMapBeginX == 0 && this.myHalfMapBeginY == 5)
			{
				return Direction.Up;
			}
			return Direction.Left;
			
		}
		else if (this.enemyHalfMapBeginX == 0 && this.enemyHalfMapBeginY == 5)
		{
			return Direction.Down;
		}
		return Direction.Right;
	}
	
	public HashMap<Direction, Integer> getEnemyHalfMapBounds()
	{
		HashMap<Direction, Integer> result = new HashMap<Direction, Integer>();
		
		result.put(Direction.Left, enemyHalfMapBeginX);
		result.put(Direction.Right, enemyHalfMapEndX);
		result.put(Direction.Up, enemyHalfMapBeginY);
		result.put(Direction.Down, enemyHalfMapEndY);
		
		return result;
	}
	
	public HashMap<Direction, Integer> getMyHalfMapBounds()
	{
		HashMap<Direction, Integer> result = new HashMap<Direction, Integer>();
		result.put(Direction.Left, myHalfMapBeginX);
		result.put(Direction.Right, myHalfMapEndX);
		result.put(Direction.Up, myHalfMapBeginY);
		result.put(Direction.Down, myHalfMapEndY);
		
		return result;
	}
	
	public boolean checkMyHalfMapBounds(Coordinate coordinate)
	{
		if (coordinate.getX() >= myHalfMapBeginX && coordinate.getX() <= myHalfMapEndX && coordinate.getY() >= myHalfMapBeginY && coordinate.getY() <= myHalfMapEndY)
		{
			return true;
		}
		return false;
	}
	
}
