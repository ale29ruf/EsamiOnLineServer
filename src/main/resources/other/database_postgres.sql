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

CREATE SEQUENCE risposte_seq;

CREATE TABLE "risposte" (
                            id INTEGER DEFAULT NEXTVAL ('risposte_seq') PRIMARY KEY,
                            idDomanda INTEGER,
                            risposta INTEGER,
                            FOREIGN KEY (idDomanda) REFERENCES domande (id)

);