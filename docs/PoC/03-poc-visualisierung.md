# 03-PoC: Visualisierung

**Status:** Done

---

## Problem / Fragestellung

Die Analyse automatisierter Interaktionen soll für Endnutzer verständlich und direkt im Nutzungskontext sichtbar gemacht werden. Dabei besteht die Herausforderung, komplexe Analyseergebnisse auf eine reduzierte und nachvollziehbare Form zu abstrahieren.

Es soll geprüft werden, welche Visualisierungsform sich dafür am besten eignet.

---

## POC-Ziel

Evaluation geeigneter Visualisierungsansätze zur Darstellung heuristischer Bewertungen im direkten Nutzungskontext.

Zentrale Fragen:

* Welche Darstellungsform ist für Nutzer intuitiv erfassbar?
* Wie kann die Darstellung möglichst platzsparend erfolgen?
* Welche Darstellungsform ist am besten erkennbar?

---

## Scope

Untersucht werden verschiedene Visualisierungskonzepte innerhalb einer Kommentaransicht.

Mögliche Visualisierungsformen:

* Ampelsystem
* kontinuierliche Farbcodierung
* numerischer Risk Score

---

## Methodik

Es werden verschiedene Darstellungsformen schematisch anhand eines identischen Beispielkommentars getestet.

Untersucht werden:

* Sichtbarkeit im Nutzungskontext
* Verständlichkeit
* Platzbedarf

---

## Evaluationskriterien

Eine Visualisierung gilt als geeignet, wenn:

* sie intuitiv verständlich ist
* sie den Nutzungskontext nicht überlädt, aber klar erkennbar ist
* sie auch bei begrenztem verfügbaren Platz eindeutig erkennbar bleibt

---

## Ressourcen

* UI-Skizzen oder Mockups
* Test-Kommentar

---

## Durchführung

1. Entwicklung erster Visualisierungsentwürfe und Umsetzung als Mockups
2. Vergleich verschiedener Darstellungsformen
3. Bewertung anhand der Evaluationskriterien

---

## Ergebnisse

Die [Ampeldarstellung](/docs/PoC/Artefakte/03-Mockup_Ampel.jpg) ermöglicht eine intuitiv verständliche Einordnung des Risk Scores. Die Verwendung eines einzelnen farbigen Punktes anstelle einer vollständigen Ampel reduziert dabei den benötigten Platz und ermöglicht eine unaufdringliche Integration direkt am Kommentar. Dies ist insbesondere für kleinere Displays relevant. Die Möglichkeit, den farbigen Punkt anzuklicken und weitere Informationen zur Bewertung anzuzeigen, wird als mögliche Erweiterung für eine spätere Version betrachtet.

Die [kontinuierliche Farbcodierung](/docs/PoC/Artefakte/03-Mockup_Farbcodierung.jpg) hingegen ermöglicht eine kompakte Darstellung, ist jedoch ohne zusätzliche Legende nicht unmittelbar verständlich. Für Nutzer ist nicht eindeutig erkennbar, welche Bedeutung die jeweilige Farbe besitzt.

Die [numerische Darstellung](/docs/PoC/Artefakte/03-Mockup_Numerisch.jpg) des Risk Scores ermöglicht eine präzise Angabe des Bewertungswertes. Der Prozentwert hebt sich jedoch nur wenig vom eigentlichen Kommentarinhalt ab und ist dadurch im Nutzungskontext weniger auffällig.

---

## Entscheidungen / Konsequenzen

Für das weitere Visualisierungskonzept wird die Darstellung als [farbiger Punkt auf Grundlage eines Ampelschemas](/docs/PoC/Artefakte/03-Beispielkommentar.png) verwendet. Die konkrete Festlegung der Farbbereiche wird im ADR zum Visualisierungskonzept getroffen. 

Die ausgewählte [Visualisierung](/docs/PoC/Artefakte/TestpunktmitZuordnungsbeweis.png) wurde mithilfe einer vereinfachten Berechnungslogik technisch umgesetzt und getestet.

Eine klickbare Darstellung mit zusätzlichen Informationen zur Zusammensetzung des Risk Scores wird als Erweiterung für eine spätere Version vorgesehen.

Die Ergebnisse bilden die Grundlage für:

* [05-ADR:](/docs/ADR/05-adr-visualisierungskonzept.md) Visualisierungskonzept
* [06-ADR:](/docs/ADR/06-adr-systemarchitektur.md) Systemarchitektur