package exceptions;

public class TerrainCostAssociactionException extends RuntimeException
{
	public TerrainCostAssociactionException(String message)
	{
		super("Was not able to associate terrain with a cost" + message);
	}
}
