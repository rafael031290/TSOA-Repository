/*
 * Nombre: Rafael Castillo Amaral
 * Codigo: 005165318
 * Seccion: D03
 * Modificado: Prac1
 */

package sistemaDistribuido.sistema.clienteServidor.modoUsuario;

import sistemaDistribuido.sistema.clienteServidor.modoMonitor.Nucleo;
import sistemaDistribuido.sistema.clienteServidor.modoUsuario.Proceso;
import sistemaDistribuido.util.Escribano;


public class ProcesoCliente extends Proceso{
	String messag = "";
	String actio = "";

	public ProcesoCliente(Escribano esc){
		super(esc);
		start();
	}

	public void run(){
		imprimeln("EL PROCESO CLIENTE SE ESTA EJECUTANDO.");
		imprimeln("EN ESPERA DE PETICION.");
		Nucleo.suspenderProceso();
		//imprimeln("Hola =)");
		byte[] solCliente=new byte[1024];
		byte[] respCliente=new byte[1024];
		//byte dato;
		imprimeln("CREANDO MENSAJE PARA ENVIAR.");
		requ(solCliente);
		imprimeln("SE HA SENALADO AL NUCLEO PARA ENVIO.");
		//solCliente[0]=(byte)10;
		Nucleo.send(248,solCliente);
		Nucleo.receive(dameID(),respCliente);
		//dato=respCliente[0];
		//imprimeln("el servidor me envió un "+dato);
		imprimeln("EL SERVIDOR HA RESPINDO");
		imprimeln("RESPUESTA DE SERVIDOR PROCESANDOSE");
		imprimeln("EL SERVIDOR RESPONDIO: "+respData(respCliente)+".");
	}
	public void actio(String actio){
		this.actio = actio;
	}
	public void messag (String messag){
		this.messag = messag;
	}
	public void requ(byte[] solCliente){
		byte[] term;
		int ent;
		if(actio.equals("Crear"))
			solCliente[8] = (byte)1;
		else if(actio.equals("Eliminar"))
			solCliente[8] = (byte)2;
		else if(actio.equals("Leer"))
			solCliente[8] = (byte)3;
		else if(actio.equals("Escribir"))
			solCliente[8] = (byte)4;
		term = messag.getBytes();
		for(ent=0; ent<messag.length(); ent++){
			solCliente[ent+9] = term[ent];
		}
	}
	public String respData(byte[] bytes){
		int ente;
		String linkee = "";
		for(ente=9; ente<bytes.length; ente++){
			char shar = (char)bytes[ente];
			linkee += Character.toString(shar);
		}
	return linkee;
	}
	                                  
}
