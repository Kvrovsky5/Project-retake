# Menedżer Zadań

## Spis treści
1. [Opis projektu](#opis-projektu)
2. [Funkcjonalności](#funkcjonalności)
3. [Instrukcje obsługi](#instrukcje-obsługi)
    - [obsługa](#obsługa)
    - [Wymagania](#wymagania)
    - [Instalacja](#instalacja)
    - [Uruchomienie](#uruchomienie)

## Opis projektu
Projekt Menedżer Zadań jest aplikacją do zarządzania zadaniami i użytkownikami. Umożliwia dodawanie, edytowanie, usuwanie zadań oraz przypisywanie ich do konkretnych użytkowników. Aplikacja pozwala również na generowanie raportów dotyczących zadań wykonanych przez użytkowników.

## Funkcjonalności
1. **Dodawanie zadań**
   - Możliwość dodawania nowych zadań wraz z nazwą, opisem i przypisaniem do użytkownika.
   
2. **Edycja zadań**
   - Możliwość edytowania istniejących zadań, zmiana nazwy, opisu oraz przypisania do innego użytkownika.
   
3. **Zmiana statusu zadań**
   - Zmiana statusu zadań na "Do zrobienia", "W trakcie" lub "Zrobione".
   
4. **Dodawanie użytkowników**
   - Możliwość dodawania nowych użytkowników do listy.
   
5. **Usuwanie użytkowników**
   - Usuwanie użytkowników oraz automatyczne przypisywanie zadań usuniętego użytkownika do innego użytkownika.
   
6. **Generowanie raportów**
   - Generowanie raportów z ilością zadań przypisanych do użytkowników oraz czasu spędzonego na zadaniach.

7. **Zapisywanie i ładowanie danych**
   - Możliwość zapisywania stanu zadań i użytkowników do pliku JSON oraz ładowanie tych danych z pliku.

## Instrukcje obsługi

### obsługa
Obsługa aplikacji
Dodawanie użytkowników:

Kliknij przycisk "Dodaj użytkownika" i wprowadź nazwę nowego użytkownika.
Dodawanie zadań:

Kliknij przycisk "Dodaj zadanie", wypełnij formularz, podając nazwę, opis i przypisanie do użytkownika, a następnie kliknij "Dodaj Zadanie".
Zmiana statusu zadań:

Kliknij prawym przyciskiem myszy na wybrane zadanie i wybierz nowy status z menu kontekstowego.
Edycja zadań:

Kliknij prawym przyciskiem myszy na wybrane zadanie i wybierz "Edytuj zadanie", a następnie wprowadź zmiany i kliknij "Zapisz Zmiany".
Generowanie raportu:

Kliknij przycisk "Wygeneruj raport", aby zobaczyć raport zadań.
Zapis i odczyt danych:

Kliknij przycisk "Zapisz zadania do formatu JSON", aby zapisać dane do pliku.
Kliknij przycisk "Załaduj zadania z pliku tasks.json", aby załadować dane z pliku

### Wymagania
- Java 8 lub nowsza
- JavaFX
- Gson

### Instalacja

1. Pobierz i zainstaluj [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html).
2. Pobierz i zainstaluj [JavaFX SDK](https://gluonhq.com/products/javafx/).
3. Pobierz bibliotekę [Gson](https://github.com/google/gson/releases).

### Uruchomienie

1. Skonfiguruj środowisko:
    - Dodaj JavaFX do swojego projektu.
    - Dodaj plik JAR biblioteki Gson do swojego projektu.

2. Sklonuj repozytorium z GitHub:

   ```sh
   git clone https://github.com/twoje_uzytkownik/twoje_repozytorium.git
   cd twoje_repozytorium

3.Skompiluj i uruchom aplikację:

```sh
    Skopiuj kod
    javac --module-path /path/to/javafx/lib --add-modules javafx.controls TaskManagerApp.java
    java --module-path /path/to/javafx/lib --add-modules javafx.controls TaskManagerApp
    Upewnij się, że zamieniasz /path/to/javafx/lib na rzeczywistą ścieżkę do plików JavaFX na Twoim komputerze.