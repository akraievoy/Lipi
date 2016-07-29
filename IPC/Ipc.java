import java.io.*;
import java.util.*;

//Give executable filename, add optional arguments/commands
//Call runProc()
public class Ipc{
    private String progName,execLoc;
    private List<String> command;
    private Process proc;
    private String stdOut,stdErr;
    
    Ipc(String progName){
    
        setProgName(progName);
        setProgArgs();
    }
    
    public int waitFor() throws InterruptedException{
        return proc.waitFor();
    }
    
    public void setProgName(String progName){
    
        this.progName = progName;
        setProgArgs();
        setExecLoc();
    }
    
    public void setProgArgs(){
    
        command = new ArrayList<String>();
        command.add(execLoc);
    }
     
    public void setProgArgs(List<String> args){
    
        setProgArgs();
        //Converts Space separated args to list then add to end of command
        command.addAll(args);
    }
    
    private void setExecLoc(){
    
        execLoc = System.getProperty("user.dir") + "/" + progName;
    
        //Check for executable file and adjust
            File f = new File(execLoc);

            if(!f.exists() || f.isDirectory()){
                System.out.println("Winduch");
                execLoc = execLoc + ".exe";
                
                if(!f.exists() || f.isDirectory()){
                    System.out.println("File not found at all");
                }
                
            }
            else{
                System.out.println("File found,Assuming Linux");
                }
                
    }
    
    public void runProc(){
    
        try{
            proc = new ProcessBuilder().command(command).redirectErrorStream(true).start();
            showOutput();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
    }
    
    private void showOutput(){
        try{
                BufferedReader stdInput = new BufferedReader(new 
                        InputStreamReader(proc.getInputStream()));

                BufferedReader stdError = new BufferedReader(new 
                        InputStreamReader(proc.getErrorStream()));

                // read the output from the command
                System.out.println("Here is the standard output of the command:");
                String stdOut = null;
                while ((stdOut = stdInput.readLine()) != null){
                    System.out.println(stdOut);
                }

                // read any errors from the attempted command
                System.out.println("Here is the standard error of the command (if any):");
                while ((stdErr = stdError.readLine()) != null){
                    System.out.println(stdErr);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
                
        }
        
    public static void main(String[] args){
            
            Ipc pandoc = new Ipc("pandoc");
            
            List<String> params = new ArrayList<String>();
            
            params.add("test.html");
            params.add("-o");
            params.add("test.md");
            
            pandoc.setProgArgs(params); 
            
            pandoc.runProc();
    }

}
