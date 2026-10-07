# 06-ADR: Systemarchitektur

**Status:** entschieden
**Datum:** 07-10-2026

---

## Kontext

Für die prototypische Umsetzung müssen die zuvor getroffenen Entscheidungen zu Datenzugriff, Analyse und Visualisierung in eine gemeinsame Systemarchitektur überführt werden.

---

## Optionen

- Analyse und Verarbeitung in der Browser Extension
- Verarbeitung im Backend mit Browser Extension zur Integration und Visualisierung

---

## Entscheidung

Die prototypische Umsetzung verwendet eine Architektur aus Browser Extension und Spring Boot Applikation als Backend. Die [Systemarchitektur](/docs/Modelle/Systemarchitektur/Architekturmodell.V1.png) ist als Modell in einer vorläufigen Form zur Übersicht aufbereitet. 

Die Browser Extension übernimmt die Integration in den Nutzungskontext von YouTube. Sie erkennt die dargestellten Kommentare und ermittelt deren Kommentar-IDs zur eindeutigen Zuordnung der Analyseergebnisse.

Das Backend übernimmt den Zugriff auf die YouTube Data API sowie die Datenaufbereitung, Merkmalsberechnung als Grundlage für die Berechnung des Risk Scores.

---

## Folgen und To-dos

### Folgen

- Analyse und Bewertungslogik sind getrennt von der Benutzeroberfläche
- Die Browser Extension bleibt auf die Darstellung und Kontextintegration beschränkt
- Die Analyse kann unabhängig von der YouTube-Oberfläche weiterentwickelt werden
- Die Architektur ermöglicht eine schrittweise Erweiterung des Merkmalsmodells

### To-dos

- Umsetzung der Merkmalsberechnung
- Integration des Risk Scores

## Probleme

Die Browser Extension ist von der Struktur der YouTube-Webseite abhängig. Änderungen an der Benutzeroberfläche können daher Anpassungen bei der Zuordnung der Analyseergebnisse erforderlich machen.

Die Verwendung der YouTube Data API begrenzt die verfügbaren Informationen auf die von der Plattform öffentlich bereitgestellten Daten.