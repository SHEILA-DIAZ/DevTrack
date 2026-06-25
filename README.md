# DevTrack
Aplicación móvil Android desarrollada con Kotlin y Jetpack Compose para la gestión de proyectos tecnológicos mediante MVVM, Room, Firebase y Retrofit.

## Configuración de Firebase
Para ejecutar el proyecto, descarga `google-services.json` desde Firebase Console y colócalo dentro de la carpeta `app/`.

## Reglas de Seguridad recomendadas para Firestore
```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /usuarios/{userId}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```
