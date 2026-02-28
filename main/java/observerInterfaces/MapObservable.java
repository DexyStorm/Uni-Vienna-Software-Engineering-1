package observerInterfaces;

import java.util.ArrayList;
import java.util.List;

import data.map.fullMap.InternalFullMap;

public interface MapObservable
{
	List<MapObserver> mapObservers = new ArrayList<MapObserver>();
	
	public default void addMapObserver(MapObserver mapObserver)
	{
		mapObservers.add(mapObserver);
	}
	
	public default void removeMapObserver(MapObserver mapObserver)
	{
		mapObservers.remove(mapObserver);
	}
	
	public default void notifyMapObservers(InternalFullMap internalFullMap, int roundCounter)
	{
		for (MapObserver eachMapObserver : mapObservers)
		{
			eachMapObserver.updateMapObserver(internalFullMap, roundCounter);
		}
	}
}
