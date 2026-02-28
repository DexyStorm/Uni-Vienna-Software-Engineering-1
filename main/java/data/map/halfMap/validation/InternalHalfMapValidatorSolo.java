package data.map.halfMap.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import algorithms.searchAlgorithms.FloodFill;
import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.halfMap.InternalHalfMap;
import enums.Terrain;

/**
 * this class is for validating the internal halfmap once it gets created. it is
 * used for *both* if i'm the first one that sends the halfmap and/or if i'm the
 * second one that sends the halfmap
 */
public class InternalHalfMapValidatorSolo
{
	
	private final static Logger logger = LoggerFactory.getLogger(InternalHalfMapValidatorSolo.class);
	
	private boolean checkIfCorrectAmountIsPresent(InternalHalfMap internalHalfMap)
	{
		int mountainCounter = 0;
		int minMountain = 5;
		int grassCounter = 0;
		int minGrass = 25;
		int waterCounter = 0;
		int minWater = 7;
		int castleCounter = 0;
		int minCastle = 1;
		
		for (Entry<Coordinate, InternalNodeData> eachNode : internalHalfMap.getMapData().entrySet())
		{
			Terrain terrain = eachNode.getValue().getTerrain();
			
			if (terrain == Terrain.Grass)
			{
				grassCounter = grassCounter + 1;
				
			}
			else if (terrain == Terrain.Water)
			{
				waterCounter = waterCounter + 1;
				
			}
			else if (terrain == Terrain.Mountain)
			{
				mountainCounter = mountainCounter + 1;
			}
		}
		
		if (!internalHalfMap.getEntities().getMyCastle().equals(new Coordinate(-1, -1)))
		{
			castleCounter = castleCounter + 1;
		}
		
		if (grassCounter >= minGrass && waterCounter >= minWater && mountainCounter >= minMountain && castleCounter >= minCastle)
		{
			return true;
		}
		return false;
	}
	
	// 40% of an edge must be walkable
	// 20% of an edge must be non-walkable
	// the halfmap is always 9x4 (starting to count at (0,0)))
	// so
	// walkable for north and south: 10/100*40=4
	// non-walkable for north and south: 10/100*20=2
	// walkable for east and west: 5/100*40=2
	// non-walkable for east and west: 5/100*20=1
	private boolean checkIfEdgeAccessabilityIsCorrect(InternalHalfMap internalHalfMap)
	{
		int edgeWaterNorth = 0;
		int maxEdgeWaterNorth = 4;
		int minEdgeWaterNorth = 2;
		int edgeWaterSouth = 0;
		int maxEdgeWaterSouth = 4;
		int minEdgeWaterSouth = 2;
		int edgeWaterEast = 0;
		int maxEdgeWaterEast = 2;
		int minEdgeWaterEast = 1;
		int edgeWaterWest = 0;
		int maxEdgeWaterWest = 2;
		int minEdgeWaterWest = 1;
		
		for (Entry<Coordinate, InternalNodeData> eachNode : internalHalfMap.getMapData().entrySet())
		{
			if (eachNode.getValue().getTerrain() == Terrain.Water)
			{
				if (eachNode.getKey().getX() == 0)
				{
					edgeWaterWest = edgeWaterWest + 1;
				}
				if (eachNode.getKey().getX() == 9)
				{
					edgeWaterEast = edgeWaterEast + 1;
				}
				if (eachNode.getKey().getY() == 0)
				{
					edgeWaterNorth = edgeWaterNorth + 1;
				}
				if (eachNode.getKey().getY() == 4)
				{
					edgeWaterSouth = edgeWaterSouth + 1;
				}
			}
		}
		
		if (edgeWaterNorth > maxEdgeWaterNorth)
		{
			return false;
		}
		if (edgeWaterSouth > maxEdgeWaterSouth)
		{
			return false;
		}
		if (edgeWaterWest > maxEdgeWaterWest)
		{
			return false;
		}
		if (edgeWaterEast > maxEdgeWaterEast)
		{
			return false;
		}
		
		if (edgeWaterNorth < minEdgeWaterNorth)
		{
			return false;
		}
		if (edgeWaterSouth < minEdgeWaterSouth)
		{
			return false;
		}
		if (edgeWaterWest < minEdgeWaterWest)
		{
			return false;
		}
		if (edgeWaterEast < minEdgeWaterEast)
		{
			return false;
		}
		
		return true;
		
	}
	
	private boolean checkIfIslandsExist(InternalHalfMap internalHalfMap)
	{
		FloodFill floodFill = new FloodFill();
		List<Coordinate> walkableFields = new ArrayList<Coordinate>();
		
		for (Entry<Coordinate, InternalNodeData> eachNode : internalHalfMap.getMapData().entrySet())
		{
			if (eachNode.getValue().getTerrain() == (Terrain.Grass))
			{
				walkableFields.add(eachNode.getKey());
			}
			else if (eachNode.getValue().getTerrain() == (Terrain.Mountain))
			{
				walkableFields.add(eachNode.getKey());
			}
		}
		
		Set<Coordinate> visitedNodes = floodFill.performAlgorithm(walkableFields.get(0), internalHalfMap);
		
		for (Coordinate walkableField : walkableFields)
		{
			if (visitedNodes.contains(walkableField) == false)
			{// if u want islands, switch the next and last return
				return true;
			}
		}
		
		return false;
	}
	
	public List<String> validateHalfMap(InternalHalfMap internalMyHalfMap)
	{
		List<String> errorMessages = new ArrayList<String>();
		if (checkIfCorrectAmountIsPresent(internalMyHalfMap) == false)
		{
			errorMessages.add("Not correct amount of Terrain was present");
		}
		
		if (checkIfEdgeAccessabilityIsCorrect(internalMyHalfMap) == false)
		{
			errorMessages.add("Not all edges have the correct accessability (>=40% walkable, >=20% non-walkable)");
		}
		
		if (checkIfIslandsExist(internalMyHalfMap) == true)
		{
			errorMessages.add("Island detected");
		}
		
		return errorMessages;
	}
	
}
