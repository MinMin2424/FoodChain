# Systémové požadavky - FoodChain
## Funkční požadavky
BRQ1: Hlavní entity a jejich interakce

•	SRQ1: Systém pracuje s několika hlavními entitami:

1. Zemědělec/Farmář: Pěstuje plodiny nebo chová zvířata a dodává suroviny do systému.

2. Zpracovatel: Přijímá suroviny od zemědělců a zpracovává je do podoby vhodné pro další distribuci nebo prodej.

3. Sklad: Slouží k uskladnění potravin při specifických teplotních podmínkách.

4. Prodejce: Prodává zpracované potraviny koncovým zákazníkům.

5. Distribuce: Zajišťuje přepravu potravin mezi různými entitami v systému.

6. Zákazník: Nakupuje potraviny od prodejců.

7. Potravina: Produkt, který prochází systémem od zemědělce až po zákazníka.

•	SRQ2: Systém umožňuje jednotlivým stranám předávat potraviny mezi sebou.

•	SRQ3: Každá strana zapojená do systému platí za operace s potravinami pomocí peněz. Tento proces zahrnuje nákup, prodej, zpracování, vrácení a další operace spojené s potravinami.

•	SRQ4: Každá operace s potravinou má několik parametrů, které je třeba evidovat. Mezi tyto parametry patří délka skladování, teplota, cena, datum provádění a další.

•	SRQ5: Systém eviduje každou operaci s potravinou jako transakci. Tato transakce obsahuje všechny důležité parametry operace, identifikaci strany, která operaci provedla, a informace o místě, odkud a kam byla operace provedena.

BRQ2: Blockchain platforma pro evidenci transakcí

•	SRQ6: Systém je postaven na zjednodušené blockchain platformě, která slouží k evidenci transakcí.

•	SRQ7: Každá transakce v rámci systému odkazuje na předchozí transakci, čímž se vytváří řetězec transakcí.

•	SRQ8: Provedené transakce a jejich parametry nelze zpětně upravovat.

BRQ3: Transparentní sledování životního cyklu potraviny

•	SRQ9: Systém umožňuje sledovat historii každé potraviny. Je možné zjistit, přes jaké strany potravina prošla a jaké operace byly s potravinou provedeny.

•	SRQ10: Systém generuje reporty o historii potraviny ve formátu textového souboru. Tyto reporty obsahují kompletní historii potraviny, včetně všech operací a parametrů.

BRQ4: Kanály pro sdílení a výměnu informací

•	SRQ11: Systém umožňuje vytvářet kanály spojující různé strany a operace.

•	SRQ12: Každý kanál má svůj vlastní řetězec událostí, který zaznamenává všechny operace a interakce v rámci daného kanálu.

•	SRQ13: Operace a strany jsou definované na základě účasti v konkrétních kanálech.

•	SRQ14: Do kanálů lze zasílat požadavky, například poptávky po potravinách. Strany mohou využívat kanály k zasílání a přijímání různých požadavků.

•	SRQ15: Strany se mohou registrovat a odregistrovat z kanálů nebo typů požadavků v rámci kanálů.

BRQ5: Detekce bezpečnostních problémů

•	SRQ16: Systém detekuje pokus o tzv. double spending, například pokus o prodej stejné potraviny dvakrát.

•	SRQ17: Systém detekuje pokusy o zpětnou změnu záznamů v řetězci událostí.

BRQ6: Zpracování potravin

•	SRQ18: Systém umožňuje modelovat zpracování potravin jako stavový automat. Potravina prochází různými stavy, které jsou definované předem. Stavy potravin zahrnují:

1. Neprodané: Potravina je připravena k prodeji, ale ještě nebyla nabídnuta k prodeji nebo převedena do další fáze.

2. Na prodej: Potravina je aktuálně k dispozici k prodeji. Tento stav znamená, že potravina je nabídnuta zákazníkům k nákupu.

3. Prodáno: Potravina byla zakoupena zákazníkem. Tento stav zabraňuje tomu, aby byla stejná potravina prodána vícekrát různým zákazníkům, což eliminuje možnost tzv. double spending.

•	SRQ19: Mezi jednotlivými diskrétními kroky simulace může dojít pouze k jednomu přechodu mezi stavy.

BRQ7: Generování reportů

•	SRQ20: Systém umožňuje generovat následující reporty:

o	Parties report: Obsahuje informace o stranách podílejících se na procesování potravin, včetně délky zpracování.

o	Food chain report: Zobrazuje historii potraviny, včetně provedených operací a parametrů. Každá transakce obsahuje:

1. Informace o produktu, na který se transakce vztahuje.

2. Typ provedené operace, například zpracování, prodej nebo vrácení.

3. Osobu, která operaci provedla.

4. Osobu, která produkt obdržela.

5. Místo, odkud byl produkt přemístěn.

6. Místo, kam byl produkt přemístěn.

7. Datum provedení transakce.

8. Cena operace.

9. Odkaz na předchozí transakci v řetězci.

o	Security report: Identifikuje pokusy o podvržení původu potravin a pokusy o double spending. Každá transakce obsahuje:

1. Informace o produktu, na který se transakce vztahuje.

2. Typ provedené operace.

4. Osobu, která se pokusila o nákup.

5. Původního vlastníka produktu.

6. Počet pokusů o provedení operace.

7. Data jednotlivých transakcí.

o	Transaction report: Shrnuje transakce provedené během každého diskrétního kroku ekosystému, včetně stavu peněz a potravin u jednotlivých stran. Každá transakce obsahuje:

1. Informace o produktu, na který se transakce vztahuje.

2. Typ provedené operace.

3. Cena produktu.

4. Osobu, která operaci provedla.

5. Osobu, která produkt obdržela.

6. Datum provedení transakce.

7. Stav peněženky osoby před a po provedení transakce (odesílatel).

8. Stav peněženky osoby před a po provedení transakce (příjemce).

## Nefunkční požadavky

•	NRQ1: Systém nevyžaduje autentizaci ani autorizaci.

•	NRQ2: Aplikace neobsahuje GUI a komunikuje pouze prostřednictvím příkazového řádku nebo textových výstupů do souboru.

•	NRQ3: Aplikace není distribuovaná, běží v jedné JVM a nevyužívá multithreading.

•	NRQ4: Veškeré proměnné a metody, které nemají být přístupné ostatním třídám, jsou skryté.

•	NRQ5: PKI infrastruktura je simulována abstrakcí, například pomocí flagů označujících podepsaný obsah.

•	NRQ6: Reporty jsou generovány jako textové soubory.

•	NRQ7: Konfigurace systému může být zadána přímo v kódu nebo externím souborem, například JSON.

# Použité design patterny

Factory method

State

Strategy

Observer

Decorator

Template method

Adapter

