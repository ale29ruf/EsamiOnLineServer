CREATE SEQUENCE appelli_seq;

CREATE TABLE "appelli" (
                           id INTEGER DEFAULT NEXTVAL ('appelli_seq') PRIMARY KEY,
                           data VARCHAR(20),
                           ora VARCHAR(20),
                           durata VARCHAR(90)
);

CREATE SEQUENCE domande_seq;

CREATE TABLE "domande" (
                           id INTEGER DEFAULT NEXTVAL ('domande_seq') PRIMARY KEY,
                           testo VARCHAR(50),
                           appello INTEGER,
                           FOREIGN KEY (appello) REFERENCES appelli (id)
);

CREATE SEQUENCE scelte_seq;

CREATE TABLE "scelta" (
                           id INTEGER DEFAULT NEXTVAL ('scelte_seq') PRIMARY KEY,
                           testo VARCHAR(50),
                           domanda INTEGER,
                           FOREIGN KEY (domanda) REFERENCES domande (id)
);

CREATE SEQUENCE risposte_seq;

CREATE TABLE "risposte" (
                            id INTEGER DEFAULT NEXTVAL ('risposte_seq') PRIMARY KEY,
                            idDomanda INTEGER,
                            risposta INTEGER,
                            FOREIGN KEY (idDomanda) REFERENCES domande (id)

);


CREATE SEQUENCE studenti_seq;
CREATE TABLE "studenti" (
                            id INTEGER DEFAULT NEXTVAL ('studenti_seq') PRIMARY KEY,
                            matricola VARCHAR(10),
                            codFiscale VARCHAR(20),
                            codiceAppello VARCHAR(36),
                            idAppello INTEGER,
                            FOREIGN KEY (idAppello) REFERENCES Appelli (id)

);