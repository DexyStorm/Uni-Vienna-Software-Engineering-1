package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import algorithms.MoveGenerator;
import algorithms.goalFinders.PossibleEnemyCastleLocationsExtractor;
import data.map.BoundsChecker;
import data.map.Coordinate;
import data.map.fullMap.InternalFullMap;
import data.map.halfMap.HalfMapExtractor;
import data.map.halfMap.InternalHalfMap;
import data.map.halfMap.validation.InternalHalfMapValidatorDuo;
import data.map.halfMap.validation.InternalHalfMapValidatorSolo;
import enums.Direction;
import exceptions.GetException;
import exceptions.PlayerNotFoundException;
import exceptions.PostException;
import jakarta.xml.bind.JAXBException;
import network.NetworkClient;
import observerInterfaces.MapObservable;
import view.InternalHalfMapView;

public class GameData implements MapObservable
{
	private final static Logger logger = LoggerFactory.getLogger(GameData.class);
	
	private BoundsChecker boundsChecker;
	private InternalFullMap internalFullMap;
	private InternalHalfMap internalMyHalfMap;
	private InternalHalfMap internalEnemyHalfMap;
	private Set<Coordinate> possibleEnemyCastleLocations;
	
	public GameData(BoundsChecker boundsChecker, InternalFullMap internalFullMap, InternalHalfMap internalMyHalfMap, InternalHalfMap internalEnemyHalfMap)
	{
		this.boundsChecker = boundsChecker;
		this.internalFullMap = internalFullMap;
		this.internalMyHalfMap = internalMyHalfMap;
		this.internalEnemyHalfMap = internalEnemyHalfMap;
	}
	
	public void exchangeMaps(NetworkClient networkClient) throws GetException, PostException, PlayerNotFoundException, JAXBException
	{
		// before exchangeMaps() has been called, networkClient.updateGameState()
		// has been called
		// so internalFullMap might or might not contain the enemy's halfmap
		if (internalFullMap.getMapData().isEmpty())
		{
			// i'm the first one that sends a map to the server
			logger.info("I am the first one to send my map to the server");
			InternalHalfMapValidatorSolo internalHalfMapValidatorSolo = new InternalHalfMapValidatorSolo();
			List<String> errorMessages = new ArrayList<String>();
			do
			{
				internalMyHalfMap.generateInternalHalfMap();
				errorMessages = internalHalfMapValidatorSolo.validateHalfMap(internalMyHalfMap);
				if (errorMessages.isEmpty() == false)
				{
					for (String eachErrorMessage : errorMessages)
					{
						logger.warn(eachErrorMessage);
					}
					logger.warn("\n" + internalMyHalfMap.toString());
				}
			}
			while (errorMessages.isEmpty() == false);
			
		}
		else
		{
			// i'm the second one that sends a map to the server
			logger.info("I am the second one to send my map to the server");
			
			// now that we know that the enemy's halfmap is in the fullmap
			// we need to update the bounds
			boundsChecker.updateBounds(this.internalFullMap);
			
			// extract the enemy's halfmap
			HalfMapExtractor halfMapExtractor = new HalfMapExtractor();
			this.internalEnemyHalfMap.setMapData(halfMapExtractor.extractEnemyHalfMap(internalFullMap, internalMyHalfMap));
			
			InternalHalfMapValidatorSolo internalHalfMapValidatorSolo = new InternalHalfMapValidatorSolo();
			InternalHalfMapValidatorDuo internalHalfMapValidatorDuo = new InternalHalfMapValidatorDuo();
			List<String> errorMessages = new ArrayList<String>();
			do
			{
				internalMyHalfMap.generateInternalHalfMap();
				errorMessages = internalHalfMapValidatorSolo.validateHalfMap(internalMyHalfMap);
				errorMessages.addAll(internalHalfMapValidatorDuo.validateHalfMapBasedOnEnemyMap(boundsChecker, internalEnemyHalfMap, internalMyHalfMap));
				if (errorMessages.isEmpty() == false)
				{
					for (String eachErrorMessage : errorMessages)
					{
						logger.warn(eachErrorMessage);
					}
					logger.warn("\n" + internalMyHalfMap.toString());
				}
			}
			while (errorMessages.isEmpty() == false);
			
		}
		
		logger.info("HalfMap which I will send to the server:\n" + internalMyHalfMap.toString());
		InternalHalfMapView internalHalfMapView = new InternalHalfMapView();
		internalHalfMapView.printInternalHalfMap(internalMyHalfMap);
		networkClient.sendInternalHalfMap(internalMyHalfMap);
		networkClient.updateGameState(this.internalFullMap, 0); // 0 is passed here as roundCounter cuz it doesnt really matter in this case
	}
	
	public void prepare()
	{
		
		boundsChecker.updateBounds(internalFullMap);
		internalMyHalfMap.updateCoordinates(boundsChecker);
		
		// if i went first, the enemy's halfmap would net get updated
		// but i dont think that this is needed ngl...
		if (internalEnemyHalfMap.getMapData().size() == 0)
		{
			HalfMapExtractor halfMapExtractor = new HalfMapExtractor();
			internalEnemyHalfMap.setMapData(halfMapExtractor.extractEnemyHalfMap(internalFullMap, internalMyHalfMap));
		}
		
	}
	
	public void extractPossibleLocationsEnemyCastle()
	{
		PossibleEnemyCastleLocationsExtractor possibleEnemyCastleLocationsExtractor = new PossibleEnemyCastleLocationsExtractor();
		this.possibleEnemyCastleLocations = possibleEnemyCastleLocationsExtractor.extractPossibleEnemyCastleLocations(internalFullMap, internalEnemyHalfMap);
		
	}
	
	public List<Direction> getMoves(int roundCounter)
	{
		ArrayList<Direction> result = new ArrayList<Direction>();
		
		MoveGenerator moveGenerator = new MoveGenerator();
		
		if (internalFullMap.getEntities().getMyPlayer().ifTreasureCollected() == true)
		{
			if (boundsChecker.checkMyHalfMapBounds(internalFullMap.getEntities().getMyPlayer().getLocation()))
			{
				// make ur way to the enemy's half
				result.addAll(moveGenerator.getNextMoves(boundsChecker, internalFullMap, possibleEnemyCastleLocations, roundCounter));
			}
			else // player is already on enemy's half
			{
				result.addAll(moveGenerator.getNextMoves(boundsChecker, internalEnemyHalfMap, possibleEnemyCastleLocations, roundCounter));
			}
		}
		else // player has not yet collected the treasure
		{
			result.addAll(moveGenerator.getNextMoves(boundsChecker, internalMyHalfMap, possibleEnemyCastleLocations, roundCounter));
			
		}
		
		logger.info("Moves which i want to perform:" + result);
		
		return result;
	}
	
	public InternalFullMap getInternalFullMap()
	{
		return internalFullMap;
	}
	
	public InternalHalfMap getInternalMyHalfMap()
	{
		return internalMyHalfMap;
	}
	
	public InternalHalfMap getInternalEnemyHalfMap()
	{
		return internalEnemyHalfMap;
	}
	
}
