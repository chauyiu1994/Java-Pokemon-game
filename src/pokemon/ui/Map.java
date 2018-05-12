package pokemon.ui;
import java.util.ArrayList;

/*
 * Map of the game, store the maximum row and column, starting and ending cells, using two arraylist to record the cells of type Pokemon and Station respectively
 */
public class Map{
	final private int M;
	final private int N;
	private Cell B;
	private Cell D;
	private Cell[][] map;
	private ArrayList<Pokemon> pokemon;
	private ArrayList<Station> stations; 
	
	public Map(int M,int N){
		this.M=M;
		this.N=N;
		map=new Cell[N][M];
		pokemon=new ArrayList<Pokemon>();
		stations=new ArrayList<Station>();
		
	}
	
	/*
	 * initialize the map by creating new cell in related array location, except the pokemon and station cell, and store the starting and ending cell
	 * @param M the y location
	 * @param N the x location
	 * @param c the cell content
	 */
	public void setMap(int M,int N,Character c){
		if(c!='P'||c!='S'){
			map[N][M]=new Cell(M,N,c);
		}
		if(c=='B'){
			B=map[N][M];
		}
		if(c=='D'){
			D=map[N][M];
		}
	}
	
	/*
	 * return cell in specified location
	 * @param M the y location
	 * @param N the x location
	 */
	public Cell getMap(int M,int N){
		return map[N][M];
	}
	
	/*
	 * setup pokemon in specified location, called after reading the last few lines in the input file
	 * @param M the y location
	 * @param N the x location
	 * @param Name the name of the pokemon
	 * @param type the type of the pokemon
	 * @param N the x location
	 */
	public Pokemon setPokemon(int M,int N,String Name,String type,int CP,int Rball){
		Pokemon a=new Pokemon(M,N,Name,type,CP,Rball);
		map[N][M]=a;
		pokemon.add(a);
		return a;
		
	}
	
	public Cell setEmpty(int M,int N){
		Cell c=new Cell(M,N,' ');
		map[N][M]=c;
		return c;
	}
	
	
	/*
	 * setup station in specified location, called after reading the last few lines in the input file
	 * @param M the y location
	 * @param N the x location
	 * @param Nball the number of balls
	 */
	public Station setStation(int M,int N,int Nball){
		Station a=new Station(M,N,Nball);
		map[N][M]=a;
		stations.add(a);
		return a;
	}
	
	/*
	 * @return maximum row
	 */
	public int getM(){
		return M;
	}
	
	/*
	 * @return maximum column
	 */
	public int getN(){
		return N;
	}
	
	/*
	 * @return starting cell
	 */
	public Cell getB(){
		return B;
	}
	
	/*
	 * @return ending cell
	 */
	public Cell getD(){
		return D;
	}
	
	public void addCell(int M,int N,Cell c){
			map[N][M]=c;
			c.setM(M);
			c.setN(N);

	}
    /*
     * @return cell at specified location
     * @param M y coordinate
     * @param N x coordinate
     */
	public Cell getCell(int M,int N){
		return map[N][M];
	}
	
	public ArrayList<Station> getStations(){
		return stations;
	}
	public ArrayList<Pokemon> getPokemons(){
		return pokemon;
	}
	/*
	 * reactivate the stations and pokemon which a player didn't reach or catch
	 * this method is used in the findPath method in the Game class
	 * since only one map is created but many "Players" is created during this recursive method
	 * we have to continue activate and deactivate the station and pokemon
	 * @param p Player
	 */
	public void activate(Player p){
		for(int i=0;i<pokemon.size();i++){
			if(!p.getCpokemon().contains(pokemon.get(i))){
				pokemon.get(i).reactivate();
			}
		}
		for(int i=0;i<stations.size();i++){
			if(!p.getStations().contains(stations.get(i))){
				stations.get(i).reactivate();
			}
		}
	}
}