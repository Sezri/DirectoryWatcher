package directoryWatcher;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainClass {
	public static void main(String args[]) {
		
		Scanner input = new Scanner(System.in);
		String path;
		String extension;
		
		if(args.length < 1) {
			System.out.print("Please input a valid file path to monitor: ");
			path = input.nextLine();
		} else {
			path = args[0];
		}
		
		boolean success = false;
		while(!success) {
			try {
				if(!new File(path).isDirectory()) throw new NullPointerException();
				success = true;
			} catch(NullPointerException e) {
				System.err.print("Invalid file path or path is not a directory, please try again: ");
				path = input.nextLine();
			}
		}
		
		if(args.length < 2) {
			System.out.print("Please input a valid file extension to monitor: ");
			extension = input.nextLine();
		} else {
			extension = args[1];
		}
		
		DirectoryWatcher watcher = new DirectoryWatcher(path, extension);

		
		Runnable runnableWatcher = new Runnable() {
			
			@Override
			public void run() {
				watcher.checkDirectory();
			}
		};
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(runnableWatcher, 0, 10, TimeUnit.SECONDS);
        input.close();
        
		String anim= "|/-\\";
		int i = 0;
        while(true) {
            String data = "\rMonitoring Relevant Files" + anim.charAt(i++ % anim.length());
            
            try {
				Thread.sleep(100);
				System.out.write(data.getBytes());
			} catch (InterruptedException e) {
			} catch (IOException e) {
			}
        }
        

	}
}
