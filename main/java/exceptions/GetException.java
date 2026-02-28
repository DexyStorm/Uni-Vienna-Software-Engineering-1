package exceptions;

public class GetException extends Exception
{
	public GetException(String message)
	{
		super("The server has responded with an error message when i sent a get request. Here is it's message:\n" + message);
	}
}
