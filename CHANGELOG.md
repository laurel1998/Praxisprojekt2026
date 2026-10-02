# Changelog

Alle nennenswerten Änderungen an diesem Projekt werden in dieser Datei dokumentiert.
Architekturentscheidungen sind über ADR-Dateien [(Architecture Decision Records)](docs/ADR/) dokumentiert.


## [1.1.0] – 2.10.2026
### Hinzugefügt
- Spring Boot Applikation als Backend integriert (V1 - [Prototyp](/prototype/backend/))
  - YouTube API Integration 
  - Auslesen und Aufbereiten von Kommentarinformationen



## [1.0.0] – 1.10.2026
### Hinzugefügt
- Browser-Extension zum Testen implementiert (V1 - [Prototyp](/prototype/extension/))
  - Erkennung des YouTube-Kommentarbereichs 
  - Auslesen der Video-ID
  - Einbindung eines Beispiel-Popups



## [0.11.0] – 17.09.2026
### Hinzugefügt
- [04-ADR:](/docs/ADR/04-adr-risc-score.md) Heuristischer Risk Score
  - [Bewertungskatalog](/docs/ADR/Artefakte/04-Bewertungskatalog.png) mit konkreten Schwellenwerten
  - [Risk Score](/docs/ADR/Artefakte/04-RiskScore.png) mit Bewertungslogik
- [05-ADR:](/docs/ADR/05-adr-visualisierungskonzept.md) Visualisierungskonzept
### Bearbeitet
- [PoC zur Visualisierung](/docs/PoC/03-poc-visualisierung.md)



## [0.10.0] – 12.08.2026
### Hinzugefügt
- [03-ADR:](/docs/ADR/03-adr-merkmalsmodell.md) Merkmalsmodell
- Merkmalsmodell als [tabellarische Übersicht](/docs/ADR/Artefakte/03-Merkmalsmodell-Tabelle.png)
- [Visualisierung](/docs/Modelle/Risk-Score/Merkmalsmodell.jpg) des Merkmalsmodells
 


## [0.9.0] - 10.08.2026
### Dokumentation
- Antrag auf Zugriff auf die Reddit API eingereicht (13.07.2026)
- Absage des Requests (24.07.2026)
- YouTube Data API v3 eingerichtet und getestet (03.08.2026)
- Zugriff auf öffentliche Videodaten & Kommentardaten erfolgreich validiert (04.08.2026)
- Auswirkungen auf folgende Dateien:
### Bearbeitet
- [PoC zur Datenerfassung](/docs/PoC/01-poc-datenerfassung.md) 
- [01-ADR:](/docs/ADR/01-adr-plattformwahl.md) Datenerfassung
- [02-ADR:](/docs/ADR/02-adr-datenzugriffsstrategie.md) Datenzugriffsstrategie
- [PoC zur Merkmalsextraktion und Analyse](/docs/PoC/02-poc-merkmalsanalyse.md) 



## [0.8.0] – 10.07.2026
### Hinzugefügt
- [01-ADR:](/docs/ADR/01-adr-plattformwahl.md) Plattformwahl
  - Begründung der Entscheidung
  - Festlegung von Folgen und ToDos
- [02-ADR:](/docs/ADR/02-adr-datenzugriffsstrategie.md) Datenzugriffsstrategie
  - Begründung der Entscheidung
  - Grundlage für die Analyse der Merkmale



## [0.7.0] – 06.07.2026
### Bearbeitet
- [PoC zur Datenerfassung](/docs/PoC/01-poc-datenerfassung.md) abgeschlossen
### Hinzugefügt
- [Vergleichsmatrix](/docs/PoC/Artefakte/01-PlattformVergleich.png) zur Plattformevaluation
  - Technische Zugriffsmöglichkeiten
  - Verfügbare Analysemerkmale



## [0.6.0] – 29.06.2026
### Hinzugefügt
- [MVP](docs/MVP.md) definiert
  - Grundprinzip und Fokus
  - Kernfunktionen für den Prototyp
- Grundlage für [PoCs](docs/PoC/) erstellt
  - erste Versionen als Basis für die Erarbeitung
  - Ziel, Scope und Kriterien festgelegt


## [0.5.0] – 25.06.2026
### Hinzugefügt
- [Marktrecherche](docs/Research-Notes/Marktrecherche.md) durchgeführt
  - Ansätze, Technologien und Visualisierungskonzepte bereits bestehender Lösungen
  - Abgrenzungen für den Prototyp



## [0.4.0] – 15.06.2026
### Hinzugefügt
- [Risikoanalyse](docs/Risikoanalyse.md) durchgeführt
  - Aufstellung der Top 5 - Risiken
  - Ansätze zur Risikominderung



## [0.3.0] – 08.06.2026
### Hinzugefügt
- [Forschungsstand](docs/Research-Notes/Forschungsstand.md) recherchiert
  - Allgemeine Informationen über Social Bots
  - Ansätze für Bot-Detektion und Begründete Auswahl der Analysekriterien



## [0.2.0] – 03.06.2026
### Hinzugefügt
- [Exposé](docs/Expose_Praxisprojekt.pdf)
  - Identifikation und Beschreibung des Problemraums  
  - Definition zentraler Entwicklungsziele
- [Domänenmodell](docs/Modelle/Domainmodel/Domainmodel.png)
  - Ausarbeitung des Problemraums



## [0.1.0] – 13.05.2026
### Hinzugefügt
- Projektinitialisierung   
  - Entscheidung für Projektthema und Fokus
  - Einrichtung der Dokumentationsgrundlagen (README, CHANGELOG, ADR-Verzeichnis)

