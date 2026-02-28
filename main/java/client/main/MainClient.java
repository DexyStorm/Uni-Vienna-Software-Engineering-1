package client.main;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.GameData;
import data.entities.Entities;
import data.entities.Player;
import data.map.BoundsChecker;
import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.fullMap.InternalFullMap;
import data.map.halfMap.InternalHalfMap;
import enums.GameStatus;
import exceptions.GetException;
import exceptions.PlayerNotFoundException;
import exceptions.PostException;
import gameCoordinator.GameCoordinator;
import jakarta.xml.bind.JAXBException;
import network.NetworkClient;
import view.RoundView;

public class MainClient
{
	private final static Logger logger = LoggerFactory.getLogger(MainClient.class);
	
	public static void main(String[] args)
	{
		// should stay here otherwise eclipse terminal won't open for some reason
		// System.out.println("");
		
		/*
		 * just some stuff for debugging i only commented those lines out cuz i didn't
		 * feel like re-writing them each time i wanted to do some debugging
		 */
//		args[0] = "TR";
//		args[1] = "http://swe1.wst.univie.ac.at:18235";
//		args[2] = "dhv34";
		
		// when u execute the program, u always need to have 3 command line arguments
		if (args.length != 3)
		{
			StringBuilder exceptionMessage = new StringBuilder();
			exceptionMessage.append("This application needs exactly 3 CLI arguments.\n1: Game Type\n2: URL\n3: Game Code\nYour arguments were:\n");
			for (int i = 0; i < args.length; ++i)
			{
				exceptionMessage.append(i + " " + args[i] + "\n");
			}
			
			throw new IllegalArgumentException(exceptionMessage.toString());
		}
		
		// i never use anything other than the "normal" (tournament) mode
		// so i don't need gameMode
		// String gameMode = args[0];
		String serverBaseUrl = args[1];
		String gameId = args[2];
		
		int minSleepDuration = 400;
		NetworkClient networkClient = new NetworkClient(GameStatus.Wait, gameId, System.currentTimeMillis(), minSleepDuration, serverBaseUrl);
		
		Coordinate defaultCoordinate = new Coordinate(-1, -1);
		
		Player myPlayer = new Player(defaultCoordinate, false);
		Player enemyPlayer = new Player(defaultCoordinate, false);
		
		Entities entities = new Entities(defaultCoordinate, defaultCoordinate, myPlayer, enemyPlayer, defaultCoordinate);
		
		InternalFullMap internalFullMap = new InternalFullMap(entities, new HashMap<Coordinate, InternalNodeData>());
		InternalHalfMap internalMyHalfMap = new InternalHalfMap(entities, new HashMap<Coordinate, InternalNodeData>());
		InternalHalfMap internalEnemyHalfMap = new InternalHalfMap(entities, new HashMap<Coordinate, InternalNodeData>());
		
		BoundsChecker boundsChecker = new BoundsChecker();
		
		GameData gameData = new GameData(boundsChecker, internalFullMap, internalMyHalfMap, internalEnemyHalfMap);
		RoundView roundView = new RoundView();
		
		GameCoordinator gameCoordinator = new GameCoordinator(gameData, 0, networkClient, roundView);
		
		gameData.addMapObserver(roundView);
		internalFullMap.addMapObserver(internalMyHalfMap);
		internalFullMap.addMapObserver(internalEnemyHalfMap);
		networkClient.addGameStateObserver(gameData.getInternalFullMap());
		
		try
		{
			gameCoordinator.registerPlayer("firstName", "lastName", "uspaceNickName");
			
			gameCoordinator.exchangeMaps();
			gameCoordinator.prepare();
			gameCoordinator.startGame();
		}
		catch (PostException postException)
		{
			System.err.println(postException.getMessage() + postException.getStackTrace());
		}
		catch (GetException getException)
		{
			System.err.println(getException.getMessage() + getException.getStackTrace());
		}
		catch (PlayerNotFoundException playerNotFoundException)
		{
			System.err.println(playerNotFoundException.getMessage() + playerNotFoundException.getStackTrace());
		}
		catch (JAXBException JAXBException)
		{
			System.err.println(JAXBException.getStackTrace());
		}
		catch (Exception exception)
		{
			System.err.println(exception.getStackTrace());
		}
		
	}
	
}
