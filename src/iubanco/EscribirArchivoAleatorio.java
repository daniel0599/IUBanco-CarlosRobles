package iubanco;

// EscribirArchivoAleatorio.java *** *** VENTANA ***
// Este programa utiliza campos de texto para obtener informacion del usuario mediante el 
// teclado, y escribe la informacion en un archivo de acceso aleatorio.

import iubanco.RegistroCuentasAccesoAleatorio;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;



public class EscribirArchivoAleatorio extends JFrame
{   
private static final long serialVersionUID = 1L;
private RandomAccessFile salida;
   private IUBanco interfazUsuario;
   private JButton botonEntrar, botonAbrir;
   
   private static final int NUMERO_REGISTROS = 100;
  
   // configurar GUI
   public EscribirArchivoAleatorio()
   {
      super( "Escribir en archivo de acceso aleatorio" );

      // crear instancia de la interfaz de usuario reutilizable IUBanco
      interfazUsuario = new IUBanco( 4 );  // cuatro campos de texto
      getContentPane().add( interfazUsuario,
         BorderLayout.CENTER );

      // obtener referencia al bot�n de tarea generico hacerTarea1 en IUBanco
      botonAbrir = interfazUsuario.obtenerBotonHacerTarea1();
      botonAbrir.setText( "Abrir..." );

      // registrar componente de escucha para llamar a abrirArchivo cuando se oprima el bot�n
      botonAbrir.addActionListener(

         // clase interna anonima para manejar evento de botonAbrir
         new ActionListener() {

            // permitir al usuario seleccionar el archivo a abrir
            public void actionPerformed( ActionEvent evento )
            {
               abrirArchivo();
            }

         } // fin de la clase interna anonima

      ); // fin de la llamada a addActionListener

      // registrar componente de escucha de ventana para el evento de cierre de ventana
      addWindowListener(

         // clase interna anonima para manejar evento windowClosing
         new WindowAdapter() {

            // agregar registro en la GUI, despues cerrar el archivo
            public void windowClosing( WindowEvent evento )
            {
               if ( salida != null ) 
                  agregarRegistro();

               cerrarArchivo();
            }

         } // fin de la clase interna anonima

      ); // fin de la llamada a addWindowListener

      // obtener referencia al boton de tarea generico hacerTarea2 en IUBanco
      botonEntrar = interfazUsuario.obtenerBotonHacerTarea2();
      botonEntrar.setText( "Introducir" );
      botonEntrar.setEnabled( false );

      // registrar componente de escucha para llamar a agregarRegistro cuando se oprima el boton
      botonEntrar.addActionListener(

         // clase interna anonima para manejar evento de botonEntrar
         new ActionListener() {

            // agregar registro al archivo
            public void actionPerformed( ActionEvent evento )
            {
               agregarRegistro();
            }

         } // fin de la clase interna anonima

      ); // fin de la llamada a addActionListener

      setSize( 300, 150 );
      setVisible( true );  
   }

   // permitir al usuario seleccionar el archivo a abrir
   private void abrirArchivo()
   {
      // mostrar cuadro de dialogo para que el usuario pueda seleccionar el archivo
      JFileChooser selectorArchivo = new JFileChooser();
      selectorArchivo.setFileSelectionMode( JFileChooser.FILES_ONLY );

      int resultado = selectorArchivo.showOpenDialog( this );
     
      // si el usuario hizo clic en el boton Cancelar del cuadro de dialogo, regresar
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
            salida = new RandomAccessFile( nombreArchivo, "rw" );
            botonEntrar.setEnabled( true );
            botonAbrir.setEnabled( false );
         }

         // procesar excepcion que puede ocurrir al abrir el archivo
         catch ( IOException excepcionES ) {
            JOptionPane.showMessageDialog( this, "El archivo no existe",
               "Nombre de archivo incorrecto", JOptionPane.ERROR_MESSAGE );
         } 
         
      } // fin de instruccion else

   } // fin del metodo abrirArchivo

   // cerrar el archivo y terminar la aplicacion
   private void cerrarArchivo() 
   {
      // cerrar el archivo y salir
      try {
         if ( salida != null )
            salida.close();

         System.exit( 0 );
      }

      // procesar excepcion que puede ocurrir al cerrar el archivo
      catch( IOException excepcionES ) {
         JOptionPane.showMessageDialog( this, "Error al cerrar el archivo",
            "Error", JOptionPane.ERROR_MESSAGE );

         System.exit( 1 );
      }

   } // fin del metodo cerrarArchivo

   // agregar un registro al archivo
   private void agregarRegistro()
   {
      String campos[] = interfazUsuario.obtenerValoresCampos();
      
      // asegurarse que el campo cuenta tenga un valor
      if ( ! campos[ IUBanco.CUENTA ].equals( "" ) ) {
      
         // escribir valores en el archivo
         try {
            int numeroCuenta = Integer.parseInt( campos[ IUBanco.CUENTA ] );

            if ( numeroCuenta > 0 && numeroCuenta <= NUMERO_REGISTROS ) { 
               RegistroCuentasAccesoAleatorio registro =
		  new RegistroCuentasAccesoAleatorio();

               registro.establecerCuenta( numeroCuenta );
      
               registro.establecerPrimerNombre( campos[ IUBanco.PRIMERNOMBRE ] );
               registro.establecerApellidoPaterno( campos[ IUBanco.APELLIDO ] );
               registro.establecerSaldo( Double.parseDouble(
                  campos[ IUBanco.SALDO ] ) );

               salida.seek( ( numeroCuenta - 1 ) *
                  RegistroCuentasAccesoAleatorio.TAMANIO );
               registro.escribir( salida );
            }
            else
            {
                JOptionPane.showMessageDialog( this,
                   "El numero de cuenta debe ser entre 0 y 100",
                    "Numero de cuenta incorrecto", JOptionPane.ERROR_MESSAGE );
            }
            
            interfazUsuario.borrarCampos();  // borrar el contenido de los campos de texto

         } // fin del bloque try

         // procesar formato incorrecto de numero de cuenta o saldo
         catch ( NumberFormatException excepcionFormato ) {
                JOptionPane.showMessageDialog( this,
                   "Numero de cuenta o saldo incorrectos",
                   "Formato de numero incorrecto", JOptionPane.ERROR_MESSAGE );
         }

         // procesar excepciones que pueden ocurrir al escribir en el archivo
         catch ( IOException excepcionES ) {
             JOptionPane.showMessageDialog( this,
                "Error al escribir en el archivo", "Excepcion de ES",
                JOptionPane.ERROR_MESSAGE );
            cerrarArchivo();
         }

      } // fin de instruccion if

   } // fin del metodo agregarRegistro
   
   public static void main( String args[] )
   {
      new EscribirArchivoAleatorio();
   }

} // fin de la clase EscribirArchivoAleatorio

