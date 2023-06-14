package bd;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Direccionador  {

	/**
	 * 
	 */
	public static final String PATH  = "C:/FacturaLista/Instaladores/";
	
	private static Direccionador instance = null;

	protected Direccionador() {
	 }
	 public static Direccionador getInstance() {
	    if(instance == null) {
	       instance = new Direccionador();
	    }
	    return instance;
	 }
	 
	 public Properties getArchivoConfiguracion(){
		 try {
			File entrada = new File(PATH+"configuracion.txt");
			//System.out.println(PATH+"configuracion.txt");
			FileInputStream f = new FileInputStream(entrada);
			Properties propiedades = new Properties();
			propiedades.load(f);
			f.close();
			return propiedades;
		} catch (FileNotFoundException e) {
			//System.out.println("Error en tomando archivo de configuracion");
			e.printStackTrace();
			return null;		
		} catch (IOException e) {
			//System.out.println("Error en tomando archivo de configuracion");
			e.printStackTrace();
			return null;
		}
			
	 }
}
	 
