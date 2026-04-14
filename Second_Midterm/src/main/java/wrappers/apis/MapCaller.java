package wrappers.apis;

public class MapCaller {
    ProcessBuilder command;

    public MapCaller(){
        setCommand(new ProcessBuilder());
    }

    private void setCommand(ProcessBuilder pb){
        this.command = pb;
    }
}
