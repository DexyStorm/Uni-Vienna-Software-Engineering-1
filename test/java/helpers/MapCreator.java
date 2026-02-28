package helpers;

import java.util.HashMap;
import java.util.Map.Entry;

import data.entities.Entities;
import data.entities.Player;
import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.fullMap.InternalFullMap;
import data.map.halfMap.InternalHalfMap;
import enums.Direction;
import enums.InternalExploredState;
import enums.Terrain;

public class MapCreator
{
	
	private Entities setUpEntities()
	{
		
		Coordinate defaultCoordinate = new Coordinate(-1, -1);
		Player myPlayer = new Player(defaultCoordinate, false);
		Player enemyPlayer = new Player(defaultCoordinate, false);
		Entities entities = new Entities(defaultCoordinate, defaultCoordinate, myPlayer, enemyPlayer, defaultCoordinate);
		
		return entities;
	}
	
	private HashMap<Coordinate, InternalNodeData> setUpMapData(int maxX, int maxY)
	{
		HashMap<Coordinate, InternalNodeData> mapData = new HashMap<Coordinate, InternalNodeData>();
		for (int y = 0; y <= maxY; ++y)
		{
			for (int x = 0; x <= maxX; ++x)
			{
				Coordinate coordinate = new Coordinate(x, y);
				mapData.put(coordinate, new InternalNodeData(Terrain.Grass, InternalExploredState.NotExplored));
			}
		}
		
		return mapData;
	}
	
	private Terrain extractTerrain(String string)
	{
		if (string == "g")
		{
			return Terrain.Grass;
		}
		if (string == "m")
		{
			return Terrain.Mountain;
		}
		return Terrain.Water;
	}
	
	public InternalHalfMap setUpHalfMap()
	{
		Entities entities = setUpEntities();
		HashMap<Coordinate, InternalNodeData> mapData = setUpMapData(9, 4);
		
		InternalHalfMap internalHalfMap = new InternalHalfMap(entities, mapData);
		return internalHalfMap;
	}
	
	public InternalFullMap setUpFullMapXxY(int x, int y)
	{
		Entities entities = setUpEntities();
		HashMap<Coordinate, InternalNodeData> mapData = setUpMapData(x, y);
		
		InternalFullMap InternalFullMap = new InternalFullMap(entities, mapData);
		return InternalFullMap;
	}
	
	public InternalFullMap setUpFullMap(String[][] nodeArray)
	{
		HashMap<Coordinate, InternalNodeData> mapData = new HashMap<Coordinate, InternalNodeData>();
		Entities entities = setUpEntities();
		
		for (int y = 0; y < nodeArray.length; ++y)
		{
			for (int x = 0; x < nodeArray[0].length; ++x)
			{
				InternalNodeData internalNodeData = new InternalNodeData(extractTerrain(nodeArray[y][x]), InternalExploredState.NotExplored);
				mapData.put(new Coordinate(x, y), internalNodeData);
				
			}
		}
		
		InternalFullMap internalFullMap = new InternalFullMap(entities, mapData);
		return internalFullMap;
	}
	
	public InternalHalfMap setUpHalfMap(String[][] nodeArray)
	{
		HashMap<Coordinate, InternalNodeData> mapData = new HashMap<Coordinate, InternalNodeData>();
		Entities entities = setUpEntities();
		
		for (int y = 0; y < nodeArray.length; ++y)
		{
			
			for (int x = 0; x < nodeArray[0].length; ++x)
			{
				InternalNodeData internalNodeData = new InternalNodeData(extractTerrain(nodeArray[y][x]), InternalExploredState.NotExplored);
				mapData.put(new Coordinate(x, y), internalNodeData);
			}
		}
		InternalHalfMap internalHalfMap = new InternalHalfMap(entities, mapData);
		
		return internalHalfMap;
	}
	
	public void moveHalfMap(Direction direction, InternalHalfMap internalHalfMap)
	{
		HashMap<Coordinate, InternalNodeData> newMapData = new HashMap<Coordinate, InternalNodeData>();
		
		switch (direction)
		{
			case Direction.Right:
				for (Entry<Coordinate, InternalNodeData> eachNode : internalHalfMap.getMapData().entrySet())
				{
					Coordinate newNode = new Coordinate(eachNode.getKey().getX() + 10, eachNode.getKey().getY());
					newMapData.put(newNode, eachNode.getValue());
				}
				break;
			
			case Direction.Down:
				for (Entry<Coordinate, InternalNodeData> eachNode : internalHalfMap.getMapData().entrySet())
				{
					Coordinate newNode = new Coordinate(eachNode.getKey().getX(), eachNode.getKey().getY() + 5);
					newMapData.put(newNode, eachNode.getValue());
				}
				break;
		}
		
		internalHalfMap.setMapData(newMapData);
	}
	
}
