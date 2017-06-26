package iubanco;
// CrearArchivoSecuencial.java *** VENTANA ***
// Escribir objetos secuencialmente en un archivo, mediante la clase  ObjectOutputStream.
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CrearArchivoSecuencial extends JFrame
{
private static final long serialVersionUID = 1L;
private ObjectOutputStream salida;
   private IUBanco interfazUsuario;
   private JButton botonIntro, botonAbrir;

   // configurar GUI
   public CrearArchivoSecuencial()
   {
      super( "Creacion de un archivo secuencial de objetos" );

      // crear instancia de interfaz de usuario reutilizable
      interfazUsuario = new IUBanco( 4 );  // cuatro campos de texto
      getContentPane().add( interfazUsuario, BorderLayout.CENTER );
      
      // configurar boton hacerTarea1 para usarlo en este programa
      botonAbrir = interfazUsuario.obtenerBotonHacerTarea1();
      botonAbrir.setText( "Guardar en archivo ..." );

      // registrar componente de escucha para llamar a abrirArchivo cuando se oprima el boton
      botonAbrir.addActionListener(

         // clase interna anonima para manejar evento de botonAbrir
         new ActionListener() {

            // llamar a abrirArchivo cuando se oprima el boton
            public void actionPerformed( ActionEvent evento )
            {
               abrirArchivo();
            }

         } // fin de la clase interna anonima

      ); // fin de la llamada a addActionListener

      // configurar boton hacerTarea2 para usarlo en este programa
      botonIntro = interfazUsuario.obtenerBotonHacerTarea2();
      botonIntro.setText( "Introducir" );
      botonIntro.setEnabled( false );  // deshabilitar boton

      // registrar componente de escucha para llamar a agregarRegistro cuando se oprima el boton
      botonIntro.addActionListener(

         // clase interna anonima para manejar evento de botonIntro
         new ActionListener() {

            // llamar a agregarRegistro cuando se oprima el boton
            public void actionPerformed( ActionEvent evento )
            {
               agregarRegistro();
            }

         } // fin de la clase interna anonima

      ); // fin de la llamada a addActionListener

      // registrar componente de escucha de ventana para manejar evento de cierre de ventana
      addWindowListener(

         // clase interna anonima para manejar evento windowClosing
         new WindowAdapter() {

            // agregar registro actual en la GUI al archivo, despues cerrar el archivo
            public void windowClosing( WindowEvent evento )
            {
               if ( salida != null )
                  agregarRegistro();

               cerrarArchivo();
            }

         } // fin de la clase interna anonima

      ); // fin de la llamada a addWindowListener

      setSize( 300, 200 );
      setVisible( true );

   } // fin del constructor de CrearArchivoSecuencial

   // permitir al usuario especificar el nombre del archivo
   private void abrirArchivo()
   {
      // mostrar cuadro de dialogo de archivo, para que el usuario pueda elegir el archivo a abrir
      JFileChooser selectorArchivo = new JFileChooser();
      selectorArchivo.setFileSelectionMode( JFileChooser.FILES_ONLY );
      
      int resultado = selectorArchivo.showSaveDialog( this );

      // si el usuario hizo clic en el boton Cancelar del cuadro de dialogo, regresar
      if ( resultado == JFileChooser.CANCEL_OPTION )
         return;

      File nombreArchivo = selectorArchivo.getSelectedFile(); // obtener archivo seleccionado
      
      // mostrar error si es invalido
      if ( nombreArchivo == null || nombreArchivo.getName().equals( "" ) )
         JOptionPane.showMessageDialog( this, "Nombre de archivo invalido", 
            "Nombre de archivo invalido", JOptionPane.ERROR_MESSAGE );

      else {

         // abrir archivo
         try {
            salida = new ObjectOutputStream(
               new FileOutputStream( nombreArchivo ) );

            botonAbrir.setEnabled( false );
            botonIntro.setEnabled( true );
         }

         // procesar excepciones que pueden ocurrir al abrir el archivo
         catch ( IOException excepcionES ) {
            JOptionPane.showMessageDialog( this, "Error al abrir el archivo", 
               "Error", JOptionPane.ERROR_MESSAGE );
         }  
         
      } // fin de instruccion else
  
   } // fin del metodo abrirArchivo

   // cerrar archivo y terminar la aplicacion 
   private void cerrarArchivo() 
   {
      // cerrar el archivo 
      try {
         salida.close();
         System.exit( 0 );
      }

      // procesar excepciones que pueden ocurrir al cerrar el archivo 
      catch( IOException excepcionES ) {
         JOptionPane.showMessageDialog( this, "Error al cerrar el archivo", 
            "Error", JOptionPane.ERROR_MESSAGE );
         System.exit( 1 );
      }

   } // fin del metodo cerrarArchivo

   // agregar registro al archivo
   public void agregarRegistro()
   {
      int numeroCuenta = 0;
      RegistroCuentas registro;
      String valoresCampos[] = interfazUsuario.obtenerValoresCampos();
     
      // si el valor del campo cuenta no esta vacio
      if ( ! valoresCampos[ IUBanco.CUENTA ].equals( "" ) ) {

         // escribir valores en el archivo
         try {
            numeroCuenta = Integer.parseInt(
               valoresCampos[ IUBanco.CUENTA ] );

            if ( numeroCuenta > 0 ) {

               // crear nuevo registro
               registro = new RegistroCuentas( numeroCuenta,
                  valoresCampos[ IUBanco.PRIMERNOMBRE ],
                  valoresCampos[ IUBanco.APELLIDO ],
                  Double.parseDouble( valoresCampos[ IUBanco.SALDO ] ) );

               // escribir el registro y vaciar el bufer
               salida.writeObject( registro );
               salida.flush();
            }
            else
            {
                JOptionPane.showMessageDialog( this,
                   "El numero de cuenta debe ser mayor que 0",
                   "Numero de cuenta incorrecto", JOptionPane.ERROR_MESSAGE );
            }

            // borrar campos de texto
            interfazUsuario.borrarCampos();

         } // fin de bloque try

         // procesar formato invalido de numero de cuenta o saldo
         catch ( NumberFormatException excepcionFormato ) {
            JOptionPane.showMessageDialog( this,
               "Numero de cuenta o saldo incorrecto", "Formato de numero incorrecto",
               JOptionPane.ERROR_MESSAGE );
         }

         // procesar excepciones que pueden ocurrir al escribir en el archivo
         catch ( IOException excepcionES ) {
             JOptionPane.showMessageDialog( this, "Error al escribir en el archivo",
                "Excepcion de ES", JOptionPane.ERROR_MESSAGE );
            cerrarArchivo();
         }

      } // fin de instruccion if

   } // fin del metodo agregarRegistro

   public static void main( String args[] )
   {
      new CrearArchivoSecuencial();
   }

} // fin de la clase CrearArchivoSecuencial