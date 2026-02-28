package observerInterfaces;

import messagesbase.messagesfromserver.GameState;

public interface GameStateObserver
{
	public void updateGameStateObserver(GameState gameState, int roundCounter);
}
