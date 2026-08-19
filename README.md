# emu-java

Aplicación Android para cargar/emular juegos Java JAR/JAD (MIDlets) y jugarlos con una cruceta y botones A/B estilo Game Boy Color.

Estado
- Esqueleto inicial del proyecto con módulo `app` y módulo `emulator-core` (placeholder).

Características previstas
- Cargar .jar/.jad desde almacenamiento local
- Emulación J2ME integrada (módulo emulator-core)
- Controles táctiles: D-pad + A + B
- Perfiles por juego (mapeos de botones, escala)

Aviso legal
Este proyecto no incluye juegos. No redistribuyas archivos con copyright sin permiso. El usuario es responsable de proveer sus propios archivos .jar/.jad.

Cómo empezar
1. Abre el proyecto en Android Studio.
2. Implementa o integra un motor J2ME en `emulator-core` (p. ej. MicroEmulator o J2ME-Loader).
3. Compila y ejecuta la app en un dispositivo o emulador Android.

Contribuciones
Si quieres ayudar con integración del motor, skins o mejoras de input, abre un issue o PR.
