# 05-ADR: Visualisierungskonzept

**Status:** entschieden
**Datum:** 17-09-2026

---

## Kontext

Im [PoC zur Visualisierung](/docs/PoC/03-poc-visualisierung.md) wurde die Darstellung des Risk Scores als farbiger Punkt auf Grundlage eines Ampelschemas ausgewählt.

Für die prototypische Umsetzung müssen nun die konkreten Farbbereiche festgelegt und die technologische Umsetzung der Visualisierung bestimmt werden.

---

## Optionen

### Farbbereiche

Für die Zuordnung des Risk Scores zu den drei Farben stehen unterschiedliche Aufteilungen der Skala von 0–100 % zur Verfügung.

### Technologische Umsetzung

Für die Integration der Visualisierung in den Nutzungskontext werden folgende Möglichkeiten betrachtet:

- Browser-Extension
- Webanwendung

---

## Entscheidung

### Farbbereiche

Die drei Farben werden folgenden Risk-Score-Bereichen zugeordnet:

- **Grün:** 0–33 %
- **Gelb:** 34–66 %
- **Rot:** 67–100 %

Die Farbbereiche dienen der vereinfachten visuellen Einordnung des heuristischen Risk Scores.

### Technologische Umsetzung

Die Visualisierung wird als Browser Extension umgesetzt.

Die Browser Extension ermöglicht die direkte Integration des farbigen Punktes in die bestehende YouTube-Kommentaransicht. Dadurch bleibt die Bewertung direkt im Nutzungskontext sichtbar und Nutzer müssen die Plattform für die Analyse nicht verlassen.

---

## Folgen und To-dos

### Folgen

- Die Bewertung ist direkt innerhalb der YouTube-Kommentaransicht sichtbar
- Die Visualisierung benötigt nur wenig zusätzlichen Platz
- Die Browser-Extension ermöglicht eine nachträgliche Erweiterung der bestehenden Benutzeroberfläche

### To-dos

- Umsetzung der Browser-Extension
- Integration des farbigen Punktes in die Kommentaransicht

---

## Probleme

Die Farbcodierung ermöglicht eine schnelle Einordnung, zeigt jedoch nicht, wie sich der Risk Score zusammensetzt.

Die Umsetzung als Browser-Extension ist von der technischen Struktur und dem Verhalten der YouTube-Webseite abhängig. Dynamisch nachgeladene Kommentare müssen erkannt und mit der Punkt-Visualisierung versehen werden. Darüber hinaus müssen die [Kommunikation](/docs/PoC/Artefakte/TestderKommunikation.png) zwischen Extension und Backend sowie mögliche Verzögerungen oder Verbindungsfehler berücksichtigt werden.

Die konkrete technische Umsetzung und der Umgang mit diesen Herausforderungen werden im [06-ADR: Systemarchitektur](/docs/ADR/06-adr-systemarchitektur.md) betrachtet.