package algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import algorithms.goalFinders.Devaluator;
import algorithms.goalFinders.Evaluator;
import algorithms.goalFinders.PathReconstructer;
import algorithms.searchAlgorithms.Dijkstra;
import data.map.BoundsChecker;
import data.map.Coordinate;
import data.map.PlayerMap;
import enums.Direction;
import exceptions.NoPathFoundException;

//generates the movement
public class MoveGenerator
{
	private final static Logger logger = LoggerFactory.getLogger(MoveGenerator.class);
	
	public List<Direction> getNextMoves(BoundsChecker boundsChecker, PlayerMap playerMap, Set<Coordinate> possibleEnemyCastleLocations, int roundCounter)
	{
		List<Direction> result = new ArrayList<Direction>();
		Dijkstra dijkstra = new Dijkstra();
		Evaluator evaluator = new Evaluator();
		Devaluator devaluator = new Devaluator();
		
		dijkstra.performDijkstra(playerMap, playerMap.getEntities().getMyPlayer().getLocation());
		Map<Coordinate, Double> evaluatedNodes = evaluator.evaluateNodes(dijkstra, playerMap);
		
		// for more info on what the 8 means, read the description of the class
		// PossibleEnemyCastleLocationsExtractor
		if (roundCounter >= 8)
		{
			// if the playerMap's mapsize is 100, that means that the fullmap has been sent
			// in, which means that my player must make it's way to the enemy's half
			if (playerMap.getMapData().size() == 100)
			{
				devaluator.devalueNonPossibleEnemyCastleLocationNodes(evaluatedNodes, playerMap, possibleEnemyCastleLocations);
				
			}
			// enemy map has been sent in
			else if (playerMap.getMapData().containsKey(playerMap.getEntities().getMyCastle()) == false)
			{
				devaluator.devalueNonPossibleEnemyCastleLocationNodes(evaluatedNodes, playerMap, possibleEnemyCastleLocations);
			}
		}
		else
		{
			if (playerMap.getMapData().size() == 100)
			{
				devaluator.devalueMyHalfMapNodes(boundsChecker, evaluatedNodes, playerMap);
				
			}
		}
		
		Coordinate mostValuableNode = evaluator.getMostValuableCoordinate(evaluatedNodes, playerMap);
		logger.info("Next goal: " + mostValuableNode);
		// now i need to look at djikstra.getParent() and re-trace steps back to get the
		// path (i can probably just use reconstructPath() or smthing like that
		
		PathReconstructer pathReconstructer = new PathReconstructer();
		result = pathReconstructer.reconstructPath(dijkstra.getParentMap(), playerMap.getEntities().getMyPlayer().getLocation(), mostValuableNode, playerMap);
		
		if (result.isEmpty())
		{
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("Player Position: ");
			stringBuilder.append(playerMap.getEntities().getMyPlayer().getLocation());
			stringBuilder.append("\n playerMap:\n");
			stringBuilder.append(playerMap);
			throw new NoPathFoundException(stringBuilder.toString());
		}
		
		return result;
		
	}
	
}
