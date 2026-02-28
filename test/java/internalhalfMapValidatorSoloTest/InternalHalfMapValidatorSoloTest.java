package internalhalfMapValidatorSoloTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import data.map.Coordinate;
import data.map.halfMap.InternalHalfMap;
import data.map.halfMap.validation.InternalHalfMapValidatorSolo;
import helpers.MapCreator;

public class InternalHalfMapValidatorSoloTest
{
	InternalHalfMapValidatorSolo internalHalfMapValidatorSolo = new InternalHalfMapValidatorSolo();
	MapCreator mapCreator = new MapCreator();
	
	@Test
	void InternalHalfMapValidatorSoloCeckIfCorrectAmountIsPresent_CorrectAmountIsPresent() throws Exception
	{
		Coordinate defaultCoordinate = new Coordinate(3, 3);
		String[][] nodeArray =
		{
				{ "w", "w", "w", "w", "w", "w", "w", "m", "m", "m" },
				{ "m", "m", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } };
		
		InternalHalfMap internalHalfMap = mapCreator.setUpHalfMap(nodeArray);
		internalHalfMap.getEntities().setMyCastle(defaultCoordinate);
		internalHalfMap.getEntities().getMyPlayer().setLocation(defaultCoordinate);
		
		Method method = InternalHalfMapValidatorSolo.class.getDeclaredMethod("checkIfCorrectAmountIsPresent", InternalHalfMap.class);
		method.setAccessible(true);
		
		System.out.println(internalHalfMap);
		
		boolean result = (boolean) method.invoke(internalHalfMapValidatorSolo, internalHalfMap);
		assertTrue(result);
	}
	
	@Test
	void InternalHalfMapValidatorSoloCeckIfCorrectAmountIsPresent_TooLittleWater() throws Exception
	{
		String[][] nodeArray =
		{
				{ "w", "w", "w", "w", "w", "w", "g", "m", "m", "m" },
				{ "m", "m", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } };
		
		InternalHalfMap internalHalfMap = mapCreator.setUpHalfMap(nodeArray);
		internalHalfMap.getEntities().setMyCastle(new Coordinate(3, 3));
		
		Method method = InternalHalfMapValidatorSolo.class.getDeclaredMethod("checkIfCorrectAmountIsPresent", InternalHalfMap.class);
		method.setAccessible(true);
		
		boolean result = (boolean) method.invoke(internalHalfMapValidatorSolo, internalHalfMap);
		assertFalse(result);
	}
	
	@Test
	void InternalHalfMapValidatorSoloCeckIfCorrectAmountIsPresent_TooLittleMountain() throws Exception
	{
		String[][] nodeArray =
		{
				{ "w", "w", "w", "w", "w", "w", "w", "g", "m", "m" },
				{ "m", "m", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } };
		
		InternalHalfMap internalHalfMap = mapCreator.setUpHalfMap(nodeArray);
		internalHalfMap.getEntities().setMyCastle(new Coordinate(3, 3));
		
		Method method = InternalHalfMapValidatorSolo.class.getDeclaredMethod("checkIfCorrectAmountIsPresent", InternalHalfMap.class);
		method.setAccessible(true);
		
		boolean result = (boolean) method.invoke(internalHalfMapValidatorSolo, internalHalfMap);
		assertFalse(result);
	}
	
	@Test
	void InternalHalfMapValidatorSoloCeckIfEdgesAreAccessibleNoCornerWaterTest_ShouldBeAccessible() throws Exception
	{
		String[][] nodeArray =
		{
				{ "g", "w", "g", "g", "g", "g", "w", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "w" },
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "w", "g", "g", "g", "g", "w", "g", "g", "g" } };
		
		InternalHalfMap internalHalfMap = mapCreator.setUpHalfMap(nodeArray);
		
		Method method = InternalHalfMapValidatorSolo.class.getDeclaredMethod("checkIfEdgeAccessabilityIsCorrect", InternalHalfMap.class);
		method.setAccessible(true);
		
