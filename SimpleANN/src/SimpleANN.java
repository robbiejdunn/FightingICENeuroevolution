import commandcenter.CommandCenter;
import enumerate.Action;
import java.lang.Math;
import java.util.Arrays;

import structs.FrameData;
import structs.GameData;
import structs.Key;
import structs.MotionData;
import gameInterface.AIInterface;


public class SimpleANN implements AIInterface {
	ANN ann;
	boolean p;
	GameData gd;
	Key inputKey;
	FrameData fd;
	CommandCenter cc;
	
	@Override
	public int initialize(GameData gameData, boolean playerNumber) {
		// TODO Auto-generated method stub
		gd = gameData;
		p = playerNumber;
		
		inputKey = new Key();
		fd = new FrameData();
		cc = new CommandCenter();
		ann = new ANN(1);
		return 0;
	}

	@Override
	public void getInformation(FrameData frameData) {
		// TODO Auto-generated method stub
		fd = frameData;
		cc.setFrameData(fd, p);

	}

	@Override
	public void processing() {
		if(!fd.getEmptyFlag()){
			if(fd.getRemainingTime() > 0){
				//  In order to get CancelAbleFrame's information on the current action of the opponent character, first you write as follows:
				Action oppAct = cc.getEnemyCharacter().getAction();
				// If you want the same information on a specific action, say "STAND_A", you can simply write:
				// Action action = Action.STAND_A;
				
				// Next, get the MotionData information on the opponent character's action of interest from GameData.
				// You can access the MotionData information with
				// gd.getMotion.elementAt("an instance of action (e.g., oppAct or action)".ordinal())
				MotionData oppMotion = new MotionData();
				if(p)oppMotion = gd.getPlayerTwoMotion().elementAt(oppAct.ordinal());
				else oppMotion = gd.getPlayerOneMotion().elementAt(oppAct.ordinal());
				inputKey.L = true;
				
				// ANN INPUTS
				// Enemy character
				System.out.println((cc.getEnemyCharacter().toString()));
				// Enemy energy
				System.out.println(Math.tanh((double)cc.getEnemyEnergy()));
				// Enemy X
				System.out.println(cc.getEnemyX());
				// Enemy Y
				System.out.println(cc.getEnemyY());
				
				// Energy
				System.out.println(cc.getMyEnergy());
				// X
				System.out.println(Math.tanh((double)cc.getMyX()));
				// Y
				System.out.println(cc.getMyX());
				double[] ins = {Math.tanh(cc.getMyX()), Math.tanh(cc.getMyY())};
				System.out.println(Arrays.toString(ann.feed(ins)));
				System.out.println(oppMotion.getMotionName()+":cancelable " + oppMotion.getCancelAbleFrame() + " frame.");
			}
		}
	}

	@Override
	public Key input() {
		// TODO Auto-generated method stub
		return inputKey;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	public String getCharacter(){
		return CHARACTER_ZEN;
	}

}
