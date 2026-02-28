package network;

import java.io.StringWriter;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import data.map.fullMap.InternalFullMap;
import data.map.halfMap.InternalHalfMap;
import enums.Direction;
import enums.GameStatus;
import exceptions.GetException;
import exceptions.PlayerNotFoundException;
import exceptions.PostException;
import jakarta.xml.bind.JAXBException;
import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;
import observerInterfaces.GameStateObservable;
import reactor.core.publisher.Mono;

public class NetworkClient implements GameStateObservable
{
	private final static Logger logger = LoggerFactory.getLogger(NetworkClient.class);
	
	// gameMode is for when/if you want to make a custom GUI for the game
	// currently gameMode will always be "TR" -> CLI
	// private final String gameMode;
	private GameStatus gameStatus;
	private final String gameId;
	private long lastRequestTime;
	private final int minSleepDuration;
	WebClient baseWebClient;
	private String myUniquePlayerID;
	private String enemyUniquePlayerID;
	
	public NetworkClient(GameStatus gameStatus, String gameId, long lastRequestTime, int minSleepDuration, String serverBaseUrl)
	{
		// this.gameMode = gameMode;
		this.gameStatus = gameStatus;
		this.gameId = gameId;
		this.lastRequestTime = lastRequestTime;
		
		this.minSleepDuration = minSleepDuration;
		this.baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games").defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
		
	}
	
	// maybe object is bad here?
	// maybe wrapping the Identifiers would be better?
	private <T> ResponseEnvelope<T> sendPost(String linkAppend, Object body, ParameterizedTypeReference<ResponseEnvelope<T>> typeRef) throws PostException, GetException
	{
		// no need to check whose turn it is when you want to register
		// registration happens if linkAppend == "/players"
		if ((linkAppend == "/players") == false)
		{
			continueWhenMyTurn();
		}
		
		waitTimeOut();
		
		Mono<ResponseEnvelope<T>> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + linkAppend).body(BodyInserters.fromValue(body)).retrieve().bodyToMono(typeRef);
		
		ResponseEnvelope<T> postResult = webAccess.block();
		lastRequestTime = System.currentTimeMillis();
		
		if (postResult.getState() == ERequestState.Error)
		{
			logger.error("Post request has failed.");
			throw new PostException(postResult.getExceptionMessage());
		}
		
