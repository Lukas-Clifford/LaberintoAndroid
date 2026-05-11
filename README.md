---

# Laberinto con giroscopio y Canvas

## Lukas Clifford Vogdt Torralba

---

A continuación detallo el proceso de desarrollo de la práctica de recuperación, un videojuego de laberinto para Android utilizando Canvas y el acelerómetro del dispositivo. El desarrollo lo fui planteando paso a paso de la siguiente manera:

## 1\. Estructura base de la aplicación

Empecé dividiendo el proyecto en 3 activities principales para separar bien las distintas fases del juego:

* Una pantalla de inicio que simplemente contiene un botón para empezar.  
* La pantalla del laberinto, donde ocurre toda la lógica del juego.  
* Una pantalla final de resultados para cuando el jugador logra completar el nivel.

## 2\. Creación del mapa del laberinto

Para diseñar el mapa de forma eficiente, dibujé una imagen muy sencilla de 10x10 píxeles. Después, escribí un pequeño script en Python utilizando la librería Pillow para leer esa imagen e imprimir una matriz numérica. Esta matriz me la llevé a Kotlin usando 1 y 0 como identificadores para distinguir qué casillas eran camino libre y cuáles eran muro (el 1 representa una pared).  

## 3\. Físicas de la bola y colisiones

A la hora de implementar el movimiento, utilicé la estructura de código de otro proyecto que ya tenía para las físicas básicas usando el sensor. Sin embargo, tuve que programar desde cero el sistema de colisiones con las paredes del laberinto.

Para plantear las matemáticas de la colisión le pedí a Copilot que me explicara cómo abordarlo ([enlace a la conversación](https://github.com/copilot/share/8848533e-4880-80e4-b941-600240be29fe%20)). La lógica funciona así:

1. Primero se comprueba si la bola está cerca del centro de una pared (como el mapa funciona por casillas, trato cada pared como un cuadrado con un punto central).  
2. Luego se calcula sobre qué eje se está produciendo la superposición.  
3. Se verifica por qué lado de ese eje está chocando la bola.  
4. Con estos datos, se ajusta la posición de la bola para que no atraviese el muro y se modifica su velocidad para hacer el rebote.

## 4\. Puntos de inicio y meta

Una vez que las colisiones funcionaban correctamente, volví a modificar el array del mapa añadiendo dos identificadores nuevos: el 2 para marcar la casilla donde inicia la bola, y el 3 para la casilla de meta. De esta forma, programé que si la bola colisiona con el bloque 3, el nivel se da por superado y se pasa directamente a la activity final.

## 5\. Efectos de sonido (Rebotes)

Después me puse con el apartado del audio usando SoundPool. Implementé un sonido para cuando la bola choca contra una pared, pero tuve que ajustarlo: le puse una condición para que solo suene si el choque sobrepasa una velocidad específica. De esta forma evito que el sonido se reproduzca en bucle cuando la bola simplemente se está deslizando o apoyando continuamente contra una pared; solo suena en los rebotes fuertes.

## 6\. Audio y animaciones de victoria

También añadí un efecto de sonido para cuando se consigue terminar el laberinto. Este audio no lo cargo en la pantalla del juego, sino que se carga y reproduce directamente al abrirse la activity final.

En esta misma activity final incluí una animación utilizando ValueAnimator donde un label de texto se desplaza de abajo hacia arriba.


## 7\. Brillo adaptativo (Sensor de luz)

Finalmente, abordé el requisito de la luz de pantalla adaptativa. Para no tener que repetir y programar la lógica del sensor de iluminación en cada una de las tres activities, la solución más limpia fue crear una Activity Base que gestione el brillo y actúe como clase padre. De esta forma, las otras activities simplemente heredan de ella y el brillo se adapta en toda la aplicación automáticamente.

