# Laboratorio_3_Cifrado
En esta branch se encuentra terminada la serie de cifrado y decifrado por el algoritmo SDES
# Consideraciones
Aqui estan algunas consireciones que se deben de tener a la hora de la prueba de esta aplicacion:
* La aplicacion fue probada y posee toda su funcionalidad en los siguientes dispositivos:
    * Android 6.0.1 y API 23
    * Android 8.0.0 y API 26
    * Emulador Nexus 5X, Android 8 y API 27
* Para la modificacion de las Permutaciones en el cifrado y decifrado con el algoritmo SDES se puede realizar directamente
  en la clase llamada Permutaciones. En ella encontrara los metodos de permutaciones que contendran un arreglo de valores tipo char.
  Estos arreglos contienen el orden de la permutacion y es donde se puede cambiar dicho orden.
* Cabe destacar que al intentar salir de la interfaz de guardado de archivos en cualqiuera de los algoritmos de cifrado o decifrado la
  aplicacion se cerrara automaticamente.
* Si desea ver el resultado de decifrado del archivo de algun algoritmo se puede hacer simplemente al seleccionar el archivo en alguna 
  de las vistas de cifrado que son capaces de mostrar archivos con extension .txt.
* Para ver las opciones de cifrado se debe dar click en el boton de la esquina superior derecha de la ventana principal de la aplicacion.
