package exceptions;

public class InvalidEvaluationException extends RuntimeException
{
	public InvalidEvaluationException(String message)
	{
		super("The most valuable node has a value of " + message);
	}
}
