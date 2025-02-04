# Popis aplikace

Projekt "Food Chain" se zaměřuje na implementaci systému pro sledování toku potravin pomocí zjednodušené implementace blockchain technologie. Systém zajišťuje, že lze sledovat, jak potraviny procházejí různými fázemi procesu, od jejich vypěstování až po jejich prodej zákazníkovi. Tento proces zahrnuje všechny fáze, včetně zpracování, skladování, distribuce a dalších operací.

Hlavními entitami v systému jsou: farmář, zpracovatel, sklad, prodejce, distribuce, zákazník a optravina. Každá entita v systému provádí specifické operace s potravinami. Ne všechny operace mohou provádět všechny entity. Například farmář může skladovat, vytvářet, zpracovávat a odstraňovat produkty ze skladu, ale nemůže vracet produkty, nakupovat je nebo je distribuovat. Zpracovat může provádět všechny operace kromě distribuce, což platí i pro prodejce. Zákazník má možnost pouze nakupovat nebo vracet produkty.

Systém detekuje potenciální problémy jakou double spending (pokud by byla ta samá potravina prodána vícekrát) a pokusy o zpětnou změnu v již provedených transakcích.

Systém rovněž generuje různé reporty, mezi které patří:

   - Parties report - uvádí jak dlouho a komu byl produkt skladován nebo držen
   - Food chain report - popisuje, co se stalo s produktem, například zda byl vytvořen, skladován, apod., a kdo tyto operace vykonal
   - Security report - zaměřuje se především na detekci double spending a pokusy o zpětné změny v provedených transakcích
   - Money report - uvádí všechny transakce týkající se nákupu produktu, včetně ceny produktu, a kolik má každá strana před a po nákupu

Popis všech funkcí a operací lze najít přímo v kódu jako komentáře.
Pro podrobnější popis je možné se také podívat do souboru Systémové požadavky.