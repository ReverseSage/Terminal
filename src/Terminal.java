
public class CLI {

	/**
	 * @param args
	 */
	private static void date() {
		// E : for day (Sun Mon Thu ..)
		// getTime() returns also Time zone like 'EET'
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss E");
		Calendar cal = Calendar.getInstance();
		System.out.println(dateFormat.format(cal.getTime()));
	}
	public static void main(String[] args) {
		
		
		files x = new files();
		
		
		
				
	}	
}
