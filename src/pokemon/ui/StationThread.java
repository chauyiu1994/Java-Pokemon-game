package pokemon.ui;

import java.util.Random;

public class StationThread implements Runnable{
		
	private Station instance;
	private Player player;
	private Map map;
	private volatile boolean stop=false;
	private int x;
	private int y;
	
		
	StationThread(Station a,Map map,Player player){
		this.map=map;
		this.player=player;
		this.instance=a;
		this.x=a.getN();
		this.y=a.getM();
	}
	@Override
	public void run(){
		Random r=new Random();
		int delay=r.nextInt(5000)+5000;		
		try{
			player.setccell(map.setEmpty(instance.getM(),instance.getN()));
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