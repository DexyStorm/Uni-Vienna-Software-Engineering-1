package data.map.halfMap.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.map.BoundsChecker;
import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.halfMap.InternalHalfMap;
import enums.Direction;
import enums.Terrain;

/**
 * at least 40% of the tiles along each edge must allow for a successful
 * transition from my halfmap to the enemy's halfmap. this class is here to
 * ensure that this rule is not broken when my halfmap is created
 */

public class InternalHalfMapValidatorDuo
{
	private final static Logger logger = LoggerFactory.getLogger(InternalHalfMapValidatorDuo.class);
	
	private int getCountWalkableNodes(Direction direction, InternalHalfMap internalEnemyHalfMap, InternalHalfMap internalMyHalfMap, BoundsChecker boundsChecker)
	{
		int transitionableCounter = 0;
		HashMap<Direction, Integer> enemyHalfMapBounds = boundsChecker.getEnemyHalfMapBounds();
		
		for (Entry<Coordinate, InternalNodeData> eachMyCoordinate : internalMyHalfMap.getMapData().entrySet())
		{
			if (checkIfCoordinateIsBorderCoordinate(boundsChecker, direction, eachMyCoordinate) == true)
			{
				if (eachMyCoordinate.getValue().getTerrain() != Terrain.Water)
				{
					
					Coordinate neighborcoordinateCoordinate = createNeighborCoordinate(enemyHalfMapBounds, direction, eachMyCoordinate);
					
					if (internalEnemyHalfMap.getMapData().get(neighborcoordinateCoordinate).getTerrain() != Terrain.Water)
					{
						transitionableCounter = transitionableCounter + 1;
					}
				}
			}
			
		}
		
		return transitionableCounter;
	}
	
	private Coordinate createNeighborCoordinate(HashMap<Direction, Integer> enemyHalfMapBounds, Direction direction, Entry<Coordinate, InternalNodeData> eachMycoordinate)
	{
		if (direction == Direction.Up || direction == Direction.Down)
		{
			return new Coordinate(eachMycoordinate.getKey().getX(), enemyHalfMapBounds.get(direction.inverted()));
		}
		
		return new Coordinate(enemyHalfMapBounds.get(direction.inverted()), eachMycoordinate.getKey().getY());
		
	}
	
	private boolean checkIfCoordinateIsBorderCoordinate(BoundsChecker boundsChecker, Direction direction, Entry<Coordinate, InternalNodeData> eachMycoordinate)
	{
		switch (direction)
		{
			case Direction.Up:
			{
				if (eachMycoordinate.getKey().getY() == boundsChecker.getMyHalfMapBounds().get(Direction.Up))
				{
					return true;
				}
				break;
			}
			case Direction.Down:
			{
				if (eachMycoordinate.getKey().getY() == boundsChecker.getMyHalfMapBounds().get(Direction.Down))
				{
					return true;
				}
				break;
			}
			case Direction.Left:
			{
				if (eachMycoordinate.getKey().getX() == boundsChecker.getMyHalfMapBounds().get(Direction.Left))
				{
					return true;
				}
				break;
			}
			case Direction.Right:
			{
				if (eachMycoordinate.getKey().getX() == boundsChecker.getMyHalfMapBounds().get(Direction.Right))
				{
					return true;
				}
				break;
			}
			
		}
		
		return false;
		
	}
	
	private String writeMessage(Direction direction, int walkableCounter)
	{
		StringBuilder output = new StringBuilder();
		output.append(direction.toString());
		output.append(" is not transitionable enough. There are only ");
		output.append(walkableCounter);
		output.append(" nodes which are accessible.");
		return output.toString();
		
	}
	
	// at least 40% of the tiles along each edge must allow for a
	// successful transition from my halfmap to the enemy's halfmap.
	// the right and left edge have a length of 5:
	// 5/100*40=2 -> at least 2 nodes must be transitionable
	// the up and down edge have a length of 10:
	// 10/100*40=4 -> at least 4 nodes must be transitionable
	public List<String> validateHalfMapBasedOnEnemyMap(BoundsChecker boundsChecker, InternalHalfMap internalEnemyHalfMap, InternalHalfMap internalMyHalfMap)
	{
		List<String> errorMessages = new ArrayList<String>();
		
		int transitionableCounter = 0;
		if (boundsChecker.getEnemyHalfMapDirection() == Direction.Left || boundsChecker.getEnemyHalfMapDirection() == Direction.Up)
		{
			// enemy map is topleft
			
			transitionableCounter = getCountWalkableNodes(Direction.Left, internalEnemyHalfMap, internalMyHalfMap, boundsChecker);
			if (transitionableCounter < 2)
			{
				errorMessages.add(writeMessage(Direction.Left, transitionableCounter));
			}
			
			transitionableCounter = getCountWalkableNodes(Direction.Up, internalEnemyHalfMap, internalMyHalfMap, boundsChecker);
			if (transitionableCounter < 4)
			{
				errorMessages.add(writeMessage(Direction.Up, transitionableCounter));
			}
			
		}
		else if (boundsChecker.getEnemyHalfMapDirection() == Direction.Right)
		{
			// enemy map is right
			
			// checkWalkRight
			transitionableCounter = getCountWalkableNodes(Direction.Right, internalEnemyHalfMap, internalMyHalfMap, boundsChecker);
			if (transitionableCounter < 2)
			{
				errorMessages.add(writeMessage(Direction.Right, transitionableCounter));
			}
			
		}
		else if (boundsChecker.getEnemyHalfMapDirection() == Direction.Down)
		{
			// enemy map is bottom
			
			transitionableCounter = getCountWalkableNodes(Direction.Down, internalEnemyHalfMap, internalMyHalfMap, boundsChecker);
			if (transitionableCounter < 4)
			{
				errorMessages.add(writeMessage(Direction.Down, transitionableCounter));
			}
			
		}
		
		return errorMessages;
		
	}
}
