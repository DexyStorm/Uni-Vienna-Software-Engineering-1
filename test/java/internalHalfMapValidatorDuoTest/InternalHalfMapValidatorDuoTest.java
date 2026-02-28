package internalHalfMapValidatorDuoTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import data.map.BoundsChecker;
import data.map.fullMap.InternalFullMap;
import data.map.halfMap.InternalHalfMap;
import data.map.halfMap.validation.InternalHalfMapValidatorDuo;
import enums.Direction;
import helpers.MapCreator;

public class InternalHalfMapValidatorDuoTest
{
	InternalHalfMapValidatorDuo internalHalfMapValidatorDuo = new InternalHalfMapValidatorDuo();
	MapCreator mapCreator = new MapCreator();
	
	private void prepareBoundsChecker(BoundsChecker boundsChecker, InternalHalfMap internalMyHalfMap, InternalHalfMap internalEnemyHalfMap)
	{
		InternalFullMap internalFullMap = new InternalFullMap(internalMyHalfMap.getEntities(), internalEnemyHalfMap.getMapData());
		
		boundsChecker.updateBounds(internalFullMap);
	}
	
	@Test
	void InternalHalfMapValidatorDuovalidateHalfMapBasedOnEnemyMap_EnemyMapRight_OnlyGrass_ShouldBeValid()
	{
		BoundsChecker boundsChecker = new BoundsChecker();
		
		String[][] nodeArrayInternalMyHalfMap =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } };
		
		String[][] nodeArrayInternalEnemyHalfMap =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } };
		
		InternalHalfMap internalMyHalfMap = mapCreator.setUpHalfMap(nodeArrayInternalMyHalfMap);
		InternalHalfMap internalEnemyHalfMap = mapCreator.setUpHalfMap(nodeArrayInternalEnemyHalfMap);
		mapCreator.moveHalfMap(Direction.Right, internalEnemyHalfMap);
		prepareBoundsChecker(boundsChecker, internalMyHalfMap, internalEnemyHalfMap);
		
		List<String> result = internalHalfMapValidatorDuo.validateHalfMapBasedOnEnemyMap(boundsChecker, internalEnemyHalfMap, internalMyHalfMap);
		assertTrue(result.isEmpty());
	}
	
	@Test
	void InternalHalfMapValidatorDuovalidateHalfMapBasedOnEnemyMap_EnemyMapRight_ShouldBeValid()
	{
		BoundsChecker boundsChecker = new BoundsChecker();
		
		String[][] nodeArrayInternalMyHalfMap =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "w" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "w" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } };
		
		String[][] nodeArrayInternalEnemyHalfMap =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "g" } };
		
		InternalHalfMap internalMyHalfMap = mapCreator.setUpHalfMap(nodeArrayInternalMyHalfMap);
		InternalHalfMap internalEnemyHalfMap = mapCreator.setUpHalfMap(nodeArrayInternalEnemyHalfMap);
		mapCreator.moveHalfMap(Direction.Right, internalEnemyHalfMap);
		prepareBoundsChecker(boundsChecker, internalMyHalfMap, internalEnemyHalfMap);
		
		List<String> result = internalHalfMapValidatorDuo.validateHalfMapBasedOnEnemyMap(boundsChecker, internalEnemyHalfMap, internalMyHalfMap);
		assertTrue(result.isEmpty());
	}
	
	@Test
	void InternalHalfMapValidatorDuovalidateHalfMapBasedOnEnemyMap_EnemyMapRight_ShouldNotBeValid()
	{
		BoundsChecker boundsChecker = new BoundsChecker();
		
		String[][] nodeArrayInternalMyHalfMap =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "w" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "w" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } };
		
		String[][] nodeArrayInternalEnemyHalfMap =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "g" } };
		
		InternalHalfMap internalMyHalfMap = mapCreator.setUpHalfMap(nodeArrayInternalMyHalfMap);
		InternalHalfMap internalEnemyHalfMap = mapCreator.setUpHalfMap(nodeArrayInternalEnemyHalfMap);
		mapCreator.moveHalfMap(Direction.Right, internalEnemyHalfMap);
		prepareBoundsChecker(boundsChecker, internalMyHalfMap, internalEnemyHalfMap);
		
		List<String> result = internalHalfMapValidatorDuo.validateHalfMapBasedOnEnemyMap(boundsChecker, internalEnemyHalfMap, internalMyHalfMap);
		assertFalse(result.isEmpty());
	}
	
	@Test
	void InternalHalfMapValidatorDuovalidateHalfMapBasedOnEnemyMap_EnemyMapDown_ShouldBeValid()
	{
		BoundsChecker boundsChecker = new BoundsChecker();
		
		String[][] nodeArrayInternalMyHalfMap =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "w", "w", "w", "w", "w", "g", "g", "w", "g" } };
		
		String[][] nodeArrayInternalEnemyHalfMap =
		{
				{ "g", "w", "w", "w", "w", "w", "g", "g", "w", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } };
		
		InternalHalfMap internalMyHalfMap = mapCreator.setUpHalfMap(nodeArrayInternalMyHalfMap);
		InternalHalfMap internalEnemyHalfMap = mapCreator.setUpHalfMap(nodeArrayInternalEnemyHalfMap);
		mapCreator.moveHalfMap(Direction.Down, internalEnemyHalfMap);
		prepareBoundsChecker(boundsChecker, internalMyHalfMap, internalEnemyHalfMap);
		
		List<String> result = internalHalfMapValidatorDuo.validateHalfMapBasedOnEnemyMap(boundsChecker, internalEnemyHalfMap, internalMyHalfMap);
		assertTrue(result.isEmpty());
	}
	
	@Test
	void InternalHalfMapValidatorDuovalidateHalfMapBasedOnEnemyMap_EnemyMapDown_ShouldNotBeValid()
	{
		BoundsChecker boundsChecker = new BoundsChecker();
		
		String[][] nodeArrayInternalMyHalfMap =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "w", "w", "w", "w", "g", "g", "g", "g", "g", "g" } };
		
		String[][] nodeArrayInternalEnemyHalfMap =
		{
				{ "g", "g", "g", "g", "g", "g", "w", "w", "w", "w" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } };
		
		InternalHalfMap internalMyHalfMap = mapCreator.setUpHalfMap(nodeArrayInternalMyHalfMap);
		InternalHalfMap internalEnemyHalfMap = mapCreator.setUpHalfMap(nodeArrayInternalEnemyHalfMap);
		mapCreator.moveHalfMap(Direction.Down, internalEnemyHalfMap);
		prepareBoundsChecker(boundsChecker, internalMyHalfMap, internalEnemyHalfMap);
		
		List<String> result = internalHalfMapValidatorDuo.validateHalfMapBasedOnEnemyMap(boundsChecker, internalEnemyHalfMap, internalMyHalfMap);
		assertFalse(result.isEmpty());
	}
	
	@Test
	void InternalHalfMapValidatorDuovalidateHalfMapBasedOnEnemyMap_EnemyMapTopLeft_ShouldBeValid()
	{
		BoundsChecker boundsChecker = new BoundsChecker();
		
		String[][] nodeArrayInternalEnemyHalfMap =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "w" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "w", "w", "w", "w", "g", "g", "g", "g", "g", "w" } };
		
		String[][] nodeArrayInternalMyHalfMap =
		{
				{ "g", "g", "g", "g", "g", "w", "g", "g", "g", "g" },
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "g" } };
		
		InternalHalfMap internalMyHalfMap = mapCreator.setUpHalfMap(nodeArrayInternalMyHalfMap);
		InternalHalfMap internalEnemyHalfMap = mapCreator.setUpHalfMap(nodeArrayInternalEnemyHalfMap);
		// mapCreator.moveHalfMap(Direction.Right, internalEnemyHalfMap); //not needed
		// cuz enemy map should be TopLeft
		prepareBoundsChecker(boundsChecker, internalMyHalfMap, internalEnemyHalfMap);
		
		List<String> result = internalHalfMapValidatorDuo.validateHalfMapBasedOnEnemyMap(boundsChecker, internalEnemyHalfMap, internalMyHalfMap);
		assertTrue(result.isEmpty());
	}
	
	@Test
	void InternalHalfMapValidatorDuovalidateHalfMapBasedOnEnemyMap_EnemyMapTopLeft_ShouldNotBeValid()
	{
		BoundsChecker boundsChecker = new BoundsChecker();
		
		String[][] nodeArrayInternalEnemyHalfMap =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "w" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "w", "w", "w", "w", "g", "g", "g", "w", "g", "w" } };
		
		String[][] nodeArrayInternalMyHalfMap =
		{
				{ "g", "g", "g", "g", "g", "w", "g", "g", "g", "g" },
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "g" } };
		
		InternalHalfMap internalMyHalfMap = mapCreator.setUpHalfMap(nodeArrayInternalMyHalfMap);
		InternalHalfMap internalEnemyHalfMap = mapCreator.setUpHalfMap(nodeArrayInternalEnemyHalfMap);
		// mapCreator.moveHalfMap(Direction.Right, internalEnemyHalfMap); //not needed
		// cuz enemy map should be TopLeft
		prepareBoundsChecker(boundsChecker, internalMyHalfMap, internalEnemyHalfMap);
		
		List<String> result = internalHalfMapValidatorDuo.validateHalfMapBasedOnEnemyMap(boundsChecker, internalEnemyHalfMap, internalMyHalfMap);
		assertFalse(result.isEmpty());
	}
	
}
