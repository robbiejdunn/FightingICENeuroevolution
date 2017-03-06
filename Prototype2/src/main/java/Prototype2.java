import commandcenter.CommandCenter;
import gameInterface.AIInterface;
import structs.FrameData;
import structs.GameData;
import structs.Key;

/**
 * Created by Robbie on 01/03/2017.
 */
public class Prototype2 implements AIInterface {
    boolean p;
    GameData gd;
    Key inputKey;
    FrameData fd;
    CommandCenter cc;
    NeuralNetwork nn;

    @Override
    public int initialize(GameData gameData, boolean playerNumber) {
        // TODO Auto-generated method stub
        gd = gameData;
        p = playerNumber;
        inputKey = new Key();
        fd = new FrameData();
        cc = new CommandCenter();
        nn = new NeuralNetwork();
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
                // INPUTS FOR NN
                // x range -120 680 (-120 left 680 right)
                // y range 335 -130 (335 when on ground, -130 at height of jump)
//                System.out.println("My x: " + cc.getMyX());
//                System.out.println("My y: " + cc.getMyY());
//                System.out.println("Enemy x: " + cc.getEnemyX());
//                System.out.println("Enemy y: " + cc.getEnemyY());
//                System.out.println("My energy: " + cc.getMyEnergy());
                double[] out = nn.feed(cc.getMyX(), cc.getMyY(), cc.getEnemyX(), cc.getEnemyY(), cc.getMyEnergy());
                System.out.println(out.toString());
                // feeds through network
                System.out.println(out[0] + " " + out[1] + " " + out[2] + " " + out[3] + " " + out[4]);

                inputKey.U = (out[0] > 0) ? true : false;
                inputKey.D = (out[1] > 0) ? true : false;
                inputKey.L = (out[2] > 0) ? true : false;
                inputKey.R = (out[3] > 0) ? true : false;
                inputKey.A = (out[4] > 0) ? true : false;
                inputKey.B = (out[5] > 0) ? true : false;
                inputKey.C = (out[6] > 0) ? true : false;

                // need for IFS in case of multiple presses????
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