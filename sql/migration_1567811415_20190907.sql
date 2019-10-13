SET CLIENT_MIN_MESSAGES = WARNING;BEGIN;DROP TABLE IF EXISTS public.teacher_meta;CREATE TABLE public.teacher_meta(    id           serial,    user_id      int                  NOT NULL REFERENCES "user" (id),    intro        text,    country_code character varying(2) NOT NULL,    avatar       text,    created_at   timestamp without time zone DEFAULT now(),    updated_at   timestamp without time zone DEFAULT now(),    CONSTRAINT teacher_meta_pkey PRIMARY KEY (id),    CONSTRAINT teacher_meta_ukey UNIQUE (user_id));COMMIT;