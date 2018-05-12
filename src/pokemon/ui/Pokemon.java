package pokemon.ui;

/*
 * special type of cell, store the name, type, CP and Required balls of the pokemon and whether it is caught
 */
public class Pokemon extends Cell{
	final private String name;
	final private String type;
	final private int CP;
	final private int Rball;
	private boolean activated;
	
	
	public Pokemon(int M,int N,String name,String type,int CP,int Rball){
		super(M,N,'P');
		this.name=name;
		this.type=type;
		this.CP=CP;
		this.Rball=Rball;
		this.activated=true;
	}
	
	/*
	 * @return name of the pokemon
	 */
	public String getName(){
		return name;
	}
	
	/*
	 * @return type of the pokemon
	 */
	public String gettype(){
		return type;
	}
	
	/*
	 * @return CP of the pokemon
	 */
	public int getCP(){
		return CP;
	}
	
	/*
	 * @return required number of balls
	 */
	public int getNB(){
		return Rball;
	}
	
	/*
	 * called when player catchs the pokemon
	 * @return true when player successfully catch the pokemon
	 */
	public boolean catchs(){
		if(activated){
			activated=false;
			return true;
		}
		return false;
	}
	public void reactivate(){
		activated=true;
	}
	
	public boolean isactivate(){
		return activated;
	}

}