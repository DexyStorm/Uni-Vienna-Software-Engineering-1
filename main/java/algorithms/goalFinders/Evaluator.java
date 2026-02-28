package algorithms.goalFinders;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import algorithms.searchAlgorithms.Dijkstra;
import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.PlayerMap;
import enums.InternalExploredState;
import enums.Terrain;
import exceptions.InvalidEvaluationException;

public class Evaluator
{
	private final static Logger logger = LoggerFactory.getLogger(Evaluator.class);
	
	private Double calculateMountainValue(Entry<Coordinate, InternalNodeData> node, double grassNotExploredValue, PlayerMap playerMap)
	{
		// this number can be adjusted to make mountains more or less attractive
		double mountainValue = 0;
		
		Set<Coordinate> neighBors = node.getKey().getAllNeighbors(playerMap);
		for (Coordinate eachNeighbor : neighBors)
		{
			if (playerMap.getNodeData(eachNeighbor).getTerrain() == Terrain.Grass)
			{
				if (playerMap.getNodeData(eachNeighbor).getInternalExploredState() == InternalExploredState.NotExplored)
				{
					mountainValue = mountainValue + grassNotExploredValue;
				}
			}
		}
		
		return mountainValue;
	}
	
	public Map<Coordinate, Double> evaluateNodes(Dijkstra dijkstra, PlayerMap playerMap)
	{
		Map<Coordinate, Double> evaluatedNodes = new HashMap<Coordinate, Double>();
		
		// these numbers can be adjusted to make the nodes more or less attractive
		// an explored Grassfield should always have a value of 0 cuz it can't contain
		// the treasure (anymore)
		double grassExploredValue = 0.0;
		double grassNotExploredValue = 1.0;
		// should always be an extremely high number to show to incentivize the player
		// to move towards it
		double treasureValue = Double.MAX_VALUE;
		double enemyCastleValue = treasureValue;
		
		for (Entry<Coordinate, InternalNodeData> eachNode : playerMap.getMapData().entrySet())
		{
			if (eachNode.getValue().getTerrain() == Terrain.Grass)
			{
				if (eachNode.getKey().equals(playerMap.getEntities().getTreasure()))
				{
					evaluatedNodes.put(eachNode.getKey(), treasureValue);
				}
				
				else if (eachNode.getKey().equals(playerMap.getEntities().getEnemyCastle()))
				{
					evaluatedNodes.put(eachNode.getKey(), enemyCastleValue);
				}
				else if (eachNode.getValue().getInternalExploredState() == InternalExploredState.NotExplored)
				{
					
					evaluatedNodes.put(eachNode.getKey(), grassNotExploredValue / dijkstra.getCostToReach().get(eachNode.getKey()));
				}
				else // eachNode.getValue().getInternalExploredState() ==
						// InternalExploredState.Explored
				{
					evaluatedNodes.put(eachNode.getKey(), grassExploredValue);
				}
				
			}
			
			else if (eachNode.getValue().getTerrain() == Terrain.Mountain)
			{
				
				evaluatedNodes.put(eachNode.getKey(), (calculateMountainValue(eachNode, grassNotExploredValue, playerMap)) / dijkstra.getCostToReach().get(eachNode.getKey()));
			}
		}
		
		return evaluatedNodes;
	}
	
	public Coordinate getMostValuableCoordinate(Map<Coordinate, Double> evaluatedNodes, PlayerMap playerMap)
	{
		double result = 0;
		
		for (Entry<Coordinate, Double> eachNode : evaluatedNodes.entrySet())
		{
			if (result < eachNode.getValue())
			{
				result = eachNode.getValue();
			}
		}
		
		if (result <= 0)
		{
			throw new InvalidEvaluationException(String.valueOf(result));
		}
		
		for (Entry<Coordinate, Double> eachNode : evaluatedNodes.entrySet())
		{
			if (eachNode.getValue() == result)
			{
				return eachNode.getKey();
			}
		}
		
		throw new InvalidEvaluationException(String.valueOf(result));
		
	}
	
}
