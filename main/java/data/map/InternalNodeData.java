package data.map;

import java.util.Objects;

import enums.InternalExploredState;
import enums.Terrain;

public class InternalNodeData
{
	private Terrain terrain;
	private InternalExploredState internalExploredState;
	
	public InternalNodeData(Terrain terrain, InternalExploredState internalExploredState)
	{
		this.terrain = terrain;
		this.internalExploredState = internalExploredState;
	}
	
	// setters & getters
	
	public Terrain getTerrain()
	{
		return terrain;
	}
	
	public InternalExploredState getInternalExploredState()
	{
		return internalExploredState;
	}
	
	public void setInternalExploredState(InternalExploredState internalExploredState)
	{
		this.internalExploredState = internalExploredState;
	}
	
	public void setTerrain(Terrain terrain)
	{
		this.terrain = terrain;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(internalExploredState, terrain);
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
		InternalNodeData other = (InternalNodeData) obj;
		return internalExploredState == other.internalExploredState && terrain == other.terrain;
	}
	
	@Override
	public String toString()
	{
		return "InternalNodeData [terrain=" + terrain + ", internalExploredState=" + internalExploredState + "]";
	}
	
}
