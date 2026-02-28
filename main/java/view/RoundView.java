package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.entities.Entities;
import data.map.Coordinate;
import data.map.InternalNodeData;
import data.map.fullMap.InternalFullMap;
import enums.GameStatus;
import enums.Terrain;
import observerInterfaces.MapObserver;

public class RoundView implements MapObserver
{
	private final static Logger logger = LoggerFactory.getLogger(RoundView.class);
	
	private void printRound(InternalFullMap internalFullMap, int roundCounter)
	{
		StringBuilder output = new StringBuilder();
		output.append("======================================\n");
		output.append(printRoundNumber(roundCounter));
		output.append(getOutputForPlayers(internalFullMap.getEntities()));
//		output.append("\n");
		output.append("---------------------------------------\n");
		output.append(getOutputForFullMap(internalFullMap));
		output.append("======================================");
		
		System.out.println(output.toString());
		
	}
	
	private StringBuilder printRoundNumber(int roundCounter)
	{
		StringBuilder output = new StringBuilder();
		
		output.append("Round: ");
		output.append(roundCounter);
		output.append("/160");
		output.append("\n");
		
		return output;
	}
	
	private StringBuilder getOutputForPlayers(Entities entities)
	{
		StringBuilder output = new StringBuilder();
		
		output.append("My player has collected treasure: ");
		if (entities.getMyPlayer().ifTreasureCollected())
		{
			output.append("✅");
		}
		else
		{
			output.append("❌");
		}
		output.append("\n");
		
		output.append("Enemy player has collected treasure: ");
		if (entities.getEnemyPlayer().ifTreasureCollected())
		{
			output.append("✅");
		}
		else
		{
			output.append("❌");
		}
		output.append("\n");
		
		return output;
	}
	
	private StringBuilder addExtraNodeOutputInfo(Coordinate coordinate, Entities entities, String s)
	{
		StringBuilder output = new StringBuilder();
		if (coordinate.equals(entities.getEnemyPlayer().getLocation()))
		{
			if (coordinate.equals(entities.getMyPlayer().getLocation()))
			{
				output.append(" 💏 ");
			}
			else
			{
				output.append(" 🥷 ");
			}
		}
		else if (coordinate.equals(entities.getMyPlayer().getLocation()))
		{
			output.append(" 🦸 ");
		}
		else if (coordinate.equals(entities.getMyCastle()))
		{
			output.append(" 🏰 ");
		}
		else if (coordinate.equals(entities.getEnemyCastle()))
		{
			output.append(" 🏯 ");
		}
		else if (coordinate.equals(entities.getTreasure()))
		{
			output.append(" 💎 ");
		}
		
		else
		{
			output.append(s + s);
		}
		
		output.append(" ");
		return output;
		
	}
	
