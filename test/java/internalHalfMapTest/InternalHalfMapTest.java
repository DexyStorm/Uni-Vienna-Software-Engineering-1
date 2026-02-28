package internalHalfMapTest;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data.entities.Entities;
import data.entities.Player;
import data.map.BoundsChecker;
import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.halfMap.InternalHalfMap;
import enums.Direction;
import enums.Terrain;

class InternalHalfMapTest
{
	
	private InternalHalfMap internalHalfMap;
	
	@BeforeEach
	void setUp()
	{
		
		Coordinate defaultCoordinate = new Coordinate(-1, -1);
		Player myPlayer = new Player(defaultCoordinate, false);
		Player enemyPlayer = new Player(defaultCoordinate, false);
		
		Entities entities = new Entities(defaultCoordinate, defaultCoordinate, myPlayer, enemyPlayer, defaultCoordinate);
		HashMap<Coordinate, InternalNodeData> halfMapData = new HashMap<Coordinate, InternalNodeData>();
		
		internalHalfMap = new InternalHalfMap(entities, halfMapData);
		internalHalfMap.generateInternalHalfMap();
		
	}
	
	private Map<Direction, Integer> HalfMapUpdateCoordinatesSetUp(BoundsChecker boundsCheckerMock)
	{
		Map<Direction, Integer> coordinates = new HashMap<Direction, Integer>();
		internalHalfMap.updateCoordinates(boundsCheckerMock);
		
		int biggestX = 0;
		int biggestY = 0;
		int smallestX = 100;
		int smallestY = 100;
		for (Entry<Coordinate, InternalNodeData> eachNode : internalHalfMap.getMapData().entrySet())
		{
			if (eachNode.getKey().getX() < smallestX)
			{
				smallestX = eachNode.getKey().getX();
			}
			if (eachNode.getKey().getX() > biggestX)
			{
				biggestX = eachNode.getKey().getX();
			}
			
			if (eachNode.getKey().getY() < smallestY)
			{
				smallestY = eachNode.getKey().getY();
			}
			if (eachNode.getKey().getY() > biggestY)
			{
				biggestY = eachNode.getKey().getY();
			}
		}
		coordinates.put(Direction.Left, smallestX);
		coordinates.put(Direction.Right, biggestX);
		coordinates.put(Direction.Up, smallestY);
		coordinates.put(Direction.Down, biggestY);
		
		return coordinates;
	}
	
	@Test
	void HalfMapUpdateCoordinatesTest_HalfMapShouldBeMovedRight()
	{
		
		BoundsChecker boundsCheckerMock = mock(BoundsChecker.class, RETURNS_DEEP_STUBS);
		when(boundsCheckerMock.getMyHalfMapBounds().get(Direction.Right)).thenReturn(19);
		
		Map<Direction, Integer> coordinates = HalfMapUpdateCoordinatesSetUp(boundsCheckerMock);
		
		verify(boundsCheckerMock.getMyHalfMapBounds()).get(Direction.Right);
		
		Assertions.assertEquals(coordinates.get(Direction.Left), 10);
		Assertions.assertEquals(coordinates.get(Direction.Right), 19);
		Assertions.assertEquals(coordinates.get(Direction.Up), 0);
		Assertions.assertEquals(coordinates.get(Direction.Down), 4);
		
	}
	
	@Test
	void HalfMapUpdateCoordinatesTest_HalfMapShouldBeMovedDown()
	{
		
		BoundsChecker boundsCheckerMock = mock(BoundsChecker.class, RETURNS_DEEP_STUBS);
		when(boundsCheckerMock.getMyHalfMapBounds().get(Direction.Down)).thenReturn(9);
		
		Map<Direction, Integer> coordinates = HalfMapUpdateCoordinatesSetUp(boundsCheckerMock);
		
		verify(boundsCheckerMock.getMyHalfMapBounds()).get(Direction.Down);
		
		Assertions.assertEquals(coordinates.get(Direction.Left), 0);
		Assertions.assertEquals(coordinates.get(Direction.Right), 9);
		Assertions.assertEquals(coordinates.get(Direction.Up), 5);
		Assertions.assertEquals(coordinates.get(Direction.Down), 9);
		
	}
	
	@Test
	void HalfMapUpdateCoordinatesTest_HalfMapShouldBeMovedNowhere()
	{
		
		BoundsChecker boundsCheckerMock = mock(BoundsChecker.class, RETURNS_DEEP_STUBS);
		when(boundsCheckerMock.getMyHalfMapBounds().get(Direction.Down)).thenReturn(4);
		
		Map<Direction, Integer> coordinates = HalfMapUpdateCoordinatesSetUp(boundsCheckerMock);
		
		verify(boundsCheckerMock.getMyHalfMapBounds()).get(Direction.Down);
		
		Assertions.assertEquals(coordinates.get(Direction.Left), 0);
		Assertions.assertEquals(coordinates.get(Direction.Right), 9);
		Assertions.assertEquals(coordinates.get(Direction.Up), 0);
		Assertions.assertEquals(coordinates.get(Direction.Down), 4);
		
	}
	
	@Test
	void HalfMapGenerationTest_CastleShouldBeOnMap()
	{
		
		Coordinate defaultCoordinate = new Coordinate(-1, -1);
		
		Assertions.assertNotEquals(defaultCoordinate, internalHalfMap.getEntities().getMyCastle());
		
	}
	
	@Test
	void HalfMapGenerationTest_CastleShouldBeOnGrass()
	{
		
		Assertions.assertEquals(Terrain.Grass, internalHalfMap.getMapData().get(internalHalfMap.getEntities().getMyCastle()).getTerrain());
		
	}
	
}
