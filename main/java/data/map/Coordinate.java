package data.map;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import enums.Terrain;

public class Coordinate
{
	
	private int x;
	private int y;
	
	public Coordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	private boolean checkIfCoordinateIsValid(PlayerMap playerMap)
	{
		
		if (playerMap.checkIfLocationIsInsideMap(this))
		{
			if (playerMap.getMapData().get(this).getTerrain() != Terrain.Water)
			{
				return true;
			}
		}
		return false;
	}
	
	private Set<Coordinate> filterValidCoordinates(PlayerMap playerMap, Collection<Coordinate> coordinatesToCheck)
	{
		Set<Coordinate> filteredCoordinates = new HashSet<Coordinate>();
		
		for (Coordinate coordinateToCheck : coordinatesToCheck)
		{
			if (coordinateToCheck.checkIfCoordinateIsValid(playerMap))
			{
				filteredCoordinates.add(coordinateToCheck);
			}
			
		}
		
		return filteredCoordinates;
	}
	
	private Set<Coordinate> addDifferenceToCoordinate(Set<Coordinate> neighborsDifference)
	{
		Set<Coordinate> neighbors = new HashSet<Coordinate>();
		
		for (Coordinate eachNeighborDifference : neighborsDifference)
		{
			Coordinate newNeighbor = new Coordinate(this.getX() + eachNeighborDifference.getX(), this.getY() + eachNeighborDifference.getY());
			
			neighbors.add(newNeighbor);
		}
		
		return neighbors;
	}
	
	private Set<Coordinate> getDiagonalNeighbors(PlayerMap playerMap)
	{
		Set<Coordinate> diagonalNeighborsDifference = new HashSet<Coordinate>(
				Arrays.asList(new Coordinate(1, 1), new Coordinate(-1, -1), new Coordinate(-1, 1), new Coordinate(1, -1)));
		
		Set<Coordinate> validDiagonalNeighbors = filterValidCoordinates(playerMap, addDifferenceToCoordinate(diagonalNeighborsDifference));
		
		return validDiagonalNeighbors;
	}
	
	public Set<Coordinate> getCardinalNeighbors(PlayerMap playerMap)
	{
//		Set<Coordinate> neighbors = new HashSet<Coordinate>();
		Set<Coordinate> cardinalNeighborsDifference = new HashSet<Coordinate>(
				Arrays.asList(new Coordinate(1, 0), new Coordinate(-1, 0), new Coordinate(0, 1), new Coordinate(0, -1)));
		
		Set<Coordinate> validCardinalNeighbors = filterValidCoordinates(playerMap, addDifferenceToCoordinate(cardinalNeighborsDifference));
		
		return validCardinalNeighbors;
		
	}
	
	public Set<Coordinate> getAllNeighbors(PlayerMap playerMap)
	{
		
		Set<Coordinate> allNeighbors = (getCardinalNeighbors(playerMap));
		allNeighbors.addAll(getDiagonalNeighbors(playerMap));
		
		return allNeighbors;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(x, y);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		return x == other.x && y == other.y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public void setXY(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	@Override
	public String toString()
	{
		return "[x=" + x + ", y=" + y + "]";
	}
	
}
