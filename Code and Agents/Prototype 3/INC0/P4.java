import java.util.Random;

import structs.FrameData;
import structs.GameData;
import structs.Key;
import gameInterface.AIInterface;


public class P4 implements AIInterface {

    // Key class for return.
    Key inputKey;

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
        // initializes a Key instance.
        inputKey = new Key();

        return 0;
    }

    @Override
    public Key input() {
        // TODO Auto-generated method stub

        // returns Key
        return inputKey;
    }

    @Override
    public void processing() {
    }

    public String getCharacter(){
        return CHARACTER_ZEN;
    }

}
