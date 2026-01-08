create table public.spare_parts
(
    price          numeric(38, 2),
    stock          integer,
    catalog_id     bigint
        constraint fkgeor80sgg25wj62rgr2qgj6lu
            references public.catalog,
    id             bigint not null
        primary key,
    category       varchar(255)
        constraint spare_parts_category_check
            check ((category)::text = ANY
        ((ARRAY ['ORIGINAL_PARTS'::character varying, 'OIL_PARTS'::character varying, 'TYRES'::character varying])::text[])),
    description    varchar(255),
    image          varchar(255),
    name           varchar(255),
    specifications jsonb
);

alter table public.spare_parts
    owner to postgres;

