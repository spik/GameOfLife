package modellering;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Simulation {

	static int populationSize;
	static double infectionRisk;
	static int minDay;
	static int maxDay;
	static int initSick = 1;
	static int initSickX;
	static int initSickY;
	static double deathRisk;

	public static int simulationDay = 1;		//day of simulation
	public static int numOfInfected = 0;		//number of infected people each day
	public static int numOfImmune = 0;			//number of immune persons each day
	public static int numOfDead = 0;			//number of dead persons each day
	public static int totNumOfDead = 0;			//total number of dead persons
	public static int totNumOfInfected = 1;		//total number of infected persons
	public static int totNumOfImmune = 0;		//total number of infected persons
	public static int currentNumOfInfected = 1;		//current number of infected people

	public static void main(String[] args) throws FileNotFoundException {

//		Random random = new Random();
//
		inputData();
//
//		Population population = new Population(populationSize, populationSize, minDay, maxDay, random);
//		population.setInitialSickPerson(initSickX, initSickY);
//
//		simulation(populationSize, infectionRisk, minDay, maxDay, deathRisk, initSick, population, random);
		seeds();
	}

	/**
	 * Gets the user input for the simulation
	 */
	public static void inputData(){

		Scanner scanner = new Scanner(System.in);

		System.out.println("Size of population: ");
		populationSize = scanner.nextInt();

		System.out.println("Infection possibility: ");
		infectionRisk = scanner.nextDouble();

		System.out.println("Minimum number of sickdays: ");
		minDay = scanner.nextInt();

		System.out.println("Maximum number of sickdays: ");
		maxDay = scanner.nextInt();

		System.out.println("Risk of dying: ");
		deathRisk = scanner.nextDouble();

		System.out.println("X coordinate for initial sick: ");
		initSickX = scanner.nextInt();

		System.out.println("Y coordinate for initial sick: ");
		initSickY = scanner.nextInt();

	}

	/**
	 * Reads the seed numbers from the "seeds.txt" file and runs the simulation with each of this numbers
	 */
	public static void seeds() throws FileNotFoundException{

		//reads the seeds file that contains a hundred random seed numbers
		Scanner file = new Scanner(new File("seeds.txt")).useDelimiter("\\s+");

		//creates an array list for the seeds
		ArrayList<String> seedList = new ArrayList<String>();

		while (file.hasNext()){
			//puts all the numbers from the file into the array
			String currentSeed = file.next();
			seedList.add(currentSeed);
		}
		file.close();

		String[] seedArray = seedList.toArray(new String[0]);

		for (int j = 0; j < seedArray.length; j++) {

			System.out.println("Run with seednumber " + j);
			Random random = new Random(Integer.parseInt(seedArray[j]));
			Population population = new Population (populationSize, populationSize, minDay, maxDay, random);
			population.setInitialSickPerson(initSickX, initSickY);
			currentNumOfInfected = 1; 
			simulation(populationSize, infectionRisk, minDay, maxDay, deathRisk, initSick, population, random);
			System.out.println("");
			System.out.println("");
			System.out.println("");
		}
	}

	/**
	 * Decides the fate of every person in the population
	 * @param sizeOfPop The number of rows and columns the population should consist of
	 * @param infectionRisk The risk of a person getting infected. Given as a double between 0 and 1
	 * @param minDays The minimum number of days a person can be sick
	 * @param maxDays The maximum number of days a person can be sick
	 * @param riskOfDying The risk of a person dying. Given as a double between 0 and 1
	 * @param initInfected The initial number of infected people
	 * @param population The population for which we want to run the simulation
	 * @param random Randomly generated number
	 */
	public static void simulation(int sizeOfPop, double infectionRisk, int minDays, int maxDays, double riskOfDying, 
			int initInfected, Population population, Random random)
	{
		numOfInfected = initInfected;

		//the simulation is run as long as there exist infected people in the population
		while(currentNumOfInfected > 0){

			//on the first day, set the number of sick people to initially infected
			if(simulationDay == 1)
				numOfInfected = initInfected;
			
			//if we're not on the first day these variables needs to be reset to print out the data for each day
			else {
				numOfInfected = 0;
				numOfDead = 0;
				numOfImmune = 0;
			}
			//Loops through the population
			for (int i = 0; i < sizeOfPop; i++) {
				for (int j = 0; j < sizeOfPop; j++) { 

					//Check if a person is infected
					if(population.population[i][j].getCondition() == "Person is infected"){


						int infectionDay = population.population[i][j].getInfectionDay();
						
						//If a person got infected on the same day as the simulation day...
						if(infectionDay == simulationDay){
							//...decide whether or not this person will die
							deathDecider(i, j, random, riskOfDying, population);
						}
						else {					
							//If the person didn't get sick on the simulation day, it can infect others
							infectOthers(i, j, random, infectionRisk, population, sizeOfPop);
							deathDecider(i, j, random, riskOfDying, population);
						}
						//If a person has become healthy again...
						if(simulationDay - population.population[i][j].getInfectionDay() == population.population[i][j].getsickDays() 
								&& population.population[i][j].getCondition() == "Person is infected"){

							//...the person becomes immune
							population.population[i][j].setCondition("Immune", true);

							//numOfInfected--;
							numOfImmune++;
							totNumOfImmune++;
							currentNumOfInfected--;
						}

					}
				}			
			}
			dailyStatus(simulationDay);
			simulationDay++;
		}
	}

	/**
	 * Determines whether or not a person will die
	 * @param currentRowNum The row number of the person for which we will decide life or death
	 * @param currentColNum The column number of the person for which we will decide life or death
	 * @param random Randomly generated number
	 * @param riskOfDying The risk of a person dying determined when starting the simulation
	 * @param population The matrix of people in the simulation
	 * @return true
	 */
	public static boolean deathDecider(int currentRowNum, int currentColNum, Random random, double riskOfDying, Population population){
		
		double r = random.nextDouble();
		if(r <= riskOfDying){

			population.population[currentRowNum][currentColNum].setCondition("Dead", true);
			numOfDead++;
			totNumOfDead++;
			currentNumOfInfected--;
		}
		return true;
	}
	
	/**
	 * This method determines if a subject will infect other subjects
	 * @param currentRowNum The row number of the person whose condition is to be determined
	 * @param currentColNum The column number of the person whose condition is to be determined
	 * @param random Randomly generated number
	 * @param infectionRisk The risk of a person getting infected
	 * @param population The matrix of people in the simulation
	 * @param sizeOfPop
	 * @return true
	 */
	public static boolean infectOthers(int currentRowNum, int currentColNum, Random random, double infectionRisk, Population population, int sizeOfPop){
		
		//go through all the adjacent neighbors
		for (int k1 = currentRowNum-1; k1 <= currentRowNum+1; k1++) {
			for (int k2 = currentColNum-1; k2 <= currentColNum+1; k2++) {

				//a person can't infect itself
				if(k1 == currentRowNum && k2 == currentColNum)
					continue;
				
				//Make a try-catch to handle the people at the edge of the matrix
				try{
					if (population.population[k1][k2].getCondition() == "Person is healthy"){
						double r = random.nextDouble();
						if(r <= infectionRisk){
							population.population[k1][k2].setCondition("Infected", true);
							population.population[k1][k2].setInfectionDay(simulationDay);
							numOfInfected++;
							totNumOfInfected++;
							currentNumOfInfected++;
						}
					}
				}
				catch(Exception e){

				}

			}

		}
		return true;
	}

	/**
	 * Prints the population status of the current day
	 * @param day The current day
	 */
	public static void dailyStatus(int day){


		System.out.println("Simulationday: " + day);
		System.out.println("Number of people getting infected today: " + numOfInfected);
		System.out.println("Total number of infected people: " + totNumOfInfected);
		System.out.println("Number of people that has died today: " + numOfDead);
		System.out.println("Total number of dead people: " + totNumOfDead);
		System.out.println("Number of people getting immune today: " + numOfImmune);
		System.out.println("Total number of immune people: " + totNumOfImmune);
		System.out.println("Current number of infected people: " + currentNumOfInfected);
		System.out.println("");

	}
}