		return postResult;
		
	}
	
	private <T> ResponseEnvelope<T> sendGet(String linkAppend, ParameterizedTypeReference<ResponseEnvelope<T>> typeRef) throws GetException
	{
		// don't
		// continueWhenMyTurn();
		waitTimeOut();
		Mono<ResponseEnvelope<T>> webAccess = baseWebClient.method(HttpMethod.GET).uri("/" + gameId + linkAppend).retrieve().bodyToMono(typeRef);
		
		ResponseEnvelope<T> requestResult = webAccess.block();
		lastRequestTime = System.currentTimeMillis();
		
		if (requestResult.getState() == ERequestState.Error)
		{
			logger.error("Get request has failed.");
			throw new GetException(requestResult.getExceptionMessage());
		}
		
		return requestResult;
	}
	
	private void delayAction(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException interruptException)
		{
			logger.error(interruptException.getMessage());
			interruptException.printStackTrace();
		}
	}
	
	private PlayerState getMyPlayer(Set<PlayerState> players) throws PlayerNotFoundException
	{
		for (PlayerState eachPlayer : players)
		{
			if (eachPlayer.getUniquePlayerID().equals(this.myUniquePlayerID))
			{
				
				return eachPlayer;
				
			}
			
		}
		
		logger.error("Could not find my Player when searching through the PlayerSates");
		throw new PlayerNotFoundException("My myUniquePlayerID was: " + this.myUniquePlayerID);
		
	}
	
	private PlayerState getEnemyPlayer(Set<PlayerState> players) throws GetException, PlayerNotFoundException
	{
		
		for (PlayerState eachPlayer : players)
		{
			if (eachPlayer.getUniquePlayerID().equals(this.myUniquePlayerID) == false)
			{
				return eachPlayer;
			}
		}
		logger.error("Could not find enemy Player when searching through the PlayerSates");
		throw new PlayerNotFoundException("Enemy enemyUniquePlayerID was: " + this.enemyUniquePlayerID);
	}
	
	public void initializeEnemyUniquePlayerID() throws GetException, PlayerNotFoundException
	{
		ResponseEnvelope<GameState> requestResult = sendGet("/states/" + this.myUniquePlayerID, new ParameterizedTypeReference<ResponseEnvelope<GameState>>()
		{
		});
		
		Set<PlayerState> players = requestResult.getData().get().getPlayers();
		
		this.enemyUniquePlayerID = getEnemyPlayer(players).getUniquePlayerID();
		
	}
	
	public void registerPlayer(String firstName, String lastName, String userName) throws PostException, GetException
	{
		logger.info("Registering player...");
		PlayerRegistration playerReg = new PlayerRegistration(firstName, lastName, userName);
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = sendPost("/players", playerReg, new ParameterizedTypeReference<ResponseEnvelope<UniquePlayerIdentifier>>()
		{
		});
		
		this.myUniquePlayerID = resultReg.getData().get().getUniquePlayerID();
		logger.info("Finished registering player");
	}
	
	public void updateGameState(InternalFullMap internalFullMap, int roundCounter) throws GetException, PlayerNotFoundException
	{
		continueWhenMyTurn();
		ResponseEnvelope<GameState> requestResult = sendGet("/states/" + this.myUniquePlayerID, new ParameterizedTypeReference<ResponseEnvelope<GameState>>()
		{
		});
		
		checkIfGameEnded(getMyPlayer(requestResult.getData().get().getPlayers()));
		
		this.notifyGameStateObservers(requestResult.getData().get(), roundCounter);
		
		// this is shit. but idk how else i could do it
		// i don't see how i could solve this nicely with observer pattern
		internalFullMap.getEntities().getMyPlayer().updateTreasure(getMyPlayer(requestResult.getData().get().getPlayers()));
		internalFullMap.getEntities().getEnemyPlayer().updateTreasure(getEnemyPlayer(requestResult.getData().get().getPlayers()));
		
	}
	
	public void waitTimeOut()
	{
		long nowTime = System.currentTimeMillis();
		long elapsedTime = nowTime - lastRequestTime;
		
		if (minSleepDuration > elapsedTime)
		{
			long sleepTime = minSleepDuration - elapsedTime;
			delayAction(sleepTime);
		}
		
	}
	
	public void continueWhenMyTurn() throws GetException
	{
		gameStatus = GameStatus.Wait;
		while (gameStatus == GameStatus.Wait)
		{
			
			waitTimeOut();
			ResponseEnvelope<GameState> requestResult = sendGet("/states/" + this.myUniquePlayerID, new ParameterizedTypeReference<ResponseEnvelope<GameState>>()
			{
			});
			
			GameState currentServerGameState = requestResult.getData().get();
			
			Set<PlayerState> players = currentServerGameState.getPlayers();
			
			for (PlayerState player : players)
			{
				if (player.getUniquePlayerID().equals(this.myUniquePlayerID))
				{
					if (player.getState().equals(EPlayerGameState.MustAct))
					{
						gameStatus = GameStatus.Act;
						return;
						
					}
					checkIfGameEnded(player);
					
				}
				
			}
		} // end while (gameStatus == GameStatus.Wait)
		
	}
	
	public void sendInternalHalfMap(InternalHalfMap internalHalfMap) throws PostException, GetException, JAXBException
	{
		
		// first convert it
		Converter converter = new Converter();
		PlayerHalfMap convertedExternalHalfMap = converter.convertHalfMap(internalHalfMap, this.myUniquePlayerID);
		
		// then translate (=marshal) it
		Translator translator = new Translator();
		StringWriter marshalledPlayerHalfMap = translator.translateInternalToExternalHalfMap(convertedExternalHalfMap);
		String marshalledPlayerHalfMapString = marshalledPlayerHalfMap.toString();
		
		// then send it
		sendPost("/halfmaps", marshalledPlayerHalfMapString, new ParameterizedTypeReference<ResponseEnvelope<PlayerHalfMap>>()
		{
		});
		
	}
	
	public void checkIfGameEnded(PlayerState player)
	{
		
		if (player.getState().equals(EPlayerGameState.Won))
		{
			gameStatus = GameStatus.Won;
			
		}
		else if (player.getState().equals(EPlayerGameState.Lost))
		{
			gameStatus = GameStatus.Lost;
			
		}
		
	}
	
	public void sendMove(Direction direction) throws PostException, GetException, JAXBException
	{
		Converter converter = new Converter();
		EMove externalMove = converter.convertFromInternalToExternalMove(direction);
		
		Translator translator = new Translator();
		
		PlayerMove playerMove = PlayerMove.of(this.myUniquePlayerID, externalMove);
		
		StringWriter marshalledPlayerMove = translator.translateInternalToExternalMove(playerMove);
		
		String marshalledPlayerMoveString = marshalledPlayerMove.toString();
		
		sendPost("/moves", marshalledPlayerMoveString, new ParameterizedTypeReference<ResponseEnvelope<GameState>>()
		{
		});
		
	}
	
	public boolean checkIfTreasureCollected(Set<PlayerState> players)
	{
		
		for (PlayerState player : players)
		{
			if (player.getUniquePlayerID().equals(this.myUniquePlayerID))
			{
				if (player.hasCollectedTreasure() == false)
				{
					return false;
				}
				
			}
			
		}
		
		return true;
	}
	
	public GameStatus getGameStatus()
	{
		return gameStatus;
	}
	
}
