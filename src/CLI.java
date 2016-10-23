import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
 
public class CLI {
	static Scanner cin = new Scanner(System.in);
	private static final String[] validCommands = { "pwd", "ls", "cd", "cp", "mv", "rm", "rmdir", "mkdir", "cat", "more",
			"date", "clear", "help", "args" };
	private static final int numOfArguments[] = {0, 0, 1, -1, -1, 1, 1, 1, 1, 0, 0, 0, 0, 0};
	private static final String defaultDirectory = System.getProperty("user.dir");
	private static final String[] Description = { "display current user directory.",
			"list each given file or directory name.", "changes the current directory to another one.",
			"copy two files content cp -r ~/folder1/. ~/new_folder1.",
			"renames file SOURCE to DEST, or moves the SOURCE file (or files) to DIRECTORY.", "remove file.",
			"remove directory", "creates a directory with each given name.",
			"Concatenate files and print on the standard output.",
			"display and scroll down the output in one direction only.",
			"display or set the date and time of the system.", "clear the current terminal screen.",
			"gives a description of all functions.",
			"list all parameters on the command line, numbers or strings for specific command." };
	private static void args() {
		System.out
				.println("\nls()\npwd()\narg()\ndate()\nmore()\nless()\nhelp()\nclear()\n" + "cp(String newdirectory)\n"
						+ "cat( String directory )\n" + "cd( String directory )\n" + "mv(String directory)\n"
						+ "rm( String filename )\n" + "mkdir( String newdirectory )" + "\nrmdir( String directory )\n");
	}
	private static void date() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss E");
		Calendar cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));
	}
	private static void help(String command) { // '?' for all
		for (int i = 0; i < 14; i++) {
			if (command.equals("?") || validCommands[i].equals(command)) {
				System.out.print(validCommands[i] + ": " + Description[i] + "\n");
			}
		}
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
	private static void exit() {
		System.exit(0);
	}
	private static void mv ( ArrayList <String> args )
	{
		// Edit paths format to the current directory if it exists in it
		String path;
		for ( int i = 0 ; i < args.size(); i++ )
		{
			if ( args.get(i).charAt(0) != '/' )
			{
				path =  "/" + args.get(i);
			}
			else
			{
				path = args.get(i);
			}
			if ( new File( System.getProperty("user.dir") + path ).exists() ||
				new File( defaultDirectory + path ).exists()  || 					
				new File( System.getProperty("user.dir") + path ).getParentFile() == null ||
				new File( System.getProperty("user.dir") + path ).getParentFile().exists() )
			{
					args.set( i , System.getProperty("user.dir") + path );
			}
 
		}
		// Start to move each files or directory if exists
		String source, destination;
		if ( args.size() == 2 )
		{
			source = args.get(0);
			destination = args.get(1); 
 
			File sourcePath = new File(source), destinationPath = new File(destination);
 
			if ( sourcePath.exists() && destinationPath.isDirectory() ) 
			{
				if ( destination.charAt(destination.length()-1) != '/' )
				{
					destination += "/";
				}
				// Check if there is a file with the same name in the destination directory
				if ( new File(destination + sourcePath.getName()).exists() )
				{
					String choice;
					do {
						System.out.print("mv : Overwrite " + destination + sourcePath.getName() + " (Yes/No) : " );
						choice = cin.next();
					} while ( !choice.equals("Yes") && !choice.equals("No") );
 
					if ( choice.equals("Yes") )
					{
						new File(destination + sourcePath.getName()).delete();
						sourcePath.renameTo(new File(destination + sourcePath.getName()));
						System.out.println("mv : " + source + " is moved successfully to " + destination );
					}
					else
					{
						System.out.println("mv : moving "+ source + " to " + destination + " is canceled" );
					}
				}
				else
				{
					sourcePath.renameTo(new File(destination + sourcePath.getName()));
					System.out.println("mv : " + source + " is moved successfully to " + destination );
				}
			} 
			else if ( sourcePath.exists() && !destinationPath.exists() )														
			{
				// Change the names of the file
				if ( sourcePath.getParentFile().equals( destinationPath.getParentFile() ) ) 
				{
					sourcePath.renameTo(destinationPath);
					System.out.print( "mv : File " + sourcePath.getName() );
					System.out.println( " is renamed successfully to " + destinationPath.getName());
				}
				else
				{
					System.out.println("mv : cannot state " + destination + " : no such file or directory");
				}
			} 
			else 
			{
				System.out.println("mv : cannot state " + source + " : no such file or directory");
			}
		}
		else
		{
			ArrayList <String> unavailablePaths = new ArrayList <String> ();
			ArrayList <String> parameters = null;
			destination = args.get(args.size()-1);
			if ( new File(destination).isDirectory() )
			{
				for ( int i = 0 ; i < args.size()-1 ; i++ )
				{
					parameters = new ArrayList <String> (2); 
					parameters.add(args.get(i)); 
					parameters.add(destination);
					if ( new File(args.get(i)).exists() )
					{
						mv(parameters);
					}
					else
					{
						unavailablePaths.add(args.get(i));
					}
				}
 
				if ( unavailablePaths.size() != 0 )
				{
					System.out.print("mv : cannot state ");
					for ( int i = 0 ; i < unavailablePaths.size() ; i++ )
					{
						System.out.print(" '"+unavailablePaths.get(i)+"' ");
					}
					System.out.println(": no such file or directory");
				}
			}
			else
			{
				System.out.println("mv : cannot state " + destination + " : no such file or directory" );
			}
		}
	}
	private static void cp(ArrayList<String> args) {
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
			else 
				destinationFile = new File (destination);
			try {
				if (destinationFile.exists()){
					System.out.println("Destination file already exists - overwrite?");
					String choice = cin.next();
					if (!(choice.equals("Y") || choice.equals("YES") || choice.equals("Yes"))) {
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
				ArrayList<String> parameters = new ArrayList<String>();
				parameters.add((String)file.getAbsolutePath());
				parameters.add(destinationFile.getAbsolutePath());
				mv(parameters);
 
			}
		}
 
	}
 
 
	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		while (true) {
			System.out.print("$ ");
			String line = cin.nextLine();
			line = line.trim();
			if (line.compareTo("exit") == 0) {
				cin.close();
				exit();
			}
			String[] commands = line.split(";");
 
			for (int i = 0; i < commands.length; ++i) {
				commands[i] = commands[i].trim();
				String[] splitted = commands[i].split(" ");
				int len = splitted.length;
				if (len == 0)
					break;
				if (splitted[0].equals("?")) {
					if (len > 2) {
						System.out.print("Only one function is permitted\n");
						continue;
					}
					help(splitted[1]);
					continue;
				}
				boolean found = false;
				for (int j = 0; j < 14; j++){
					if (validCommands[j].compareTo(splitted[0]) == 0 ) {
						if (j == 2 && len == 1) { // command = cd
							cd(null);
							found = true;
							break;
						}
						if (checkArguments(j, len - 1)){
							ArrayList<String>A = new ArrayList<String>();
							for (int k = 1; k < len; ++k) 
								A.add(splitted[k]);
							execute(j, A);
							found = true;
						}
					}
				}
				if (!found) {
					System.out.println("No such command - press help\n");
				}
			}
		}
	}
	private static boolean checkArguments(int i, int len){
		if ( numOfArguments[i] == -1)
			return len > 1;
		if (len > numOfArguments[i]) {
			System.out.print("too many Arguments\n");
			return false;
		}
		if (len < numOfArguments[i]) {
			System.out.print("too few Arguments\n");
			return false;
		}
		return true;
	}
	private static  void cd (String path) {
		// If the command is "cd" only or "cd ~" make the default directory the current on
		if ( path == null || path.equals("~") ) 
		{
			System.setProperty("user.dir", defaultDirectory );
		}
		// If the command is "cd .." make the parent directory the current one
		else if ( path.equals("..") )
		{
			if ( new File(System.getProperty("user.dir")).getParent() != null )
			{
				System.setProperty("user.dir", new File(System.getProperty("user.dir")).getParent());	
			}
		}
		// If the command is "cd /" make the root directory of the default the current one
		else if ( path.equals("/") )
		{
			cd(null);
			while ( new File(System.getProperty("user.dir")).getParent() != null )
			{
				System.setProperty("user.dir", new File(System.getProperty("user.dir")).getParent());	
			}
		}
		// if the command is "cd /with/specific/path" 
		else
		{
			if ( new File(path).isDirectory() )
		    {
				System.setProperty("user.dir", path);
		    }
		    else
		    {
		    	if ( path.charAt(0) != '/' ) path = "/" + path;
		    	// if the path is for directory inside the current directory
		    	if ( new File ( System.getProperty("user.dir") + path ).isDirectory() )
		    	{
		    		System.setProperty( "user.dir" , System.getProperty("user.dir") + path );	
		    	}
		    	else
		    	{
		    		System.out.println("cd : cannot state " + path + " : no such file or directory" );
		    	}
		    }
		}
	}
	private static void mkdir( String filePath){
		File f;
		if(filePath.contains("/"))f= new File(filePath);
		else{
			f = new File(System.getProperty("user.dir")+"/"+filePath);
			System.out.println(System.getProperty("user.dir"));
		}
		if(f.mkdir())System.out.println("Directory created successfully !");
		else System.err.println("Couldn't create directory");
	}
 
	private static void rmdir(String filePath){ 
		File f;
	
		if(filePath.contains("/"))f= new File(filePath);
		else f = new File(System.getProperty("user.dir")+"/"+filePath);
		if(f.isDirectory()){
			if(f.delete())System.out.println("Directory was removed successfull !");
			else System.err.println("Couldn't remove directory");
		}
		else  System.err.println("Error. Wrong directory path");
	}
 
	private static void rm(String filePath){	
		File f;
		if(filePath.contains("/"))f= new File(filePath);
		else f = new File(System.getProperty("user.dir")+"/"+filePath);
		if(f.isFile()){
			if(f.delete())System.out.println("File was removed succesfully !");
			else System.err.println("Couldn't remove file");
		}
		else System.err.println("Error. Wrong file path");
	}
 
	private static void cat(String ...filePaths){
 
		for(String s : filePaths){
			File f;
			if(s.contains("/"))f= new File(s);
			else f = new File(System.getProperty("user.dir")+"/"+s);
			if(!f.isFile()){
				System.err.println("cat: "+ s +": No such file found");
				continue;
			}
 
			try {
				Scanner in = new Scanner(f);
				while(in.hasNextLine())System.out.println(in.nextLine());
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
 
 
	}
	private static void execute(int index, ArrayList<String>Args) {
		if (numOfArguments[index] == 0) {
			if (validCommands[index].equals("help"))
				help("?");
 
			if (validCommands[index].equals("pwd")) 
				pwd();
			if (validCommands[index].equals("ls")) 
				ls();
			if (validCommands[index].equals("args")) 
				args();
			if (validCommands[index].equals("date")) 
				date();
			if (validCommands[index].equals("clear")) 
				clear();
 
		}
		if (validCommands[index].equals("mv"))
			mv(Args);
 
		if (validCommands[index].equals("cp"))
			cp(Args);
		if (validCommands[index].equals("cd")){
			cd(Args.get(0));
		}
 
		if (validCommands[index].equals("mkdir"))
			mkdir(Args.get(0));
		if (validCommands[index].equals("rmdir"))
			rmdir(Args.get(0));
		if (validCommands[index].equals("rm"))
			rm(Args.get(0));
		if (validCommands[index].equals("cat"))
			cat(Args.get(0));
 
	}
}
