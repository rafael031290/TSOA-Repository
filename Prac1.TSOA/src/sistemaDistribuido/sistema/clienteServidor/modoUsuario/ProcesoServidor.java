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
import sistemaDistribuido.util.Pausador;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class ProcesoServidor extends Proceso{

	String filework,datafile;
	
	public ProcesoServidor(Escribano esc){
		super(esc);
		start();
	}

	public void run(){
		imprimeln("PROCESO SERVIDOR EJECUTANDOSE.");
		byte[] solServidor = new byte[1024];
		byte[] respServidor = new byte[1024];
		File filew;
		//byte dato;
		while(continuar()){
			imprimeln("EN ESPERA DE PETICION.");
			Nucleo.receive(dameID(),solServidor);
			//dato=solServidor[0];
			imprimeln("PETICION DE CLIENTE PROCESANDOSE");
			conten(solServidor);
			//respServidor=new byte[20];
			//respServidor[0]=(byte)(dato*dato);
			
			switch(solServidor[8]){
			case 1:
				/*Para crear se escribre el nombre y extension del archivo.
				 * Ejemplo: PRUBEA.ASM
				 */
				imprimeln("SOLICITUD DE CLIENTE CREAR:"+filework+".");
				filew = new File(filework);
				if(!filew.exists()){
					try{
						filew.createNewFile();
						respServidor = byarra("SE HA CREDO EL ARCHIVO: "+filework+" SE HA CREDO.");
					}
					catch(IOException excep) {
						respServidor = byarra("NO SE HA PODIDO CREAR EL ARCHIVO.");
					}
				}
					else
						respServidor = byarra("EL ARCHIVO: "+filework+", YA EXISTE.");
					break;
					
			case 2:
				/*Para eliminar se escribre el nombre y extension del archivo.
				 * Ejemplo: PRUBEA.ASM
				 */
				imprimeln("SOLICITUD DE CLIENTE ELIMINAR: "+filework+".");
				filew = new File(filework);
				if(filew.delete())
					respServidor = byarra("EL ARCHIVO SE HA ELIMINADO: "+filework+".");
					else
						respServidor = byarra("NO SE HA PODIDO ELIMINAR EL ARCHIVO: "+filework+".");
				break;
				
			case 3:
				/*Para leer se escribe el nombre y extension del archivo.
				 * Ejemplo: PRUEBA.ASM
				 */
				imprimeln("SOLICITUD DE CLINETE LEER: "+filework+".");
				filew = new File(filework);
				String readd = "";
				try {
					BufferedReader readf = new BufferedReader(new FileReader(filew));
					int liner = 0;
					try{
						readd = "EL ARCHIVO HA SIDO LEIDO: ";
						while(liner != -1){
							liner = readf.read();
							char shar = (char) liner;
							if(liner !=-1)
								readd += Character.toString(shar);
						}
						readf.close();
						respServidor = byarra(readd);
					}
					catch (IOException excep){
						respServidor = byarra("NO SE HA PODIDO LEER EL ARCHIVO");
						excep.printStackTrace();
					}
				}
				catch (FileNotFoundException except){
					respServidor = byarra("EL ARCHIVO NO EXISTE");
					except.printStackTrace();
				}
				break;
					
			case 4:
				/*Para escribir en el archivo se escribe el nombre y extension del archivo, seguido de un '|' (PIPE)
				 * y a continuacion lo que se desea escribir.
				 * Ejemplo: PRUEBA.ASM|ADCA 15; suma.
				 */
				imprimeln("SOLICITUD DE CLIENTE ESCRIBIR EN "+filework+":"+datafile+".");
				filew = new File(filework);
				if(filew.exists()){
					try{
						PrintWriter wrifile = new PrintWriter(new FileWriter(filew));
						wrifile.print(datafile);
						wrifile.close();
						respServidor = byarra("SE HA ESCRITO EN: "+filework+"");
						}
					catch(IOException excep){
						respServidor = byarra("NO SE HA PODIRO ESCRIBIR EN: "+filework+".");
						excep.printStackTrace();
					}
				}
				else 
					respServidor = byarra("EL ARCHIVO NO EXISTE");
			break;
				
				
				
				}
			imprimeln("ALISTANDO MENSAJE PARA ENVIARSE.");
			Pausador.pausa(1000);  //sin esta línea es posible que Servidor solicite send antes que Cliente solicite receive
			imprimeln("SE HA SENALADO AL NUCLEO PARA ENVIO.");
			imprimeln("RESPUESTA ENVIADA.");
			Nucleo.send(0,respServidor);
		}
	}
	public void conten(byte[] solServidor){
		int ent = 9;
		char shar;
		filework = "";
		datafile = "";
		
		do{
			shar = (char)solServidor[ent];
			if(shar !='|')
				filework += Character.toString(shar);
				ent++;
		}
		while(ent < solServidor.length && shar != '|');
			while(ent < solServidor.length){
				shar = (char)solServidor[ent];
				datafile += Character.toString(shar);
				ent++;
			}
	}
	
	public byte[] byarra(String linke){
		byte[] bytes = new byte[1024];
		byte[] term;
		int ente;
		term = linke.getBytes();
		for(ente=0; ente<linke.length() && ente<1015; ente++){
			bytes[ente+9] = term[ente];
		}
		return bytes;
	}

}
