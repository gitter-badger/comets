package edu.bu.segrelab.comets.test.unit.fba;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.bu.segrelab.comets.Comets;
import edu.bu.segrelab.comets.exception.LayoutFileException;
import edu.bu.segrelab.comets.fba.FBACometsLoader;
import edu.bu.segrelab.comets.fba.FBAParameters;

public class TestFBACometsLoader{

	SubFBACometsLoader loader;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		loader = new SubFBACometsLoader();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	//TODO: test adding the forward reaction rate as 
	//the 4th argument for a reactant in a simple reaction
	public void testParseReactionsBlock(){
		String[] lines = 
			{"REACTANTS .05",
				"1 1 2",
				"1 2 ",
				"2 1 ",
				"3 2 .1",
				"ENZYMES 2",
				"2 3 ",
				"3 4 5",
				"PRODUCTS",
				"1 5",
				"2 6 1",
				"3 6 2"};
		double[][] expectParams = {
				{2.0, 1.0, 0.0, 0.0, 0.0, 0.0},
				{0.05, 0.0, 0.0, 0.0, 0.0, 0.0},
				{0.0, 0.1, 0.0, 0.0, 0.0, 0.0}};
		double[][] expectKcats = {
				{0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
				{0.0, 0.0, 2.0, 0.0, 0.0, 0.0},
				{0.0, 0.0, 0.0, 5.0, 0.0, 0.0}};
		double[][] expectStoich = {
				{-2.0, -1.0, 0.0, 0.0, 1.0, 0.0},
				{-1.0, 0.0, 0.0, 0.0, 0.0, 1.0},
				{0.0, -1.0, 0.0, 0.0, 0.0, 2.0}};
		
		try {loader.executeParseReactionsBlock(Arrays.asList(lines));}
		catch (NumberFormatException e) {	e.printStackTrace();}
		catch (LayoutFileException e) { e.printStackTrace();}
		
		assertArrayEquals(expectKcats,loader.getExRxnKcats());
		assertArrayEquals(expectStoich,loader.getExRxnStoich());
		assertArrayEquals(expectParams,loader.getExRxnParams());
		
		int x =1;//this line is just here to hold a breakpoint
	}
	
	//private subclass to expose protected methods
	private class SubFBACometsLoader extends FBACometsLoader{	
		public SubFBACometsLoader(){
			super();
			this.pParams = new FBAParameters((Comets) null);
		}
		protected final void executeParseReactionsBlock(List<String> lines) throws LayoutFileException, NumberFormatException{
			parseReactionsBlock(lines);
		}
		protected final double[][] getExRxnStoich() {return exRxnStoich;}
		protected final double[][] getExRxnKcats() {return exRxnKcats;}
		protected final double[][] getExRxnParams() {return exRxnParams;}
	}

}
