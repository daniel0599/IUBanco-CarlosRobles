package iubanco;
// LeerArchivoAleatorio.java 
// Este programa lee un archivo de acceso aleatorio en forma secuencial y
// muestra el contenido un registro a la vez, en campos de texto.
import iubanco.RegistroCuentasAccesoAleatorio;
import java.awt.event.*;
import java.io.*;
//import java.text.DecimalFormat;
import javax.swing.*;

public class LeerArchivoAleatorio extends JFrame
{
private static final long serialVersionUID = 1L;
private IUBanco interfazUsuario;
   private RandomAccessFile entrada;  
   private JButton botonSiguiente, botonAbrir;
   
   //private static DecimalFormat dosDigitos = new DecimalFormat( "0.00" );
   
   // configurar GUI
   public LeerArchivoAleatorio()
   {
      super( "Leer archivo del cliente" );

      // crear instancia de interfaz de usuario reutilizable
      interfazUsuario = new IUBanco( 4 );  // cuatro campos de texto
      getContentPane().add( interfazUsuario );

      // configurar bot�n hacerTarea1 generico de IUBanco
      botonAbrir = interfazUsuario.obtenerBotonHacerTarea1();
      botonAbrir.setText( "Abrir archivo para leer..." );

      // registrar componente de escucha para llamar a archivoAbrir cuando se oprima el boton
      botonAbrir.addActionListener(

         // clase interna an�nima para manejar evento de botonAbrir
         new ActionListener() {

            // permitir al usuario seleccionar el archivo a abrir
            public void actionPerformed( ActionEvent evento )
            {      
               abrirArchivo();
            }

         } // fin de la clase interna an�nima  

      ); // fin de la llamada a addActionListener  

      // configurar bot�n hacerTarea2 gen�rico de IUBanco
      botonSiguiente = interfazUsuario.obtenerBotonHacerTarea2();
      botonSiguiente.setText( "Siguiente" );
      botonSiguiente.setEnabled( false );

      // registrar componente de escucha para llamar a leerRegistro cuando se oprima el bot�n
      botonSiguiente.addActionListener(

         // clase interna an�nima para manejar evento de botonSiguiente
         new ActionListener() {

            // leer un registro cuando el usuario haga clic en botonSiguiente
            public void actionPerformed( ActionEvent evento )
            {
               leerRegistro();
            }

         } // fin de la clase interna an�nima

      ); // fin de la llamada a addActionListener
   
      // registrar componente de escucha para evento de cierre de ventana
      addWindowListener(

         // clase interna an�nima para manejar evento windowClosing
         new WindowAdapter() {

            // cerrar archivo y terminar la aplicaci�n
            public void windowClosing( WindowEvent evento )
            {
               cerrarArchivo();
            }

         } // fin de la clase interna an�nima

      ); // fin de la llamada a addWindowListener

      setSize( 300, 150 );
      setVisible( true );  

   } // fin del constructor
   
   // permitir al usuario seleccionar el archivo a abrir
   private void abrirArchivo()
   {
      // mostrar cuadro de di�logo para que el usuario pueda seleccionar el archivo
      JFileChooser selectorArchivo = new JFileChooser();
      selectorArchivo.setFileSelectionMode( JFileChooser.FILES_ONLY );

      int resultado = selectorArchivo.showOpenDialog( this );
   
      // si el usuario hizo clic en el bot�n Cancelar del cuadro de di�logo, regresar
      if ( resultado == JFileChooser.CANCEL_OPTION )
         return;

      // obtener el archivo seleccionado
      File nombreArchivo = selectorArchivo.getSelectedFile();

      // mostrar error si el nombre de archivo es incorrecto
      if ( nombreArchivo == null || nombreArchivo.getName().equals( "" ) )
         JOptionPane.showMessageDialog( this, "Nombre de archivo incorrecto", 
            "Nombre de archivo incorrecto", JOptionPane.ERROR_MESSAGE );

      else {

         // abrir el archivo
         try {
            entrada = new RandomAccessFile( nombreArchivo, "r" );
            botonSiguiente.setEnabled( true );
            botonAbrir.setEnabled( false );
         }

         // atrapar excepci�n que puede ocurrir al abrir el archivo
         catch ( IOException ioException ) {
            JOptionPane.showMessageDialog( this, "El archivo no existe", 
               "Nombre de archivo incorrecto", JOptionPane.ERROR_MESSAGE );
         }   
         
      } // fin de instrucci�n else

   } // fin del m�todo abrirArchivo
   
   // leer un registro
   private void leerRegistro()
   {
      RegistroCuentasAccesoAleatorio registro = new RegistroCuentasAccesoAleatorio();
   
      // leer un registro y mostrarlo
      try {

         do {
            registro.leer( entrada );
         } while ( registro.obtenerCuenta() == 0 );

         String valores[] = { String.valueOf( registro.obtenerCuenta() ),
            registro.obtenerPrimerNombre(), registro.obtenerApellidoPaterno(),
            String.valueOf( registro.obtenerSaldo() ) };
         interfazUsuario.establecerValoresCampos( valores );
      }

      // cerrar el archivo al llegar a su fin
      catch ( EOFException excepcionEOF ) {
         JOptionPane.showMessageDialog( this, "No hay m�s registros",
            "Se lleg� al fin del archivo", JOptionPane.INFORMATION_MESSAGE );
         cerrarArchivo();
      }

      // procesar excepciones que pueden ocurrir por alg�n problema con el archivo
      catch ( IOException excepcionES ) {
         JOptionPane.showMessageDialog( this, "Error al leer el archivo", 
            "Error", JOptionPane.ERROR_MESSAGE );

         System.exit( 1 );
      }

   } // fin del m�todo leerRegistro
   
   // cerrar el archivo y terminar la aplicaci�n
   private void cerrarArchivo() 
   {
      // cerrar el archivo y salir
      try {
         if ( entrada != null )
            entrada.close();

         System.exit( 0 );
      }

      // procesar excepci�n que puede ocurrir al cerrar el archivo
      catch( IOException excepcionES ) {
         JOptionPane.showMessageDialog( this, "Error al cerrar el archivo",
            "Error", JOptionPane.ERROR_MESSAGE );

         System.exit( 1 );
      }

   } // fin del m�todo cerrarArchivo

   public static void main( String args[] )
   {
      new LeerArchivoAleatorio();
   }

} // fin de la clase LeerArchivoAleatorio

