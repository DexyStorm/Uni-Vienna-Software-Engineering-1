package exceptions;

public class NoPathFoundException extends RuntimeException
{
	public NoPathFoundException(String message)
	{
		super("No path to the goal was found. " + message);
	}
}
