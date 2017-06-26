package iubanco.TestSuite.TestCase_Unidad;

import static org.junit.Assert.*;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import iubanco.RegistroCuentas;
import org.junit.Test;

public class ConsultaCreditosTest {

	String tipoC1="Saldos con creditos";
	String tipoC2="Saldos con debito";
	ArrayList<RegistroCuentas> registros= new ArrayList<RegistroCuentas>();
	
    private boolean debeMostrarse( double saldo )
    {
        if ( tipoC1.equals( "Saldos con credito" ) && saldo < 0 )
           return true;

        else if ( tipoC2.equals( "Saldos con debito" ) && saldo > 0 )
           return true;
//        else if ( tipoCuenta.equals( "Saldos en ceros" ) && saldo == 0 )
//           return true;
        return false;
     }
   
    private void cargar () throws IOException, ClassNotFoundException{
    	ObjectInputStream entrada = null;  
    	entrada = new ObjectInputStream(new FileInputStream( "Secuencial.txt"));
    	  	
    	for(int i=0; i<1; i++){
    		RegistroCuentas registro = (RegistroCuentas) entrada.readObject();
     		registros.add(registro);
    		System.out.println("El nombre es"+registro.obtenerPrimerNombre()+registro.obtenerApellidoPaterno());
			System.out.println("El saldo es"+registro.obtenerSaldo());
			
    	}
    	entrada.close();
    }
    
    private RegistroCuentas cargarobjeto(int NtipoCuenta){
    	RegistroCuentas objeto = new RegistroCuentas();
    	for(RegistroCuentas i:registros){
    		if(NtipoCuenta==1 && i.obtenerSaldo()<0)
    		{
    			System.out.println("El nombre es"+i.obtenerPrimerNombre());
    			System.out.println("El saldo es   "+i.obtenerSaldo());
    			objeto=i;
    			return objeto;
    		}else if(NtipoCuenta==2 && i.obtenerSaldo()>0){
    			System.out.println("El nombre es"+i.obtenerPrimerNombre());
    			System.out.println("El saldo es   "+i.obtenerSaldo());
    			objeto=i;
    			return objeto;
    		}
    	}
		return objeto;
    }
	
	@Test
	public void test() throws IOException, ClassNotFoundException {
		
		int saldodebito =2;
		cargar();
		assertTrue(this.debeMostrarse(this.cargarobjeto(saldodebito).obtenerSaldo()));
		
	}

}
