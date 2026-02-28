package observerInterfaces;

import java.util.ArrayList;
import java.util.List;

import messagesbase.messagesfromserver.GameState;

public interface GameStateObservable
{
	List<GameStateObserver> gameStateObservers = new ArrayList<GameStateObserver>();
	
	public default void addGameStateObserver(GameStateObserver gameStateObserver)
	{
		gameStateObservers.add(gameStateObserver);
	}
	
	public default void removeGameStateObserver(GameStateObserver gameStateObserver)
	{
		gameStateObservers.remove(gameStateObserver);
	}
	
	public default void notifyGameStateObservers(GameState gameState, int roundCounter)
	{
		for (GameStateObserver eachGameStateObserver : gameStateObservers)
		{
			eachGameStateObserver.updateGameStateObserver(gameState, roundCounter);
		}
	}
	
}
