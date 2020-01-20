# mi2-ajanlo_rendszerek

Mesterséges inteligencia 2. házifeladat, 2019/20 őszi félév.

## Feladatleírás

### Bevezetés
Adott egy online hangoskönyv áruház, amely minden eladásra kínált műnél megjeleníti
a korábbi vásárlók értékelését és a szokásos jellemzőket, úgymint tartalmi
összefoglaló, szerző, narrátor, terjedelem. A felhasználók egy ötfokozatú skálán
értékelhetik a hangoskönyveket. Ezen értékelések alapján ajánl a rendszer további
műveket a felhasználónak. 

### Feladat
Implementáljon tetszőleges ajánló algoritmust Java vagy Python nyelven! Az
algoritmus tetszőlegesen megválasztható, kódot viszont nem emelhet át külső
forrásból. Ez alól kivétel Java esetén az Apache Commons könyvtár
(http://commons.apache.org ), amely tehát használható.

### Java
A megoldás tartalmazzon egy Main osztályt, ezen belül pedig egy main() függvényt. A
bemeneti mátrixot a standard inputon várja, a kimenetet a standard outputra írja. A
program forráskódját zip fájlba tömörítve töltse fel a HF portálon 

### Bemenet
A bemenet első sora az ismert értékelések, a felhasználók és a hangoskönyvek számát
tartalmazza. Ezután minden sorban egy felhasználó-azonosító, egy hangoskönyvazonosító és a megfelelő értékelés áll. A mezők tabulátorral vannak elválasztva. Példa:  
60000 500 200  
0 3 4  
0 31 3  
0 87 2  
...  
499 192 5  
499 198 4  

### Kimenet

A kimenet minden felhasználóhoz tartalmazza a legjobb 10 ajánlott hangoskönyv
azonosítóját, tabulátorral elválasztva.
Az azonosítók úgy jelenjenek meg, hogy a leginkább ajánlott hangoskönyv kerüljön a
lista elejére, de ne szerepeljen olyan mű, amelynek értékelése ismert! A sorok a
felhasználók azonosítója szerinti sorrendben kövessék egymást. Az azonosítót nem kell
megjeleníteni.
Az előbbi példa esetében a kimenet 500 sort tartalmaz:  
175 21 76 77 119 2 40 42 56 117  
32 142 38 111 14 18 101 138 64 29  
75 88 47 43 18 150 83 124 166 182  



