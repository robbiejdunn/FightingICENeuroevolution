import java.util.Random;

import commandcenter.CommandCenter;
import structs.FrameData;
import structs.GameData;
import structs.Key;
import gameInterface.AIInterface;


public class INC0 implements AIInterface {

    // Key class for return.
    Key inputKey;
    // is used for getting a random number.
    Random rnd;
    FrameData fd;
    boolean p;
    CommandCenter cc;

    boolean playerNumber;

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
    public void getInformation(FrameData frameData) {
        fd = frameData;
        cc.setFrameData(fd, p);
    }

    @Override
    public int initialize(GameData gameData,boolean playerNumber) {
        // initializes a Key instance.
        inputKey = new Key();
        // initializes a random instance.
        rnd = new Random();
        p = playerNumber;
        fd = new FrameData();
        cc = new CommandCenter();

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
        if (fd.getFrameNumber() % 20 == 0) {
        /* Action is chosen randomly from 4 possible movements (up down left right) */
            int action = rnd.nextInt(4);

            inputKey.A = false;
            inputKey.B = false;
            inputKey.C = false;

            inputKey.U = false;
            inputKey.D = false;
            inputKey.L = false;
            inputKey.R = false;

            switch (action) {
                case 0:
                    inputKey.D = true;
//                System.out.println("DOWN");
                    break;
                case 1:
                    inputKey.R = true;
//                System.out.println("RIGHT");
                    break;
                case 2:
                    inputKey.L = true;
//                System.out.println("LEFT");
                    break;
                case 3:
                    inputKey.U = true;
//                    System.out.println("UP");
                    break;
            }
        }
    }

    public String getCharacter(){
        return CHARACTER_ZEN;
    }

}
