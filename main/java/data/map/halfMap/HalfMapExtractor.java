package data.map.halfMap;

import java.util.HashMap;
import java.util.Map.Entry;

import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.fullMap.InternalFullMap;

public class HalfMapExtractor
{
	
	public HashMap<Coordinate, InternalNodeData> extractEnemyHalfMap(InternalFullMap internalFullMap, InternalHalfMap internalMyHalfMap)
	{
		HashMap<Coordinate, InternalNodeData> result = new HashMap<Coordinate, InternalNodeData>();
		
		// if the internalFullMap's size is 50, then the enemy has already sent in
		// his halfmap. this should only happen during the exchange of the maps
		if (internalFullMap.getMapData().size() == 50)
		{
			for (Entry<Coordinate, InternalNodeData> eachEnemyNode : internalFullMap.getMapData().entrySet())
			{
				result.put(eachEnemyNode.getKey(), eachEnemyNode.getValue());
			}
		}
		else
		{
			// both HalfMaps are in the FullMap
			// so just go through all the nodes and add all the nodes into
			// result that are not in "internalMyHalfMap"
			for (Entry<Coordinate, InternalNodeData> eachHalfMapNode : internalFullMap.getMapData().entrySet())
			{
				if (internalMyHalfMap.getMapData().containsKey(eachHalfMapNode.getKey()) == false)
				{
					
					result.put(eachHalfMapNode.getKey(), eachHalfMapNode.getValue());
				}
			}
			
		}
		return result;
		
	}
	
	// this is for when you don't know if the map is the enemy's or mine
	public HashMap<Coordinate, InternalNodeData> extractUpdate(InternalFullMap internalFullMap, HashMap<Coordinate, InternalNodeData> oldHalfMap)
	{
		HashMap<Coordinate, InternalNodeData> result = new HashMap<Coordinate, InternalNodeData>();
		
		for (Entry<Coordinate, InternalNodeData> eachOldHalfMapNode : oldHalfMap.entrySet())
		{
			InternalNodeData updatedInternalNodeData = internalFullMap.getMapData().get(eachOldHalfMapNode.getKey());
			
			result.put(eachOldHalfMapNode.getKey(), updatedInternalNodeData);
		}
		
		return result;
	}
	
}
