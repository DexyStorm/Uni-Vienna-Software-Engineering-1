package exceptions;

public class InvalidArgumentException extends Exception
{
	public InvalidArgumentException(String message)
	{
		super("This application needs exactly 3 CLI arguments.\n1: Game Type\n2: URL\n3: Game Code\nYour arguments were:" + message);
	}
}
