# Swagger UI

## Configuración

El archivo "config.json" permite configurar la aplicación. Los parámetros que reconoce son los siguientes:

* "url": path del json o yml de la API.
* "urls": lista de objetos para indicar múltiples API. Los objetos siguen el formato { "url": "", "name": "" }, siendo **url** la ruta del json o yml y **name** el nombre identificativo.
* "primary": nombre de la API a cargar por defecto en caso de que se haya rellenado el parámetro "urls".
* "title": título identificativo. También representa el título de la web.
* "subtitle": subtítulo identificativo
* "color": color de la fuente para título y subtítulo.
* "background": fondo de la cabecera.
* "selectorBorderColor": color del borde para el selector de APIs.

Es obligatorio introducir al menos uno de los parámetros "url" o "urls".

## Nota

Los archivos contenidos en la carpeta **images** son los utilizados en la propia web, véase:

* Favicon -> /images/favicon.png
* Logo -> /images/logo.png

## Comandos

    npm install -> instala dependencias del proyecto
    npm start -> lanza la aplicación en modo desarrollador
    npm run build -> genera en la carpeta **build** el proyecto listo para desplegar