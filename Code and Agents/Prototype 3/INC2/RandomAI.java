import java.util.Random;

import structs.FrameData;
import structs.GameData;
import structs.Key;
import gameInterface.AIInterface;


public class RandomAI implements AIInterface {

  Key inputKey;
  Random rnd;
  
  boolean playerNumber;
  
  @Override
  public void close() {
    // TODO Auto-generated method stub

  }

  @Override
  public void getInformation(FrameData frameData) {
    
  }

  @Override
  public int initialize(GameData gameData,boolean playerNumber) {
    // Initialise key and random object
    inputKey = new Key();
    rnd = new Random();
    
    return 0;
  }

  @Override
  public Key input() {
    // TODO Auto-generated method stub
    
    return inputKey;
  }

  @Override
  public void processing() {
    // Set every key randomly
    inputKey.A = (rnd.nextInt(10) > 4) ? true : false;
    inputKey.B = (rnd.nextInt(10) > 4) ? true : false;
    inputKey.C = (rnd.nextInt(10) > 4) ? true : false;
    inputKey.U = (rnd.nextInt(10) > 4) ? true : false;
    inputKey.D = (rnd.nextInt(10) > 4) ? true : false;
    inputKey.L = (rnd.nextInt(10) > 4) ? true : false;
    inputKey.R = (rnd.nextInt(10) > 4) ? true : false;
  } 

  public String getCharacter(){
    return CHARACTER_ZEN;
  }
  
}
