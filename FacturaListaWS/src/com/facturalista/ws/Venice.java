package com.facturalista.ws;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import bd.DBDriverPostgreSQL;
import bd.DBDriverPostgreSQLOpera;
import bd.DBDriverPostgreSQLWS;
//import jdk.nashorn.internal.parser.JSONParser;
 
@Path("/Venice")
public class Venice {
    @GET
    @Path("/authors")
    @Produces(MediaType.APPLICATION_JSON)
    public List getTrackInJSON() {
        List listPerson = new ArrayList();
        Person p1 = new Person();
        p1.setId(1);
        p1.setName("Santiago");
        Person p2 = new Person();
        p2.setId(2);
        p2.setName("Rodrigo");
        listPerson.add(p1);
        listPerson.add(p2);
        return listPerson;
    }
     
	@GET
	@Path("/setFacturasNuevas/{z},{a},{b},{c},{d},{e}")
	@Produces({MediaType.APPLICATION_JSON})
	public String saludoJsonPP(@PathParam("z") String idHotel,@PathParam("a") Boolean bandera,@PathParam("b") String fecha,@PathParam("c") String puestoRecepcion, @PathParam("d") String operacion,@PathParam("e") String nroInterno) throws Exception {

		String respuesta=insertarBD(idHotel, bandera, fecha, puestoRecepcion, operacion, nroInterno);

		
		return "ID Hotel: "+idHotel+"\n"+"Bandera: "+bandera+"\n"+"Puesto de recepcion: "+puestoRecepcion+"\n"+"Fecha: "+fecha+"\n"+"Operacion: "+operacion+"\n"+"Nro interno venice: "+nroInterno+"\n"+"Respuesta web service: "+respuesta;
		//Boolean b = Boolean.valueOf(bandera);
		//actualizarBD(b);
		
		//return idHotel+", "+ b + ", "+puestoRecepcion+", "+fecha+", "+operacion+", "+nroInterno;

	}
	
	/*@POST
	@Path("/GetHrMsg/json_data")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void gethrmessage(RequestBody requestBody) {
	    System.out.println(requestBody.hello);
	    System.out.println(requestBody.foo);
	    System.out.println(requestBody.count);
	}*/
	
    @POST
    @Path("/setFacturasNuevasP")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response gethrmessage(InputStream incomingData) {
    	String retorno ="";
    	String jsonstring="";
        StringBuilder crunchifyBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line = null;
            while ((line = in.readLine()) != null) {
                crunchifyBuilder.append(line);
            }
        } catch (Exception e) {
            System.out.println("Error Parsing: - ");
        }
        System.out.println("Data Received: " + crunchifyBuilder.toString());
        try {
        	JSONParser parser = new JSONParser();
        	JSONObject json = (JSONObject) parser.parse(crunchifyBuilder.toString());
        	jsonstring = crunchifyBuilder.toString();
        	
        	
        	//json.replace('"', ' ');
			retorno = insertarBDOpera(jsonstring);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			retorno=e.getMessage();
			e.printStackTrace();
		}
        
        String retornoString = getString();

