package iubanco.TestSuite.TestCase_Unidad;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Test;

public class ProcesadorTransaccionesTest {
   
	private RandomAccessFile archivo=null;
	
	private int nCuenta = 1;
    private String nombre = "Carlos";
    private String apellido = "Robles";
    private double saldo=500;
    
	private String ObtenerNombre (RandomAccessFile archivo) throws IOException{
		
		char nombre[] = new char[15], temp;
		for(int cuenta=0; cuenta<nombre.length; cuenta++){
			temp= archivo.readChar();
			nombre[cuenta]= temp;
		}
		
		return new String(nombre).replace('\0', ' ');
		
	}
	@Test
	public void test() {

		int nCuentaAux;
		String nombreAux;
		String ApellidoAux;
		double SaldoAux;
		
		try{
			this.archivo = new RandomAccessFile("archivo.txt", "rw");
			this.archivo.seek(nCuenta -1);
			nCuentaAux = this.archivo.readInt();
			nombreAux = this.ObtenerNombre(archivo);
			ApellidoAux = this.ObtenerNombre(archivo);
			SaldoAux = this.archivo.readDouble();
			
			if(this.nCuenta==nCuentaAux){
				assertTrue(true);
			}else{
				fail("Los numeros no son iguales");
			}
			assertEquals(this.nombre.trim(), nombreAux.trim());
			assertEquals(this.apellido.trim(), ApellidoAux.trim());
			
			if(this.saldo == SaldoAux){
				assertTrue(true);
			}else{
				fail("Los Saldos no son iguales");
			}
			
			
		    	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
	}

}
