import java.io.File;
public  class files {
	
	
	private int count = 1;
	public void pwd(){
		System.out.println(System.getProperty("user.dir"));
	}
	public void clear(){
		
	}
	
	public void mkdir(final String s){
		File f = new File(s);
		if(f.exists()){
			String h = s;
			System.out.println("Such a directory exists with same name");
			h +="(" + String.valueOf(count) + ")";
			f = new File(h);
			f.mkdir();
		}
		else f.mkdir();
		
	}
	
	
	
}
