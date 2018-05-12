package pokemon.ui;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.ArrayList;
import javafx.geometry.Insets;
import java.util.HashMap;

public class PokemonWindow extends Application{
	
	private static final String wall=new File("icons/tree.png").toURI().toString();
	private static final String exit=new File("icons/exit.png").toURI().toString();
	private static final String station=new File("icons/ball_ani.gif").toURI().toString();
	
	/**
	 * width of the window
	 */
	private static int W = 1100;
	
	AnimationTimer timer;
	
	/**
	 * height of the window
	 */
	private static int H = 400;
	
	private static int VBOX_WIDTH=220;
	
	private static long messageDisappearingCount=0;

	private static boolean pause=false;
	
	private static boolean finished=false;

	// this define the size of one CELL
	private final static int STEP_SIZE = 40;
	
	// this are the urls of the images
	private static final String front = new File("icons/front.png").toURI().toString();
	private static final String back = new File("icons/back.png").toURI().toString();
	private static final String left = new File("icons/left.png").toURI().toString();
	private static final String right = new File("icons/right.png").toURI().toString();

	// these booleans correspond to the key pressed by the user
	boolean goUp, goDown, goRight, goLeft;

	// current position of the avatar
	double currentPosx = 0;
	double currentPosy = 0;
	
	protected boolean stop = false;
	
	private ImageView avatar;
	
	Group mapgroup;
	
	VBox vbox;
	
	Stage stage;
	
	Button pausebutton;
	
	Button resumebutton;
	
	private Player player;
	
	private Map map;
	
	private ArrayList<Pokemon> pokemons;
	
	private HashMap<Integer,HashMap<Integer,ImageView>> stationimageview;
	
	private HashMap<Station,ImageView> stationimageview1;
	
	private HashMap<Pokemon,ImageView> pokemonimageview1;
	
	private HashMap<Pokemon,Thread> pokemonthread;
	
	private HashMap<Pokemon,PokemonThread> pokemonrunnable;
	
	private HashMap<Pokemon,Thread> pokemonthread2;
	
	private HashMap<Pokemon,PokemonThread2> pokemonrunnable2;
	
	private HashMap<Station,Thread> stationthread;
	
	private HashMap<Station,StationThread> stationrunnable;
	
	public static void main(String[] args) {
		launch(PokemonWindow.class, args);
	}
	
