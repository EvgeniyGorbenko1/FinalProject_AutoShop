create table public.security
(
    is_enabled boolean,
    id         bigint not null
        primary key,
    user_id    bigint not null
        unique
        constraint fkcbur1y7y3x7khvxpg3jumbnca
            references public.users,
    password   varchar(255),
    role       varchar(255)
        constraint security_role_check
            check ((role)::text = ANY
                   ((ARRAY ['USER'::character varying, 'ADMIN'::character varying, 'MODERATOR'::character varying])::text[])),
    username   varchar(255)
        unique
);

alter table public.security
    owner to postgres;

