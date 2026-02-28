package algorithms.searchAlgorithms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.PlayerMap;
import enums.Terrain;
import exceptions.TerrainCostAssociactionException;

public class Dijkstra
{
	private HashMap<Coordinate, Coordinate> parentMap;
	private HashMap<Coordinate, Integer> costToReach;
	
	private int associateTerrainWithCost(Coordinate coordinate, PlayerMap playerMap)
	{
		switch (playerMap.getNodeData(coordinate).getTerrain())
		{
			case Terrain.Grass:
				return 1;
			case Terrain.Mountain:
				return 2;
			case Terrain.Water:
				return Integer.MAX_VALUE;
		}
		
		throw new TerrainCostAssociactionException(coordinate + " " + playerMap.getMapData().get(coordinate).toString());
	}
	
	private int getCost(Coordinate currentCoordinate, Coordinate neighborCoordinate, PlayerMap playerMap)
	{
		int costCurrentCoordinate = associateTerrainWithCost(currentCoordinate, playerMap);
		int costneighborCoordinate = associateTerrainWithCost(neighborCoordinate, playerMap);
		
		return costCurrentCoordinate + costneighborCoordinate;
		
	}
	
	/**
	 * returns a HashMap with every node and the "cost" of how expensive it is to
	 * reach that node
	 */
	public Dijkstra performDijkstra(PlayerMap playerMap, Coordinate startCoordinate)
	{
		HashMap<Coordinate, Integer> costToReach = new HashMap<Coordinate, Integer>();
		HashMap<Coordinate, Coordinate> parentMap = new HashMap<Coordinate, Coordinate>();
		PriorityQueue<Coordinate> priorityQueue = new PriorityQueue<Coordinate>(Comparator.comparing(coordinate -> costToReach.get(coordinate)));
		// List<Direction> moves = new ArrayList<Direction>();
		
		// new Coordinate(-1, -1) is just a "placeholder" to signalize that the starting
		// position has no parent
		parentMap.put(playerMap.getEntities().getMyPlayer().getLocation(), new Coordinate(-1, -1));
		
		for (Entry<Coordinate, InternalNodeData> eachNode : playerMap.getMapData().entrySet())
		{
			costToReach.put(eachNode.getKey(), Integer.MAX_VALUE);
		}
		
		costToReach.put(startCoordinate, 0);
		priorityQueue.add(startCoordinate);
		
		while (priorityQueue.isEmpty() == false)
		{
			Coordinate currentCoordinate = priorityQueue.poll();
			
			for (Coordinate eachNeighbor : currentCoordinate.getCardinalNeighbors(playerMap))
			{
				if (playerMap.checkIfLocationIsInsideMap(eachNeighbor))
				{
					// useless cuz i already filter water but whatevs
					if (playerMap.getNodeData(eachNeighbor).getTerrain() != Terrain.Water)
					{
						int cost = getCost(currentCoordinate, eachNeighbor, playerMap);
						int newCost = costToReach.get(currentCoordinate) + cost;
						
						if (newCost < costToReach.get(eachNeighbor))
						{
							costToReach.put(eachNeighbor, newCost);
							parentMap.put(eachNeighbor, currentCoordinate);
							priorityQueue.add(eachNeighbor);
						}
					}
				}
			}
		}
		
		this.costToReach = costToReach;
		this.parentMap = parentMap;
		
		return this;
	}
	
	public HashMap<Coordinate, Coordinate> getParentMap()
	{
		return parentMap;
	}
	
	public HashMap<Coordinate, Integer> getCostToReach()
	{
		return costToReach;
	}
	
}
