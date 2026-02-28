package internalFullMapTest;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import data.map.Coordinate;
import data.map.fullMap.InternalFullMap;
import enums.InternalExploredState;
import enums.Terrain;
import helpers.MapCreator;

public class InternalFullMapTest
{
	MapCreator mapCreator = new MapCreator();
	
	@Test
	void InternalFullMapMarkLocationAsVisitedOnGrassTest_NodesShouldBeMarkedAsVisited()
	{
		
		InternalFullMap internalFullMap = mapCreator.setUpFullMapXxY(19, 4);
		Coordinate coordinate = new Coordinate(3, 3);
		internalFullMap.markLocationAsVisited(coordinate);
		
		Assertions.assertEquals(internalFullMap.getMapData().get(coordinate).getInternalExploredState(), InternalExploredState.Explored);
	}
	
	@Test
	void InternalFullMapMarkLocationAroundMountainVisitedTest_NodesShouldBeMarkedAsVisited()
	{
		
		InternalFullMap internalFullMap = mapCreator.setUpFullMapXxY(19, 4);
		Coordinate coordinate = new Coordinate(3, 3);
		internalFullMap.getMapData().get(coordinate).setTerrain(Terrain.Mountain);
		
		internalFullMap.markLocationAsVisited(coordinate);
		
		Set<Coordinate> allNeighbors = coordinate.getAllNeighbors(internalFullMap);
		
		boolean allNeighborsVisited = true;
		for (Coordinate neighbor : allNeighbors)
		{
			if (internalFullMap.getMapData().get(neighbor).getInternalExploredState() == InternalExploredState.NotExplored)
			{
				allNeighborsVisited = false;
			}
		}
		
		Assertions.assertTrue(allNeighborsVisited);
	}
	
}