	private StringBuilder getOutputForFullMap(InternalFullMap internalFullMap)
	{
		
		Set<Coordinate> alreadyVisited = internalFullMap.getAlreadyVisited();
		
		int maxX = 0;
		int maxY = 0;
		
		for (Entry<Coordinate, InternalNodeData> eachNode : internalFullMap.getMapData().entrySet())
		{
			if (eachNode.getKey().getX() > maxX)
			{
				maxX = eachNode.getKey().getX();
			}
			if (eachNode.getKey().getY() > maxY)
			{
				maxY = eachNode.getKey().getY();
			}
		}
		
		StringBuilder output = new StringBuilder();
		if (maxX == 19)
		{
			output.append("   0    1    2    3    4    5    6    7    8    9   10   11   12   13   14   15   16   17   18   19\n");
		}
		else
		{
			output.append("   0    1    2    3    4    5    6    7    8    9\n");
		}
		
		List<Coordinate> orderedTupleList = new ArrayList<Coordinate>();
		
		int rowCounter = 0;
		
		// first all of the nodes of a row are saved in tupleList
		for (int y = 0; y <= maxY; ++y)
		{
			StringBuilder letterCopyer = new StringBuilder();
			for (int x = 0; x <= maxX; ++x)
			{
				
				orderedTupleList.add(new Coordinate(x, y));
				
			}
			
			// then the information from those nodes are put
			// into the StringBuilder output line by line
			// the first line is always the exact same as the third line
			// for example:
			// GGGG
			// GEPG
			// GGGG
			// so that's why the letterCopyer will always copy
			// what the output gets and will "paste" it back
			// into the output for the third line
			for (int i = 1; i <= 3; ++i)
			{
				if (i == 1)
				{
					output.append("  ");
					for (Coordinate eachCoordinate : orderedTupleList)
					{
						
						if (internalFullMap.getMapData().get(eachCoordinate).getTerrain() == Terrain.Water)
						{
							output.append("🌊🌊 ");
							letterCopyer.append("🌊🌊 ");
							
						}
						if (internalFullMap.getMapData().get(eachCoordinate).getTerrain() == Terrain.Mountain)
						{
							if (alreadyVisited.contains(eachCoordinate))
							{
								output.append("V🏔️V ");
								letterCopyer.append("V🏔️V ");
							}
							else
							{
								output.append("🏔️🏔️ ");
								letterCopyer.append("🏔️🏔️ ");
							}
						}
						// maybe just else here instead?
						if (internalFullMap.getMapData().get(eachCoordinate).getTerrain() == Terrain.Grass)
						{
							if (alreadyVisited.contains(eachCoordinate))
							{
								output.append("V🌿V ");
								letterCopyer.append("V🌿V ");
							}
							else if (internalFullMap.getMapData().get(eachCoordinate).equals(internalFullMap.getEntities().getMyCastle()))
							{
								output.append("V🌿V ");
								letterCopyer.append("V🌿V ");
							}
							else
							{
								output.append("🌿🌿 ");
								letterCopyer.append("🌿🌿 ");
								
							}
							
						}
					}
					
					output.append("\n");
				}
				
				// this is now for the middle line
				// GGGG
				// GEPG <-
				// GGGG
				else if (i == 2)
				{
					output.append(rowCounter + " ");
					rowCounter = rowCounter + 1;
					for (Coordinate eachCoordinate : orderedTupleList)
					{
						if (internalFullMap.getMapData().get(eachCoordinate).getTerrain() == Terrain.Water)
						{
							// dont need addExtraNodeOutputInfo
							// cuz only enemy player can be on water
							if ((eachCoordinate).equals(internalFullMap.getEntities().getEnemyPlayer().getLocation()) == true)
							{
								output.append("W🥷W ");
							}
							else
							{
								output.append("🌊🌊 ");
							}
						}
						else if (internalFullMap.getMapData().get(eachCoordinate).getTerrain() == Terrain.Mountain)
						{
							output.append(addExtraNodeOutputInfo(eachCoordinate, internalFullMap.getEntities(), "🏔️"));
						}
						// maybe just else here instead?
						else if (internalFullMap.getMapData().get(eachCoordinate).getTerrain() == Terrain.Grass)
						{
							output.append(addExtraNodeOutputInfo(eachCoordinate, internalFullMap.getEntities(), "🌿"));
							
						}
					}
					output.append("\n");
				}
				// this is now for the last line
				// GGGG
				// GEPG
				// GGGG <-
				else
				{
					output.append("  ");
					output.append(letterCopyer);
					output.append("\n");
				}
			}
			
			if (y != maxY)
			{
				output.append("\n");
			}
			
			orderedTupleList.clear();
		}
		
		return output;
		
	}
	
	public void printGameOver(GameStatus gameStatus)
	{
		if (gameStatus == GameStatus.Won)
		{
			System.out.println("You have WON! 🤩💯🎉");
		}
		else
		{
			System.out.println("You have LOST! 😭💀🥀");
		}
		
	}
	
	@Override
	public void updateMapObserver(InternalFullMap internalFullMap, int roundCounter)
	{
		// the internalFullMap should only get printed AFTER
		// internalFullMap.updateMapSize() has been called.
		// (this happens in controller.startGame().gameData.prepare()).
		if (internalFullMap.getMapData().size() == 100)
		{
			printRound(internalFullMap, roundCounter);
			
		}
		
	}
	
}
