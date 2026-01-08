create table public.promo
(
    id        serial
        primary key,
    code      varchar(255)     not null
        unique,
    discount  double precision not null,
    is_active boolean default true
);

alter table public.promo
    owner to postgres;

