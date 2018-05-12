package pokemon.ui;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * this class store the current status of the player during the game
 * 
 */
public class Player{
	/*
	 * current number of ball
	 */
	private int NB;
	/*
	 * current number of pokemon
	 */
	private int NP;
	/*
	 * current number of step traveled
	 */
	private int NS;
	/*
	 * current maxmium combat power
	 */
	private int MCP;
	/*
	 * current number of pokemoon type
	 */
	private int NT;
	/*
	 * current cell
	 */
	private Cell Ccell;
	/*
	 * list of current caught pokemon
	 */
	private ArrayList<Pokemon> Cpokemon;
	/*
	 * list of current activated stations
	 */
	private ArrayList<Station> stations;
	/*
	 * list of current traveled cells
	 */
	private ArrayList<Cell> Vcells;
	/*
	 * list of current pokemon types
	 */
	private ArrayList<String> types;
	/*
	 * hashmap to record traveled cells and respective status when moved to it
	 * used to prevent unlimited recursion by avoiding traveling to the same cell with same status
	 * for simplicity, only the score with considering the number of steps is stored as status, but it is enough for preventing unlimited recursion
	 */
	private HashMap<Cell,Integer> status;

	/*
	 * constructor by starting cell
	 */
	public Player(Cell c){	
		NB=0;
		NP=0;
		NS=0;
		MCP=0;
		NT=0;
		Ccell=c;
		Cpokemon=new ArrayList<Pokemon>();
		stations=new ArrayList<Station>();
		Vcells=new ArrayList<Cell>();
		types=new ArrayList<String>();
		status=new HashMap<Cell,Integer>();
		Vcells.add(c);
		
	}
	
	/*
	 * Deep copy constructor
	 */
	public Player(Player p){
		this.NB=p.NB;
		this.NP=p.NP;
		this.NS=p.NS;
		this.MCP=p.MCP;
		this.NT=p.NT;
		this.Ccell=p.Ccell;
		this.Cpokemon=new ArrayList<Pokemon>(p.Cpokemon);
		this.stations=new ArrayList<Station>(p.stations);
		this.Vcells=new ArrayList<Cell>(p.Vcells);
		this.types=new ArrayList<String>(p.types);
		this.status=new HashMap<Cell,Integer>(p.status);
	}
	
	/*
	 * called when desire player to move, updating the member data by determinating the input cell is wall,empty cell, pokemon or station 
	 * @param c cell desired to move to
	 * @return whether the movement is successful
	 */
	public boolean move(Cell c){
		if(c.getcontent()=='#'){
			return false;
		}
		NS++;
		Ccell=c;
		Vcells.add(c);
		if(c instanceof Pokemon){
			if(NB>=((Pokemon) c).getNB()){
		    	if(((Pokemon) c).catchs()){
				    NP++;
				    Cpokemon.add((Pokemon) c);
				    NB-=((Pokemon) c).getNB();
				    if(((Pokemon) c).getCP()>MCP){
					    MCP=((Pokemon) c).getCP();
				    }
				    if(!types.contains(((Pokemon) c).gettype())){
					    types.add(((Pokemon) c).gettype());
					    NT++;
				    }
			    }
			}
		}
		if(c instanceof Station){
			if(((Station) c).get()){
				NB+=((Station) c).getNB();
				stations.add((Station) c);
			}
		}
		status.put(c,NB+5*NP+10*NT+MCP);
		return true;
	}
	
	public String stayaction(){
		if(Ccell instanceof Pokemon){
			if(NB>=((Pokemon) Ccell).getNB()){
		    	if(((Pokemon) Ccell).catchs()){
				    NP++;
				    Cpokemon.add((Pokemon) Ccell);
				    NB-=((Pokemon) Ccell).getNB();
				    if(((Pokemon) Ccell).getCP()>MCP){
					    MCP=((Pokemon) Ccell).getCP();
				    }
				    if(!types.contains(((Pokemon) Ccell).gettype())){
					    types.add(((Pokemon) Ccell).gettype());
					    NT++;
				    }
				    
				    return "c";
			    }
			}
			if(((Pokemon) Ccell).isactivate()) return "f";
		}	
		return " ";
	}
	
	public void setccell(Cell c){
		Ccell=c;
	}
	
	/*
	 * @return score
	 */
	public int calculatescore(){
		return NB+5*NP+10*NT+MCP-NS;
	}
	
	/*
	 * @return String format of player
	 */
	public String printplayer(){
		return String.valueOf(this.calculatescore())+"\n"+ String.valueOf(this.NB)+":"+String.valueOf(this.NP)+":"+String.valueOf(this.NT)+":"+String.valueOf(this.MCP)+"\n";
	}
	
	/*
	 * @return visited cells
	 */
	public ArrayList<Cell> getvcells(){
		return Vcells;
	}
	
	/*
	 * @return caught pokemon
	 */
	public ArrayList<Pokemon> getCpokemon(){
		return Cpokemon;
	}
	
	/*
	 * @return activated stations
	 */
	public ArrayList<Station> getStations(){
		return stations;
	}
	
	/*
	 * @return number of steps
	 */
	public int getNS(){
		return NS;
	}
	
	public int getNP(){
		return NP;
	}
	
	public int getNB(){
		return NB;
	}
	
	public Cell getccell(){
		return Ccell;
	}
}