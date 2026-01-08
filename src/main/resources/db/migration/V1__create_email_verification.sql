create table public.email_verification
(
    id          integer      not null
        primary key,
    expiry_date timestamp(6) not null,
    security_id bigint
        unique
        constraint fkn9i3alcq60xp6akbcov4vs5j3
            references public.security,
    token       varchar(255) not null
        unique
);

alter table public.email_verification
    owner to postgres;

