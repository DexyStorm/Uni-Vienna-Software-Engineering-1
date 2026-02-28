package view;

import java.util.Map.Entry;

import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.halfMap.InternalHalfMap;
import enums.Terrain;

public class InternalHalfMapView
{
	public StringBuilder getOutputStringForInternalHalfMap(InternalHalfMap internalHalfMap)
	{
		StringBuilder output = new StringBuilder();
		
		if (internalHalfMap.getMapData().isEmpty() == false)
		{
			
			int smallestX = 0;
			int biggestX = 9;
			int smallestY = 0;
			int biggestY = 4;
			
			for (Entry<Coordinate, InternalNodeData> eachNode : internalHalfMap.getMapData().entrySet())
			{
				if (eachNode.getKey().getX() > biggestX)
				{
					biggestX = eachNode.getKey().getX();
				}
				
				if (eachNode.getKey().getX() < smallestX)
				{
					smallestX = eachNode.getKey().getX();
				}
				
				if (eachNode.getKey().getY() > biggestY)
				{
					biggestY = eachNode.getKey().getY();
				}
				
				if (eachNode.getKey().getY() < smallestY)
				{
					smallestY = eachNode.getKey().getY();
				}
			}
			
			for (int y = smallestY; y <= biggestY; ++y)
			{
				
				for (int x = smallestX; x <= biggestX; ++x)
				{
					
					InternalNodeData eachNode = internalHalfMap.getMapData().get(new Coordinate(x, y));
					
					if (eachNode.getTerrain().equals(Terrain.Grass))
					{
						if (new Coordinate(x, y).equals(internalHalfMap.getEntities().getMyCastle()))
						{
							output.append("🏰");
						}
						else
						{
							output.append("🌿");
						}
						
					}
					if (eachNode.getTerrain().equals(Terrain.Mountain))
					{
						output.append("🏔️");
					}
					if (eachNode.getTerrain().equals(Terrain.Water))
					{
						output.append("🌊");
					}
					
				}
				
				output.append("\n");
				
			}
			
		}
		return output;
	}
	
	public void printInternalHalfMap(InternalHalfMap internalHalfMap)
	{
		StringBuilder internalHalfMapOutput = new StringBuilder();
		internalHalfMapOutput.append("====================\n");
		internalHalfMapOutput.append(getOutputStringForInternalHalfMap(internalHalfMap));
		// internalHalfMapOutput.append("\n");
		internalHalfMapOutput.append("====================");
		System.out.println(internalHalfMapOutput);
		internalHalfMapOutput.append("\n\n");
	}
}