	public void start(Stage stage) {
		stationimageview=new HashMap<Integer,HashMap<Integer,ImageView>>();
		stationimageview1=new HashMap<Station,ImageView>();
		pokemonimageview1=new HashMap<Pokemon,ImageView>();
		pokemonthread=new HashMap<Pokemon,Thread>();
		pokemonrunnable=new HashMap<Pokemon,PokemonThread>();
		pokemonthread2=new HashMap<Pokemon,Thread>();
		pokemonrunnable2=new HashMap<Pokemon,PokemonThread2>();
		stationthread=new HashMap<Station,Thread>();
		stationrunnable=new HashMap<Station,StationThread>();
		this.stage=stage;
		try{
			File inputFile = new File("./sampleIn.txt");
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
		
		    // Read the first of the input file
		    String line = br.readLine();
		    int M = Integer.parseInt(line.split(" ")[0]);
		    int N = Integer.parseInt(line.split(" ")[1]);
		
		    // To do: define a map
		    map = new Map(M,N);
		    H=STEP_SIZE*M;
		    W=STEP_SIZE*N+VBOX_WIDTH;
		
		    // Read the following M lines of the Map
		    for (int i = 0; i < M; i++) {
		    	line = br.readLine();
	
		    	// to do
		    	// Read the map line by line
		    	for (int j = 0; j < N; j++){
		    		map.setMap(i, j, line.charAt(j));
		    	}	
		    }
		
		    // to do
		    // Find the number of stations and pokemons in the map 
		    // Continue read the information of all the stations and pokemons by using br.readLine();
		    while((line=br.readLine())!=null){
		    	String[] a=line.replace(" ","").replace("<","").replace(">","").split(",");	
		    	if(a.length==6){
		    		map.setPokemon(Integer.valueOf(a[0]),Integer.valueOf(a[1]),a[2],a[3],Integer.valueOf(a[4]),Integer.valueOf(a[5]));
		    	}else{
		    		map.setStation(Integer.valueOf(a[0]),Integer.valueOf(a[1]),Integer.valueOf(a[2]));
		    	}	
		    }
		    player=new Player(map.getB());
		    br.close();
		}catch(FileNotFoundException f){
			f.printStackTrace();
			return;
		}
		catch(NumberFormatException n){
			n.printStackTrace();
			return;
		}
		catch(IOException i){
			i.printStackTrace();
			return;
		}

		BorderPane border = new BorderPane();
		pokemons=map.getPokemons();
		
		mapgroup=new Group();
		for(int j=0;j<map.getM();j++){
			for(int i=0;i<map.getN();i++){
				if(map.getMap(j,i).getcontent()=='#'){
					ImageView imageview=new ImageView(new Image(wall));
					imageview.setFitHeight(STEP_SIZE);
					imageview.setFitWidth(STEP_SIZE);
					imageview.relocate(i*STEP_SIZE,j*STEP_SIZE);
					mapgroup.getChildren().add(imageview);
					
					
				}
				if(map.getMap(j,i).getcontent()=='S'){
					ImageView imageview=new ImageView(new Image(station));
					imageview.setFitHeight(STEP_SIZE);
					imageview.setFitWidth(STEP_SIZE);
					imageview.relocate(i*STEP_SIZE,j*STEP_SIZE);
					mapgroup.getChildren().add(imageview);
					HashMap<Integer,ImageView> s=new HashMap<Integer,ImageView>();
					s.put(j,imageview);
					stationimageview.put(i,s);
					stationimageview1.put((Station) map.getMap(j,i),imageview);
					
				}
				if(map.getMap(j,i).getcontent()=='P'){
					String name=((Pokemon) map.getCell(j,i)).getName();
					int id=PokemonList.getIdOfFromName(name);
					String image="icons/"+String.valueOf(id)+".png";
					String image1=new File(image).toURI().toString();
					ImageView imageview=new ImageView(new Image(image1));
					imageview.setFitHeight(STEP_SIZE);
					imageview.setFitWidth(STEP_SIZE);
					imageview.relocate(i*STEP_SIZE,j*STEP_SIZE);
					mapgroup.getChildren().add(imageview);
					pokemonimageview1.put((Pokemon) map.getMap(j,i),imageview);
					
					
				}
				if(map.getMap(j,i).getcontent()=='D'){
					ImageView imageview=new ImageView(new Image(exit));
					imageview.setFitHeight(STEP_SIZE);
					imageview.setFitWidth(STEP_SIZE);
					imageview.relocate(i*STEP_SIZE,j*STEP_SIZE);
					mapgroup.getChildren().add(imageview);
				}
				if(map.getMap(j,i).getcontent()=='B'){
					avatar=new ImageView(new Image(front));
					avatar.setFitHeight(STEP_SIZE);
					avatar.setFitWidth(STEP_SIZE);
					currentPosx=i*40;
					currentPosy=j*40;
					avatar.relocate(i*STEP_SIZE,j*STEP_SIZE);
					mapgroup.getChildren().add(avatar);
				}
			}
		}
		// add top, left and center
		border.setLeft(mapgroup);
		vbox=addVBox();
		border.setRight(vbox);
		Scene scene = new Scene(border,W,H);		
		ArrayList<Pokemon> pokemons=map.getPokemons();
		for(int i=0;i<pokemons.size();i++){
			PokemonThread r=new PokemonThread(pokemons.get(i),map,pokemonimageview1.get(pokemons.get(i)),player);
			Thread t=new Thread(r);
			t.setDaemon(true);
			t.start();
			pokemonthread.put(pokemons.get(i),t);
			pokemonrunnable.put(pokemons.get(i),r);
			
		}
	
		
		// add listener on key pressing
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(!pause){
					switch (event.getCode()) {
					case UP:
						goUp = true;
						avatar.setImage(new Image(back));	
						break;
					case DOWN:
					    goDown = true;
						avatar.setImage(new Image(front));
						break;
					case LEFT:
						goLeft = true;
						avatar.setImage(new Image(left));
						break;
					case RIGHT:
						goRight = true;
						avatar.setImage(new Image(right));
						break;
					default:
						break;
					}
				}
			}
		});
		
		// add listener key released
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				
					switch (event.getCode()) {
					case UP:
						goUp = false;
						break;
					case DOWN:
						goDown = false;
						break;
					case LEFT:
						goLeft = false;
						break;
					case RIGHT:
						goRight = false;
						break;
					default:
						break;
					}
					stop = false;
				}
			
		});
		
		stage.setScene(scene);
		stage.setTitle("Pokemon COMP 3021");
		stage.show();
		
		// it will execute this periodically
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if(!pause){
				ArrayList<Station> stations=map.getStations();
				for(int i=0;i<stations.size();i++){
					Thread t=stationthread.get(stations.get(i));
					if(t!=null){
						if(!t.isAlive()){
							int x=stationrunnable.get(stations.get(i)).getX();
							int y=stationrunnable.get(stations.get(i)).getY();
							stationimageview1.get(stations.get(i)).relocate(x*STEP_SIZE,y*STEP_SIZE);
							stationimageview1.get(stations.get(i)).setVisible(true);
							stationthread.remove(stations.get(i));
							stationrunnable.remove(stations.get(i));
						}
					}
				}
				ArrayList<Pokemon> pokemons=map.getPokemons();
				for(int i=0;i<pokemons.size();i++){
					Thread t=pokemonthread2.get(pokemons.get(i));
					
					if(t!=null){
						if(!t.isAlive()){
							int x=pokemonrunnable2.get(pokemons.get(i)).getX();
							int y=pokemonrunnable2.get(pokemons.get(i)).getY();
							pokemonimageview1.get(pokemons.get(i)).relocate(x*STEP_SIZE,y*STEP_SIZE);
							pokemonimageview1.get(pokemons.get(i)).setVisible(true);
							pokemonthread2.remove(pokemons.get(i));
							pokemonrunnable2.remove(pokemons.get(i));
						}
					}
				}
				String action=player.stayaction();
				if(action.equals("c")){
					String name=((Pokemon) player.getccell()).getName();
					mapgroup.getChildren().remove(pokemonimageview1.get((Pokemon) player.getccell()));
					pokemonthread.get((Pokemon) player.getccell()).interrupt();
					pokemonthread.remove((Pokemon) player.getccell());
					pokemonrunnable.remove((Pokemon) player.getccell());
					Cell cell=map.setEmpty((int) (currentPosy/STEP_SIZE), (int) (currentPosx/STEP_SIZE));
					player.setccell(cell);
					newStage(name);
				}else if(action.equals("f")){
					pokemonimageview1.get((Pokemon) player.getccell()).setVisible(false);
					PokemonThread2 r=new PokemonThread2((Pokemon) player.getccell(),map,pokemonimageview1.get((Pokemon) player.getccell()),player,pokemonthread,pokemonrunnable);
					Thread t=new Thread(r);

					pokemonrunnable2.put((Pokemon) player.getccell(),r);
					pokemonthread2.put((Pokemon) player.getccell(),t);
					Cell cell=map.setEmpty((int) (currentPosy/STEP_SIZE), (int) (currentPosx/STEP_SIZE));
					player.setccell(cell);
					t.setDaemon(true);
					t.start();
					
				}
				
				if(action!=" "&&!action.equals("c")) updateVBox(action);
				if(messageDisappearingCount!=0){
					if(System.currentTimeMillis()>messageDisappearingCount) updateVBox("w");
				}
				
				if (stop)
					return;

				int dx = 0, dy = 0;

				if (goUp) {
					dy -= (STEP_SIZE);
				} else if (goDown) {
					dy += (STEP_SIZE);
				} else if (goRight) {
					dx += (STEP_SIZE);
				} else if (goLeft) {
					dx -= (STEP_SIZE);
				} else {
					// no key was pressed return
					return;
				}
				
				moveAvatarBy(dx, dy);

			}
		}
		};
		// start the timer
		timer.start();
		
		
		
		
	}
	
	private void moveAvatarBy(int dx, int dy) {
		final double cx = avatar.getBoundsInLocal().getWidth() / 2;
		final double cy = avatar.getBoundsInLocal().getHeight() / 2;
		double x = cx + avatar.getLayoutX() + dx;
		double y = cy + avatar.getLayoutY() + dy;
		moveAvatar(x, y);
	}

	private void moveAvatar(double x, double y) {
		final double cx = avatar.getBoundsInLocal().getWidth() / 2;
		final double cy = avatar.getBoundsInLocal().getHeight() / 2;


		if (x - cx >= 0 && x + cx <= W-VBOX_WIDTH && y - cy >= 0 && y + cy <= H) {
			int n=player.getNB();
			boolean caught=true;
			boolean activatep=false;
			boolean activates=false;
            // relocate ImageView avatar
			Cell c=map.getCell((int) ((y-cy)/STEP_SIZE-(y-cy)%40),(int) ((x-cx)/STEP_SIZE-(x-cx)%40));
			if(c instanceof Station){
				activates=((Station) c).isactivate();
			}
			if(player.move(c)){
				if(c instanceof Pokemon){
					if(((Pokemon) c).isactivate()){
						activatep=true;
					}
					caught=false;
				}
				currentPosx = x - cx;
				currentPosy = y - cy;
				avatar.relocate(currentPosx,currentPosy);
			}
			// I moved the avatar lets set stop at true and wait user release the key :)
			stop = true;
			String name="";
			if(player.getNB()<n){
				name=((Pokemon) c).getName();
				mapgroup.getChildren().remove(pokemonimageview1.get((Pokemon) c));	
				Cell cell=map.setEmpty((int) (currentPosy/STEP_SIZE), (int) (currentPosx/STEP_SIZE));
				player.setccell(cell);
				pokemonthread.get((Pokemon) c).interrupt();
				pokemonthread.remove((Pokemon) c);
				pokemonrunnable.remove((Pokemon) c);
				
			}else if(!caught&&activatep){
				pokemonimageview1.get((Pokemon) c).setVisible(false);
				PokemonThread2 r=new PokemonThread2((Pokemon) c,map,pokemonimageview1.get((Pokemon) c),player,pokemonthread,pokemonrunnable);
				Thread t=new Thread(r);
				pokemonthread2.put((Pokemon) c,t);
				pokemonrunnable2.put((Pokemon) c,r);
				Cell cell=map.setEmpty((int) (currentPosy/STEP_SIZE), (int) (currentPosx/STEP_SIZE));
				player.setccell(cell);
				t.setDaemon(true);
				t.start();
				updateVBox("f");
			}else{
				updateVBox("w");
			}
			if(c instanceof Station &&activates){
				StationThread r=new StationThread((Station) c,map,player);
				stationimageview1.get((Station) c).setVisible(false);
				
				Thread t=new Thread(r);
				stationthread.put((Station) c,t);
				stationrunnable.put((Station) c,r);
				updateVBox("b"+String.valueOf(((Station) c).getNB()));
				Cell cell=map.setEmpty((int) (currentPosy/STEP_SIZE), (int) (currentPosx/STEP_SIZE));
				player.setccell(cell);
				t.setDaemon(true);
				t.start();
				
			}
			if(c.getcontent()=='D'){
				finished=true;
				updateVBox("e");
				pause=true;
				ArrayList<Station> stations=map.getStations();
				for(int i=0;i<stations.size();i++){
					if(stationthread.get(stations.get(i))!=null){
						stationthread.get(stations.get(i)).interrupt();
					}
				}
				ArrayList<Pokemon> pokemons=map.getPokemons();
				for(int i=0;i<pokemons.size();i++){
					if(pokemonthread2.get(pokemons.get(i))!=null){
						pokemonthread2.get(pokemons.get(i)).interrupt();
					}
					if(pokemonthread.get(pokemons.get(i))!=null){
						pokemonthread.get(pokemons.get(i)).interrupt();
					}
				}
				timer.stop();
			}
			if(name!=""){
				newStage(name);
			}
		}
	}

	private VBox addVBox(){
		VBox vbox=new VBox(8);
		vbox.setPrefWidth(VBOX_WIDTH);
		vbox.setPadding(new Insets(10,10,30,30));
		Label currentscore=new Label("Current Score:"+String.valueOf(player.calculatescore()));
		Label nplabel=new Label("# of Pokemons Caught:"+String.valueOf(player.getNP()));
		Label nblabel=new Label("# of Pokeballs owned:"+String.valueOf(player.getNB()));
		Text message=new Text();
		resumebutton=new Button("Resume");
		pausebutton=new Button("Pause");
		HBox hbox=new HBox();
		hbox.getChildren().addAll(resumebutton,pausebutton);
		vbox.getChildren().addAll(currentscore,nplabel,nblabel,message,hbox);
		
		pausebutton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				pause();
				updateVBox("p");
			}
		});
		
		resumebutton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				if(pause) updateVBox("r");
				resume();
				
			}
		});
		return vbox;
	}
	
	private void updateVBox(String s){
		if(s.equals("p")){
			((Text) vbox.getChildren().get(3)).setText("Pause");
			((Text) vbox.getChildren().get(3)).setFill(Color.RED);
			return;
		}
		if(s.equals("r")){
			((Text) vbox.getChildren().get(3)).setText("Resume");
			((Text) vbox.getChildren().get(3)).setFill(Color.GREEN);
			return;
		}
		messageDisappearingCount=System.currentTimeMillis()+3000;
		((Label) vbox.getChildren().get(0)).setText("Current Score:"+String.valueOf(player.calculatescore()));
		((Label) vbox.getChildren().get(1)).setText("# of Pokemons Caught:"+String.valueOf(player.getNP()));
		((Label) vbox.getChildren().get(2)).setText("# of Pokeballs owned:"+String.valueOf(player.getNB()));
		if(s.equals("c")){
			((Text) vbox.getChildren().get(3)).setText("Pokemon Caught!");
			((Text) vbox.getChildren().get(3)).setFill(Color.GREEN);
		}else if(s.equals("f")){
			((Text) vbox.getChildren().get(3)).setText("NOT enough pokemon ball");
			((Text) vbox.getChildren().get(3)).setFill(Color.RED);
		}else if(s.equals("w")){
			((Text) vbox.getChildren().get(3)).setText("");
			messageDisappearingCount=0;
		}else if(s.charAt(0)=='b'){
			int NB=Integer.valueOf(s.substring(1));
			if(NB>1){
				((Text) vbox.getChildren().get(3)).setText("You get "+s.substring(1)+" pokemon balls");
			}else{
				((Text) vbox.getChildren().get(3)).setText("You get "+s.substring(1)+" pokemon ball");
			}
			((Text) vbox.getChildren().get(3)).setFill(Color.BLUE);
		}else if(s.equals("e")){
			messageDisappearingCount=0;
			((Text) vbox.getChildren().get(3)).setText("You arrive at the destination!!!");
			((Text) vbox.getChildren().get(3)).setFill(Color.DARKMAGENTA);
		}
	}
	public static void relocateImageView(ImageView imageview,int x,int y){
		imageview.relocate(x*STEP_SIZE,y*STEP_SIZE);
	}
	
	public void newStage(String name){
		Stage stage2=new Stage();
		resumebutton.setDisable(true);
		pausebutton.setDisable(true);
		pause();
	    ImageView pokeball=new ImageView(new Image(new File("icons/pokeball.png").toURI().toString()));
	    pokeball.setFitWidth(100);
	    pokeball.setFitHeight(100);
	    Pane pane=new Pane();
	    int id=PokemonList.getIdOfFromName(name);
		String image="icons/"+String.valueOf(id)+".png";
		String image1=new File(image).toURI().toString();
		ImageView imageview=new ImageView(new Image(image1));
		pane.getChildren().addAll(pokeball,imageview);
		pokeball.relocate(0, 400);
		imageview.relocate(400, 0);
		pokeball.setVisible(false);
	    Scene scene=new Scene(pane,500,500);
	    stage2.setScene(scene);
	    ImageView throwballgif=new ImageView(new Image(new File("icons/throwball.gif").toURI().toString()));
	    ImageView pokeballgif=new ImageView(new Image(new File("icons/pokeball.gif").toURI().toString()));
	    pane.getChildren().add(throwballgif);
	    long starttime=System.currentTimeMillis();
		// add listener key released
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				
					switch (event.getCode()) {
					case UP:
						goUp = false;
						break;
					case DOWN:
						goDown = false;
						break;
					case LEFT:
						goLeft = false;
						break;
					case RIGHT:
						goRight = false;
						break;
					default:
						break;
					}
					stop = false;
				}	
		});

		AnimationTimer timer1 = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if(System.currentTimeMillis()>starttime+600){
					pane.getChildren().remove(throwballgif);
					if(pokeball.getLayoutX()<350){
						pokeball.setVisible(true);
					    pokeball.relocate(pokeball.getLayoutX()+4, pokeball.getLayoutY()-4);
					}else if(pokeball.getLayoutX()<353){
					    pane.getChildren().removeAll(pokeball,imageview);
					    if(!pane.getChildren().contains(pokeballgif)){
					    	pane.getChildren().add(pokeballgif);	
					    	pokeballgif.relocate(-150,0);
					    }
					}
                    if(System.currentTimeMillis()>starttime+4000){
						pane.getChildren().remove(pokeballgif);
						stage2.close();
						stop();
						resume();
						updateVBox("c");
						resumebutton.setDisable(false);
						pausebutton.setDisable(false);
					}
				}
			}
		};
		timer1.start();
		
					
		
	}
	
	public void pause(){
		pause=true;
		ArrayList<Station> stations=map.getStations();
		for(int i=0;i<stationrunnable.size();i++){
			if(stationrunnable.get(stations.get(i))!=null){
				stationrunnable.get(stations.get(i)).stop();
			}
		}
		ArrayList<Pokemon> pokemons=map.getPokemons();
		for(int i=0;i<pokemons.size();i++){
			if(pokemonrunnable2.get(pokemons.get(i))!=null){
				pokemonrunnable2.get(pokemons.get(i)).stop();
			}
			if(pokemonthread.get(pokemons.get(i))!=null){
				pokemonrunnable.get(pokemons.get(i)).stop();
			}
		}
	}
	
	public void resume(){
		if(finished) return;
		pause=false;
		ArrayList<Station> stations=map.getStations();
		for(int i=0;i<stationrunnable.size();i++){
			if(stationrunnable.get(stations.get(i))!=null){
				stationrunnable.get(stations.get(i)).resume();
			}
		}
		ArrayList<Pokemon> pokemons=map.getPokemons();
		for(int i=0;i<pokemons.size();i++){
			if(pokemonrunnable2.get(pokemons.get(i))!=null){
				pokemonrunnable2.get(pokemons.get(i)).resume();
			}
			if(pokemonrunnable.get(pokemons.get(i))!=null){
				pokemonrunnable.get(pokemons.get(i)).resume();
			}
		}
	}
}

