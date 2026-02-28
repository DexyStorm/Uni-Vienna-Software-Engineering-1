package data.map.fullMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.entities.Entities;
import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.PlayerMap;
import enums.InternalExploredState;
import enums.Terrain;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.GameState;
import network.Converter;
import observerInterfaces.GameStateObserver;
import observerInterfaces.MapObservable;

public class InternalFullMap extends PlayerMap implements GameStateObserver, MapObservable
{
	private final static Logger logger = LoggerFactory.getLogger(InternalFullMap.class);
	
	public InternalFullMap(Entities entities, HashMap<Coordinate, InternalNodeData> mapData)
	{
		super(entities, mapData);
		
	}
	
	private void markLocationAroundMountainVisited(Coordinate newVisitedCoordinate)
	{
		if (mapData.get(newVisitedCoordinate).getTerrain() == Terrain.Mountain)
		{
			// 1 and -1 are used and iterated over here so that they can get added to the
			// newVisitedCoordinate to easily get all the neighbors (cuz the neighbors are
			// always at max 1 or -1 away)
			
			for (int x = -1; x <= 1; x++)
			{
				for (int y = -1; y <= 1; y++)
				{
					Coordinate neighbor = new Coordinate(newVisitedCoordinate.getX() + x, newVisitedCoordinate.getY() + y);
					
					if (mapData.containsKey(neighbor))
					{
						if (mapData.get(neighbor).getTerrain() == Terrain.Grass)
						{
							// this line needs to be here, otherwise the castle won't be displayed anymore
							if (mapData.get(neighbor).getInternalExploredState() == InternalExploredState.NotExplored)
							{
								this.mapData.get(neighbor).setInternalExploredState(InternalExploredState.Explored);
							}
						}
						
					}
					
				}
			}
			
		}
	}
	
	private void updateMapData(FullMap externalFullMap)
	{
		Converter converter = new Converter();
		
		Set<Coordinate> alreadyVisited = getAlreadyVisited();
		
		Entities newEntities = converter.convertFromExternalToInternalEntities(externalFullMap);
		newEntities.getMyPlayer().setTreasureCollected(this.entities.getMyPlayer().ifTreasureCollected());
		newEntities.getEnemyPlayer().setTreasureCollected(this.entities.getEnemyPlayer().ifTreasureCollected());
		this.entities.setEntities(newEntities);
		
		HashMap<Coordinate, InternalNodeData> newMapData = converter.convertFromExternalToInternalMapData(externalFullMap);
		this.mapData = newMapData;
		
		markLocationAsVisited(this.entities.getMyPlayer().getLocation());
		
		for (Coordinate eachAlreadyPreviouslyVisitedNode : alreadyVisited)
		{
			this.mapData.get(eachAlreadyPreviouslyVisitedNode).setInternalExploredState(InternalExploredState.Explored);
		}
	}
	
	public void markLocationAsVisited(Coordinate currentPlayerPosition)
	{
		if (checkIfLocationIsInsideMap(currentPlayerPosition))
		{
			// if not explored
			if (this.mapData.get(currentPlayerPosition).getInternalExploredState() == InternalExploredState.NotExplored)
			{
				// set as explored
				this.mapData.get(currentPlayerPosition).setInternalExploredState(InternalExploredState.Explored);
				
				// if standing on a mountain
				if (this.mapData.get(currentPlayerPosition).getTerrain() == Terrain.Mountain)
				{
					// mark the nodes around the mountain as visited
					markLocationAroundMountainVisited(currentPlayerPosition);
				}
			}
		}
		
	}
	
	public Set<Coordinate> getAlreadyVisited()
	{
		Set<Coordinate> alreadyVisited = new HashSet<Coordinate>();
		
		for (Entry<Coordinate, InternalNodeData> eachNode : mapData.entrySet())
		{
			if (eachNode.getValue().getInternalExploredState() == InternalExploredState.Explored)
			{
				alreadyVisited.add(eachNode.getKey());
			}
		}
		
		return alreadyVisited;
	}
	
	public void setFullMapData(HashMap<Coordinate, InternalNodeData> fullMapData)
	{
		this.mapData = fullMapData;
	}
	
	@Override
	public void updateGameStateObserver(GameState gameState, int roundCounter)
	{
		updateMapData(gameState.getMap());
		
		notifyMapObservers(this, roundCounter);
		
	}
	
}
