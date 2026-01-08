create table public.catalog
(
    id          bigint not null
        primary key,
    description varchar(255),
    image       varchar(255),
    name        varchar(255)
);

alter table public.catalog
    owner to postgres;

