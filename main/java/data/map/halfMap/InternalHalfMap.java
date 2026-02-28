package data.map.halfMap;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.entities.Entities;
import data.map.BoundsChecker;
import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.PlayerMap;
import data.map.fullMap.InternalFullMap;
import enums.Direction;
import enums.InternalExploredState;
import enums.Terrain;
import observerInterfaces.MapObserver;
import view.InternalHalfMapView;

/**
 * the internal halfmap which i generate is ***ALWAYS*** have a size of 9x4
 * (starting to count at (0,0) from the top-left). the map might get moved later
 * once the exchange with the server has happened
 */
public class InternalHalfMap extends PlayerMap implements MapObserver
{
	private final static Logger logger = LoggerFactory.getLogger(InternalHalfMap.class);
	
	public InternalHalfMap(Entities entities, HashMap<Coordinate, InternalNodeData> mapData)
	{
		super(entities, mapData);
	}
	
	public void generateInternalHalfMap()
	{
		this.mapData.clear();
		
		Random rand = new Random();
		HashMap<Coordinate, InternalNodeData> halfMapData = new HashMap<Coordinate, InternalNodeData>();
		
		for (int y = 0; y <= 4; ++y)
		{
			for (int x = 0; x <= 9; ++x)
			{
				
				Coordinate newLocation = new Coordinate(x, y);
				Terrain terrain = Terrain.Grass;
				
				// generates a random number between 0 and 100
				int random = rand.nextInt(100);
				// 0-8 -> mountain
				// 9-20 -> water
				// 21-100 -> grass
				
				if (random >= 21)
				{
					terrain = Terrain.Grass;
					
				}
				else if (random >= 9 && random <= 20) // water
				{
					terrain = Terrain.Water;
					
				}
				else // (random >= 0 && random <= 8) //mountain
				{
					terrain = Terrain.Mountain;
					
				}
				InternalNodeData internalNodeData = new InternalNodeData(terrain, InternalExploredState.NotExplored);
				halfMapData.put(newLocation, internalNodeData);
				
			}
		}
		
		boolean fortPlaced = false;
		while (fortPlaced == false)
		{
			for (Entry<Coordinate, InternalNodeData> eachNode : halfMapData.entrySet())
			{
				if (fortPlaced == false) // useless if u use break
				{
					if (eachNode.getValue().getTerrain() == Terrain.Grass)
					{
						int random = rand.nextInt(100);
						
						if (random == 1)
						{
							entities.setMyCastle(eachNode.getKey());
							fortPlaced = true;
							break; // dont know if break is bad cuz in pr1 we were told to not use it
						}
					}
				}
				
			}
		}
		
		this.mapData = halfMapData;
		
	}
	
	public void updateCoordinates(BoundsChecker boundsChecker)
	{
		HashMap<Coordinate, InternalNodeData> newMapData = new HashMap<Coordinate, InternalNodeData>();
		
		if (boundsChecker.getMyHalfMapBounds().get(Direction.Down) == 9)
		{
			for (Entry<Coordinate, InternalNodeData> eachNode : this.mapData.entrySet())
			{
				// moves the entire halfmap 5 coordinates down
				Coordinate newCoordinate = new Coordinate(eachNode.getKey().getX(), eachNode.getKey().getY() + 5);
				newMapData.put(newCoordinate, eachNode.getValue());
			}
			
			this.mapData = newMapData;
			logger.info("Moved internalMyHalfMap down");
		}
		else if (boundsChecker.getMyHalfMapBounds().get(Direction.Right) == 19)
		{
			
			for (Entry<Coordinate, InternalNodeData> eachNode : this.mapData.entrySet())
			{
				// moves the entire halfmap 5 coordinates to the right
				Coordinate newCoordinate = new Coordinate(eachNode.getKey().getX() + 10, eachNode.getKey().getY());
				newMapData.put(newCoordinate, eachNode.getValue());
			}
			
			this.mapData = newMapData;
			logger.info("Moved internalMyHalfMap right");
		}
		
	}
	
	// setter
	public void setMapData(HashMap<Coordinate, InternalNodeData> newMapData)
	{
		this.mapData = newMapData;
	}
	
	@Override
	public String toString()
	{
		StringBuilder output = new StringBuilder();
		InternalHalfMapView internalHalfMapView = new InternalHalfMapView();
		int mountainCounter = 0;
		int grassCounter = 0;
		int waterCounter = 0;
		int myCastleCounter = 0;
		
		for (Entry<Coordinate, InternalNodeData> eachNode : this.mapData.entrySet())
		{
			if (eachNode.getValue().getTerrain() == Terrain.Grass)
			{
				grassCounter = grassCounter + 1;
				if (eachNode.getKey() == this.getEntities().getMyCastle())
				{
					myCastleCounter = myCastleCounter + 1;
				}
			}
			else if (eachNode.getValue().getTerrain() == Terrain.Mountain)
			{
				mountainCounter = mountainCounter + 1;
			}
			else if (eachNode.getValue().getTerrain() == Terrain.Water)
			{
				waterCounter = waterCounter + 1;
			}
			
		}
		
		output = internalHalfMapView.getOutputStringForInternalHalfMap(this);
		output.append("\n");
		output.append("Grass: " + grassCounter + "\n");
		output.append("Mountain: " + mountainCounter + "\n");
		output.append("Water: " + waterCounter + "\n");
		output.append("MyCastle: " + myCastleCounter + "\n");
		
		return output.toString();
		
	}
	
	@Override
	public void updateMapObserver(InternalFullMap internalFullMap, int roundCounter)
	{
		HalfMapExtractor halfMapExtractor = new HalfMapExtractor();
		
		this.mapData = halfMapExtractor.extractUpdate(internalFullMap, this.getMapData());
		
	}
	
}
