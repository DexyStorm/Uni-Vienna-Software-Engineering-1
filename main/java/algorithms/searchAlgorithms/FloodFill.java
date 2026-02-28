package algorithms.searchAlgorithms;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import data.map.Coordinate;
import data.map.PlayerMap;

public class FloodFill
{
	
	public Set<Coordinate> performAlgorithm(Coordinate startCoordinate, PlayerMap playerMap)
	{
		Queue<Coordinate> queue = new ArrayDeque<>();
		queue.add(startCoordinate);
		
		Set<Coordinate> visited = new HashSet<Coordinate>();
		visited.add(startCoordinate);
		
		while (queue.isEmpty() == false)
		{
			Coordinate currentTuple = queue.remove();
			
			for (Coordinate neighbor : currentTuple.getCardinalNeighbors(playerMap))
			{
				if (visited.contains(neighbor) == false)
				{
					queue.add(neighbor);
					visited.add(neighbor);
				}
				
			}
		}
		
		return visited;
	}
}
