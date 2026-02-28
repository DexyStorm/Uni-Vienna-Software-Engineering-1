package network;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import data.entities.Entities;
import data.entities.Player;
import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.halfMap.InternalHalfMap;
import enums.Direction;
import enums.InternalExploredState;
import enums.Terrain;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.FullMapNode;

/**
 * the converter is only for converting objects into the format which the server
 * wants. marshalling is done in translator
 */
public class Converter
{
	
	public Converter()
	{
		
	}
	
	// i think this will become useless after the first time the InternalFullMap is
	// set
	public HashMap<Coordinate, InternalNodeData> convertFromExternalToInternalMapData(FullMap externalFullMap)
	{
		
		Terrain terrain = Terrain.Grass;
		InternalExploredState exploredState;
		HashMap<Coordinate, InternalNodeData> hashMap = new HashMap<Coordinate, InternalNodeData>();
		
		for (FullMapNode eachExternalFullMapNode : externalFullMap.getMapNodes())
		{
			terrain = Terrain.Water;
			exploredState = InternalExploredState.NotExplored;
			
			int X = eachExternalFullMapNode.getX();
			int Y = eachExternalFullMapNode.getY();
			if (eachExternalFullMapNode.getTerrain() == ETerrain.Water)
			{
				terrain = Terrain.Water;
			}
			else if (eachExternalFullMapNode.getTerrain() == ETerrain.Mountain)
			{
				terrain = Terrain.Mountain;
			}
			else if (eachExternalFullMapNode.getTerrain() == ETerrain.Grass)
			{
				terrain = Terrain.Grass;
				
			}
			
			Coordinate coordinate = new Coordinate(X, Y);
			InternalNodeData internalNodeData = new InternalNodeData(terrain, exploredState);
			hashMap.put(coordinate, internalNodeData);
		}
		return hashMap;
		
	}
	
	public Entities convertFromExternalToInternalEntities(FullMap externalFullMap)
	{
		
		Coordinate defaultCoordinate = new Coordinate(-1, -1);
		Coordinate myCastle = defaultCoordinate;
		Coordinate enemyCastle = defaultCoordinate;
		Player myPlayer = new Player(defaultCoordinate, false);
		Player enemyPlayer = new Player(defaultCoordinate, false);
		Coordinate treasure = defaultCoordinate;
		
		for (FullMapNode eachExternalFullMapNode : externalFullMap.getMapNodes())
		{
			if (eachExternalFullMapNode.getTerrain() == ETerrain.Grass)
			{
				if (eachExternalFullMapNode.getTreasureState() == ETreasureState.MyTreasureIsPresent)
				{
					treasure = new Coordinate(eachExternalFullMapNode.getX(), eachExternalFullMapNode.getY());
				}
				else if (eachExternalFullMapNode.getFortState() == EFortState.MyFortPresent)
				{
					myCastle = new Coordinate(eachExternalFullMapNode.getX(), eachExternalFullMapNode.getY());
				}
				else if (eachExternalFullMapNode.getFortState() == EFortState.EnemyFortPresent)
				{
					enemyCastle = new Coordinate(eachExternalFullMapNode.getX(), eachExternalFullMapNode.getY());
				}
			}
			
			if (eachExternalFullMapNode.getPlayerPositionState() == EPlayerPositionState.MyPlayerPosition)
			{
				myPlayer.setLocation(new Coordinate(eachExternalFullMapNode.getX(), eachExternalFullMapNode.getY()));
			}
			else if (eachExternalFullMapNode.getPlayerPositionState() == EPlayerPositionState.EnemyPlayerPosition)
			{
				enemyPlayer.setLocation(new Coordinate(eachExternalFullMapNode.getX(), eachExternalFullMapNode.getY()));
			}
			
			else if (eachExternalFullMapNode.getPlayerPositionState() == EPlayerPositionState.BothPlayerPosition)
			{
				myPlayer.setLocation(new Coordinate(eachExternalFullMapNode.getX(), eachExternalFullMapNode.getY()));
				enemyPlayer.setLocation(new Coordinate(eachExternalFullMapNode.getX(), eachExternalFullMapNode.getY()));
			}
		}
		
		Entities newEntities = new Entities(myCastle, enemyCastle, myPlayer, enemyPlayer, treasure);
		return newEntities;
		
	}
	
	public PlayerHalfMap convertHalfMap(InternalHalfMap internalHalfMap, String uniquePlayerID)
	{
		Set<PlayerHalfMapNode> halfMapNodes = new HashSet<PlayerHalfMapNode>();
		
		for (Entry<Coordinate, InternalNodeData> eachInternalHalfMapNode : internalHalfMap.getMapData().entrySet())
		{
			int X = eachInternalHalfMapNode.getKey().getX();
			int Y = eachInternalHalfMapNode.getKey().getY();
			ETerrain externalTerrain = ETerrain.Grass;
			boolean fortPresent = false;
			
			if (eachInternalHalfMapNode.getValue().getTerrain() == Terrain.Grass)
			{
				externalTerrain = ETerrain.Grass;
				if (eachInternalHalfMapNode.getKey() == internalHalfMap.getEntities().getMyCastle())
				{
					externalTerrain = ETerrain.Grass;
					fortPresent = true;
				}
			}
			
			else if (eachInternalHalfMapNode.getValue().getTerrain() == Terrain.Water)
			{
				externalTerrain = ETerrain.Water;
			}
			else if (eachInternalHalfMapNode.getValue().getTerrain() == Terrain.Mountain)
			{
				externalTerrain = ETerrain.Mountain;
			}
			
			PlayerHalfMapNode externalPlayerHalfMapNode = new PlayerHalfMapNode(X, Y, fortPresent, externalTerrain);
			
			halfMapNodes.add(externalPlayerHalfMapNode);
			
		}
		
		PlayerHalfMap externalPlayerHalfMap = new PlayerHalfMap(uniquePlayerID, halfMapNodes);
		
		return externalPlayerHalfMap;
	}
	
	public EMove convertFromInternalToExternalMove(Direction internalMove)
	{
		switch (internalMove)
		{
			case Direction.Down:
				return EMove.Down;
			
			case Direction.Up:
				return EMove.Up;
			
			case Direction.Left:
				return EMove.Left;
			
			case Direction.Right:
				return EMove.Right;
			
			default:
				return EMove.Up;
		}
	}
	
}
