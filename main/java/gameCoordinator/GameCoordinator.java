package gameCoordinator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.GameData;
import enums.Direction;
import enums.GameStatus;
import exceptions.GetException;
import exceptions.PlayerNotFoundException;
import exceptions.PostException;
import jakarta.xml.bind.JAXBException;
import network.NetworkClient;
import view.RoundView;

public class GameCoordinator
{
	
	private final static Logger logger = LoggerFactory.getLogger(GameCoordinator.class);
	
	GameData gameData;
	int roundCounter;
	RoundView roundView;
	NetworkClient networkClient;
	
	public GameCoordinator(GameData gameData, int roundCounter, NetworkClient networkClient, RoundView roundView)
	{
		this.gameData = gameData;
		this.roundCounter = roundCounter;
		this.networkClient = networkClient;
		this.roundView = roundView;
		
	}
	
	private boolean checkGameStillPlays()
	{
		if (networkClient.getGameStatus() == GameStatus.Act || networkClient.getGameStatus() == GameStatus.Wait)
		{
			return true;
		}
		return false;
	}
	
	public void exchangeMaps() throws GetException, PostException, PlayerNotFoundException, JAXBException
	{
		
		// updating GameState to get the external Map to check if the enemy has already
		// sent his map
		networkClient.updateGameState(gameData.getInternalFullMap(), roundCounter);
		
		gameData.exchangeMaps(networkClient);
		
	}
	
	public void prepare() throws GetException, PlayerNotFoundException
	{
		gameData.prepare();
		networkClient.initializeEnemyUniquePlayerID();
		networkClient.updateGameState(gameData.getInternalFullMap(), roundCounter);
	}
	
	public void startGame() throws GetException, PostException, PlayerNotFoundException, JAXBException
	{
		
		while (checkGameStillPlays())
		{
			
			List<Direction> moves = gameData.getMoves(roundCounter);
			
			for (Direction eachMove : moves)
			{
				if (checkGameStillPlays() == false)
				{
					break;
				}
				
				networkClient.sendMove(eachMove);
				roundCounter = roundCounter + 1;
				if (roundCounter == 8)
				{
					logger.info("Round 8. Calculating possible enemy castle locations.");
					gameData.extractPossibleLocationsEnemyCastle();
				}
				
				// if the treasure has been found and there are still moves left,
				// just clear the moves
				boolean treasureBeforeUpdate = gameData.getInternalEnemyHalfMap().getEntities().getMyPlayer().ifTreasureCollected();
				networkClient.updateGameState(gameData.getInternalFullMap(), roundCounter);
				if ((treasureBeforeUpdate == false) && (gameData.getInternalEnemyHalfMap().getEntities().getMyPlayer().ifTreasureCollected() == true))
				{
					moves.clear();
					break;
				}
				
			}
		}
		
		roundView.printGameOver(networkClient.getGameStatus());
		
	}
	
	public void registerPlayer(String firstName, String lastName, String userName) throws PostException, GetException, PlayerNotFoundException
	{
		networkClient.registerPlayer(firstName, lastName, userName);
		
	}
	
}
