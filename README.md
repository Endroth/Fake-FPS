# FPS Fake (Fabric 1.21.11)

Mostra um FPS absurdo e variando no F3 (ex.: 5162882 fps). Só visual: o FPS real não muda.

## Como usar
- Tecla **K**: liga/desliga (dá pra trocar em Opções > Controles > FPS Fake)
- `/fpsfake` alterna, `/fpsfake on` liga, `/fpsfake off` desliga
- O estado fica salvo em `config/fpsfake.properties`

## Como gerar o .jar
Precisa de Java 21. Na pasta do projeto:

    ./gradlew build        (Windows: gradlew.bat build)

O arquivo fica em `build/libs/fpsfake-1.0.0.jar` (use esse, NÃO o `-sources`).
Coloque na pasta `mods` junto com o Fabric API.

## Ajustar os números
Em `src/main/java/dev/fpsfake/FpsFake.java`: `BASE_FPS`, `SPREAD` e `CHANGE_EVERY_MS`.
