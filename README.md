# Breadth-First Search Algorithm Visualization

Programma scritto in Java per la visualizzazione dell'algoritmo di ricerca in ampiezza sui grafi. È possibile disegnare dei grafi **a piacimento**, oppure crearne alcuni in **modo casuale** e vedere come viene eseguito l'algoritmo [Breadth-First Search](https://it.wikipedia.org/wiki/Ricerca_in_ampiezza) sui grafi. È possibile anche **salvare** i grafi creati in un file *json*, per poi caricarli in un secondo momento. Alcuni esempi di file *json* già pronti da utilizzare possono essere trovati in `/examples/`.

Esempio di esecuzione del programma:

![Animation gif](https://media.giphy.com/media/l4FGnSnmAUO50mbUk/giphy.gif)


## Funzionalità

- [x] Animazione dell'algoritmo BFS
- [x] Creazione di un grafo in modo **visuale**
- [x] Creazione di un grafo **casuale**
- [ ] **Salvataggio** di un grafo in formato *json*
- [x] **Caricamento** di un grafo da un file *json*
- [x] Modalità di esecuzione **automatica** e **passo-passo**
- [x] Visualizzazione delle liste di *padri* ed *elementi visitati*
- [x] **[shortcut](https://github.com/steppp/Breadth-First-Search/blob/master/README.md#shortcuts)** da tastiera


## Come utilizzare il programma

1. Avviare il programma tramite bfs-alga.jar;
2. creare un grafo in uno dei seguenti modi:
    * creando i vertici e gli archi con il mouse,
    * andando su **File > Casuale..** per generarne uno in modo casuale,
    * andando su **File > Apri..** per aprirne uno da file;
3. modificare il grafo finché non si ottiene il risultato desiderato;
4. cliccare su **Animazione > Impostazioni** e:
   1. settare il nodo radice,
   2. impostare la velocità dell'animazione (valida solamente per l'animazione automatica);
5. sempre dal menu **Animazione** scegliere **Avvia..** e poi:
    * **Avvia** se si vuole assistere ad un'animazione automatica,
    * **Passo-passo** se si vuole controllare manualmente l'animazione;
6. si sbloccheranno altre due voci nel menu **Animazione**:
    * **Stop** per terminare l'animazione,
    * **Prossimo passo** per andare avanti di uno step;
7. sulla destra è possibile controllare il vettore dei **padri** e dei **nodi visitati** che vengono aggiornati ad ogni passo dell'animazione ed in cui il nodo radice è evidenziato con un colore arancione;
8. una volta terminata l'animazione è possibile ripeterla, sia con un grafo diverso che con lo stesso.


## Come funziona l'animazione

L'animazione sfrutta i meccanismi di *Threading* per far animare i componenti sullo schermo. In particolare, è stata creata una sottoclasse di **Thread** che prende il nome di **GraphVisiter**. Ogni volta che si avvia un'animazione, viene quindi *creato un nuovo thread* che si occupa di eseguire l'algoritmo BFS (ciò non significa che ci sia uno spreco di memoria: quando l'animazione termina o viene interrotta, il thread viene **eliminato**). Dopo aver impostato il grafo su cui deve essere eseguita l'animazione ed il nodo radice, è possibile far partire l'animazione. L'algoritmo si ferma in certi punti rilevanti nella sua esecuzione chiamando su se stesso il metodo **wait()**, che quindi lo pone in uno stato di attesa finché non verrà ripreso da qualche altro thread. Il thread principale riprende la sua esecuzione ed è proprio questo che si occuperà di riprendere l'esecuzione dell'animazione. In particolare, in questo momento è possibile trovarsi in uno di questi due casi:
1. l'esecuzione è stata impostata come **automatica** (l'utente ha fatto partire l'animazione dal pulsante *Avvia*);
2. l'esecuzione è stata impostata su **passo-passo** (l'utente ha fatto partire l'animazione dal pulsante *Passo-passo*).

Nel primo caso, l'esecuzione del thread secondario deve essere ripresa ad intervalli regolari secondo quanto impostato nelle preferenze dell'animazione. Per fare ciò, prima di far partire il thread dell'animazione è stato creato un **Timer** che scatta ad intervalli regolari. Ad ogni scatto, se l'animazione non è terminata, viene chiamato il metodo **notify()** sull'istanza del thread secondario, riprendendo la sua esecuzione. Nel secondo caso invece l'esecuzione del thread relativo all'animazione non viene ripreso finché non viene premuto il bottone **Passo successivo** o attivata la relativa shortcut.

In certi punti dell'algoritmo inoltre vengono effettuate delle chiamate a delle funzioni che sono impostate come dei parametri della classe **GraphVisiter** allo scopo di aggiornare l'interfaccia utente. Tali funzioni vengono impostate tramite i corrispondenti *setter* utilizzando delle **espressioni lambda** od il **double colon operator** (disponibili solamente da *Java 8*).


### Shortcuts

`Ctrl + R`: avvia l'animazione in modalità **automatica**

`Ctrl + S`: avvia l'animazione in modalità **passo-passo**

`Ctrl + N`: effettua il prossimo passo

`Ctrl + P`: apri il pannello di preferenze dell'animazione

`Ctrl + X`: termina l'animazione


#### Creato da:
*  [Melania Ghelli](https://github.com/melastone)
*  [Stefano Andriolo](https://github.com/steppp)
