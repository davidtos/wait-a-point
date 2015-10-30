package something.wait_a_point;

import java.util.Observer;

/**
 * Created by David on 30-10-2015.
 */
public class SingleSocket {
    private static SingleSocket ourInstance = new SingleSocket();

    public static SingleSocket getInstance() {
        return ourInstance;
    }

    private SocketMessenger sm;

    private SingleSocket() {
        sm = new SocketMessenger();
    }

    public SocketMessenger getsm(){
        return sm;
    }

    public void Start(String name){
        sm.start(name);
    }

    public void AddObserver(Observer observer){
        sm.addObserver(observer);
    }

    public void RemoveObserver(Observer observer){
        sm.addObserver(observer);
    }


}
