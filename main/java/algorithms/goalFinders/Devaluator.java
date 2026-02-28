package algorithms.goalFinders;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import data.map.BoundsChecker;
import data.map.Coordinate;
import data.map.PlayerMap;

/**
 * //in order to incentivize my player to move to where i want him to go, i'll
 * devalue all the nodes where i *don't* want him to go. this class is for
 * exactly that
 * 
 */
public class Devaluator
{
	
	public void devalueNonPossibleEnemyCastleLocationNodes(Map<Coordinate, Double> evaluatedNodes, PlayerMap playerMap, Set<Coordinate> possibleEnemyCastleLocations)
	{
		Map<Coordinate, Double> devaluatedNodes = new HashMap<Coordinate, Double>();
		
		for (Entry<Coordinate, Double> eachEvaluatedNode : evaluatedNodes.entrySet())
		{
			if (possibleEnemyCastleLocations.contains(eachEvaluatedNode.getKey()) == false)
			{
				devaluatedNodes.put(eachEvaluatedNode.getKey(), 0.0); // 0.0 is the new, devalued, value of the node
			}
		}
		
		evaluatedNodes.putAll(devaluatedNodes);
		
	}
	
	public void devalueMyHalfMapNodes(BoundsChecker boundsChecker, Map<Coordinate, Double> evaluatedNodes, PlayerMap playerMap)
	{
		Map<Coordinate, Double> devaluatedNodes = new HashMap<Coordinate, Double>();
		
		for (Entry<Coordinate, Double> eachEvaluatedNode : evaluatedNodes.entrySet())
		{
			if (boundsChecker.checkMyHalfMapBounds(eachEvaluatedNode.getKey()))
			{
				devaluatedNodes.put(eachEvaluatedNode.getKey(), 0.0); // 0.0 is the new, devalued, value of the node
			}
		}
		
		evaluatedNodes.putAll(devaluatedNodes);
		
	}
	
}
