keuzes, dingen die we nog moeten onthouden

bij een stop, kiezen we ervoor om het ram terug in te vullen met paginas van de reeds aanwezige processen. als we bijvoorbeeld 4 processen in het ram hebben. dan heeft elke pagina 3 frames. en er 1 geterminate wordt, dan zullen de 3 overige processen elk 1 pagina terug inladen. We kiezen ervoor om de pagina te nemen die het recentst gebruikt wordt. Dit wil zeggen: de pagina, niet in ram aanwezig, met de hoogste recent gebruikt parameter
	
Bij het toevoegen van een process verliezen de reeds aanwezige processen enkele frames. De frames met de laagste recently used time worden gebruikt. Als een process 2 frames moet afstaan, en hij 4 frames heeft die dezelfde LRU tijd hebben, dan zal ons programma telkens dezelfde frames kiezen. Dit is een tekortkoming van ons programma.

bij read moet er nog gezorgd worden dat er eerst gecheckt wordt als het proces al in ram aanwezig is. Dit is nog niet gebeurd en is ook enkel belangrijk voor de xml met 20 processen.