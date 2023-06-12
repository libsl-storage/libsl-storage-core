create table directory (
    id        bigserial primary key,
    name      varchar(255)                                     not null,
    owner_id  bigint references account (id) on delete cascade not null,
    parent_id bigint references directory (id) on delete cascade
);
