package observerInterfaces;

import data.map.fullMap.InternalFullMap;

public interface MapObserver
{
	public void updateMapObserver(InternalFullMap internalFullMap, int roundCounter);
	
}
