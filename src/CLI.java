import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CLI {

	/**
	 * @param args
	 */
	private static String[] validCommands = { "pwd", "ls", "cd", "cp", "mv", "rm", "rmdir", "mkdir", "cat", "more",
			"date", "clear", "help", "args" };

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

	private static void cd(String path) {
		System.setProperty("user.dir", path);
	}

	public static void main(String[] args) {
		pwd();
		cd("E:/");
		pwd();
		ls();

	}

}
