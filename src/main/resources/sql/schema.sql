DROP TABLE IF EXISTS public.playlist_chanson;
DROP TABLE IF EXISTS public.chanson;
DROP TABLE IF EXISTS public.playlist;
DROP TABLE IF EXISTS public.audit_journalier;

CREATE EXTENSION IF NOT EXISTS "pgcrypto";


CREATE TYPE GENRE as ENUM (
                                'COUNTRY_FOLK',
                                'DIVERS_VARIETE',
                                'HIP_HOP_RAP',
                                'JAZZ_BLUES',
                                'MUSIQUES_DU_MONDE_REGGAE',
                                'POP',
                                'RB_SOUL',
                                'ROCK',
                                'ELECTRO_DANCE'
);

CREATE TABLE public.chanson (
                                id VARCHAR(50) PRIMARY KEY ,
                                titre VARCHAR(50) NOT NULL DEFAULT '',
                                artiste VARCHAR(100) NOT NULL DEFAULT '',
                                album VARCHAR(100) NOT NULL DEFAULT '',
                                genre GENRE,
                                label VARCHAR(100) NOT NULL DEFAULT '' ,
                                anneeSortie INTEGER NOT NULL DEFAULT 0,
                                duree INTEGER NOT NULL,
                                nbrEcoute INTEGER NOT NULL DEFAULT 0,
                                dansabilitee REAL NOT NULL DEFAULT 0,
                                imageurl TEXT DEFAULT ''
);

CREATE TABLE public.playlist (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                nom VARCHAR(50) NOT NULL DEFAULT '',
                                dateCreation Date DEFAULT CURRENT_DATE
);

CREATE TABLE public.playlist_chanson (
                                 id_chanson VARCHAR(50) REFERENCES public.chanson(id),
                                 id_playlist UUID REFERENCES public.playlist(id) ON DELETE CASCADE,
                                 position INTEGER NOT NULL,
                                 PRIMARY KEY  (id_playlist, id_chanson),
                                 UNIQUE (id_playlist, position) DEFERRABLE INITIALLY IMMEDIATE
);

CREATE TABLE audit_journalier (
                                  id SERIAL PRIMARY KEY,
                                  id_chanson VARCHAR(50) REFERENCES chanson(id),
                                  date_lecture DATE NOT NULL DEFAULT CURRENT_DATE
);
