# Projekt
Projekt iz programskog inženjerstva


USPOREDITI 2 metode
koliko su one dobre u analizi tog praga

koliko je koja dobra

sustav koji ce analizirati te metode,
za koje metrike naci neka smislena razina?
koliko moze taj treshold iznosit?

dal ima smisla trazit univerzalni treshold?

pogledat VECU, GUI s nekim kucicama haha
  -> predodzba kako ce nase klase izgledati
  -> po njemu cemo pisati kod
  
Sami isprogramirati klase/metode koje racunaju ten eke vrijednosti

IDEJA:
  -> Analiza praga sustava
  -> Hoce to bit web sucelje, neki servis, java applet, sto god
  -> moderno, centralizirana baza podataka, u koju ce se spremat ti tresholdi itd
 
 
_______________VJEZBE3_____________________ 

kljucni datumi, interni datumi zbog scruma
objasniti sto je razina praga
ne pisati opcenito -> razjasniti
izrazavat se tehnicki
tko stavlja u to do, koje kartice idu gdje
odrediti sto ide u to do, doing i done, dal ce netko provjeravati done

alati tehnike za komunikaciju, razvoj, podjela posla i odgovornosti...

zadaci - mkanuti eclipse
	- tu idu konkretni zadaci koje bi netko trebao napraviti

template izvjesca za cijeli tim

_______________________________________

csv i arff formati metrika



9.4.2018.
Specifikacija zahtjeva komentari
- Ucitavanje iz excela MOZE biti jedan unos podataka, da bimo izracunali prag moramo imat veci unos podataka (1 USE CASE)
- vise razlicitih analiza, mogu se izracunati jedni tresholdovi sa drugima 
- format ulaznih podataka .arff ili jos .csv
- navesti zahtjeve: zahtjev br 1 -> unos podataka, zahtjev br 2 -> ....
- objasnit ROC i VARL (bolje)
- Slika 2. razmisliti kako izracunati postupak VARL-a
- Slika 2. nejasno sta se usporedjuje i kako (bolje objasniti)
- Razmisliti o prikazu podataka!
- Ubacit poglavlje ZAHTJEVI (GUI jedan od zahjeva, baza podataka jedan od zahtjeva, itd itd itd)
- ČIM PRIJE POCET IMPLEMENTACIJU!


16.4.2018.

- zahtjevi sustava -> isprogramirat da ulazni podaci budu samo arff i csv a ako ne neka javi grešku
- napravit logističku regresiju prema slici (Prezentacija gdje su zadane teme projekta) 


23.4.2018

- zahtjevima pridodjelimo nekakav ID da bude jednistven svaki zahtjev
- matrica praćenja zahtjeva u dizajnu treba povezati sa zahtjevima
- kada se prvi put spominje neka kratica treba ju objasnit (spec zahtjeva VARL)
- trening modela testing modela i tek onda racunanje parametra za roc i tek onda crtanje
- cross validation - podjela na slojeve 
- u logističku regresiju saljemo metriku po metriku 
- podatke gledamo po stupcima 
- mogli bismo imati usporedbu korisnikovih thresholda sa nekim t. iz
	industrije ili open source-a te bi ga mogli odna smjestitim u neku "grupu".
	
	
---------------------------------------------------------------------------------------
7.5.18

za svaku metriku 1 koeficijent i slobodni clan. b0, b1, b2, b3, b4,... i slobodni clan, treba vidjeti koji je slobodni clan.
p[0] koliko ima nula, p[1] koliko ima 1, tested negative i tested positive

-> slati za svaku metriku da dobijemo samo 2 bete. za svaku metriku posebno b0 i b1
-> provjeriti opet ako postoji metoda za p_val u log reg. (statistički significant.)

-------------------------------------------------------------------------------------------------------------------------

15.5.2018.

Projektni plan:
-odvojiti malo onaj dio od alata i tehnika, staviti to u komunikaciju
-mehanizmi izvješćivanja
-dodati rizike koji se eventualno mogu pronaći
	
Dizajn specifikacija:
-arhitektura sustava -npr. cloud, pa objasniti prednosti,nedostatke, detaljan opis
-matrica praćenja zahtjeva je u obliku id-a i onda s čime je to povezano u spec.zahtjeva, kako je to povezano
	
-------------------------------------------------------------------------------------------------------------------------

