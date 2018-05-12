package pokemon.ui;
/*
 * A special type of cell ,store the number of balls and whether it is available
 */
public class Station extends Cell{
	final private int Pball;
	private boolean activated;
	
	
	public Station(int M,int N,int Pball){
		super(M,N,'S');
		this.activated=true;
		this.Pball=Pball;
	}
	
	/*
	 * @return number of ball
	 */
	public int getNB(){
		return Pball;
	}
	
	/*
	 * called when player get the balls from the station
	 * @return true if player successfully get the balls
	 */
	public boolean get(){
		if(activated){
			activated=false;
			return true;
		}
		return false;
	}
	
	/*
	 * reactivate the station, called in the recursion when solving the maze
	 */
	public void reactivate(){
		activated=true;
	}
	
	public boolean isactivate(){
		return activated;
	}


}

