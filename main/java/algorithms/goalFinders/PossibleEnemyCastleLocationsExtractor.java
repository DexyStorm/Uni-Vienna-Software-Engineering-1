package algorithms.goalFinders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import algorithms.searchAlgorithms.Dijkstra;
import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.fullMap.InternalFullMap;
import data.map.halfMap.InternalHalfMap;
import enums.Direction;
import enums.Terrain;

/**
 * For the first 7 rounds, the enemy's position is random. On the 8th round, the
 * true enemy's position is revealed. when this happens, this class will try to
 * find out every coordinate within a 8-move radius that could contain the
 * enemy's castle
 */
public class PossibleEnemyCastleLocationsExtractor
{
	private final static Logger logger = LoggerFactory.getLogger(PossibleEnemyCastleLocationsExtractor.class);
	
	/*
	 * InternalFullMap is passed in here instead of just the enemy's halfmap cuz the
	 * enemy could already be on my side even after 8 moves
	 */
	public Set<Coordinate> extractPossibleEnemyCastleLocations(InternalFullMap internalFullmap, InternalHalfMap internalEnemyHalfMap)
	{
		Set<Coordinate> possibleEnemyCastleLocations = new HashSet<Coordinate>();
		
		Dijkstra dijkstra = new Dijkstra();
		dijkstra.performDijkstra(internalFullmap, internalFullmap.getEntities().getEnemyPlayer().getLocation());
		
		PathReconstructer pathReconstructer = new PathReconstructer();
		Map<Coordinate, List<Direction>> allReconstructedPaths = new HashMap<Coordinate, List<Direction>>();
		for (Entry<Coordinate, InternalNodeData> eachInternalFullMapNode : internalFullmap.getMapData().entrySet())
		{
			if (internalEnemyHalfMap.getMapData().containsKey(eachInternalFullMapNode.getKey()))
			{
				allReconstructedPaths.put(eachInternalFullMapNode.getKey(), pathReconstructer.reconstructPath(dijkstra.getParentMap(),
						internalFullmap.getEntities().getEnemyPlayer().getLocation(), eachInternalFullMapNode.getKey(), internalFullmap));
				
			}
		}
		
		for (Entry<Coordinate, List<Direction>> eachReConstructedPath : allReconstructedPaths.entrySet())
		{
			// after 8 rounds, the enemy player could only make up to a maximum of 8 moves,
			// meaning that his castle must be somewhere within 8 moves from where he is
			// currently standing
			if (eachReConstructedPath.getValue().size() <= 8)
			{
				if ((internalFullmap.getMapData().get(eachReConstructedPath.getKey()).getTerrain() == Terrain.Water) == false)
				{
					possibleEnemyCastleLocations.add(eachReConstructedPath.getKey());
				}
			}
		}
		
		logger.debug("Possible Enemy Castle Locations:\n " + possibleEnemyCastleLocations);
		return possibleEnemyCastleLocations;
	}
	
}
