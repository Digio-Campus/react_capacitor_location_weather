Pequeño proyecto para practicar permisos y servicios nativos en un proyecto React con Capacitor.
En este proyecto vamos a crear una aplicación que solicite permisos de localización. Después lanzará un servicio para obtenerla. A continuación le pasará la localización a un objeto que hará una llamada a una API de climatología para recibir la temperatura actual en esa localización y mostrarla a través de una notificación.

	1. Para empezar ejecutamos "npx create-react-app react_capacitor_location_weather" para crear un nuevo proyecto React.

	2. Ejecutamos "npm install @capacitor/core @capacitor/cli @capacitor/android" para instalar las dependencias. Inicializamos Capacitor con "npx cap init" y añadimos android con "npx cap add android". Hacemos el build con "npm run build" y sincronizamos con "npx cap sync". Cuando queramos ejecutar el proyecto en android usaremos el comando "npx cap run android".
	
	3. Para poder hacer llamadas a la api "https://api.open-meteo.com/" necesitamos usar Retrofit. Para ello añadimos las dependencias en el fichero build.gradle del módulo app en nuestra plataforma Android: "implementation 'com.squareup.retrofit2:retrofit:2.9.0' implementation 'com.squareup.retrofit2:converter-gson:2.9.0'". También vamos a usar Fused Location Provider de Google Play Services por lo que ponemos también: "implementation 'com.google.android.gms:play-services-location:21.3.0'"
 
	4. En el Manifest añadimos la declaración de los permisos que vamos a utilizar: "   <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />" 
	
	5. En el directorio donde se encuentra MainActivity.java creamos una clase LocationService que hereda de Service. Añadimos una etiqueta <service> en el Manifest que haga referencia a esta clase.

	6. En el mismo directorio creamos los ficheros "CurrentData", "WeatherApiFetch", "WeatherFetch" y "WeatherResponse". 

	7. Ahora vamos a MainActivity y comprobamos si ya están aceptados los permisos. En caso afirmativo usamos un Intent para lanzar el servicio LocationService, en caso negativo los pedimos y si el usuario los acepta entonces lanzamos el servicio. 

	8. En LocationService usamos un objeto Handler para que se ejecute el servicio cada 15 segundos y un objeto FusedLocationProviderClient para obtener la localización. Cuando la obtenemos la publicamos en un MutableLiveData estático. En el onCreate() del MainActivity observamos los cambios del MutableLiveData y los recogemos cuando cambien.

	9. A continuación nos vamos a WeatherFetch que será donde hagamos la llamada a la API. Necesitamos un método fetchWeather(latitude,longitude) para llamarlo desde MainActivity cada vez que haya un cambio en el MutableLiveData de localización.

	10. En WeatherApiFetch creamos la interfaz para la consulta con retrofit y en WeatherResponse y CurrentData creamos el modelo de datos para la respuesta.

	11. Ahora en WeatherFetch creamos un canal de notificaciones y un método para enviar una notificación. A través de esta notificación mostraremos la temperatura y las coordenadas.

	12. Y con estos pasos debería de funcionar perfectamente.