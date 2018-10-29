package modellering;

import java.util.Random;

public class Person {
	
	public int sickDays;
	public int infectionDay = 0;
	public boolean isInfected = false;
	public boolean isImmune = false;
	public boolean isDead = false;
	
	/**
	 * Sets the number of days a person is sick to a random number between minDay and maxDay
	 * @param minDay The minimum number of days a person can be sick
	 * @param maxDay The maximum number of days a person can be sick
	 * @param random Randomly generated number
	 */
	public Person(int minDay, int maxDay, Random random){
		
		sickDays = random.nextInt((maxDay - minDay) + 1) + minDay;
	}
	
	/**
	 * Sets the condition of a person in the population
	 * @param condition The condition to be set
	 * @param b True or false value for this condition
	 */
	public void setCondition(String condition, boolean status){
//		if (condition == "Infected")
//			isInfected = status;
//		if (condition == "Immune")
//			isImmune = status;
//		if (condition == "Dead")
//			isDead = status;
		switch(condition){
			case "Infected":
				isInfected = status;
				break;
			case "Immune":
				isImmune = status;
				break;
			case "Dead":
				isDead = status;
				break;
		}
	}
	
	/**
	 * Returns the condition of a person 
	 * @return The condition of a person
	 */
	public String getCondition(){
		if(isImmune == true)
			return "Person is immune";
		else if(isDead == true)
			return "Person is dead";
		else if(isInfected == false)
			return "Person is healthy";
		else if(isInfected == true)
			return "Person is infected";
		else
			return "";
	}
	
	/**
	 * Sets the day the person got infected to the integer n
	 * @param n The day of the simulation the the person got infected
	 */
	public void setInfectionDay(int n){
		infectionDay = n;
	}
	
	/**
	 * Returns the day of the simulation a person was infected
	 * @return The day of the simulation a person was infected
	 */
	public int getInfectionDay(){
		return infectionDay;
	}
	
	/**
	 * Returns the number of days a person will be sick
	 * @return The number of days a person will be sick
	 */
	public int getsickDays (){
		return sickDays;
	}
	
	@Override 
	public String toString(){
		
		if(getCondition() == "Person is healthy"){
			return "Person is healthy";
		}
		
		else{
			return "Condition: " + getCondition() + "\n"
					+ "Infectionday: " + getInfectionDay() + "\n"
					+ "SickDays: " + getsickDays();
		}
	}

}
