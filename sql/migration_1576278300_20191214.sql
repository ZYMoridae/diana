SET CLIENT_MIN_MESSAGES = WARNING;BEGIN;alter table public.order_item    add column sku_code varchar(200);alter table public.cart_item    add column sku_code varchar(200);DROP TABLE IF EXISTS public.user_log;CREATE TABLE public.user_log(    id         serial,    action     text not null,    user_id    int NOT NULL,    ip_addr    character varying(50) NOT NULL,    created_at timestamp without time zone DEFAULT now(),    updated_at timestamp without time zone DEFAULT now(),    CONSTRAINT user_log_pkey PRIMARY KEY (id));INSERT INTO public.order_status(id, "name", created_at, updated_at)VALUES(4, 'token_expired', '2019-04-03 19:00:03.088', '2019-04-03 19:00:03.088');DROP TABLE IF EXISTS public.currency_rates;CREATE TABLE public.currency_rates(    id         serial,    currency_code   character varying(5) NOT NULL,    base_currency   character varying(5) NOT NULL,    rate    double precision NOT NULL,    created_at timestamp without time zone DEFAULT now(),    updated_at timestamp without time zone DEFAULT now(),    CONSTRAINT currency_rates_pkey PRIMARY KEY (id),    CONSTRAINT currency_code_base_currency_ukey UNIQUE (currency_code, base_currency));DROP TABLE IF EXISTS public.class;CREATE TABLE public.class(    id      serial,    name    text not null,    description text not null,    unit    text not null,    unit_price  double precision not null,    created_at timestamp without time zone DEFAULT now(),    updated_at timestamp without time zone DEFAULT now(),    teacher_id int NOT NULL REFERENCES "user"(id),    CONSTRAINT class_pkey PRIMARY KEY (id));DROP TABLE IF EXISTS public.teacher_available_time;CREATE TABLE public.teacher_available_time(    id      serial,    teacher_id int NOT NULL REFERENCES "user"(id),    start_time timestamp without time zone DEFAULT now(),    end_time timestamp without time zone DEFAULT now(),    created_at timestamp without time zone DEFAULT now(),    updated_at timestamp without time zone DEFAULT now(),    is_reserved boolean default false,    CONSTRAINT teacher_available_time_pkey PRIMARY KEY (id));DROP TABLE IF EXISTS public.class_order;CREATE TABLE public.class_order(    id  serial,    user_id int NOT NULL REFERENCES "user"(id),    total_price double precision NOT NULL,    created_at timestamp without time zone DEFAULT now(),    updated_at timestamp without time zone DEFAULT now(),    status_id int NOT NULL REFERENCES "order_status"(id),    CONSTRAINT class_order_pkey PRIMARY KEY (id));DROP TABLE IF EXISTS public.class_order_item;CREATE TABLE public.clazz_order_item(    id  serial,    class_order_id int NOT NULL REFERENCES "class_order_id"(id),    class_id int NOT NULL REFERENCES "class"(id),    teacher_available_time_id int NOT NULL REFERENCES "teacher_available_time"(id),    price double precision NOT NULL,    created_at timestamp without time zone DEFAULT now(),    updated_at timestamp without time zone DEFAULT now(),    CONSTRAINT class_order_item_pkey PRIMARY KEY (id));COMMIT;