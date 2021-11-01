 # Busqueda Local: 
   Ruta de Camiones para atender a peticiones: mínimiza tiempo y peticiones desatendidas, maximiza beneficio

**DOCUMENTACIÓN (./DOCUMENTACIÓ):**
- enunciado.pdf
- documentacion_practica.pdf [NUESTRA DOCUMENTACIÓN]

**CÓDIGO FUENTE (./src/IA/Abastecimiento):**
- AbastecimientoState.java: operdaores, solución inicial
- AbastecimientoHeuristica1.java: pondera las peticiones no atendidas con exponencial 2
- AbastecimientoHeuristica2.java: pondera las peticiones no atendidas con exponencial 4
- AbastecimientoSuccessorFunction1.java: funcion sucesora del algoritmo hill climbing
- AbastecimientoSuccessorFunction2.java: funcion sucesora del algoritmo simulated annealing

**COMO EJECUTAR:**
- Ir hasta directorio Busqueda-Local y ejecutar (en terminal): _./script.sh_
- Ir hasta directorio Busqueda-Local y ejecutar (en terminal): _java -jar Abastecimiento.jar_

**PARA PROBAR DIFERENTES VALORES**
En cuanto se ejecuta el programa aparece el siguiente mensaje:

VALORES POR DEFECTO: 
número gasolineras: 100
número centros de distribucion: 10
multiplicidad (camiones por centro distribucion): 1
algoritmo de busqueda: hill climbing
heuristica: 1
estado inicial: randomizado
seed 1234
horas de trabajo: 8 (camiones siempre van a 80km/h
coste de viajar un kilómetro: 2
steps del algoritmo sa: 150000
k del algoritmo sa: 125
stiter del algoritmo sa: 10
lambda del algoritmo sa: 1.0E-4


OPCIONES: 

run        --  ejecutar busqueda
ngas       --  cambiar numero gasolineras
ncen       --  cambiar numero de centros de distribucion
mult       --  cambiar multiplicidad de centros de distribucion
costekm    --  cambiar coste de recorrer un kilometro
horas      --  cambiar horas que puede trabajar un camion (0 <= h <= 24
algo       --  cambiar algoritmo de busqueda (hc == hill climbing, sa == simulated annealing)
inicial    --  cambiar solucion inicial (0 - randomizada, 1 - ponderada
heuristica --	cambiar heuristica (0 - penalización 2^x, 1 - penalización 4^x)
seed       --  cambiar la semilla aleatoria
steps      --  cambiar steps para algoritmo sa
stiter     --  cambiar stiter para algoritmo sa
k          --  cambiar k para algoritmo sa
lambda     --  cambiar lambda para algoritmo sa
print      --  ver opciones escogidas
opts       --  ver opciones
----------------------------------------------

Para cambiar los valores se escribe el nombre de lo que quieres cambiar y el valor. Para ejecutar, escribe el comando run. 

_EJEMPLOS:_
ngas 5 ncen 2 mult 2 run
algo sa costekm 3 run

En caso de necesitar ver los valores definidos usar el comando _print_ y en caso de necesitar ver las opciones disponibles usar el comando _opts_.
