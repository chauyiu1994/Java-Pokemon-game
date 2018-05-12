//for the pokemons to move around
package pokemon.ui;

import javafx.scene.image.ImageView;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class PokemonThread implements Runnable{
	private final static int timeBudgetms=5*60*1000;
	private Pokemon instance;
	private Map map;
	private Player player;
	private ImageView imageview;
	private volatile boolean stop=false;
	
	PokemonThread(Pokemon pokemon,Map map,ImageView imageview,Player player){
        this.instance=pokemon;
        this.map=map;
        this.imageview=imageview;
        this.player=player;
        this.stop=false;
	}
	
	@Override
	public void run(){
		long startTime=System.currentTimeMillis();
		while(startTime+timeBudgetms>System.currentTimeMillis()){
			Random r=new Random();
			int delay=r.nextInt(1000)+1000;
			try{
				Thread.sleep(delay);
				while(stop){}
					int x=instance.getN();
					int y=instance.getM();
					boolean canRight=true;
					boolean canLeft=true;
					boolean canUp=true;
					boolean canDown=true;
					if(x<map.getN()-1){
						if(map.getCell(y,x+1).getcontent()!=' ') canRight=false; 
					}else{
						canRight=false;
					}
					if(x>0){
						if(map.getCell(y,x-1).getcontent()!=' ') canLeft=false;
					}else{
						canLeft=false;
					}
					if(y<map.getM()-1){
						if(map.getCell(y+1,x).getcontent()!=' ') canDown=false;
					}else{
						canDown=false;
					}
					if(y>0){
						if(map.getCell(y-1,x).getcontent()!=' ') canUp=false;
					}else{
						canUp=false;
					}
					if((!canUp)&&(!canDown)&&(!canRight)&&(!canLeft)){
						continue;
					}
					
					Cell c=map.setEmpty(instance.getM(),instance.getN());
					if(player.getccell().getM()==c.getM()&&player.getccell().getN()==c.getN()){
						player.setccell(c);
					}
					Cell.lock();
					
					ArrayList<String> directions=new ArrayList<String>();
					if(canUp) directions.add("up");
					if(canDown) directions.add("down");
					if(canRight) directions.add("right");
					if(canLeft) directions.add("left");
					Collections.shuffle(directions);
					String s=directions.get(0);
	
					if(s.equals("up")){
						y--;
					}else if(s.equals("down")){
						y++;
					}else if(s.equals("right")){
						x++;
					}else if(s.equals("left")){
						x--;
					}
					
					imageview.relocate(x*40,y*40);
					
					map.addCell(y,x,instance);
					if(player.getccell().getM()==y&&player.getccell().getN()==x){
						player.setccell(instance);
					}
					Cell.unlock();
			}catch(InterruptedException e){
				Thread.currentThread().interrupt();
				return;
			}
		}
	}
	
	public void stop(){
		stop=true;
	} 
	
	public void resume(){
		stop=false;
	}
}