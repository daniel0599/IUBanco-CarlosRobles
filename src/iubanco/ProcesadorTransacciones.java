package iubanco;
// ProcesadorTransacciones.java
// Un programa para procesar transacciones utilizando archivos de acceso aleatorio.
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import iubanco.RegistroCuentasAccesoAleatorio;

public class ProcesadorTransacciones extends JFrame
{
   
   private static final long serialVersionUID = 1L;
   private IUBanco interfazUsuario;
   private JMenuItem elementoNuevo, elementoActualizar, elementoEliminar, elementoAbrir, elementoSalir;
   private JTextField campos[];
   private JTextField campoCuenta, campoTransaccion;
   private JButton botonAccion, botonCancelar;
   private EditorArchivo archivoDatos;
   //private RegistroCuentasAccesoAleatorio registro;
    
   public ProcesadorTransacciones()
   {
      super( "Procesador de transacciones" );
      
      // establecer escritorio, barra de menus y menu Archivo
      interfazUsuario = new IUBanco( 5 );
      getContentPane().add( interfazUsuario );
      interfazUsuario.setVisible( false );
      
      // establecer el boton de accion
      botonAccion = interfazUsuario.obtenerBotonHacerTarea1();
      botonAccion.setText( "Guardar cambios" );
      botonAccion.setEnabled( false );
      
      // registrar componente de escucha para el boton de accion
      botonAccion.addActionListener( 
      
         new ActionListener() { // clase interna anonima
            
            public void actionPerformed( ActionEvent evento )
            {
               String accion = evento.getActionCommand();
               realizarAccion( accion );
               
            } // fin del metodo actionPerformed
            
         } // fin de la clase interna anonima
      
      ); // fin de la llamada a addActionListener
      
      // establecer el boton para cancelar
      botonCancelar = interfazUsuario.obtenerBotonHacerTarea2();
      botonCancelar.setText( "Cancelar" );
      botonCancelar.setEnabled( false );
      
      // registrar componente de escucha para el boton de cancelacion
      botonCancelar.addActionListener( 
      
         new ActionListener() { // clase interna anonima
            
            // borrar los campos
            public void actionPerformed( ActionEvent evento ) 
            {
               interfazUsuario.borrarCampos();
            }
            
         } // fin de la clase interna anonima
      
      ); // fin de la llamada a addActionListener
      
      // establecer el componente de escucha para el campo cuenta
      campos = interfazUsuario.obtenerCampos();
      campoCuenta = campos[ IUBanco.CUENTA ];
      campoCuenta.addActionListener( 
      
         new ActionListener() { // clase interna anonima
            
            public void actionPerformed( ActionEvent evento )
            {
               mostrarRegistro( "0" );
            }
            
         } // fin de la clase interna anonima
      
      ); // fin de la llamada a addActionListener
      
      // crear referencia al campo transaccion
      campoTransaccion = campos[ IUBanco.TRANSACCION ];
      
      // registrar componente de escucha para campo transaccion
      campoTransaccion.addActionListener( 
      
         new ActionListener() { // clase interna anonima
            
            // actualizar los campos de la GUI
            public void actionPerformed( ActionEvent evento ) 
            {
               mostrarRegistro( campoTransaccion.getText() );
            }
            
         } // fin de la clase interna anonima
      
      ); // fin de la llamada a addActionListener

      JMenuBar barraMenus = new JMenuBar(); // establecer el menu
      setJMenuBar( barraMenus );

      JMenu menuArchivo = new JMenu( "Archivo" );
      barraMenus.add( menuArchivo );
      
      // establecer elemento de menu para agregar un registro
      elementoNuevo = new JMenuItem( "Nuevo registro" ); 
      elementoNuevo.setEnabled( false );
      
      // registrar componente de escucha de elemento para nuevo registro
      elementoNuevo.addActionListener( 
      
         new ActionListener() { // clase interna anonima
         
            public void actionPerformed( ActionEvent evento ) 
            {
            
               // configurar los campos de la GUI para su edicion
               campos[ IUBanco.CUENTA ].setEnabled( true );
               campos[ IUBanco.PRIMERNOMBRE ].setEnabled( true );
               campos[ IUBanco.APELLIDO ].setEnabled( true );
               campos[ IUBanco.SALDO ].setEnabled( true );
               campos[ IUBanco.TRANSACCION ].setEnabled( false );
               
               botonAccion.setEnabled( true );
               botonAccion.setText( "Crear" );
               botonCancelar.setEnabled( true );
            
               interfazUsuario.borrarCampos(); // restablecer los campos de texto
               
            } // fin del metodo actionPerformed
            
         } // fin de la clase interna anonima
      
      ); // fin de la llamada a addActionListener
      
      // establecer elemento de menu para actualizar un registro
      elementoActualizar = new JMenuItem( "Actualizar registro" );
      elementoActualizar.setEnabled( false );
      
      // registrar componente de escucha de elemento para actualizar
      elementoActualizar.addActionListener( 
      
         new ActionListener() { // clase interna anonima
            
            public void actionPerformed( ActionEvent evento ) 
            {
               // configurar los campos de la GUI para su edicion
               campos[ IUBanco.CUENTA ].setEnabled( true );
               campos[ IUBanco.PRIMERNOMBRE ].setEnabled( false );
               campos[ IUBanco.APELLIDO ].setEnabled( false );
               campos[ IUBanco.SALDO ].setEnabled( false );
               campos[ IUBanco.TRANSACCION ].setEnabled( true );
               
               botonAccion.setEnabled( true );
               botonAccion.setText( "Actualizar" );
               botonCancelar.setEnabled( true );
            
               interfazUsuario.borrarCampos(); // restablecer los campos de texto
               
            } // fin del metodo actionPerformed
            
         } // fin de la clase interna anonima
      
      ); // fin de la llamada a addActionListener
      
      // establecer elemento de menu para eliminar un registro
      elementoEliminar = new JMenuItem( "Eliminar registro" );
      elementoEliminar.setEnabled( false );
      
      // registrar componente de escucha para elemento eliminar
      elementoEliminar.addActionListener( 
      
         new ActionListener() { // clase interna anonima
            
            public void actionPerformed( ActionEvent evento ) 
            {
               // configurar los campos de la GUI para su edicion
               campos[ IUBanco.CUENTA ].setEnabled( true );
               campos[ IUBanco.PRIMERNOMBRE ].setEnabled( false );
               campos[ IUBanco.APELLIDO ].setEnabled( false );
               campos[ IUBanco.SALDO ].setEnabled( false );
               campos[ IUBanco.TRANSACCION ].setEnabled( false );
               
               botonAccion.setEnabled( true );
               botonAccion.setText( "Eliminar" );
               botonCancelar.setEnabled( true );
            
               interfazUsuario.borrarCampos(); // restablecer los campos de texto
               
            } // fin del metodo actionPerformed
            
         } // fin de la clase interna anonima
      
      ); // fin de la llamada a addActionListener

      // establecer elemento de menu para abrir archivo
      elementoAbrir = new JMenuItem( "Nuevo/abrir archivo" );
      
      // registrar componente de escucha para elemento abrir
      elementoAbrir.addActionListener( 
      
         new ActionListener() { // clase interna anonima
            
            public void actionPerformed( ActionEvent evento ) 
            {
               // tratar de abrir el archivo
               if ( !abrirArchivo() )
                  return;
            
               // establecer los elementos del menu
               elementoNuevo.setEnabled( true );
               elementoActualizar.setEnabled( true );
               elementoEliminar.setEnabled( true );
               elementoAbrir.setEnabled( false );
            
               // establecer la interfaz
               interfazUsuario.setVisible( true );
               campos[ IUBanco.CUENTA ].setEnabled( false );
               campos[ IUBanco.PRIMERNOMBRE ].setEnabled( false );
               campos[ IUBanco.APELLIDO ].setEnabled( false );
               campos[ IUBanco.SALDO ].setEnabled( false );
               campos[ IUBanco.TRANSACCION ].setEnabled( false );
               
            } // fin del metodo actionPerformed
            
         } // fin de la clase interna anonima
      
      ); // fin de la llamada a addActionListener
      
      // establecer elemento de menu para salir del programa
      elementoSalir = new JMenuItem( "Salir" );
      
      // registrar componente de escucha para elemento salir
      elementoSalir.addActionListener( 
      
         new ActionListener() { // clase interna anonima
         
            public void actionPerformed( ActionEvent evento ) 
            {
               try {
                  archivoDatos.cerrarArchivo(); // cerrar el archivo
               }
               
               catch ( IOException excepcionES ) {
                  JOptionPane.showMessageDialog( 
                     ProcesadorTransacciones.this, "Error al cerrar el archivo", 
                     "Error de ES", JOptionPane.ERROR_MESSAGE );
               }
               
               finally {
                  System.exit( 0 ); // salir del programa
               }
               
            } // fin del metodo actionPerformed
            
         } // fin de la clase interna anonima
      
      ); // fin de la llamada a addActionListener
        
      // adjuntar elementos de menu al menu Archivo
      menuArchivo.add( elementoAbrir );
      menuArchivo.add( elementoNuevo );
      menuArchivo.add( elementoActualizar );
      menuArchivo.add( elementoEliminar );
      menuArchivo.addSeparator();
      menuArchivo.add( elementoSalir );

      setSize( 400, 250  );
      setVisible( true );
      
    } // fin del constructor
    
