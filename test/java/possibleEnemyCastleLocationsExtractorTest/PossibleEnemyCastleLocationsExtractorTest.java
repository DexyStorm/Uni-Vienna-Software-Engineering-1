package possibleEnemyCastleLocationsExtractorTest;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import algorithms.goalFinders.PossibleEnemyCastleLocationsExtractor;
import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.fullMap.InternalFullMap;
import data.map.halfMap.InternalHalfMap;
import enums.Terrain;
import helpers.MapCreator;

public class PossibleEnemyCastleLocationsExtractorTest
{
	MapCreator mapCreator = new MapCreator();
	PossibleEnemyCastleLocationsExtractor possibleEnemyCastleLocationsExtractor = new PossibleEnemyCastleLocationsExtractor();
	
	@Test
	void possibleEnemyCastleLocationsExtractor_fieldsAroundEnemyPlayerShouldBeReturned()
	{
		String[][] internalFullMapNodeArray =
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
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } }; // 9
		
		String[][] internalEnemyHalfMapNodeArray =
		{ // ....0....1....2....3....4....5....6....7....8....9
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 0
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 1
				{ "g", "g", "g", "g", "g", "G", "g", "g", "g", "g" }, // 2
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" }, // 3
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } }; // 4
		
		String[][] controlMapNodeArray =
		{ // ....0....1....2....3....4....5....6....7....8....9
				{ "g", "g", "g", "m", "m", "m", "m", "m", "g", "g" }, // 0
				{ "g", "g", "m", "m", "m", "m", "m", "m", "m", "g" }, // 1
				{ "g", "m", "m", "m", "m", "m", "m", "m", "m", "m" }, // 2
				{ "g", "g", "m", "m", "m", "m", "m", "m", "m", "g" }, // 3
				{ "g", "g", "g", "m", "m", "m", "m", "m", "g", "g" } }; // 4
		
		InternalFullMap internalFullmap = mapCreator.setUpFullMap(internalFullMapNodeArray);
		InternalHalfMap internalEnemyHalfMap = mapCreator.setUpHalfMap(internalEnemyHalfMapNodeArray);
		InternalHalfMap controlMap = mapCreator.setUpHalfMap(controlMapNodeArray);
		
		internalFullmap.getEntities().getEnemyPlayer().setLocation(new Coordinate(5, 2));
		
		Set<Coordinate> possibleEnemyCastleLocations = possibleEnemyCastleLocationsExtractor.extractPossibleEnemyCastleLocations(internalFullmap, internalEnemyHalfMap);
		
		Set<Coordinate> correctResult = new HashSet<Coordinate>();
		for (Entry<Coordinate, InternalNodeData> eachControlMapNode : controlMap.getMapData().entrySet())
		{
			if (eachControlMapNode.getValue().getTerrain() == Terrain.Mountain)
			{
				correctResult.add(eachControlMapNode.getKey());
			}
		}
		
		Assertions.assertEquals(possibleEnemyCastleLocations, correctResult);
		
	}
	
}
