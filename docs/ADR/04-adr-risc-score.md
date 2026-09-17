# 04-ADR: Heuristischer Risk Score

**Status:** entschieden
**Datum:** 17-09-2026

---

## Kontext

Für die Bewertung der Merkmale wird ein heuristischer Risk Score benötigt, der die einzelnen Auffälligkeiten eines Kommentars zusammenführt und für Endnutzer nachvollziehbar darstellbar macht.

Der Risk Score soll als prozentualer Wert ausgegeben werden und damit unabhängig von der konkreten Anzahl der verwendeten Kennzahlen bleiben.

---

## Optionen

Für die Bewertung wurden folgende Ansätze betrachtet:

- Gleichgewichtete Bewertung aller Kennzahlen
- Individuelle Gewichtung von Merkmalskombinationen
- Mehrstufige Bewertung mit Kombinations- und Kategoriebonus

---

## Entscheidung

Für den MVP wird ein punktbasiertes Bewertungsmodell verwendet. Grundlage bildet der [Bewertungskatalog](/docs/ADR/Artefakte/04-Bewertungskatalog.png), in dem die für den MVP ausgewählten Kennzahlen, deren Berechnung, Bewertungsstufen, Schwellenwerte und Begründungen dokumentiert sind. Die daraus resultierende Bewertungslogik ist im [Risk Score](/docs/ADR/Artefakte/04-RiskScore.png) dargestellt.

### Bewertungskatalog

Der Bewertungskatalog operationalisiert das definierte [Merkmalsmodell](/docs/Modelle/Risk-Score/Merkmalsmodell.jpg) und bildet damit die verbindliche Grundlage für die anschließende Berechnung des Risk Scores.

Die Kennzahlen werden jeweils auf einer Skala von 0 bis 2 Punkten bewertet:

- **0 Punkte:** unauffällige Ausprägung
- **1 Punkt:** leicht auffällige Ausprägung
- **2 Punkte:** stark auffällige Ausprägung

Die Schwellenwerte sind dabei grundsätzlich heuristisch zu verstehen. Die verwendeten Quellen dienen unter anderem der Orientierung bei der Festlegung von Schwellenwerten.

Merkmale, die aufgrund fehlender Daten oder einer für den MVP zu hohen Komplexität nicht zuverlässig bewertet werden können, sind entsprechend als ausgeschlossen gekennzeichnet.

### Risk Score

Der [Risk Score](/docs/ADR/Artefakte/04-RiskScore.png) führt die Bewertungen der einzelnen Kennzahlen zu einer Gesamtbewertung zusammen. Da einzelne Merkmale nur begrenzte Aussagekraft besitzen, wird ein erhöhtes Risiko erst durch das gemeinsame Auftreten mehrerer Auffälligkeiten begründet.

Der Kombinationsbonus berücksichtigt dabei das gemeinsame Auftreten mehrerer Auffälligkeiten innerhalb einer Merkmalskombination. Der Kategoriebonus berücksichtigt zusätzlich, wenn starke Auffälligkeiten gleichzeitig in mehreren unterschiedlichen Merkmalskombinationen auftreten. 

Die Kennzahlen besitzen zwar unterschiedliche qualitative Aussagekraft. Auf eine individuelle Gewichtung wird im MVP dennoch verzichtet, da sich die unterschiedliche Bedeutung der Kennzahlen nicht zuverlässig in konkrete Gewichtungen übertragen lässt.

### Erweiterbarkeit

Das Punktesystem ist auf eine spätere Erweiterung ausgelegt. Neue Kennzahlen können bestehenden Merkmalskombinationen hinzugefügt werden, ohne die grundlegende Bewertungslogik zu verändern.

Der Risk Score wird deshalb nicht anhand eines dauerhaft festgelegten absoluten Maximalwertes berechnet, sondern relativ zum jeweils maximal erreichbaren Rohwert des aktuellen Merkmalsmodells auf 0–100 % normiert.

---

## Folgen und To-dos

### Folgen

- Risk Score wird für Endnutzer als Prozentwert von 0–100 % dargestellt
- Einzelne auffällige Kennzahlen führen nicht automatisch zu einer hohen Gesamtbewertung
- Risk Score = heuristische Auffälligkeitsbewertung

### To-dos

- Entwicklung des Visualisierungskonzepts und Entscheidung für Technologie (05-ADR) & (03-PoC)
- Implementierung der Bewertungslogik
- Evaluation der Bewertungslogik anhand realer Kommentarinteraktionen

---

## Probleme

Die Schwellenwerte der einzelnen Kennzahlen sowie die Bonusstufen sind heuristisch festgelegt. Die verwendeten Quellen liefern nicht für alle Kennzahlen konkrete Schwellenwerte für die Erkennung automatisierter Interaktionen.

Der Risk Score kann daher nicht als statistisch validierte Wahrscheinlichkeit für automatisiertes Verhalten interpretiert werden, sondern dient als Hinweis für Nutzer auf mögliche Auffälligkeiten in Kommentarinteraktionen.