   public static void main( String args[] )
   { 
      new ProcesadorTransacciones();
   }

   // obtener el nombre del archivo y abrirlo
   private boolean abrirArchivo()
   {
      // mostrar cuadro de dialogo para que el usuario pueda seleccionar el archivo
      JFileChooser selectorArchivo = new JFileChooser();
      selectorArchivo.setFileSelectionMode( JFileChooser.FILES_ONLY );

      int resultado = selectorArchivo.showOpenDialog( this );

      // si el usuario hizo clic en el boton Cancelar del cuadro de dialogo, regresar
      if ( resultado == JFileChooser.CANCEL_OPTION )
         return false;
      
      // obtener el archivo seleccionado
      File nombreArchivo = selectorArchivo.getSelectedFile(); 
      
      // mostrar error si el nombre de archivo es incorrecto
      if ( nombreArchivo == null || nombreArchivo.getName().equals( "" ) ) {
         JOptionPane.showMessageDialog( this, "Nombre de archivo incorrecto",
            "Nombre de archivo incorrecto", JOptionPane.ERROR_MESSAGE );
         return false;
      }
       
      try {
         // llamar al metodo ayudante para abrir el archivo
         archivoDatos = new EditorArchivo( nombreArchivo ); 
      }
            
      catch( IOException excepcionES ) {
         JOptionPane.showMessageDialog( this, "Error al abrir el archivo",
            "Error de ES", JOptionPane.ERROR_MESSAGE );
         return false;
      }
       
      return true;
      
   } // fin del metodo abrirArchivo

