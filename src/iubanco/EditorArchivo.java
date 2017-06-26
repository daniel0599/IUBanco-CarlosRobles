package iubanco;
// EditorArchivo.java
// Esta clase declara metodos para manipular los registros de una
// cuenta de banco en un archivo de acceso aleatorio.
import java.io.*;

public class EditorArchivo {
   
   RandomAccessFile archivo; // referencia al archivo
   
   // abrir el archivo
   public EditorArchivo( File nombreArchivo ) throws IOException
   {
      archivo = new RandomAccessFile( nombreArchivo, "rw" );
   }
   
   // cerrar el archivo
   public void cerrarArchivo() throws IOException
   {
      if ( archivo != null )
         archivo.close();
   }
   
   // obtener un registro del archivo
   public RegistroCuentasAccesoAleatorio obtenerRegistro( int numeroCuenta )
      throws IllegalArgumentException, NumberFormatException, IOException
   {
      RegistroCuentasAccesoAleatorio registro = new RegistroCuentasAccesoAleatorio();

      if ( numeroCuenta < 1 || numeroCuenta > 100 )
         throw new IllegalArgumentException( "Fuera de rango" );

      // buscar registro apropiado en el archivo
      archivo.seek( ( numeroCuenta - 1 ) * RegistroCuentasAccesoAleatorio.TAMANIO );
      
      registro.leer( archivo );

      return registro;

   } // fin del metodo obtenerRegistro
   
   // actualizar registro en el archivo
   public void actualizarRegistro( int numeroCuenta, String primerNombre, 
      String apellidoPaterno, double saldo )
      throws IllegalArgumentException, IOException
   {
      RegistroCuentasAccesoAleatorio registro = obtenerRegistro( numeroCuenta );
      if ( numeroCuenta == 0 )
         throw new IllegalArgumentException( "La cuenta no existe" );

      // buscar registro apropiado en el archivo
      archivo.seek( ( numeroCuenta - 1 ) * RegistroCuentasAccesoAleatorio.TAMANIO );   

      registro = new RegistroCuentasAccesoAleatorio( numeroCuenta,
         primerNombre, apellidoPaterno, saldo );
         
      registro.escribir( archivo ); // escribir registro actualizado en el archivo
      
   } // fin del metodo actualizarRegistro
   
   // agregar registro al archivo
   public void nuevoRegistro( int numeroCuenta, String primerNombre, 
      String apellidoPaterno, double saldo )
      throws IllegalArgumentException, IOException
   {
      RegistroCuentasAccesoAleatorio registro = obtenerRegistro( numeroCuenta );
      
      if ( registro.obtenerCuenta() != 0 )
         throw new IllegalArgumentException( "La cuenta ya existe" );

       // buscar registro apropiado en el archivo
      archivo.seek( ( numeroCuenta - 1 ) * RegistroCuentasAccesoAleatorio.TAMANIO );   

      registro = new RegistroCuentasAccesoAleatorio( numeroCuenta, 
         primerNombre, apellidoPaterno, saldo );
         
      registro.escribir( archivo ); // escribir registro en el archivo
      
   } // fin del metodo nuevoRegistro
   
   // eliminar registro del archivo
   public void eliminarRegistro( int numeroCuenta )
      throws IllegalArgumentException, IOException
   {
      RegistroCuentasAccesoAleatorio registro = obtenerRegistro( numeroCuenta );
      
      if ( registro.obtenerCuenta() == 0 )
         throw new IllegalArgumentException( "La cuenta no existe" );
      
      // buscar registro apropiado en el archivo
      archivo.seek( ( numeroCuenta - 1 ) * RegistroCuentasAccesoAleatorio.TAMANIO );

      // crear un registro en blanco para escribir en el archivo
      registro = new RegistroCuentasAccesoAleatorio();
      registro.escribir( archivo );
      
   } // fin del metodo eliminarRegistro
   
} // fin de la clase EditorArchivo