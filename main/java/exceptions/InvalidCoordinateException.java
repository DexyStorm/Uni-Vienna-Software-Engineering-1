package exceptions;

public class InvalidCoordinateException extends RuntimeException
{
	public InvalidCoordinateException(String message)
	{
		super("Invalid coordinate was passed " + message);
	}
}
