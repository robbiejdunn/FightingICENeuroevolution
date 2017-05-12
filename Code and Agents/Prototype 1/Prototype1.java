import commandcenter.CommandCenter;
import enumerate.Action;
import java.lang.Math;
import java.util.Arrays;

import structs.FrameData;
import structs.GameData;
import structs.Key;
import structs.MotionData;
import gameInterface.AIInterface;


public class Prototype1 implements AIInterface {
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
		ann = new ANN();
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
				//System.out.println(cc.getEnemyX());
				// X RANGE -120 530
				//System.out.println(cc.getEnemyY());
				// feeds through network
				double[] out = ann.feed(
					Math.tanh((double)cc.getMyX()),			// horizontal distance
					Math.tanh((double)cc.getMyY()),			// vertical distance
					Math.tanh((double)cc.getMyEnergy())		// player's energy
				);
				inputKey.A = (out[0] > 0) ? true : false;
				inputKey.B = (out[1] > 0) ? true : false;
				inputKey.C = (out[2] > 0) ? true : false;
				inputKey.U = (out[3] > 0) ? true : false;
				inputKey.D = (out[4] > 0) ? true : false;
				inputKey.L = (out[5] > 0) ? true : false;
				inputKey.R = (out[6] > 0) ? true : false;
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