		boolean result = (boolean) method.invoke(internalHalfMapValidatorSolo, internalHalfMap);
		assertTrue(result);
	}
	
	@Test
	void InternalHalfMapValidatorSoloCeckIfEdgesAreAccessibleWaterCornerTest_ShouldBeAccessible() throws Exception
	{
		String[][] nodeArray =
		{
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "w" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "w" } };
		
		InternalHalfMap internalHalfMap = mapCreator.setUpHalfMap(nodeArray);
		
		Method method = InternalHalfMapValidatorSolo.class.getDeclaredMethod("checkIfEdgeAccessabilityIsCorrect", InternalHalfMap.class);
		method.setAccessible(true);
		
		boolean result = (boolean) method.invoke(internalHalfMapValidatorSolo, internalHalfMap);
		assertTrue(result);
	}
	
	@Test
	void InternalHalfMapValidatorSoloCeckIfEdgesAreAccessibleTest_ShouldNotBeAccessible() throws Exception
	{
		String[][] nodeArray =
		{
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "w" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "w", "g", "g", "g", "g", "g", "g", "g", "g", "w" } };
		
		InternalHalfMap internalHalfMap = mapCreator.setUpHalfMap(nodeArray);
		
		Method method = InternalHalfMapValidatorSolo.class.getDeclaredMethod("checkIfEdgeAccessabilityIsCorrect", InternalHalfMap.class);
		method.setAccessible(true);
		
		boolean result = (boolean) method.invoke(internalHalfMapValidatorSolo, internalHalfMap);
		assertFalse(result);
	}
	
	@Test
	void InternalHalfMapValidatorSoloCheckIfIslandsCornersExist_ShouldExist() throws Exception
	{
		String[][] nodeArray =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "w", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "w" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } };
		
		InternalHalfMap internalHalfMap = mapCreator.setUpHalfMap(nodeArray);
		
		Method method = InternalHalfMapValidatorSolo.class.getDeclaredMethod("checkIfIslandsExist", InternalHalfMap.class);
		method.setAccessible(true);
		
		boolean result = (boolean) method.invoke(internalHalfMapValidatorSolo, internalHalfMap);
		assertTrue(result);
	}
	
	@Test
	void InternalHalfMapValidatorSoloCheckIfIslandsExist_ShouldNotExist() throws Exception
	{
		String[][] nodeArray =
		{
				{ "g", "g", "g", "g", "g", "w", "g", "g", "g", "g" },
				{ "g", "w", "w", "w", "g", "w", "g", "g", "w", "g" },
				{ "g", "g", "g", "w", "w", "w", "g", "w", "g", "g" },
				{ "g", "w", "w", "w", "g", "g", "g", "w", "w", "w" },
				{ "g", "g", "g", "g", "g", "w", "g", "g", "w", "w" } };
		
		InternalHalfMap internalHalfMap = mapCreator.setUpHalfMap(nodeArray);
		
		Method method = InternalHalfMapValidatorSolo.class.getDeclaredMethod("checkIfIslandsExist", InternalHalfMap.class);
		method.setAccessible(true);
		
		boolean result = (boolean) method.invoke(internalHalfMapValidatorSolo, internalHalfMap);
		assertFalse(result);
	}
	
	@Test
	void InternalHalfMapValidatorSoloCheckIfIslands_ShouldExist() throws Exception
	{
		String[][] nodeArray =
		{
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "w", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "w", "g", "w", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "w", "g", "g", "g" },
				{ "g", "g", "g", "g", "g", "g", "g", "g", "g", "g" } };
		
		InternalHalfMap internalHalfMap = mapCreator.setUpHalfMap(nodeArray);
		
		Method method = InternalHalfMapValidatorSolo.class.getDeclaredMethod("checkIfIslandsExist", InternalHalfMap.class);
		method.setAccessible(true);
		
		boolean result = (boolean) method.invoke(internalHalfMapValidatorSolo, internalHalfMap);
		assertTrue(result);
	}
	
}
