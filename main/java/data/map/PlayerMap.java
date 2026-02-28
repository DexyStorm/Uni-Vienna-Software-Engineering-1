package data.map;

import java.util.HashMap;

import data.entities.Entities;

public abstract class PlayerMap
{
	protected Entities entities;
	protected HashMap<Coordinate, InternalNodeData> mapData;
	
	public PlayerMap(Entities entities, HashMap<Coordinate, InternalNodeData> mapData)
	{
		this.entities = entities;
		this.mapData = mapData;
	}
	
	public boolean checkIfLocationIsInsideMap(Coordinate location)
	{
		if (this.getMapData().containsKey(location))
		{
			return true;
		}
		
		return false;
	}
	
	public Entities getEntities()
	{
		return entities;
	}
	
	public HashMap<Coordinate, InternalNodeData> getMapData()
	{
		return mapData;
	}
	
	public InternalNodeData getNodeData(Coordinate coordinate)
	{
		return mapData.get(coordinate);
	}
	
}
