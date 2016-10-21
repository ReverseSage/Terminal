
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class CLI {

	/**
	 * @param args
	 */
	static Scanner cin = new Scanner(System.in);
	private static String[] validCommands = { "pwd", "ls", "cd", "cp", "mv", "rm", "rmdir", "mkdir", "cat", "more",
			"date", "clear", "help", "args" };
	private static final String defaultDirectory = System.getProperty("user.dir");

	private static String[] Description = { // to be edit - not important
											// anyway!
			"display current user directory.", "list each given file or directory name.",
			"changes the current directory to another one.", "copy two files content cp -r ~/folder1/. ~/new_folder1.",
			"renames file SOURCE to DEST, or moves the SOURCE file (or files) to DIRECTORY.", "remove file.",
			"remove directory", "creates a directory with each given name.",
			"Concatenate files and print on the standard output.",
			"display and scroll down the output in one direction only.",
			"display or set the date and time of the system.", "clear the current terminal screen.",
			"gives a description of all functions.",
			"list all parameters on the command line, numbers or strings for specific command." };

	private static void date() {
		// E : for day (Sun Mon Thu ..)
		// getTime() returns also Time zone like 'EET'

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss E");
		Calendar cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));
	}

	private static void help(String command) { // '?' for all
		for (int i = 0; i < 14; i++) {
			if (command == "?" || validCommands[i] == command) {
				System.out.print(validCommands[i] + ": " + Description[i] + "\n");
			}
		}
	}

	private static void args() { // to be continued .. not important anyway
									// bardo :D
		System.out
				.println("\nls()\npwd()\narg()\ndate()\nmore()\nless()\nhelp()\nclear()\n" + "cp(String newdirectory)\n"
						+ "cat( String directory )\n" + "cd( String directory )\n" + "mv(String directory)\n"
						+ "rm( String filename )\n" + "mkdir( String newdirectory )" + "\nrmdir( String directory )\n");
	}

	private static void pwd() {
		System.out.println(System.getProperty("user.dir"));
	}

	private static void clear() {
		for (int i = 0; i < 60; i++) {
			System.out.print("\n");
		}
	}

	private static void ls() {
		File currentPath = new File(System.getProperty("user.dir"));
		File[] paths = currentPath.listFiles();
		for (int i = 0; i < paths.length; i++) {
			System.out.println(paths[i].getName());
		}

	}

	private static void cd (String path) {
		// default directory will be sent as parameter if cd only entered bye the user, to be checked in main.
		if ( path == ".." )
		{
			System.setProperty("user.dir", new File(System.getProperty("user.dir")).getParent());	
		}
		else
		{
			if ( new File(path).isDirectory() )
		    {
				System.setProperty("user.dir", path);
		    }
		    else
		    {
		    	System.out.println("There is no such directory.");
		    }
		}
	}
	
	private static void mv ( ArrayList <String> args ){
		String source, destination;
		if ( args.size() < 2 )
		{
			System.out.println("Error: few nubmer of arguments");
			return;
		}
		else if ( args.size() == 2 )
		{
			source = args.get(0);
			destination = args.get(1);
			File sourcePath = new File(source);
			File destinationPath = new File(destination);

			if (sourcePath.exists() && destinationPath.isDirectory()) 
			{
				if (destination.charAt(destination.length() - 1) == '/') 
				{
					sourcePath.renameTo(new File(destination + sourcePath.getName()));
				} 
				else 
				{
					sourcePath.renameTo(new File(destination + "/" + sourcePath.getName()));
				}
			} 
			else if (sourcePath.exists() && !destinationPath.exists()) // Changing name															// Names
			{
				if (sourcePath.getParentFile() == destinationPath.getParentFile() ||
				    sourcePath.getParentFile().equals(destinationPath.getParentFile())) 
				{
					sourcePath.renameTo(destinationPath);
				} 
				else 
				{
					System.out.println("There is no such directory.");
				}
			} 
			else 
			{
				System.out.println("There is no such directory.");
			}
		}
		else
		{
			ArrayList <String> notFound = new ArrayList <String> ();
			ArrayList <String> found = null;
			destination = args.get(args.size()-1);
			if ( new File(destination).isDirectory() )
			{
				for ( int i = 0 ; i < args.size()-1 ; i++ )
				{
					found = new ArrayList <String> (2); found.add(args.get(i)); found.add(destination);
					if ( new File(args.get(i)).exists() )
					{
						mv(found);
					}
					else
					{
						notFound.add(args.get(i));
					}
				}
				
				if ( notFound.size() == 0 )
				{
					System.out.println("All files were successfully moved to " + destination );
				}
				else
				{
					System.out.print("cannot stat ");
					for ( int i = 0 ; i < notFound.size() ; i++ ) System.out.print(" '"+notFound.get(i)+"' ");
					System.out.println(": No such file or directory");
				}
			}
			else
			{
				System.out.println(destination+" : no such directory");
			}
			
		}
	}
	private static void cp(ArrayList<String> args) {
		if (args.size() < 2) {
			System.out.println("to few arguments");
			return;
		}
		String destination = args.get(args.size() - 1);
		File destinationFile = null;
		if (args.size() == 2) {
			String source = args.get(0);
			File sourceFile = new File(source);
			if (!sourceFile.isFile()) {
				sourceFile = new File(System.getProperty("user.dir") + '/' + source);
			}
			if (!sourceFile.isFile()) {
				System.out.println("Source does not exists");
				return;
			}

			int idx = destination.indexOf("/");

			if (idx == -1) // destination will be on the current director
				destinationFile = new File(System.getProperty("user.dir") + '/' + destination);
			try {
				if (destinationFile.exists()){
					System.out.println("Destination file already exists - overwrite?");
					String choice = cin.next();
					if (!(choice == "Y" || choice == "YES" || choice == "Yes")) {
						return;
					}
				}
				Files.copy(sourceFile.toPath(), destinationFile.toPath(), REPLACE_EXISTING, NOFOLLOW_LINKS);
			} catch (IOException e) {
				System.out.println("Invalid path.");
			}
		} else {
			destinationFile = new File(destination);
			if (!destinationFile.isDirectory()){
				System.out.println(destination + "\n" + destinationFile.getAbsolutePath() + "\ninvalid Destination path");
				return;
			}
			for (int i = 0; i + 1 < args.size(); i++) {
				File file = new File(args.get(i));
				int idx = args.get(i).indexOf("/");
				if (idx == -1) {
					file =  new File(System.getProperty("user.dir") + '/' + args.get(i));
				}
				if (!file.exists()){
					System.out.println("path number " + (i + 1) + " does not exist");
					continue;
				}
				mv(file.getAbsolutePath(), destinationFile.getAbsolutePath());
				
			}
		}

	}

	
	public static void main(String[] args) {
		

	}

}
