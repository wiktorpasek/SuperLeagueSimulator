#Super League Simulator (v1)

To jest mój pierwszy, autorski projekt symulatora ligi piłkarskiej napisany w Javie. Obecna wersja to MVP (Minimum Viable Product),
stworzona głównie w celach edukacyjnych, aby przećwiczyć programowanie obiektowe, algorytmy i tworzenie prostego interfejsu graficznego.

Głowne funkcje v1:

* **Prawdziwy Terminarz (Algorytm Round-Robin):** Gra generuje profesjonalny kalendarz rozgrywek przed startem sezonu.
Każda drużyna gra z każdą dokładnie dwa razy (mecz u siebie i rewanż na wyjeździe), dokładnie tak jak w prawdziwych ligach.

* **Autorski Silnik Meczowy:** Wyniki spotkań nie są czysto losowe. Silnik bierze pod uwagę statystyki ataku i obrony każdej z
drużyn (obecnie wczytywane z pliku). Silniejsza drużyna ma statystycznie większe szanse na wygraną, ale zachowałem element
losowości – w piłce nożnej słabszy zawsze może sprawić niespodziankę!

* **Interfejs Graficzny (UI):** Do obsługi symulatora wykorzystałem bibliotekę Java Swing wzbogaconą o znaczniki
HTML i CSS. Dzięki temu tabela ligowa, menu oraz wyniki na żywo posiadają czytelny, niestandardowy wygląd w
ciemnym motywie (Dark Mode).

* **Symulacja Rozgrywek:** Użytkownik może ręcznie przechodzić kolejkę po kolejce śledząc wyniki (Live Score) lub
użyć opcji szybkiej symulacji całego sezonu.

## 🚀 Plany na przyszłość (Roadmap)

To dopiero początek. W przyszłości planuję rozbudować ten projekt i przenieść go do świata aplikacji webowych
(architektura klient-serwer). Główne cele to:

* Stworzenie pełnoprawnego interfejsu w przeglądarce (np. React / Angular).
* Wprowadzenie mechaniki tworzenia własnego zespołu od zera.
* Rozbudowa silnika o system rozwoju klubu, formę zawodników i zarządzanie taktyką.