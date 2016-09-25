# Projekt-Thema

## Was erfüllt die Applikation?
- Exploration der Daten 
 -> Gewichtsfunktionen (Basisfunktion) anpassen
 -> betrachten von Teilmengen der zu untersuchenden Daten
- Informationsverbreitung

## Welche Daten werden wie dargestellt?
- scattered data
 -> unstrukturierte Daten 
 -> keine Nachbarschaftsrelation
- reguläres Gitter als Hilfsstruktur verwendet
- an Gitterpunkten des Grids Summe der einfließenden Faktoren gespeichert 
  -> teuer, da alle POIs betrachtet werden 
  -> Beschleunigungsstruktur (Okttree, Kd-Bäume)
- Farbwerte dem Gitterpunkten zugeordnet (Nearest-Neighbor-Interpolation)
- Farbinterpolation

## Farbmodi
- Heatmap (Rot=Höster Wert, Blau niedrigster)
 - Normierung aus (Möglichkeit ein spezifischen maximal Wert anzugeben)
 - Normierung an (= Maximalwert suchen -> durch diesen geteilt = normiert)
- Color-Mode 
(jede Kategorie wird durch ein Farbkanal kodiert, vorher wird normiert durch höchsten Wert der gegebenen Kategorie)

## Interpolationsmodi
- Bilineare Interpolation
 siehe wiki
- Bicubic 
 interpolation über 4x4 Matrix mit Indizes x-1,x,x+1,x+2 (das selbe für y)

## Warum so:
- einfache Berechnungen durch effiziente Strukturen (reguläres Gitter)
- Vereinfachung der Interpolation (keine Sheppard, mit inverser Distanz-Gewichtung)

## Was könnte man noch machen:
- Gittertypen
- Sheppard-interpolation
- Höhengraph -> Morsetheorie
- Skalarfeldtopologie mit Morsetheorie, Isokonturen, Konturbaum
- Marching Cubes 
 -> Beschleunigungsstrukturen
 -> Wie arbeitet er
 -> Probleme
 -> Lösungsansätze: 
  konsistente: einfach irgendwie verbinden
 , korrekte: asymptotik decider, dennoch Problem da nur Randflächen 

## Gegenteilige Themen:
- Vektorfeldtopologie
- Merkmalskurven

