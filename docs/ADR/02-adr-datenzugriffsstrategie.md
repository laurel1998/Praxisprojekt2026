# 02-ADR: Datenzugriffsstrategie

**Status:** entschieden  
**Datum:** 05-08-2026

**Anpassung:** 31-10-2026

---

## Kontext

Für die Umsetzung des MVP muss festgelegt werden, wie Kommentar-, Account- und Interaktionsdaten der ausgewählten Plattform erfasst werden.

Im Rahmen von PoC 01 wurde untersucht, welche Zugriffsmöglichkeiten YouTube für öffentliche Daten bereitstellt. Dabei zeigte sich, dass strukturierte Daten wie Kommentare, Zeitstempel und Accountinformationen über die YouTube API verfügbar sind. Gleichzeitig können öffentlich sichtbare Inhalte der Kommentaransicht direkt über das DOM der Webseite ausgelesen werden.

---

## Optionen

- Ausschließliche Nutzung der offiziellen API
- Ausschließliches DOM-Scraping
- Kombination aus API und DOM-Scraping

---

## Entscheidung

Für den Prototyp wird die YouTube API als primäre Datenquelle verwendet.

Die API stellt die für die Merkmalsanalyse benötigten Informationen wie Kommentare, Zeitstempel und Accountdaten strukturiert bereit und ermöglicht einen konsistenten Zugriff auf öffentliche Informationen. Ein ergänzender Zugriff über DOM-Scraping wird zu diesem Zeitpunkt für die Datenerfassung im MVP nicht benötigt. Die Nutzung einer einzelnen Datenquelle reduziert die technische Komplexität und erleichtert die Reproduzierbarkeit der Ergebnisse.

---

## Folgen und To-dos

**Folgen**

- Die Datenerfassung basiert vollständig auf der YouTube API

**To-dos**

- Analyse der Merkmale [(02-PoC)](/docs/PoC/02-poc-merkmalsanalyse.md)
- Ableitung des Merkmalsmodells [(03-ADR)](/docs/ADR/03-adr-merkmalsmodell.md)

---

## Probleme

Die Merkmalsanalyse ist auf die über die YouTube API verfügbaren Daten beschränkt. Insbesondere steht keine vollständige Interaktionshistorie einzelner Nutzer zur Verfügung. Merkmale, die langfristige Aktivitätsmuster oder plattformübergreifende Beziehungen erfordern, können daher im MVP nur eingeschränkt berücksichtigt werden.

--

## Hinweis zur Umsetzung:

Im Verlauf der Implementierung hat sich gezeigt, dass die YouTube API nicht alle für die Merkmalsanalyse benötigten Informationen bereitstellt. Daher wurde die ursprüngliche Entscheidung um eine ergänzende DOM-Auswertung erweitert, um insbesondere die tatsächliche Verschachtelung von Kommentarantworten zu erfassen.