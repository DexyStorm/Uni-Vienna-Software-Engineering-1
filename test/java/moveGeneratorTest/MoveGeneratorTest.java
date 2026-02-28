package moveGeneratorTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import algorithms.MoveGenerator;
import algorithms.goalFinders.PossibleEnemyCastleLocationsExtractor;
import data.map.BoundsChecker;
import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.fullMap.InternalFullMap;
import data.map.halfMap.InternalHalfMap;
import enums.Direction;
import enums.InternalExploredState;
import enums.Terrain;
import helpers.MapCreator;

public class MoveGeneratorTest
{
	MoveGenerator moveGenerator = new MoveGenerator();
	MapCreator mapCreator = new MapCreator();
	
	@Test
	void MoveGeneratorCheckIfCorrectMovesAreReturned_9MovesRight()
	{
		Coordinate startingLocation = new Coordinate(0, 2);
		ArrayList<Direction> correctResult = new ArrayList<Direction>(
				List.of(Direction.Right, Direction.Right, Direction.Right, Direction.Right, Direction.Right, Direction.Right, Direction.Right, Direction.Right, Direction.Right));
		String[][] nodeArray =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 0
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 1
				{ "g", "g", "g", "g", "m", "g", "g", "g", "g", "g" }, // 2
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 3
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } }; // 4
		
		InternalHalfMap internalHalfMap = mapCreator.setUpHalfMap(nodeArray);
		internalHalfMap.getMapData().put(startingLocation, new InternalNodeData(Terrain.Grass, InternalExploredState.Explored));
		internalHalfMap.getEntities().getMyPlayer().setLocation(startingLocation);
		
		BoundsChecker boundsChecker = new BoundsChecker();
		Set<Coordinate> possibleEnemyCastleLocations = new HashSet<Coordinate>();
		List<Direction> moves = moveGenerator.getNextMoves(boundsChecker, internalHalfMap, possibleEnemyCastleLocations, 0);
		
		Assertions.assertEquals(moves, correctResult);
	}
	
	@Test
	void MoveGeneratorCheckIfCorrectMovesAreReturned_10MovesDown()
	{
		Coordinate startingLocation = new Coordinate(0, 0);
		ArrayList<Direction> correctResult = new ArrayList<Direction>(List.of(Direction.Down, Direction.Down, Direction.Down, Direction.Down, Direction.Down, Direction.Down,
				Direction.Down, Direction.Down, Direction.Down, Direction.Down));
		String[][] nodeArray =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", }, // 0
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 1
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 2
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 3
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 4
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 5
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 6
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 7
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 8
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } } // 9
		;
		
		InternalFullMap internalFullMap = mapCreator.setUpFullMap(nodeArray);
		internalFullMap.getMapData().put(startingLocation, new InternalNodeData(Terrain.Grass, InternalExploredState.Explored));
		internalFullMap.getEntities().getMyPlayer().setLocation(startingLocation);
		internalFullMap.getEntities().getMyPlayer().setTreasureCollected(true);
		internalFullMap.getEntities().setMyCastle(startingLocation);
		
		BoundsChecker boundsChecker = new BoundsChecker();
		boundsChecker.updateBounds(internalFullMap);
		
		Set<Coordinate> possibleEnemyCastleLocations = new HashSet<Coordinate>();
		List<Direction> moves = moveGenerator.getNextMoves(boundsChecker, internalFullMap, possibleEnemyCastleLocations, 0);
		
		System.out.println(moves);
		
		Assertions.assertEquals(moves, correctResult);
	}
	
	@Test
	void MoveGeneratorCheckIfCorrectMovesAreReturned_10MovesRight()
	{
		Coordinate startingLocation = new Coordinate(0, 0);
		Coordinate enemyPlayerLocation = new Coordinate(19, 0);
		
		ArrayList<Direction> correctResult = new ArrayList<Direction>();
		for (int i = 0; i < 30; ++i)
		{
			correctResult.add(Direction.Right);
		}
		
		String[][] nodeArray =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 0
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 1
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 2
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 3
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } } // 4
		
		;
		
		String[][] nodeArrayInternalEnemyHalfMap =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 0
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 1
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 2
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 3
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } }; // 4
		
		InternalHalfMap internalEnemyHalfMap = mapCreator.setUpHalfMap(nodeArrayInternalEnemyHalfMap);
		mapCreator.moveHalfMap(Direction.Right, internalEnemyHalfMap);
		InternalFullMap internalFullMap = mapCreator.setUpFullMap(nodeArray);
		internalFullMap.getMapData().put(startingLocation, new InternalNodeData(Terrain.Grass, InternalExploredState.Explored));
		internalFullMap.getEntities().getMyPlayer().setLocation(startingLocation);
		internalFullMap.getEntities().getMyPlayer().setTreasureCollected(true);
		internalFullMap.getEntities().getEnemyPlayer().setLocation(enemyPlayerLocation);
		internalFullMap.getEntities().setMyCastle(startingLocation);
		
		BoundsChecker boundsChecker = new BoundsChecker();
		boundsChecker.updateBounds(internalFullMap);
		
		PossibleEnemyCastleLocationsExtractor possibleEnemyCastleLocationsExtractor = new PossibleEnemyCastleLocationsExtractor();
		Set<Coordinate> possibleEnemyCastleLocations = possibleEnemyCastleLocationsExtractor.extractPossibleEnemyCastleLocations(internalFullMap, internalEnemyHalfMap);
		
		List<Direction> moves = moveGenerator.getNextMoves(boundsChecker, internalFullMap, possibleEnemyCastleLocations, 9);
		
		Assertions.assertEquals(moves, correctResult);
	}
	
	@Test
	void MoveGeneratorCheckIfCorrectMovesAreReturned_MyTreasureVisible()
	{
		Coordinate startingLocation = new Coordinate(9, 0);
		ArrayList<Direction> correctResult = new ArrayList<Direction>();
		
		for (int i = 0; i < 18; ++i)
		{
			correctResult.add(Direction.Left);
		}
		
		String[][] nodeArray =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 0
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 1
				{ "g", "g", "g", "g", "m", "g", "g", "g", "g", "g" }, // 2
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 3
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } }; // 4
		
		InternalHalfMap internalHalfMap = mapCreator.setUpHalfMap(nodeArray);
		internalHalfMap.getMapData().put(startingLocation, new InternalNodeData(Terrain.Grass, InternalExploredState.Explored));
		internalHalfMap.getEntities().getMyPlayer().setLocation(startingLocation);
		internalHalfMap.getEntities().setTreasure(new Coordinate(0, 0));
		
		BoundsChecker boundsChecker = new BoundsChecker();
		
		Set<Coordinate> possibleEnemyCastleLocations = new HashSet<Coordinate>();
		List<Direction> moves = moveGenerator.getNextMoves(boundsChecker, internalHalfMap, possibleEnemyCastleLocations, 0);
		
		Assertions.assertEquals(moves, correctResult);
	}
	
}