   // crear, actualizar o eliminar el registro
   private void realizarAccion( String accion )
   {
      try {
         
         // obtener los valores de los campos de texto
         String[] valores = interfazUsuario.obtenerValoresCampos();
     
         int numeroCuenta = Integer.parseInt( valores[ IUBanco.CUENTA ] );
         String primerNombre = valores[ IUBanco.PRIMERNOMBRE ];
         String apellidoPaterno = valores[ IUBanco.APELLIDO ];
         double saldo = Double.parseDouble( valores[ IUBanco.SALDO ] );
         
         if ( accion.equals( "Crear" ) )
            archivoDatos.nuevoRegistro( numeroCuenta, // crear un nuevo registro
               primerNombre, apellidoPaterno, saldo );
         
         else if ( accion.equals( "Actualizar" ) )
            archivoDatos.actualizarRegistro( numeroCuenta, // actualizar registro
               primerNombre, apellidoPaterno, saldo );
         
         else if ( accion.equals( "Eliminar" ) )
            archivoDatos.eliminarRegistro( numeroCuenta ); // eliminar registro
         
         else
            JOptionPane.showMessageDialog( this, "Accion incorrecta", 
               "Error al ejecutar la accion", JOptionPane.ERROR_MESSAGE );

      } // fin del bloque try
      
      catch( NumberFormatException formato ) {
         JOptionPane.showMessageDialog( this, "Entrada incorrecta",
            "Error en formato de numero", JOptionPane.ERROR_MESSAGE );
      }
      
      catch( IllegalArgumentException cuentaIncorrecta ) {
         JOptionPane.showMessageDialog( this, cuentaIncorrecta.getMessage(),
            "Numero de cuenta incorrecto", JOptionPane.ERROR_MESSAGE );
      }
      catch( IOException excepcionES ) {
         JOptionPane.showMessageDialog( this, "Error al escribir en el archivo",
            "Error de ES", JOptionPane.ERROR_MESSAGE );
      }
      
   } // fin del metodo realizarAccion
   
   //  escribir un registro en los campos de texto y actualizar el saldo
   private void mostrarRegistro( String transaccion )
   {
      try {
         // obtener el numero de cuenta
         int numeroCuenta = Integer.parseInt( 
            interfazUsuario.obtenerValoresCampos()[ IUBanco.CUENTA ] );
      
         // obtener el registro asociado
         RegistroCuentasAccesoAleatorio registro =
            archivoDatos.obtenerRegistro( numeroCuenta );
         
         if ( registro.obtenerCuenta() == 0 )
            JOptionPane.showMessageDialog( this, "El registro no existe", 
               "Numero de cuenta incorrecto",  JOptionPane.ERROR_MESSAGE );
         
         // obtener la transaccion
         double cambiar = Double.parseDouble( transaccion );
     
         // crear un arreglo de cadena para enviarlo a los campos de texto
         String[] valores = { String.valueOf( registro.obtenerCuenta() ),
            registro.obtenerPrimerNombre(), registro.obtenerApellidoPaterno(),
            String.valueOf( registro.obtenerSaldo() + cambiar ),
            "Cargo(+) o pago (-)" };
     
         interfazUsuario.establecerValoresCampos( valores );

      } // fin del bloque try
      
      catch( NumberFormatException formato ) {
         JOptionPane.showMessageDialog( this, "Entrada incorrecta",
            "Error en formato de numero", JOptionPane.ERROR_MESSAGE );
      }
      
      catch ( IllegalArgumentException cuentaIncorrecta ) {
         JOptionPane.showMessageDialog( this, cuentaIncorrecta.getMessage(),
            "Numero de cuenta incorrecto", JOptionPane.ERROR_MESSAGE );
      }
      
      catch( IOException excepcionES ) {
         JOptionPane.showMessageDialog( this, "Error al leer el archivo",
            "Error de ES", JOptionPane.ERROR_MESSAGE );
      }
      
   } // fin del metodo mostrarRegistro
   
} // fin de la clase ProcesadorTransacciones