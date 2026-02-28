package boundsCheckerTest;

import java.util.HashMap;
import java.util.Map.Entry;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import data.map.BoundsChecker;
import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.fullMap.InternalFullMap;
import enums.Direction;
import helpers.MapCreator;

public class BoundsCheckerTest
{
	MapCreator mapCreator = new MapCreator();
	
	@ParameterizedTest
	@CsvSource(
	{
			// fullMapX, fullMapY, myCastleX, myCastleY, myHalfLeft, myHalfRight, myHalfUp,
			// myHalfDown, enemyHalfLeft, enemyHalfRight, enemyHalfUp, enemyHalfDown,
			"19, 4, 1, 1, 0, 9, 0, 4, 10, 19, 0, 4", "19, 4, 11, 1, 10, 19, 0, 4, 0, 9, 0, 4", "9, 9, 1, 1, 0, 9, 0, 4, 0, 9, 5, 9", "9, 9 , 1, 6, 0, 9, 5, 9, 0, 9, 0, 4", })
	
	void boundsCheckerHalfMapBoundsTest_Parameterized_ShouldFitForParameters(int fullMapX, int fullMapY, int myCastleX, int myCastleY, int myHalfLeft, int myHalfRight, int myHalfUp,
			int myHalfDown, int enemyHalfLeft, int enemyHalfRight, int enemyHalfUp, int enemyHalfDown)
	{
		
		BoundsChecker boundsChecker = new BoundsChecker();
		InternalFullMap internalFullMap = mapCreator.setUpFullMapXxY(fullMapX, fullMapY);
		internalFullMap.getEntities().setMyCastle(new Coordinate(myCastleX, myCastleY));
		
		boundsChecker.updateBounds(internalFullMap);
		
		HashMap<Direction, Integer> myHalfMapBounds = boundsChecker.getMyHalfMapBounds();
		Assertions.assertEquals(myHalfMapBounds.get(Direction.Left), myHalfLeft);
		Assertions.assertEquals(myHalfMapBounds.get(Direction.Right), myHalfRight);
		Assertions.assertEquals(myHalfMapBounds.get(Direction.Up), myHalfUp);
		Assertions.assertEquals(myHalfMapBounds.get(Direction.Down), myHalfDown);
		
		HashMap<Direction, Integer> enemyHalfMapBounds = boundsChecker.getEnemyHalfMapBounds();
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Left), enemyHalfLeft);
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Right), enemyHalfRight);
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Up), enemyHalfUp);
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Down), enemyHalfDown);
	}
	
	@Test
	void boundsCheckerHalfMapBoundsTest_ShouldFitForOnlyEnemyHalfMapIsInFullMapTopLeft()
	{
		BoundsChecker boundsChecker = new BoundsChecker();
		InternalFullMap internalFullMap = mapCreator.setUpFullMapXxY(9, 4);
		
		boundsChecker.updateBounds(internalFullMap);
		
		HashMap<Direction, Integer> enemyHalfMapBounds = boundsChecker.getEnemyHalfMapBounds();
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Left), 0);
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Right), 9);
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Up), 0);
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Down), 4);
	}
	
	@Test
	void boundsCheckerHalfMapBoundsTest_ShouldFitForOnlyEnemyHalfMapIsInFullMapRight()
	{
		BoundsChecker boundsChecker = new BoundsChecker();
		InternalFullMap internalFullMap = mapCreator.setUpFullMapXxY(9, 4);
		
		HashMap<Coordinate, InternalNodeData> newMapData = new HashMap<Coordinate, InternalNodeData>();
		for (Entry<Coordinate, InternalNodeData> oldCoordinate : internalFullMap.getMapData().entrySet())
		{
			Coordinate newCoordinate = new Coordinate(oldCoordinate.getKey().getX() + 10, oldCoordinate.getKey().getY());
			newMapData.put(newCoordinate, oldCoordinate.getValue());
		}
		InternalFullMap newInternalFullMap = new InternalFullMap(internalFullMap.getEntities(), newMapData);
		
		boundsChecker.updateBounds(newInternalFullMap);
		
		HashMap<Direction, Integer> myHalfMapBounds = boundsChecker.getMyHalfMapBounds();
		
		Assertions.assertEquals(myHalfMapBounds.get(Direction.Left), 0);
		Assertions.assertEquals(myHalfMapBounds.get(Direction.Right), 9);
		Assertions.assertEquals(myHalfMapBounds.get(Direction.Up), 0);
		Assertions.assertEquals(myHalfMapBounds.get(Direction.Down), 4);
		
		HashMap<Direction, Integer> enemyHalfMapBounds = boundsChecker.getEnemyHalfMapBounds();
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Left), 10);
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Right), 19);
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Up), 0);
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Down), 4);
	}
	
	@Test
	void boundsCheckerHalfMapBoundsTest_ShouldFitForOnlyEnemyHalfMapIsInFullMapDown()
	{
		BoundsChecker boundsChecker = new BoundsChecker();
		InternalFullMap internalFullMap = mapCreator.setUpFullMapXxY(9, 4);
		
		HashMap<Coordinate, InternalNodeData> newMapData = new HashMap<Coordinate, InternalNodeData>();
		for (Entry<Coordinate, InternalNodeData> oldCoordinate : internalFullMap.getMapData().entrySet())
		{
			Coordinate newCoordinate = new Coordinate(oldCoordinate.getKey().getX(), oldCoordinate.getKey().getY() + 5);
			newMapData.put(newCoordinate, oldCoordinate.getValue());
		}
		InternalFullMap newInternalFullMap = new InternalFullMap(internalFullMap.getEntities(), newMapData);
		
		boundsChecker.updateBounds(newInternalFullMap);
		
		HashMap<Direction, Integer> myHalfMapBounds = boundsChecker.getMyHalfMapBounds();
		
		Assertions.assertEquals(myHalfMapBounds.get(Direction.Left), 0);
		Assertions.assertEquals(myHalfMapBounds.get(Direction.Right), 9);
		Assertions.assertEquals(myHalfMapBounds.get(Direction.Up), 0);
		Assertions.assertEquals(myHalfMapBounds.get(Direction.Down), 4);
		
		HashMap<Direction, Integer> enemyHalfMapBounds = boundsChecker.getEnemyHalfMapBounds();
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Left), 0);
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Right), 9);
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Up), 5);
		Assertions.assertEquals(enemyHalfMapBounds.get(Direction.Down), 9);
	}
	
	@Test
	void boundsCheckerDirectionTest_EnemyHalfMapShouldBeRight()
	{
		BoundsChecker boundsChecker = new BoundsChecker();
		
		InternalFullMap internalFullMap = mapCreator.setUpFullMapXxY(19, 4);
		// my Castle is now in the "right half" of the map
		internalFullMap.getEntities().setMyCastle(new Coordinate(1, 1));
		
		boundsChecker.updateBounds(internalFullMap);
		
		Assertions.assertEquals(boundsChecker.getEnemyHalfMapDirection(), Direction.Right);
	}
	
	@Test
	void boundsCheckerDirectionTest_EnemyHalfMapShouldBeLeft()
	{
		BoundsChecker boundsChecker = new BoundsChecker();
		
		InternalFullMap internalFullMap = mapCreator.setUpFullMapXxY(19, 4);
		// my Castle is now in the "right half" of the map
		internalFullMap.getEntities().setMyCastle(new Coordinate(13, 1));
		
		boundsChecker.updateBounds(internalFullMap);
		
		Assertions.assertEquals(boundsChecker.getEnemyHalfMapDirection(), Direction.Left);
	}
	
	@Test
	void boundsCheckerDirectionTest_EnemyHalfMapShouldBeDown()
	{
		BoundsChecker boundsChecker = new BoundsChecker();
		
		InternalFullMap internalFullMap = mapCreator.setUpFullMapXxY(9, 9);
		// my Castle is now in the "right half" of the map
		internalFullMap.getEntities().setMyCastle(new Coordinate(1, 1));
		
		boundsChecker.updateBounds(internalFullMap);
		
		Assertions.assertEquals(boundsChecker.getEnemyHalfMapDirection(), Direction.Down);
	}
	
	@Test
	void boundsCheckerDirectionTest_EnemyHalfMapShouldBeUp()
	{
		BoundsChecker boundsChecker = new BoundsChecker();
		
		InternalFullMap internalFullMap = mapCreator.setUpFullMapXxY(9, 9);
		// my Castle is now in the "right half" of the map
		internalFullMap.getEntities().setMyCastle(new Coordinate(1, 6));
		
		boundsChecker.updateBounds(internalFullMap);
		
		Assertions.assertEquals(boundsChecker.getEnemyHalfMapDirection(), Direction.Up);
	}
	
}