        // return HTTP response 200 in case of success
        return Response.status(200).entity(retorno + retornoString).build();
        //return retorno;
    }
	
	private String getString() {
		// TODO Auto-generated method stub
		String respuesta="";
		try {
		int contador = 0;
		DBDriverPostgreSQLOpera bd = null;
		bd = abrirConexionOpera(bd, contador);
		String consultaR1 = "select * from operawss";
		PreparedStatement psR1 = bd.prepareStatement(consultaR1);
		ResultSet rs = psR1.executeQuery();

		while(rs.next()) {
			respuesta=rs.getString(3);
		}
		cerrarConexionOpera(bd, 1);

		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			respuesta=e.getMessage();
		}
		return respuesta;	}

	private String insertarBDOpera(String json) throws Exception {
		// TODO Auto-generated method stub
		String retorno = "";
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");  
		String hoy = dateFormat.format(now);

		try {
		int contador = 0;
		DBDriverPostgreSQLOpera bd = null;
		bd = abrirConexionOpera(bd, contador);
		String consultaR1 = "insert into operawss (idllamado,idreserva, idhotel, json, fecha, procesado"
				+ ") values (default,'','','"+json+"','"+hoy+"','FALSE')";
		PreparedStatement psR1 = bd.prepareStatement(consultaR1);
		psR1.executeUpdate();
		cerrarConexionOpera(bd, 1);
		retorno="exito";

		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			retorno=e.getMessage();
		}
		return retorno;
	}
	/*@POST
	@Path("/setFacturasNuevasP")
	@Produces({MediaType.APPLICATION_JSON})
	public String saludoJsonP(RequestBody string) throws Exception {
		
		return "entre";

	    
		/*String body = null;
	    StringBuilder stringBuilder = new StringBuilder();
	    BufferedReader bufferedReader = null;

	    try {
	        InputStream inputStream = request.getInputStream();
	        if (inputStream != null) {
	            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	            char[] charBuffer = new char[128];
	            int bytesRead = -1;
	            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                stringBuilder.append(charBuffer, 0, bytesRead);
	            }
	        } else {
	            stringBuilder.append("");
	        }
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (bufferedReader != null) {
	            try {
	                bufferedReader.close();
	            } catch (IOException ex) {
	                throw ex;
	            }
	        }
	    }

	    body = stringBuilder.toString();
	    return body;*/
		
		/*JSONParser parser = new JSONParser();
        String[] response = new String[5];

        Object obj = parser.parse(text);
		JSONObject jsonObject = (JSONObject) obj;
		/*offerProcess ofc = new offerProcess();
		ofc.setLatitude((double) jsonObject.get("latitude"));
		ofc.setLongitude((double) jsonObject.get("longitude"));
		ofc.setIMSI((long) jsonObject.get("IMSI"));

		response = ofc.fetchOffers();
		//System.out.println(text);

		return "entre";
		
		/*Boolean b = Boolean.valueOf(bandera);
		actualizarBD(b);
		
		return bandera;

	}*/
	
	@GET
	@Path("/getFacturasNuevas/{i}")
	@Produces({MediaType.APPLICATION_JSON})
	public String saludoJsonPP(@PathParam("i") String idHotel) throws Exception {

		
		return idHotel;

	}

	private DBDriverPostgreSQL abrirConexion(DBDriverPostgreSQL bd, int contador) throws Exception {
		if (bd == null) {
			bd = new DBDriverPostgreSQL();
			bd.conectar();
			System.out.println("Abro conexion " + contador);
		}
		return bd;
	}

	private DBDriverPostgreSQL cerrarConexion(DBDriverPostgreSQL bd, int contador) throws Exception {
		if (bd != null) {
			bd.desconectar();
			System.out.println("Cerrar conexion " + contador);
		}
		return bd;
	}
	
	private DBDriverPostgreSQLWS abrirConexionWS(DBDriverPostgreSQLWS bd, int contador) throws Exception {
		if (bd == null) {
			bd = new DBDriverPostgreSQLWS();
			bd.conectar();
			System.out.println("Abro conexion " + contador);
		}
		return bd;
	}

	private DBDriverPostgreSQLWS cerrarConexionWS(DBDriverPostgreSQLWS bd, int contador) throws Exception {
		if (bd != null) {
			bd.desconectar();
			System.out.println("Cerrar conexion " + contador);
		}
		return bd;
	}
	
	private DBDriverPostgreSQLOpera abrirConexionOpera(DBDriverPostgreSQLOpera bd, int contador) throws Exception {
		if (bd == null) {
			bd = new DBDriverPostgreSQLOpera();
			bd.conectar();
			System.out.println("Abro conexion " + contador);
		}
		return bd;
	}

	private DBDriverPostgreSQLOpera cerrarConexionOpera(DBDriverPostgreSQLOpera bd, int contador) throws Exception {
		if (bd != null) {
			bd.desconectar();
			System.out.println("Cerrar conexion " + contador);
		}
		return bd;
	}
	
	private String actualizarBD(Boolean bandera) throws Exception {
		// TODO Auto-generated method stub
		String retorno = "";
		try {
		int contador = 0;
		DBDriverPostgreSQL bd = null;
		bd = abrirConexion(bd, contador);
		String consultaR1 = "update veniceWebService set bandera =?";
		PreparedStatement psR1 = bd.prepareStatement(consultaR1);
		psR1.setBoolean(1, bandera);
		psR1.executeUpdate();
		cerrarConexion(bd, 1);
		retorno="exito";
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			retorno=e.getMessage();
		}
		
		return retorno;
	}
	
	private String insertarBD(String idHotel, Boolean bandera, String fecha, String puestoRecepcion,
			String operacion,String nroVenice) throws Exception {
		// TODO Auto-generated method stub
		String retorno = "";

		try {
		int contador = 0;
		DBDriverPostgreSQLWS bd = null;
		bd = abrirConexionWS(bd, contador);
		String consultaR1 = "insert into venicewebservice (idllamado,idHotel, bandera, fecha, puestoRecepcion, operacion,"
				+ "nroInternoVenice) values (default,'"+idHotel+"','"+bandera+"','"+fecha+"','"+puestoRecepcion+"',"
						+ "'"+operacion+"','"+nroVenice+"')";
		PreparedStatement psR1 = bd.prepareStatement(consultaR1);
		psR1.executeUpdate();
		cerrarConexionWS(bd, 1);
		retorno="exito";

		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			retorno=e.getMessage();
		}
		return retorno;
	}

     
}