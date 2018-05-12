//for not enough pokeballs
package pokemon.ui;

import javafx.scene.image.ImageView;
import java.util.Random;
import java.util.HashMap;

public class PokemonThread2 implements Runnable{
	
	private Pokemon instance;
	private ImageView imageview;
	private Map map;
	private Player player;
	private HashMap<Pokemon,Thread> pokemonthread;
	private HashMap<Pokemon,PokemonThread> pokemonrunnable;
	private int x;
	private int y;
	private volatile boolean stop=false;
	
	
	PokemonThread2(Pokemon pokemon,Map map,ImageView imageview,Player player,HashMap<Pokemon,Thread> pokemonthread,HashMap<Pokemon,PokemonThread> pokemonrunnable){
		this.instance=pokemon;
		this.map=map;
		this.imageview=imageview;
		this.player=player;
		this.pokemonthread=pokemonthread;
		this.pokemonrunnable=pokemonrunnable;
		this.x=0;
		this.y=0;
	}
	
	@Override
	public void run(){
		Random r=new Random();
		int delay=r.nextInt(3000)+2000;
		try{
			Thread t=pokemonthread.get(instance);
			t.interrupt();
			
			pokemonthread.remove(instance);
			pokemonrunnable.remove(instance);
			Cell c=map.setEmpty(instance.getM(),instance.getN());
			if(player.getccell().getM()==c.getM()&&player.getccell().getN()==c.getN()){
				player.setccell(c);
			}
			Thread.sleep(delay);
			while(stop){}
			Cell.lock();
			Random x1=new Random();
			Random y1=new Random();
			int x=x1.nextInt(map.getN());
			int y=y1.nextInt(map.getM());
			while(map.getCell(y,x).getcontent()!=' '||(player.getccell().getM()==y&&player.getccell().getN()==x)){
				x1=new Random();
				y1=new Random();
				x=x1.nextInt(map.getN());
				y=y1.nextInt(map.getM());	
			}
			map.addCell(y,x,instance);
			instance.reactivate();
			this.x=x;
			this.y=y;
			
			
			PokemonThread r1=new PokemonThread(instance,map,imageview,player);
			Thread t1=new Thread(r1);
			pokemonrunnable.put(instance,r1);
			pokemonthread.put(instance,t1);
			t1.setDaemon(true);
			t1.start();
			Cell.unlock();
			
			
		}catch(InterruptedException e){
			Thread.currentThread().interrupt();
			return;
		}
	}
	
	public void stop(){
		stop=true;
	}
	
	public void resume(){
		stop=false;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
}