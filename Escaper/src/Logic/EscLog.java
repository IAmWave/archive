/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

/**
 *
 * @author Vašek
 */
public class EscLog {

    String[] log;

    public EscLog(int size) {
        log = new String[size];
    }

    public void log(String message) {
        for (int i = 0; i < log.length; i++) {
            if (log[i] == null) {
                log[i] = message;
                return;
            }
        }

        for (int i = 1; i < log.length; i++) {
            log[i - 1] = log[i];
        }
        log[log.length - 1] = message;
    }

    public void logIfDifferent(String message){
        if(!message.equals(getLastMessage())){
        log(message);
        }
    }
    
    public String getLog() {
        String res = "";
        for (int i = 0; i < log.length; i++) {
            if (log[i] != null) {
                res += log[i] + "\r\n";
            } else {
                break;
            }
        }
        return res;
    }
    
    public String getLog(int index){
    return log[index];
    }
    
    public int getLogSize(){
    return log.length;
    }
    
    public String getLastMessage(){
        for (int i = log.length-1; i >=0; i--) {
            if(log[i]!=null){
                return log[i];
            }
        }
        return ""; //log je prázdný
    }
}
