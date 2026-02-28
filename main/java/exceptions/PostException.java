package exceptions;

public class PostException extends Exception
{
	public PostException(String message)
	{
		super("The server has responded with an error message when i sent a post request. Here is it's message:\n" + message);
	}
}
