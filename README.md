# Projekt
Projekt iz  kolegija programsko inženjerstvo na 3. godini preddiplomskog studija računarstva

## Opis projekta

Programski proizvod ima glavnu funkciju da evaluira kvalitetu koda raznih klasa objektno orijentiranih programskih jezika, u našem slučaju Java jezika. Proizvod koristi metodu logističke regresije za učenje modela te VARL metodom razine praga klasificira zadane klase i određuje vjerojatnost pojave pogreške u njoj.Također, programski proizvod identificira metrike koje su odgovorne za pojavu pogreške. Time korisnik zna na koje metrike i u kojim klasama treba raditi kako bi smanjio rizik pojave pogreške.

## Korištenje programskog proizvoda

Programski proizvod koristi se na način da se pritisne na gumb „Dodaj Datoteku“ te se u prozorčiću pronađe željena .csv ili .arff datoteka. Nakon što se datoteka uspješno učita kontrolni okvir (engl. Checkbox) s natpisom „Datoteka spremna“ označit će se. Nakon što je kontrolni okvir označen korisnik može kliknuti na gumb „Izračunaj“ te se time pokreće:
- Proces za izračun koeficijenta logističke regresije putem cross validacije
- Izračun VARL praga ( Threshold ) za svaku metriku
- Scoring po klasama 

Pritiskom na gumb „Nacrtaj ROC graf“ korisniku se otvara Weka-ino grafičko sučelje s prikazom grafa te su omogućene razne mogućnosti koje korisnik može pritisnuti kako bi drugačije reprezentirao podatke. Pritiskom na gumb „Zatvori“ programski proizvod se zatvara.
