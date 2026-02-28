package algorithms.goalFinders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import data.map.Coordinate;
import data.map.PlayerMap;
import enums.Direction;
import enums.Terrain;
import exceptions.InvalidCoordinateException;

public class PathReconstructer
{
	public List<Direction> reconstructPath(Map<Coordinate, Coordinate> predecessors, Coordinate startCoordinate, Coordinate endCoordinate, PlayerMap playerMap)
	{
		if (playerMap.getMapData().containsKey(startCoordinate) == false)
		{
			throw new InvalidCoordinateException(startCoordinate.toString());
		}
		
		List<Coordinate> reversedPath = new ArrayList<Coordinate>();
		
		Coordinate currentCoordinate = endCoordinate;
		
		while (currentCoordinate != predecessors.get(startCoordinate))
		{
			reversedPath.add(currentCoordinate);
			currentCoordinate = predecessors.get(currentCoordinate);
		}
		
		Collections.reverse(reversedPath);
		
		List<Direction> directionsList = convertPathToDirections(reversedPath, playerMap);
		
		return directionsList;
	}
	
	private List<Direction> convertPathToDirections(List<Coordinate> path, PlayerMap playerMap)
	{
		List<Direction> directions = new ArrayList<>();
		
		// here i try to calculate the difference between 2 coordinates and assign a
		// direction to the difference between those 2 coordinates. for example:
		// (5,2) - (5,1) = (0,1) -> Down
		// Maybe read the BoundsChecker class for more info
		for (int i = 0; i < path.size() - 1; ++i)
		{
			Coordinate current = path.get(i);
			Coordinate next = path.get(i + 1);
			
			int differenceX = next.getX() - current.getX();
			int differenceY = next.getY() - current.getY();
			
			Direction direction = Direction.Up; // placeholder
			if (differenceX == 1 && differenceY == 0)
			{
				direction = Direction.Right;
			}
			else if (differenceX == -1 && differenceY == 0)
			{
				direction = Direction.Left;
			}
			else if (differenceX == 0 && differenceY == 1)
			{
				direction = Direction.Down;
			}
			else if (differenceX == 0 && differenceY == -1)
			{
				direction = Direction.Up;
			}
			calculateDirections(playerMap, directions, current, next, direction);
			
		}
		
		return directions;
		
	}
	
	private void calculateDirections(PlayerMap playerMap, List<Direction> directions, Coordinate current, Coordinate next, Direction direction)
	{
		
		if (playerMap.getMapData().get(current).getTerrain() == Terrain.Grass)
		{
			directions.add(direction);
			if (playerMap.getMapData().get(next).getTerrain() == Terrain.Grass)
			{
				directions.add(direction);
			}
			else if (playerMap.getMapData().get(next).getTerrain() == Terrain.Mountain)
			{
				directions.add(direction);
				directions.add(direction);
			}
		}
		else if (playerMap.getMapData().get(current).getTerrain() == Terrain.Mountain)
		{
			directions.add(direction);
			directions.add(direction);
			if (playerMap.getMapData().get(next).getTerrain() == Terrain.Grass)
			{
				directions.add(direction);
				
			}
			else if (playerMap.getMapData().get(next).getTerrain() == Terrain.Mountain)
			{
				directions.add(direction);
				directions.add(direction);
			}
		}
	}
}
