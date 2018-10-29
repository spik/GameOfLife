package modellering;

import java.util.Random;

public class Population {
	
	//create a 2D array for the population, NxN matrix
	public Person[][] population;
	
	/**
     * Creates a matrix and fills all the positions with people
     * @param rows Number of rows in the matrix, decides population size together with columns
   	 * @param columns Number of columns in the matrix, decides population size together with rows
     * @param minDay The minimum number of days a person can be sick
     * @param maxDay The maximum number if days a person can be sick
	 * @param random Randomly generated number
	 */
	public Population(int rows, int columns, int minDay, int maxDay, Random random){
		
		population = new Person[rows][columns];

		for (int i = 0; i < rows; i++) {
			
			for (int j = 0; j < columns; j++) {			
				
				//creates a person for each location in the matrix with a minimal and maximum number of sick days
				population[i][j] = new Person(minDay, maxDay, random);
			}		
		}
	}

	/**
 	 * Decides the position of the initial person to get sick
	 * @param rowNumber The row number of the first person to get sick
	 * @param ColumnNumber The column number of the first person to get sick
	 */
	public void setInitialSickPerson(int rowNumber, int columnNumber){
		
		//set the persons condition to infected
		population[rowNumber][columnNumber].setCondition("Infected", true);
		//sets the persons infection day to the first day (day 1)
		population[rowNumber][columnNumber].setInfectionDay(1);
	}
	
	@Override
	public String toString(){
		
		String string = "";
		for (int i = 0; i < population.length; i++) {
			string += "[";
			for (int j = 0; j < population.length; j++) {
				string += "[" + population[i][j].toString() +"]";
			}
			string += "]\n";
		}
		return string;
	}

}
