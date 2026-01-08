create table public.users
(
    age        integer,
    created    timestamp(6),
    id         bigint not null
        primary key,
    updated    timestamp(6),
    email      varchar(255),
    first_name varchar(255),
    last_name  varchar(255)
);

alter table public.users
    owner to postgres;

