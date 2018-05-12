package pokemon.ui;

import java.util.concurrent.locks.*;
/*
 * Base class for all cell, contain x-y coordinations and character content
 * 
 */
public class Cell{
	private int M;
	private int N;
	final private Character content;
	private static ReentrantLock lock=new ReentrantLock();
	private static Condition condit=lock.newCondition();
	
	
	public Cell(int M,int N,Character c){
		this.M=M;
		this.N=N;
		this.content=c;
	}
	
	/*
	 *@ return String representation of cell object
	 */
	@Override
	public String toString(){
		return "<"+this.M+","+this.N+">";
	}
	
	/*
	 * @return character content
	 */
	public Character getcontent(){
		return content;
	}
	
	/*
	 * @return y coordinate
	 */
	public int getM(){
		return M;
	}
	
	/*
	 * @return x coordinate
	 */
	public int getN(){
		return N;
	}
	
	public void setM(int M){
		this.M=M;
	}
	
	public void setN(int N){
		this.N=N;
	}
	
	public static void lock(){
		lock.lock();
	}
	
	public static void unlock(){
		lock.unlock();
	}
	public static Condition getCondition(){
		return condit;
		
	}
}